package kevin.command.commands;

import kevin.command.IClientCommand;
import kevin.main.KevinClient;
import kevin.utils.ChatUtils;

public class ReloadClientCommand implements IClientCommand {

    @Override
    public void run(String[] args) {
        ChatUtils.messageWithStart("Reloading! (Client might crashes)");
        KevinClient.INSTANCE.run();
    }
}
