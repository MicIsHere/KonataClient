package top.fpsmaster.forge.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import top.fpsmaster.forge.api.IRenderManager;
import top.fpsmaster.features.impl.render.FreeLook;

@Mixin(RenderManager.class)
@Implements(@Interface(iface = IRenderManager.class, prefix = "fpsmaster$"))
public class MixinRenderManager implements IRenderManager {

    @Shadow
    private double renderPosX;
    @Shadow
    private double renderPosY;
    @Shadow
    private double renderPosZ;

    @Override
    public double renderPosX() {
        return renderPosX;
    }

    @Override
    public double renderPosY() {
        return renderPosY;
    }

    @Override
    public double renderPosZ() {
        return renderPosZ;
    }
    @Shadow
    public float playerViewY;
    @Shadow
    public float playerViewX;
    @Shadow
    public double viewerPosX;
    @Shadow
    public double viewerPosY;
    @Shadow
    public double viewerPosZ;
    @Shadow
    public World world;
    @Shadow
    public GameSettings options;
    @Shadow
    public Entity renderViewEntity;
    @Shadow
    public Entity pointedEntity;
    @Shadow
    private FontRenderer textRenderer;


    /**
     * @author SuperSkidder
     * @reason FreeLook
     */
    @Overwrite
    public void cacheActiveRenderInfo(World worldIn, FontRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks) {
        this.world = worldIn;
        this.options = optionsIn;
        this.renderViewEntity = livingPlayerIn;
        this.pointedEntity = pointedEntityIn;
        this.textRenderer = textRendererIn;
        if (livingPlayerIn instanceof EntityLivingBase && ((EntityLivingBase)livingPlayerIn).isPlayerSleeping()) {
            IBlockState iblockstate = worldIn.getBlockState(new BlockPos(livingPlayerIn));
            Block block = iblockstate.getBlock();
            if (block.isBed(iblockstate, worldIn, new BlockPos(livingPlayerIn), livingPlayerIn)) {
                int i = block.getBedDirection(iblockstate, worldIn, new BlockPos(livingPlayerIn)).getHorizontalIndex();
                this.playerViewY = (float)(i * 90 + 180);
                this.playerViewX = 0.0F;
            }
        } else {
            if (FreeLook.using) {
                this.playerViewY = FreeLook.getCameraPrevYaw() + (FreeLook.getCameraYaw() - FreeLook.getCameraPrevYaw()) * partialTicks;
                this.playerViewX = FreeLook.getCameraPrevPitch() + (FreeLook.getCameraPitch() - FreeLook.getCameraPrevPitch()) * partialTicks;
            } else {
                this.playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.rotationYaw - livingPlayerIn.prevRotationYaw) * partialTicks;
                this.playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
            }
        }

        if (optionsIn.thirdPersonView == 2) {
            this.playerViewY += 180.0F;
        }

        this.viewerPosX = livingPlayerIn.lastTickPosX + (livingPlayerIn.posX - livingPlayerIn.lastTickPosX) * (double)partialTicks;
        this.viewerPosY = livingPlayerIn.lastTickPosY + (livingPlayerIn.posY - livingPlayerIn.lastTickPosY) * (double)partialTicks;
        this.viewerPosZ = livingPlayerIn.lastTickPosZ + (livingPlayerIn.posZ - livingPlayerIn.lastTickPosZ) * (double)partialTicks;
    }
}
