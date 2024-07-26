package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import net.minecraft.block.BlockStairs
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventRender3D
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.features.settings.impl.NumberSetting
import cn.cutemic.konata.utils.render.Render3DUtils
import cn.cutemic.konata.interfaces.ProviderManager
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.wrapper.blockpos.WrapperBlockPos
import cn.cutemic.konata.wrapper.util.WrapperAxisAlignedBB
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityGiantZombie
import net.minecraft.entity.monster.EntityZombie
import java.awt.Color

class SmartWeapon : Module("SmartWeapon", Category.Zombies) {
    private var fill = BooleanSetting("Fill", true)
    private var outline = BooleanSetting("Outline", true)
    private var sendMessage = BooleanSetting("SendMessage", false)
    private var width = NumberSetting("Width", 1, 0.1, 10, 0.1) { outline.value }
    private var color1 = ColorSetting("FillColor", Color(255, 255, 255, 50)) { fill.value }
    private var color2 = ColorSetting("OutlineColor", Color(255, 255, 255, 255)) { outline.value }

    init {
        addSettings(fill, color1, outline, width, color2, sendMessage)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    @Subscribe
    fun onRender3D(e: EventRender3D) {
        if (mc.theWorld.loadedEntityList != null){
            mc.theWorld.loadedEntityList.stream()
                .filter { it.isDead }
                .filter { it is EntityZombie }
                .filter { (it as EntityZombie).isChild }
                .forEach {

                    if (sendMessage.value){
                        Utility.sendClientMessage(Konata.i18n["theoldonecheck.message"])
                    }

                    val x = it.posX - mc.renderManager.viewerPosX
                    val y = it.posY - mc.renderManager.viewerPosY
                    val z = it.posZ - mc.renderManager.viewerPosZ
                    GL11.glPushMatrix()
                    GlStateManager.enableAlpha()
                    GlStateManager.enableBlend()
                    GL11.glBlendFunc(770, 771)
                    GL11.glDisable(3553)
                    GL11.glEnable(2848)
                    GL11.glDepthMask(false)
                    val minX: Double = it.entityBoundingBox.minX
                    val minY: Double = it.entityBoundingBox.minY
                    val minZ: Double = it.entityBoundingBox.minZ
                    val maxX: Double = it.entityBoundingBox.maxX
                    val maxY: Double = it.entityBoundingBox.maxY
                    val maxZ: Double = it.entityBoundingBox.maxZ
                    it.entityBoundingBox
                    if (fill.value) {
                        val color = color1.value.color
                        GL11.glPushMatrix()
                        GlStateManager.color(
                            color.red / 255.0f,
                            color.green / 255.0f,
                            color.blue / 255.0f,
                            color.alpha / 255.0f
                        )
                        Render3DUtils.drawBoundingBox(
                            WrapperAxisAlignedBB(
                                x + minX - 0.01,
                                y + minY - 0.01,
                                z + minZ - 0.01,
                                x + maxX,
                                y + maxY,
                                z + maxZ
                            )
                        )
                        GL11.glPopMatrix()
                    }
                    if (outline.value) {
                        val color = color2.value.color
                        GL11.glPushMatrix()
                        GlStateManager.color(
                            color.red / 255.0f,
                            color.green / 255.0f,
                            color.blue / 255.0f,
                            color.alpha / 255.0f
                        )
                        GL11.glLineWidth(width.value.toFloat())
                        Render3DUtils.drawBoundingBoxOutline(
                            WrapperAxisAlignedBB(
                                x + minX - 0.005,
                                y + minY - 0.005,
                                z + minZ - 0.005,
                                x + maxX,
                                y + maxY,
                                z + maxZ
                            )
                        )
                        GL11.glPopMatrix()
                    }
                    GL11.glDisable(2848)
                    GL11.glEnable(3553)
//                if (throughBlock.value) {
//                    GL11.glEnable(2929)
//                }
                    GL11.glDepthMask(true)
                    GL11.glLineWidth(1.0f)
                    GL11.glPopMatrix()
                }
        }
    }

    companion object {
        var using = false
    }
}
