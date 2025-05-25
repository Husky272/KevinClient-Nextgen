package kevin.command.commands;

import kevin.command.IClientCommand;
import kevin.utils.ChatUtils;

public class PanicClientCommand implements IClientCommand {
    @Override
    public void run(String[] args) {
        if(args.length < 1){
            showUsage();
            return;
        }
        try{
            int j = Integer.parseInt(args[1]);
            System.exit(j);
        } catch (NumberFormatException e){
            showUsage();
            return;
        }
    }

    public void showUsage(){
        ChatUtils.message(".panic [System.exit integer]");
    }
}
