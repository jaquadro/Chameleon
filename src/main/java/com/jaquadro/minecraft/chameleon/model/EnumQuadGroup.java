package com.jaquadro.minecraft.chameleon.model;

public enum EnumQuadGroup
{
    FACE(true, false),
    GENERAL(false, true),
    BOTH(true, true);

    private boolean faceType;
    private boolean generalType;

    EnumQuadGroup (boolean face, boolean general) {
        faceType = face;
        generalType = general;
    }

    public boolean isFaceType () {
        return faceType;
    }

    public boolean isGeneralType () {
        return generalType;
    }
}
