/*
 * Copyright (C) 2018 Thinh Pham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.minipedia.codec.ssbp.v3;

import com.minipedia.codec.ssbp.data.SDAnimation;
import java.nio.ByteBuffer;
import com.minipedia.codec.ssbp.data.SDFrame;

/**
 *
 * @author Thinh Pham
 */
class SRFrame extends SRInitial implements SDFrame {
    
    private static final int    PART_FLAG_INVISIBLE = 1,
                                PART_FLAG_FLIP_H = 1 << 1,
                                PART_FLAG_FLIP_V = 1 << 2,

                                // optional parameter flags
                                PART_FLAG_CELL_INDEX = 1 << 3,
                                PART_FLAG_POSITION_X = 1 << 4,
                                PART_FLAG_POSITION_Y = 1 << 5,
                                PART_FLAG_POSITION_Z = 1 << 6,
                                PART_FLAG_PIVOT_X = 1 << 7,
                                PART_FLAG_PIVOT_Y = 1 << 8,
                                PART_FLAG_ROTATIONX = 1 << 9,
                                PART_FLAG_ROTATIONY = 1 << 10,
                                PART_FLAG_ROTATIONZ = 1 << 11,
                                PART_FLAG_SCALE_X = 1 << 12,
                                PART_FLAG_SCALE_Y = 1 << 13,
                                PART_FLAG_OPACITY = 1 << 14,
                                PART_FLAG_COLOR_BLEND = 1 << 15,
                                PART_FLAG_VERTEX_TRANSFORM = 1 << 16,

                                PART_FLAG_SIZE_X = 1 << 17,
                                PART_FLAG_SIZE_Y = 1 << 18,

                                PART_FLAG_U_MOVE = 1 << 19,
                                PART_FLAG_V_MOVE = 1 << 20,
                                PART_FLAG_UV_ROTATION = 1 << 21,
                                PART_FLAG_U_SCALE = 1 << 22,
                                PART_FLAG_V_SCALE = 1 << 23,
                                PART_FLAG_BOUNDINGRADIUS = 1 << 24,

                                PART_FLAG_INSTANCE_KEYFRAME = 1 << 25,
                                PART_FLAG_INSTANCE_START = 1 << 26,
                                PART_FLAG_INSTANCE_END = 1 << 27,
                                PART_FLAG_INSTANCE_SPEED = 1 << 28,
                                PART_FLAG_INSTANCE_LOOP = 1 << 29,
                                PART_FLAG_INSTANCE_LOOP_FLG = 1 << 30;
    
    private static final int    VERTEX_FLAG_LT  = 1,
                                VERTEX_FLAG_RT  = 1 << 1,
                                VERTEX_FLAG_LB  = 1 << 2,
                                VERTEX_FLAG_RB  = 1 << 3,
                                VERTEX_FLAG_ONE = 1 << 4;  // color blend only
    
    // vertex transform only
    final short[][] vertexTransform = new short[4][2];
    
    // instance only
    short   instKeyframe,
            instStartFrame,
            instEndFrame;
    float   instSpeed;
    short   instNumLoops;
    boolean instLoopInfinity,
            instLoopReverse,
            instLoopPingPong,
            instLoopIndependent;
    
    // extract from flags
    boolean flipX,
            flipY,
            visible,
            hasVertexTransform,
            hasColorBlend;
    
    final SRAnimation _anim;
    
    SRFrame(ByteBuffer bb, int ptr, SRAnimation animation) {
        super(bb, ptr);
        this._anim = animation;
    }

    @Override
    protected int getData() {
        // https://github.com/SpriteStudio/SS5PlayerForCocos2d-x/blob/1.2.5_SS5.6.1/Players/Cocos2d-x_v3/SS5Player.cpp#L2396
        partIndex = bb.getShort();                                                                           // #L2396
        SRInitial initData = _anim.initData[partIndex];
        flags = bb.getInt();                                                                                 // #L2401
        cellIndex = (flags & PART_FLAG_CELL_INDEX) != 0 ? bb.getShort() : initData.cellIndex;                // #L2402
        positionX = (flags & PART_FLAG_POSITION_X) != 0 ? bb.getShort() : initData.positionX;                // #L2403
        positionY = (flags & PART_FLAG_POSITION_Y) != 0 ? bb.getShort() : initData.positionY;                // #L2404
        positionZ = (flags & PART_FLAG_POSITION_Z) != 0 ? bb.getShort() : initData.positionZ;                // #L2405
        pivotX = (flags & PART_FLAG_PIVOT_X) != 0 ? bb.getFloat() : initData.pivotX;                         // #L2406
        pivotY = (flags & PART_FLAG_PIVOT_Y) != 0 ? bb.getFloat() : initData.pivotY;                         // #L2407
        rotationX = (flags & PART_FLAG_ROTATIONX) != 0 ? bb.getFloat() : initData.rotationX;                 // #L2408
        rotationY = (flags & PART_FLAG_ROTATIONY) != 0 ? bb.getFloat() : initData.rotationY;                 // #L2409
        rotationZ = (flags & PART_FLAG_ROTATIONZ) != 0 ? bb.getFloat() : initData.rotationZ;                 // #L2410
        scaleX = (flags & PART_FLAG_SCALE_X) != 0 ? bb.getFloat() : initData.scaleX;                         // #L2411
        scaleY = (flags & PART_FLAG_SCALE_Y) != 0 ? bb.getFloat() : initData.scaleY;                         // #L2412
        opacity = (flags & PART_FLAG_OPACITY) != 0 ? bb.getShort() : initData.opacity;                       // #L2413
        sizeX = (flags & PART_FLAG_SIZE_X) != 0 ? bb.getFloat() : initData.sizeX;                            // #L2414
        sizeY = (flags & PART_FLAG_SIZE_Y) != 0 ? bb.getFloat() : initData.sizeY;                            // #L2415
        uvMoveX = (flags & PART_FLAG_U_MOVE) != 0 ? bb.getFloat() : initData.uvMoveX;                        // #L2416
        uvMoveY = (flags & PART_FLAG_V_MOVE) != 0 ? bb.getFloat() : initData.uvMoveY;                        // #L2417
        uvRotation = (flags & PART_FLAG_UV_ROTATION) != 0 ? bb.getFloat() : initData.uvRotation;             // #L2418
        uvScaleX = (flags & PART_FLAG_U_SCALE) != 0 ? bb.getFloat() : initData.uvScaleX;                     // #L2419
        uvScaleY = (flags & PART_FLAG_V_SCALE) != 0 ? bb.getFloat() : initData.uvScaleY;                     // #L2420
        boundingRadius = (flags & PART_FLAG_BOUNDINGRADIUS) != 0 ? bb.getFloat() : initData.boundingRadius;  // #L2421
        // vertex transform
        hasVertexTransform = (flags & PART_FLAG_VERTEX_TRANSFORM) != 0;
        if (hasVertexTransform) {
            short vertexFlags = bb.getShort();                  // #L2730
            if ((vertexFlags & VERTEX_FLAG_LT) != 0) {
                vertexTransform[VERTEX_LT][0] = bb.getShort();  // #L2733
                vertexTransform[VERTEX_LT][1] = bb.getShort();  // #L2734
            }
            if ((vertexFlags & VERTEX_FLAG_RT) != 0) {
                vertexTransform[VERTEX_RT][0] = bb.getShort();  // #L2738
                vertexTransform[VERTEX_RT][1] = bb.getShort();  // #L2739
            }
            if ((vertexFlags & VERTEX_FLAG_LB) != 0) {
                vertexTransform[VERTEX_LB][0] = bb.getShort();  // #L2743
                vertexTransform[VERTEX_LB][1] = bb.getShort();  // #L2744
            }
            if ((vertexFlags & VERTEX_FLAG_RB) != 0) {
                vertexTransform[VERTEX_RB][0] = bb.getShort();  // #L2748
                vertexTransform[VERTEX_RB][1] = bb.getShort();  // #L2749
            }
        }
        // TODO: color blend
        hasColorBlend = (flags & PART_FLAG_COLOR_BLEND) != 0;
        if (hasColorBlend) {
            int typeAndFlags = bb.getShort();   // #L2795
            int cb_flags = typeAndFlags >> 8;
            float blendRate;
            byte[] argb = new byte[4];
            if ((cb_flags & VERTEX_FLAG_ONE) != 0) {
                blendRate = bb.getFloat();      // #L2804
                readColor(argb, bb);            // #L2805
                argb[0] *= blendRate;
            } else {
                if ((cb_flags & VERTEX_FLAG_LT) != 0) {
                    blendRate = bb.getFloat();  // #L2817
                    readColor(argb, bb);        // #L2805
                    argb[0] *= blendRate;
                }
                if ((cb_flags & VERTEX_FLAG_RT) != 0) {
                    blendRate = bb.getFloat();  // #L2824
                    readColor(argb, bb);        // #L2805
                    argb[0] *= blendRate;
                }
                if ((cb_flags & VERTEX_FLAG_LB) != 0) {
                    blendRate = bb.getFloat();  // #L2832
                    readColor(argb, bb);        // #L2805
                    argb[0] *= blendRate;
                }
                if ((cb_flags & VERTEX_FLAG_RB) != 0) {
                    blendRate = bb.getFloat();  // #L2839
                    readColor(argb, bb);        // #L2805
                    argb[0] *= blendRate;
                }
            }
        }
        // instance
        SRPart partData = _anim._animPack.parts[partIndex];
        if (partData.type == SRPart.PARTTYPE_INSTANCE) {
            if ((flags & PART_FLAG_INSTANCE_KEYFRAME) != 0) {
                instKeyframe = bb.getShort();       // #L2939
            }
            if ((flags & PART_FLAG_INSTANCE_START) != 0) {
                instStartFrame = bb.getShort();     // #L2943
            }
            if ((flags & PART_FLAG_INSTANCE_END) != 0) {
                instEndFrame = bb.getShort();       // #L2947
            }
            if ((flags & PART_FLAG_INSTANCE_SPEED) != 0) {
                instSpeed = bb.getFloat();          // #L2951
            }
            if ((flags & PART_FLAG_INSTANCE_LOOP) != 0) {
                instNumLoops = bb.getShort();       // #L2955
            }
            if ((flags & PART_FLAG_INSTANCE_LOOP_FLG) != 0) {
                short loopFlags = bb.getShort();    // #L2959
                instLoopInfinity = (loopFlags & INSTANCE_LOOP_FLAG_INFINITY) != 0;
                instLoopReverse = (loopFlags & INSTANCE_LOOP_FLAG_REVERSE) != 0;
                instLoopPingPong = (loopFlags & INSTANCE_LOOP_FLAG_PINGPONG) != 0;
                instLoopIndependent = (loopFlags & INSTANCE_LOOP_FLAG_INDEPENDENT) != 0;
            }
        }
        int end = bb.position();
        
        flipX = (flags & PART_FLAG_FLIP_H) != 0;
        flipY = (flags & PART_FLAG_FLIP_V) != 0;
        visible = (flags & PART_FLAG_INVISIBLE) == 0;
        
        return end;
    }
    
    private void readColor(byte[] argb, ByteBuffer bb) {
        argb[0] = bb.get();
        argb[1] = bb.get();
        argb[2] = bb.get();
        argb[3] = bb.get();
    }

    @Override
    public int getPartIndex() {
        return partIndex;
    }

    @Override
    public int getCellIndex() {
        return cellIndex;
    }

    @Override
    public float getPositionX() {
        return positionX * 0.1f;
    }

    @Override
    public float getPositionY() {
        return positionY * 0.1f;
    }

    @Override
    public float getPositionZ() {
        return positionZ * 0.1f;
    }

    @Override
    public float getPivotX() {
        return pivotX;
    }

    @Override
    public float getPivotY() {
        return pivotY;
    }

    @Override
    public float getRotationX() {
        return rotationX;
    }

    @Override
    public float getRotationY() {
        return rotationY;
    }

    @Override
    public float getRotationZ() {
        return rotationZ;
    }

    @Override
    public float getScaleX() {
        return scaleX;
    }

    @Override
    public float getScaleY() {
        return scaleY;
    }

    @Override
    public int getOpacity() {
        return opacity;
    }

    @Override
    public float getSizeX() {
        return sizeX;
    }

    @Override
    public float getSizeY() {
        return sizeY;
    }

    @Override
    public float getUVMoveX() {
        return uvMoveX;
    }

    @Override
    public float getUVMoveY() {
        return uvMoveY;
    }

    @Override
    public float getUVRotation() {
        return uvRotation;
    }

    @Override
    public float getUVScaleX() {
        return uvScaleX;
    }

    @Override
    public float getUVScaleY() {
        return uvScaleY;
    }

    @Override
    public float getVertexTransform(int verticleIndex, int channelIndex) {
        return vertexTransform[verticleIndex][channelIndex];
    }

    @Override
    public boolean isFlipX() {
        return flipX;
    }

    @Override
    public boolean isFlipY() {
        return flipY;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public SDAnimation getAnimation() {
        return _anim;
    }

    @Override
    public boolean hasVertexTransform() {
        return hasVertexTransform;
    }

    @Override
    public boolean hasColorBlend() {
        return hasColorBlend;
    }
}
