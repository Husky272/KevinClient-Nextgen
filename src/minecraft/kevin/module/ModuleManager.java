package kevin.module;

import kevin.event.EventTarget;
import kevin.event.impl.KeyEvent;
import kevin.event.Listenable;
import kevin.main.KevinClient;
import kevin.module.modules.Targets;
import kevin.module.modules.combat.*;
import kevin.module.modules.exploit.*;
import kevin.module.modules.misc.*;
import kevin.module.modules.movement.*;
import kevin.module.modules.player.*;
import kevin.module.modules.render.*;
import kevin.module.modules.world.*;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public final class ModuleManager implements Listenable {
    @NotNull private final ArrayList<ClientModule> clientModules;
    @NotNull private final Lazy<XRay> xRay$delegate = LazyKt.lazy(() -> ModuleManager.xRay_delegate$lambda$0(this));

    @Nullable
    private ArrayList<ClientModule> combatList,exploitList, miscList,movementList,playerList,renderList,worldList;
    public ModuleManager() {
        /*SL:32*/ super();
        /*SL:34*/ this.clientModules = new ArrayList<ClientModule>();
    }

    private static XRay xRay_delegate$lambda$0(final ModuleManager this$0) {
        /*SL:44*/
        return this$0.getModule(XRay.class);
    }

    @NotNull
    public XRay getXRay() {
        return this.xRay$delegate.getValue();
    }

    public void load() {

        /*SL:47*/
        this.combatList = CollectionsKt.arrayListOf(
                /*EL:48*/new AntiKnockback(),
                /*EL:49*/new AutoArmor(),
                /*EL:50*/new AutoBow(),
                /*EL:51*/new AutoClicker(),
                /*EL:52*/new AntiFireball(),
                /*EL:53*/new AutoWeapon(),
                /*EL:54*/new BackTrack(),
                /*EL:55*/new BowAimbot(),
                /*EL:56*/new BowAura(),
                /*EL:57*/new Criticals(),
                /*EL:58*/new FastBow(),
                /*EL:59*/new HitBox(),
                /*EL:60*/new KeepRange(),
                /*EL:61*/new KillAura(),
                /*EL:62*/new SuperKnockback(),
                /*EL:63*/new TeleportAttack(),
                TimerRange.INSTANCE
        );

        /*SL:67*/
        this.exploitList = CollectionsKt.arrayListOf(
                /*EL:68*/new AbortBreaking(),
                /*EL:69*/new AntiHunger(),
                ClientSpoof.INSTANCE,
                /*EL:71*/new Clip(),
                Disabler.INSTANCE,
                /*EL:73*/new ForceUnicodeChat(),
                /*EL:74*/new Ghost(),
                /*EL:75*/new GhostHand(),
                IllegalItems.INSTANCE,
                /*EL:77*/new KeepContainer(),
                /*EL:78*/new Kick(),
                /*EL:79*/new Log4j2(),
                /*EL:80*/new MultiActions(),
                /*EL:81*/new NoPitchLimit(),
                /*EL:82*/new Phase(),
                /*EL:83*/new PingSpoof(),
                /*EL:84*/new Plugins(),
                /*EL:85*/new PortalMenu(),
                /*EL:87*/new ServerSidePacketDelayer(),
                /*EL:88*/new TP(),
                /*EL:89*/new VehicleOneHit()
        );

        /*SL:92*/
        this.miscList = CollectionsKt.arrayListOf(
                /*EL:93*/AdminDetector.INSTANCE,
                AntiBot.INSTANCE,
                /*EL:96*/new AntiInvalidBlockPlacement(),
                /*EL:97*/new AntiShop(),
                /*EL:99*/new AutoCommand(),
                AutoDisable.INSTANCE,
                /*EL:101*/new AutoL(),
                /*EL:102*/new BadPacketPrevention(),
                ChatControl.INSTANCE,
                ComponentOnHover.INSTANCE,
                ConfigsManager.INSTANCE,
                ClientFriend.INSTANCE,
                /*EL:108*/new Diastimeter(),
                HackDetector.INSTANCE,
                HideAndSeekHack.INSTANCE,
                /*EL:111*/new KillerDetector(),
                /*EL:112*/new LagBackDetector(),
                /*EL:113*/new NameProtect(),
                NoAchievement.INSTANCE,
                NoCommand.INSTANCE,
                NoRotateSet.INSTANCE,
                NoScoreboard.INSTANCE,
                /*EL:118*/new PacketLogger(),
                PerformanceBooster.INSTANCE,
                /*EL:120*/new ResourcePackSpoof(),
                /*EL:121*/new SuperSpammer(),
                /*EL:122*/new Teams(),
                Translator.INSTANCE
        );
        /*SL:125*/
        this.movementList = CollectionsKt.arrayListOf(
                /*EL:126*/new AirJump(),
                /*EL:127*/new AirLadder(),
                /*EL:128*/new AntiVoid(),
                /*EL:130*/new CoordinateStrafe(),
                /*EL:131*/new Fly(),
                /*EL:132*/new Freeze(),
                /*EL:133*/new HighJump(),
                /*EL:134*/new InvMove(),
                /*EL:135*/new LiquidWalk(),
                /*EL:136*/new LongJump(),
                /*EL:137*/new MoveCorrection(),
                /*EL:138*/new NoClip(),
                NoJumpDelay.INSTANCE,
                /*EL:140*/new NoSlow(),
                /*EL:141*/new NoWeb(),
                /*EL:142*/new Parkour(),
                /*EL:143*/new SafeWalk(),
                /*EL:144*/Speed.INSTANCE,
                /*EL:145*/new Sprint(),
                /*EL:146*/new Step(),
                /*EL:147*/new Strafe(),
                /*EL:148*/new TargetStrafe(),
                /*EL:149*/new VehicleJump(),
                /*EL:150*/new WallClimb(),
                /*EL:151*/new WaterSpeed()
        );

        /*SL:153*/
        this.playerList = CollectionsKt.arrayListOf(
                /*EL:154*/new AntiAFK(),
                /*EL:155*/new AntiCactus(),
                /*EL:156*/new AutoFish(),
                /*EL:157*/new AutoRespawn(),
                /*EL:158*/new AutoSneak(),
                /*EL:159*/new AutoTool(),
                /*EL:160*/new Blink(),
                /*EL:162*/new FastUse(),
                /*EL:163*/new InventoryCleaner(),
                /*EL:164*/new NoFall(),
                /*EL:165*/new Reach(),
                /*EL:166*/new Regen()
        );

        /*SL:168*/
        this.renderList = CollectionsKt.arrayListOf(
                /*EL:169*/new Animations(),
                /*EL:170*/new AntiBlind(),
                /*EL:171*/new BlockESP(),
                /*EL:172*/new BlockOverlay(),
                /*EL:173*/new CameraClip(),
                /*EL:174*/new CapeManager(),
                Chams.INSTANCE,
                /*EL:176*/new ClickGui(),
                /*EL:177*/new ChinaHat(),
                Crosshair.INSTANCE,
                /*EL:179*/new DamageParticle(),
                /*EL:180*/new ESP(),
                new FireFlies(),
                /*EL:181*/new FreeCam(),
                /*EL:182*/new FullBright(),
                /*EL:183*/new HUD(),
                /*EL:184*/new HudDesigner(),
                /*EL:185*/new ItemESP(),
                new LineGlyphs(),
                /*EL:186*/new NameTags(),
                /*EL:187*/new NoBob(),
                /*EL:188*/new NoFOV(),
                /*EL:189*/new NoHurtCam(),
                /*EL:190*/new NoSwing(),
                Particles.INSTANCE,
                /*EL:192*/new Projectiles(),
                RenderSettings.INSTANCE,
                Rotations.INSTANCE,
                /*EL:196*/new SourceESP(),
                /*EL:197*/new StorageESP(),
                /*EL:198*/new TNTESP(),
                /*EL:199*/new Tracers(),
                /*EL:200*/new Trajectories(),
                /*EL:201*/new TrueSight(),
                /*EL:202*/new XRay()
        );

        /*SL:204*/
        this.worldList = CollectionsKt.arrayListOf(
                /*EL:205*/BlockFly.INSTANCE,
                /*EL:206*/new Fucker(),
                /*EL:207*/new ChestStealer(),
                /*EL:208*/new FastBreak(),
                /*EL:209*/new FastPlace(),
                LightningDetector.INSTANCE,
                /*EL:211*/new LegitScaffold(),
                /*EL:212*/new NoSlowBreak(),
                /*EL:213*/new Nuker(),
                /*EL:214*/new OldScaffold(),
                /*EL:215*/new Scaffold(),
                /*EL:216*/new ExperimentalBlockFly(),
                /*EL:217*/new TeleportUse(),
                /*EL:218*/new Timer(),
                World.INSTANCE
        );
        /*SL:222*/
        this.clientModules.add(new Targets());
        final ArrayList<ClientModule> clientModules = /*EL:223*/this.clientModules;
        final ArrayList<ClientModule> combatList = this.combatList;
        Intrinsics.checkNotNull(combatList);
        clientModules.addAll(combatList);

        final ArrayList<ClientModule> clientModules2 = /*EL:224*/this.clientModules;
        final ArrayList<ClientModule> exploitList = this.exploitList;
        Intrinsics.checkNotNull(exploitList);
        clientModules2.addAll(exploitList);

        final ArrayList<ClientModule> clientModules3 = /*EL:225*/this.clientModules;
        final ArrayList<ClientModule> miscList = this.miscList;
        Intrinsics.checkNotNull(miscList);
        clientModules3.addAll(miscList);

        final ArrayList<ClientModule> clientModules4 = /*EL:226*/this.clientModules;
        final ArrayList<ClientModule> movementList = this.movementList;
        Intrinsics.checkNotNull(movementList);
        clientModules4.addAll(movementList);

        final ArrayList<ClientModule> clientModules5 = /*EL:227*/this.clientModules;
        final ArrayList<ClientModule> playerList = this.playerList;
        Intrinsics.checkNotNull(playerList);
        clientModules5.addAll(playerList);

        final ArrayList<ClientModule> clientModules6 = /*EL:228*/this.clientModules;
        final ArrayList<ClientModule> renderList = this.renderList;
        Intrinsics.checkNotNull(renderList);
        clientModules6.addAll(renderList);

        final ArrayList<ClientModule> clientModules7 = /*EL:229*/this.clientModules;
        final ArrayList<ClientModule> worldList = this.worldList;
        Intrinsics.checkNotNull(worldList);
        clientModules7.addAll(worldList);

        final Iterable<ClientModule> $this$forEach$iv = /*EL:230*/this.clientModules;
        /*SL:272*/
        for (final ClientModule element$iv : $this$forEach$iv) {
            KevinClient.INSTANCE.getEventManager().registerListener(element$iv);
        }
        KevinClient.INSTANCE.getEventManager().registerListener(this);
    }

    @NotNull
    public ArrayList<ClientModule> getModules() {
        /*SL:235*/
        return this.clientModules;
    }

    @Nullable
    public ClientModule getModuleByName(@NotNull final String name) {
        /*EL:239*/
        for (ClientModule next : this.clientModules) {
            /*SL:240*/
            if (StringsKt.equals(next.getName(), name, true)) {
                return next;
            }
        }
        /*SL:242*/
        return null;
    }

    @NotNull
    public <T extends ClientModule> T getModule(@NotNull final Class<T> module) {
        Intrinsics.checkNotNullParameter(module, "module");
        /*EL:246*/
        /*SL:274*/
        for (final ClientModule element$iv : this.clientModules) {
            if (Intrinsics.areEqual(element$iv.getClass(), module)) {
                Intrinsics.checkNotNull(element$iv, "null cannot be cast to non-null type T of kevin.module.ModuleManager.getModule");
                return (T) element$iv;
            }
        }
        /*SL:275*/
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <T extends ClientModule> T get(@NotNull final Class<T> module) {
        /*SL:248*/
        return (T) this.getModule((Class<ClientModule>) module);
    }

    @EventTarget
    public void onKey(@NotNull final KeyEvent key) {
        final Iterator<ClientModule> iterator = /*EL:252*/this.clientModules.iterator();
        while (iterator.hasNext()) {
            final ClientModule next = iterator.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            /*SL:253*/
            if (next.getKeyBind() == key.getKey()) {
                next.toggle();
            }
        }
    }

    public boolean handleEvents() {
        /*SL:258*/
        return true;
    }

    public void registerModule(@NotNull final ClientModule clientModule) {
        /*SL:262*/
        this.clientModules.add(clientModule);
        KevinClient.INSTANCE.getEventManager().registerListener(/*EL:263*/clientModule);
    }

    public void unregisterModule(@NotNull final ClientModule clientModule) {
        /*SL:267*/
        this.clientModules.remove(clientModule);
        KevinClient.INSTANCE.getEventManager().unregisterListener(/*EL:268*/clientModule);
    }
}
