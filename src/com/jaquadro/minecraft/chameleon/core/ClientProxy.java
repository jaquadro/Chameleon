package com.jaquadro.minecraft.chameleon.core;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.resources.IconRegistry;
import com.jaquadro.minecraft.chameleon.resources.ModelRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initSidedResources () {
        Chameleon.instance.iconRegistry = new IconRegistry();
        Chameleon.instance.modelRegistry = new ModelRegistry();
    }

    @SubscribeEvent
    public void onPreTextureStitch (TextureStitchEvent.Pre event) {
        Chameleon.instance.iconRegistry.loadIcons(event.map);
    }

    @SubscribeEvent
    public void onModelBakeEvent (ModelBakeEvent event) {
        Chameleon.instance.modelRegistry.loadModels(event.modelRegistry);
    }
}
