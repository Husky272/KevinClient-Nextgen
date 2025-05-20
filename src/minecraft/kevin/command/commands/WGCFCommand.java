package kevin.command.commands;


import kevin.command.ICommand;

public class WGCFCommand implements ICommand {

    @Override
    public void run(String[] args) {
        Thread t = new Thread(()->{

        });
        t.start();
    }


}