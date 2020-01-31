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

import java.nio.ByteBuffer;

/**
 *
 * @author Thinh Pham
 */
public abstract class SDReader {
    
    protected final ByteBuffer bb;
    public final int ptr;
    
    protected SDReader(ByteBuffer bb, int ptr) {
        this.bb = bb;
        this.ptr = ptr;
    }
    
    public final int read() {
        bb.position(ptr);
        return getData();
    }
    
    /**
     * Rebuild this struct from data.
     * @return Pointer position after reading.
     */
    protected abstract int getData();
    
    protected String getString(int ptr) {
        if (0 < ptr && ptr < bb.capacity()) {
            StringBuilder sb = new StringBuilder();
            bb.position(ptr);
            byte b;
            while (bb.hasRemaining() && (b = bb.get()) != 0)
                sb.append((char)(b & 0xff));
            return sb.toString();
        } else {
            return null;
        }
    }
}
