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
package com.minipedia.codec.ssbp;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.minipedia.codec.ssbp.data.SDCell;
import java.awt.Dimension;

/**
 *
 * @author Thinh Pham
 */
public class SSCell {
    
    final SDCell cellData;
    final Texture texture;
    final Vector2 bl;
    final Vector2 tl;
    final Vector2 tr;
    final Vector2 br;
    
    public SSCell(SDCell cellData, Texture texture) {
        this.cellData = cellData;
        this.texture = texture;
        float invTexWidth = 1f / texture.getWidth();
        float invTexHeight = 1f / texture.getHeight();
        float u1 = cellData.getRegionX() * invTexWidth;                                 // left
        float v1 = cellData.getRegionY() * invTexHeight;                                // top
        float u2 = (cellData.getRegionX() + cellData.getRegionWidth()) * invTexWidth;   // right
        float v2 = (cellData.getRegionY() + cellData.getRegionHeight()) * invTexHeight; // bottom
        bl = new Vector2(u1, v2);
        tl = new Vector2(u1, v1);
        tr = new Vector2(u2, v1);
        br = new Vector2(u2, v2);
    }
    
    public SSCell(SDCell cellData, Dimension textureSize) {
        this.cellData = cellData;
        this.texture = null;
        float invTexWidth = 1f / textureSize.width;
        float invTexHeight = 1f / textureSize.height;
        float u1 = cellData.getRegionX() * invTexWidth;                                 // left
        float v1 = cellData.getRegionY() * invTexHeight;                                // top
        float u2 = (cellData.getRegionX() + cellData.getRegionWidth()) * invTexWidth;   // right
        float v2 = (cellData.getRegionY() + cellData.getRegionHeight()) * invTexHeight; // bottom
        bl = new Vector2(u1, v2);
        tl = new Vector2(u1, v1);
        tr = new Vector2(u2, v1);
        br = new Vector2(u2, v2);
    }
}
