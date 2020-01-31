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

/**
 *
 * @author Thinh Pham
 */
public abstract class StringHelper {
    
    public static String formatFloat(float value, int limit) {
        String full = String.format("%1$."+limit+"f", value);
        String trim = full.replaceAll("\\.?0+$", "");
        return "-0".equals(trim) ? "0" : trim;
    }
}
