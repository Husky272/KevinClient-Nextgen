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
package kevin.event

import kevin.event.struct.CancellableEvent
import kevin.event.struct.Event
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity

class PushOutEvent : CancellableEvent()

class Render2DEvent(var partialTicks: Float) : Event()

class Render3DEvent(var partialTicks: Float) : Event()

class RenderEntityEvent(val entity: Entity, val x: Double, val y: Double, val z: Double, val entityYaw: Float,
                        val partialTicks: Float) : Event()

class ScreenEvent(val guiScreen: GuiScreen?) : kevin.event.struct.Event()


class StepConfirmEvent : kevin.event.struct.Event()

class TextEvent(var text: String?) : kevin.event.struct.Event()

class TickEvent : kevin.event.struct.Event()

class UpdateEvent(/**val eventState: UpdateState**/) : kevin.event.struct.Event()

class WorldEvent(val worldClient: WorldClient?) : kevin.event.struct.Event()

class ClickWindowEvent(val windowId: Int, val slotId: Int, val mouseButtonClicked: Int, val mode: Int) : CancellableEvent()
