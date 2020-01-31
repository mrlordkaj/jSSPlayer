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

import java.io.File;

/**
 *
 * @author Thinh Pham
 */
public abstract class FileHelper {
    
    public static String convertToAbsolutePath(String baseDirectory, String relativePath, String separator) {
        StringBuilder sb = new StringBuilder(baseDirectory);
        String[] relParts = relativePath.split("\\" + separator);
        for (String part : relParts) {
            if (part.equals("..")) {
                int idx = sb.lastIndexOf(separator);
                sb.delete(idx, sb.length());
            } else {
                sb.append(separator).append(part);
            }
        }
        return sb.toString();
    }
    
    public static String convertToAbsolutePath(String baseDirectory, String relativePath) {
        return convertToAbsolutePath(baseDirectory, relativePath, File.separator);
    }
    
    public static File changeExtension(File file, String newExt) {
        String path = file.getAbsolutePath();
        int idx = path.lastIndexOf('.');
        path = path.substring(0, idx).concat(".").concat(newExt);
        return new File(path);
    }
}
