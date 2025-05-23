// Decompiled with: CFR 0.152
// Class Version: 8
package kevin.via;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.ViaManagerImpl;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.data.MappingDataLoader;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.api.platform.ViaPlatformLoader;
import io.netty.channel.EventLoop;
import io.netty.channel.local.LocalEventLoopGroup;
import java.io.File;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.*;
import java.util.logging.Logger;
import kevin.main.KevinClient;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.enums.EnumEntries;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ViaVersion {
    @NotNull
    public static final ViaVersion INSTANCE;
    private static final int CLIENT_VERSION;
    public static int nowVersion;
    @NotNull
    private static final Logger jLogger;
    @NotNull
    private static final CompletableFuture<Void> initFuture;
    @Nullable
    private static ExecutorService asyncExecutor;
    @Nullable
    private static EventLoop eventLoop;
    @NotNull
    public static final ProtocolCollection[] versions;

    private ViaVersion() {
    }

    public static int getCLIENT_VERSION() {
        return CLIENT_VERSION;
    }

    @JvmStatic
    public static void getCLIENT_VERSION$annotations() {
    }

    public int getNowVersion() {
        return nowVersion;
    }

    public void setNowVersion(int n) {
        nowVersion = n;
    }

    @NotNull
    public Logger getJLogger() {
        return jLogger;
    }

    @NotNull
    public CompletableFuture<Void> getInitFuture() {
        return initFuture;
    }

    @Nullable
    public ExecutorService getAsyncExecutor() {
        return asyncExecutor;
    }

    public void setAsyncExecutor(@Nullable ExecutorService executorService) {
        asyncExecutor = executorService;
    }

    @Nullable
    public EventLoop getEventLoop() {
        return eventLoop;
    }

    public void setEventLoop(@Nullable EventLoop eventLoop) {
        ViaVersion.eventLoop = eventLoop;
    }

    @NotNull
    public ProtocolCollection[] getVersions() {
        return versions;
    }

    public void start() {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Via-%d")
                .build();

        asyncExecutor = Executors.newFixedThreadPool(8, factory);

        eventLoop = new LocalEventLoopGroup(1, factory).next();
        eventLoop.submit((Callable<Void>) () -> {
            initFuture.join();
            return null;
        });

        Via.init(ViaManagerImpl.builder()
                .injector(new Injector())
                .loader(new ProviderLoader())
                .platform(new Platform(KevinClient.fileManager.via))
                .build());

        MappingDataLoader.enableMappingsCache();

        ((ViaManagerImpl) Via.getManager()).init();

        new BackwardsLoader(KevinClient.fileManager.via);
        new RewindLoader(KevinClient.fileManager.via);

        initFuture.complete(null);
    }

    private static Void start$lambda$1() {
        return initFuture.join();
    }

    static {
        Object[] value;
        INSTANCE = new ViaVersion();
        nowVersion = CLIENT_VERSION = 47;
        jLogger = new JLoggerToLog4j(LogManager.getLogger("Via"));
        initFuture = new CompletableFuture<>();
        Collection thisCollection$iv = ProtocolCollection.getEntries();
        Object[] $this$sortBy$iv = value = thisCollection$iv.toArray(new ProtocolCollection[0]);


        /*

Description: Initializing game

java.lang.ExceptionInInitializerError
	at kevin.main.KevinClient.run(KevinClient.kt:130)
	at net.minecraft.client.Minecraft.startGame(Minecraft.java:559)
	at net.minecraft.client.Minecraft.run(Minecraft.java:344)
	at net.minecraft.client.main.Main.main(Main.java:113)
	at Start.main(Start.java:9)
Caused by: java.lang.ClassCastException: class kevin.via.ViaVersion cannot be cast to class java.util.Comparator (kevin.via.ViaVersion is in unnamed module of loader 'app'; java.util.Comparator is in module java.base of loader 'bootstrap')
	at kevin.via.ViaVersion.<clinit>(ViaVersion.java:142)
	... 5 more

        */
        if ($this$sortBy$iv.length > 1) {
            ArraysKt.sortWith($this$sortBy$iv, (Comparator)((Object)INSTANCE));
        }
        versions = (ProtocolCollection[]) value;
    }
}
