/*
 * MoonLight Hacked Client
 *
 * A free and open-source hacked client for Minecraft.
 * Developed using Minecraft's resources.
 *
 * Repository: https://github.com/randomguy3725/MoonLight
 *
 * Author(s): [Randumbguy & wxdbie & opZywl & MukjepScarlet & lucas & eonian]
 */
package kevin.module.modules.render;

import kevin.event.EventTarget;
import kevin.event.Render3DEvent;
import kevin.event.UpdateEvent;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.IntegerValue;
import kevin.module.ModuleCategory;
import kevin.utils.MathUtils;
import kevin.utils.anination.Direction;
import kevin.utils.anination.SmoothStepAnimation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LineGlyphs extends ClientModule {
    public final BooleanValue slowSpeed = new BooleanValue("Slow Speed", false);
    public final IntegerValue glyphsCount = new IntegerValue("Glyphs Count", 70, 0, 200);

    private static Color color = new Color(0,0,0);

    // Color vars
    public final IntegerValue red = new IntegerValue("Red", color.getRed(), 0, 255);
    public final IntegerValue green = new IntegerValue("Green", color.getGreen(), 0, 255);
    public final IntegerValue blue = new IntegerValue("Blue", color.getBlue(), 0, 255);



    private final ThreadLocalRandom RAND = ThreadLocalRandom.current();

    private final List<Vec3> temp3dVecs = new ArrayList<>();
    private static final Tessellator tessellator = Tessellator.getInstance();
    private final List<GliphsVecGen> GLIPHS_VEC_GENS = new ArrayList<>();

    public LineGlyphs(){
        super("LineGlyphs", "Skidded from MoonLight", ModuleCategory.RENDER);
    }
    private int[] lineMoveSteps() {
        return new int[]{0, 3};
    }

    private int[] lineStepsAmount() {
        return new int[]{7, 12};
    }

    private int[] spawnRanges() {
        return new int[]{6, 24, 0, 12};
    }

    private int maxObjCount() {
        return (int) this.glyphsCount.get();
    }

    private int getR360X() {
        return this.RAND.nextInt(0, 4) * 90;
    }

    private int getR360Y() {
        return this.RAND.nextInt(-2, 2) * 90;
    }

    private int[] getR360XY() {
        return new int[]{this.RAND.nextInt(0, 4) * 90, this.RAND.nextInt(-1, 1) * 90};
    }

    private int[] getA90R(int[] outdated) {
        int maxAttempt;
        int b;
        int a;
        int ao = a = outdated[0];
        int bo = b = outdated[1];
        for (maxAttempt = 150; maxAttempt > 0 && Math.abs(b - bo) != 90; --maxAttempt) {
            b = this.getR360Y();
        }
        for (maxAttempt = 5; maxAttempt > 0 && (Math.abs(a - ao) != 90 || Math.abs(a - ao) != 270); --maxAttempt) {
            a = this.getR360X();
        }
        return new int[]{a, b};
    }

    private Vec3i offsetFromRXYR(Vec3i vec3i, int[] rxy, int r) {
        double yawR = Math.toRadians(rxy[0]);
        double pitchR = Math.toRadians(rxy[1]);
        double r1 = r;
        int ry = (int) (Math.sin(pitchR) * r1);
        if (pitchR != 0.0) {
            r1 = 0.0;
        }
        int rx = (int) (-(Math.sin(yawR) * r1));
        int rz = (int) (Math.cos(yawR) * r1);
        int xi = vec3i.getX() + rx;
        int yi = vec3i.getY() + ry;
        int zi = vec3i.getZ() + rz;
        return new Vec3i(xi, yi, zi);
    }

    private float moveAdvanceFromTicks(int ticksSet, int ticksExpiring, float pTicks) {
        return Math.min(Math.max(1.0f - ((float) ticksExpiring - pTicks) / (float) ticksSet, 0.0f), 1.0f);
    }

    private List<Vec3> getSmoothTickedFromList(List<Vec3i> vec3is, float moveAdvance) {
        this.temp3dVecs.clear();

        for (Vec3i vec3i : vec3is) {
            double x = vec3i.getX();
            double y = vec3i.getY();
            double z = vec3i.getZ();
            if (!vec3is.isEmpty() && vec3i == vec3is.get(vec3is.size() - 1)) {
                Vec3i prevVec3i = vec3is.get(vec3is.size() - 2);
                x = MathUtils.lerp(moveAdvance, prevVec3i.getX(), x);
                y = MathUtils.lerp(moveAdvance, prevVec3i.getY(), y);
                z = MathUtils.lerp(moveAdvance, prevVec3i.getZ(), z);
            }
            this.temp3dVecs.add(new Vec3(x, y, z));
        }
        return this.temp3dVecs;
    }

    private Vec3i randGliphSpawnPos() {
        int[] spawnRanges = this.spawnRanges();
        double dst = this.RAND.nextInt(spawnRanges[0], spawnRanges[1]);
        double fov = LineGlyphs.mc.gameSettings.fovSetting;
        double radianYaw = Math.toRadians(this.RAND.nextInt((int) ((double) mc.thePlayer.rotationYaw - fov * 0.75), (int) ((double) mc.thePlayer.rotationYaw + fov * 0.75)));
        int randXOff = (int) (-(Math.sin(radianYaw) * dst));
        int randYOff = this.RAND.nextInt(-spawnRanges[2], spawnRanges[3]);
        int randZOff = (int) (Math.cos(radianYaw) * dst);
        return new Vec3i(mc.getRenderManager().viewerPosX + (double) randXOff, mc.getRenderManager().viewerPosY + (double) randYOff, mc.getRenderManager().viewerPosZ + (double) randZOff);
    }

    private void addAllGliphs(int countCap) {
        for (int maxAttempt = 8; maxAttempt > 0 && (long) this.GLIPHS_VEC_GENS.size() < (long) countCap; --maxAttempt) {
            int[] lineStepsAmount = this.lineStepsAmount();
            while (this.GLIPHS_VEC_GENS.size() < countCap) {
                Vec3i pos = this.randGliphSpawnPos();
                this.GLIPHS_VEC_GENS.add(new LineGlyphs.GliphsVecGen(pos, this.RAND.nextInt(lineStepsAmount[0], lineStepsAmount[1])));
            }
        }
    }

    private void gliphsRemoveAuto() {
        this.GLIPHS_VEC_GENS.removeIf(LineGlyphs.GliphsVecGen::isToRemove);
    }

    private void gliphsUpdate() {
        if (!this.GLIPHS_VEC_GENS.isEmpty()) {
            this.GLIPHS_VEC_GENS.forEach(LineGlyphs.GliphsVecGen::update);
        }
    }

    private void drawAllGliphs(float pTicks) {
        if (this.GLIPHS_VEC_GENS.isEmpty()) {
            return;
        }
        List<LineGlyphs.GliphsVecGen> filteredGens = this.GLIPHS_VEC_GENS;
        LineGlyphs.GliphVecRenderer.set3DRendering(() -> {
            int colorIndex = 0;
            for (LineGlyphs.GliphsVecGen filteredGen : filteredGens) {
                LineGlyphs.GliphVecRenderer.clientColoredBegin(filteredGen, ++colorIndex, (float) filteredGen.animation.getOutput(), pTicks);
            }
        });
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        color = new Color(red.get(), green.get(), blue.get());
        this.gliphsUpdate();
        this.addAllGliphs(this.maxObjCount());
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        this.gliphsRemoveAuto();
        this.drawAllGliphs(event.getPartialTicks());
    }

    private class GliphsVecGen {
        private final List<Vec3i> vecGens = new ArrayList<>();
        private int currentStepTicks;
        private int lastStepSet;
        private int stepsAmount;
        private int[] lastYawPitch;
        private final SmoothStepAnimation animation = new SmoothStepAnimation(400,1);

        public GliphsVecGen(Vec3i spawnPos, int maxStepsAmount) {
            this.vecGens.add(spawnPos);
            this.lastYawPitch = LineGlyphs.this.getR360XY();
            this.stepsAmount = maxStepsAmount;
            animation.setDirection(Direction.FORWARDS);
        }

        private void update() {
            if (this.stepsAmount == 0) {
                animation.setDirection(Direction.BACKWARDS);
            }
            if (this.currentStepTicks > 0) {
                this.currentStepTicks -= LineGlyphs.this.slowSpeed.get() ? 1 : 2;
                if (this.currentStepTicks < 0) {
                    this.currentStepTicks = 0;
                }
                return;
            }
            this.lastYawPitch = LineGlyphs.this.getA90R(this.lastYawPitch);
            this.lastStepSet = this.currentStepTicks = LineGlyphs.this.RAND.nextInt(LineGlyphs.this.lineMoveSteps()[0], LineGlyphs.this.lineMoveSteps()[1]);
            this.vecGens.add(LineGlyphs.this.offsetFromRXYR(this.vecGens.get(this.vecGens.size() - 1), this.lastYawPitch, this.currentStepTicks));
            --this.stepsAmount;
        }

        public List<Vec3> getPosVectors(float pTicks) {
            return LineGlyphs.this.getSmoothTickedFromList(this.vecGens, LineGlyphs.this.moveAdvanceFromTicks(this.lastStepSet, this.currentStepTicks, pTicks));
        }

        public boolean isToRemove() {
            return animation.finished(Direction.BACKWARDS) && animation.getOutput() == 0;
        }
    }

    private static class GliphVecRenderer {

        private static void set3DRendering(Runnable render) {
            double glX = mc.getRenderManager().viewerPosX;
            double glY = mc.getRenderManager().viewerPosY;
            double glZ = mc.getRenderManager().viewerPosZ;
            GL11.glPushMatrix();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            GL11.glEnable(3042);
            GL11.glLineWidth(1.0f);
            GL11.glPointSize(1.0f);
            GL11.glEnable(2832);
            GL11.glDisable(3553);
            mc.entityRenderer.disableLightmap();
            GL11.glDisable(2896);
            GL11.glShadeModel(7425);
            GL11.glAlphaFunc(516, 0.003921569f);
            GL11.glDisable(2884);
            GL11.glDepthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glTranslated(-glX, -glY, -glZ);
            render.run();
            GL11.glTranslated(glX, glY, glZ);
            GL11.glLineWidth(1.0f);
            GL11.glHint(3154, 4352);
            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glAlphaFunc(516, 0.1f);
            GL11.glLineWidth(1.0f);
            GL11.glPointSize(1.0f);
            GL11.glShadeModel(7424);
            GL11.glEnable(3553);
            GlStateManager.resetColor();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glPopMatrix();
        }

        private static float calcLineWidth(LineGlyphs.GliphsVecGen gliphVecGen) {
            Vec3 cameraPos = new Vec3(mc.getRenderManager().getRenderPosX(), mc.getRenderManager().getRenderPosY(), mc.getRenderManager().getRenderPosZ());
            Vec3i pos = gliphVecGen.vecGens.stream().sorted(Comparator.comparingDouble(vec3i -> -vec3i.distanceTo(cameraPos))).findAny().orElse(new Vec3i(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
            double dst = cameraPos.getDistanceAtEyeByVec(mc.thePlayer, pos.getX(), pos.getY(), pos.getZ());
            return 1.0E-4f + 3.0f * (float) MathHelper.clamp_double(1.0 - dst / 20.0, 0.0, 1.0);
        }

        private static void clientColoredBegin(LineGlyphs.GliphsVecGen gliphVecGen, int objIndex, float alphaPC, float pTicks) {
            if (gliphVecGen.vecGens.size() < 2) {
                return;
            }
            float lineWidth = LineGlyphs.GliphVecRenderer.calcLineWidth(gliphVecGen);
            GL11.glLineWidth(lineWidth);
            tessellator.getWorldRenderer().begin(3, DefaultVertexFormats.POSITION_COLOR);
            int colorIndex = objIndex;
            for (Vec3 vec3d : gliphVecGen.getPosVectors(pTicks)) {
                tessellator.getWorldRenderer().pos(vec3d).color(color).endVertex();
                colorIndex += 180;
            }
            tessellator.draw();
            GL11.glPointSize(lineWidth * 3.0f);
            tessellator.getWorldRenderer().begin(0, DefaultVertexFormats.POSITION_COLOR);
            colorIndex = objIndex;
            for (Vec3 vec3d : gliphVecGen.getPosVectors(pTicks)) {
                tessellator.getWorldRenderer().pos(vec3d).color(color).endVertex();
                colorIndex += 180;
            }
            tessellator.draw();
        }
    }
}
