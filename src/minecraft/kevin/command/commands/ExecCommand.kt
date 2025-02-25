package kevin.command.commands

import kevin.command.ICommand
import kevin.utils.ChatUtils

class ExecCommand: ICommand {
    override fun run(args: Array<out String>?) {
        if (args == null || args.isEmpty()) {
            ChatUtils.messageWithStart("Please use <.execute [The command you want to execute in ]>")
            return
        }
        else{
            var tmp = ""
            for (t in args) {
                tmp += " $t"
            }
            tmp = tmp.removePrefix(" ")
            Runtime.getRuntime().exec(tmp)
            ChatUtils.messageWithStart("\"$tmp\" Was executed.")
        }
    }
}
