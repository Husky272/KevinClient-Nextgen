package kevin.module.modules.movement;

import kevin.event.EventState;
import kevin.event.EventTarget;
import kevin.event.MotionEvent;
import kevin.main.KevinClient;
import kevin.module.*;
import kevin.module.modules.combat.KillAura;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@SuppressWarnings("unused")
public final class NoSlow extends ClientModule {

    public final BooleanValue soulsand = new BooleanValue("Soulsand", true);
    public final BooleanValue liquidPush = new BooleanValue("LiquidPush", true);
    private final BooleanValue sword = new BooleanValue("Sword", false);
    private final BooleanValue consume = new BooleanValue("Consume", false);
    private final BooleanValue bow = new BooleanValue("Bow", false);
    private final FloatValue blockForwardMultiplier = new FloatValue("BlockForwardMultiplier", 1F, 0.2F, 1.0F, () -> sword.get());
    private final FloatValue blockStrafeMultiplier = new FloatValue("BlockStrafeMultiplier", 1F, 0.2F, 1.0F, () -> sword.get());
    private final FloatValue consumeForwardMultiplier = new FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F, () -> consume.get());
    private final FloatValue consumeStrafeMultiplier = new FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F, () -> consume.get());
    private final FloatValue bowForwardMultiplier = new FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F, () -> bow.get());
    private final FloatValue bowStrafeMultiplier = new FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F, () -> bow.get());
    private final BooleanValue swordPlace = new BooleanValue("Sword-Place", true, () -> sword.get());
    private final ListValue swordPlaceTiming = new ListValue("Sword-Place-Timing", new String[]{"PRE", "POST", "NONE"}, "NONE", () -> sword.get() && swordPlace.get());
    private final BooleanValue swordSwitch = new BooleanValue("Sword-Switch", false, () -> sword.get());
    private final ListValue swordSwitchTiming = new ListValue("Sword-Switch-Timing", new String[]{"PRE", "POST", "NONE"}, "NONE", () -> sword.get() && swordSwitch.get());
    private final BooleanValue consumeSwitch = new BooleanValue("Consume-Switch", false, () -> consume.get());
    private final ListValue consumeSwitchTiming = new ListValue("Consume-Switch-Timing", new String[]{"PRE", "POST", "NONE"}, "NONE", () -> consume.get() && consumeSwitch.get());
    private final BooleanValue consumeGay = new BooleanValue("Consume-Gay", true, () -> consume.get());
    private final BooleanValue consumeBug = new BooleanValue("Consume-Bug", false, () -> consume.get());
    private final BooleanValue consumeBugStopUsingItem = new BooleanValue("Consume-Bug-StopUsingItem", true, () -> consume.get() && consumeBug.get());
    private final BooleanValue consumeIntave = new BooleanValue("Consume-Intave", false, () -> consume.get());
    private final String stringTest = "helicopter helicopter";
    private final BooleanValue bowSwitch = new BooleanValue("Bow-Switch", false, () -> bow.get());
    private final ListValue bowSwitchTiming = new ListValue("Bow-Switch-Timing", new String[]{"PRE", "POST", "NONE"}, "NONE", () -> bow.get() && bowSwitch.get());

    public NoSlow() {
        super("NoSlow", "Modify slowness caused by using items.", ModuleCategory.MOVEMENT);
    }

    @Override
    public String getTag() {
        return "Sword: " + sword.get() + ", Consume: " + consume.get() + ", Bow: " + bow.get();
    }

    private boolean amIBlocking() {
        try {
            if (mc.thePlayer == null || mc.thePlayer.getItemInUse() == null || mc.thePlayer.getItemInUse().getItem() == null)
                return false;
        } catch (Throwable e) {
            return false;
        }

        boolean whatAnAwsomeVarName = false;
        try {
            whatAnAwsomeVarName = !mc.thePlayer.getItemInUse().isStackable() && mc.thePlayer.getItemInUse().getItem() instanceof ItemSword;
        } catch (ClassCastException e) {
            return false;
        }

        return mc.thePlayer.isBlocking() || KevinClient.moduleManager.get(KillAura.class).getBlockingStatus() || whatAnAwsomeVarName;
    }

    private boolean amIConsuming() {
        try {
            if (mc.thePlayer.getItemInUse() == null) return false;
            if (mc.thePlayer.getItemInUse().getItem() == null) return false;
        } catch (Throwable e) {
            return false;
        }
        return isHoldingConsumable(mc.thePlayer.getCurrentEquippedItem().getItem()) && isHoldingConsumable(mc.thePlayer.getItemInUse().getItem());
    }

    /*


    */
    @EventTarget
    public void onMotion(MotionEvent event) {
        if (amIBlocking()) {
            if (sword.get()) {
                if (swordPlace.get()) {
                    switch (swordPlaceTiming.get().toUpperCase()) {
                        case "PRE":
                            if (event.getEventState() == EventState.PRE) {
                                place();
                            }
                            break;
                        case "POST":
                            if (event.getEventState() == EventState.POST) {
                                place();
                            }
                            break;
                        case "NONE":
                            place();
                            break;
                    }
                }
                if (swordSwitch.get()) {
                    switch (swordSwitchTiming.get().toUpperCase()) {
                        case "PRE":
                            if (event.getEventState() == EventState.PRE) {
                                switchAndBack();
                            }
                            break;
                        case "POST":
                            if (event.getEventState() == EventState.POST) {
                                switchAndBack();
                            }
                            break;
                        case "NONE":
                            switchAndBack();
                            break;
                    }
                }
            }
        }
        if (amIConsuming()) {
            if (consumeSwitch.get()) {
                switch (consumeSwitchTiming.get().toUpperCase()) {
                    case "PRE":
                        if (event.getEventState() == EventState.PRE) {
                            switchAndBack();
                        }
                        break;
                    case "POST":
                        if (event.getEventState() == EventState.POST) {
                            switchAndBack();
                        }
                        break;
                    case "NONE":
                        switchAndBack();
                        break;
                }
            }
        }
        if (mc.thePlayer.isUsingItem()) {
            if (isHoldingBow(mc.thePlayer.getCurrentEquippedItem().getItem())) {
                if (bowSwitch.get()) {
                    switch (bowSwitchTiming.get().toUpperCase()) {
                        case "PRE":
                            if (event.getEventState() == EventState.PRE) {
                                switchAndBack();
                            }
                            break;
                        case "POST":
                            if (event.getEventState() == EventState.POST) {
                                switchAndBack();
                            }
                            break;
                        case "NONE":
                            switchAndBack();
                            break;
                    }
                }
            }
            if (isHoldingConsumable(mc.thePlayer.getCurrentEquippedItem().getItem())) {
                if (consumeIntave.get()) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
            }
        }
    }

    private boolean isHoldingConsumable(Item item) {
        if (item == null) return false;
        return item instanceof ItemFood || item instanceof ItemBucketMilk || item instanceof ItemPotion;
    }

    private boolean isHoldingBow(Item item) {
        if (item == null) return false;
        return item instanceof ItemBow;
    }

    private boolean isHoldingSword(Item item) {
        if (item == null) return false;
        return item instanceof ItemSword;
    }

    private void switchAndBack() {
        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    private void place() {
        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(
                new BlockPos(-1, -1, -1),
                255,
                mc.thePlayer.inventory.getCurrentItem(),
                0f, 0f, 0f
        ));
    }
}