package kevin.module.modules.misc

import kevin.module.ClientModule
import kevin.module.ModuleCategory
import java.util.LinkedList


object ClientFriend : ClientModule("ClientFriend", "Allow your hack don't attack your friend.", ModuleCategory.MISC) {
    val friendsName = LinkedList<String>()

    fun isFriend(name: String) : Boolean = friendsName.contains(name.lowercase())
}