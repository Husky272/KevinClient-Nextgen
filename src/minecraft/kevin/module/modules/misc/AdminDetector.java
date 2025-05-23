package kevin.module.modules.misc;

import joptsimple.internal.Strings;
import kevin.command.ICommand;
import kevin.event.EventTarget;
import kevin.event.PacketEvent;
import kevin.event.UpdateEvent;
import kevin.hud.element.elements.ConnectNotificationType;
import kevin.hud.element.elements.Notification;
import kevin.main.KevinClient;
import kevin.module.*;
import kevin.utils.ChatUtils;
import kevin.utils.timer.TickTimer;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminDetector extends ClientModule implements ICommand {

    public static final AdminDetector INSTANCE = new AdminDetector();

    public static AdminDetector getInstance() {
        return INSTANCE;
    }

    private final File adminNamesFile;

    private final ListValue modeValue = new ListValue("Mode", new String[]{"Tab"}, "Tab");
    private final TextValue tabCommand = new TextValue("TabCommand", "/tell");
    private final IntegerValue waitTicks = new IntegerValue("WaitTick", 100, 0, 200);
    private final ListValue notificationMode = new ListValue("NotificationMode", new String[]{"Chat", "Notification"}, "Chat");
    private final BooleanValue noNotFindNotification = new BooleanValue("NoNotFindNotification", true);

    private final TickTimer timer = new TickTimer();
    private boolean waiting = false;

    private final ExecutorService pool = Executors.newSingleThreadExecutor();

    private AdminDetector() {
        super("AdminDetector", "Detect server admins.", ModuleCategory.MISC);

        File file = KevinClient.fileManager.adminNamesFile;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.adminNamesFile = file;
    }

    @Override
    public void run(String[] args) {
        if (args == null || args.length < 2) {
            usageMessage();
            return;
        }

        String command = args[0];
        List<String> argNames = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            argNames.add(args[i]);
        }

        List<String> names = readLines(adminNamesFile);

        switch (command.toLowerCase()) {
            case "add":
                for (String name : argNames) {
                    if (names.contains(name)) {
                        ChatUtils.messageWithStart("§cName is already in the list!");
                        return;
                    }
                    appendToFile(name);
                    ChatUtils.messageWithStart("§aName successfully added to the list!");
                }
                break;
            case "remove":
                for (String name : argNames) {
                    if (!names.contains(name)) {
                        ChatUtils.messageWithStart("§cName is not in the list!");
                        return;
                    }
                    List<String> filtered = new ArrayList<>();
                    for (String line : names) {
                        if (!line.equals(name) && !line.isEmpty()) {
                            filtered.add(line);
                        }
                    }
                    try {
                        Files.write(adminNamesFile.toPath(), filtered);
                        ChatUtils.messageWithStart("§aName successfully removed from the list!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                usageMessage();
        }
    }

    private void usageMessage() {
        ChatUtils.messageWithStart("§cUsage: .Admin <Add/Remove> <Name>");
    }

    private void appendToFile(String text) {
        try {
            Files.write(adminNamesFile.toPath(), (text + "\n").getBytes(), java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readLines(File file) {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        timer.update();
        if (!timer.hasTimePassed(waitTicks.get() + 1)) return;

        switch (modeValue.get()) {
            case "Tab":
                mc.getNetHandler().addToSendQueue(new C14PacketTabComplete(tabCommand.get() + " "));
                waiting = true;
                timer.reset();
                break;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        Object packet = event.getPacket();

        switch (modeValue.get()) {
            case "Tab":
                if (!waiting) return;
                if (packet instanceof S3APacketTabComplete) {
                    pool.execute(() -> {
                        S3APacketTabComplete tabPacket = (S3APacketTabComplete) packet;
                        String[] players = tabPacket.func_149630_c();
                        List<String> admins = readLines(adminNamesFile);
                        admins.removeIf(String::isEmpty);

                        List<String> findAdmins = new ArrayList<>();
                        for (String player : players) {
                            if (admins.contains(player)) {
                                findAdmins.add(player);
                            }
                        }
                        notifyResult(findAdmins);
                    });
                    waiting = false;
                    event.cancelEvent();
                }
                break;
        }
    }

    private void notifyResult(List<String> findAdmins) {
        if (findAdmins.isEmpty()) {
            if (!noNotFindNotification.get()) {
                switch (notificationMode.get()) {
                    case "Chat":
                        ChatUtils.messageWithStart("[AdminDetector] No admin found.");
                        break;
                    case "Notification":
                        KevinClient.hud.addNotification(
                                new Notification("No admin found.", "Admin Detector", ConnectNotificationType.OK));
                        break;
                }
            }
            return;
        }

        String formatted = Strings.join(findAdmins.toArray(new String[0]), "§7, §c");

        switch (notificationMode.get()) {
            case "Chat":
                ChatUtils.messageWithStart("[AdminDetector] Warning: found " + findAdmins.size() + " admin(s)! [§c" + formatted + "]");
                break;
            case "Notification":
                KevinClient.hud.addNotification(
                        new Notification("Warning: found " + findAdmins.size() + " admin(s)! [§c" + formatted + "]",
                                "Admin Detector", ConnectNotificationType.Warn));
                break;
        }
    }
}