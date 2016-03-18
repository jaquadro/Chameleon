package com.jaquadro.minecraft.chameleon.render.helpers;

import com.jaquadro.minecraft.chameleon.render.ChamRender;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PanelBoxRenderer
{
    public static final int FACE_YNEG = 0;
    public static final int FACE_YPOS = 1;
    public static final int FACE_ZNEG = 2;
    public static final int FACE_ZPOS = 3;
    public static final int FACE_XNEG = 4;
    public static final int FACE_XPOS = 5;

    private static final int TRIM_CUT = ModularBoxRenderer.CUT_XNEG | ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_ZNEG
        | ModularBoxRenderer.CUT_XPOS | ModularBoxRenderer.CUT_YPOS | ModularBoxRenderer.CUT_ZPOS;

    private ChamRender renderer;
    private ModularBoxRenderer trimRenderer;

    private double trimWidth = 0.0625;
    private double trimDepth = 0;

    private float[] trimColor = new float[3];
    private TextureAtlasSprite trimIcon;

    private float[] panelColor = new float[3];
    private TextureAtlasSprite panelIcon;

    public PanelBoxRenderer (ChamRender renderer) {
        this.renderer = renderer;
        trimRenderer = new ModularBoxRenderer(renderer);
    }

    public void setTrimWidth (double width) {
        trimWidth = width;
    }

    public void setTrimDepth (double depth) {
        trimDepth = depth;
    }

    public void setTrimIcon (TextureAtlasSprite icon) {
        trimIcon = icon;
    }

    public void setTrimColor (float[] color) {
        copyFrom(trimColor, color);
    }

    public void setPanelIcon (TextureAtlasSprite icon) {
        panelIcon = icon;
    }

    public void setPanelColor (float[] color) {
        copyFrom(panelColor, color);
    }

    public void renderFacePanel (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos) {
        switch (face.getIndex()) {
            case FACE_YNEG:
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimDepth, zNeg + trimWidth, xPos - trimWidth, yNeg + trimDepth, zPos - trimWidth);
                renderPanelFace(face, blockAccess, state, pos);
                break;

            case FACE_YPOS:
                renderer.setRenderBounds(xNeg + trimWidth, yPos - trimDepth, zNeg + trimWidth, xPos - trimWidth, yPos - trimDepth, zPos - trimWidth);
                renderPanelFace(face, blockAccess, state, pos);
                break;

            case FACE_ZNEG:
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimWidth, zNeg + trimDepth, xPos - trimWidth, yPos - trimWidth, zNeg + trimDepth);
                renderPanelFace(face, blockAccess, state, pos);
                break;

            case FACE_ZPOS:
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimWidth, zPos - trimDepth, xPos - trimWidth, yPos - trimWidth, zPos - trimDepth);
                renderPanelFace(face, blockAccess, state, pos);
                break;

            case FACE_XNEG:
                renderer.setRenderBounds(xNeg + trimDepth, yNeg + trimWidth, zNeg + trimWidth, xNeg + trimDepth, yPos - trimWidth, zPos - trimWidth);
                renderPanelFace(face, blockAccess, state, pos);
                break;

            case FACE_XPOS:
                renderer.setRenderBounds(xPos - trimDepth, yNeg + trimWidth, zNeg + trimWidth, xPos - trimDepth, yPos - trimWidth, zPos - trimWidth);
                renderPanelFace(face, blockAccess, state, pos);
                break;
        }
    }

    public void renderInteriorTrim (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos) {
        switch (face.getIndex()) {
            case FACE_YNEG:
                renderer.setRenderBounds(xNeg + trimWidth, yNeg, zPos - trimWidth, xPos - trimWidth, yNeg + trimDepth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yNeg, zNeg + trimWidth, xPos - trimWidth, yNeg + trimDepth, zNeg + trimWidth);
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimWidth, yNeg, zNeg + trimWidth, xPos - trimWidth, yNeg + trimDepth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yNeg, zNeg + trimWidth, xNeg + trimWidth, yNeg + trimDepth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
                break;

            case FACE_YPOS:
                renderer.setRenderBounds(xNeg + trimWidth, yPos - trimDepth, zPos - trimWidth, xPos - trimWidth, yPos, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yPos - trimDepth, zNeg + trimWidth, xPos - trimWidth, yPos, zNeg + trimWidth);
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimWidth, yPos - trimDepth, zNeg + trimWidth, xPos - trimWidth, yPos, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yPos - trimDepth, zNeg + trimWidth, xNeg + trimWidth, yPos, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
                break;

            case FACE_ZNEG:
                renderer.setRenderBounds(xNeg + trimWidth, yPos - trimWidth, zNeg, xPos - trimWidth, yPos - trimWidth, zNeg + trimDepth);
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimWidth, zNeg, xPos - trimWidth, yNeg + trimWidth, zNeg + trimDepth);
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimWidth, yNeg + trimWidth, zNeg, xPos - trimWidth, yPos - trimWidth, zNeg + trimDepth);
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimWidth, zNeg, xNeg + trimWidth, yPos - trimWidth, zNeg + trimDepth);
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
                break;

            case FACE_ZPOS:
                renderer.setRenderBounds(xNeg + trimWidth, yPos - trimWidth, zPos - trimDepth, xPos - trimWidth, yPos - trimWidth, zPos);
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimWidth, zPos - trimDepth, xPos - trimWidth, yNeg + trimWidth, zPos);
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimWidth, yNeg + trimWidth, zPos - trimDepth, xPos - trimWidth, yPos - trimWidth, zPos);
                renderCutFace(ChamRender.FACE_XNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + trimWidth, yNeg + trimWidth, zPos - trimDepth, xNeg + trimWidth, yPos - trimWidth, zPos);
                renderCutFace(ChamRender.FACE_XPOS, blockAccess, state, pos);
                break;

            case FACE_XNEG:
                renderer.setRenderBounds(xNeg, yNeg + trimWidth, zPos - trimWidth, xNeg + trimDepth, yPos - trimWidth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg + trimWidth, zNeg + trimWidth, xNeg + trimDepth, yPos - trimWidth, zNeg + trimWidth);
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos - trimWidth, zNeg + trimWidth, xNeg + trimDepth, yPos - trimWidth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg + trimWidth, zNeg + trimWidth, xNeg + trimDepth, yNeg + trimWidth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
                break;

            case FACE_XPOS:
                renderer.setRenderBounds(xPos - trimDepth, yNeg + trimWidth, zPos - trimWidth, xPos, yPos - trimWidth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_ZNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimDepth, yNeg + trimWidth, zNeg + trimWidth, xPos, yPos - trimWidth, zNeg + trimWidth);
                renderCutFace(ChamRender.FACE_ZPOS, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimDepth, yPos - trimWidth, zNeg + trimWidth, xPos, yPos - trimWidth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_YNEG, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - trimDepth, yNeg + trimWidth, zNeg + trimWidth, xPos, yNeg + trimWidth, zPos - trimWidth);
                renderCutFace(ChamRender.FACE_YPOS, blockAccess, state, pos);
                break;
        }
    }

    public void renderFaceTrim (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos, double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos) {
        double unit = trimWidth;

        switch (face.getIndex()) {
            case FACE_YNEG:
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yNeg, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yNeg, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg, zPos - unit, xNeg + unit, yNeg, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg, zPos - unit, xPos, yNeg, zPos);
                renderCutFace(face, blockAccess, state, pos);

                renderer.setRenderBounds(xNeg + unit, yNeg, zNeg, xPos - unit, yNeg, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + unit, yNeg, zPos - unit, xPos - unit, yNeg, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg, zNeg + unit, xNeg + unit, yNeg, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg, zNeg + unit, xPos, yNeg, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                break;

            case FACE_YPOS:
                renderer.setRenderBounds(xNeg, yPos, zNeg, xNeg + unit, yPos, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yPos, zNeg, xPos, yPos, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos, zPos - unit, xNeg + unit, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yPos, zPos - unit, xPos, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);

                renderer.setRenderBounds(xNeg + unit, yPos, zNeg, xPos - unit, yPos, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + unit, yPos, zPos - unit, xPos - unit, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos, zNeg + unit, xNeg + unit, yPos, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yPos, zNeg + unit, xPos, yPos, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                break;

            case FACE_ZNEG:
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yNeg + unit, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yNeg + unit, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos - unit, zNeg, xNeg + unit, yPos, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yPos - unit, zNeg, xPos, yPos, zNeg);
                renderCutFace(face, blockAccess, state, pos);

                renderer.setRenderBounds(xNeg + unit, yNeg, zNeg, xPos - unit, yNeg + unit, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + unit, yPos - unit, zNeg, xPos - unit, yPos, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg + unit, zNeg, xNeg + unit, yPos - unit, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg + unit, zNeg, xPos, yPos - unit, zNeg);
                renderCutFace(face, blockAccess, state, pos);
                break;

            case FACE_ZPOS:
                renderer.setRenderBounds(xNeg, yNeg, zPos, xNeg + unit, yNeg + unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg, zPos, xPos, yNeg + unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos - unit, zPos, xNeg + unit, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yPos - unit, zPos, xPos, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);

                renderer.setRenderBounds(xNeg + unit, yNeg, zPos, xPos - unit, yNeg + unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg + unit, yPos - unit, zPos, xPos - unit, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg + unit, zPos, xNeg + unit, yPos - unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos - unit, yNeg + unit, zPos, xPos, yPos - unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                break;

            case FACE_XNEG:
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xNeg, yNeg + unit, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos - unit, zNeg, xNeg, yPos, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg, zPos - unit, xNeg, yNeg + unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos - unit, zPos - unit, xNeg, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);

                renderer.setRenderBounds(xNeg, yNeg + unit, zNeg, xNeg, yPos - unit, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg + unit, zPos - unit, xNeg, yPos - unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yNeg, zNeg + unit, xNeg, yNeg + unit, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xNeg, yPos - unit, zNeg + unit, xNeg, yPos, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                break;

            case FACE_XPOS:
                renderer.setRenderBounds(xPos, yNeg, zNeg, xPos, yNeg + unit, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos, yPos - unit, zNeg, xPos, yPos, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos, yNeg, zPos - unit, xPos, yNeg + unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos, yPos - unit, zPos - unit, xPos, yPos, zPos);
                renderCutFace(face, blockAccess, state, pos);

                renderer.setRenderBounds(xPos, yNeg + unit, zNeg, xPos, yPos - unit, zNeg + unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos, yNeg + unit, zPos - unit, xPos, yPos - unit, zPos);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos, yNeg, zNeg + unit, xPos, yNeg + unit, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                renderer.setRenderBounds(xPos, yPos - unit, zNeg + unit, xPos, yPos, zPos - unit);
                renderCutFace(face, blockAccess, state, pos);
                break;
        }
    }

    private void renderCutFace (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos) {
        renderer.renderFace(face, blockAccess, state, pos, trimIcon, trimColor[0], trimColor[1], trimColor[2]);
    }

    private void renderPanelFace (EnumFacing face, IBlockAccess blockAccess, IBlockState state, BlockPos pos) {
        renderer.renderFace(face, blockAccess, state, pos, panelIcon, panelColor[0], panelColor[1], panelColor[2]);
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
}
