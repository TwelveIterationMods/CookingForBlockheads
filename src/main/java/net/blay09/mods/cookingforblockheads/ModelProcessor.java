package net.blay09.mods.cookingforblockheads;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public class ModelProcessor {

	public static void main(String[] args) {
		final String modid = "cookingforblockheads";

		File inputDir = new File("./models/");
		File outputDir = new File("./src/main/resources/assets/" + modid + "/models/block/");
		fixTextures(inputDir, modid);
		generateModels(inputDir, outputDir);
	}

	private static void fixTextures(File inputDir, final String modId) {
		final Gson gson = new Gson();
		try {
			Files.walkFileTree(inputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if(file.toFile().getName().endsWith(".json")) {
						try(FileReader reader = new FileReader(file.toFile())) {
							JsonObject json = gson.fromJson(reader, JsonObject.class);
							JsonObject textures = json.getAsJsonObject("textures");
							for(Map.Entry<String, JsonElement> entry : textures.entrySet()) {
								if (!entry.getValue().getAsString().startsWith(modId + ":")) {
									textures.addProperty(entry.getKey(), modId + ":" + entry.getValue().getAsString());
								}
							}
							try(JsonWriter writer = new JsonWriter(new FileWriter(file.toFile()))) {
								gson.toJson(json, writer);
							}
						}
					}
					return super.visitFile(file, attrs);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateModels(File inputDir, final File outputDir) {
		try {
			FileUtils.deleteDirectory(outputDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//noinspection ResultOfMethodCallIgnored
		outputDir.mkdirs();

		final Gson gson = new Gson();
		try {
			Files.walkFileTree(inputDir.toPath(), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toFile().getName().endsWith("_full.json")) {
						try (FileReader reader = new FileReader(file.toFile())) {
							JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
							ArrayListMultimap<String, JsonObject> modelParts = ArrayListMultimap.create();
							for (JsonElement element : jsonObject.getAsJsonArray("elements")) {
								JsonObject obj = element.getAsJsonObject();
								if (obj.has("__comment")) {
									String partName = obj.get("__comment").getAsString();
									int idx = partName.indexOf('_');
									if (idx != -1) {
										modelParts.put(partName.substring(0, idx), obj);
									}
								}
							}
							for (String subName : modelParts.keySet()) {
								JsonObject newObject = new JsonObject();
								newObject.addProperty("__comment", jsonObject.get("__comment").getAsString() + " (Sub Model: " + subName + ")");
								newObject.add("textures", jsonObject.getAsJsonObject("textures"));
								JsonArray elements = new JsonArray();
								for (JsonElement element : modelParts.get(subName)) {
									elements.add(element);
								}
								newObject.add("elements", elements);
								try (JsonWriter writer = new JsonWriter(new FileWriter(new File(outputDir, file.toFile().getName().substring(0, file.toFile().getName().length() - 10) + subName + ".json")))) {
									gson.toJson(newObject, writer);
								}
							}
						}
					} else if (file.toFile().getName().endsWith(".json")) {
						Files.copy(file, outputDir.toPath().resolve(file.getFileName()));
					}
					return super.visitFile(file, attrs);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
