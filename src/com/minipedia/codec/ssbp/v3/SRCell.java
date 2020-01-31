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
import com.minipedia.codec.ssbp.data.SDCell;
import com.minipedia.codec.ssbp.data.SDCellMap;
import com.minipedia.codec.ssbp.data.SDProject;

/**
 *
 * @author Thinh Pham
 */
class SRCell extends SDReader implements SDCell {
    
    String       name;            // ss_offset const char*
    SRCellMap  cellMap;         // ss_offset const CellMap*
    short        indexInCellMap;  // ss_s16
    short        rectX, rectY;    // ss_s16
    int          rectW, rectH;    // ss_u16
    short        reserved;        // ss_s16
    float        pivotX, pivotY;
    
    final SRProject _project;
    
    SRCell(ByteBuffer bb, int ptr, SRProject project) {
        super(bb, ptr);
        this._project = project;
    }

    @Override
    protected int getData() {
        int namePtr = bb.getInt(); // dummy?
        int cellMapPtr = bb.getInt();
        indexInCellMap = bb.getShort();
        rectX = bb.getShort();
        rectY = bb.getShort();
        rectW = bb.getShort() & 0xffff;
        rectH = bb.getShort() & 0xffff;
        reserved = bb.getShort();
        pivotX = bb.getFloat();
        pivotY = bb.getFloat();
        int end = bb.position();
        
        cellMap = _project.getCellMapAtPtr(cellMapPtr);
        return end;
    }

    @Override
    public SDCellMap getCellMap() {
        return cellMap;
    }

    @Override
    public int getRegionX() {
        return rectX;
    }

    @Override
    public int getRegionY() {
        return rectY;
    }

    @Override
    public int getRegionWidth() {
        return rectW;
    }

    @Override
    public int getRegionHeight() {
        return rectH;
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
    public SDProject getProjectData() {
        return _project;
    }
}
