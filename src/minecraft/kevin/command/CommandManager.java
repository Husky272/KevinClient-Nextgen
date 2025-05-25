package kevin.command;

import kevin.command.bind.BindClientCommandManager;
import kevin.command.commands.*;
import kevin.main.KevinClient;
import kevin.module.ClientModule;
import kevin.module.modules.misc.AdminDetector;
import kevin.module.modules.misc.AutoDisable;
import kevin.utils.ChatUtils;

import java.util.*;

public class CommandManager {
    private final Map<String[], IClientCommand> commands = new HashMap<>();
    private final String prefix = ".";

    public void load() {
        commands.put(new String[]{"t", "toggle"}, new ToggleClientCommand());
        commands.put(new String[]{"h", "help"}, new HelpClientCommand());
        commands.put(new String[]{"bind"}, new BindClientCommand());
        commands.put(new String[]{"friend"}, new FriendClientCommand());
        commands.put(new String[]{"binds"}, new BindsClientCommand());

        // Collect module names for dynamic command registration
        List<String> modulesCommand = new ArrayList<>();
        for (ClientModule m : KevinClient.moduleManager.getModules()) {
            modulesCommand.add(m.getName());
        }
        commands.put(modulesCommand.toArray(new String[0]), new ValueClientCommand());

        commands.put(new String[]{"say"}, new SayClientCommand());
        commands.put(new String[]{"login"}, new LoginClientCommand());
        commands.put(new String[]{"modulestate"}, new StateClientCommand());
        commands.put(new String[]{"skin"}, new SkinClientCommand());
        commands.put(new String[]{"cfg", "config"}, new ConfigClientCommand());
        commands.put(new String[]{"hide"}, new HideClientCommand());
        commands.put(new String[]{"AutoDisableSet"}, AutoDisable.INSTANCE);

        // Uncomment if needed
//         commands.put(new String[]{"ReloadScripts", "ReloadScript"}, ScriptManager);

        commands.put(new String[]{"Admin"}, AdminDetector.INSTANCE);
        commands.put(new String[]{"ClientTitle"}, ClientTitleClientCommand.INSTANCE);
        commands.put(new String[]{"DisableAllModule"}, DisableAllClientCommand.INSTANCE);
        commands.put(new String[]{"ClearMainConfig"}, new ClearMainConfigClientCommand());
        commands.put(new String[]{"font", "fonts"}, new FontClientCommand());
        commands.put(new String[]{"bindCommand"}, BindClientCommandManager.INSTANCE);
        commands.put(new String[]{"panic"}, new PanicClientCommand());
    }

    public boolean execCommand(String message) {
        if (!message.startsWith(prefix)) {
            return false;
        }
        String[] parts = message.split("\\s+");
        String commandKey = parts[0].substring(prefix.length()).toLowerCase();

        IClientCommand command = getCommand(commandKey);
        if (command != null) {
            List<String> args = new ArrayList<>(Arrays.asList(parts));
            args.remove(0); // remove the command keyword
            if (!(command instanceof ValueClientCommand)) {
                args.remove(0); // remove the command key if not a ValueClientCommand
            }
            command.run(args.toArray(new String[0]));
        } else {
            ChatUtils.message(KevinClient.getCStart() + " §l§4Command Not Found! Use .help for help");
        }
        return true;
    }

    public IClientCommand getCommand(String key) {
        for (Map.Entry<String[], IClientCommand> entry : commands.entrySet()) {
            for (String s : entry.getKey()) {
                if (s.equalsIgnoreCase(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public void registerCommand(String[] arr, IClientCommand commandObject) {
        commands.put(arr, commandObject);
    }
}