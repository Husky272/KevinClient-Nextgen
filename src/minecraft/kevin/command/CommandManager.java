/*
//不行，这玩意是炸的
package kevin.command;


import kevin.command.commands.*;
import kevin.main.KevinClient;
import kevin.module.ClientModule;
import kevin.module.modules.misc.AdminDetector;
import kevin.module.modules.misc.AutoDisable;
import kevin.utils.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final String prefix = ".";
    private final HashMap<String[], IClientCommand> commands = new HashMap<>();

    public void load() {
        commands.put(new String[]{"t", "toggle"}, new ToggleClientCommand());

        commands.put(new String[]{"h", "help"}, new HelpClientCommand());

        commands.put(new String[]{"bind"}, new BindClientCommand());

        commands.put(new String[]{"friend"}, new FriendClientCommand());

        commands.put(new String[]{"binds"}, new BindsClientCommand());

        // noinspection MismatchedQueryAndUpdateOfCollection
        ArrayList<String> modulesCommand = new ArrayList<>();
        for (ClientModule m : KevinClient.moduleManager.getModules()) {
            modulesCommand.add(m.getName());
        }
        commands.put(new String[]{"modulestate"}, new StateClientCommand());
        commands.put(new String[]{"skin"}, new SkinClientCommand());

        commands.put(new String[]{"cfg"}, new ConfigClientCommand());

        commands.put(new String[]{"say"}, new SayClientCommand());
        commands.put(new String[]{"login"}, new LoginClientCommand());

        commands.put(new String[]{"AutoDisableSet"}, AutoDisable.INSTANCE);
        //commands.put(new String[]{"ReloadScripts","ReloadScript"}, new ScriptManager);   // Java中的ArrayList不支持添加数组
        commands.put(new String[]{"Admin"}, AdminDetector.INSTANCE);

        commands.put(new String[]{"ClientTitle"}, ClientTitleClientCommand.INSTANCE);

        commands.put(new String[]{"DisableAllModule"}, DisableAllClientCommand.INSTANCE);

        commands.put(new String[]{"ClearMainConfig"}, new ClearMainConfigClientCommand());

        commands.put(new String[]{"font", "fonts"}, new FontClientCommand());

        commands.put(new String[]{"bindCommand"}, new BindClientCommand());

        commands.put(new String[]{"panic"}, new PanicClientCommand());
    }

    public boolean execCommand(String message) {
        if (!message.startsWith(prefix)) return false;
        String[] run = message.split(prefix, 2);
        if (run.length > 1) {
            String key = run[0];
            // noinspection SuspiciousMethodCalls
            IClientCommand command = commands.get(key.toLowerCase());
            if (command != null && !(command instanceof ValueClientCommand)) {
                command.run(Arrays.copyOfRange(run, 1, run.length));
            } else {
                ChatUtils.message("§l§4Command Not Found! Use .help for help");
            }
        } else {
            ChatUtils.message("§l§4Command Not Found! Use .help for help");
        }
        return true;
    }

    public IClientCommand getCommand(String key) {
        for (Map.Entry<String[], IClientCommand> entry : commands.entrySet()) {
            for (String s : entry.getKey()) {
                if (s.equalsIgnoreCase(key)) return entry.getValue();
            }
        }
        return null;
    }

    public void registerCommand(String[] arr, IClientCommand commandObject) {
        commands.put(arr, commandObject);
    }

}*/
