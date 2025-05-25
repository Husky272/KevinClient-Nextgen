package kevin.command.commands;


import kevin.command.IClientCommand;
import kevin.utils.ChatUtils;

// Grab cloudflare warp wireguard configuration and print to chat
// TODO: Implement it
public class WGCFClientCommand implements IClientCommand {

    @Override
    public void run(String[] args) {
        ChatUtils.message("没实现呢，别急");
    }
}