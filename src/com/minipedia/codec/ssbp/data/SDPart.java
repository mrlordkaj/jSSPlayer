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
public interface SDPart {
    
    public static final int PARTTYPE_NULL       = 0,    // It has no area and only SRT information. However, circular hit determination can be set.
                            PARTTYPE_NORMAL     = 1,    // Usually parts. Having a region. There is no need for images.
                            PARTTYPE_TEXT       = 2,    // Text (Reserved not yet implemented).
                            PARTTYPE_INSTANCE   = 3,    // Instance. Reference to other animation, parts. An alternative to the scene edit mode.
                            PARTTYPE_EFFECT     = 4,    // ss5.5 Corresponding effect parts.
                            PARTTYPE_NUM        = 5;
    
    public String getName();
    
    public int getIndex();
    
    public int getParentIndex();
    
    public int getType();
    
    public String getInstanceReferenceName();
    
    public SDPackage getAnimationPack();
    
    public static String getAlias(SDPart part) {
        return String.format("%1$s[%2$d]", part.getAnimationPack().getName(), part.getIndex());
    }
    
    public static String getParentAlias(SDPart part) {
        return String.format("%1$s[%2$d]", part.getAnimationPack().getName(), part.getParentIndex());
    }
    
    public static String getReferenceName(SDPart part) {
        return String.format("%1$s/%2$s", part.getAnimationPack().getName(), part.getName());
    }
}
