// Decompiled with: CFR 0.152
// Class Version: 8
package kevin.via;

import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.platform.PlatformTask;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import kotlin.jvm.internal.Intrinsics;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public final class Platform
        implements ViaPlatform<UUID> {
    @NotNull
    private final JLoggerToLog4j logger;
    @NotNull
    private final File dataFolder;
    @NotNull
    private final ViaConfig config;
    @NotNull
    private final ViaAPI api;

    public Platform(@NotNull File dataFolder) {
        Intrinsics.checkNotNullParameter(dataFolder, "dataFolder");
        this.logger = new JLoggerToLog4j(LogManager.getLogger("ViaVersion"));
        Path configDir = dataFolder.toPath().resolve("ViaVersion");
        this.config = new ViaConfig(configDir.resolve("viaversion.yml").toFile());
        this.dataFolder = configDir.toFile();
        this.api = new ViaAPI();
    }

    private static Void runAsync$lambda$0(Throwable throwable) {
        Intrinsics.checkNotNullParameter(throwable, "throwable");
        if (!(throwable instanceof CancellationException)) {
            throwable.printStackTrace();
        }
        return null;
    }

    private static void runSync$lambda$1(Platform this$0, Runnable $runnable) {
        this$0.runSync($runnable);
    }

    private static void runRepeatingSync$lambda$2(Platform this$0, Runnable $runnable) {
        this$0.runSync($runnable);
    }

    private static void errorLogger$lambda$3(Future future) {
        Future future2 = future;
        Intrinsics.checkNotNull(future2);
        if (!future2.isCancelled() && future.cause() != null) {
            future.cause().printStackTrace();
        }
    }

    @NotNull
    public String legacyToJson(@Nullable String legacy) {
        GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();
        LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacySection();
        String string = legacy;
        // Anti NPE
        string = string == null ? "" : string;
        String object = gsonComponentSerializer.serialize(legacyComponentSerializer.deserialize(string));
        Intrinsics.checkNotNullExpressionValue(object, "serialize(...)");
        return object;
    }

    @NotNull
    public JLoggerToLog4j getLogger() {
        return this.logger;
    }

    @NotNull
    public String getPlatformName() {
        return "KevinClient";
    }

    @NotNull
    public String getPlatformVersion() {
        return String.valueOf(ViaVersion.getCLIENT_VERSION());
    }

    @NotNull
    public String getPluginVersion() {
        return "4.0.0";
    }

    @NotNull
    public FutureTaskId runAsync(@Nullable Runnable runnable) {
        CompletionStage completionStage = CompletableFuture.runAsync(runnable, ViaVersion.INSTANCE.getAsyncExecutor()).exceptionally(Platform::runAsync$lambda$0);
        Intrinsics.checkNotNullExpressionValue(completionStage, "exceptionally(...)");
        return new FutureTaskId((java.util.concurrent.Future) completionStage);
    }

    @NotNull
    public FutureTaskId runSync(@NotNull Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        EventLoop eventLoop = ViaVersion.INSTANCE.getEventLoop();
        Intrinsics.checkNotNull(eventLoop);
        Future future = eventLoop.submit(runnable).addListener(this.errorLogger());
        Intrinsics.checkNotNullExpressionValue(future, "addListener(...)");
        return new FutureTaskId(future);
    }

    @NotNull
    public PlatformTask<?> runSync(@NotNull Runnable runnable, long ticks) {
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        EventLoop eventLoop = ViaVersion.INSTANCE.getEventLoop();
        Intrinsics.checkNotNull(eventLoop);
        Future future = eventLoop.schedule(() -> Platform.runSync$lambda$1(this, runnable), ticks * (long) 50, TimeUnit.MILLISECONDS).addListener(this.errorLogger());
        Intrinsics.checkNotNullExpressionValue(future, "addListener(...)");
        return new FutureTaskId(future);
    }

    @NotNull
    public PlatformTask<?> runRepeatingSync(@NotNull Runnable runnable, long ticks) {
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        EventLoop eventLoop = ViaVersion.INSTANCE.getEventLoop();
        Intrinsics.checkNotNull(eventLoop);
        Future future = eventLoop.scheduleAtFixedRate(() -> Platform.runRepeatingSync$lambda$2(this, runnable), 0L, ticks * (long) 50, TimeUnit.MILLISECONDS).addListener(this.errorLogger());
        Intrinsics.checkNotNullExpressionValue(future, "addListener(...)");
        return new FutureTaskId(future);
    }

    private <T extends Future<?>> GenericFutureListener<T> errorLogger() {
        return Platform::errorLogger$lambda$3;
    }

    @NotNull
    public ViaCommandSender[] getOnlinePlayers() {
        return new ViaCommandSender[1145];
    }

    private ViaCommandSender[] getServerPlayers() {
        return new ViaCommandSender[1145];
    }

    public void sendMessage(@Nullable UUID uuid, @Nullable String s) {
    }

    public boolean kickPlayer(@Nullable UUID uuid, @Nullable String s) {
        return false;
    }

    public boolean isPluginEnabled() {
        return true;
    }

    @NotNull
    public ViaAPI getApi() {
        return this.api;
    }

    @NotNull
    public ViaConfig getConf() {
        return this.config;
    }

    @NotNull
    public ViaConfig getConfigurationProvider() {
        return this.config;
    }

    @NotNull
    public File getDataFolder() {
        return this.dataFolder;
    }

    public void onReload() {
    }

    @NotNull
    public JsonObject getDump() {
        return new JsonObject();
    }

    public boolean isOldClientsAllowed() {
        return true;
    }
}
