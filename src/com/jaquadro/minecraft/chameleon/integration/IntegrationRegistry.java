package com.jaquadro.minecraft.chameleon.integration;

import com.jaquadro.minecraft.chameleon.Chameleon;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class IntegrationRegistry
{
    private String modId;
    private List<IntegrationModule> registry;

    public IntegrationRegistry (String modId) {
        this.modId = modId;
        this.registry = new ArrayList<IntegrationModule>();
    }

    public void add (IntegrationModule module) {
        if (module.versionCheck())
            registry.add(module);
    }

    public void init () {
        for (int i = 0; i < registry.size(); i++) {
            IntegrationModule module = registry.get(i);
            if (module.getModID() != null && !Loader.isModLoaded(module.getModID())) {
                registry.remove(i--);
                continue;
            }

            try {
                module.init();
            }
            catch (Throwable t) {
                registry.remove(i--);
                Chameleon.log.info("Could not load integration module: " + module.getClass().getName());
            }
        }
    }

    public void postInit () {
        for (IntegrationModule module : registry)
            module.postInit();
    }

    public boolean isModLoaded (String mod_id) {
        for (IntegrationModule module : registry) {
            if (module.getModID().equals(mod_id))
                return true;
        }

        return false;
    }
}
