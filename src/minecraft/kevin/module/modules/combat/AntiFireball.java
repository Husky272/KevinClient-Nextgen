package kevin.module.modules.combat;

import kevin.event.EventTarget;
import kevin.event.UpdateEvent;
import kevin.module.*;
import kevin.utils.entity.rotation.RotationUtils;
import kevin.utils.system.timer.MSTimer;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C0APacketAnimation;
import org.jetbrains.annotations.NotNull;

public final class AntiFireball extends ClientModule {
    private static final String[] var1 = new String[]{"Normal", "Packet", "None"};
    private static final String[] var10004 = ClientModule.arrayOf(var1);
    @NotNull
    private final MSTimer timer = new MSTimer();
    @NotNull
    private final ListValue swingValue = new ListValue("Swing", var10004, "Normal");
    @NotNull
    private final BooleanValue rotationValue = new BooleanValue("Rotation", true);
    @NotNull
    private final FloatValue radius = new FloatValue("Radius", 3.0F, 3.0F, 6.0F);
    private boolean needModifyMove;

    public AntiFireball() {
        super("AntiFireball", "", ModuleCategory.COMBAT);

    }

    @EventTarget
    public void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");

        for (Entity entity : ClientModule.mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityFireball && ClientModule.mc.thePlayer.getDistanceToEntity(entity) < this.radius.get().floatValue() && this.timer.hasTimePassed(300)) {
                this.needModifyMove = true;
                if (this.rotationValue.get()) {
                    RotationUtils.setTargetRotation(RotationUtils.getRotations(entity));
                }

                if (this.swingValue.equal("Normal")) {
                    ClientModule.mc.thePlayer.swingItem();
                } else if (this.swingValue.equal("Packet")) {
                    ClientModule.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                }

                ClientModule.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, Action.ATTACK));
                this.timer.reset();
                break;
            }
        }

    }
}
