package kevin.module;


import org.jetbrains.annotations.NotNull;


public enum ModuleCategory {
    COMBAT,
    EXPLOIT,
    MISC,
    MOVEMENT,
    PLAYER,
    RENDER,
    WORLD;

    // $FF: synthetic field
    private static final ModuleCategory[] $ENTRIES = $values();

    @NotNull
    public static ModuleCategory[] getEntries() {
        return $ENTRIES;
    }

    // $FF: synthetic method
    private static ModuleCategory[] $values() {
        return new ModuleCategory[]{COMBAT, EXPLOIT, MISC, MOVEMENT, PLAYER, RENDER, WORLD};
    }
}
