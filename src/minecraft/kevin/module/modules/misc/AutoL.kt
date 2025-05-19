/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kevin.module.modules.misc

import kevin.event.AttackEvent
import kevin.event.EventTarget
import kevin.event.UpdateEvent
import kevin.event.WorldEvent
import kevin.main.KevinClient
import kevin.module.*
import net.minecraft.entity.player.EntityPlayer
import java.io.File
import java.io.FileFilter

class AutoL : ClientModule("AutoL","Send messages automatically when you kill a player.", ModuleCategory.MISC) {
    //从文件夹加载
    private val modeList = arrayListOf("Single", "SkidMa", "Cum")

    private val fileSuffix = ".txt"
    private val messageFiles: Array<out File>?
    init {
        val files = KevinClient.fileManager.killMessages
        messageFiles = files.listFiles(FileFilter { it.name.endsWith(fileSuffix) })
        if (messageFiles != null) for (i in messageFiles) modeList.add(i.name.split(fileSuffix)[0]) }

    private val modeValue = ListValue("Mode", modeList.toTypedArray(),"Single")
    private val prefix = ListValue("Prefix", arrayOf("None","/shout",".","@","!","Custom"), "None")
    private val customPrefix = TextValue("CustomPrefix", "")
    private val singleMessage = TextValue("SingleMessage","%MyName killed %name")
    private val flood = FloatValue("Flood", 1f, 0f, 5f)

    //攻击目标列表
    private val entityList = arrayListOf<EntityPlayer>()

    //在世界切换时清空攻击目标列表
    @EventTarget fun onWorld(event: WorldEvent) = entityList.clear()

    //在攻击时 如果 攻击目标是玩家 且 攻击目标不在列表内 将目标添加进列表
    @EventTarget fun onAttack(event: AttackEvent) {
        if (event.targetEntity is EntityPlayer && event.targetEntity !in entityList)
            entityList.add(event.targetEntity)
    }
    @EventTarget fun onUpdate(event: UpdateEvent) {
        //如果玩家死亡 发送消息并从列表移除
        entityList.filter { it.isDead }.forEach { entityPlayer ->
            val text = if (modeValue equal "Single") {
                singleMessage.get()
            } else if (modeValue equal "SkidMa") {
                skidMaMSG.random()
            }else if (modeValue equal "Cum"){
                cumMSG.random()
            }
            else {
                messageFiles!!.first { it.name.replace(fileSuffix,"") == modeValue.get() }.readLines().random()
            }
            mc.thePlayer.sendChatMessage(
                addPrefix(text).
                replace("%MyName",mc.thePlayer.name).
                replace("%name",entityPlayer.name.repeat(flood.toString() .toFloat() .toInt()) )
            );entityList.remove(entityPlayer)
        }
    }
    private fun addPrefix(message: String) =
        when(prefix.get()) {
            "/shout" -> "/shout $message"
            "." -> ".say $message"
            "@" -> "@$message"
            "!" -> "!$message"
            "Custom" -> "$customPrefix$message"
            else -> message
        }


    private val skidMaMSG = arrayOf(
        "Sigma made this world a better place, killing you with it even more",
        "My whole life changed since I discovered Sigma",
        "Learn your alphabet with the Sigma client: Panda, Sigma, Epsilon, Alpha!",
        "Why Sigma? Cause it is the addition of pure skill and incredible intellectual abilities",
        "Sigma Client users be like: Hit or miss I guess I never miss!",
        "Stop it, get some help! Get Sigma",
        "Hypixel wants to know Sigma Client owner's location [Accept] [Deny]",
        "I don't miss hit, i see you miss that",
        "I don't hack i just have Sigma Gaming Chair",
        "How are you so bad? just practice your aim and hold w",
        "Did I really just forget that melody? Si sig sig sig Sigma"
    )

    private val cumMSG = arrayOf(
        "呐呐~杂鱼哥哥不会这样就被捉弄的不会说话了吧 真是弱哎~",
        "嘻嘻~杂鱼哥哥不会以为竖个大拇哥就能欺负我了吧~不会吧~不会吧~杂鱼哥哥怎么可能欺负",
        "哥哥真是好欺负啊~嘻嘻~",
        "哎~杂鱼说话就是无趣唉~只会发一张表情包的笨蛋到处都有吧",
        "呐呐~杂鱼哥哥发这个是想教育我吗~嘻嘻~怎么可能啊~",
        "欸？你这个杂鱼~又来问我问题了吗",
        "你这种杂鱼~怎么有资格和我说话？",
        "哥哥这么短~根本没感觉的好吧!",
        "我就是喜欢捉弄这样笨笨的大叔哦~",
        "想带我走?大叔不会是想做色涩的事情吧~",
        "大叔是没有萝莉控吗？要不然怎么天天围着我转呀，好恶心~",
    )
}
