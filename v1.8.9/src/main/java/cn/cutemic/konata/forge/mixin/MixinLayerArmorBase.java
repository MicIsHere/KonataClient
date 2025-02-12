package cn.cutemic.konata.forge.mixin;

import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import cn.cutemic.konata.features.impl.optimizes.OldAnimations;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase {
    /**
     * @author SuperSkidder
     * @reason old hurt Animation
     */
    @Overwrite
    public boolean shouldCombineTextures() {
        return OldAnimations.using && OldAnimations.oldDamage.getValue();
    }
}
