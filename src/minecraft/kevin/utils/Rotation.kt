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
package kevin.utils

import kevin.event.StrafeEvent
import kevin.utils.entity.rotation.RotationUtils
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MathHelper
import net.minecraft.util.Vec3
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Rotation with vector
 */
public data class VecRotation(val vec: Vec3, val rotation: Rotation)

/**
 * Rotation with place info
 */

public data class PlaceRotation(val placeInfo: PlaceInfo, val rotation: Rotation)