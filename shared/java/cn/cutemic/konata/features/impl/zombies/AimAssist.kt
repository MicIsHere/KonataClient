package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventRender3D
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ModeSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.modules.logger.Logger
import net.minecraft.entity.Entity
import net.minecraft.util.MouseHelper
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.Vec3
import net.minecraft.util.Vector3d
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import kotlin.math.*

class AimAssist : Module("AimAssist", Category.Zombies) {
    private val aaMode = ModeSetting("AAMode", 0, "Magnetism", "Friction")
    private val raytraceDistance = NumberSetting("RaytraceDistance", 6.0, 0.0, 100.0, 0.5)
    private val leftCompensation = NumberSetting("LeftCompensation", 0.4, 0.0, 1.0, 0.01)
    private val rightCompensation = NumberSetting("RightCompensation", 0.4, 0.0, 1.0, 0.01)
    private val pitchOptimization = BooleanSetting("PitchOptimization", true)

    private var locked: LockData? = null

    init {
        addSettings(aaMode, raytraceDistance, leftCompensation, rightCompensation, pitchOptimization)
    }

    private fun lockEntity(entity: Entity) {
        locked = LockData(entity, entity.getPositionEyes(0f), mc.thePlayer.getPositionEyes(0f))
    }

    class LockData(
        val entity: Entity,
        val entityPos: Vec3,
        val playerPos: Vec3,
    )

    private val feedbackRate
        get() = if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0)
            leftCompensation.value.toDouble() else rightCompensation.value.toDouble()

    @Subscribe
    fun onRender(event: EventRender3D) {
        val locked = locked
        val result0 = mc.objectMouseOver
        if (result0?.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            val result = result0.entityHit
            if (locked != null && result != null && locked.entity == result) {
                val lastPlayerPos = locked.playerPos
                val lastEntityPos = locked.entityPos
                val preVec = Vec3f(lastEntityPos.subtract(lastPlayerPos))
                val vec = Vec3f(result.getPositionEyes(0f).subtract(mc.thePlayer.getPositionEyes(0f)))
                val angle = vec.angle(preVec)
                val zeroAngle = vec == preVec

                if (!angle.isNaN()) {
                    if (zeroAngle) {
//                        if (zeroAngleFriction) FrictionAA.compensate(sensitivity, true)
                    } else {
                        val facing = Vec3f(mc.thePlayer.rotationYaw.toRadian(), mc.thePlayer.rotationPitch.toRadian())

                        // Clamp range
                        val xzDelta = abs(vec.yaw - preVec.yaw).toDegree()
                        val xzRange = -xzDelta..xzDelta
                        val yDelta = abs(vec.pitch - preVec.pitch).toDegree()
                        val yRange = -yDelta..yDelta

                        // Vertical
                        val vRefPlane = Plane(Vec3f(0f, 1f, 0f), facing)
                        val vPre = vRefPlane.projectOnPlane(preVec)
                        val vNow = vRefPlane.projectOnPlane(vec)
                        val pitchOffset = ((vNow.pitch - vPre.pitch).toDegree() * feedbackRate).toFloat().coerceIn(yRange)
                        if (!pitchOffset.isNaN())
                            mc.thePlayer.rotationPitch = (mc.thePlayer.rotationPitch + pitchOffset).coerceIn(-89.5f..89.5f)

                        // Horizontal
                        if (xzDelta <= 90.0) {
                            val hRefPlane = Plane(Vec3f(1f, 0f, 0f), Vec3f(0f, 0f, 1f))
                            val hPre = hRefPlane.projectOnPlane(preVec)
                            val hNow = hRefPlane.projectOnPlane(vec)
                            val yawOffset = ((hNow.yaw - hPre.yaw).toDegree() * feedbackRate).toFloat().coerceIn(xzRange)
                            if (!yawOffset.isNaN())
                                mc.thePlayer.rotationYaw += yawOffset * if (pitchOptimization.value) cos(
                                    mc.thePlayer.rotationPitch.toRadian()
                            ) else 1f
                        } // Cancel
                    }
                }
            }
            lockEntity(result)
        } else this.locked = null
    }

    private fun Vec3.angle(other: Vec3): Float {
        return acos((this.dotProduct(other)) / (this.lengthVector() * other.lengthVector())).toFloat()
    }

    fun Vec3f(yaw: Float, pitch: Float): Vec3f {
        val x = cos(yaw)
        val z = sin(yaw)
        val y = sqrt(x * x + z * z) * tan(pitch)
        return Vec3f(x, y, z)
    }
}
val PI_FLOAT = 3.1415927f

inline fun Float.toRadian() = this / 180.0f * PI_FLOAT
inline fun Double.toRadian() = this / 180.0 * PI

inline fun Float.toDegree() = this * 180.0f / PI_FLOAT
inline fun Double.toDegree() = this * 180.0 / PI

val Vec3.yaw get() = Vec3f(this).yaw
val Vec3.pitch get() = Vec3f(this).pitch

data class Vec3f(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f) {

    constructor(x: Number, y: Number, z: Number) : this(x.toFloat(), y.toFloat(), z.toFloat())

    constructor(vec3f: Vec3f) : this(vec3f.x, vec3f.y, vec3f.z)

    constructor(vec3: Vec3) : this(vec3.xCoord, vec3.yCoord, vec3.zCoord)

    inline val xInt get() = x.toInt()
    inline val yInt get() = y.toInt()
    inline val zInt get() = z.toInt()
    inline val xLong get() = x.toLong()
    inline val yLong get() = y.toLong()
    inline val zLong get() = z.toLong()
    inline val xDouble get() = x.toDouble()
    inline val yDouble get() = y.toDouble()
    inline val zDouble get() = z.toDouble()
    inline val length get() = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

    inline val yaw get() = atan(z / x)
    inline val pitch get() = atan(y / sqrt(x * x + z * z))
    inline val roll get() = atan(x / y)

    // Divide
    operator fun div(vec3f: Vec3f) = div(vec3f.x, vec3f.y, vec3f.z)

    operator fun div(divider: Float) = div(divider, divider, divider)

    fun div(x: Float, y: Float, z: Float) = Vec3f(this.x / x, this.y / y, this.z / z)

    // Multiply
    operator fun times(vec3f: Vec3f) = times(vec3f.x, vec3f.y, vec3f.z)

    operator fun times(multiplier: Float) = times(multiplier, multiplier, multiplier)

    fun times(x: Float, y: Float, z: Float) = Vec3f(this.x * x, this.y * y, this.z * z)

    // Minus
    operator fun minus(vec3f: Vec3f) = minus(vec3f.x, vec3f.y, vec3f.z)

    operator fun minus(sub: Float) = minus(sub, sub, sub)

    fun minus(x: Float, y: Float, z: Float) = plus(-x, -y, -z)

    // Plus
    operator fun plus(vec3f: Vec3f) = plus(vec3f.x, vec3f.y, vec3f.z)

    operator fun plus(add: Float) = plus(add, add, add)

    fun plus(x: Float, y: Float, z: Float) = Vec3f(this.x + x, this.y + y, this.z + z)

    infix fun dot(target: Vec3f): Float = x * target.x + y * target.y + z * target.z

    infix fun cross(target: Vec3f): Vec3f = Vec3f(
        y * target.z - z * target.y,
        z * target.x - x * target.z,
        x * target.y - y * target.x
    )

    fun normalize(): Vec3f {
        val r = length
        if (r == 0f) return this
        return Vec3f(x / r, y / r, z / r)
    }

    fun angle(other: Vec3f): Float {
        return acos((this dot other) / (length * other.length).toDouble()).toFloat()
    }

    fun toEuler(): Triple<Float, Float, Float> {
        val normalized = normalize()
        val reference = Vec3f(0, 0, 10)
        val cosTheta = normalized.angle(reference)
        val theta = acos(cosTheta)
        val axis = reference cross normalized

        val k = axis.normalize()
        val c = cos(theta / 2)
        val s = sin(theta / 2)
        val w = c
        val x = k.x * s
        val y = k.y * s
        val z = k.z * s

        val t0 = 2.0 * (w * x + y * z)
        val t1 = 1.0 - 2.0 * (x * x + y * y)
        val roll = atan2(t0, t1)

        val t2 = (2.0 * (w * y - z * x)).coerceIn(-1.0..1.0)
        val pitch = asin(t2)

        val t3 = 2.0 * (w * z + x * y)
        val t4 = 1.0 - 2.0 * (y * y + z * z)
        val yaw = atan2(t3, t4)
        return Triple(yaw.toDegree().toFloat(), pitch.toDegree().toFloat(), roll.toDegree().toFloat())
    }

    override fun toString(): String {
        return "Vec3f[${this.x}, ${this.y}, ${this.z}]"
    }

    override fun equals(other: Any?): Boolean {
        return other is Vec3f && other.x == x && other.y == y && other.z == z
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    companion object {
        @JvmField
        val ZERO = Vec3f(0f, 0f, 0f)
    }

}


class Plane(val normal: Vec3f) {

    var valid = true; private set

    constructor(vec1: Vec3f, vec2: Vec3f) : this(vec1 cross vec2) {
        valid = vec1.normalize() != vec2.normalize()
    }

    // Return the projection of the input on the plane
    fun projectOnPlane(vec: Vec3f): Vec3f {
        return if (!valid) vec
        else {
            val num1 = normal dot normal
            val num2 = vec dot normal
            Vec3f(
                vec.x - normal.x * num2 / num1,
                vec.y - normal.y * num2 / num1,
                vec.z - normal.z * num2 / num1
            )
        }
    }

}