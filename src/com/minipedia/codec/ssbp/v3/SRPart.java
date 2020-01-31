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

import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDReader;
import java.nio.ByteBuffer;
import com.minipedia.codec.ssbp.data.SDPackage;

/**
 *
 * @author Thinh Pham
 */
class SRPart extends SDReader implements SDPart {
    
    String name;            // ss_offset const char*
    short index;            // ss_s16 Part index in SS
    short parentIndex;      // ss_s16
    short type;             // ss_s16
    short boundsType;       // ss_s16 Hit determination type
    short alphaBlendType;   // ss_s16
    short dummy;            // ss_s16
    String animRefName;     // ss_offset const char*　Animation name to be placed as an instance
    String effectFileName;  // ss_offset const char*　Name of the effect file to be referenced
    String colorLabel;      // ss_offset const char*
    
    final SRPackage _animPack;
    
    SRPart(ByteBuffer bb, int ptr, SRPackage animPack) {
        super(bb, ptr);
        this._animPack = animPack;
    }

    @Override
    protected int getData() {
        int namePtr = bb.getInt();
        index = bb.getShort();
        parentIndex = bb.getShort();
        type = bb.getShort();
        boundsType = bb.getShort();
        alphaBlendType = bb.getShort();
        dummy = bb.getShort();
        int refnamePtr = bb.getInt();
        int effectfilenamePtr = bb.getInt();
        int colorLabelPtr = bb.getInt();
        int end = bb.position();
        
        name = getString(namePtr);
        animRefName = getString(refnamePtr);
        effectFileName = getString(effectfilenamePtr);
        colorLabel = getString(colorLabelPtr);
        return end;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getParentIndex() {
        return parentIndex;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public String getInstanceReferenceName() {
        return animRefName;
    }

    @Override
    public SDPackage getAnimationPack() {
        return _animPack;
    }
}
