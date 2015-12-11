package com.jaquadro.minecraft.chameleon.render;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class ChamRender
{
    public static final int YNEG = 0;
    public static final int YPOS = 1;
    public static final int ZNEG = 2;
    public static final int ZPOS = 3;
    public static final int XNEG = 4;
    public static final int XPOS = 5;

    public static final int FULL_BRIGHTNESS = 15728880;

    private static final float rgbMap[][] = {
        { 0.5f, 0.5f, 0.5f },
        { 1.0f, 1.0f, 1.0f },
        { 0.8f, 0.8f, 0.8f },
        { 0.8f, 0.8f, 0.8f },
        { 0.6f, 0.6f, 0.6f },
        { 0.6f, 0.6f, 0.6f },
    };

    private static final float normMap[][] = {
        { 0, -1, 0 },
        { 0, 1, 0 },
        { 0, 0, -1 },
        { 0, 0, 1 },
        { -1, 0, 0 },
        { 1, 0, 0 },
    };

    private WorldRenderer tessellator;

    public ChamRenderState state = new ChamRenderState();

    private ChamRenderAO aoHelper = new ChamRenderAO(state);
    private ChamRenderLL llHelper = new ChamRenderLL(state);

    private float[] colorScratch = new float[3];

    public static ChamRender instance = new ChamRender();

    public static void calculateBaseColor (float[] target, int color) {
        float r = (float)(color >> 16 & 255) / 255f;
        float g = (float)(color >> 8 & 255) / 255f;
        float b = (float)(color & 255) / 255f;

        if (EntityRenderer.anaglyphEnable) {
            float gray = (r * 30f + g * 59f + b * 11f) / 100f;
            float rg = (r * 30f + g * 70f) / 100f;
            float rb = (r * 30f + b * 70f) / 100f;

            r = gray;
            g = rg;
            b = rb;
        }

        target[0] = r;
        target[1] = g;
        target[2] = b;
    }

    public static void scaleColor (float[] target, float[] source, float scale) {
        target[0] = source[0] * scale;
        target[1] = source[1] * scale;
        target[2] = source[2] * scale;
    }

    void setWorldRenderer (WorldRenderer worldRenderer) {
        tessellator = worldRenderer;
    }

    WorldRenderer getWorldRenderer () {
        return tessellator;
    }

    public void renderEmptyPlane (int x, int y, int z) {
        state.setRenderBounds(0, 0, 0, 0, 0, 0);
        llHelper.drawFace(ChamRender.YNEG, x, y, z, getDefaultSprite());
    }

    public void setRenderBounds (double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        state.setRenderBounds(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public void setRenderBounds (Block block) {
        setRenderBounds(block.getBlockBoundsMinX(), block.getBlockBoundsMinY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ());
    }

    /*public void renderBlock (IBlockAccess blockAccess, Block block, int meta) {
        calculateBaseColor(colorScratch, block.getRenderColor(meta));
        float r = colorScratch[0];
        float g = colorScratch[1];
        float b = colorScratch[2];

        renderFace(YNEG, blockAccess, block, block.getIcon(0, meta), r, g, b);
        renderFace(YPOS, blockAccess, block, block.getIcon(1, meta), r, g, b);
        renderFace(ZNEG, blockAccess, block, block.getIcon(2, meta), r, g, b);
        renderFace(ZPOS, blockAccess, block, block.getIcon(3, meta), r, g, b);
        renderFace(XNEG, blockAccess, block, block.getIcon(4, meta), r, g, b);
        renderFace(XPOS, blockAccess, block, block.getIcon(5, meta), r, g, b);
    }*/

    /*public void renderBlock (IBlockAccess blockAccess, Block block, int x, int y, int z) {
        calculateBaseColor(colorScratch, block.colorMultiplier(blockAccess, x, y, z));
        float r = colorScratch[0];
        float g = colorScratch[1];
        float b = colorScratch[2];

        renderFace(YNEG, blockAccess, block, x, y, z, block.getIcon(blockAccess, x, y, z, 0), r, g, b);
        renderFace(YPOS, blockAccess, block, x, y, z, block.getIcon(blockAccess, x, y, z, 1), r, g, b);
        renderFace(ZNEG, blockAccess, block, x, y, z, block.getIcon(blockAccess, x, y, z, 2), r, g, b);
        renderFace(ZPOS, blockAccess, block, x, y, z, block.getIcon(blockAccess, x, y, z, 3), r, g, b);
        renderFace(XNEG, blockAccess, block, x, y, z, block.getIcon(blockAccess, x, y, z, 4), r, g, b);
        renderFace(XPOS, blockAccess, block, x, y, z, block.getIcon(blockAccess, x, y, z, 5), r, g, b);
    }*/

    public void renderFace (int face, IBlockAccess blockAccess, IBlockState blockState, TextureAtlasSprite icon, int meta) {
        calculateBaseColor(colorScratch, blockState.getBlock().getRenderColor(blockState));
        renderFaceColorMult(face, blockAccess, blockState, BlockPos.ORIGIN, icon, colorScratch[0], colorScratch[1], colorScratch[2]);
    }

    public void renderFace (int face, IBlockAccess blockAccess, IBlockState blockState, TextureAtlasSprite icon, float r, float g, float b) {
        renderFaceColorMult(face, blockAccess, blockState, BlockPos.ORIGIN, icon, r, g, b);
    }

    public void renderFace (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon) {
        calculateBaseColor(colorScratch, blockState.getBlock().colorMultiplier(blockAccess, pos));
        renderFace(face, blockAccess, blockState, pos, icon, colorScratch[0], colorScratch[1], colorScratch[2]);
    }

    public void renderFace (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, float r, float g, float b) {
        if (Minecraft.isAmbientOcclusionEnabled() && blockAccess != null && blockState.getBlock().getLightValue(blockAccess, pos) == 0)
            renderFaceAOPartial(face, blockAccess, blockState, pos, icon, r, g, b);
        else
            renderFaceColorMult(face, blockAccess, blockState, pos, icon, r, g, b);
    }

    public void renderFaceColorMult (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, float r, float g, float b) {
        setupColorMult(face, blockAccess, blockState, pos, r, g, b);

        face = ChamRenderState.FACE_BY_FACE_ROTATION[face][state.rotateTransform];
        llHelper.drawFace(face, pos.getX(), pos.getY(), pos.getZ(), icon);

        if (blockAccess == null)
            Tessellator.getInstance().draw();
    }
    
    public void renderFaceAOPartial (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, float r, float g, float b) {
        state.enableAO = true;

        face = ChamRenderState.FACE_BY_FACE_ROTATION[face][state.rotateTransform];

        switch (face) {
            case YNEG:
                aoHelper.setupYNegAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case YPOS:
                aoHelper.setupYPosAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case ZNEG:
                aoHelper.setupZNegAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case ZPOS:
                aoHelper.setupZPosAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case XNEG:
                aoHelper.setupXNegAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case XPOS:
                aoHelper.setupXPosAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
        }

        llHelper.drawFace(face, pos.getX(), pos.getY(), pos.getZ(), icon);

        state.enableAO = false;
    }

    public void renderPartialFace (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax) {
        calculateBaseColor(colorScratch, blockState.getBlock().colorMultiplier(blockAccess, pos));
        renderPartialFace(face, blockAccess, blockState, pos, icon, uMin, vMin, uMax, vMax, colorScratch[0], colorScratch[1], colorScratch[2]);
    }

    public void renderPartialFace (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax, float r, float g, float b) {
        if (Minecraft.isAmbientOcclusionEnabled() && blockAccess != null && blockState.getBlock().getLightValue(blockAccess, pos) == 0)
            renderPartialFaceAOPartial(face, blockAccess, blockState, pos, icon, uMin, vMin, uMax, vMax, r, g, b);
        else
            renderPartialFaceColorMult(face, blockAccess, blockState, pos, icon, uMin, vMin, uMax, vMax, r, g, b);
    }

    public void renderPartialFaceColorMult (int face, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax, float r, float g, float b) {
        setupColorMult(face, r, g, b);
        renderPartialFace(face, icon, uMin, vMin, uMax, vMax);

        Tessellator.getInstance().draw();
    }

    public void renderPartialFaceColorMult (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax, float r, float g, float b) {
        setupColorMult(face, blockAccess, blockState, pos, r, g, b);
        renderPartialFace(face, pos.getX(), pos.getY(), pos.getZ(), icon, uMin, vMin, uMax, vMax);

        if (blockAccess == null)
            Tessellator.getInstance().draw();
    }

    public void renderPartialFaceAOPartial (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax, float r, float g, float b) {
        state.enableAO = true;

        switch (ChamRenderState.FACE_BY_FACE_ROTATION[face][state.rotateTransform]) {
            case YNEG:
                aoHelper.setupYNegAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case YPOS:
                aoHelper.setupYPosAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case ZNEG:
                aoHelper.setupZNegAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case ZPOS:
                aoHelper.setupZPosAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case XNEG:
                aoHelper.setupXNegAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
            case XPOS:
                aoHelper.setupXPosAOPartial(blockAccess, blockState.getBlock(), pos, r, g, b);
                break;
        }

        renderPartialFace(face, pos.getX(), pos.getY(), pos.getZ(), icon, uMin, vMin, uMax, vMax);
        state.enableAO = false;
    }

    public void renderPartialFace (int face, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax) {
        state.enableAO = false;
        face = ChamRenderState.FACE_BY_FACE_ROTATION[face][state.rotateTransform];
        llHelper.drawPartialFace(face, 0, 0, 0, icon, uMin, vMin, uMax, vMax);
    }

    public void renderPartialFace (int face, double x, double y, double z, TextureAtlasSprite icon, double uMin, double vMin, double uMax, double vMax) {
        face = ChamRenderState.FACE_BY_FACE_ROTATION[face][state.rotateTransform];
        llHelper.drawPartialFace(face, x, y, z, icon, uMin, vMin, uMax, vMax);
    }

    /*public void renderCrossedSquares (Block block, int meta) {
        renderCrossedSquares(block, meta, getBlockIconFromSide(block, 0, meta));
    }*/

    public void renderCrossedSquares (IBlockState blockState, TextureAtlasSprite icon) {
        if (tessellator == null)
            return;

        state.setBrightness(FULL_BRIGHTNESS);

        calculateBaseColor(colorScratch, blockState.getBlock().getRenderColor(blockState));
        state.setColor(colorScratch);

        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHTING);

        //tessellator.startDrawingQuads();

        drawCrossedSquares(icon, 0, 0, 0, 1.0F);

        //Tessellator.getInstance().draw();

        if (lighting)
            GL11.glEnable(GL11.GL_LIGHTING);
    }

    /*public void renderCrossedSquares (IBlockAccess blockAccess, Block block, BlockPos pos) {
        renderCrossedSquares(blockAccess, block, x, y, z, getBlockIconFromSide(block, 0, blockAccess.getBlockMetadata(x, y, z)));
    }*/

    public void renderCrossedSquares (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, TextureAtlasSprite icon) {
        if (tessellator == null)
            return;

        Block block = blockState.getBlock();

        state.setBrightness(block.getMixedBrightnessForBlock(blockAccess, pos));

        calculateBaseColor(colorScratch, block.colorMultiplier(blockAccess, pos));
        state.setColor(colorScratch);

        drawCrossedSquares(icon, pos.getX(), pos.getY(), pos.getZ(), 1.0F);
    }

    public void drawCrossedSquares(TextureAtlasSprite icon, double x, double y, double z, float scale)
    {
        if (tessellator == null)
            return;
        //WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();

        double uMin = icon.getInterpolatedU(state.renderMinX * 16.0D);
        double uMax = icon.getInterpolatedU(state.renderMaxX * 16.0D);
        double vMin = icon.getInterpolatedV(16 - state.renderMaxY * 16.0D);
        double vMax = icon.getInterpolatedV(16 - state.renderMinY * 16.0D);

        double d7 = 0.45D * (double)scale;
        double xMin = x + 0.5D - d7;
        double xMax = x + 0.5D + d7;
        double yMin = y + state.renderMinY * scale;
        double yMax = y + state.renderMaxY * scale;
        double zMin = z + 0.5D - d7;
        double zMax = z + 0.5D + d7;

        addBlockVertex(xMin, yMax, zMin, uMin, vMin);
        addBlockVertex(xMin, yMin, zMin, uMin, vMax);
        addBlockVertex(xMax, yMin, zMax, uMax, vMax);
        addBlockVertex(xMax, yMax, zMax, uMax, vMin);
        addBlockVertex(xMax, yMax, zMax, uMin, vMin);
        addBlockVertex(xMax, yMin, zMax, uMin, vMax);
        addBlockVertex(xMin, yMin, zMin, uMax, vMax);
        addBlockVertex(xMin, yMax, zMin, uMax, vMin);

        addBlockVertex(xMin, yMax, zMax, uMin, vMin);
        addBlockVertex(xMin, yMin, zMax, uMin, vMax);
        addBlockVertex(xMax, yMin, zMin, uMax, vMax);
        addBlockVertex(xMax, yMax, zMin, uMax, vMin);
        addBlockVertex(xMax, yMax, zMin, uMin, vMin);
        addBlockVertex(xMax, yMin, zMin, uMin, vMax);
        addBlockVertex(xMin, yMin, zMax, uMax, vMax);
        addBlockVertex(xMin, yMax, zMax, uMax, vMin);
    }

    public void drawCrossedSquaresBounded(TextureAtlasSprite icon, double x, double y, double z, float scale)
    {
        if (tessellator == null)
            return;
        //WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();

        double vMin = icon.getInterpolatedV(16 - state.renderMaxY * 16.0D);
        double vMax = icon.getInterpolatedV(16 - state.renderMinY * 16.0D);

        double xzNN = Math.max(state.renderMinX, state.renderMinZ);
        double xzPP = Math.min(state.renderMaxX, state.renderMaxZ);

        double xNN = x + .5 - (.5 - xzNN) * 0.9;
        double zNN = z + .5 - (.5 - xzNN) * 0.9;
        double xNP = x + .5 - (.5 - Math.max(state.renderMinX, 1 - state.renderMaxZ)) * 0.9;
        double zNP = z + .5 - (.5 - Math.min(1 - state.renderMinX, state.renderMaxZ)) * 0.9;
        double xPN = x + .5 - (.5 - Math.min(state.renderMaxX, 1 - state.renderMinZ)) * 0.9;
        double zPN = z + .5 - (.5 - Math.max(1 - state.renderMaxX, state.renderMinZ)) * 0.9;
        double xPP = x + .5 - (.5 - xzPP) * 0.9;
        double zPP = z + .5 - (.5 - xzPP) * 0.9;

        double yMin = y + state.renderMinY * scale;
        double yMax = y + state.renderMaxY * scale;

        double uNN = icon.getInterpolatedU(xzNN * 16.0D);
        double uPP = icon.getInterpolatedU(xzPP * 16.0D);

        addBlockVertex(xNN, yMax, zNN, uNN, vMin);
        addBlockVertex(xNN, yMin, zNN, uNN, vMax);
        addBlockVertex(xPP, yMin, zPP, uPP, vMax);
        addBlockVertex(xPP, yMax, zPP, uPP, vMin);

        uNN = icon.getInterpolatedU(16 - xzNN * 16.0D);
        uPP = icon.getInterpolatedU(16 - xzPP * 16.0D);

        addBlockVertex(xPP, yMax, zPP, uPP, vMin);
        addBlockVertex(xPP, yMin, zPP, uPP, vMax);
        addBlockVertex(xNN, yMin, zNN, uNN, vMax);
        addBlockVertex(xNN, yMax, zNN, uNN, vMin);

        double uNP = icon.getInterpolatedU(Math.max(state.renderMinX, 1 - state.renderMaxZ) * 16.0D);
        double uPN = icon.getInterpolatedU(Math.min(state.renderMaxX, 1 - state.renderMinZ) * 16.0D);

        addBlockVertex(xNP, yMax, zNP, uNP, vMin);
        addBlockVertex(xNP, yMin, zNP, uNP, vMax);
        addBlockVertex(xPN, yMin, zPN, uPN, vMax);
        addBlockVertex(xPN, yMax, zPN, uPN, vMin);

        uNP = icon.getInterpolatedU(16 - Math.max(state.renderMinX, 1 - state.renderMaxZ) * 16.0D);
        uPN = icon.getInterpolatedU(16 - Math.min(state.renderMaxX, 1 - state.renderMinZ) * 16.0D);

        addBlockVertex(xPN, yMax, zPN, uPN, vMin);
        addBlockVertex(xPN, yMin, zPN, uPN, vMax);
        addBlockVertex(xNP, yMin, zNP, uNP, vMax);
        addBlockVertex(xNP, yMax, zNP, uNP, vMin);
    }

    private void setupColorMult (int face, float r, float g, float b) {
        if (tessellator == null)
            return;

        float[] rgb = rgbMap[face];
        float[] norm = normMap[face];

        state.setColor(rgb[0] * r, rgb[1] * g, rgb[2] * b);
        state.setNormal(norm);
        tessellator.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        state.enableAO = false;
    }

    private void setupColorMult (int face, IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        if (tessellator == null)
            return;

        //WorldRenderer tessellator = Tessellator.getInstance().getWorldRenderer();
        float[] rgb = rgbMap[face];
        float[] norm = normMap[face];

        if (blockAccess == null) {
            //tessellator.startDrawingQuads();
            state.setColor(r, g, b);
            state.setNormal(norm);
        }
        else {
            int brightX = pos.getX();
            int brightY = pos.getY();
            int brightZ = pos.getZ();

            switch (face) {
                case YNEG: brightY = (state.renderMinY > 0) ? brightY : brightY - 1; break;
                case YPOS: brightY = (state.renderMaxY < 1) ? brightY : brightY + 1; break;
                case ZNEG: brightZ = (state.renderMinZ > 0) ? brightZ : brightZ - 1; break;
                case ZPOS: brightZ = (state.renderMaxZ < 1) ? brightZ : brightZ + 1; break;
                case XNEG: brightX = (state.renderMinX > 0) ? brightX : brightX - 1; break;
                case XPOS: brightX = (state.renderMaxX < 1) ? brightX : brightX + 1; break;
            }

            if (brightX != pos.getX() || brightY != pos.getY() || brightZ != pos.getZ())
                pos = new BlockPos(brightX, brightY, brightZ);

            state.setColor(rgb[0] * r, rgb[1] * g, rgb[2] * b);
            state.setBrightness(blockState.getBlock().getMixedBrightnessForBlock(blockAccess, pos));
        }

        state.enableAO = false;
    }

    private TextureAtlasSprite getDefaultSprite () {
        return ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
    }

    private void addBlockVertex (double x, double y, double z, double u, double v) {

        if (tessellator.getVertexFormat().hasNormal()) {
            tessellator.pos(x, y, z).tex(u, v).normal(state.normal[0], state.normal[1], state.normal[2]).color(state.color[0], state.color[1], state.color[2], 1).endVertex();
        }
        else {
            int lsky = (state.brightness >> 16) & 255;
            int lblk = (state.brightness & 255);
            tessellator.pos(x, y, z).tex(u, v).lightmap(lsky, lblk).color(state.color[0], state.color[1], state.color[2], 1).endVertex();
        }
    }
}
