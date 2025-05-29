package net.optifine.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModelAdapterQuadruped extends ModelAdapter
{
    public ModelAdapterQuadruped(Class entityClass, String name, float shadowSize)
    {
        super(entityClass, name, shadowSize);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelQuadruped))
        {
            return null;
        }
        else
        {
            ModelQuadruped modelquadruped = (ModelQuadruped)model;

            switch (modelPart) {
                case "head":
                    return modelquadruped.head;
                case "body":
                    return modelquadruped.body;
                case "leg1":
                    return modelquadruped.leg1;
                case "leg2":
                    return modelquadruped.leg2;
                case "leg3":
                    return modelquadruped.leg3;
                default:
                    return modelPart.equals("leg4") ? modelquadruped.leg4 : null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "body", "leg1", "leg2", "leg3", "leg4"};
    }
}
