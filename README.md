# Smooth Menu

Клиентский мод для Fabric (Minecraft 1.21.4), который добавляет плавные анимации.

## Что делает

- **Заставка Mojang**: более долгое и мягкое появление и затухание (`SPLASH_SLOWDOWN`).
- **Все экраны** (главное меню, пауза, инвентарь, настройки и т.д.): плавное появление с лёгким приближением и осветлением.
- **Все кнопки**: плавная подсветка при наведении.
- **Хотбар**: рамка выбранного слота скользит между слотами, а не прыгает.

Все параметры (длительность, скорость, сила эффекта) лежат в `src/main/java/dev/smoothmenu/AnimationConfig.java`.

## Сборка

Нужны Java 21 и Gradle.

```
gradle build
```

Готовый `.jar` появится в `build/libs/`. Его же собирает GitHub Actions при каждом push (вкладка Actions, раздел Artifacts).

Версии Minecraft, Fabric Loader и Fabric API задаются в `gradle.properties`.

## Установка

1. Установите Fabric Loader для Minecraft 1.21.4.
2. Положите Fabric API и `smoothmenu-*.jar` в папку `mods`.
