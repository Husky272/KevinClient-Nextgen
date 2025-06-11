package kevin.module.modules.render;

import kevin.main.KevinClient;
import kevin.module.ClientModule;
import kevin.module.FloatValue;
import kevin.module.ListValue;
import kevin.module.ModuleCategory;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class RenderSettings extends ClientModule {
    @NotNull
    public static final RenderSettings INSTANCE = new RenderSettings();
    @NotNull
    private static final ListValue fontRendererValue = new ListValue("FontRenderer", ClientModule.arrayOf("Glyph", "Vector"), "Glyph");
    @NotNull
    private static final FloatValue fontEpsilonValue = new FloatValue("FontVectorEpsilon", 0.5F, 0.0F, 1.5F);

    private RenderSettings() {
        super("RenderSettings", "Some render settings.", ModuleCategory.RENDER);
    }

    @NotNull
    public FloatValue getFontEpsilonValue() {
        return fontEpsilonValue;
    }

    private String getGetFontRender() {
        return KevinClient.INSTANCE.isStarting() ? "Glyph" : fontRendererValue.get();
    }

    public boolean getUseGlyphFontRenderer() {
        return Intrinsics.areEqual(this.getGetFontRender(), "Glyph");
    }

    public void onEnable() {
        this.setState(false);
    }

}
