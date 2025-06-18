package kevin.event.impl;

import kevin.event.struct.Event;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class BlockBBEvent extends Event {
    @NotNull
    private final Block block;
    private final int x;
    private final int y;
    private final int z;
    @Nullable
    private AxisAlignedBB boundingBox;

    public BlockBBEvent(@NotNull BlockPos blockPos, @NotNull Block block, @Nullable AxisAlignedBB boundingBox) {
        super();
        this.block = block;
        this.boundingBox = boundingBox;
        this.x = blockPos.getX();
        this.y = blockPos.getY();
        this.z = blockPos.getZ();
    }

    @NotNull
    public Block getBlock() {
        return this.block;
    }

    @Nullable
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(@Nullable AxisAlignedBB var1) {
        this.boundingBox = var1;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}
