package kevin.module.modules.world;

import kevin.event.EventTarget;
import kevin.event.impl.PacketEvent;
import kevin.event.UpdateEvent;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.FloatValue;
import kevin.module.ModuleCategory;
import kevin.utils.MovementUtils;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

public final class Timer extends ClientModule {
    // $FF: synthetic field
    static final KProperty[] $$delegatedProperties;

    static {
        KProperty[] var0 = new KProperty[]{Reflection.property1(new PropertyReference1Impl(Timer.class, "balanceValue", "getBalanceValue()Z", 0)), Reflection.property1(new PropertyReference1Impl(Timer.class, "balanceWaitTimer", "getBalanceWaitTimer()F", 0)), Reflection.property1(new PropertyReference1Impl(Timer.class, "balanceDebug", "getBalanceDebug()Z", 0))};
        $$delegatedProperties = var0;
    }

    @NotNull
    private final FloatValue speedValue = new FloatValue("Speed", 2.0F, 0.1F, 30.0F);
    @NotNull
    private final BooleanValue balanceValue$delegate = new BooleanValue("BalanceTimer", false);
    @NotNull
    private final FloatValue balanceWaitTimer$delegate = new FloatValue("BalanceWaitTimer", 0.05F, 0.05F, 0.99F);
    @NotNull
    private final BooleanValue onMoveValue = new BooleanValue("OnlyOnMove", true);
    @NotNull
    private final BooleanValue balanceDebug$delegate = new BooleanValue("BalanceDebug", false);
    private long balance;
    private boolean balanceState;
    private long lastSent = System.currentTimeMillis();

    public Timer() {
        super("Timer", "Changes the speed of the entire game.", ModuleCategory.WORLD);
    }

    private boolean getBalanceValue() {
        return this.balanceValue$delegate.getValue(this, $$delegatedProperties[0]);
    }

    private float getBalanceWaitTimer() {
        return this.balanceWaitTimer$delegate.getValue(this, $$delegatedProperties[1]).floatValue();
    }

    private boolean getBalanceDebug() {
        return this.balanceDebug$delegate.getValue(this, $$delegatedProperties[2]);
    }

    public void onDisable() {
        if (ClientModule.mc.thePlayer != null) {
            ClientModule.mc.getTimer().timerSpeed = 1.0F;
        }
    }

    public void onEnable() {
        if (this.getBalanceValue()) {
            this.balanceState = true;
            this.balance = 0L;
            this.lastSent = System.currentTimeMillis();
        }

    }

    @EventTarget
    public void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.balanceState) {
            if (ClientModule.mc.thePlayer.motionX * ClientModule.mc.thePlayer.motionX + ClientModule.mc.thePlayer.motionZ * ClientModule.mc.thePlayer.motionZ <= 5.0E-4) {
                ClientModule.mc.getTimer().timerSpeed = this.getBalanceWaitTimer();
                return;
            }

            if (this.balance < -50L) {
                this.balanceState = false;
            }
        }

        if (!MovementUtils.isPlayerMoving() && this.onMoveValue.get() || this.balanceState && this.getBalanceValue()) {
            ClientModule.mc.getTimer().timerSpeed = 1.0F;
        } else {
            ClientModule.mc.getTimer().timerSpeed = this.speedValue.get().floatValue();
        }
    }

    @EventTarget
    public void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.getBalanceValue()) {
            if (event.getPacket() instanceof C03PacketPlayer && !event.isCancelled()) {
                this.balance += 50;
                if (this.balance > 0L) {
                    this.balanceState = true;
                }

                long time = System.currentTimeMillis();
                this.balance -= time - this.lastSent;
                this.lastSent = time;
            }

        }
    }

    /**
     @EventTarget fun onWorld(event: WorldEvent) {
     if (event.worldClient != null) return
     if (!autoDisable.get()) return
     this.toggle(false)
     }
     **/

    @Override
    @NotNull
    public String getTag() {
        return this.getBalanceValue() && this.getBalanceDebug() ? "Balance:" + this.balance : "Speed:" + this.speedValue.get().floatValue();
    }
}
