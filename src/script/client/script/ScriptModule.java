package client.script;

import kevin.event.*;
import kevin.event.impl.*;
import kevin.event.impl.PushOutEvent;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kevin.module.Value;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyString;

import java.util.HashMap;
import java.util.LinkedHashMap;

@SuppressWarnings("StringConcatenationArgumentToLogCall")
// Please do not explode
public final class ScriptModule extends ClientModule {
    @NotNull
    private final HashMap<String, PyObject> events;
    @NotNull
    private final LinkedHashMap<String, Value<?>> _values;
    @NotNull
    private final HashMap<String, Value<?>> settings$delegate;
    @Nullable
    private String _tag;

    public ScriptModule(@NotNull String name, @NotNull String description, @NotNull ModuleCategory category, @NotNull PyObject moduleObject) {
        // Call to 'super()' must be first statement in constructor body
/*
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(description, "description");
        Intrinsics.checkNotNullParameter(category, "category");
        Intrinsics.checkNotNullParameter(moduleObject, "moduleObject");

*/

        super(name, description, category);
        this.events = new HashMap<String, PyObject>();
        this._values = new LinkedHashMap<String, Value<?>>();
        this.settings$delegate = settings_delegate$lambda$0(this);

        if (((PyDictionary) moduleObject).has_key(new PyString("settings"))) {

            Object var11 = ((PyDictionary) moduleObject).get("settings");

            Intrinsics.checkNotNull(var11, "null cannot be cast to non-null type org.python.core.PyDictionary");
            PyDictionary settings = (PyDictionary) var11;

            for (Object settingName : settings.keySet()) {
                String var9 = String.valueOf(settingName);
                var11 = settings.get(settingName);
                Intrinsics.checkNotNull(var11, "null cannot be cast to non-null type kevin.module.Value<*>");
                Value var10 = (Value) var11;
                this._values.put(var9, var10);
            }
        }

        if (((PyDictionary) moduleObject).has_key(new PyString("tag"))) {
            Object var10001 = ((PyDictionary) moduleObject).get("tag");
            Intrinsics.checkNotNull(var10001, "null cannot be cast to non-null type kotlin.String");
            this._tag = (String) var10001;
        }

    }

    // $FF: synthetic method
    static void callEvent$default(ScriptModule var0, String var1, Object var2, int var3, Object var4) {
        if ((var3 & 2) != 0) {
            var2 = null;
        }

        var0.callEvent(var1, var2);
    }

    private static LinkedHashMap<String, Value<?>> settings_delegate$lambda$0(ScriptModule this$0) {
        return this$0._values;
    }

    @NotNull
    public LinkedHashMap getSettings() {
        return (LinkedHashMap) this.settings$delegate.values();
    }

    @Nullable
    public String getTag() {
        return this._tag;
    }

    public void setTag(@NotNull String v) {
        Intrinsics.checkNotNullParameter(v, "v");
        this._tag = v;
    }

    public void on(@NotNull String event, @NotNull PyObject pyObject) {

        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(pyObject, "pyObject");

        this.events.put(event, pyObject);
    }

    private void callEvent(String eventName, Object payload) {
        try {
            try {
                PyObject var12 = this.events.get(eventName);
                if (var12 != null) {
                    var12.__call__(Py.java2py(payload), Py.java2py(this));
                } else {
                    Object var13 = null;
                }
            } catch (Throwable var5) {
                if (var5.getMessage() != null) {
                    String var10000 = var5.getMessage();
                    Intrinsics.checkNotNull(var10000);
                    if (StringsKt.contains(var10000, "takes exactly 1 argument (2 given)", false)) {
                        PyObject var10 = this.events.get(eventName);
                        if (var10 != null) {
                            var10.__call__(Py.java2py(payload));
                        } else {
                            Object var11 = null;
                        }

                        return;
                    }
                }

                if (var5.getMessage() != null) {
                    String var7 = var5.getMessage();
                    Intrinsics.checkNotNull(var7);
                    if (StringsKt.contains(var7, "takes no arguments (2 given)", false)) {
                        PyObject var8 = this.events.get(eventName);
                        var8 = var8 != null ? var8.__call__() : null;
                        return;
                    }
                }

                throw var5;
            }
        } catch (Throwable throwable) {
            Minecraft.logger.error("[ScriptAPI] Exception in module '" + this.getName() + "'!", throwable);
        }

    }

    public void onEnable() {
        callEvent$default(this, "enable", null, 2, null);
    }

    public void onDisable() {
        callEvent$default(this, "disable", null, 2, null);
    }

    @EventTarget
    public void onUpdate(@NotNull UpdateEvent updateEvent) {
        Intrinsics.checkNotNullParameter(updateEvent, "updateEvent");
        callEvent$default(this, "update", null, 2, null);
    }

    @EventTarget
    public void onMotion(@NotNull MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "motionEvent");
        this.callEvent("motion", motionEvent);
    }

    @EventTarget
    public void onRender2D(@NotNull Render2DEvent render2DEvent) {
        Intrinsics.checkNotNullParameter(render2DEvent, "render2DEvent");
        this.callEvent("render2D", render2DEvent);
    }

    @EventTarget
    public void onRender3D(@NotNull Render3DEvent render3DEvent) {
        Intrinsics.checkNotNullParameter(render3DEvent, "render3DEvent");
        this.callEvent("render3D", render3DEvent);
    }

    @EventTarget
    public void onPacket(@NotNull PacketEvent packetEvent) {
        Intrinsics.checkNotNullParameter(packetEvent, "packetEvent");
        this.callEvent("packet", packetEvent);
    }

    @EventTarget
    public void onJump(@NotNull JumpEvent jumpEvent) {
        Intrinsics.checkNotNullParameter(jumpEvent, "jumpEvent");
        this.callEvent("jump", jumpEvent);
    }

    @EventTarget
    public void onAttack(@NotNull AttackEvent attackEvent) {
        Intrinsics.checkNotNullParameter(attackEvent, "attackEvent");
        this.callEvent("attack", attackEvent);
    }

    @EventTarget
    public void onKey(@NotNull KeyEvent keyEvent) {
        Intrinsics.checkNotNullParameter(keyEvent, "keyEvent");
        this.callEvent("key", keyEvent);
    }

    @EventTarget
    public void onMove(@NotNull MoveEvent moveEvent) {
        Intrinsics.checkNotNullParameter(moveEvent, "moveEvent");
        this.callEvent("move", moveEvent);
    }

    @EventTarget
    public void onStep(@NotNull StepEvent stepEvent) {
        Intrinsics.checkNotNullParameter(stepEvent, "stepEvent");
        this.callEvent("step", stepEvent);
    }

    @EventTarget
    public void onStepConfirm(@NotNull StepConfirmEvent stepConfirmEvent) {
        Intrinsics.checkNotNullParameter(stepConfirmEvent, "stepConfirmEvent");
        callEvent$default(this, "stepConfirm", null, 2, null);
    }

    @EventTarget
    public void onWorld(@NotNull WorldEvent worldEvent) {
        Intrinsics.checkNotNullParameter(worldEvent, "worldEvent");
        this.callEvent("world", worldEvent);
    }

    @EventTarget
    public void onClickBlock(@NotNull ClickBlockEvent clickBlockEvent) {
        Intrinsics.checkNotNullParameter(clickBlockEvent, "clickBlockEvent");
        this.callEvent("clickBlock", clickBlockEvent);
    }

    @EventTarget
    public void onStrafe(@NotNull StrafeEvent strafeEvent) {
        Intrinsics.checkNotNullParameter(strafeEvent, "strafeEvent");
        this.callEvent("strafe", strafeEvent);
    }

    @EventTarget
    public void onSlowDown(@NotNull SlowDownEvent slowDownEvent) {
        Intrinsics.checkNotNullParameter(slowDownEvent, "slowDownEvent");
        this.callEvent("slowDown", slowDownEvent);
    }

    @EventTarget
    public void onShutdown(@NotNull ClientShutdownEvent shutdownEvent) {
        Intrinsics.checkNotNullParameter(shutdownEvent, "shutdownEvent");
        callEvent$default(this, "shutdown", null, 2, null);
    }

    @EventTarget
    public void onEntityKilled(@NotNull EntityKilledEvent entityKilledEvent) {
        Intrinsics.checkNotNullParameter(entityKilledEvent, "entityKilledEvent");
        this.callEvent("entityKilled", entityKilledEvent);
    }

    @EventTarget
    public void onBlockBB(@NotNull BlockBBEvent blockBBEvent) {
        Intrinsics.checkNotNullParameter(blockBBEvent, "blockBBEvent");
        this.callEvent("blockBB", blockBBEvent);
    }

    @EventTarget
    public void onEntityMovement(@NotNull EntityMovementEvent entityMovementEvent) {
        Intrinsics.checkNotNullParameter(entityMovementEvent, "entityMovementEvent");
        this.callEvent("entityMovement", entityMovementEvent);
    }

    @EventTarget
    public void onPushOut(@NotNull PushOutEvent pushOutEvent) {
        Intrinsics.checkNotNullParameter(pushOutEvent, "pushOutEvent");
        this.callEvent("pushOut", pushOutEvent);
    }

    @EventTarget
    public void onRenderEntity(@NotNull RenderEntityEvent renderEntityEvent) {
        Intrinsics.checkNotNullParameter(renderEntityEvent, "renderEntityEvent");
        this.callEvent("renderEntity", renderEntityEvent);
    }

    @EventTarget
    public void onScreen(@NotNull ScreenEvent screenEvent) {
        Intrinsics.checkNotNullParameter(screenEvent, "screenEvent");
        this.callEvent("screen", screenEvent);
    }

    @EventTarget
    public void onText(@NotNull TextEvent textEvent) {
        Intrinsics.checkNotNullParameter(textEvent, "textEvent");
        this.callEvent("text", textEvent);
    }

    @EventTarget
    public void onTick(@NotNull TickEvent tickEvent) {
        Intrinsics.checkNotNullParameter(tickEvent, "tickEvent");
        this.callEvent("tick", tickEvent);
    }

    @EventTarget
    public void onClickWindow(@NotNull ClickWindowEvent clickWindowEvent) {
        Intrinsics.checkNotNullParameter(clickWindowEvent, "clickWindowEvent");
        this.callEvent("clickWindow", clickWindowEvent);
    }
}
