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
package kevin.utils.proxy

import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import kevin.main.KevinClient
import kevin.main.KevinClient.pool
import kevin.utils.ServerUtils
import kevin.utils.ServerUtils.sendGet
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import org.lwjgl.input.Keyboard
import java.lang.System.nanoTime
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.HashSet
import java.util.LinkedList
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicInteger
import javax.swing.JOptionPane
import javax.swing.UIManager

class GuiProxySelect(private val prevGui: GuiScreen) : GuiScreen() {

    private lateinit var textField: GuiTextField
    private lateinit var type: GuiButton
    private lateinit var stat: GuiButton
    private lateinit var auth: GuiButton

    override fun initGui() {
        Keyboard.enableRepeatEvents(true)
        textField = GuiTextField(3, mc.fontRendererObj, width / 2 - 100, 60, 200, 20)
        textField.isFocused = true
        textField.text = ProxyManager.proxy
        textField.maxStringLength = 114514
        buttonList.add(GuiButton(1, width / 2 - 100, height / 4 + 76, "").also { type = it })
        buttonList.add(GuiButton(2, width / 2 - 100, height / 4 + 100, "").also { stat = it })
        buttonList.add(GuiButton(3, width / 2 - 100, height / 4 + 124, "SyncAuthServiceProxy").also { auth = it })
        buttonList.add(GuiButton(4, width / 2 - 100, height / 4 + 172, "FreeProxy"))
        buttonList.add(GuiButton(0, width / 2 - 100, height / 4 + 148, "Back"))
        updateButtonStat()
    }

    private fun updateButtonStat() {
        type.displayString = "Type: " + ProxyManager.proxyType.name
        stat.displayString = "Status: " + if (ProxyManager.isEnable) "§aEnabled" else "§cDisabled"
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawBackground(0)
        drawCenteredString(mc.fontRendererObj, "Proxy Manager", width / 2, 34, 0xffffff)
        textField.drawTextBox()
        if (textField.text.isEmpty() && !textField.isFocused) {
            drawString(mc.fontRendererObj, "§7Address", width / 2 - 100, 66, 0xffffff)
        }
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                ProxyManager.proxy = textField.text
                mc.displayGuiScreen(prevGui)
            }
            1 -> {
                when(ProxyManager.proxyType) {
                    Proxy.Type.SOCKS -> ProxyManager.proxyType = Proxy.Type.HTTP
                    Proxy.Type.HTTP -> ProxyManager.proxyType = Proxy.Type.SOCKS
                    else -> throw IllegalStateException("Proxy type is not supported!")
                }
            }
            2 -> {
                ProxyManager.isEnable = !ProxyManager.isEnable
            }
            3 -> {
                mc.sessionService = YggdrasilAuthenticationService(if (ProxyManager.isEnable) ProxyManager.proxyInstance else Proxy.NO_PROXY, UUID.randomUUID().toString()).createMinecraftSessionService()
                auth.displayString = "Auth service's proxy was set to: ${if (ProxyManager.isEnable) "§aEnabled" else "§cDisabled"}"
            }
            4 -> {
                FreeProxyManager.update(this)
            }
        }
        updateButtonStat()
    }

    override fun onGuiClosed() {
        ProxyManager.proxy = textField.text
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (Keyboard.KEY_ESCAPE == keyCode) {
            mc.displayGuiScreen(prevGui)
            return
        }

        if (textField.isFocused) {
            textField.textboxKeyTyped(typedChar, keyCode)
        }

        super.keyTyped(typedChar, keyCode)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        textField.mouseClicked(mouseX, mouseY, mouseButton)
        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    override fun updateScreen() {
        textField.updateCursorCounter()
        super.updateScreen()
    }

    data object FreeProxyManager {
        private val proxies = LinkedList<Pair<Proxy, String>>()
        private var initialized = false
        private val pings = ConcurrentHashMap<String, String>()
        private fun init() {
            if (proxies.isEmpty() && !initialized) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                initialized = true
                val sockets = arrayOf(
                    "https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks4.txt",
                    "https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/socks5.txt",
                    "https://raw.githubusercontent.com/zloi-user/hideip.me/main/socks4.txt",
                    "https://raw.githubusercontent.com/zloi-user/hideip.me/main/socks5.txt"
                )
                var i = 0

                for (it in sockets) {
                    var get = sendGet(it)
                    if (get.second != 0) {
                        get = sendGet(it.replace("https://raw.githubusercontent.com/", "https://raw.fgit.cf/"))
                    }
                    if (get.second != 0) continue
                    val s = get.first ?: continue
                    Minecraft.logger.info("[Proxy] got socks proxies from $it")
                    for (s1 in HashSet(s.split("\n"))) {
                        val split = s1.split(":")
                        if (split.size == 1) continue
                        proxies.add(Proxy(Proxy.Type.SOCKS, InetSocketAddress(split[0], split[1].toInt())) to "SOCKET ${if (split.size > 2) split[2] else "unknown country"} ${i++}")
                    }
                }
                val https = arrayOf(
                    "https://raw.githubusercontent.com/zloi-user/hideip.me/main/http.txt",
                    "https://raw.githubusercontent.com/TheSpeedX/SOCKS-List/master/http.txt"
                )
                for (url in https) {
                    var http = sendGet(url)
                    if (http.second != 0) {
                        http = sendGet(url.replace("https://raw.githubusercontent.com/", "https://raw.fgit.cf/"))
                    }
                    if (http.second == 0) {
                        Minecraft.logger.info("[Proxy] got http proxies from $url")
                        for (s in HashSet(http.first?.split("\n")?: continue)) {
                            val split = s.split(":")
                            if (split.size == 1) continue
                            proxies.add(Proxy(Proxy.Type.HTTP, InetSocketAddress(split[0], split[1].toInt())) to "HTTP ${if (split.size > 2) split[2] else "unknown country"} ${i++}")
                        }
                    }
                }
                ping()
            }
        }

        fun ping() {
            val brr = ArrayList(proxies)
            brr.shuffle()
            val arr = ConcurrentLinkedDeque(brr)
            // I know, it isn't smart to create a lot of threads
            pool.execute {
                while (arr.isNotEmpty()) {
                    val proxy = arr.poll()
                    pool.execute {
                        ping0(proxy)
                    }
                    Thread.sleep(25)
                }
            }
        }

        private fun ping0(proxy: Pair<Proxy, String>?) {
            proxy ?: return
            val start = nanoTime() / 1000000
            val result = sendGet("http://www.vpngate.net/api/iphone/", proxy.first)
            val end = nanoTime() / 1000000
            val ping = end - start
            if (result.second == 0) {
                pings[proxy.second] = "${ping}ms"
            } else {
                pings[proxy.second] = "time out"
            }
        }

        fun update(gui: GuiProxySelect) {
            init()
            if (proxies.isEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to get proxy list",
                    "Proxy selector",
                    JOptionPane.INFORMATION_MESSAGE
                )
                return
            }
            val str = JOptionPane.showInputDialog(
                null,
                "Select proxy:\n",
                "Proxy selector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                proxies.map { "${pings.getOrDefault(it.second, "no ping")}| ${it.second}" }.filter { !it.endsWith("time out", true) }.sorted().toTypedArray(),
                null
            ) as String
            proxies.find { str.endsWith(it.second, true) }?.apply {
                ProxyManager.proxyType = first.type()
                gui.textField.text = first.address().toString().run {
                    this.substring(this.indexOf("/") + 1, this.length)
                }
            }
        }
    }
}