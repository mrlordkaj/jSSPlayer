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
package com.minipedia.codec.ssbp.data;

/**
 *
 * @author Thinh Pham
 */
public interface SDFrame {
    
    public static final int VERTEX_LT = 0,
                            VERTEX_RT = 1,
                            VERTEX_LB = 2,
                            VERTEX_RB = 3;
    
    public static final int INSTANCE_LOOP_FLAG_INFINITY     = 1,
                            INSTANCE_LOOP_FLAG_REVERSE      = 1 << 1,
                            INSTANCE_LOOP_FLAG_PINGPONG     = 1 << 2,
                            INSTANCE_LOOP_FLAG_INDEPENDENT  = 1 << 3;
    
    public int getPartIndex();
    
    public int getCellIndex();
    
    public float getPositionX();
    
    public float getPositionY();
    
    public float getPositionZ();
    
    public float getPivotX();
    
    public float getPivotY();
    
    public float getRotationX();
    
    public float getRotationY();
    
    public float getRotationZ();
    
    public float getScaleX();
    
    public float getScaleY();
    
    public int getOpacity();
    
    public float getSizeX();
    
    public float getSizeY();
    
    public float getUVMoveX();
    
    public float getUVMoveY();
    
    public float getUVRotation();
    
    public float getUVScaleX();
    
    public float getUVScaleY();
    
    public boolean hasVertexTransform();
    
    public float getVertexTransform(int verticleIndex, int channelIndex);
    
    public boolean isFlipX();
    
    public boolean isFlipY();
    
    public boolean isVisible();
    
    public boolean hasColorBlend();
    
    public SDAnimation getAnimation();
}
