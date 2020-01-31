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

import com.minipedia.codec.ssbp.data.SDReader;
import java.nio.ByteBuffer;

/**
 *
 * @author Thinh Pham
 */
class SRInitial extends SDReader {
    
    short   partIndex;  // ss_s16
    int     flags;      // ss_u32
    short   cellIndex,  // ss_s16
            positionX,  // ss_s16 / 10
            positionY,
            positionZ;    
    float   pivotX,
            pivotY,
            rotationX,
            rotationY,
            rotationZ,
            scaleX,
            scaleY;
    int     opacity;    // ss_u16 (+ss_u16 dummy on initial)
    float   sizeX,
            sizeY,
            uvMoveX,
            uvMoveY,
            uvRotation,
            uvScaleX,
            uvScaleY,
            boundingRadius;
    
    SRInitial(ByteBuffer bb, int ptr) {
        super(bb, ptr);
    }
    
    @Override
    protected int getData() {
        partIndex = (short)bb.getInt();
        flags = bb.getInt();
        cellIndex = bb.getShort();
        positionX = bb.getShort();
        positionY = bb.getShort();
        positionZ = bb.getShort();
        opacity = bb.getInt();
        pivotX = bb.getFloat();
        pivotY = bb.getFloat();
        rotationX = bb.getFloat();
        rotationY = bb.getFloat();
        rotationZ = bb.getFloat();
        scaleX = bb.getFloat();
        scaleY = bb.getFloat();
        sizeX = bb.getFloat();
        sizeY = bb.getFloat();
        uvMoveX = bb.getFloat();
        uvMoveY = bb.getFloat();
        uvRotation = bb.getFloat();
        uvScaleX = bb.getFloat();
        uvScaleY = bb.getFloat();
        boundingRadius = bb.getFloat();
        return bb.position();
    }
}
