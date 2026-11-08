package dev.smoothmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;

/** Тихие интерфейсные звуки. Используются стандартные звуки игры. */
public final class SmoothSounds {
    private SmoothSounds() {}

    /** Короткий лёгкий «тик» при наведении на кнопку. */
    public static void playHover() {
        if (!AnimationConfig.SOUNDS_ENABLED) return;
        play(AnimationConfig.HOVER_SOUND_PITCH, AnimationConfig.HOVER_SOUND_VOLUME);
    }

    /** Мягкий низкий звук при открытии экрана. */
    public static void playOpen() {
        if (!AnimationConfig.SOUNDS_ENABLED) return;
        play(AnimationConfig.OPEN_SOUND_PITCH, AnimationConfig.OPEN_SOUND_VOLUME);
    }

    private static void play(float pitch, float volume) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getSoundManager() == null) return;
        mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), pitch, volume));
    }
}
