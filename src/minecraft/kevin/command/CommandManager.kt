package kevin.command

import client.script.ScriptManager
import kevin.command.bind.BindClientCommandManager
import kevin.command.commands.*
import kevin.main.KevinClient
import kevin.module.modules.misc.AdminDetector
import kevin.module.modules.misc.AutoDisable
import kevin.utils.ChatUtils

class CommandManager {
    val commands = HashMap<Array<String>, IClientCommand>()

    private val prefix = "."

    fun load(){
        commands[arrayOf("t","toggle")] = ToggleClientCommand()

        commands[arrayOf("h","help")] = HelpClientCommand()

        commands[arrayOf("bind")] = BindClientCommand()

        commands[arrayOf("friend")] = FriendClientCommand()

        commands[arrayOf("binds")] = BindsClientCommand()

        val modulesCommand = arrayListOf<String>()
        for (m in KevinClient.moduleManager.getModules()) modulesCommand.add(m.name)
        commands[modulesCommand.toTypedArray()] = ValueClientCommand()

        commands[arrayOf("say")] = SayClientCommand()

        commands[arrayOf("login")] = LoginClientCommand()

        commands[arrayOf("modulestate")] = StateClientCommand()

        commands[arrayOf("skin")] = SkinClientCommand()

        commands[arrayOf("cfg", "config")] = ConfigClientCommand()

        commands[arrayOf("hide")] = HideClientCommand()

        commands[arrayOf("AutoDisableSet")] = AutoDisable

//        commands[arrayOf("ReloadScripts","ReloadScript")] = ScriptManager

        commands[arrayOf("Admin")] = AdminDetector.INSTANCE

        commands[arrayOf("ClientTitle")] = ClientTitleClientCommand

        commands[arrayOf("DisableAllModule")] = DisableAllClientCommand.INSTANCE

        commands[arrayOf("ClearMainConfig")] = ClearMainConfigClientCommand()

        commands[arrayOf("font", "fonts")] = FontClientCommand()

        commands[arrayOf("bindCommand")] = BindClientCommandManager

        commands[arrayOf("panic")] = PanicClientCommand()

    }

    fun execCommand(message: String): Boolean{
        if (!message.startsWith(prefix)) return false
        val run = message.split(prefix).size > 1
        if (run) {
            val second = message.removePrefix(prefix).split(" ")
            val list = ArrayList<String>()
            list.addAll(second)
            val key = list[0]
            val command = getCommand(key)
            if (command != null) {
                if (command !is ValueClientCommand) list.remove(key)
                command.run(list.toTypedArray())
            } else {
                ChatUtils.message("${KevinClient.cStart} §l§4Command Not Found! Use .help for help")
            }
        }else {
            ChatUtils.message("${KevinClient.cStart} §l§4Command Not Found! Use .help for help")
        }
        return true
    }

    fun getCommand(key: String): IClientCommand? {
        for (entry in commands.entries){
            for (s in entry.key){
                if (s.equals(key,ignoreCase = true)) return entry.value
            }
        }
        return null
    }

    fun registerCommand(arr: Array<String>, commandObject: IClientCommand) {
        commands[arr] = commandObject
    }
}