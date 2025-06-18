/*
; * Copyright (c) 2025 a114mc
; * All Rights Reserved.
; */
package kevin.module.modules.misc;

import kevin.event.EventTarget;
import kevin.event.impl.PacketEvent;
import kevin.event.UpdateEvent;
import kevin.hud.element.elements.ConnectNotificationType;
import kevin.hud.element.elements.Notification;
import kevin.main.KevinClient;
import kevin.module.*;
import kevin.utils.ChatUtils;
import kevin.utils.system.timer.MSTimer;
import net.minecraft.event.ClickEvent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;


public class AutoCommand extends ClientModule {
    private final BooleanValue autoLoginValue = new BooleanValue("AutoLogin", true);
    private final BooleanValue autoRegisterValue = new BooleanValue("AutoRegister", true);
    private final BooleanValue autoJoin = new BooleanValue("AutoJoin", true);

    private final ListValue autoJoinMessageMode = new ListValue("AutoJoinSendMessageMode", arrayOf("Custom", "FindCommand"), "Custom");
    private final TextValue autoJoinDetectMessage = new TextValue("AutoJoinDetectMessage", "top");
    private final TextValue autoJoinDetectMessage2 = new TextValue("AutoJoinDetectMessage2", "died");

    private final IntegerValue autoJoinDelay = new IntegerValue("AutoJoinDelay", 5000, 50, 10000);
    private final TextValue autoJoinMessage = new TextValue("AutoJoinMessage", "/join");
    private final ListValue autoJoinNotificationMode = new ListValue("AutoJoinNotificationMode", arrayOf("Notification", "Chat", "None"), "Notification");

    private final TextValue registerAndLoginPassword = new TextValue("RegisterAndLoginPassword", "Password");
    private final IntegerValue autoLoginAndRegisterDelay = new IntegerValue("AutoLoginAndRegisterDelay", 2500, 100, 5000);

    private final ListValue autoLoginMode = new ListValue("AutoLoginMode", arrayOf("/l", "/login", "Custom"), "/login");
    private final TextValue autoLoginCustom = new TextValue("AutoLoginCustomCommand", "/login");

    private final TextValue autoRegisterCommand = new TextValue("AutoRegisterCommand", "/register");
    private final TextValue autoLoginDetectMessage = new TextValue("AutoLoginDetectMessage", "login");

    private final TextValue autoRegisterDetectMessage = new TextValue("AutoRegisterDetectMessage", "register");

    private final MSTimer timer = new MSTimer();

    private final MSTimer autoJoinTimer = new MSTimer();
    private String command = "";
    private boolean register = false;
    private boolean login = false;
    private boolean join = false;

    public AutoCommand() {
        super("AutoCommand", "Send commands automatically.", ModuleCategory.MISC);
    }

    @Override
    public void onDisable() {
        register = false;
        login = false;
        super.onDisable();
    }

    @Override
    public String getTag() {
        return "Auto" + ((autoJoin.get()) ? " Join" : "") + ((autoLoginValue.get()) ? " Login" : "") + ((autoRegisterValue.get()) ? " Register" : "");
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Object packet = event.getPacket();
        if (!(packet instanceof S02PacketChat)) {
            return;
        }
        final String text = ((S02PacketChat) packet).getChatComponent().getFormattedText();
        final IChatComponent component = ((S02PacketChat) packet).getChatComponent();
        if (text.contains(autoRegisterDetectMessage.get()) && autoRegisterValue.get()) {
            register = true;
            timer.reset();
            return;
        }
        if (text.contains(autoLoginDetectMessage.get()) && autoLoginValue.get()) {
            login = true;
            timer.reset();
        }
        if ((text.contains(autoJoinDetectMessage.get()) || text.contains(autoJoinDetectMessage2.get())) && autoJoin.get()) {
            join = true;
            autoJoinTimer.reset();
            switch (autoJoinNotificationMode.get()) {
                case "Notification":
                    KevinClient.hud.addNotification(new Notification("Send command after " + autoJoinDelay.get() + " MS.", "AutoJoin", ConnectNotificationType.Info));
                    break;

                case "Chat":
                    ChatUtils.messageWithStart("[AutoJoin] Send command after " + autoJoinDelay.get() + " MS.");
                    break;
            }

            if (autoJoinMessageMode.equal("Custom") || autoJoinMessage.get().equalsIgnoreCase("custom")) {
                command = autoJoinMessage.get();
            } else {
                for (IChatComponent sib : component.getSiblings()) {
                    final ClickEvent clickEvent = sib.getChatStyle().getChatClickEvent();
                    if (clickEvent != null && clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND && clickEvent.getValue().startsWith("/")) {
                        command = clickEvent.getValue();
                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (autoJoinTimer.hasTimePassed((long) autoJoinDelay.get()) && join) {
            mc.thePlayer.sendChatMessage(autoJoinMessage.get());
            join = false;
            switch (autoJoinNotificationMode.get()) {
                case "Notification":
                    KevinClient.hud.addNotification(new Notification("Auto Join...", "AutoJoin", ConnectNotificationType.OK));
                    break;
                case "Chat":
                    ChatUtils.messageWithStart("[AutoJoin] Auto Join...");
                    break;
            }
        }
        if (!timer.hasTimePassed(autoLoginAndRegisterDelay.get().longValue()))
            return;
        if (register) {
            mc.thePlayer.sendChatMessage(autoRegisterCommand.get() + " " + registerAndLoginPassword.get() + " " + registerAndLoginPassword.get());
            register = false;
        }
        if (login) {
            final Object start;
            switch (autoLoginMode.get()) {
                case "/l":
                    start = "/l";
                    break;
                case "/login":
                    start = "/login";
                    break;
                case "Custom":
                    start = autoLoginCustom.get();
                    break;
                default:
                    start = "";
                    break;
            }
            final Object text = start + " " + registerAndLoginPassword.get();
            mc.thePlayer.sendChatMessage((String) text);
            login = false;
        }
    }
}