package net.blay09.mods.cookingforblockheads.block.entity.util;

public interface TransferableBlockEntity<T> {
    T snapshotDataForTransfer();
    void restoreFromTransferSnapshot(T data);
}
