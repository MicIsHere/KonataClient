package cn.cutemic.konata.features.impl.zombies

import cn.cutemic.konata.Konata
import cn.cutemic.konata.event.Subscribe
import cn.cutemic.konata.event.events.EventRender3D
import cn.cutemic.konata.features.manager.Category
import cn.cutemic.konata.features.manager.Module
import cn.cutemic.konata.features.settings.impl.BooleanSetting
import cn.cutemic.konata.features.settings.impl.ColorSetting
import cn.cutemic.konata.features.settings.impl.ModeSetting
import cn.cutemic.konata.utils.Utility
import cn.cutemic.konata.utils.render.Render3DUtils
import cn.cutemic.konata.wrapper.util.WrapperAxisAlignedBB
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Timer
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.lang.reflect.Field


class TheOldOneCheck : Module("TheOldOneCheck", Category.Zombies) {
    private var sendMessage = BooleanSetting("SendMessage", false)
    private var damageRender = BooleanSetting("DamageRender", false)
    private var mode = ModeSetting("RenderMode", 0,"1", "2", "3", "4")
    private var color = ColorSetting("Color", Color(255, 255, 255, 50))

    private var sended = false

    init {
        addSettings(color,sendMessage, damageRender, mode)
    }

    override fun onEnable() {
        super.onEnable()
        using = true
    }

    override fun onDisable() {
        super.onDisable()
        using = false
    }

    private fun getTick(): Timer? {
        try {
            val client = Minecraft::class.java
            val f: Field = client.getDeclaredField(String(charArrayOf('t', 'i', 'm', 'e', 'r')))
            f.setAccessible(true)
            return f.get(mc) as Timer
        } catch (er: Exception) {
            try {
                val c2 = Minecraft::class.java
                val f2: Field = c2.getDeclaredField(
                    String(
                        charArrayOf(
                            'f',
                            'i',
                            'e',
                            'l',
                            'd',
                            '_',
                            '7',
                            '1',
                            '4',
                            '2',
                            '8',
                            '_',
                            'T'
                        )
                    )
                )
                f2.setAccessible(true)
                return f2.get(mc) as Timer
            } catch (er2: Exception) {
                return null
            }
        }
    }

    @Subscribe
    fun onRender3D(e: EventRender3D) {
        if (mc.theWorld.loadedEntityList != null){
            mc.theWorld.loadedEntityList.stream()
                .filter { !it.isDead } // 过滤未死亡的实体
                .filter { it is EntityZombie && it.isChild }
                .forEach {

                    if ((it as EntityZombie).heldItem.item != Items.diamond_sword){
                        return@forEach
                    }

                    if (it.getEquipmentInSlot(4).item != Items.skull){
                        return@forEach
                    }

                    if (sendMessage.value && !sended){
                        sended = true
                        Utility.sendClientMessage(Konata.i18n["theoldonecheck.message"])
                    }

                    renderEntity(it, color.color.rgb, damageRender.value, mode.mode)
                }

            sended = false
        }
    }

    private fun renderEntity(e: Entity, color: Int, damage: Boolean, type: Int) {
        var color = color
        val x: Double =
            e.lastTickPosX + (e.posX - e.lastTickPosX) * getTick()!!.renderPartialTicks - mc.renderManager.viewerPosX
        val y: Double =
            e.lastTickPosY + (e.posY - e.lastTickPosY) * getTick()!!.renderPartialTicks - mc.renderManager.viewerPosY
        val z: Double =
            e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * getTick()!!.renderPartialTicks - mc.renderManager.viewerPosZ
        if ((e is EntityPlayer && damage) && e.hurtTime != 0) {
            color = Color.RED.rgb
        }
        val a = (color shr 24 and 0xFF) / 255.0f
        val r = (color shr 16 and 0xFF) / 255.0f
        val g = (color shr 8 and 0xFF) / 255.0f
        val b = (color and 0xFF) / 255.0f

        when (type){
            1 -> {
                GlStateManager.pushMatrix()
                GL11.glBlendFunc(770, 771)
                GL11.glEnable(3042)
                GL11.glDisable(3553)
                GL11.glDisable(2929)
                GL11.glDepthMask(false)
                GL11.glLineWidth(3.0f)
                GL11.glColor4f(r, g, b, a)
                Render3DUtils.drawBoundingBoxOutline(
                    WrapperAxisAlignedBB(
                        e.entityBoundingBox.minX - 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                        e.entityBoundingBox.minY - e.posY + (e.posY - mc.renderManager.viewerPosY),
                        e.entityBoundingBox.minZ - 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ),
                        e.entityBoundingBox.maxX + 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                        e.entityBoundingBox.maxY + 0.1 - e.posY + (e.posY - mc.renderManager.viewerPosY),
                        e.entityBoundingBox.maxZ + 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ)
                    )
                )
                drawAxisAlignedBB(
                    AxisAlignedBB(
                        e.entityBoundingBox.minX - 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                        e.entityBoundingBox.minY - e.posY + (e.posY - mc.renderManager.viewerPosY),
                        e.entityBoundingBox.minZ - 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ),
                        e.entityBoundingBox.maxX + 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                        e.entityBoundingBox.maxY + 0.1 - e.posY + (e.posY - mc.renderManager.viewerPosY),
                        e.entityBoundingBox.maxZ + 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ)
                    ), r, g, b
                )
                GL11.glEnable(3553)
                GL11.glEnable(2929)
                GL11.glDepthMask(true)
                GL11.glDisable(3042)
                GlStateManager.popMatrix()
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            }

            2, 3 ->{
                val mode = type == 2
                GL11.glBlendFunc(770, 771)
                GL11.glEnable(3042)
                GL11.glLineWidth(3.0f)
                GL11.glDisable(3553)
                GL11.glDisable(2929)
                GL11.glDepthMask(false)
                GL11.glColor4d(r.toDouble(), g.toDouble(), b.toDouble(), a.toDouble())
                if (mode) {
                    Render3DUtils.drawBoundingBoxOutline(
                        WrapperAxisAlignedBB(
                            e.entityBoundingBox.minX - 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                            e.entityBoundingBox.minY - e.posY + (e.posY - mc.renderManager.viewerPosY),
                            e.entityBoundingBox.minZ - 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ),
                            e.entityBoundingBox.maxX + 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                            e.entityBoundingBox.maxY + 0.1 - e.posY + (e.posY - mc.renderManager.viewerPosY),
                            e.entityBoundingBox.maxZ + 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ)
                        )
                    )
                } else {
                    drawAxisAlignedBB(
                        AxisAlignedBB(
                            e.entityBoundingBox.minX - 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                            e.entityBoundingBox.minY - e.posY + (e.posY - mc.renderManager.viewerPosY),
                            e.entityBoundingBox.minZ - 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ),
                            e.entityBoundingBox.maxX + 0.05 - e.posX + (e.posX - mc.renderManager.viewerPosX),
                            e.entityBoundingBox.maxY + 0.1 - e.posY + (e.posY - mc.renderManager.viewerPosY),
                            e.entityBoundingBox.maxZ + 0.05 - e.posZ + (e.posZ - mc.renderManager.viewerPosZ)
                        ), r, g, b
                    )
                }
                GL11.glEnable(3553)
                GL11.glEnable(2929)
                GL11.glDepthMask(true)
                GL11.glDisable(3042)
            }

            4 -> {
                GL11.glPushMatrix()
                GL11.glTranslated(x, y - 0.2, z)
                GL11.glScalef(0.03f, 0.03f, 0.03f)
                GL11.glRotated(-mc.renderManager.playerViewY as Double, 0.0, 1.0, 0.0)
                GlStateManager.disableDepth()
                Gui.drawRect(-20, -1, -26, 75, Color.black.rgb)
                Gui.drawRect(-21, 0, -25, 74, color)
                Gui.drawRect(20, -1, 26, 75, Color.black.rgb)
                Gui.drawRect(21, 0, 25, 74, color)
                Gui.drawRect(-20, -1, 21, 5, Color.black.rgb)
                Gui.drawRect(-21, 0, 24, 4, color)
                Gui.drawRect(-20, 70, 21, 75, Color.black.rgb)
                Gui.drawRect(-21, 71, 25, 74, color)
                GlStateManager.enableDepth()
                GL11.glPopMatrix()
            }
        }
    }

    private fun drawAxisAlignedBB(axisAlignedBB: AxisAlignedBB, r: Float, g: Float, b: Float) {
        val a = 0.25f
        val ts = Tessellator.getInstance()
        val vb = ts.worldRenderer
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        ts.draw()
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        ts.draw()
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        ts.draw()
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        ts.draw()
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        ts.draw()
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR)
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        vb.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(r, g, b, a).endVertex()
        ts.draw()
    }

    companion object {
        var using = false
    }
}
