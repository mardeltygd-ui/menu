package dev.smoothmenu;

/** Настройки анимаций. Меняйте значения и пересобирайте мод. */
public final class AnimationConfig {
    private AnimationConfig() {}

    /** Длительность появления любого экрана, мс. */
    public static final long SCREEN_OPEN_MS = 260;
    /** Начальный масштаб экрана при появлении. */
    public static final float SCREEN_START_SCALE = 0.94f;
    /** Максимальная затемнённость в начале появления экрана (0..1). */
    public static final float SCREEN_START_DARKNESS = 0.55f;

    /** Скорость подсветки кнопки при наведении (больше = быстрее). */
    public static final float BUTTON_HOVER_SPEED = 14f;
    /** Максимальная яркость подсветки кнопки (0..255). */
    public static final int BUTTON_HOVER_ALPHA = 46;

    /** Скорость скольжения рамки выбранного слота хотбара. */
    public static final float HOTBAR_SPEED = 28f;

    /** Множитель длительности заставки Mojang (появление и затухание). */
    public static final float SPLASH_SLOWDOWN = 1.8f;
}
