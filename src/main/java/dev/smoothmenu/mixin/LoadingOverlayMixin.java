package dev.smoothmenu.mixin;

import dev.smoothmenu.AnimationConfig;
import net.minecraft.client.gui.screens.LoadingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Заставка Mojang: растягивает время затухания и появления, чтобы переход был плавнее. */
@Mixin(LoadingOverlay.class)
public abstract class LoadingOverlayMixin {

    // 1000.0F: длительность затухания заставки
    @ModifyConstant(method = "render", constant = @Constant(floatValue = 1000.0F), require = 0)
    private float smoothmenu$fadeOut(float original) {
        return original * AnimationConfig.SPLASH_SLOWDOWN;
    }

    // 500.0F: длительность появления логотипа
    @ModifyConstant(method = "render", constant = @Constant(floatValue = 500.0F), require = 0)
    private float smoothmenu$fadeIn(float original) {
        return original * AnimationConfig.SPLASH_SLOWDOWN;
    }
}
