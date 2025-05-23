// Decompiled with: CFR 0.152
// Class Version: 8
package kevin.skin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Locale;

public final class Skin {
    @NotNull
    private final String name;
    @NotNull
    private final BufferedImage image;
    @NotNull
    private final ResourceLocation resource;

    public Skin(@NotNull String name, @NotNull BufferedImage image) {

        this.name = name;
        this.image = image;
        StringBuilder stringBuilder = new StringBuilder().append("kevin/skin/");
        String string = this.name;
        Locale locale = Locale.getDefault();

        String string2 = string.toLowerCase(locale);

        this.resource = new ResourceLocation(stringBuilder.append(string2.replace(" ", "_")).toString());
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> Skin._init_$lambda$0(mc, this));
    }

    private static void _init_$lambda$0(Minecraft $mc, Skin this$0) {
        $mc.getTextureManager().loadTexture(this$0.resource, new DynamicTexture(this$0.image));
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public BufferedImage getImage() {
        return this.image;
    }

    @NotNull
    public ResourceLocation getResource() {
        return this.resource;
    }
}
