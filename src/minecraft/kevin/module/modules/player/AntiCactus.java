/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kevin.module.modules.player;

import kevin.event.impl.BlockBBEvent;
import kevin.event.EventTarget;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import net.minecraft.block.BlockCactus;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;


public final class AntiCactus extends ClientModule {
    public AntiCactus() {
        super("AntiCactus", "Prevents cactuses from damaging you.", ModuleCategory.PLAYER);
    }

    @EventTarget
    public void onBlockBB(@NotNull BlockBBEvent event) {
        if (event.getBlock() instanceof BlockCactus) {
            event.setBoundingBox(new AxisAlignedBB(event.getX(), event.getY(), event.getZ(), (double)event.getX() + (double)1.0F, (double)event.getY() + (double)1.0F, (double)event.getZ() + (double)1.0F));
        }
    }
}
