package com.jaquadro.minecraft.chameleon.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChamRenderAO
{
    private ChamRenderState state;

    private int aoBrightnessXYNI;
    private int aoBrightnessYZIN;
    private int aoBrightnessYZIP;
    private int aoBrightnessXYPI;
    private int aoBrightnessXYZNIN;
    private int aoBrightnessXYZNIP;
    private int aoBrightnessXYZPIN;
    private int aoBrightnessXYZPIP;

    private int aoBrightnessXYNN;
    private int aoBrightnessYZNN;
    private int aoBrightnessYZNP;
    private int aoBrightnessXYPN;
    private int aoBrightnessXYNP;
    private int aoBrightnessXYPP;
    private int aoBrightnessYZPN;
    private int aoBrightnessYZPP;
    private int aoBrightnessXZNN;
    private int aoBrightnessXZPN;
    private int aoBrightnessXZNP;
    private int aoBrightnessXZPP;
    private int aoBrightnessXYZNNN;
    private int aoBrightnessXYZNNP;
    private int aoBrightnessXYZPNN;
    private int aoBrightnessXYZPNP;
    private int aoBrightnessXYZNPN;
    private int aoBrightnessXYZPPN;
    private int aoBrightnessXYZNPP;
    private int aoBrightnessXYZPPP;

    private int aoBrightnessXZNI;
    private int aoBrightnessYZNI;
    private int aoBrightnessYZPI;
    private int aoBrightnessXZPI;
    private int aoBrightnessXYIN;
    private int aoBrightnessXZIN;
    private int aoBrightnessXZIP;
    private int aoBrightnessXYIP;
    private int aoBrightnessXYZNNI;
    private int aoBrightnessXYZNPI;
    private int aoBrightnessXYZPNI;
    private int aoBrightnessXYZPPI;
    private int aoBrightnessXYZINN;
    private int aoBrightnessXYZINP;
    private int aoBrightnessXYZIPN;
    private int aoBrightnessXYZIPP;

    private float aoLightValueScratchXYNI;
    private float aoLightValueScratchYZIN;
    private float aoLightValueScratchYZIP;
    private float aoLightValueScratchXYPI;
    private float aoLightValueScratchXYZNIN;
    private float aoLightValueScratchXYZNIP;
    private float aoLightValueScratchXYZPIN;
    private float aoLightValueScratchXYZPIP;

    private float aoLightValueScratchXYNN;
    private float aoLightValueScratchYZNN;
    private float aoLightValueScratchYZNP;
    private float aoLightValueScratchXYPN;
    private float aoLightValueScratchXYNP;
    private float aoLightValueScratchXYPP;
    private float aoLightValueScratchYZPN;
    private float aoLightValueScratchYZPP;
    private float aoLightValueScratchXZNN;
    private float aoLightValueScratchXZPN;
    private float aoLightValueScratchXZNP;
    private float aoLightValueScratchXZPP;
    private float aoLightValueScratchXYZNNN;
    private float aoLightValueScratchXYZNNP;
    private float aoLightValueScratchXYZPNN;
    private float aoLightValueScratchXYZPNP;
    private float aoLightValueScratchXYZNPN;
    private float aoLightValueScratchXYZPPN;
    private float aoLightValueScratchXYZNPP;
    private float aoLightValueScratchXYZPPP;

    private float aoLightValueScratchXZNI;
    private float aoLightValueScratchYZNI;
    private float aoLightValueScratchYZPI;
    private float aoLightValueScratchXZPI;
    private float aoLightValueScratchXYIN;
    private float aoLightValueScratchXZIN;
    private float aoLightValueScratchXZIP;
    private float aoLightValueScratchXYIP;
    private float aoLightValueScratchXYZNNI;
    private float aoLightValueScratchXYZNPI;
    private float aoLightValueScratchXYZPNI;
    private float aoLightValueScratchXYZPPI;
    private float aoLightValueScratchXYZINN;
    private float aoLightValueScratchXYZINP;
    private float aoLightValueScratchXYZIPN;
    private float aoLightValueScratchXYZIPP;

    public ChamRenderAO (ChamRenderState state) {
        this.state = state;
    }

    public void setupYNegAOPartial (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        state.brightness = 983055;

        BlockPos posDown = pos.down();
        BlockPos posGrass = (state.renderMinY <= 0) ? posDown : pos;

        boolean blocksGrassXYPN = !blockAccess.getBlockState(posGrass.east()).isTranslucent();
        boolean blocksGrassXYNN = !blockAccess.getBlockState(posGrass.west()).isTranslucent();
        boolean blocksGrassYZNP = !blockAccess.getBlockState(posGrass.south()).isTranslucent();
        boolean blocksGrassYZNN = !blockAccess.getBlockState(posGrass.north()).isTranslucent();

        if (state.renderMinY > 0)
            setupAOBrightnessYNeg(blockAccess, blockState, pos, blocksGrassXYPN, blocksGrassXYNN, blocksGrassYZNP, blocksGrassYZNN);

        setupAOBrightnessYPos(blockAccess, blockState, posDown, blocksGrassXYPN, blocksGrassXYNN, blocksGrassYZNP, blocksGrassYZNN);

        float yClamp = MathHelper.clamp_float((float) state.renderMinY, 0, 1);
        mixAOBrightnessLightValueY(yClamp, 1 - yClamp);

        int blockBrightness = blockState.getPackedLightmapCoords(blockAccess, pos);
        if (state.renderMinY <= 0.0D || !blockAccess.getBlockState(posDown).isOpaqueCube())
            blockBrightness = blockState.getPackedLightmapCoords(blockAccess, posDown);

        float aoOpposingBlock = blockAccess.getBlockState(posDown).getAmbientOcclusionLightValue();
        float aoXYZNNP = (aoLightValueScratchXYNI + aoLightValueScratchXYZNIP + aoOpposingBlock + aoLightValueScratchYZIP) / 4.0F;
        float aoXYZPNP = (aoOpposingBlock + aoLightValueScratchYZIP + aoLightValueScratchXYPI + aoLightValueScratchXYZPIP) / 4.0F;
        float aoXYZPNN = (aoLightValueScratchYZIN + aoOpposingBlock + aoLightValueScratchXYZPIN + aoLightValueScratchXYPI) / 4.0F;
        float aoXYZNNN = (aoLightValueScratchXYZNIN + aoLightValueScratchXYNI + aoLightValueScratchYZIN + aoOpposingBlock) / 4.0F;

        float aoTR = (float)((double)aoXYZNNP * state.renderMinX * (1.0D - state.renderMaxZ) + (double)aoXYZPNP * state.renderMinX * state.renderMaxZ + (double)aoXYZPNN * (1.0D - state.renderMinX) * state.renderMaxZ + (double)aoXYZNNN * (1.0D - state.renderMinX) * (1.0D - state.renderMaxZ));
        float aoTL = (float)((double)aoXYZNNP * state.renderMinX * (1.0D - state.renderMinZ) + (double)aoXYZPNP * state.renderMinX * state.renderMinZ + (double)aoXYZPNN * (1.0D - state.renderMinX) * state.renderMinZ + (double)aoXYZNNN * (1.0D - state.renderMinX) * (1.0D - state.renderMinZ));
        float aoBL = (float)((double)aoXYZNNP * state.renderMaxX * (1.0D - state.renderMinZ) + (double)aoXYZPNP * state.renderMaxX * state.renderMinZ + (double)aoXYZPNN * (1.0D - state.renderMaxX) * state.renderMinZ + (double)aoXYZNNN * (1.0D - state.renderMaxX) * (1.0D - state.renderMinZ));
        float aoBR = (float)((double)aoXYZNNP * state.renderMaxX * (1.0D - state.renderMaxZ) + (double)aoXYZPNP * state.renderMaxX * state.renderMaxZ + (double)aoXYZPNN * (1.0D - state.renderMaxX) * state.renderMaxZ + (double)aoXYZNNN * (1.0D - state.renderMaxX) * (1.0D - state.renderMaxZ));

        int brXYZNNP = getAOBrightness(aoBrightnessXYNI, aoBrightnessXYZNIP, aoBrightnessYZIP, blockBrightness);
        int brXYZPNP = getAOBrightness(aoBrightnessYZIP, aoBrightnessXYPI, aoBrightnessXYZPIP, blockBrightness);
        int brXYZPNN = getAOBrightness(aoBrightnessYZIN, aoBrightnessXYZPIN, aoBrightnessXYPI, blockBrightness);
        int brXYZNNN = getAOBrightness(aoBrightnessXYZNIN, aoBrightnessXYNI, aoBrightnessYZIN, blockBrightness);

        state.brightnessTopRight = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, state.renderMaxX * (1.0D - state.renderMaxZ), (1.0D - state.renderMaxX) * (1.0D - state.renderMaxZ), (1.0D - state.renderMaxX) * state.renderMaxZ, state.renderMaxX * state.renderMaxZ);
        state.brightnessTopLeft = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, state.renderMaxX * (1.0D - state.renderMinZ), (1.0D - state.renderMaxX) * (1.0D - state.renderMinZ), (1.0D - state.renderMaxX) * state.renderMinZ, state.renderMaxX * state.renderMinZ);
        state.brightnessBottomLeft = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, state.renderMinX * (1.0D - state.renderMinZ), (1.0D - state.renderMinX) * (1.0D - state.renderMinZ), (1.0D - state.renderMinX) * state.renderMinZ, state.renderMinX * state.renderMinZ);
        state.brightnessBottomRight = mixAOBrightness(brXYZNNP, brXYZNNN, brXYZPNN, brXYZPNP, state.renderMinX * (1.0D - state.renderMaxZ), (1.0D - state.renderMinX) * (1.0D - state.renderMaxZ), (1.0D - state.renderMinX) * state.renderMaxZ, state.renderMinX * state.renderMaxZ);

        state.setColor(r * state.colorMultYNeg, g * state.colorMultYNeg, b * state.colorMultYNeg);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupYPosAOPartial (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        state.brightness = 983055;

        BlockPos posUp = pos.up();
        BlockPos posGrass = (state.renderMaxY >= 1) ? posUp : pos;

        boolean blocksGrassXYNP = !blockAccess.getBlockState(posGrass.west()).isTranslucent();
        boolean blocksGrassXYPP = !blockAccess.getBlockState(posGrass.east()).isTranslucent();
        boolean blocksGrassYZPN = !blockAccess.getBlockState(posGrass.north()).isTranslucent();
        boolean blocksGrassYZPP = !blockAccess.getBlockState(posGrass.south()).isTranslucent();

        if (state.renderMaxY < 1)
            setupAOBrightnessYPos(blockAccess, blockState, pos, blocksGrassXYPP, blocksGrassXYNP, blocksGrassYZPP, blocksGrassYZPN);

        setupAOBrightnessYNeg(blockAccess, blockState, posUp, blocksGrassXYPP, blocksGrassXYNP, blocksGrassYZPP, blocksGrassYZPN);

        float yClamp = MathHelper.clamp_float((float) state.renderMaxY, 0, 1);
        mixAOBrightnessLightValueY(yClamp, 1 - yClamp);

        int blockBrightness = blockState.getPackedLightmapCoords( blockAccess, pos);
        if (state.renderMaxY >= 1.0D || !blockAccess.getBlockState(posUp).isOpaqueCube())
            blockBrightness = blockState.getPackedLightmapCoords( blockAccess, posUp);

        float aoOpposingBlock = blockAccess.getBlockState(posUp).getAmbientOcclusionLightValue();
        float aoXYZNPN = (aoLightValueScratchXYZNIP + aoLightValueScratchXYNI + aoLightValueScratchYZIP + aoOpposingBlock) / 4.0F;  // TR
        float aoXYZNPP = (aoLightValueScratchYZIP + aoOpposingBlock + aoLightValueScratchXYZPIP + aoLightValueScratchXYPI) / 4.0F;  // TL
        float aoXYZPPP = (aoOpposingBlock + aoLightValueScratchYZIN + aoLightValueScratchXYPI + aoLightValueScratchXYZPIN) / 4.0F;  // BL
        float aoXYZPPN = (aoLightValueScratchXYNI + aoLightValueScratchXYZNIN + aoOpposingBlock + aoLightValueScratchYZIN) / 4.0F;  // BR

        float aoTL = (float)((double)aoXYZPPP * state.renderMaxX * (1.0D - state.renderMaxZ) + (double)aoXYZNPP * state.renderMaxX * state.renderMaxZ + (double)aoXYZNPN * (1.0D - state.renderMaxX) * state.renderMaxZ + (double)aoXYZPPN * (1.0D - state.renderMaxX) * (1.0D - state.renderMaxZ));
        float aoBL = (float)((double)aoXYZPPP * state.renderMaxX * (1.0D - state.renderMinZ) + (double)aoXYZNPP * state.renderMaxX * state.renderMinZ + (double)aoXYZNPN * (1.0D - state.renderMaxX) * state.renderMinZ + (double)aoXYZPPN * (1.0D - state.renderMaxX) * (1.0D - state.renderMinZ));
        float aoBR = (float)((double)aoXYZPPP * state.renderMinX * (1.0D - state.renderMinZ) + (double)aoXYZNPP * state.renderMinX * state.renderMinZ + (double)aoXYZNPN * (1.0D - state.renderMinX) * state.renderMinZ + (double)aoXYZPPN * (1.0D - state.renderMinX) * (1.0D - state.renderMinZ));
        float aoTR = (float)((double)aoXYZPPP * state.renderMinX * (1.0D - state.renderMaxZ) + (double)aoXYZNPP * state.renderMinX * state.renderMaxZ + (double)aoXYZNPN * (1.0D - state.renderMinX) * state.renderMaxZ + (double)aoXYZPPN * (1.0D - state.renderMinX) * (1.0D - state.renderMaxZ));

        int brXYZPPN = getAOBrightness(aoBrightnessXYNI, aoBrightnessXYZNIP, aoBrightnessYZIP, blockBrightness);
        int brXYZNPN = getAOBrightness(aoBrightnessYZIP, aoBrightnessXYIP, aoBrightnessXYZPIP, blockBrightness);
        int brXYZNPP = getAOBrightness(aoBrightnessYZIN, aoBrightnessXYZPIN, aoBrightnessXYPI, blockBrightness);
        int brXYZPPP = getAOBrightness(aoBrightnessXYZNIN, aoBrightnessXYNI, aoBrightnessYZIN, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZPPP, brXYZNPP, brXYZNPN, brXYZPPN, state.renderMaxX * (1.0D - state.renderMaxZ), state.renderMaxX * state.renderMaxZ, (1.0D - state.renderMaxX) * state.renderMaxZ, (1.0D - state.renderMaxX) * (1.0D - state.renderMaxZ));
        state.brightnessBottomLeft = mixAOBrightness(brXYZPPP, brXYZNPP, brXYZNPN, brXYZPPN, state.renderMaxX * (1.0D - state.renderMinZ), state.renderMaxX * state.renderMinZ, (1.0D - state.renderMaxX) * state.renderMinZ, (1.0D - state.renderMaxX) * (1.0D - state.renderMinZ));
        state.brightnessBottomRight = mixAOBrightness(brXYZPPP, brXYZNPP, brXYZNPN, brXYZPPN, state.renderMinX * (1.0D - state.renderMinZ), state.renderMinX * state.renderMinZ, (1.0D - state.renderMinX) * state.renderMinZ, (1.0D - state.renderMinX) * (1.0D - state.renderMinZ));
        state.brightnessTopRight = mixAOBrightness(brXYZPPP, brXYZNPP, brXYZNPN, brXYZPPN, state.renderMinX * (1.0D - state.renderMaxZ), state.renderMinX * state.renderMaxZ, (1.0D - state.renderMinX) * state.renderMaxZ, (1.0D - state.renderMinX) * (1.0D - state.renderMaxZ));

        state.setColor(r * state.colorMultYPos, g * state.colorMultYPos, b * state.colorMultYPos);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupZNegAOPartial (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        state.brightness = 983055;

        BlockPos posNorth = pos.north();
        BlockPos posGrass = (state.renderMinZ <= 0) ? posNorth : pos;

        boolean blocksGrassXZPN = !blockAccess.getBlockState(posGrass.east()).isTranslucent();
        boolean blocksGrassXZNN = !blockAccess.getBlockState(posGrass.west()).isTranslucent();
        boolean blocksGrassYZPN = !blockAccess.getBlockState(posGrass.up()).isTranslucent();
        boolean blocksGrassYZNN = !blockAccess.getBlockState(posGrass.down()).isTranslucent();

        if (state.renderMaxZ > 0)
            setupAOBrightnessZNeg(blockAccess, blockState, pos, blocksGrassXZPN, blocksGrassXZNN, blocksGrassYZPN, blocksGrassYZNN);

        setupAOBrightnessZPos(blockAccess, blockState, posNorth, blocksGrassXZPN, blocksGrassXZNN, blocksGrassYZPN, blocksGrassYZNN);

        float zClamp = MathHelper.clamp_float((float) state.renderMinZ, 0, 1);
        mixAOBrightnessLightValueZ(zClamp, 1 - zClamp);

        int blockBrightness = blockState.getPackedLightmapCoords(blockAccess, pos);
        if (state.renderMinZ <= 0.0D || !blockAccess.getBlockState(posNorth).isOpaqueCube())
            blockBrightness = blockState.getPackedLightmapCoords(blockAccess, posNorth);

        float aoOpposingBlock = blockAccess.getBlockState(posNorth).getAmbientOcclusionLightValue();
        float aoXYZNPN = (aoLightValueScratchXZNI + aoLightValueScratchXYZNPI + aoOpposingBlock + aoLightValueScratchYZPI) / 4.0F;
        float aoXYZPPN = (aoOpposingBlock + aoLightValueScratchYZPI + aoLightValueScratchXZPI + aoLightValueScratchXYZPPI) / 4.0F;
        float aoXYZPNN = (aoLightValueScratchYZNI + aoOpposingBlock + aoLightValueScratchXYZPNI + aoLightValueScratchXZPI) / 4.0F;
        float aoXYZNNN = (aoLightValueScratchXYZNNI + aoLightValueScratchXZNI + aoLightValueScratchYZNI + aoOpposingBlock) / 4.0F;

        float aoTL = (float)((double)aoXYZNPN * state.renderMaxY * (1.0D - state.renderMinX) + (double)aoXYZPPN * state.renderMaxY * state.renderMinX + (double)aoXYZPNN * (1.0D - state.renderMaxY) * state.renderMinX + (double)aoXYZNNN * (1.0D - state.renderMaxY) * (1.0D - state.renderMinX));
        float aoBL = (float)((double)aoXYZNPN * state.renderMaxY * (1.0D - state.renderMaxX) + (double)aoXYZPPN * state.renderMaxY * state.renderMaxX + (double)aoXYZPNN * (1.0D - state.renderMaxY) * state.renderMaxX + (double)aoXYZNNN * (1.0D - state.renderMaxY) * (1.0D - state.renderMaxX));
        float aoBR = (float)((double)aoXYZNPN * state.renderMinY * (1.0D - state.renderMaxX) + (double)aoXYZPPN * state.renderMinY * state.renderMaxX + (double)aoXYZPNN * (1.0D - state.renderMinY) * state.renderMaxX + (double)aoXYZNNN * (1.0D - state.renderMinY) * (1.0D - state.renderMaxX));
        float aoTR = (float)((double)aoXYZNPN * state.renderMinY * (1.0D - state.renderMinX) + (double)aoXYZPPN * state.renderMinY * state.renderMinX + (double)aoXYZPNN * (1.0D - state.renderMinY) * state.renderMinX + (double)aoXYZNNN * (1.0D - state.renderMinY) * (1.0D - state.renderMinX));

        int brXYZNPN = getAOBrightness(aoBrightnessXZNI, aoBrightnessXYZNPI, aoBrightnessYZPI, blockBrightness);
        int brXYZPPN = getAOBrightness(aoBrightnessYZPI, aoBrightnessXZPI, aoBrightnessXYZPPI, blockBrightness);
        int brXYZPNN = getAOBrightness(aoBrightnessYZNI, aoBrightnessXYZPNI, aoBrightnessXZPI, blockBrightness);
        int brXYZNNN = getAOBrightness(aoBrightnessXYZNNI, aoBrightnessXZNI, aoBrightnessYZNI, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, state.renderMaxY * (1.0D - state.renderMinX), state.renderMaxY * state.renderMinX, (1.0D - state.renderMaxY) * state.renderMinX, (1.0D - state.renderMaxY) * (1.0D - state.renderMinX));
        state.brightnessBottomLeft = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, state.renderMaxY * (1.0D - state.renderMaxX), state.renderMaxY * state.renderMaxX, (1.0D - state.renderMaxY) * state.renderMaxX, (1.0D - state.renderMaxY) * (1.0D - state.renderMaxX));
        state.brightnessBottomRight = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, state.renderMinY * (1.0D - state.renderMaxX), state.renderMinY * state.renderMaxX, (1.0D - state.renderMinY) * state.renderMaxX, (1.0D - state.renderMinY) * (1.0D - state.renderMaxX));
        state.brightnessTopRight = mixAOBrightness(brXYZNPN, brXYZPPN, brXYZPNN, brXYZNNN, state.renderMinY * (1.0D - state.renderMinX), state.renderMinY * state.renderMinX, (1.0D - state.renderMinY) * state.renderMinX, (1.0D - state.renderMinY) * (1.0D - state.renderMinX));

        state.setColor(r * state.colorMultZNeg, g * state.colorMultZNeg, b * state.colorMultZNeg);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupZPosAOPartial (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        state.brightness = 983055;

        BlockPos posSouth = pos.south();
        BlockPos posGrass = (state.renderMaxZ >= 1) ? posSouth : pos;

        boolean blocksGrassXZPP = !blockAccess.getBlockState(posGrass.east()).isTranslucent();
        boolean blocksGrassXZNP = !blockAccess.getBlockState(posGrass.west()).isTranslucent();
        boolean blocksGrassYZPP = !blockAccess.getBlockState(posGrass.up()).isTranslucent();
        boolean blocksGrassYZNP = !blockAccess.getBlockState(posGrass.down()).isTranslucent();

        if (state.renderMaxZ < 1)
            setupAOBrightnessZPos(blockAccess, blockState, pos, blocksGrassXZPP, blocksGrassXZNP, blocksGrassYZPP, blocksGrassYZNP);

        setupAOBrightnessZNeg(blockAccess, blockState, posSouth, blocksGrassXZPP, blocksGrassXZNP, blocksGrassYZPP, blocksGrassYZNP);

        float zClamp = MathHelper.clamp_float((float) state.renderMaxZ, 0, 1);
        mixAOBrightnessLightValueZ(zClamp, 1 - zClamp);

        int blockBrightness = blockState.getPackedLightmapCoords(blockAccess, pos);
        if (state.renderMaxZ >= 1.0D || !blockAccess.getBlockState(posSouth).isOpaqueCube())
            blockBrightness = blockState.getPackedLightmapCoords(blockAccess, posSouth);

        float aoOpposingBlock = blockAccess.getBlockState(posSouth).getAmbientOcclusionLightValue();
        float aoXYZNPP = (aoLightValueScratchXZNI + aoLightValueScratchXYZNPI + aoOpposingBlock + aoLightValueScratchYZPI) / 4.0F;
        float aoXYZPPP = (aoOpposingBlock + aoLightValueScratchYZPI + aoLightValueScratchXZPI + aoLightValueScratchXYZPPI) / 4.0F;
        float aoXYZPNP = (aoLightValueScratchYZNI + aoOpposingBlock + aoLightValueScratchXYZPNI + aoLightValueScratchXZPI) / 4.0F;
        float aoXYZNNP = (aoLightValueScratchXYZNNI + aoLightValueScratchXZNI + aoLightValueScratchYZNI + aoOpposingBlock) / 4.0F;

        float aoTL = (float)((double)aoXYZNPP * state.renderMaxY * (1.0D - state.renderMinX) + (double)aoXYZPPP * state.renderMaxY * state.renderMinX + (double)aoXYZPNP * (1.0D - state.renderMaxY) * state.renderMinX + (double)aoXYZNNP * (1.0D - state.renderMaxY) * (1.0D - state.renderMinX));
        float aoBL = (float)((double)aoXYZNPP * state.renderMinY * (1.0D - state.renderMinX) + (double)aoXYZPPP * state.renderMinY * state.renderMinX + (double)aoXYZPNP * (1.0D - state.renderMinY) * state.renderMinX + (double)aoXYZNNP * (1.0D - state.renderMinY) * (1.0D - state.renderMinX));
        float aoBR = (float)((double)aoXYZNPP * state.renderMinY * (1.0D - state.renderMaxX) + (double)aoXYZPPP * state.renderMinY * state.renderMaxX + (double)aoXYZPNP * (1.0D - state.renderMinY) * state.renderMaxX + (double)aoXYZNNP * (1.0D - state.renderMinY) * (1.0D - state.renderMaxX));
        float aoTR = (float)((double)aoXYZNPP * state.renderMaxY * (1.0D - state.renderMaxX) + (double)aoXYZPPP * state.renderMaxY * state.renderMaxX + (double)aoXYZPNP * (1.0D - state.renderMaxY) * state.renderMaxX + (double)aoXYZNNP * (1.0D - state.renderMaxY) * (1.0D - state.renderMaxX));

        int brXYZNPP = getAOBrightness(aoBrightnessXZNI, aoBrightnessXYZNPI, aoBrightnessYZPI, blockBrightness);
        int brXYZPPP = getAOBrightness(aoBrightnessYZPI, aoBrightnessXZPI, aoBrightnessXYZPPI, blockBrightness);
        int brXYZPNP = getAOBrightness(aoBrightnessYZNI, aoBrightnessXYZPNI, aoBrightnessXZPI, blockBrightness);
        int brXYZNNP = getAOBrightness(aoBrightnessXYZNNI, aoBrightnessXZNI, aoBrightnessYZNI, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, state.renderMaxY * (1.0D - state.renderMinX), (1.0D - state.renderMaxY) * (1.0D - state.renderMinX), (1.0D - state.renderMaxY) * state.renderMinX, state.renderMaxY * state.renderMinX);
        state.brightnessBottomLeft = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, state.renderMinY * (1.0D - state.renderMinX), (1.0D - state.renderMinY) * (1.0D - state.renderMinX), (1.0D - state.renderMinY) * state.renderMinX, state.renderMinY * state.renderMinX);
        state.brightnessBottomRight = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, state.renderMinY * (1.0D - state.renderMaxX), (1.0D - state.renderMinY) * (1.0D - state.renderMaxX), (1.0D - state.renderMinY) * state.renderMaxX, state.renderMinY * state.renderMaxX);
        state.brightnessTopRight = mixAOBrightness(brXYZNPP, brXYZNNP, brXYZPNP, brXYZPPP, state.renderMaxY * (1.0D - state.renderMaxX), (1.0D - state.renderMaxY) * (1.0D - state.renderMaxX), (1.0D - state.renderMaxY) * state.renderMaxX, state.renderMaxY * state.renderMaxX);

        state.setColor(r * state.colorMultZPos, g * state.colorMultZPos, b * state.colorMultZPos);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupXNegAOPartial (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        state.brightness = 983055;

        BlockPos posWest = pos.west();
        BlockPos posGrass = (state.renderMinX <= 0) ? posWest : pos;

        boolean blocksGrassXYNP = !blockAccess.getBlockState(posGrass.up()).isTranslucent();
        boolean blocksGrassXYNN = !blockAccess.getBlockState(posGrass.down()).isTranslucent();
        boolean blocksGrassXZNN = !blockAccess.getBlockState(posGrass.north()).isTranslucent();
        boolean blocksGrassXZNP = !blockAccess.getBlockState(posGrass.south()).isTranslucent();

        if (state.renderMinX > 0)
            setupAOBrightnessXNeg(blockAccess, blockState, pos, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        setupAOBrightnessXPos(blockAccess, blockState, posWest, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        float xClamp = MathHelper.clamp_float((float) state.renderMinX, 0, 1);
        mixAOBrightnessLightValueX(xClamp, 1 - xClamp);

        int blockBrightness = blockState.getPackedLightmapCoords(blockAccess, pos);
        if (state.renderMinX <= 0.0D || !blockAccess.getBlockState(posWest).isOpaqueCube())
            blockBrightness = blockState.getPackedLightmapCoords(blockAccess, posWest);

        float aoOpposingBlock = blockAccess.getBlockState(posWest).getAmbientOcclusionLightValue();
        float aoXYZNNP = (aoLightValueScratchXYIN + aoLightValueScratchXYZINP + aoOpposingBlock + aoLightValueScratchXZIP) / 4.0F;
        float aoXYZNPP = (aoOpposingBlock + aoLightValueScratchXZIP + aoLightValueScratchXYIP + aoLightValueScratchXYZIPP) / 4.0F;
        float aoXYZNPN = (aoLightValueScratchXZIN + aoOpposingBlock + aoLightValueScratchXYZIPN + aoLightValueScratchXYIP) / 4.0F;
        float aoXYZNNN = (aoLightValueScratchXYZINN + aoLightValueScratchXYIN + aoLightValueScratchXZIN + aoOpposingBlock) / 4.0F;

        float aoTL = (float)((double)aoXYZNPP * state.renderMaxY * state.renderMaxZ + (double)aoXYZNPN * state.renderMaxY * (1.0D - state.renderMaxZ) + (double)aoXYZNNN * (1.0D - state.renderMaxY) * (1.0D - state.renderMaxZ) + (double)aoXYZNNP * (1.0D - state.renderMaxY) * state.renderMaxZ);
        float aoBL = (float)((double)aoXYZNPP * state.renderMaxY * state.renderMinZ + (double)aoXYZNPN * state.renderMaxY * (1.0D - state.renderMinZ) + (double)aoXYZNNN * (1.0D - state.renderMaxY) * (1.0D - state.renderMinZ) + (double)aoXYZNNP * (1.0D - state.renderMaxY) * state.renderMinZ);
        float aoBR = (float)((double)aoXYZNPP * state.renderMinY * state.renderMinZ + (double)aoXYZNPN * state.renderMinY * (1.0D - state.renderMinZ) + (double)aoXYZNNN * (1.0D - state.renderMinY) * (1.0D - state.renderMinZ) + (double)aoXYZNNP * (1.0D - state.renderMinY) * state.renderMinZ);
        float aoTR = (float)((double)aoXYZNPP * state.renderMinY * state.renderMaxZ + (double)aoXYZNPN * state.renderMinY * (1.0D - state.renderMaxZ) + (double)aoXYZNNN * (1.0D - state.renderMinY) * (1.0D - state.renderMaxZ) + (double)aoXYZNNP * (1.0D - state.renderMinY) * state.renderMaxZ);

        int brXYZNNP = getAOBrightness(aoBrightnessXYIN, aoBrightnessXYZINP, aoBrightnessXZIP, blockBrightness);
        int brXYZNPP = getAOBrightness(aoBrightnessXZIP, aoBrightnessXYIP, aoBrightnessXYZIPP, blockBrightness);
        int brXYZNPN = getAOBrightness(aoBrightnessXZIN, aoBrightnessXYZIPN, aoBrightnessXYIP, blockBrightness);
        int brXYZNNN = getAOBrightness(aoBrightnessXYZINN, aoBrightnessXYIN, aoBrightnessXZIN, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, state.renderMaxY * state.renderMaxZ, state.renderMaxY * (1.0D - state.renderMaxZ), (1.0D - state.renderMaxY) * (1.0D - state.renderMaxZ), (1.0D - state.renderMaxY) * state.renderMaxZ);
        state.brightnessBottomLeft = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, state.renderMaxY * state.renderMinZ, state.renderMaxY * (1.0D - state.renderMinZ), (1.0D - state.renderMaxY) * (1.0D - state.renderMinZ), (1.0D - state.renderMaxY) * state.renderMinZ);
        state.brightnessBottomRight = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, state.renderMinY * state.renderMinZ, state.renderMinY * (1.0D - state.renderMinZ), (1.0D - state.renderMinY) * (1.0D - state.renderMinZ), (1.0D - state.renderMinY) * state.renderMinZ);
        state.brightnessTopRight = mixAOBrightness(brXYZNPP, brXYZNPN, brXYZNNN, brXYZNNP, state.renderMinY * state.renderMaxZ, state.renderMinY * (1.0D - state.renderMaxZ), (1.0D - state.renderMinY) * (1.0D - state.renderMaxZ), (1.0D - state.renderMinY) * state.renderMaxZ);

        state.setColor(r * state.colorMultXNeg, g * state.colorMultXNeg, b * state.colorMultXNeg);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    public void setupXPosAOPartial (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, float r, float g, float b) {
        state.brightness = 983055;

        BlockPos posEast = pos.east();
        BlockPos posGrass = (state.renderMaxX >= 1) ? posEast : pos;

        boolean blocksGrassXYNP = !blockAccess.getBlockState(posGrass.up()).isTranslucent();
        boolean blocksGrassXYNN = !blockAccess.getBlockState(posGrass.down()).isTranslucent();
        boolean blocksGrassXZNN = !blockAccess.getBlockState(posGrass.north()).isTranslucent();
        boolean blocksGrassXZNP = !blockAccess.getBlockState(posGrass.south()).isTranslucent();

        if (state.renderMaxX < 1)
            setupAOBrightnessXPos(blockAccess, blockState, pos, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        setupAOBrightnessXNeg(blockAccess, blockState, posEast, blocksGrassXYNP, blocksGrassXYNN, blocksGrassXZNN, blocksGrassXZNP);

        float xClamp = MathHelper.clamp_float((float) state.renderMaxX, 0, 1);
        mixAOBrightnessLightValueX(xClamp, 1 - xClamp);

        int blockBrightness = blockState.getPackedLightmapCoords(blockAccess, pos);
        if (state.renderMaxX >= 1.0D || !blockAccess.getBlockState(posEast).isOpaqueCube())
            blockBrightness = blockState.getPackedLightmapCoords(blockAccess, posEast);

        float aoOpposingBlock = blockAccess.getBlockState(posEast).getAmbientOcclusionLightValue();
        float aoXYZPNP = (aoLightValueScratchXYIN + aoLightValueScratchXYZINP + aoOpposingBlock + aoLightValueScratchXZIP) / 4.0F;
        float aoXYZPNN = (aoLightValueScratchXYZINN + aoLightValueScratchXYIN + aoLightValueScratchXZIN + aoOpposingBlock) / 4.0F;
        float aoXYZPPN = (aoLightValueScratchXZIN + aoOpposingBlock + aoLightValueScratchXYZIPN + aoLightValueScratchXYIP) / 4.0F;
        float aoXYZPPP = (aoOpposingBlock + aoLightValueScratchXZIP + aoLightValueScratchXYIP + aoLightValueScratchXYZIPP) / 4.0F;

        float aoTL = (float)((double)aoXYZPNP * (1.0D - state.renderMinY) * state.renderMaxZ + (double)aoXYZPNN * (1.0D - state.renderMinY) * (1.0D - state.renderMaxZ) + (double)aoXYZPPN * state.renderMinY * (1.0D - state.renderMaxZ) + (double)aoXYZPPP * state.renderMinY * state.renderMaxZ);
        float aoBL = (float)((double)aoXYZPNP * (1.0D - state.renderMinY) * state.renderMinZ + (double)aoXYZPNN * (1.0D - state.renderMinY) * (1.0D - state.renderMinZ) + (double)aoXYZPPN * state.renderMinY * (1.0D - state.renderMinZ) + (double)aoXYZPPP * state.renderMinY * state.renderMinZ);
        float aoBR = (float)((double)aoXYZPNP * (1.0D - state.renderMaxY) * state.renderMinZ + (double)aoXYZPNN * (1.0D - state.renderMaxY) * (1.0D - state.renderMinZ) + (double)aoXYZPPN * state.renderMaxY * (1.0D - state.renderMinZ) + (double)aoXYZPPP * state.renderMaxY * state.renderMinZ);
        float aoTR = (float)((double)aoXYZPNP * (1.0D - state.renderMaxY) * state.renderMaxZ + (double)aoXYZPNN * (1.0D - state.renderMaxY) * (1.0D - state.renderMaxZ) + (double)aoXYZPPN * state.renderMaxY * (1.0D - state.renderMaxZ) + (double)aoXYZPPP * state.renderMaxY * state.renderMaxZ);

        int brXYZPNP = getAOBrightness(aoBrightnessXYIN, aoBrightnessXYZINP, aoBrightnessXZIP, blockBrightness);
        int brXYZPNN = getAOBrightness(aoBrightnessXZIP, aoBrightnessXYIP, aoBrightnessXYZIPP, blockBrightness);
        int brXYZPPN = getAOBrightness(aoBrightnessXZIN, aoBrightnessXYZIPN, aoBrightnessXYIP, blockBrightness);
        int brXYZPPP = getAOBrightness(aoBrightnessXYZINN, aoBrightnessXYIN, aoBrightnessXZIN, blockBrightness);

        state.brightnessTopLeft = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - state.renderMinY) * state.renderMaxZ, (1.0D - state.renderMinY) * (1.0D - state.renderMaxZ), state.renderMinY * (1.0D - state.renderMaxZ), state.renderMinY * state.renderMaxZ);
        state.brightnessBottomLeft = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - state.renderMinY) * state.renderMinZ, (1.0D - state.renderMinY) * (1.0D - state.renderMinZ), state.renderMinY * (1.0D - state.renderMinZ), state.renderMinY * state.renderMinZ);
        state.brightnessBottomRight = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - state.renderMaxY) * state.renderMinZ, (1.0D - state.renderMaxY) * (1.0D - state.renderMinZ), state.renderMaxY * (1.0D - state.renderMinZ), state.renderMaxY * state.renderMinZ);
        state.brightnessTopRight = mixAOBrightness(brXYZPNP, brXYZPPP, brXYZPPN, brXYZPNN, (1.0D - state.renderMaxY) * state.renderMaxZ, (1.0D - state.renderMaxY) * (1.0D - state.renderMaxZ), state.renderMaxY * (1.0D - state.renderMaxZ), state.renderMaxY * state.renderMaxZ);

        state.setColor(r * state.colorMultXPos, g * state.colorMultXPos, b * state.colorMultXPos);
        state.scaleColor(state.colorTopLeft, aoTL);
        state.scaleColor(state.colorBottomLeft, aoBL);
        state.scaleColor(state.colorBottomRight, aoBR);
        state.scaleColor(state.colorTopRight, aoTR);
    }

    private void setupAOBrightnessYNeg (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, boolean bgXP, boolean bgXN, boolean bgZP, boolean bgZN) {
        BlockPos posWest = pos.west();
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posEast = pos.east();

        aoLightValueScratchXYNN = blockAccess.getBlockState(posWest).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNN = blockAccess.getBlockState(posNorth).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNP = blockAccess.getBlockState(posSouth).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPN = blockAccess.getBlockState(posEast).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
        aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
        aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
        aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;

        aoBrightnessXYNN = blockState.getPackedLightmapCoords(blockAccess, posWest);
        aoBrightnessYZNN = blockState.getPackedLightmapCoords(blockAccess, posNorth);
        aoBrightnessYZNP = blockState.getPackedLightmapCoords(blockAccess, posSouth);
        aoBrightnessXYPN = blockState.getPackedLightmapCoords(blockAccess, posEast);
        aoBrightnessXYZNNN = aoBrightnessXYNN;
        aoBrightnessXYZNNP = aoBrightnessXYNN;
        aoBrightnessXYZPNN = aoBrightnessXYPN;
        aoBrightnessXYZPNP = aoBrightnessXYPN;

        if (bgXN || bgZN) {
            BlockPos posCorner = pos.add(-1, 0, -1);
            aoLightValueScratchXYZNNN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXN || bgZP) {
            BlockPos posCorner = pos.add(-1, 0, 1);
            aoLightValueScratchXYZNNP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgZN) {
            BlockPos posCorner = pos.add(1, 0, -1);
            aoLightValueScratchXYZPNN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgZP) {
            BlockPos posCorner = pos.add(1, 0, 1);
            aoLightValueScratchXYZPNP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }
    }

    private void setupAOBrightnessYPos (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, boolean bgXP, boolean bgXN, boolean bgZP, boolean bgZN) {
        BlockPos posWest = pos.west();
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posEast = pos.east();

        aoLightValueScratchXYNP = blockAccess.getBlockState(posWest).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPN = blockAccess.getBlockState(posNorth).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPP = blockAccess.getBlockState(posSouth).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPP = blockAccess.getBlockState(posEast).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
        aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
        aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
        aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;

        aoBrightnessXYNP = blockState.getPackedLightmapCoords(blockAccess, posWest);
        aoBrightnessYZPN = blockState.getPackedLightmapCoords(blockAccess, posNorth);
        aoBrightnessYZPP = blockState.getPackedLightmapCoords(blockAccess, posSouth);
        aoBrightnessXYPP = blockState.getPackedLightmapCoords(blockAccess, posEast);
        aoBrightnessXYZNPN = aoBrightnessXYNP;
        aoBrightnessXYZNPP = aoBrightnessXYNP;
        aoBrightnessXYZPPN = aoBrightnessXYPP;
        aoBrightnessXYZPPP = aoBrightnessXYPP;

        if (bgXN || bgZN) {
            BlockPos posCorner = pos.add(-1, 0, -1);
            aoLightValueScratchXYZNPN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXN || bgZP) {
            BlockPos posCorner = pos.add(-1, 0, 1);
            aoLightValueScratchXYZNPP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgZN) {
            BlockPos posCorner = pos.add(1, 0, -1);
            aoLightValueScratchXYZPPN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgZP) {
            BlockPos posCorner = pos.add(1, 0, 1);
            aoLightValueScratchXYZPPP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }
    }

    private void setupAOBrightnessZNeg (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, boolean bgXP, boolean bgXN, boolean bgYP, boolean bgYN) {
        BlockPos posWest = pos.west();
        BlockPos posDown = pos.down();
        BlockPos posUp = pos.up();
        BlockPos posEast = pos.east();

        aoLightValueScratchXZNN = blockAccess.getBlockState(posWest).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNN = blockAccess.getBlockState(posDown).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPN = blockAccess.getBlockState(posUp).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPN = blockAccess.getBlockState(posEast).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
        aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;

        aoBrightnessXZNN = blockState.getPackedLightmapCoords(blockAccess, posWest);
        aoBrightnessYZNN = blockState.getPackedLightmapCoords(blockAccess, posDown);
        aoBrightnessYZPN = blockState.getPackedLightmapCoords(blockAccess, posUp);
        aoBrightnessXZPN = blockState.getPackedLightmapCoords(blockAccess, posEast);
        aoBrightnessXYZNNN = aoBrightnessXZNN;
        aoBrightnessXYZNPN = aoBrightnessXZNN;
        aoBrightnessXYZPNN = aoBrightnessXZPN;
        aoBrightnessXYZPPN = aoBrightnessXZPN;

        if (bgXN || bgYN) {
            BlockPos posCorner = pos.add(-1, -1, 0);
            aoLightValueScratchXYZNNN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXN || bgYP) {
            BlockPos posCorner = pos.add(-1, 1, 0);
            aoLightValueScratchXYZNPN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgYN) {
            BlockPos posCorner = pos.add(1, -1, 0);
            aoLightValueScratchXYZPNN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgYP) {
            BlockPos posCorner = pos.add(1, 1, 0);
            aoLightValueScratchXYZPPN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }
    }

    private void setupAOBrightnessZPos (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, boolean bgXP, boolean bgXN, boolean bgYP, boolean bgYN) {
        BlockPos posWest = pos.west();
        BlockPos posDown = pos.down();
        BlockPos posUp = pos.up();
        BlockPos posEast = pos.east();

        aoLightValueScratchXZNP = blockAccess.getBlockState(posWest).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPP = blockAccess.getBlockState(posEast).getAmbientOcclusionLightValue();
        aoLightValueScratchYZNP = blockAccess.getBlockState(posDown).getAmbientOcclusionLightValue();
        aoLightValueScratchYZPP = blockAccess.getBlockState(posUp).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
        aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
        aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
        aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;

        aoBrightnessXZNP = blockState.getPackedLightmapCoords(blockAccess, posWest);
        aoBrightnessXZPP = blockState.getPackedLightmapCoords(blockAccess, posEast);
        aoBrightnessYZNP = blockState.getPackedLightmapCoords(blockAccess, posDown);
        aoBrightnessYZPP = blockState.getPackedLightmapCoords(blockAccess, posUp);
        aoBrightnessXYZNNP = aoBrightnessXZNP;
        aoBrightnessXYZNPP = aoBrightnessXZNP;
        aoBrightnessXYZPNP = aoBrightnessXZPP;
        aoBrightnessXYZPPP = aoBrightnessXZPP;

        if (bgXN || bgYN) {
            BlockPos posCorner = pos.add(-1, -1, 0);
            aoLightValueScratchXYZNNP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXN || bgYP) {
            BlockPos posCorner = pos.add(-1, 1, 0);
            aoLightValueScratchXYZNPP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgYN) {
            BlockPos posCorner = pos.add(1, -1, 0);
            aoLightValueScratchXYZPNP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgXP || bgYP) {
            BlockPos posCorner = pos.add(1, 1, 0);
            aoLightValueScratchXYZPPP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }
    }

    private void setupAOBrightnessXNeg (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, boolean bgYP, boolean bgYN, boolean bgZN, boolean bgZP) {
        BlockPos posDown = pos.down();
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posUp = pos.up();

        aoLightValueScratchXYNN = blockAccess.getBlockState(posDown).getAmbientOcclusionLightValue();
        aoLightValueScratchXZNN = blockAccess.getBlockState(posNorth).getAmbientOcclusionLightValue();
        aoLightValueScratchXZNP = blockAccess.getBlockState(posSouth).getAmbientOcclusionLightValue();
        aoLightValueScratchXYNP = blockAccess.getBlockState(posUp).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
        aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
        aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;

        aoBrightnessXYNN = blockState.getPackedLightmapCoords(blockAccess, posDown);
        aoBrightnessXZNN = blockState.getPackedLightmapCoords(blockAccess, posNorth);
        aoBrightnessXZNP = blockState.getPackedLightmapCoords(blockAccess, posSouth);
        aoBrightnessXYNP = blockState.getPackedLightmapCoords(blockAccess, posUp);
        aoBrightnessXYZNNN = aoBrightnessXZNN;
        aoBrightnessXYZNNP = aoBrightnessXZNP;
        aoBrightnessXYZNPN = aoBrightnessXZNN;
        aoBrightnessXYZNPP = aoBrightnessXZNP;

        if (bgZN || bgYN) {
            BlockPos posCorner = pos.add(0, -1, -1);
            aoLightValueScratchXYZNNN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgZP || bgYN) {
            BlockPos posCorner = pos.add(0, -1, 1);
            aoLightValueScratchXYZNNP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNNP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgZN || bgYP) {
            BlockPos posCorner = pos.add(0, 1, -1);
            aoLightValueScratchXYZNPN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgZP || bgYP) {
            BlockPos posCorner = pos.add(0, 1, 1);
            aoLightValueScratchXYZNPP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZNPP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }
    }

    private void setupAOBrightnessXPos (IBlockAccess blockAccess, IBlockState blockState, BlockPos pos, boolean bgYP, boolean bgYN, boolean bgZN, boolean bgZP) {
        BlockPos posDown = pos.down();
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posUp = pos.up();

        aoLightValueScratchXYPN = blockAccess.getBlockState(posDown).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPN = blockAccess.getBlockState(posNorth).getAmbientOcclusionLightValue();
        aoLightValueScratchXZPP = blockAccess.getBlockState(posSouth).getAmbientOcclusionLightValue();
        aoLightValueScratchXYPP = blockAccess.getBlockState(posUp).getAmbientOcclusionLightValue();
        aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
        aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
        aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
        aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;

        aoBrightnessXYPN = blockState.getPackedLightmapCoords(blockAccess, posDown);
        aoBrightnessXZPN = blockState.getPackedLightmapCoords(blockAccess, posNorth);
        aoBrightnessXZPP = blockState.getPackedLightmapCoords(blockAccess, posSouth);
        aoBrightnessXYPP = blockState.getPackedLightmapCoords(blockAccess, posUp);
        aoBrightnessXYZPNN = aoBrightnessXZPN;
        aoBrightnessXYZPNP = aoBrightnessXZPP;
        aoBrightnessXYZPPN = aoBrightnessXZPN;
        aoBrightnessXYZPPP = aoBrightnessXZPP;

        if (bgYN || bgZN) {
            BlockPos posCorner = pos.add(0, -1, -1);
            aoLightValueScratchXYZPNN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgYN || bgZP) {
            BlockPos posCorner = pos.add(0, -1, 1);
            aoLightValueScratchXYZPNP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPNP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgYP || bgZN) {
            BlockPos posCorner = pos.add(0, 1, -1);
            aoLightValueScratchXYZPPN = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPN = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }

        if (bgYP || bgZP) {
            BlockPos posCorner = pos.add(0, 1, 1);
            aoLightValueScratchXYZPPP = blockAccess.getBlockState(posCorner).getAmbientOcclusionLightValue();
            aoBrightnessXYZPPP = blockState.getPackedLightmapCoords(blockAccess, posCorner);
        }
    }

    private void mixAOBrightnessLightValueY (float fMin, float fMax) {
        if (fMin == 1 && fMax == 0) {
            aoLightValueScratchXYNI = aoLightValueScratchXYNN;
            aoLightValueScratchYZIN = aoLightValueScratchYZNN;
            aoLightValueScratchYZIP = aoLightValueScratchYZNP;
            aoLightValueScratchXYPI = aoLightValueScratchXYPN;
            aoLightValueScratchXYZNIN = aoLightValueScratchXYZNNN;
            aoLightValueScratchXYZNIP = aoLightValueScratchXYZNNP;
            aoLightValueScratchXYZPIN = aoLightValueScratchXYZPNN;
            aoLightValueScratchXYZPIP = aoLightValueScratchXYZPNP;

            aoBrightnessXYNI = aoBrightnessXYNN;
            aoBrightnessYZIN = aoBrightnessYZNN;
            aoBrightnessYZIP = aoBrightnessYZNP;
            aoBrightnessXYPI = aoBrightnessXYPN;
            aoBrightnessXYZNIN = aoBrightnessXYZNNN;
            aoBrightnessXYZNIP = aoBrightnessXYZNNP;
            aoBrightnessXYZPIN = aoBrightnessXYZPNN;
            aoBrightnessXYZPIP = aoBrightnessXYZPNP;
        }
        else if (fMin == 0 && fMax == 1) {
            aoLightValueScratchXYNI = aoLightValueScratchXYNP;
            aoLightValueScratchYZIN = aoLightValueScratchYZPN;
            aoLightValueScratchYZIP = aoLightValueScratchYZPP;
            aoLightValueScratchXYPI = aoLightValueScratchXYPP;
            aoLightValueScratchXYZNIN = aoLightValueScratchXYZNPN;
            aoLightValueScratchXYZNIP = aoLightValueScratchXYZNPP;
            aoLightValueScratchXYZPIN = aoLightValueScratchXYZPPN;
            aoLightValueScratchXYZPIP = aoLightValueScratchXYZPPP;

            aoBrightnessXYNI = aoBrightnessXYNP;
            aoBrightnessYZIN = aoBrightnessYZPN;
            aoBrightnessYZIP = aoBrightnessYZPP;
            aoBrightnessXYPI = aoBrightnessXYPP;
            aoBrightnessXYZNIN = aoBrightnessXYZNPN;
            aoBrightnessXYZNIP = aoBrightnessXYZNPP;
            aoBrightnessXYZPIN = aoBrightnessXYZPPN;
            aoBrightnessXYZPIP = aoBrightnessXYZPPP;
        }
        else {
            aoLightValueScratchXYNI = aoLightValueScratchXYNN * fMin + aoLightValueScratchXYNP * fMax;
            aoLightValueScratchYZIN = aoLightValueScratchYZNN * fMin + aoLightValueScratchYZPN * fMax;
            aoLightValueScratchYZIP = aoLightValueScratchYZNP * fMin + aoLightValueScratchYZPP * fMax;
            aoLightValueScratchXYPI = aoLightValueScratchXYPN * fMin + aoLightValueScratchXYPP * fMax;
            aoLightValueScratchXYZNIN = aoLightValueScratchXYZNNN * fMin + aoLightValueScratchXYZNPN * fMax;
            aoLightValueScratchXYZNIP = aoLightValueScratchXYZNNP * fMin + aoLightValueScratchXYZNPP * fMax;
            aoLightValueScratchXYZPIN = aoLightValueScratchXYZPNN * fMin + aoLightValueScratchXYZPPN * fMax;
            aoLightValueScratchXYZPIP = aoLightValueScratchXYZPNP * fMin + aoLightValueScratchXYZPPP * fMax;

            aoBrightnessXYNI = mixAOBrightness(aoBrightnessXYNN, aoBrightnessXYNP, fMin, fMax);
            aoBrightnessYZIN = mixAOBrightness(aoBrightnessYZNN, aoBrightnessYZPN, fMin, fMax);
            aoBrightnessYZIP = mixAOBrightness(aoBrightnessYZNP, aoBrightnessYZPP, fMin, fMax);
            aoBrightnessXYPI = mixAOBrightness(aoBrightnessXYPN, aoBrightnessXYPP, fMin, fMax);
            aoBrightnessXYZNIN = mixAOBrightness(aoBrightnessXYZNNN, aoBrightnessXYZNPN, fMin, fMax);
            aoBrightnessXYZNIP = mixAOBrightness(aoBrightnessXYZNNP, aoBrightnessXYZNPP, fMin, fMax);
            aoBrightnessXYZPIN = mixAOBrightness(aoBrightnessXYZPNN, aoBrightnessXYZPPN, fMin, fMax);
            aoBrightnessXYZPIP = mixAOBrightness(aoBrightnessXYZPNP, aoBrightnessXYZPPP, fMin, fMax);
        }
    }

    private void mixAOBrightnessLightValueZ (float fMin, float fMax) {
        if (fMin == 1 && fMax == 0) {
            aoLightValueScratchXZNI = aoLightValueScratchXZNN;
            aoLightValueScratchYZNI = aoLightValueScratchYZNN;
            aoLightValueScratchYZPI = aoLightValueScratchYZPN;
            aoLightValueScratchXZPI = aoLightValueScratchXZPN;
            aoLightValueScratchXYZNNI = aoLightValueScratchXYZNNN;
            aoLightValueScratchXYZNPI = aoLightValueScratchXYZNPN;
            aoLightValueScratchXYZPNI = aoLightValueScratchXYZPNN;
            aoLightValueScratchXYZPPI = aoLightValueScratchXYZPPN;

            aoBrightnessXZNI = aoBrightnessXZNN;
            aoBrightnessYZNI = aoBrightnessYZNN;
            aoBrightnessYZPI = aoBrightnessYZPN;
            aoBrightnessXZPI = aoBrightnessXZPN;
            aoBrightnessXYZNNI = aoBrightnessXYZNNN;
            aoBrightnessXYZNPI = aoBrightnessXYZNPN;
            aoBrightnessXYZPNI = aoBrightnessXYZPNN;
            aoBrightnessXYZPPI = aoBrightnessXYZPPN;
        }
        else if (fMin == 0 && fMax == 1) {
            aoLightValueScratchXZNI = aoLightValueScratchXZNP;
            aoLightValueScratchYZNI = aoLightValueScratchYZNP;
            aoLightValueScratchYZPI = aoLightValueScratchYZPP;
            aoLightValueScratchXZPI = aoLightValueScratchXZPP;
            aoLightValueScratchXYZNNI = aoLightValueScratchXYZNNP;
            aoLightValueScratchXYZNPI = aoLightValueScratchXYZNPP;
            aoLightValueScratchXYZPNI = aoLightValueScratchXYZPNP;
            aoLightValueScratchXYZPPI = aoLightValueScratchXYZPPP;

            aoBrightnessXZNI = aoBrightnessXZNP;
            aoBrightnessYZNI = aoBrightnessYZNP;
            aoBrightnessYZPI = aoBrightnessYZPP;
            aoBrightnessXZPI = aoBrightnessXZPP;
            aoBrightnessXYZNNI = aoBrightnessXYZNNP;
            aoBrightnessXYZNPI = aoBrightnessXYZNPP;
            aoBrightnessXYZPNI = aoBrightnessXYZPNP;
            aoBrightnessXYZPPI = aoBrightnessXYZPPP;
        }
        else {
            aoLightValueScratchXZNI = aoLightValueScratchXZNN * fMin + aoLightValueScratchXZNP * fMax;
            aoLightValueScratchYZNI = aoLightValueScratchYZNN * fMin + aoLightValueScratchYZNP * fMax;
            aoLightValueScratchYZPI = aoLightValueScratchYZPN * fMin + aoLightValueScratchYZPP * fMax;
            aoLightValueScratchXZPI = aoLightValueScratchXZPN * fMin + aoLightValueScratchXZPP * fMax;
            aoLightValueScratchXYZNNI = aoLightValueScratchXYZNNN * fMin + aoLightValueScratchXYZNNP * fMax;
            aoLightValueScratchXYZNPI = aoLightValueScratchXYZNPN * fMin + aoLightValueScratchXYZNPP * fMax;
            aoLightValueScratchXYZPNI = aoLightValueScratchXYZPNN * fMin + aoLightValueScratchXYZPNP * fMax;
            aoLightValueScratchXYZPPI = aoLightValueScratchXYZPPN * fMin + aoLightValueScratchXYZPPP * fMax;

            aoBrightnessXZNI = mixAOBrightness(aoBrightnessXZNN, aoBrightnessXZNP, fMin, fMax);
            aoBrightnessYZNI = mixAOBrightness(aoBrightnessYZNN, aoBrightnessYZNP, fMin, fMax);
            aoBrightnessYZPI = mixAOBrightness(aoBrightnessYZPN, aoBrightnessYZPP, fMin, fMax);
            aoBrightnessXZPI = mixAOBrightness(aoBrightnessXZPN, aoBrightnessXZPP, fMin, fMax);
            aoBrightnessXYZNNI = mixAOBrightness(aoBrightnessXYZNNN, aoBrightnessXYZNNP, fMin, fMax);
            aoBrightnessXYZNPI = mixAOBrightness(aoBrightnessXYZNPN, aoBrightnessXYZNPP, fMin, fMax);
            aoBrightnessXYZPNI = mixAOBrightness(aoBrightnessXYZPNN, aoBrightnessXYZPNP, fMin, fMax);
            aoBrightnessXYZPPI = mixAOBrightness(aoBrightnessXYZPPN, aoBrightnessXYZPPP, fMin, fMax);
        }
    }

    private void mixAOBrightnessLightValueX (float fMin, float fMax) {
        if (fMin == 1 && fMax == 0) {
            aoLightValueScratchXYIN = aoLightValueScratchXYNN;
            aoLightValueScratchXZIN = aoLightValueScratchXZNN;
            aoLightValueScratchXZIP = aoLightValueScratchXZNP;
            aoLightValueScratchXYIP = aoLightValueScratchXYNP;
            aoLightValueScratchXYZINN = aoLightValueScratchXYZNNN;
            aoLightValueScratchXYZINP = aoLightValueScratchXYZNNP;
            aoLightValueScratchXYZIPN = aoLightValueScratchXYZNPN;
            aoLightValueScratchXYZIPP = aoLightValueScratchXYZNPP;

            aoBrightnessXYIN = aoBrightnessXYNN;
            aoBrightnessXZIN = aoBrightnessXZNN;
            aoBrightnessXZIP = aoBrightnessXZNP;
            aoBrightnessXYIP = aoBrightnessXYNP;
            aoBrightnessXYZINN = aoBrightnessXYZNNN;
            aoBrightnessXYZINP = aoBrightnessXYZNNP;
            aoBrightnessXYZIPN = aoBrightnessXYZNPN;
            aoBrightnessXYZIPP = aoBrightnessXYZNPP;
        }
        else if (fMin == 0 && fMax == 1) {
            aoLightValueScratchXYIN = aoLightValueScratchXYPN;
            aoLightValueScratchXZIN = aoLightValueScratchXZPN;
            aoLightValueScratchXZIP = aoLightValueScratchXZPP;
            aoLightValueScratchXYIP = aoLightValueScratchXYPP;
            aoLightValueScratchXYZINN = aoLightValueScratchXYZPNN;
            aoLightValueScratchXYZINP = aoLightValueScratchXYZPNP;
            aoLightValueScratchXYZIPN = aoLightValueScratchXYZPPN;
            aoLightValueScratchXYZIPP = aoLightValueScratchXYZPPP;

            aoBrightnessXYIN = aoBrightnessXYPN;
            aoBrightnessXZIN = aoBrightnessXZPN;
            aoBrightnessXZIP = aoBrightnessXZPP;
            aoBrightnessXYIP = aoBrightnessXYPP;
            aoBrightnessXYZINN = aoBrightnessXYZPNN;
            aoBrightnessXYZINP = aoBrightnessXYZPNP;
            aoBrightnessXYZIPN = aoBrightnessXYZPPN;
            aoBrightnessXYZIPP = aoBrightnessXYZPPP;
        }
        else {
            aoLightValueScratchXYIN = aoLightValueScratchXYNN * fMin + aoLightValueScratchXYPN * fMax;
            aoLightValueScratchXZIN = aoLightValueScratchXZNN * fMin + aoLightValueScratchXZPN * fMax;
            aoLightValueScratchXZIP = aoLightValueScratchXZNP * fMin + aoLightValueScratchXZPP * fMax;
            aoLightValueScratchXYIP = aoLightValueScratchXYNP * fMin + aoLightValueScratchXYPP * fMax;
            aoLightValueScratchXYZINN = aoLightValueScratchXYZNNN * fMin + aoLightValueScratchXYZPNN * fMax;
            aoLightValueScratchXYZINP = aoLightValueScratchXYZNNP * fMin + aoLightValueScratchXYZPNP * fMax;
            aoLightValueScratchXYZIPN = aoLightValueScratchXYZNPN * fMin + aoLightValueScratchXYZPPN * fMax;
            aoLightValueScratchXYZIPP = aoLightValueScratchXYZNPP * fMin + aoLightValueScratchXYZPPP * fMax;

            aoBrightnessXYIN = mixAOBrightness(aoBrightnessXYNN, aoBrightnessXYPN, fMin, fMax);
            aoBrightnessXZIN = mixAOBrightness(aoBrightnessXZNN, aoBrightnessXZPN, fMin, fMax);
            aoBrightnessXZIP = mixAOBrightness(aoBrightnessXZNP, aoBrightnessXZPP, fMin, fMax);
            aoBrightnessXYIP = mixAOBrightness(aoBrightnessXYNP, aoBrightnessXYPP, fMin, fMax);
            aoBrightnessXYZINN = mixAOBrightness(aoBrightnessXYZNNN, aoBrightnessXYZPNN, fMin, fMax);
            aoBrightnessXYZINP = mixAOBrightness(aoBrightnessXYZNNP, aoBrightnessXYZPNP, fMin, fMax);
            aoBrightnessXYZIPN = mixAOBrightness(aoBrightnessXYZNPN, aoBrightnessXYZPPN, fMin, fMax);
            aoBrightnessXYZIPP = mixAOBrightness(aoBrightnessXYZNPP, aoBrightnessXYZPPP, fMin, fMax);
        }
    }


    public static int getAOBrightness (int com1, int com2, int com3, int base) {
        if (com1 == 0)
            com1 = base;
        if (com2 == 0)
            com2 = base;
        if (com3 == 0)
            com3 = base;

        return com1 + com2 + com3 + base >> 2 & 16711935;
    }

    public static int mixAOBrightness (int part1, int part2, int part3, int part4, double weight1, double weight2, double weight3, double weight4) {
        int brightSky = (int)((part1 >> 16 & 255) * weight1 + (part2 >> 16 & 255) * weight2 + (part3 >> 16 & 255) * weight3 + (part4 >> 16 & 255) * weight4) & 255;
        int brightBlk = (int)((part1 & 255) * weight1 + (part2 & 255) * weight2 + (part3 & 255) * weight3 + (part4 & 255) * weight4) & 255;

        return brightSky << 16 | brightBlk;
    }

    public static int mixAOBrightness (int brightTL, int brightBL, int brightBR, int brightTR, double lerpTB, double lerpLR) {
        double brightSkyL = (brightTL >> 16 & 255) * (1 - lerpTB) + (brightBL >> 16 & 255) * lerpTB;
        double brightSkyR = (brightTR >> 16 & 255) * (1 - lerpTB) + (brightBR >> 16 & 255) * lerpTB;
        int brightSky = (int)(brightSkyL * (1 - lerpLR) + brightSkyR * lerpLR) & 255;

        double brightBlkL = (brightTL & 255) * (1 - lerpTB) + (brightBL & 255) * lerpTB;
        double brightBlkR = (brightTR & 255) * (1 - lerpTB) + (brightBR & 255) * lerpTB;
        int brightBlk = (int)(brightBlkL * (1 - lerpLR) + brightBlkR * lerpLR) & 255;

        return brightSky << 16 | brightBlk;
    }

    public static int mixAOBrightness (int brightMin, int brightMax, float fMin, float fMax) {
        if (brightMin == 0)
            return 0;
        if (brightMax == 0)
            return 0;

        float brightSky = (brightMin >> 16 & 255) * fMin + (brightMax >> 16 & 255) * fMax;
        float brightBlk = (brightMin & 255) * fMin + (brightMax & 255) * fMax;

        return ((int)brightSky & 255) << 16 | ((int)brightBlk & 255);
    }
}
