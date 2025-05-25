package kevin.command.commands;

import kevin.command.IClientCommand;
import kevin.main.KevinClient;
import kevin.module.ClientModule;
import kevin.utils.ChatUtils;
import org.jetbrains.annotations.Nullable;

public final class DisableAllClientCommand implements IClientCommand {

    public static DisableAllClientCommand INSTANCE = new DisableAllClientCommand();

    public void run(@Nullable String[] args) {

        for(ClientModule mod : KevinClient.INSTANCE.getModuleManager().getModules()) {
            mod.setState(false);
        }

        ChatUtils.messageWithStart("§aDisabled all modules.");
    }
}
