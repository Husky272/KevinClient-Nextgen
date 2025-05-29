package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import net.optifine.reflect.Reflector;

public class ModelAdapterDragon extends ModelAdapter {
    public ModelAdapterDragon() {
        super(EntityDragon.class, "dragon", 0.5F);
    }

    public ModelBase makeModel() {
        return new ModelDragon(0.0F);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelDragon)) {
            return null;
        } else {
            ModelDragon modeldragon = (ModelDragon) model;

            switch (modelPart) {
                case "head":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 0);
                case "spine":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 1);
                case "jaw":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 2);
                case "body":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 3);
                case "rear_leg":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 4);
                case "front_leg":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 5);
                case "rear_leg_tip":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 6);
                case "front_leg_tip":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 7);
                case "rear_foot":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 8);
                case "front_foot":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 9);
                case "wing":
                    return (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 10);
                default:
                    return modelPart.equals("wing_tip") ? (ModelRenderer) Reflector.getFieldValue(modeldragon, Reflector.ModelDragon_ModelRenderers, 11) : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "spine", "jaw", "body", "rear_leg", "front_leg", "rear_leg_tip", "front_leg_tip", "rear_foot", "front_foot", "wing", "wing_tip"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderDragon renderdragon = new RenderDragon(rendermanager);
        renderdragon.mainModel = modelBase;
        renderdragon.shadowSize = shadowSize;
        return renderdragon;
    }
}
