package dev.smoothmenu;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

public class SmoothMenuClient implements ClientModInitializer {
    public static final String MOD_ID = "smoothmenu";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Screen lastScreen;
    private static long openedAt;
    private static long lastFrame;

    private static final Map<AbstractWidget, Float> HOVER = new WeakHashMap<>();

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            // AFTER_INIT вызывается и при изменении размера окна, анимацию перезапускаем только для нового экрана
            if (screen != lastScreen) {
                lastScreen = screen;
                openedAt = Util.getMillis();
                HOVER.clear();
            }

            ScreenEvents.beforeRender(screen).register(SmoothMenuClient::beforeRender);
            ScreenEvents.afterRender(screen).register(SmoothMenuClient::afterRender);
        });
        LOGGER.info("Smooth Menu загружен");
    }

    private static float openProgress() {
        long elapsed = Util.getMillis() - openedAt;
        return Easing.clamp01(elapsed / (float) AnimationConfig.SCREEN_OPEN_MS);
    }

    private static void beforeRender(Screen screen, GuiGraphics g, int mouseX, int mouseY, float tickDelta) {
        float e = Easing.easeOutCubic(openProgress());
        float scale = AnimationConfig.SCREEN_START_SCALE + (1f - AnimationConfig.SCREEN_START_SCALE) * e;

        g.pose().pushPose();
        float cx = screen.width / 2f;
        float cy = screen.height / 2f;
        g.pose().translate(cx, cy, 0f);
        g.pose().scale(scale, scale, 1f);
        g.pose().translate(-cx, -cy, 0f);
    }

    private static void afterRender(Screen screen, GuiGraphics g, int mouseX, int mouseY, float tickDelta) {
        g.pose().popPose();

        long now = Util.getMillis();
        float dt = lastFrame == 0 ? 0.016f : Math.min((now - lastFrame) / 1000f, 0.1f);
        lastFrame = now;

        // Плавная подсветка кнопок при наведении
        for (AbstractWidget w : Screens.getButtons(screen)) {
            if (!w.visible) continue;
            float cur = HOVER.getOrDefault(w, 0f);
            float target = (w.active && w.isHoveredOrFocused()) ? 1f : 0f;
            cur = Easing.approach(cur, target, dt, AnimationConfig.BUTTON_HOVER_SPEED);
            HOVER.put(w, cur);
            int a = Math.round(cur * AnimationConfig.BUTTON_HOVER_ALPHA);
            if (a > 0) {
                g.fill(w.getX(), w.getY(), w.getX() + w.getWidth(), w.getY() + w.getHeight(), (a << 24) | 0xFFFFFF);
            }
        }

        // Плавное появление: затемнение исчезает
        float t = openProgress();
        if (t < 1f) {
            int a = Math.round((1f - Easing.easeOutCubic(t)) * AnimationConfig.SCREEN_START_DARKNESS * 255f);
            if (a > 0) {
                g.fill(0, 0, screen.width, screen.height, a << 24);
            }
        }
    }
}
