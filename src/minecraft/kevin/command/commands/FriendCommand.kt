package kevin.command.commands

import kevin.command.ICommand
import kevin.module.modules.misc.ClientFriend.friendsName
import kevin.utils.ChatUtils
import java.util.*

class FriendCommand : ICommand {
    override fun run(args: Array<String>) {
        if (args == null || args.size == 0) {
            ChatUtils.messageWithStart("§cUsage: §c.friend §c<add/remove> <player> §cor .friend§c list")
            return
        }
        val length = args.size
        val sub = args[0].lowercase(Locale.getDefault())

        val friends = friendsName
        if ("add".equals(sub, ignoreCase = true)) {
            if (length == 1) {
                ChatUtils.messageWithStart("§cUsage: §c.friend§c add <player>")
                return
            }
            friends.add(args[1].lowercase(Locale.getDefault()))
            ChatUtils.messageWithStart(args[1].lowercase(Locale.getDefault()))
            return
        }
        else if ("remove".equals(sub, ignoreCase = true)) {
            if (length == 1) {
                ChatUtils.messageWithStart("§cUsage: §c.friend§c remove <player>")
                return
            }
            friends.remove(args[1].lowercase(Locale.getDefault()))
            return
        }
        else if ("list".equals(sub, ignoreCase = true)) {
            if (friends.isEmpty()) {
                ChatUtils.messageWithStart("§cYou don't have any friend currently!")
                return
            }
            ChatUtils.messageWithStart("§cYour friends:\n")
            for (s in friends) {
                ChatUtils.messageWithStart("  §7- §3$s\n")
            }
            return
        }
        ChatUtils.messageWithStart("§cUsage: §c.friend §c<add/remove> <player> §cor .friend§c list")
    }
}
