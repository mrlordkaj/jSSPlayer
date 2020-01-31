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
import com.minipedia.codec.ssbp.data.SDReader;
import java.nio.ByteBuffer;
import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDProject;
import com.minipedia.codec.ssbp.data.SDPackage;

/**
 *
 * @author Thinh Pham
 */
class SRPackage extends SDReader implements SDPackage {
    
    String name;            // ss_offset const char*
    SRPart[] parts;       // ss_offset const PartData*
    SRAnimation[] anims;  // ss_offset const AnimationData*
    short numParts;         // ss_s16
    short numAnims;         // ss_s16
    
    final SRProject _project;
    
    SRPackage(ByteBuffer bb, int ptr, SRProject project) {
        super(bb, ptr);
        this._project = project;
    }
    
    @Override
    protected int getData() {
        int namePtr = bb.getInt();
        int partListPtr = bb.getInt();
        int animationListPtr = bb.getInt();
        numParts = bb.getShort();
        numAnims = bb.getShort();
        int end = bb.position();
        
        name = getString(namePtr);
        parts = new SRPart[numParts];
        for (int i = 0; i < numParts; i++) {
            SRPart part = new SRPart(bb, partListPtr, this);
            partListPtr = part.read();
            parts[part.index] = part;
        }
        anims = new SRAnimation[numAnims];
        for (int i = 0; i < numAnims; i++) {
            SRAnimation anim = anims[i] = new SRAnimation(bb, animationListPtr, this);
            animationListPtr = anim.read();
        }
        return end;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPartCount() {
        return numParts;
    }

    @Override
    public SDPart getPart(int partNo) {
        return parts[partNo];
    }

    @Override
    public int getAnimationCount() {
        return numAnims;
    }

    @Override
    public SDAnimation getAnimation(int animNo) {
        return anims[animNo];
    }

    @Override
    public SDProject getProject() {
        return _project;
    }
}
