package kevin.command.commands

import kevin.command.ICommand
import kevin.utils.ChatUtils
import org.lwjgl.opengl.Display

object ClientTitleCommand : ICommand {
    /**
     * TODO: set the client's title
     * @author a114
     */
    override fun run(args: Array<out String>?) {
        if (args == null || args.isEmpty()) {
            ChatUtils.messageWithStart("L , Use \".ClientTitle <title>\"")
            return
        }
        else{
            var tmp: String = ""
            for (t in args) {
                tmp += " $t"
            }
            tmp = tmp.removePrefix(" ")
            Display.setTitle(tmp)
            ChatUtils.messageWithStart("Client's title has been set to <$tmp>")
        }
    }
}
