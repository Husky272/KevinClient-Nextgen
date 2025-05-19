package kevin.module;

import kevin.event.Listenable;
import kevin.hud.element.elements.ConnectNotificationType;
import kevin.hud.element.elements.Notification;
import kevin.main.KevinClient;
import kevin.utils.ClassUtils;
import kevin.utils.ColorUtils;
import kevin.utils.MSTimer;
import kevin.utils.MinecraftInstance;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.text.StringsKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class ClientModule extends MinecraftInstance implements Listenable {

    public String tag;

    public static String[] arrayOf(String... args){
        StringBuilder temp = new StringBuilder();
        String toSplit = "😂";
        for(String s : args){
            temp.append(s);
            temp.append(toSplit);
        }
        return temp.toString().split(toSplit);
    }

    @Nullable
    public String getTag() {

        // Null Check
        // noinspection ConstantValue
        return tag.isEmpty() || tag == null ? "" : tag;
    }


    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private ModuleCategory category;
    private int keyBind;
    @NotNull
    private final MSTimer enabledTimer;
    @NotNull
    private final MSTimer disabledTimer;
    private boolean state;
    private final float hue;
    private float slide;
    private float slideStep;
    private boolean array;
    @NotNull
    private Pair<Boolean, String> autoDisable;


    // Full constructor with Supplier
    public ClientModule(@NotNull String name,
                        @NotNull String description,
                        Supplier<Integer> keyBindSupplier,
                        @NotNull ModuleCategory category) {

        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBind = keyBindSupplier.get() != null ? keyBindSupplier.get() : -1;

        this.enabledTimer = new MSTimer();
        this.disabledTimer = new MSTimer();
        this.hue = (float) Math.random();
        this.array = true;
        this.autoDisable = TuplesKt.to(false, "");
    }


    public ClientModule(@NotNull String name, @NotNull String description, @NotNull ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabledTimer = new MSTimer();
        this.disabledTimer = new MSTimer();
        this.hue = (float) Math.random();
        this.array = true;
        this.autoDisable = TuplesKt.to(false, "");
    }


    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String var1) {

        this.name = var1;
    }

    @NotNull
    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(@NotNull String var1) {

        this.description = var1;
    }

    @NotNull
    public final ModuleCategory getCategory() {
        return this.category;
    }

    public final void setCategory(@NotNull ModuleCategory var1) {

        this.category = var1;
    }

    public final int getKeyBind() {
        return this.keyBind;
    }

    public final void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
        if (!KevinClient.INSTANCE.isStarting()) {
            KevinClient.INSTANCE.getFileManager().saveConfig(KevinClient.INSTANCE.getFileManager().modulesConfig);
        }

    }

    @NotNull
    public final MSTimer getEnabledTimer() {
        return this.enabledTimer;
    }

    @NotNull
    public final MSTimer getDisabledTimer() {
        return this.disabledTimer;
    }

    public final boolean getState() {
        return this.state;
    }

    public final void setState(boolean value) {
        if (this.state != value) {
            this.onToggle(value);
            if (!KevinClient.INSTANCE.isStarting()) {
                Minecraft.getMinecraft().getSoundHandler().playSound((ISound) PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), value ? 1.0F : 0.6F));
                KevinClient.INSTANCE.getHud().addNotification(new Notification((value ? "Enabled" : "Disabled") + ' ' + this.name, "ModuleManager", value ? ConnectNotificationType.Connect : ConnectNotificationType.Disconnect));
            }

            this.state = value;
            if (value) {
                this.onEnable();
                this.enabledTimer.reset();
            } else {
                this.onDisable();
                this.disabledTimer.reset();
            }

            KevinClient.INSTANCE.getFileManager().saveConfig(KevinClient.INSTANCE.getFileManager().modulesConfig);
        }
    }

    public final float getHue() {
        return this.hue;
    }

    public final float getSlide() {
        return this.slide;
    }

    public final void setSlide(float var1) {
        this.slide = var1;
    }

    @NotNull
    public final String getTagName(@NotNull String tagleft, @NotNull String tagright) {
        return this.name + (this.getTag() == null ? "" : " §7" + tagleft + this.getTag() + tagright);
    }

    @NotNull
    public final String getColorlessTagName(@NotNull String tagleft, @NotNull String tagright) {
        return this.name + (this.getTag() == null ? "" : ' ' + tagleft + ColorUtils.stripColor(this.getTag()) + tagright);
    }

    public final float getSlideStep() {
        return this.slideStep;
    }

    public final void setSlideStep(float var1) {
        this.slideStep = var1;
    }

    public final boolean getArray() {
        return this.array;
    }

    public final void setArray(boolean array) {
        this.array = array;
        if (!KevinClient.INSTANCE.isStarting()) {
            KevinClient.INSTANCE.getFileManager().saveConfig(KevinClient.INSTANCE.getFileManager().modulesConfig);
        }

    }


    @NotNull
    public final Pair getAutoDisable() {
        return this.autoDisable;
    }

    public final void setAutoDisable(@NotNull Pair<Boolean, String> var1) {
        this.autoDisable = var1;
    }

    public final void toggle() {
        this.setState(!this.state);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle(boolean state) {
    }

    public boolean handleEvents() {
        return this.state;
    }

    /**
     * 根据名称查找 Value 对象，忽略大小写。
     *
     * @param valueName 查找的名称
     * @return 匹配的对象，如果没有找到则返回 null
     */
    @Nullable
    public Value getValue(@NotNull String valueName) {
        return StreamSupport.stream(getValues().spliterator(), false)
                .filter(value -> value.getName().equalsIgnoreCase(valueName))
                .findFirst()
                .orElse(null);
    }

    @NotNull
    public List<Value<?>> getValues() {
        return ClassUtils.getValues(this.getClass(), this);
    }
}
