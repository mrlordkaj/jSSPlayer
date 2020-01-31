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
import com.minipedia.codec.ssbp.data.SDCellMap;
import java.nio.ByteBuffer;

/**
 *
 * @author Thinh Pham
 */
class SRCellMap extends SDReader implements SDCellMap {
    
    String  name;       // ss_offset const char*
    String  imagePath;  // ss_offset const char*
    short   index;      // ss_s16
    short   wrapMode;   // ss_s16
    short   filterMode; // ss_s16
    short   reserved;   // ss_s16
    
    SRCellMap(ByteBuffer bb, int ptr) {
        super(bb, ptr);
    }

    @Override
    protected int getData() {
        int nameOffset = bb.getInt();
        int imagePathOffset = bb.getInt();
        index = bb.getShort();
        wrapMode = bb.getShort();
        filterMode = bb.getShort();
        reserved = bb.getShort();
        int end = bb.position();
        
        name = getString(nameOffset);
        imagePath = getString(imagePathOffset);
        return end;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
