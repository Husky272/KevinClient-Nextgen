package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;

public class ModelAdapterChicken extends ModelAdapter
{
    public ModelAdapterChicken()
    {
        super(EntityChicken.class, "chicken", 0.3F);
    }

    public ModelBase makeModel()
    {
        return new ModelChicken();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelChicken))
        {
            return null;
        }
        else
        {
            ModelChicken modelchicken = (ModelChicken)model;

            switch (modelPart) {
                case "head":
                    return modelchicken.head;
                case "body":
                    return modelchicken.body;
                case "right_leg":
                    return modelchicken.rightLeg;
                case "left_leg":
                    return modelchicken.leftLeg;
                case "right_wing":
                    return modelchicken.rightWing;
                case "left_wing":
                    return modelchicken.leftWing;
                case "bill":
                    return modelchicken.bill;
                default:
                    return modelPart.equals("chin") ? modelchicken.chin : null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "body", "right_leg", "left_leg", "right_wing", "left_wing", "bill", "chin"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderChicken renderchicken = new RenderChicken(rendermanager, modelBase, shadowSize);
        return renderchicken;
    }
}
