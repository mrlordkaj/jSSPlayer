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
public interface SDPackage {
    
    public String getName();
    
    public int getPartCount();
    
    public SDPart getPart(int partNo);
    
    public int getAnimationCount();
    
    public SDAnimation getAnimation(int animNo);
    
    public SDProject getProject();
}
