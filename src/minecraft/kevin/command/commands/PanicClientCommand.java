package kevin.command.commands;

import kevin.command.IClientCommand;
import kevin.main.MainUtil;
import kevin.utils.ChatUtils;

public class PanicClientCommand implements IClientCommand {
    @Override
    public void run(String[] args) {
        MainUtil.freeMemory();
    }

    public void showUsage(){
        ChatUtils.message(".panic");
    }
}
