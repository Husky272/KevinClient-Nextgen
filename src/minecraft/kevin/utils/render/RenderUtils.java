package kevin.utils.render;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {
    public static void color(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }

    public static void color(int color) {
        GL11.glColor4ub(
                (byte) (color >> 16 & 0xFF),
                (byte) (color >> 8 & 0xFF),
                (byte) (color & 0xFF),
                (byte) (color >> 24 & 0xFF));
    }
    public static int applyOpacity(int color, float opacity) {
        Color old = new Color(color);
        return applyOpacity(old, opacity).getRGB();
    }

    public static Color applyOpacity(final Color color, float opacity) {
        opacity = Math.min(1.0f, Math.max(0.0f, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * opacity));
    }

    public static int darker(int color, float factor) {
        int r = (int) ((color >> 16 & 0xFF) * factor);
        int g = (int) ((color >> 8 & 0xFF) * factor);
        int b = (int) ((color & 0xFF) * factor);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }
    public static int getRedFromColor(int color) {
        return color >> 16 & 0xFF;
    }

    public static int getGreenFromColor(int color) {
        return color >> 8 & 0xFF;
    }

    public static int getBlueFromColor(int color) {
        return color & 0xFF;
    }

    public static int getAlphaFromColor(int color) {
        return color >> 24 & 0xFF;
    }


}
