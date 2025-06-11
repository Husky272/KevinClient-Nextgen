package kevin.module.modules;

import kevin.event.EventTarget;
import kevin.event.TickEvent;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kevin.utils.entity.combatAndInventory.EntityUtils;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class Targets extends ClientModule {
    @NotNull
    private final BooleanValue players = new BooleanValue("Players", true);
    @NotNull
    private final BooleanValue mobs = new BooleanValue("Mobs", true);
    @NotNull
    private final BooleanValue animals = new BooleanValue("Animals", false);
    @NotNull
    private final BooleanValue invisible = new BooleanValue("Invisible", true);
    @NotNull
    private final BooleanValue death = new BooleanValue("Death", false);

    public Targets() {
        super("Targets", "Targets", ModuleCategory.COMBAT);
    }

    @EventTarget(
            ignoreCondition = true
    )
    public final void onUpdate(@NotNull TickEvent event) {

        EntityUtils.targetPlayer = this.players.get();
        EntityUtils.targetMobs = this.mobs.get();
        EntityUtils.targetAnimals = this.animals.get();
        EntityUtils.targetInvisible = this.invisible.get();
        EntityUtils.targetDeath = this.death.get();
    }
}
