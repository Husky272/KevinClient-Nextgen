package kevin.module.modules.render;

import kevin.event.*;
import kevin.event.impl.KeyEvent;
import kevin.event.impl.PacketEvent;
import kevin.hud.element.elements.ScoreboardElement;
import kevin.hud.designer.GuiHudDesigner;
import kevin.main.KevinClient;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S09PacketHeldItemChange;

import java.util.List;

public final class HUD extends ClientModule {

    private static int currentPacketSlot = -1;

    public final BooleanValue keepScoreboard = new BooleanValue("KeepScoreboard", true);
    private final BooleanValue hotBarShowCurrentSlot = new BooleanValue("HotBarShowCurrentSlot", true);

    public HUD() {
        super("HUD", "Toggles visibility of the HUD.", ModuleCategory.RENDER);
        setState(true);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (!(mc.currentScreen instanceof GuiHudDesigner)) {
            KevinClient.INSTANCE.getHud().render(false);
        }
    }

    @EventTarget(ignoreCondition = true)
    public void renderScoreboard(Render2DEvent event) {
        if (!getState() && keepScoreboard.get() && containsScoreboardElement()) {
            KevinClient.INSTANCE.getHud().renderScoreboardOnly();
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        KevinClient.INSTANCE.getHud().update();
    }

    @EventTarget(ignoreCondition = true)
    public void onPacket(PacketEvent event) {
        //noinspection unchecked
        Packet<?> packet = event.getPacket();

        if (packet instanceof C09PacketHeldItemChange) {
            currentPacketSlot = ((C09PacketHeldItemChange) packet).getSlotId();
        }
        if (packet instanceof S09PacketHeldItemChange) {
            currentPacketSlot = ((S09PacketHeldItemChange) packet).getHeldItemHotbarIndex();
        }

    }

    @EventTarget(ignoreCondition = true)
    public void updateScoreboard(UpdateEvent event) {
        if (!getState() && keepScoreboard.get() && containsScoreboardElement()) {
            KevinClient.INSTANCE.getHud().updateScoreboardOnly();
        }
    }

    @EventTarget
    public void onKey(KeyEvent event) {
        KevinClient.INSTANCE.getHud().handleKey('a', event.getKey());
    }

    public int getCurrentSlot() {
        return hotBarShowCurrentSlot.get() ? currentPacketSlot : mc.thePlayer.inventory.currentItem;
    }

    private boolean containsScoreboardElement() {
        List elements = KevinClient.INSTANCE.getHud().getElements();
        for (Object element : elements) {
            if (element instanceof ScoreboardElement) {
                return true;
            }
        }
        return false;
    }

}