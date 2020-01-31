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
public interface SDProject {
    
    public String getImageBaseDir();
    
    public int getCellCount();
    
    public SDCell getCell(int cellNo);
    
    public int getPackageCount();
    
    public SDPackage getPackage(int animPackNo);
    
    public int getCellMapCount();
    
    public SDCellMap getCellMap(int cellMapNo);
    
    public static SDAnimation findAnimation(String animRefName, SDProject project) {
        for (int i = 0; i < project.getPackageCount(); i++) {
            SDPackage animPack = project.getPackage(i);
            for (int j = 0; j < animPack.getAnimationCount(); j++) {
                SDAnimation anim = animPack.getAnimation(j);
                if (SDAnimation.getReferenceName(anim).equals(animRefName))
                    return anim;
            }
        }
        return null;
    }
}
