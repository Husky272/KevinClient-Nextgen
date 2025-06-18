package kevin.event.impl;

import kevin.event.struct.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.Nullable;

public final class ClickBlockEvent extends Event {
    @Nullable
    private final BlockPos clickedBlock;
    @Nullable
    private final EnumFacing EnumFacing;

    public ClickBlockEvent(@Nullable BlockPos clickedBlock, @Nullable EnumFacing EnumFacing) {
        this.clickedBlock = clickedBlock;
        this.EnumFacing = EnumFacing;
    }

    @Nullable
    public BlockPos getClickedBlock() {
        return this.clickedBlock;
    }

    @Nullable
    public EnumFacing getEnumFacing() {
        return this.EnumFacing;
    }
}
