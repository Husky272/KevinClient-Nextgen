package kevin.module.modules.render;

import kevin.module.*;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class Animations extends ClientModule {
    @NotNull
    private final ListValue animations;
    @NotNull
    private final FloatValue itemPosX;
    @NotNull
    private final FloatValue itemPosY;
    @NotNull
    private final FloatValue itemPosZ;
    @NotNull
    private final BooleanValue onlyOnBlock;
    @NotNull
    private final BooleanValue rotationItem;
    @NotNull
    private final IntegerValue animationSpeed;
    @NotNull
    private FloatValue translateX;
    @NotNull
    private FloatValue translateY;
    @NotNull
    private FloatValue translateZ;
    @NotNull
    private FloatValue itemScale;

    public Animations() {
        super("Animations", "Changes animations.", ModuleCategory.RENDER);
        String[] var1 = new String[]{
                "Akrien", "Avatar",
                "ETB", "Exhibition",
                "HSlide", "Jello",
                "Kevin", "LiquidBounce",
                "Push", "Reverse", "Rotate",
                "Shield", "SigmaNew", "SigmaOld",
                "Slide", "SlideDown", "Swank",
                "Swong", "VisionFX"
        };
        String[] var10004 = ClientModule.arrayOf(var1);
        this.animations = new ListValue("Preset", var10004, "SlideDown");
        this.translateX = new FloatValue("TranslateX", 0.0F, 0.0F, 1.5F);
        this.translateY = new FloatValue("TranslateY", 0.0F, 0.0F, 0.5F);
        this.translateZ = new FloatValue("TranslateZ", 0.0F, 0.0F, -2.0F);
        this.itemPosX = new FloatValue("ItemPosX", 0.56F, -1.0F, 1.0F);
        this.itemPosY = new FloatValue("ItemPosY", -0.52F, -1.0F, 1.0F);
        this.itemPosZ = new FloatValue("ItemPosZ", -0.71999997F, -1.0F, 1.0F);
        this.itemScale = new FloatValue("ItemScale", 0.4F, 0.0F, 2.0F);
        this.onlyOnBlock = new BooleanValue("OnlyBlock", false);
        this.rotationItem = new BooleanValue("RotationItem", false);
        this.animationSpeed = new IntegerValue("AnimationSpeed", 6, 1, 30);
    }

    @NotNull
    public ListValue getAnimations() {
        return this.animations;
    }

    @NotNull
    public FloatValue getTranslateX() {
        return this.translateX;
    }

    public void setTranslateX(@NotNull FloatValue var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.translateX = var1;
    }

    @NotNull
    public FloatValue getTranslateY() {
        return this.translateY;
    }

    public void setTranslateY(@NotNull FloatValue var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.translateY = var1;
    }

    @NotNull
    public FloatValue getTranslateZ() {
        return this.translateZ;
    }

    public void setTranslateZ(@NotNull FloatValue var1) {
        Intrinsics.checkNotNullParameter(var1, "<set-?>");
        this.translateZ = var1;
    }

    @NotNull
    public FloatValue getItemPosX() {
        return this.itemPosX;
    }

    @NotNull
    public FloatValue getItemPosY() {
        return this.itemPosY;
    }

    @NotNull
    public FloatValue getItemPosZ() {
        return this.itemPosZ;
    }

    @NotNull
    public FloatValue getItemScale() {
        return this.itemScale;
    }

    public void setItemScale(@NotNull FloatValue var1) {
        this.itemScale = var1;
    }

    @NotNull
    public BooleanValue getOnlyOnBlock() {
        return this.onlyOnBlock;
    }

    @NotNull
    public BooleanValue getRotationItem() {
        return this.rotationItem;
    }

    @NotNull
    public IntegerValue getAnimationSpeed() {
        return this.animationSpeed;
    }

    @NotNull
    public String getTag() {
        return this.animations.get();
    }
}
