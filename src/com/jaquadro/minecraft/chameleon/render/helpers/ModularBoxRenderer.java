package com.jaquadro.minecraft.chameleon.render.helpers;

import com.jaquadro.minecraft.chameleon.render.ChamRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class ModularBoxRenderer
{
    public static final int CONNECT_YNEG = 1 << 0;
    public static final int CONNECT_YPOS = 1 << 1;
    public static final int CONNECT_ZNEG = 1 << 2;
    public static final int CONNECT_ZPOS = 1 << 3;
    public static final int CONNECT_XNEG = 1 << 4;
    public static final int CONNECT_XPOS = 1 << 5;
    public static final int CONNECT_YNEG_ZNEG = 1 << 6;
    public static final int CONNECT_YNEG_ZPOS = 1 << 7;
    public static final int CONNECT_YNEG_XNEG = 1 << 8;
    public static final int CONNECT_YNEG_XPOS = 1 << 9;
    public static final int CONNECT_YPOS_ZNEG = 1 << 10;
    public static final int CONNECT_YPOS_ZPOS = 1 << 11;
    public static final int CONNECT_YPOS_XNEG = 1 << 12;
    public static final int CONNECT_YPOS_XPOS = 1 << 13;
    public static final int CONNECT_ZNEG_XNEG = 1 << 14;
    public static final int CONNECT_ZNEG_XPOS = 1 << 15;
    public static final int CONNECT_ZPOS_XNEG = 1 << 16;
    public static final int CONNECT_ZPOS_XPOS = 1 << 17;

    public static final int CUT_YNEG = 1 << 0;
    public static final int CUT_YPOS = 1 << 1;
    public static final int CUT_ZNEG = 1 << 2;
    public static final int CUT_ZPOS = 1 << 3;
    public static final int CUT_XNEG = 1 << 4;
    public static final int CUT_XPOS = 1 << 5;

    public static final int CUT_ALL = (1 << 6) - 1;

    private static final int TEST_YNEG_ZNEG = CUT_YNEG | CUT_ZNEG;
    private static final int TEST_YNEG_ZPOS = CUT_YNEG | CUT_ZPOS;
    private static final int TEST_YNEG_XNEG = CUT_YNEG | CUT_XNEG;
    private static final int TEST_YNEG_XPOS = CUT_YNEG | CUT_XPOS;
    private static final int TEST_YPOS_ZNEG = CUT_YPOS | CUT_ZNEG;
    private static final int TEST_YPOS_ZPOS = CUT_YPOS | CUT_ZPOS;
    private static final int TEST_YPOS_XNEG = CUT_YPOS | CUT_XNEG;
    private static final int TEST_YPOS_XPOS = CUT_YPOS | CUT_XPOS;
    private static final int TEST_ZNEG_XNEG = CUT_ZNEG | CUT_XNEG;
    private static final int TEST_ZNEG_XPOS = CUT_ZNEG | CUT_XPOS;
    private static final int TEST_ZPOS_XNEG = CUT_ZPOS | CUT_XNEG;
    private static final int TEST_ZPOS_XPOS = CUT_ZPOS | CUT_XPOS;

    private static final int TEST_YNEG_ZNEG_XNEG = CUT_YNEG | CUT_ZNEG | CUT_XNEG;
    private static final int TEST_YNEG_ZNEG_XPOS = CUT_YNEG | CUT_ZNEG | CUT_XPOS;
    private static final int TEST_YNEG_ZPOS_XNEG = CUT_YNEG | CUT_ZPOS | CUT_XNEG;
    private static final int TEST_YNEG_ZPOS_XPOS = CUT_YNEG | CUT_ZPOS | CUT_XPOS;
    private static final int TEST_YPOS_ZNEG_XNEG = CUT_YPOS | CUT_ZNEG | CUT_XNEG;
    private static final int TEST_YPOS_ZNEG_XPOS = CUT_YPOS | CUT_ZNEG | CUT_XPOS;
    private static final int TEST_YPOS_ZPOS_XNEG = CUT_YPOS | CUT_ZPOS | CUT_XNEG;
    private static final int TEST_YPOS_ZPOS_XPOS = CUT_YPOS | CUT_ZPOS | CUT_XPOS;

    private static final int PLANE_YNEG = CONNECT_YNEG | CONNECT_YNEG_ZNEG | CONNECT_YNEG_ZPOS | CONNECT_YNEG_XNEG | CONNECT_YNEG_XPOS;
    private static final int PLANE_YPOS = CONNECT_YPOS | CONNECT_YPOS_ZNEG | CONNECT_YPOS_ZPOS | CONNECT_YPOS_XNEG | CONNECT_YPOS_XPOS;

    public static final float[] COLOR_WHITE = new float[] { 1, 1, 1 };

    public static final int[] sideCut = new int[] { CUT_YNEG, CUT_YPOS, CUT_ZNEG, CUT_ZPOS, CUT_XNEG, CUT_XPOS };

    private ChamRender renderHelper;

    private double unit = 0.0625;

    private float[][] exteriorColor = new float[6][3];
    private float[][] interiorColor = new float[6][3];
    private float[][] cutColor = new float[6][3];

    private TextureAtlasSprite[] exteriorIcon = new TextureAtlasSprite[6];
    private TextureAtlasSprite[] interiorIcon = new TextureAtlasSprite[6];
    private TextureAtlasSprite[] cutIcon = new TextureAtlasSprite[6];

    public boolean flipOpposite;

    public ModularBoxRenderer (ChamRender renderer) {
        renderHelper = renderer;
    }

    private void copyFrom (float[] target, float[] source) {
        target[0] = source[0];
        target[1] = source[1];
        target[2] = source[2];
    }

    private void copyFrom (float[] target, float r, float g, float b) {
        target[0] = r;
        target[1] = g;
        target[2] = b;
    }

    public void setColor (float[] color) {
        setExteriorColor(color);
        setInteriorColor(color);
        setCutColor(color);
    }

    public void setScaledColor (float[] color, float scale) {
        setScaledExteriorColor(color, scale);
        setScaledInteriorColor(color, scale);
        setScaledCutColor(color, scale);
    }

    public void setExteriorColor (float[] color) {
        for (int i = 0; i < 6; i++)
            copyFrom(exteriorColor[i], color);
    }

    public void setExteriorColor (float[] color, int side) {
        copyFrom(exteriorColor[side], color);
    }

    public void setScaledExteriorColor (float[] color, float scale) {
        for (int i = 0; i < 6; i++)
            copyFrom(exteriorColor[i], color[0] * scale, color[1] * scale, color[2] * scale);
    }

    public void setScaledExteriorColor (float[] color, float scale, int side) {
        copyFrom(exteriorColor[side], color[0] * scale, color[1] * scale, color[2] * scale);
    }

    public void setInteriorColor (float[] color) {
        for (int i = 0; i < 6; i++)
            copyFrom(interiorColor[i], color);
    }

    public void setInteriorColor (float[] color, int side) {
        side = (side % 2 == 0) ? side + 1 : side - 1;
        copyFrom(interiorColor[side], color);
    }

    public void setScaledInteriorColor (float[] color, float scale) {
        for (int i = 0; i < 6; i++)
            copyFrom(interiorColor[i], color[0] * scale, color[1] * scale, color[2] * scale);
    }

    public void setCutColor (float[] color) {
        for (int i = 0; i < 6; i++)
            copyFrom(cutColor[i], color);
    }

    public void setCutColor (float[] color, int side) {
        copyFrom(cutColor[side], color);
    }

    public void setScaledCutColor (float[] color, float scale) {
        for (int i = 0; i < 6; i++)
            copyFrom(cutColor[i], color[0] * scale, color[1] * scale, color[2] * scale);
    }

    public void setIcon (TextureAtlasSprite icon) {
        setExteriorIcon(icon);
        setInteriorIcon(icon);
        setCutIcon(icon);
    }

    public void setIcon (TextureAtlasSprite icon, int side) {
        setExteriorIcon(icon, side);
        setInteriorIcon(icon, side);
        setCutIcon(icon, side);
    }

    public void setExteriorIcon (TextureAtlasSprite icon) {
        for (int i = 0; i < 6; i++)
            exteriorIcon[i] = icon;
    }

    public void setExteriorIcon (TextureAtlasSprite icon, int side) {
        exteriorIcon[side] = icon;
    }

    public void setInteriorIcon (TextureAtlasSprite icon) {
        for (int i = 0; i < 6; i++)
            interiorIcon[i] = icon;
    }

    public void setInteriorIcon (TextureAtlasSprite icon, int side) {
        side = (side % 2 == 0) ? side + 1 : side - 1;
        interiorIcon[side] = icon;
    }

    public void setCutIcon (TextureAtlasSprite icon) {
        for (int i = 0; i < 6; i++)
            cutIcon[i] = icon;
    }

    public void setCutIcon (TextureAtlasSprite icon, int side) {
        cutIcon[side] = icon;
    }

    public void setUnit (double unit) {
        this.unit = unit;
    }

    public void renderOctant (IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xOff, double yOff, double zOff, int connectedFlags, int cutFlags) {
        double xPos = xOff + .5;
        double yPos = yOff + .5;
        double zPos = zOff + .5;

        renderExterior(blockAccess, state, pos, xOff, yOff, zOff, xPos, yPos, zPos, connectedFlags, cutFlags);
        renderInterior(blockAccess, state, pos, xOff, yOff, zOff, xPos, yPos, zPos, connectedFlags, cutFlags);
    }

    public void renderBox (IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos, int connectedFlags, int cutFlags) {
        renderExterior(blockAccess, state, pos, xNeg, yNeg, zNeg, xPos, yPos, zPos, connectedFlags, cutFlags);
        renderInterior(blockAccess, state, pos, xNeg, yNeg, zNeg, xPos, yPos, zPos, connectedFlags, cutFlags);
    }

    public void renderSolidBox (IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos) {
        renderExterior(blockAccess, state, pos, xNeg, yNeg, zNeg, xPos, yPos, zPos, 0, 0);
    }

    public void renderExterior (IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos, int connectedFlags, int cutFlags) {
        if ((cutFlags & CUT_YNEG) != 0)
            connectedFlags |= CONNECT_YNEG;
        if ((cutFlags & CUT_YPOS) != 0)
            connectedFlags |= CONNECT_YPOS;
        if ((cutFlags & CUT_ZNEG) != 0)
            connectedFlags |= CONNECT_ZNEG;
        if ((cutFlags & CUT_ZPOS) != 0)
            connectedFlags |= CONNECT_ZPOS;
        if ((cutFlags & CUT_XNEG) != 0)
            connectedFlags |= CONNECT_XNEG;
        if ((cutFlags & CUT_XPOS) != 0)
            connectedFlags |= CONNECT_XPOS;

        renderHelper.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);

        // Render solid faces
        if ((connectedFlags & CONNECT_YNEG) == 0)
            renderExteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        if ((connectedFlags & CONNECT_YPOS) == 0)
            renderExteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        if ((connectedFlags & CONNECT_ZNEG) == 0)
            renderExteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
        if ((connectedFlags & CONNECT_ZPOS) == 0)
            renderExteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
        if ((connectedFlags & CONNECT_XNEG) == 0)
            renderExteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        if ((connectedFlags & CONNECT_XPOS) == 0)
            renderExteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);

        if (unit == 0)
            return;

        // Render edge faces
        if ((cutFlags & TEST_YNEG_ZNEG) != 0) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg, zNeg, xPos - unit, yNeg + unit, zNeg + unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YNEG_ZPOS) != 0) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg, zPos - unit, xPos - unit, yNeg + unit, zPos);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YNEG_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yNeg, zNeg + unit, xNeg + unit, yNeg + unit, zPos - unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YNEG_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yNeg, zNeg + unit, xPos, yNeg + unit, zPos - unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_ZNEG) != 0) {
            renderHelper.setRenderBounds(xNeg + unit, yPos - unit, zNeg, xPos - unit, yPos, zNeg + unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_ZPOS) != 0) {
            renderHelper.setRenderBounds(xNeg + unit, yPos - unit, zPos - unit, xPos - unit, yPos, zPos);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yPos - unit, zNeg + unit, xNeg + unit, yPos, zPos - unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yPos - unit, zNeg + unit, xPos, yPos, zPos - unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_ZNEG_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yNeg + unit, zNeg, xNeg + unit, yPos - unit, zNeg + unit);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_ZNEG_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yNeg + unit, zNeg, xPos, yPos - unit, zNeg + unit);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_ZPOS_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yNeg + unit, zPos - unit, xNeg + unit, yPos - unit, zPos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_ZPOS_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yNeg + unit, zPos - unit, xPos, yPos - unit, zPos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }

        // Render corner faces
        if ((cutFlags & TEST_YNEG_ZNEG_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yNeg + unit, zNeg + unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XNEG | CONNECT_YNEG_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_YNEG_ZNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YNEG_ZNEG_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yNeg + unit, zNeg + unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XPOS | CONNECT_YNEG_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_YNEG_ZNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YNEG_ZPOS_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yNeg, zPos - unit, xNeg + unit, yNeg + unit, zPos);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XNEG | CONNECT_YNEG_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_YNEG_ZPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YNEG_ZPOS_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yNeg, zPos - unit, xPos, yNeg + unit, zPos);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XPOS | CONNECT_YNEG_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_YNEG_ZPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_ZNEG_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yPos - unit, zNeg, xNeg + unit, yPos, zNeg + unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XNEG | CONNECT_YPOS_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_YPOS_ZNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_ZNEG_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yPos - unit, zNeg, xPos, yPos, zNeg + unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XPOS | CONNECT_YPOS_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_YPOS_ZNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_ZPOS_XNEG) != 0) {
            renderHelper.setRenderBounds(xNeg, yPos - unit, zPos - unit, xNeg + unit, yPos, zPos);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XNEG | CONNECT_YPOS_XNEG) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_YPOS_ZPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }
        if ((cutFlags & TEST_YPOS_ZPOS_XPOS) != 0) {
            renderHelper.setRenderBounds(xPos - unit, yPos - unit, zPos - unit, xPos, yPos, zPos);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XPOS | CONNECT_YPOS_XPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_YPOS_ZPOS) != connectedFlags)
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
    }

    public void renderInterior (IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos, int connectedFlags, int cutFlags) {
        if ((cutFlags & CUT_YNEG) != 0)
            connectedFlags |= PLANE_YNEG;
        if ((cutFlags & CUT_YPOS) != 0)
            connectedFlags |= PLANE_YPOS;
        if ((cutFlags & CUT_ZNEG) != 0)
            connectedFlags |= CONNECT_ZNEG;
        if ((cutFlags & CUT_ZPOS) != 0)
            connectedFlags |= CONNECT_ZPOS;
        if ((cutFlags & CUT_XNEG) != 0)
            connectedFlags |= CONNECT_XNEG;
        if ((cutFlags & CUT_XPOS) != 0)
            connectedFlags |= CONNECT_XPOS;

        renderHelper.setRenderBounds(xNeg + unit, yNeg + unit, zNeg + unit, xPos - unit, yPos - unit, zPos - unit);

        // Render solid faces
        if ((connectedFlags & CONNECT_YNEG) == 0) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg, zNeg + unit, xPos - unit, yNeg + unit, zPos - unit);
            renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & CONNECT_YPOS) == 0) {
            renderHelper.setRenderBounds(xNeg + unit, yPos - unit, zNeg + unit, xPos - unit, yPos, zPos - unit);
            renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & CONNECT_ZNEG) == 0) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg + unit, zNeg, xPos - unit, yPos - unit, zNeg + unit);
            renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & CONNECT_ZPOS) == 0) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg + unit, zPos - unit, xPos - unit, yPos - unit, zPos);
            renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & CONNECT_XNEG) == 0) {
            renderHelper.setRenderBounds(xNeg, yNeg + unit, zNeg + unit, xNeg + unit, yPos - unit, zPos - unit);
            renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & CONNECT_XPOS) == 0) {
            renderHelper.setRenderBounds(xPos - unit, yNeg + unit, zNeg + unit, xPos, yPos - unit, zPos - unit);
            renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
        }

        if (unit == 0)
            return;

        // Render edge faces
        if ((connectedFlags & TEST_YNEG_ZNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_YNEG_ZNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg, zNeg, xPos - unit, yNeg + unit, zNeg + unit);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YNEG_ZPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_YNEG_ZPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg + unit, yNeg, zPos - unit, xPos - unit, yNeg + unit, zPos);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YNEG_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XNEG | CONNECT_YNEG_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yNeg, zNeg + unit, xNeg + unit, yNeg + unit, zPos - unit);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YNEG_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XPOS | CONNECT_YNEG_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yNeg, zNeg + unit, xPos, yNeg + unit, zPos - unit);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_ZNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_YPOS_ZNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg + unit, yPos - unit, zNeg, xPos - unit, yPos, zNeg + unit);
            if ((connectedFlags & CONNECT_YPOS) != 0)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_ZPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_YPOS_ZPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg + unit, yPos - unit, zPos - unit, xPos - unit, yPos, zPos);
            if ((connectedFlags & CONNECT_YPOS) != 0)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XNEG | CONNECT_YPOS_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yPos - unit, zNeg + unit, xNeg + unit, yPos, zPos - unit);
            if ((connectedFlags & CONNECT_YPOS) != 0)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XPOS | CONNECT_YPOS_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yPos - unit, zNeg + unit, xPos, yPos, zPos - unit);
            if ((connectedFlags & CONNECT_YPOS) != 0)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_ZNEG_XNEG) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yNeg + unit, zNeg, xNeg + unit, yPos - unit, zNeg + unit);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_ZNEG_XPOS) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yNeg + unit, zNeg, xPos, yPos - unit, zNeg + unit);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_ZPOS_XNEG) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yNeg + unit, zPos - unit, xNeg + unit, yPos - unit, zPos);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_ZPOS_XPOS) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yNeg + unit, zPos - unit, xPos, yPos - unit, zPos);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
        }

        // Render corner faces

        if ((connectedFlags & TEST_YNEG_ZNEG_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_YNEG_ZNEG | CONNECT_YNEG_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yNeg + unit, zNeg + unit);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_ZNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZNEG | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YNEG_ZNEG_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_YNEG_ZNEG | CONNECT_YNEG_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yNeg + unit, zNeg + unit);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_ZNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZNEG | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YNEG_ZPOS_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_YNEG_ZPOS | CONNECT_YNEG_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yNeg, zPos - unit, xNeg + unit, yNeg + unit, zPos);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_ZPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZPOS | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YNEG_ZPOS_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_YNEG_ZPOS | CONNECT_YNEG_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yNeg, zPos - unit, xPos, yNeg + unit, zPos);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_ZPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YNEG | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZPOS | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_ZNEG_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_YPOS_ZNEG | CONNECT_YPOS_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yPos - unit, zNeg, xNeg + unit, yPos, zNeg + unit);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_ZNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZNEG | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_ZNEG_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_YPOS_ZNEG | CONNECT_YPOS_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yPos - unit, zNeg, xPos, yPos, zNeg + unit);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_ZNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZNEG | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_ZPOS_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_YPOS_ZPOS | CONNECT_YPOS_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags) {
            renderHelper.setRenderBounds(xNeg, yPos - unit, zPos - unit, xNeg + unit, yPos, zPos);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_ZPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZPOS | CONNECT_XNEG) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
        if ((connectedFlags & TEST_YPOS_ZPOS_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_YPOS_ZPOS | CONNECT_YPOS_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags) {
            renderHelper.setRenderBounds(xPos - unit, yPos - unit, zPos - unit, xPos, yPos, zPos);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_ZPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_YPOS | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
            if ((connectedFlags | CONNECT_ZPOS | CONNECT_XPOS) == connectedFlags)
                renderInteriorFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
        }
    }

    private void renderFace (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos, TextureAtlasSprite icon, float r, float g, float b) {
        switch (face.getIndex()) {
            case ChamRender.YNEG:
            case ChamRender.YPOS:
            case ChamRender.ZPOS:
            case ChamRender.XNEG:
                renderHelper.renderFace(face, blockAccess, state, pos, icon, r, g, b);
                break;
            case ChamRender.ZNEG:
            case ChamRender.XPOS:
                renderHelper.state.flipTexture = flipOpposite;
                renderHelper.renderFace(face, blockAccess, state, pos, icon, r, g, b);
                renderHelper.state.flipTexture = false;
                break;
        }
    }

    private void renderExteriorFace (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos) {
        int index = face.getIndex();
        renderFace(face, blockAccess, state, pos, exteriorIcon[index], exteriorColor[index][0], exteriorColor[index][1], exteriorColor[index][2]);
    }

    private void renderInteriorFace (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos) {
        int index = face.getIndex();
        TextureAtlasSprite icon = interiorIcon[index];
        float r = interiorColor[index][0];
        float g = interiorColor[index][1];
        float b = interiorColor[index][2];

        renderFace(face, blockAccess, state, pos, icon, r, g, b);
    }

    private void renderCutFace (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos) {
        int index = face.getIndex();
        renderFace(face, blockAccess, state, pos, cutIcon[index], cutColor[index][0], cutColor[index][1], cutColor[index][2]);
    }
}
