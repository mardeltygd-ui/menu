package dev.smoothmenu.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.smoothmenu.AnimationConfig;
import dev.smoothmenu.Easing;
import dev.smoothmenu.SmoothSounds;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Кнопки плавно увеличиваются при наведении мыши и плавно уменьшаются, когда мышь ушла. */
@Mixin(AbstractWidget.class)
public abstract class AbstractWidgetMixin {
    @Unique private float smoothmenu$progress = 0f;
    @Unique private boolean smoothmenu$wasHovered = false;
    @Unique private long smoothmenu$lastTime = 0L;
    @Unique private boolean smoothmenu$transformed = false;

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void smoothmenu$begin(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        smoothmenu$transformed = false;
        AbstractWidget w = (AbstractWidget) (Object) this;
        if (!(w instanceof AbstractButton) || !w.visible) {
            return;
        }

        long now = Util.getMillis();
        boolean firstFrame = smoothmenu$lastTime == 0L;
        float dt = firstFrame ? 0.016f : Math.min((now - smoothmenu$lastTime) / 1000f, 0.1f);
        smoothmenu$lastTime = now;

        boolean over = mouseX >= w.getX() && mouseY >= w.getY()
                && mouseX < w.getX() + w.getWidth() && mouseY < w.getY() + w.getHeight();
        boolean hovered = w.active && (over || w.isFocused());

        if (hovered && !smoothmenu$wasHovered && !firstFrame) {
            SmoothSounds.playHover();
        }
        smoothmenu$wasHovered = hovered;

        smoothmenu$progress = Easing.approach(smoothmenu$progress, hovered ? 1f : 0f, dt, AnimationConfig.BUTTON_HOVER_SPEED);
        if (smoothmenu$progress < 0.001f) {
            smoothmenu$progress = 0f;
            return;
        }

        float scale = 1f + AnimationConfig.BUTTON_HOVER_SCALE * smoothmenu$progress;
        float cx = w.getX() + w.getWidth() / 2f;
        float cy = w.getY() + w.getHeight() / 2f;
        PoseStack pose = g.pose();
        pose.pushPose();
        pose.translate(cx, cy, 0f);
        pose.scale(scale, scale, 1f);
        pose.translate(-cx, -cy, 0f);
        smoothmenu$transformed = true;
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void smoothmenu$end(GuiGraphics g, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!smoothmenu$transformed) {
            return;
        }
        AbstractWidget w = (AbstractWidget) (Object) this;
        int a = Math.round(smoothmenu$progress * AnimationConfig.BUTTON_HOVER_ALPHA);
        if (a > 0) {
            g.fill(w.getX(), w.getY(), w.getX() + w.getWidth(), w.getY() + w.getHeight(), (a << 24) | 0xFFFFFF);
        }
        g.pose().popPose();
        smoothmenu$transformed = false;
    }
}
