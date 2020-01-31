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
package com.minipedia.helper;

import com.badlogic.gdx.graphics.Pixmap;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 *
 * @author Thinh Pham
 */
public abstract class GraphicHelper {
    
    public static Pixmap imageToPixmap(BufferedImage img) {
        BufferedImage src;
        if (img.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
            src = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            src.getGraphics().drawImage(img, 0, 0, null);
        } else {
            src = img;
        }
        byte[] srcDat = ((DataBufferByte)src.getRaster().getDataBuffer()).getData();
        IntBuffer srcBuf = ByteBuffer.wrap(srcDat).asIntBuffer();
        Pixmap pix = new Pixmap(src.getWidth(), src.getHeight(), Pixmap.Format.RGBA8888);
        ByteBuffer pixBuf = pix.getPixels().order(ByteOrder.LITTLE_ENDIAN);
        pixBuf.asIntBuffer().put(srcBuf);
        pixBuf.position(0);
        return pix;
    }
}
