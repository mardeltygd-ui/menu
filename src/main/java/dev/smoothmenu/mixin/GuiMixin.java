package dev.smoothmenu.mixin;

import dev.smoothmenu.AnimationConfig;
import dev.smoothmenu.Easing;
import net.minecraft.Util;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Плавное скольжение рамки выбранного слота хотбара. */
@Mixin(Gui.class)
public abstract class GuiMixin {
    @org.spongepowered.asm.mixin.Unique
    private static float smoothmenu$x = Float.NaN;
    @org.spongepowered.asm.mixin.Unique
    private static long smoothmenu$last;

    @ModifyArg(
        method = "renderHotbar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
            ordinal = 1
        ),
        index = 1,
        require = 0
    )
    private int smoothmenu$slideSelection(int targetX) {
        long now = Util.getMillis();
        float dt = smoothmenu$last == 0 ? 0.016f : Math.min((now - smoothmenu$last) / 1000f, 0.1f);
        smoothmenu$last = now;

        if (Float.isNaN(smoothmenu$x) || Math.abs(smoothmenu$x - targetX) > 400f) {
            smoothmenu$x = targetX;
        }
        smoothmenu$x = Easing.approach(smoothmenu$x, targetX, dt, AnimationConfig.HOTBAR_SPEED);
        return Math.round(smoothmenu$x);
    }
}
