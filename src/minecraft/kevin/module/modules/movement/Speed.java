package kevin.module.modules.movement;

import kevin.event.*;
import kevin.module.*;
import kevin.module.modules.movement.speeds.SpeedMode;
import kevin.module.modules.movement.speeds.aac.AAC5Fast;
import kevin.module.modules.movement.speeds.aac.AAC5Long;
import kevin.module.modules.movement.speeds.matrix.MatrixHop;
import kevin.module.modules.movement.speeds.matrix.MatrixNew;
import kevin.module.modules.movement.speeds.other.*;
import kevin.module.modules.movement.speeds.verus.VerusHop;
import kevin.module.modules.movement.speeds.verus.VerusYPort;
import kevin.module.modules.movement.speeds.vulcan.*;
import kevin.utils.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

import java.util.ArrayList;
import java.util.List;

public final class Speed extends ClientModule {

    public static final Speed INSTANCE = new Speed();

    // Configuration Values
    private final FloatValue speedValue = new FloatValue("CustomSpeed", 1.6F, 0.0F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue doLaunchSpeedValue = new BooleanValue("CustomDoLaunchSpeed", true, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue launchSpeedValue = new FloatValue("CustomLaunchSpeed", 1.6F, 0.2F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue doMinimumSpeedValue = new BooleanValue("CustomDoMinimumSpeed", true, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue minimumSpeedValue = new FloatValue("CustomMinimumSpeed", 0.25F, 0.1F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue addYMotionValue = new FloatValue("CustomAddYMotion", 0.0F, 0.0F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue doCustomYValue = new BooleanValue("CustomDoModifyJumpY", true, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue yValue = new FloatValue("CustomY", 0.42F, 0.0F, 4.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue upTimerValue = new FloatValue("CustomUpTimer", 1.0F, 0.1F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue jumpTimerValue = new FloatValue("CustomJumpTimer", 1.25F, 0.1F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue downTimerValue = new FloatValue("CustomDownTimer", 1.0F, 0.1F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });

    private final ListValue strafeValue = new ListValue("CustomStrafe", new String[]{"Strafe", "Boost", "Plus", "PlusOnlyUp", "PlusOnlyDown", "Non-Strafe"}, "Boost", ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final ListValue plusMode = new ListValue("PlusBoostMode", new String[]{"Add", "Multiply"}, "Add", ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final FloatValue plusMultiply = new FloatValue("PlusMultiplyAmount", 1.1F, 1.0F, 2.0F, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final IntegerValue groundStay = new IntegerValue("CustomGroundStay", 0, 0, 10, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue groundResetXZValue = new BooleanValue("CustomGroundResetXZ", false, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue resetXZValue = new BooleanValue("CustomResetXZ", false, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue resetYValue = new BooleanValue("CustomResetY", false, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue groundSpaceKeyPressed = new BooleanValue("CustomPressSpaceKeyOnGround", true, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue airSpaceKeyPressed = new BooleanValue("CustomPressSpaceKeyInAir", false, ()->{
        return mode.get().toLowerCase().equals("custom");
    });
    private final BooleanValue usePreMotion = new BooleanValue("CustomUsePreMotion", true, ()->{
        return mode.get().toLowerCase().equals("custom");
    });

    private static final ArrayList<SpeedMode> speeds = new ArrayList<>();
    public static ListValue mode = null;
    private final BooleanValue keepSprint = new BooleanValue("KeepSprint", false);
    private final BooleanValue antiKnockback = new BooleanValue("AntiKnockBack", false);
    private final FloatValue antiKnockbackLong = new FloatValue("AntiKnockBackLong", 0.0F, 0.0F, 1.0F);
    private final FloatValue antiKnockbackHigh = new FloatValue("AntiKnockBackHigh", 1.0F, 0.0F, 1.0F);

    static {
        // Initialize supported speed modes
        speeds.add(Custom.INSTANCE);
        speeds.add(AAC5Long.INSTANCE);
        speeds.add(AAC5Fast.INSTANCE);
        speeds.add(YPort.INSTANCE);
        speeds.add(LegacyNCP.INSTANCE);
        speeds.add(NCPStable.INSTANCE);
        speeds.add(Hypixel.INSTANCE);
        speeds.add(VerusYPort.INSTANCE);
        speeds.add(VerusHop.INSTANCE);
        speeds.add(CollisionLowHop.INSTANCE);
        speeds.add(MatrixHop.INSTANCE);
        speeds.add(MatrixNew.INSTANCE);
        speeds.add(VulcanHop.INSTANCE);
        speeds.add(VulcanGround.INSTANCE);
        speeds.add(VulcanLowHop.INSTANCE);
        speeds.add(VulcanYPort.INSTANCE);
        speeds.add(VulcanYPort2.INSTANCE);
        speeds.add(Spartan.INSTANCE);
        speeds.add(Prediction.INSTANCE);
        speeds.add(IntaveHop.INSTANCE);

        // Build mode names list
        List<String> namesList = new ArrayList<>();
        for (SpeedMode speedMode : speeds) {
            namesList.add(speedMode.getModeName());
        }

        // Convert to array and set as mode options
        String[] namesArray = namesList.toArray(new String[0]);
        mode = new ListValue("Mode", namesArray, namesArray[0]);
    }

    private Speed() {
        super("Speed", "Allows you to move faster.", ModuleCategory.MOVEMENT);
    }

    // Getters for configuration values
    public FloatValue getSpeedValue() {
        return speedValue;
    }

    public BooleanValue getDoLaunchSpeedValue() {
        return doLaunchSpeedValue;
    }

    public FloatValue getLaunchSpeedValue() {
        return launchSpeedValue;
    }

    public BooleanValue getDoMinimumSpeedValue() {
        return doMinimumSpeedValue;
    }

    public FloatValue getMinimumSpeedValue() {
        return minimumSpeedValue;
    }

    public FloatValue getAddYMotionValue() {
        return addYMotionValue;
    }

    public BooleanValue getDoCustomYValue() {
        return doCustomYValue;
    }

    public FloatValue getYValue() {
        return yValue;
    }

    public FloatValue getUpTimerValue() {
        return upTimerValue;
    }

    public FloatValue getJumpTimerValue() {
        return jumpTimerValue;
    }

    public FloatValue getDownTimerValue() {
        return downTimerValue;
    }

    public ListValue getStrafeValue() {
        return strafeValue;
    }

    public ListValue getPlusMode() {
        return plusMode;
    }

    public FloatValue getPlusMultiply() {
        return plusMultiply;
    }

    public IntegerValue getGroundStay() {
        return groundStay;
    }

    public BooleanValue getGroundResetXZValue() {
        return groundResetXZValue;
    }

    public BooleanValue getResetXZValue() {
        return resetXZValue;
    }

    public BooleanValue getResetYValue() {
        return resetYValue;
    }

    public BooleanValue getGroundSpaceKeyPressed() {
        return groundSpaceKeyPressed;
    }

    public BooleanValue getAirSpaceKepPressed() {
        return airSpaceKeyPressed;
    }

    public BooleanValue getUsePreMotion() {
        return usePreMotion;
    }

    public String getTag() {
        return mode.get();
    }

    private SpeedMode getNowMode() {
        for (SpeedMode speedMode : speeds) {
            if (mode.get().equals(speedMode.getModeName())) {
                return speedMode;
            }
        }
        throw new IllegalStateException("Current speed mode not found!");
    }

    @Override
    public void onDisable() {
        mc.getTimer().timerSpeed = 1.0f;
        mc.thePlayer.speedInAir = 0.02f;
        getNowMode().onDisable();
    }

    @Override
    public void onEnable() {
        getNowMode().onEnable();
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        getNowMode().onMove(event);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        getNowMode().onUpdate(event);
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (!mc.thePlayer.isSneaking() && event.getEventState() == EventState.PRE) {
            if (MovementUtils.isMoving() && keepSprint.get()) {
                mc.thePlayer.setSprinting(true);
            }
            getNowMode().onPreMotion();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity velocityPacket && antiKnockback.get()) {
            Entity entity = mc.theWorld.getEntityByID(velocityPacket.getEntityID());

            if (entity != null && entity.equals(mc.thePlayer)) {
                float horizontal = antiKnockbackLong.get();
                float vertical = antiKnockbackHigh.get();

                if (horizontal == 0.0f && vertical == 0.0f) {
                    event.cancelEvent();
                } else {
                    velocityPacket.motionX = (int) (velocityPacket.motionX * horizontal);
                    velocityPacket.motionY = (int) (velocityPacket.motionY * vertical);
                    velocityPacket.motionZ = (int) (velocityPacket.motionZ * horizontal);
                }
            }
        }
        getNowMode().onPacket(event);
    }

    @EventTarget
    public void onBB(BlockBBEvent event) {
        getNowMode().onBlockBB(event);
    }
}