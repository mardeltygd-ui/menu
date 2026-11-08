package dev.smoothmenu;

/** Функции плавности (easing). Вход и выход: 0..1. */
public final class Easing {
    private Easing() {}

    public static float clamp01(float t) {
        return t < 0f ? 0f : Math.min(t, 1f);
    }

    public static float easeOutCubic(float t) {
        t = clamp01(t);
        float u = 1f - t;
        return 1f - u * u * u;
    }

    public static float easeInOutCubic(float t) {
        t = clamp01(t);
        return t < 0.5f ? 4f * t * t * t : 1f - (float) Math.pow(-2f * t + 2f, 3) / 2f;
    }

    public static float easeOutBack(float t) {
        t = clamp01(t);
        final float c1 = 1.70158f;
        final float c3 = c1 + 1f;
        float u = t - 1f;
        return 1f + c3 * u * u * u + c1 * u * u;
    }

    /** Экспоненциальное приближение к цели, не зависит от FPS. */
    public static float approach(float current, float target, float dtSeconds, float speed) {
        float k = 1f - (float) Math.exp(-speed * dtSeconds);
        return current + (target - current) * k;
    }
}
