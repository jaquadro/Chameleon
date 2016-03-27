package com.jaquadro.minecraft.chameleon.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Array;
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
            { 0, 2, 5, 0, 2 },
            { 0, 2, 4, 0, 3 },
            { 1, 2, 4, 1, 3 },
            { 1, 2, 5, 1, 2 }
        }, {    // Y-POS
            { 1, 3, 5, 0, 2 },
            { 1, 3, 4, 0, 3 },
            { 0, 3, 4, 1, 3 },
            { 0, 3, 5, 1, 2 }
        }, {    // Z-NEG
            { 0, 3, 4, 0, 2 },
            { 1, 3, 4, 1, 2 },
            { 1, 2, 4, 1, 3 },
            { 0, 2, 4, 0, 3 }
        }, {    // Z-POS
            { 0, 3, 5, 0, 2 },
            { 0, 2, 5, 0, 3 },
            { 1, 2, 5, 1, 3 },
            { 1, 3, 5, 1, 2 }
        }, {    // X-NEG
            { 0, 3, 5, 0, 2 },
            { 0, 3, 4, 1, 2 },
            { 0, 2, 4, 1, 3 },
            { 0, 2, 5, 0, 3 }
        }, {    // X-POS
            { 1, 2, 5, 0, 2 },
            { 1, 2, 4, 1, 2 },
            { 1, 3, 4, 1, 3 },
            { 1, 3, 5, 0, 3 }
        },
    };

    private ChamRenderState state;
    private WorldRenderer tessellator;

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
    private boolean faceGroup;
    private VertexFormat bakedFormat;
    private int bakedTintIndex = -1;
    private boolean diffuseLight = false;

    private List<BakedQuad>[] quadBuffer;

    public ChamRenderLL (ChamRenderState state) {
        this.state = state;
    }

    public void startBaking (VertexFormat format) {
        startBaking(format, -1, false);
    }

    public void startBaking (VertexFormat format, int tintIndex) {
        startBaking(format, tintIndex, false);
    }

    public void startBaking (VertexFormat format, int tintIndex, boolean applyDiffuseLight) {
        isBaking = true;
        bakedFormat = format;
        bakedTintIndex = tintIndex;
        diffuseLight = applyDiffuseLight;

        if (quadBuffer == null) {
            @SuppressWarnings("unchecked")
            final List<BakedQuad>[] newBuffer = (List[]) Array.newInstance(ArrayList.class, 7);
            quadBuffer = newBuffer;

            for (int i = 0; i < quadBuffer.length; i++)
                quadBuffer[i] = new ArrayList<BakedQuad>();
        }

        for (List<BakedQuad> buffer : quadBuffer)
            buffer.clear();
    }

    public void stopBaking () {
        isBaking = false;
    }

    public List<BakedQuad> takeBakedQuads (EnumFacing dir) {
        int index = (dir == null) ? 6 : dir.getIndex();
        List<BakedQuad> result = quadBuffer[index];
        quadBuffer[index] = new ArrayList<BakedQuad>();
        return result;
    }

    public boolean isBaking () {
        return isBaking;
    }

    public VertexFormat getVertexFormat () {
        return bakedFormat;
    }

    public void setTesseleator (WorldRenderer tessellator) {
        this.tessellator = tessellator;
    }

    public void drawFace (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon, boolean isFaceGroup) {
        faceGroup = isFaceGroup;
        drawFace(face, x, y, z, icon);
        faceGroup = false;
    }

    public void drawFace (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        boolean flip = state.flipTexture;

        switch (face.getIndex()) {
            case ChamRender.YNEG:
            case ChamRender.YPOS:
                drawFaceY(face, x, y, z, icon);
                break;
            case ChamRender.ZNEG:
            case ChamRender.ZPOS:
                if (state.rotateTransform == ChamRenderState.ROTATE180 || state.rotateTransform == ChamRenderState.ROTATE90)
                    state.flipTexture = !state.flipTexture;
                drawFaceZ(face, x, y, z, icon);
                break;
            case ChamRender.XNEG:
            case ChamRender.XPOS:
                if (state.rotateTransform == ChamRenderState.ROTATE180 || state.rotateTransform == ChamRenderState.ROTATE270)
                    state.flipTexture = !state.flipTexture;
                drawFaceX(face, x, y, z, icon);
                break;
        }

        state.flipTexture = flip;
    }

    private void drawFaceY (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        if (icon == null)
            return;

        int rangeX = (int)(Math.ceil(state.renderMaxX + state.shiftU) - Math.floor(state.renderMinX + state.shiftU));
        int rangeZ = (int)(Math.ceil(state.renderMaxZ + state.shiftV) - Math.floor(state.renderMinZ + state.shiftV));

        setXYZ(x, y, z);
        if (state.renderFromInside) {
            xyz[MINX] = z + state.renderMaxX;
            xyz[MAXX] = z + state.renderMinX;
        }

        int rotate = (face == ChamRender.FACE_YNEG) ? state.uvRotate[ChamRender.YNEG] : state.uvRotate[ChamRender.YPOS];

        if (rangeX <= 1 && rangeZ <= 1) {
            if (face == ChamRender.FACE_YNEG)
                setFaceYNegUV(icon, state.renderMinX, state.renderMinZ, state.renderMaxX, state.renderMaxZ);
            else
                setFaceYPosUV(icon, state.renderMinX, state.renderMinZ, state.renderMaxX, state.renderMaxZ);

            if (state.enableAO)
                renderXYZUVAO(face, icon);
            else
                renderXYZUV(face, icon);
            return;
        }

        double uStart = (state.renderMinX + state.shiftU + rangeX) % 1.0;
        double uStop = (state.renderMaxX + state.shiftU + rangeX) % 1.0;
        double vStart = (state.renderMinZ + state.shiftV + rangeZ) % 1.0;
        double vStop = (state.renderMaxZ + state.shiftV + rangeZ) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeX, rangeZ);
        setupAOBrightnessLerp(state.renderMinX, state.renderMaxX, state.renderMinZ, state.renderMaxZ, rangeX, rangeZ);

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
                        setUV(icon, maxVDiv[ix], minVDiv[ix], minUDiv[iz], maxUDiv[iz]);
                        break;
                    case ChamRenderState.ROTATE180:
                        setUV(icon, maxUDiv[ix], minUDiv[ix], maxVDiv[iz], minVDiv[iz]);
                        break;
                    case ChamRenderState.ROTATE270:
                        setUV(icon, minVDiv[ix], maxVDiv[ix], maxUDiv[iz], minUDiv[iz]);
                        break;
                    default:
                        setUV(icon, minUDiv[ix], maxUDiv[ix], minVDiv[iz], maxVDiv[iz]);
                        break;
                }

                renderXYZUVAO(face, icon);

                xyz[MINZ] = xyz[MAXZ];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    private void setFaceYNegUV (TextureAtlasSprite icon, double minX, double minZ, double maxX, double maxZ) {
        int rotate = state.uvRotate[ChamRender.YNEG];
        if (rotate == ChamRenderState.ROTATE0)
            setUV(icon, minX + state.shiftU, maxX + state.shiftU, maxZ + state.shiftV, minZ + state.shiftV);
        if (rotate == ChamRenderState.ROTATE90)
            setUV(icon, 1 - maxZ + state.shiftU, 1 - minZ + state.shiftU, minX + state.shiftV, maxX + state.shiftV);
        if (rotate == ChamRenderState.ROTATE180)
            setUV(icon, 1 - minX + state.shiftU, 1 - maxX + state.shiftU, 1 - maxZ + state.shiftV, 1 - minZ + state.shiftV);
        if (rotate == ChamRenderState.ROTATE270)
            setUV(icon, maxZ + state.shiftU, minZ + state.shiftU, 1 - minX + state.shiftV, 1 - maxX + state.shiftV);
    }

    private void setFaceYPosUV (TextureAtlasSprite icon, double minX, double minZ, double maxX, double maxZ) {
        int rotate = state.uvRotate[ChamRender.YPOS];
        if (rotate == ChamRenderState.ROTATE0)
            setUV(icon, maxX + state.shiftU, minX + state.shiftU, maxZ + state.shiftV, minZ + state.shiftV);
        if (rotate == ChamRenderState.ROTATE90)
            setUV(icon, maxZ + state.shiftU, minZ + state.shiftU, 1 - maxX + state.shiftV, 1 - minX + state.shiftV);
        if (rotate == ChamRenderState.ROTATE180)
            setUV(icon, 1 - maxX + state.shiftU, 1 - minX + state.shiftU, 1 - maxZ + state.shiftV, 1 - minZ + state.shiftV);
        if (rotate == ChamRenderState.ROTATE270)
            setUV(icon, 1 - maxZ + state.shiftU, 1 - minZ + state.shiftU, maxX + state.shiftV, minX + state.shiftV);
    }

    private void drawFaceZ (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        if (icon == null)
            return;

        int rangeX = (int)(Math.ceil(state.renderMaxX + state.shiftU) - Math.floor(state.renderMinX + state.shiftU));
        int rangeY = (int)(Math.ceil(state.renderMaxY + state.shiftV) - Math.floor(state.renderMinY + state.shiftV));

        setXYZ(x, y, z);
        if (state.renderFromInside) {
            xyz[MINX] = z + state.renderMaxX;
            xyz[MAXX] = z + state.renderMinX;
        }

        if (rangeX <= 1 && rangeY <= 1) {
            double minX = state.renderMinX;
            double maxX = state.renderMaxX;
            if (state.flipTexture) {
                minX = state.renderMaxX;
                maxX = state.renderMinX;
            }

            if (face == ChamRender.FACE_ZNEG)
                setFaceZNegUV(icon, minX, state.renderMinY, maxX, state.renderMaxY);
            else
                setFaceZPosUV(icon, minX, state.renderMinY, maxX, state.renderMaxY);

            if (state.enableAO)
                renderXYZUVAO(face, icon);
            else
                renderXYZUV(face, icon);
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
                    setUV(icon, 1 - minUDiv[ix], 1 - maxUDiv[ix], 1 - maxVDiv[iy], 1 - minVDiv[iy]);
                else
                    setUV(icon, minUDiv[ix], maxUDiv[ix], 1 - maxVDiv[iy], 1 - minVDiv[iy]);

                renderXYZUVAO(face, icon);

                xyz[MINY] = xyz[MAXY];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    private void setFaceZNegUV (TextureAtlasSprite icon, double minX, double minY, double maxX, double maxY) {
        int rotate = state.uvRotate[ChamRender.ZNEG];
        if (rotate == ChamRenderState.ROTATE0)
            setUV(icon, maxX + state.shiftU, minX + state.shiftU, 1 - maxY + state.shiftV, 1 - minY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE90)
            setUV(icon, 1 - minY + state.shiftU, 1 - maxY + state.shiftU, minX + state.shiftV, maxX + state.shiftV);
        if (rotate == ChamRenderState.ROTATE180)
            setUV(icon, 1 - maxX + state.shiftU, 1 - minX + state.shiftU, maxY + state.shiftV, minY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE270)
            setUV(icon, minY + state.shiftU, maxY + state.shiftU, 1 - minX + state.shiftV, 1 - maxX + state.shiftV);
    }

    private void setFaceZPosUV (TextureAtlasSprite icon, double minX, double minY, double maxX, double maxY) {
        int rotate = state.uvRotate[ChamRender.ZPOS];
        if (rotate == ChamRenderState.ROTATE0)
            setUV(icon, minX + state.shiftU, maxX + state.shiftU, 1 - maxY + state.shiftV, 1 - minY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE90)
            setUV(icon, minY + state.shiftU, maxY + state.shiftU, 1 - minX + state.shiftV, 1 - maxX + state.shiftV);
        if (rotate == ChamRenderState.ROTATE180)
            setUV(icon, 1 - minX + state.shiftU, 1 - maxX + state.shiftU, maxY + state.shiftV, minY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE270)
            setUV(icon, 1 - minY + state.shiftU, 1 - maxY + state.shiftU, minX + state.shiftV, maxX + state.shiftV);
    }

    private void drawFaceX (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon) {
        if (icon == null)
            return;

        int rangeZ = (int)(Math.ceil(state.renderMaxZ + state.shiftU) - Math.floor(state.renderMinZ + state.shiftU));
        int rangeY = (int)(Math.ceil(state.renderMaxY + state.shiftV) - Math.floor(state.renderMinY + state.shiftV));

        setXYZ(x, y, z);
        if (state.renderFromInside) {
            xyz[MINZ] = z + state.renderMaxZ;
            xyz[MAXZ] = z + state.renderMinZ;
        }

        if (rangeZ <= 1 && rangeY <= 1) {
            double minZ = state.renderMinZ;
            double maxZ = state.renderMaxZ;
            if (state.flipTexture) {
                minZ = state.renderMaxZ;
                maxZ = state.renderMinZ;
            }

            if (face == ChamRender.FACE_XNEG)
                setFaceXNegUV(icon, minZ, state.renderMinY, maxZ, state.renderMaxY);
            else
                setFaceXPosUV(icon, minZ, state.renderMinY, maxZ, state.renderMaxY);

            if (state.enableAO)
                renderXYZUVAO(face, icon);
            else
                renderXYZUV(face, icon);
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
                    setUV(icon, 1 - minUDiv[iz], 1 - maxUDiv[iz], 1 - maxVDiv[iy], 1 - minVDiv[iy]);
                else
                    setUV(icon, minUDiv[iz], maxUDiv[iz], 1 - maxVDiv[iy], 1 - minVDiv[iy]);

                renderXYZUVAO(face, icon);

                xyz[MINY] = xyz[MAXY];
            }
            xyz[MINZ] = xyz[MAXZ];
        }
    }

    private void setFaceXNegUV (TextureAtlasSprite icon, double minZ, double minY, double maxZ, double maxY) {
        int rotate = state.uvRotate[ChamRender.XNEG];
        if (rotate == ChamRenderState.ROTATE0)
            setUV(icon, maxZ + state.shiftU, minZ + state.shiftU, 1 - maxY + state.shiftV, 1 - minY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE90)
            setUV(icon, minY + state.shiftU, maxY + state.shiftU, 1 - maxZ + state.shiftV, 1 - minZ + state.shiftV);
        if (rotate == ChamRenderState.ROTATE180)
            setUV(icon, 1 - maxZ + state.shiftU, 1 - minZ + state.shiftU, maxY + state.shiftV, minY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE270)
            setUV(icon, 1 - minY + state.shiftU, 1 - maxY + state.shiftU, maxZ + state.shiftV, minZ + state.shiftV);
    }

    private void setFaceXPosUV (TextureAtlasSprite icon, double minZ, double minY, double maxZ, double maxY) {
        int rotate = state.uvRotate[ChamRender.XPOS];
        if (rotate == ChamRenderState.ROTATE0)
            setUV(icon, minZ + state.shiftU, maxZ + state.shiftU, 1 - minY + state.shiftV, 1 - maxY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE90)
            setUV(icon, 1 - maxY + state.shiftU, 1 - minY + state.shiftU, maxZ + state.shiftV, minZ + state.shiftV);
        if (rotate == ChamRenderState.ROTATE180)
            setUV(icon, 1 - minZ + state.shiftU, 1 - maxZ + state.shiftU, minY + state.shiftV, maxY + state.shiftV);
        if (rotate == ChamRenderState.ROTATE270)
            setUV(icon, maxY + state.shiftU, minY + state.shiftU, 1 - maxZ + state.shiftV, 1 - minZ + state.shiftV);
    }

    public void drawPartialFace (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax, boolean isFaceGroup) {
        faceGroup = isFaceGroup;
        drawPartialFace(face, x, y, z, icon, uMin, vMin, uMax, vMax);
        faceGroup = false;
    }

    public void drawPartialFace (EnumFacing face, double x, double y, double z, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax) {
        setXYZ(x, y, z);
        setUV(icon, uMin, uMax, vMin, vMax);

        if (state.enableAO)
            renderXYZUVAO(face, icon);
        else
            renderXYZUV(face, icon);
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

        double posLR = 0;

        for (int lr = 0; lr <= rangeLR; lr++) {
            float lerpLR = (float)(posLR / diffLR);

            int brightTop = ChamRenderAO.mixAOBrightness(state.brightnessTopLeft, state.brightnessTopRight, 1 - lerpLR, lerpLR);
            int brightBottom = ChamRenderAO.mixAOBrightness(state.brightnessBottomLeft, state.brightnessBottomRight, 1 - lerpLR, lerpLR);

            double posTB = 0;
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

    private void setUV (TextureAtlasSprite icon, double uMin, double uMax, double vMin, double vMax) {
        uv[0] = icon.getInterpolatedU(uMin * 16);
        uv[1] = icon.getInterpolatedU(uMax * 16);
        uv[2] = icon.getInterpolatedV(vMin * 16);
        uv[3] = icon.getInterpolatedV(vMax * 16);
    }

    private void setUV (double uMin, double uMax, double vMin, double vMax) {
        uv[0] = uMin;
        uv[1] = uMax;
        uv[2] = vMin;
        uv[3] = vMax;
    }

    private void setXYZ (double x, double y, double z) {
        xyz[0] = x + state.renderOffsetX + state.renderMinX;
        xyz[1] = x + state.renderOffsetX + state.renderMaxX;
        xyz[2] = y + state.renderOffsetY + state.renderMinY;
        xyz[3] = y + state.renderOffsetY + state.renderMaxY;
        xyz[4] = z + state.renderOffsetZ + state.renderMinZ;
        xyz[5] = z + state.renderOffsetZ + state.renderMaxZ;
    }

    private int getFacingIndex (EnumFacing dir) {
        return (dir == null || !faceGroup) ? 6 : dir.getIndex();
    }

    private void renderXYZUV (EnumFacing facing, TextureAtlasSprite icon) {
        if (isBaking) {
            int index = getFacingIndex(facing);
            if (bakedFormat == DefaultVertexFormats.BLOCK)
                quadBuffer[index].add(bakeXYZUVBlock(facing, icon, state.color, state.color, state.color, state.color));
            else if (bakedFormat == DefaultVertexFormats.ITEM)
                quadBuffer[index].add(bakeXYZUVItem(facing, icon, state.color, state.color, state.color, state.color));
            return;
        }

        int[][] index = xyzuvMap[facing.getIndex()];
        int uvRotate = state.uvRotate[facing.getIndex()];

        int[] mapTL = index[TL];
        int[] mapBL = index[BL];
        int[] mapBR = index[BR];
        int[] mapTR = index[TR];

        double ubl = uv[mapBL[3]];
        double vbl = uv[mapBL[4]];
        double utr = uv[mapTR[3]];
        double vtr = uv[mapTR[4]];

        if (uvRotate == ChamRenderState.ROTATE90 || uvRotate == ChamRenderState.ROTATE270) {
            ubl = uv[mapTR[3]];
            vbl = uv[mapTR[4]];
            utr = uv[mapBL[3]];
            vtr = uv[mapBL[4]];
        }

        setBlockVertex(index[TL], uv[mapTL[3]], uv[mapTL[4]], state.color, state.brightness);
        setBlockVertex(index[BL], ubl, vbl, state.color, state.brightness);
        setBlockVertex(index[BR], uv[mapBR[3]], uv[mapBR[4]], state.color, state.brightness);
        setBlockVertex(index[TR], utr, vtr, state.color, state.brightness);
    }

    private void renderXYZUVAO (EnumFacing facing, TextureAtlasSprite icon) {

        if (isBaking) {
            int index = getFacingIndex(facing);
            if (bakedFormat == DefaultVertexFormats.BLOCK)
                quadBuffer[index].add(bakeXYZUVBlock(facing, icon, state.color, state.color, state.color, state.color));
            else if (bakedFormat == DefaultVertexFormats.ITEM)
                quadBuffer[index].add(bakeXYZUVItem(facing, icon, state.color, state.color, state.color, state.color));
            return;
        }

        int[][] index = xyzuvMap[facing.getIndex()];
        int uvRotate = state.uvRotate[facing.getIndex()];

        int[] mapTL = index[TL];
        int[] mapBL = index[BL];
        int[] mapBR = index[BR];
        int[] mapTR = index[TR];

        double ubl = uv[mapBL[3]];
        double vbl = uv[mapBL[4]];
        double utr = uv[mapTR[3]];
        double vtr = uv[mapTR[4]];

        if (uvRotate == ChamRenderState.ROTATE90 || uvRotate == ChamRenderState.ROTATE270) {
            ubl = uv[mapTR[3]];
            vbl = uv[mapTR[4]];
            utr = uv[mapBL[3]];
            vtr = uv[mapBL[4]];
        }

        setBlockVertex(index[TL], uv[mapTL[3]], uv[mapTL[4]], state.colorTopLeft, state.brightnessTopLeft);
        setBlockVertex(index[BL], ubl, vbl, state.colorBottomLeft, state.brightnessBottomLeft);
        setBlockVertex(index[BR], uv[mapBR[3]], uv[mapBR[4]], state.colorBottomRight, state.brightnessBottomRight);
        setBlockVertex(index[TR], utr, vtr, state.colorTopRight, state.brightnessTopRight);
    }

    private void setBlockVertex (int[] xumap, double u, double v, float[] color, int brightness) {
        if (tessellator == null)
            return;

        if (tessellator.getVertexFormat().hasNormal()) {
            tessellator.pos(xyz[xumap[0]], xyz[xumap[1]], xyz[xumap[2]])
                .color(color[0], color[1], color[2], 1)
                .tex(u, v)
                .normal(state.normal[0], state.normal[1], state.normal[2])
                .endVertex();
        }
        else {
            int lsky = (brightness >> 16) & 255;
            int lblk = (brightness & 255);

            tessellator.pos(xyz[xumap[0]], xyz[xumap[1]], xyz[xumap[2]])
                .tex(u, v)
                .lightmap(lsky, lblk)
                .color(color[0], color[1], color[2], 1)
                .endVertex();
        }
    }

    private BakedQuad bakeXYZUVBlock (EnumFacing facing, TextureAtlasSprite icon, float[] colorTL, float[] colorBL, float[] colorBR, float[] colorTR) {
        int[][] index = xyzuvMap[facing.getIndex()];
        int uvRotate = state.uvRotate[facing.getIndex()];

        int[] mapTL = index[TL];
        int[] mapBL = index[BL];
        int[] mapBR = index[BR];
        int[] mapTR = index[TR];

        double ubl = uv[mapBL[3]];
        double vbl = uv[mapBL[4]];
        double utr = uv[mapTR[3]];
        double vtr = uv[mapTR[4]];

        if (uvRotate == ChamRenderState.ROTATE90 || uvRotate == ChamRenderState.ROTATE270) {
            ubl = uv[mapTR[3]];
            vbl = uv[mapTR[4]];
            utr = uv[mapBL[3]];
            vtr = uv[mapBL[4]];
        }

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
            Float.floatToRawIntBits((float)ubl),
            Float.floatToRawIntBits((float)vbl),
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
            Float.floatToRawIntBits((float)utr),
            Float.floatToRawIntBits((float)vtr),
            light
        }, bakedTintIndex, facing, icon, diffuseLight, getVertexFormat());
    }

    private BakedQuad bakeXYZUVItem (EnumFacing facing, TextureAtlasSprite icon, float[] colorTL, float[] colorBL, float[] colorBR, float[] colorTR) {
        int[][] index = xyzuvMap[facing.getIndex()];
        int uvRotate = state.uvRotate[facing.getIndex()];

        int[] mapTL = index[TL];
        int[] mapBL = index[BL];
        int[] mapBR = index[BR];
        int[] mapTR = index[TR];

        double ubl = uv[mapBL[3]];
        double vbl = uv[mapBL[4]];
        double utr = uv[mapTR[3]];
        double vtr = uv[mapTR[4]];

        if (uvRotate == ChamRenderState.ROTATE90 || uvRotate == ChamRenderState.ROTATE270) {
            ubl = uv[mapTR[3]];
            vbl = uv[mapTR[4]];
            utr = uv[mapBL[3]];
            vtr = uv[mapBL[4]];
        }

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
            Float.floatToRawIntBits((float)ubl),
            Float.floatToRawIntBits((float)vbl),
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
            Float.floatToRawIntBits((float)utr),
            Float.floatToRawIntBits((float)vtr),
            packNormal(state.normal)
        }, bakedTintIndex, facing, icon, diffuseLight, getVertexFormat());
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
