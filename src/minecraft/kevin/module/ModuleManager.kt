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
@file:Suppress("UNCHECKED_CAST")

package kevin.module

import kevin.event.EventTarget
import kevin.event.KeyEvent
import kevin.event.Listenable
import kevin.main.KevinClient
import kevin.module.modules.Targets
import kevin.module.modules.combat.*
import kevin.module.modules.exploit.*
import kevin.module.modules.misc.*
import kevin.module.modules.movement.*
import kevin.module.modules.player.*
import kevin.module.modules.render.*
import kevin.module.modules.world.*

class ModuleManager : Listenable {

    private val clientModules = ArrayList<ClientModule>()

    private var combatList:ArrayList<ClientModule>? = null
    private var exploitList:ArrayList<ClientModule>? = null
    private var miscList:ArrayList<ClientModule>? = null
    private var movementList:ArrayList<ClientModule>? = null
    private var playerList:ArrayList<ClientModule>? = null
    private var renderList:ArrayList<ClientModule>? = null
    private var worldList:ArrayList<ClientModule>? = null

    val xRay by lazy { getModule(XRay::class.java) }

    fun load(){
        combatList = arrayListOf(
            AntiKnockback(),
            AutoArmor(),
            AutoBow(),
            AutoClicker(),
            AntiFireball(),
            AutoWeapon(),
            BackTrack(),
            BowAimbot(),
            BowAura(),
            Criticals(),
            FastBow(),
            HitBox(),
            KeepRange(),
            KillAura(),
            SuperKnockback(),
            TeleportAttack(),
            TimerRange
        )

        exploitList = arrayListOf(
            AbortBreaking(),
            AntiHunger(),
            ClientSpoof,
            Clip(),
            Disabler,
            ForceUnicodeChat(),
            Ghost(),
            GhostHand(),
            IllegalItems,
            KeepContainer(),
            Kick(),
            Log4j2(),
            MultiActions(),
            NoPitchLimit(),
            Phase(),
            PingSpoof(),
            Plugins(),
            PortalMenu(),
            //ServerCrasher(),
            ServerSidePacketDelayer(),
            TP(),
            VehicleOneHit()
        )

        miscList = arrayListOf(
            AdminDetector.INSTANCE,
            AntiBot,
//            AntiCrash,
            AntiInvalidBlockPlacement(),
            AntiShop(),
//            AntiLogSpam,
            AutoCommand(),
            AutoDisable,
            AutoL(),
            BadPacketPrevention(),
            ChatControl,
            ComponentOnHover,
            ConfigsManager,
            ClientFriend,
//            DiscordRPC(),
            Diastimeter(),
            HackDetector.INSTANCE,
            HideAndSeekHack,
            KillerDetector(),
            LagBackDetector(),
            NameProtect(),
            NoAchievement,
            NoCommand,
            NoRotateSet,
            NoScoreboard,
            PacketLogger(),
            PerformanceBooster,
            ResourcePackSpoof(),
            SuperSpammer(),
            Teams(),
            Translator
        )
        movementList = arrayListOf(
            AirJump(),
            AirLadder(),
            AntiVoid(),
            //BadSprint(),
            Fly(),
            Freeze(),
            HighJump(),
            InvMove(),
            LiquidWalk(),
            LongJump(),
            MoveCorrection(),
            NoClip(),
            NoJumpDelay,
            NoSlow(),
            NoWeb(),
            Parkour(),
            SafeWalk(),
            Speed(),
            Sprint(),
            Step(),
            Strafe(),
            TargetStrafe,
            VehicleJump(),
            WallClimb(),
            WaterSpeed()
        )
        playerList = arrayListOf(
            AntiAFK(),
            AntiCactus(),
            AutoFish(),
            AutoRespawn(),
            AutoSneak(),
            AutoTool(),
            Blink(),
//            CancelC03(),
            FastUse(),
            InventoryCleaner(),
            NoFall(),
            Reach(),
            Regen()
        )
        renderList = arrayListOf(
            Animations(),
            AntiBlind(),
            BlockESP(),
            BlockOverlay(),
            CameraClip(),
            CapeManager(),
            Chams,
            ClickGui(),
            ChinaHat(),
            Crosshair,
            DamageParticle(),
            ESP(),
            FreeCam(),
            FullBright(),
            HUD(),
            HudDesigner(),
            ItemESP(),
            NameTags(),
            NoBob(),
            NoFOV(),
            NoHurtCam(),
            NoSwing(),
            Particles,
            Projectiles(),
//            Renderer,
            RenderSettings,
            Rotations,
            SourceESP(),
            StorageESP(),
            TNTESP(),
            Tracers(),
            Trajectories(),
            TrueSight(),
            XRay()
        )
        worldList = arrayListOf(
            BlockFly,
            Breaker(),
            ChestStealer(),
            FastBreak(),
            FastPlace(),
            LightningDetector,
            LegitScaffold(),
            NoSlowBreak(),
            Nuker(),
            OldScaffold(),
            Scaffold(),
            ExperimentalBlockFly(),
            TeleportUse(),
            Timer(),
            World
        )

        clientModules.add(Targets())
        clientModules.addAll(combatList!!)
        clientModules.addAll(exploitList!!)
        clientModules.addAll(miscList!!)
        clientModules.addAll(movementList!!)
        clientModules.addAll(playerList!!)
        clientModules.addAll(renderList!!)
        clientModules.addAll(worldList!!)
        clientModules.forEach { KevinClient.eventManager.registerListener(it) }
        KevinClient.eventManager.registerListener(this)
    }

    fun getModules(): ArrayList<ClientModule>{
        return clientModules
    }

    fun getModuleByName(name: String): ClientModule?{
        for (module in clientModules){
            if (module.name.equals(name,ignoreCase = true))return module
        }
        return null
    }

    fun <T: ClientModule> getModule(module: Class<T>) = clientModules.first { it.javaClass == module } as T

    operator fun <T: ClientModule> get(module: Class<T>) = getModule(module)

    @EventTarget
    fun onKey(key: KeyEvent){
        for (module in clientModules){
            if (module.keyBind == key.key) module.toggle()
        }
    }

    override fun handleEvents(): Boolean {
        return true
    }

    fun registerModule(clientModule: ClientModule) {
        clientModules += clientModule
        KevinClient.eventManager.registerListener(clientModule)
    }

    fun unregisterModule(clientModule: ClientModule) {
        clientModules.remove(clientModule)
        KevinClient.eventManager.unregisterListener(clientModule)
    }
}
