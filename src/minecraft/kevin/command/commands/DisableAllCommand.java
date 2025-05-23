package kevin.command.commands;

import kevin.command.ICommand;
import kevin.main.KevinClient;
import kevin.module.ClientModule;
import kevin.utils.ChatUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.SourceDebugExtension;
import org.jetbrains.annotations.Nullable;

public final class DisableAllCommand implements ICommand {

    public static DisableAllCommand INSTANCE = new DisableAllCommand();

    public void run(@Nullable String[] args) {

        for(ClientModule mod : KevinClient.INSTANCE.getModuleManager().getModules()) {
            mod.setState(false);
        }

        ChatUtils.messageWithStart("§aDisabled all modules.");
    }
}
