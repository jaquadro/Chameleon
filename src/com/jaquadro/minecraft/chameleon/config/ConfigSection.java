package com.jaquadro.minecraft.chameleon.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ConfigSection
{
    private final Common common;
    private final ConfigSection parent;
    private final String name;
    private final String lang;

    private ConfigCategory category;

    public ConfigSection (Common common, ConfigSection parent, String name, String lang) {
        this.common = common;
        this.parent = parent;
        this.name = name;
        this.lang = lang;

        common.getSections().add(this);
    }

    public ConfigSection (Common common, String name, String lang) {
        this(common, null, name, lang);
    }

    public ConfigCategory getCategory () {
        if (category != null)
            return  category;

        if (parent != null)
            category = common.getConfig().getCategory(parent.getCategory().getQualifiedName() + "." + name.toLowerCase());
        else
            category = common.getConfig().getCategory(name.toLowerCase());

        category.setLanguageKey(common.getLangPrefix() + lang);
        return category;
    }

    public String getQualifiedName () {
        return getCategory().getQualifiedName();
    }

    public static class Common
    {
        private final List<ConfigSection> sections;
        private final Configuration config;
        private final String langPrefix;

        public Common (Configuration config, String langPrefix) {
            this.config = config;
            this.langPrefix = langPrefix;
            this.sections = new ArrayList<ConfigSection>();
        }

        public Common (Configuration config) {
            this(config, "");
        }

        public List<ConfigSection> getSections () {
            return sections;
        }

        public Configuration getConfig () {
            return config;
        }

        public String getLangPrefix () {
            return langPrefix;
        }
    }
}
