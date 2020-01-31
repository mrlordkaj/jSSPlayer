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
import com.minipedia.codec.ssbp.data.SDProject;
import com.minipedia.codec.ssbp.data.SDCellMap;
import com.minipedia.codec.ssbp.data.SDCell;
import java.util.ArrayList;
import com.minipedia.codec.ssbp.data.SDPackage;

/**
 *
 * @author Thinh Pham
 */
public class SRProject extends SDReader implements SDProject {
    
    int         dataId;             // ss_u32
    int         version;            // ss_u32
    int         flags;              // ss_u32
    String      imageBaseDir;       // ss_offset const char*
    SRCell[]    cells;              // ss_offset const Cell*
    SRPackage[] animPacks;          // ss_offset const AnimePackData*
    int         effectFileList;     // ss_offset const EffectFileList* TODO
    short       numCells;           // ss_s16 
    short       numAnimPacks;       // ss_s16
    short       numEffectFileList;  // ss_s16
    
    final ArrayList<SRCellMap> cellMapList = new ArrayList<>();
    
    public SRProject(java.nio.ByteBuffer bb) {
        super(bb, 0);
    }
    
    @Override
    protected int getData() {
        dataId = bb.getInt();
        version = bb.getInt();
        flags = bb.getInt();
        int imageBaseDirPtr = bb.getInt();
        int cellListPtr = bb.getInt();
        int animePackListPtr = bb.getInt();
        effectFileList = bb.getInt();
        numCells = bb.getShort();
        numAnimPacks = bb.getShort();
        numEffectFileList = bb.getShort();
        int end = bb.position();
        // imageBaseDir
        if (imageBaseDirPtr > 0)
            imageBaseDir = getString(imageBaseDirPtr);
        // cells
        cells = new SRCell[numCells];
        for (int i = 0; i < numCells; i++) {
            SRCell cell = cells[i] = new SRCell(bb, cellListPtr, this);
            cellListPtr = cell.read();
        }
        // animePacks
        animPacks = new SRPackage[numAnimPacks];
        for (int i = 0; i < numAnimPacks; i++) {
            SRPackage pack = animPacks[i] = new SRPackage(bb, animePackListPtr, this);
            animePackListPtr = pack.read();
        }
        return end;
    }
    
    SRCellMap getCellMapAtPtr(int ptr) {
        for (SRCellMap map : cellMapList) {
            if (map.ptr == ptr)
                return map;
        }
        // no map found, create a new one
        SRCellMap map = new SRCellMap(bb, ptr);
        map.read();
        cellMapList.add(map);
        return map;
    }
    
    @Override
    public String getImageBaseDir() {
        return imageBaseDir;
    }

    @Override
    public int getCellCount() {
        return numCells;
    }

    @Override
    public SDCell getCell(int cellNo) {
        return cells[cellNo];
    }

    @Override
    public int getPackageCount() {
        return numAnimPacks;
    }

    @Override
    public SDPackage getPackage(int animPackNo) {
        return animPacks[animPackNo];
    }
    
    @Override
    public int getCellMapCount() {
        return cellMapList.size();
    }
    

    @Override
    public SDCellMap getCellMap(int cellMapNo) {
        return cellMapList.get(cellMapNo);
    }
}
