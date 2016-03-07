package com.jaquadro.minecraft.chameleon.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class ChamRenderLL
{
    private static final int TL = 0;
    private static final int BL = 1;
    private static final int BR = 2;
    private static final int TR = 3;

    private static final int MINX = 0;
    private static final int MAXX = 1;
    private static final int MINY = 2;
    private static final int MAXY = 3;
    private static final int MINZ = 4;
    private static final int MAXZ = 5;

    private static final int xyzuvMap[][][] = {
        {       // Y-NEG
            { 0, 2, 5, 0, 3 },
            { 0, 2, 4, 0, 2 },
            { 1, 2, 4, 1, 2 },
            { 1, 2, 5, 1, 3 }
        }, {    // Y-POS
            { 1, 3, 5, 1, 3 },
            { 1, 3, 4, 1, 2 },
            { 0, 3, 4, 0, 2 },
            { 0, 3, 5, 0, 3 }
        }, {    // Z-NEG
            { 0, 3, 4, 1, 2 },
            { 1, 3, 4, 0, 2 },
            { 1, 2, 4, 0, 3 },
            { 0, 2, 4, 1, 3 }
        }, {    // Z-POS
            { 0, 3, 5, 0, 2 },
            { 0, 2, 5, 0, 3 },
            { 1, 2, 5, 1, 3 },
            { 1, 3, 5, 1, 2 }
        }, {    // X-NEG
            { 0, 3, 5, 1, 2 },
            { 0, 3, 4, 0, 2 },
            { 0, 2, 4, 0, 3 },
            { 0, 2, 5, 1, 3 }
        }, {    // X-POS
            { 1, 2, 5, 0, 3 },
            { 1, 2, 4, 1, 3 },
            { 1, 3, 4, 1, 2 },
            { 1, 3, 5, 0, 2 }
        },
    };

    private ChamRenderState state;

    private double[] minUDiv = new double[24];
    private double[] maxUDiv = new double[24];
    private double[] minVDiv = new double[24];
    private double[] maxVDiv = new double[24];

    private int[][] brightnessLerp = new int[10][10];

    // u-min, u-max, v-min, v-max
    private double[] uv = new double[4];

    // x-min, x-max, y-min, y-max, z-min, z-max
    private double[] xyz = new double[6];

    private boolean isBaking;
    private VertexFormat bakedFormat;
    private int bakedTintIndex = -1;
    private List<BakedQuad> quadBuffer;

    public ChamRenderLL (ChamRenderState state) {
        this.state = state;
    }

    public void startBaking (VertexFormat format) {
        startBaking(format, -1);
    }

    public void startBaking (VertexFormat format, int tintIndex) {
        isBaking = true;
        quadBuffer = new ArrayList<BakedQuad>();
        bakedFormat = format;
        bakedTintIndex = tintIndex;
    }

    public List<BakedQuad> stopBaking () {
        isBaking = false;
        List<BakedQuad> result = quadBuffer;
        quadBuffer = null;
        return result;
    }

    public VertexFormat getVertexFormat () {
        return bakedFormat;
    }

    public void drawFace (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        switch (face.getIndex()) {
            case ChamRender.YNEG:
            case ChamRender.YPOS:
                drawFaceY(face, x, y, z, icon);
                break;
            case ChamRender.ZNEG:
            case ChamRender.ZPOS:
                drawFaceZ(face, x, y, z, icon);
                break;
            case ChamRender.XNEG:
            case ChamRender.XPOS:
                drawFaceX(face, x, y, z, icon);
                break;
        }
    }

    private void drawFaceY (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        int rangeX = (int)(Math.ceil(state.renderMaxX + state.shiftU) - Math.floor(state.renderMinX + state.shiftU));
        int rangeZ = (int)(Math.ceil(state.renderMaxZ + state.shiftV) - Math.floor(state.renderMinZ + state.shiftV));

        setXYZ(x, y, z);
        if (state.renderFromInside) {
            xyz[MINX] = z + state.renderMaxX;
            xyz[MAXX] = z + state.renderMinX;
        }

        if (rangeX <= 1 && rangeZ <= 1) {
            setUV(icon, state.renderMinX + state.shiftU, state.renderMinZ + state.shiftV, state.renderMaxX + state.shiftU, state.renderMaxZ + state.shiftV);

            if (state.enableAO)
                renderXYZUVAO(face);
            else
                renderXYZUV(face);
            return;
        }

        double uStart = (state.renderMinX + state.shiftU + rangeX) % 1.0;
        double uStop = (state.renderMaxX + state.shiftU + rangeX) % 1.0;
        double vStart = (state.renderMinZ + state.shiftV + rangeZ) % 1.0;
        double vStop = (state.renderMaxZ + state.shiftV + rangeZ) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeX, rangeZ);
        setupAOBrightnessLerp(state.renderMinX, state.renderMaxX, state.renderMinZ, state.renderMaxZ, rangeX, rangeZ);

        int rotate = (face == EnumFacing.DOWN) ? state.uvRotate[0] : state.uvRotate[1];

        for (int ix = 0; ix < rangeX; ix++) {
            xyz[MAXX] = xyz[MINX] + maxUDiv[ix] - minUDiv[ix];
            xyz[MINZ] = z + state.renderMinZ;

            for (int iz = 0; iz < rangeZ; iz++) {
                xyz[MAXZ] = xyz[MINZ] + maxVDiv[iz] - minVDiv[iz];

                state.brightnessTopLeft = brightnessLerp[ix][iz];
                state.brightnessTopRight = brightnessLerp[ix + 1][iz];
                state.brightnessBottomLeft = brightnessLerp[ix][iz + 1];
                state.brightnessBottomRight = brightnessLerp[ix + 1][iz + 1];

                switch (rotate) {
                    case ChamRenderState.ROTATE90:
                        setUV(icon, maxVDiv[ix], minUDiv[iz], minVDiv[ix], maxUDiv[iz]);
                        break;
                    case ChamRenderState.ROTATE180:
                        setUV(icon, maxUDiv[ix], maxVDiv[iz], minUDiv[ix], minVDiv[iz]);
                        break;
                    case ChamRenderState.ROTATE270:
                        setUV(icon, minVDiv[ix], maxUDiv[iz], maxVDiv[ix], minUDiv[iz]);
                        break;
                    default:
                        setUV(icon, minUDiv[ix], minVDiv[iz], maxUDiv[ix], maxVDiv[iz]);
                        break;
                }

                renderXYZUVAO(face);

                xyz[MINZ] = xyz[MAXZ];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    private void drawFaceZ (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        int rangeX = (int)(Math.ceil(state.renderMaxX + state.shiftU) - Math.floor(state.renderMinX + state.shiftU));
        int rangeY = (int)(Math.ceil(state.renderMaxY + state.shiftV) - Math.floor(state.renderMinY + state.shiftV));

        setXYZ(x, y, z);
        if (state.renderFromInside) {
            xyz[MINX] = z + state.renderMaxX;
            xyz[MAXX] = z + state.renderMinX;
        }

        if (rangeX <= 1 && rangeY <= 1) {
            if (state.flipTexture)
                setUV(icon, state.renderMaxX + state.shiftU, 1 - state.renderMaxY + state.shiftV, state.renderMinX + state.shiftU, 1 - state.renderMinY + state.shiftV);
            else
                setUV(icon, state.renderMinX + state.shiftU, 1 - state.renderMaxY + state.shiftV, state.renderMaxX + state.shiftU, 1 - state.renderMinY + state.shiftV);

            if (state.enableAO)
                renderXYZUVAO(face);
            else
                renderXYZUV(face);
            return;
        }

        double uStart = (state.renderMinX + state.shiftU + rangeX) % 1.0;
        double uStop = (state.renderMaxX + state.shiftU + rangeX) % 1.0;
        double vStart = (state.renderMinY + state.shiftV + rangeY) % 1.0;
        double vStop = (state.renderMaxY + state.shiftV + rangeY) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeX, rangeY);
        setupAOBrightnessLerp(state.renderMinX, state.renderMaxX, state.renderMinY, state.renderMaxY, rangeX, rangeY);

        for (int ix = 0; ix < rangeX; ix++) {
            xyz[MAXX] = xyz[MINX] + maxUDiv[ix] - minUDiv[ix];
            xyz[MINY] = y + state.renderMinY;

            for (int iy = 0; iy < rangeY; iy++) {
                xyz[MAXY] = xyz[MINY] + maxVDiv[iy] - minVDiv[iy];

                state.brightnessTopLeft = brightnessLerp[ix][iy];
                state.brightnessTopRight = brightnessLerp[ix + 1][iy];
                state.brightnessBottomLeft = brightnessLerp[ix][iy + 1];
                state.brightnessBottomRight = brightnessLerp[ix + 1][iy + 1];

                if (state.flipTexture)
                    setUV(icon, 1 - minUDiv[ix], minVDiv[iy], 1 - maxUDiv[ix], maxVDiv[iy]);
                else
                    setUV(icon, minUDiv[ix], minVDiv[iy], maxUDiv[ix], maxVDiv[iy]);

                renderXYZUVAO(face);

                xyz[MINY] = xyz[MAXY];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    private void drawFaceX (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        int rangeZ = (int)(Math.ceil(state.renderMaxZ + state.shiftU) - Math.floor(state.renderMinZ + state.shiftU));
        int rangeY = (int)(Math.ceil(state.renderMaxY + state.shiftV) - Math.floor(state.renderMinY + state.shiftV));

        setXYZ(x, y, z);
        if (state.renderFromInside) {
            xyz[MINZ] = z + state.renderMaxZ;
            xyz[MAXZ] = z + state.renderMinZ;
        }

        if (rangeZ <= 1 && rangeY <= 1) {

            if (state.flipTexture)
                setUV(icon, state.renderMaxZ + state.shiftU, 1 - state.renderMaxY + state.shiftV, state.renderMinZ + state.shiftU, 1 - state.renderMinY + state.shiftV);
            else
                setUV(icon, state.renderMinZ + state.shiftU, 1 - state.renderMaxY + state.shiftV, state.renderMaxZ + state.shiftU, 1 - state.renderMinY + state.shiftV);

            if (state.enableAO)
                renderXYZUVAO(face);
            else
                renderXYZUV(face);
            return;
        }

        double uStart = (state.renderMinZ + state.shiftU + rangeZ) % 1.0;
        double uStop = (state.renderMaxZ + state.shiftU + rangeZ) % 1.0;
        double vStart = (state.renderMinY + state.shiftV + rangeY) % 1.0;
        double vStop = (state.renderMaxY + state.shiftV + rangeY) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeZ, rangeY);
        setupAOBrightnessLerp(state.renderMinZ, state.renderMaxZ, state.renderMinY, state.renderMaxY, rangeZ, rangeY);

        for (int iz = 0; iz < rangeZ; iz++) {
            xyz[MAXZ] = xyz[MINZ] + maxUDiv[iz] - minUDiv[iz];
            xyz[MINY] = y + state.renderMinY;

            for (int iy = 0; iy < rangeY; iy++) {
                xyz[MAXY] = xyz[MINY] + maxVDiv[iy] - minVDiv[iy];

                state.brightnessTopLeft = brightnessLerp[iz][iy];
                state.brightnessTopRight = brightnessLerp[iz + 1][iy];
                state.brightnessBottomLeft = brightnessLerp[iz][iy + 1];
                state.brightnessBottomRight = brightnessLerp[iz + 1][iy + 1];

                if (state.flipTexture)
                    setUV(icon, 1 - minUDiv[iz], minVDiv[iy], 1 - maxUDiv[iz], maxVDiv[iy]);
                else
                    setUV(icon, minUDiv[iz], minVDiv[iy], maxUDiv[iz], maxVDiv[iy]);

                renderXYZUVAO(face);

                xyz[MINY] = xyz[MAXY];
            }
            xyz[MINZ] = xyz[MAXZ];
        }
    }

    public void drawPartialFace (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax) {
        setXYZ(x, y, z);
        setUV(icon, uMin, vMin, uMax, vMax);

        if (state.enableAO)
            renderXYZUVAO(face);
        else
            renderXYZUV(face);
    }

    private void setupUVPoints (double uStart, double vStart, double uStop, double vStop, int rangeU, int rangeV) {
        if (rangeU <= 1) {
            minUDiv[0] = uStart;
            maxUDiv[0] = uStop;
        }
        else {
            minUDiv[0] = uStart;
            maxUDiv[0] = 1;
            for (int i = 1; i < rangeU - 1; i++) {
                minUDiv[i] = 0;
                maxUDiv[i] = 1;
            }
            minUDiv[rangeU - 1] = 0;
            maxUDiv[rangeU - 1] = uStop;
        }

        if (rangeV <= 1) {
            minVDiv[0] = vStart;
            maxVDiv[0] = vStop;
        }
        else {
            minVDiv[0] = vStart;
            maxVDiv[0] = 1;
            for (int i = 1; i < rangeV - 1; i++) {
                minVDiv[i] = 0;
                maxVDiv[i] = 1;
            }
            minVDiv[rangeV - 1] = 0;
            maxVDiv[rangeV - 1] = vStop;
        }
    }

    private void setupAOBrightnessLerp (double left, double right, double top, double bottom, int rangeLR, int rangeTB) {
        double diffLR = right - left;
        double diffTB = bottom - top;

        double posLR = left;

        for (int lr = 0; lr <= rangeLR; lr++) {
            float lerpLR = (float)(posLR / diffLR);

            int brightTop = ChamRenderAO.mixAOBrightness(state.brightnessTopLeft, state.brightnessTopRight, 1 - lerpLR, lerpLR);
            int brightBottom = ChamRenderAO.mixAOBrightness(state.brightnessBottomLeft, state.brightnessBottomRight, 1 - lerpLR, lerpLR);

            double posTB = top;
            for (int tb = 0; tb <= rangeTB; tb++) {
                float lerpTB = (float)(posTB / diffTB);

                brightnessLerp[lr][tb] = ChamRenderAO.mixAOBrightness(brightTop, brightBottom, 1 - lerpTB, lerpTB);

                if (tb < rangeTB)
                    posTB += maxVDiv[tb] - minVDiv[tb];
            }

            if (lr < rangeLR)
                posLR += maxUDiv[lr] - minUDiv[lr];
        }
    }

    private void setUV (TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax) {
        uv[0] = icon.getInterpolatedU(uMin * 16);
        uv[1] = icon.getInterpolatedU(uMax * 16);
        uv[2] = icon.getInterpolatedV(vMin * 16);
        uv[3] = icon.getInterpolatedV(vMax * 16);
    }

    private void setUV (double uMin, double vMin, double uMax, double vMax) {
        uv[0] = uMin;
        uv[1] = uMax;
        uv[2] = vMin;
        uv[3] = vMax;
    }

    private void setXYZ (double x, double y, double z) {
        xyz[0] = x + state.renderMinX;
        xyz[1] = x + state.renderMaxX;
        xyz[2] = y + state.renderMinY;
        xyz[3] = y + state.renderMaxY;
        xyz[4] = z + state.renderMinZ;
        xyz[5] = z + state.renderMaxZ;
    }

    private void renderXYZUV (EnumFacing facing) {
        if (isBaking) {
            if (bakedFormat == DefaultVertexFormats.BLOCK)
                quadBuffer.add(bakeXYZUVBlock(facing, state.color, state.color, state.color, state.color));
            else if (bakedFormat == DefaultVertexFormats.ITEM)
                quadBuffer.add(bakeXYZUVItem(facing, state.color, state.color, state.color, state.color));
            return;
        }

        int[][] index = xyzuvMap[facing.getIndex()];
        setBlockVertex(index[TL], state.color, state.brightness);
        setBlockVertex(index[BL], state.color, state.brightness);
        setBlockVertex(index[BR], state.color, state.brightness);
        setBlockVertex(index[TR], state.color, state.brightness);
    }

    private void renderXYZUVAO (EnumFacing facing) {

        if (isBaking) {
            if (bakedFormat == DefaultVertexFormats.BLOCK)
                quadBuffer.add(bakeXYZUVBlock(facing, state.color, state.color, state.color, state.color));
            else if (bakedFormat == DefaultVertexFormats.ITEM)
                quadBuffer.add(bakeXYZUVItem(facing, state.color, state.color, state.color, state.color));
            return;
        }

        int[][] index = xyzuvMap[facing.getIndex()];
        setBlockVertex(index[TL], state.colorTopLeft, state.brightnessTopLeft);
        setBlockVertex(index[BL], state.colorBottomLeft, state.brightnessBottomLeft);
        setBlockVertex(index[BR], state.colorBottomRight, state.brightnessBottomRight);
        setBlockVertex(index[TR], state.colorTopRight, state.brightnessTopRight);
    }

    private void setBlockVertex (int[] xumap, float[] color, int brightness) {
        WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();

        if (tessellator.getVertexFormat().hasNormal()) {
            tessellator.pos(xyz[xumap[0]], xyz[xumap[1]], xyz[xumap[2]])
                .color(color[0], color[1], color[2], 1)
                .tex(uv[xumap[3]], uv[xumap[4]])
                .normal(state.normal[0], state.normal[1], state.normal[2])
                .endVertex();
        }
        else {
            int lsky = (brightness >> 16) & 255;
            int lblk = (brightness & 255);

            tessellator.pos(xyz[xumap[0]], xyz[xumap[1]], xyz[xumap[2]])
                .tex(uv[xumap[3]], uv[xumap[4]])
                .lightmap(lsky, lblk)
                .color(color[0], color[1], color[2], 1)
                .endVertex();
        }
    }

    private BakedQuad bakeXYZUVBlock (EnumFacing facing, float[] colorTL, float[] colorBL, float[] colorBR, float[] colorTR) {
        int[][] index = xyzuvMap[facing.getIndex()];
        int[] mapTL = index[TL];
        int[] mapBL = index[BL];
        int[] mapBR = index[BR];
        int[] mapTR = index[TR];

        int light = 0xFFFFFFFF;

        return new BakedQuad(new int[] {
            Float.floatToRawIntBits((float)xyz[mapTL[0]]),
            Float.floatToRawIntBits((float)xyz[mapTL[1]]),
            Float.floatToRawIntBits((float)xyz[mapTL[2]]),
            packColor(colorTL),
            Float.floatToRawIntBits((float)uv[mapTL[3]]),
            Float.floatToRawIntBits((float)uv[mapTL[4]]),
            light,

            Float.floatToRawIntBits((float)xyz[mapBL[0]]),
            Float.floatToRawIntBits((float)xyz[mapBL[1]]),
            Float.floatToRawIntBits((float)xyz[mapBL[2]]),
            packColor(colorBL),
            Float.floatToRawIntBits((float)uv[mapBL[3]]),
            Float.floatToRawIntBits((float)uv[mapBL[4]]),
            light,

            Float.floatToRawIntBits((float)xyz[mapBR[0]]),
            Float.floatToRawIntBits((float)xyz[mapBR[1]]),
            Float.floatToRawIntBits((float)xyz[mapBR[2]]),
            packColor(colorBR),
            Float.floatToRawIntBits((float)uv[mapBR[3]]),
            Float.floatToRawIntBits((float)uv[mapBR[4]]),
            light,

            Float.floatToRawIntBits((float)xyz[mapTR[0]]),
            Float.floatToRawIntBits((float)xyz[mapTR[1]]),
            Float.floatToRawIntBits((float)xyz[mapTR[2]]),
            packColor(colorTR),
            Float.floatToRawIntBits((float)uv[mapTR[3]]),
            Float.floatToRawIntBits((float)uv[mapTR[4]]),
            light
        }, -1, facing);
    }

    private BakedQuad bakeXYZUVItem (EnumFacing facing, float[] colorTL, float[] colorBL, float[] colorBR, float[] colorTR) {
        int[][] index = xyzuvMap[facing.getIndex()];
        int[] mapTL = index[TL];
        int[] mapBL = index[BL];
        int[] mapBR = index[BR];
        int[] mapTR = index[TR];

        return new BakedQuad(new int[] {
            Float.floatToRawIntBits((float)xyz[mapTL[0]]),
            Float.floatToRawIntBits((float)xyz[mapTL[1]]),
            Float.floatToRawIntBits((float)xyz[mapTL[2]]),
            packColor(colorTL),
            Float.floatToRawIntBits((float)uv[mapTL[3]]),
            Float.floatToRawIntBits((float)uv[mapTL[4]]),
            packNormal(state.normal),

            Float.floatToRawIntBits((float)xyz[mapBL[0]]),
            Float.floatToRawIntBits((float)xyz[mapBL[1]]),
            Float.floatToRawIntBits((float)xyz[mapBL[2]]),
            packColor(colorBL),
            Float.floatToRawIntBits((float)uv[mapBL[3]]),
            Float.floatToRawIntBits((float)uv[mapBL[4]]),
            packNormal(state.normal),

            Float.floatToRawIntBits((float)xyz[mapBR[0]]),
            Float.floatToRawIntBits((float)xyz[mapBR[1]]),
            Float.floatToRawIntBits((float)xyz[mapBR[2]]),
            packColor(colorBR),
            Float.floatToRawIntBits((float)uv[mapBR[3]]),
            Float.floatToRawIntBits((float)uv[mapBR[4]]),
            packNormal(state.normal),

            Float.floatToRawIntBits((float)xyz[mapTR[0]]),
            Float.floatToRawIntBits((float)xyz[mapTR[1]]),
            Float.floatToRawIntBits((float)xyz[mapTR[2]]),
            packColor(colorTR),
            Float.floatToRawIntBits((float)uv[mapTR[3]]),
            Float.floatToRawIntBits((float)uv[mapTR[4]]),
            packNormal(state.normal)
        }, -1, facing);
    }

    private int packColor (float[] color) {
        int r = MathHelper.clamp_int((int)(color[0] * 255), 0, 255);
        int g = MathHelper.clamp_int((int)(color[1] * 255), 0, 255);
        int b = MathHelper.clamp_int((int)(color[2] * 255), 0, 255);

        return (255 << 24 | r << 16 | g << 8 | b);
    }

    private int packNormal (float[] normal) {
        int nx = (int)(normal[0] * 127) & 255;
        int ny = (int)(normal[1] * 127) & 255;
        int nz = (int)(normal[2] * 127) & 255;

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            return nx | ny << 8 | nz << 16;
        else
            return nx << 24 | ny << 16 | nz << 8;
    }
}
