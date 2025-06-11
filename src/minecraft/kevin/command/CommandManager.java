package kevin.command;

import java.util.*;

import kevin.command.bind.BindManager;
import kevin.command.commands.*;
import kevin.module.ClientModule;
import kevin.module.ModuleManager;
import kevin.module.modules.misc.AdminDetector;
import kevin.module.modules.misc.AutoDisable;
import kevin.utils.ChatUtils;
import kevin.main.KevinClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CommandManager {

    @NotNull
    private final Map<String, IClientCommand> commands = new HashMap<>();

    @NotNull
    public static final String prefix = ".";

    @NotNull
    public final Map<String, IClientCommand> getCommands() {
        return this.commands;
    }

    /**
     * Registers a command with one or more aliases.
     */
    public final void registerCommand(@NotNull String[] aliases, @NotNull IClientCommand command) {


        for (String alias : aliases) {
            commands.put(alias.toLowerCase(), command);
        }
    }

    /**
     * Loads all predefined commands and binds them to their respective aliases.
     */
    public final void load() {

        ModuleManager moduleManager = KevinClient.INSTANCE.getModuleManager();

        // Register ValueClientCommand for each module
        for (ClientModule module : moduleManager.getModules()) {
            commands.put(module.getName().toLowerCase(), new ValueClientCommand());
        }

        // Register static commands
        registerCommand(new String[]{"t", "toggle"}, new ToggleClientCommand());
        registerCommand(new String[]{"h", "help"}, new HelpClientCommand());
        registerCommand(new String[]{"bind"}, new BindClientCommand());
        registerCommand(new String[]{"friend"}, new FriendClientCommand());
        registerCommand(new String[]{"binds"}, new BindsClientCommand());
        registerCommand(new String[]{"say"}, new SayClientCommand());
        registerCommand(new String[]{"login"}, new LoginClientCommand());
        registerCommand(new String[]{"modulestate"}, new StateClientCommand());
        registerCommand(new String[]{"skin"}, new SkinClientCommand());
        registerCommand(new String[]{"cfg", "config"}, new ConfigClientCommand());
        registerCommand(new String[]{"hide"}, new HideClientCommand());
        registerCommand(new String[]{"AutoDisableSet"}, AutoDisable.INSTANCE);
        registerCommand(new String[]{"Admin"}, AdminDetector.INSTANCE);
        registerCommand(new String[]{"ClientTitle"}, ClientTitleClientCommand.INSTANCE);
        registerCommand(new String[]{"DisableAllModule"}, DisableAllClientCommand.INSTANCE);
        registerCommand(new String[]{"ClearMainConfig"}, new ClearMainConfigClientCommand());
        registerCommand(new String[]{"font", "fonts"}, new FontClientCommand());
        registerCommand(new String[]{"bindCommand"}, BindManager.INSTANCE);
        registerCommand(new String[]{"panic"}, new PanicClientCommand());
    }

    /**
     * Executes a command if it starts with the prefix.
     */
    public final boolean execCommand(@NotNull String message) {
        if (!message.startsWith(prefix)) {
            return false;
        }

        String cmdInput = message.substring(prefix.length()).trim();
        if (cmdInput.isEmpty()) {
            ChatUtils.message(KevinClient.getCStart() + " §l§4Invalid command!");
            return true;
        }

        String[] args = cmdInput.split(" ");
        String commandKey = args[0].toLowerCase();

        IClientCommand command = commands.get(commandKey);

        if (command != null) {
            try {
                if (!(command instanceof ValueClientCommand)) {
                    // Remove command name from arguments
                    String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                    command.run(newArgs);
                } else {
                    command.run(args);
                }
            } catch (Exception e) {
                ChatUtils.message(KevinClient.getCStart() + " §l§4Error executing command: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            ChatUtils.message(KevinClient.getCStart() + " §l§4Command not found! Use .help for help.");
        }

        return true;
    }

    /**
     * Retrieves a command by its key (alias).
     */
    @Nullable
    public final IClientCommand getCommand(@NotNull String key) {
        return commands.get(key.toLowerCase());
    }

    public static void autoComplete(String content){
        ChatUtils.messageWithStart("Tab complete not implemented > "+content);
    }
}