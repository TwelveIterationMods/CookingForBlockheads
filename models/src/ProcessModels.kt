import com.google.common.collect.ArrayListMultimap
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.stream.JsonWriter
import java.io.File

fun main(args: Array<String>) {
    val modid = "cookingforblockheads"

    val inputDir = File("./models/resources/")
    val outputDir = File("./src/main/resources/assets/$modid/models/block/")
    fixTextures(inputDir, modid)
    generateModels(inputDir, outputDir)
}

fun fixTextures(inputDir: File, modid: String) {
    val gson = Gson()
    inputDir.walkTopDown().forEach { file ->
        if (file.isFile && file.name.endsWith(".json")) {
            val json = gson.fromJson(file.reader(), JsonObject::class.java)
            val textures = json.getAsJsonObject("textures");
            textures.entrySet().forEach { entry ->
                if (!entry.value.asString.startsWith(modid + ":")) {
                    textures.addProperty(entry.key, modid + ":" + entry.value.asString);
                }
            }
            JsonWriter(file.writer()).use { out ->
                gson.toJson(json, out);
            };
        }
    }
}

fun generateModels(inputDir: File, outputDir: File) {
    outputDir.deleteRecursively()
    outputDir.mkdirs()

    val gson = Gson()
    inputDir.walkTopDown().forEach { file ->
        if (file.isFile) {
            if(file.name.endsWith("_full.json")) {
                val jsonObject = gson.fromJson(file.reader(), JsonObject::class.java)
                val modelParts = ArrayListMultimap.create<String, JsonObject>()
                jsonObject.getAsJsonArray("elements").forEach { element ->
                    val obj = element as JsonObject
                    if (obj.has("__comment")) {
                        val partName = obj.get("__comment").asString
                        val idx = partName.indexOf('_')
                        if (idx != -1) {
                            modelParts.put(partName.substring(0, idx), obj)
                        }
                    }
                }

                modelParts.keys().forEach { subName ->
                    val newObject = JsonObject()
                    newObject.addProperty("__comment", jsonObject.get("__comment").asString + " (Sub Model: " + subName + ")")
                    newObject.add("textures", jsonObject.getAsJsonObject("textures"))
                    val elements = JsonArray()
                    modelParts.get(subName).forEach { subPart ->
                        elements.add(subPart)
                    }
                    newObject.add("elements", elements)
                    JsonWriter(File(outputDir, file.name.substring(0, file.name.length - 10) + subName + ".json").writer()).use { writer ->
                        gson.toJson(newObject, writer);
                    };
                }
            } else if(file.name.endsWith(".json")) {
                file.copyTo(File(outputDir, file.name))
            }
        }
    }
}