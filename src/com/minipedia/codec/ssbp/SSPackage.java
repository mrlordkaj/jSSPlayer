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

import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDPackage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
class SSPackage extends SSNode {
    
    final SSPlayer player;
    final SDPackage packData;
    
    SSPackage(SDPackage packData, SSPlayer player) {
        super(packData.getName());
        this.packData = packData;
        this.player = player;
    }
    
    ArrayList<SSNode> generatePartTree(HashMap<String, SSPart> partMap) {
        if (partMap == null)
            partMap = new HashMap<>();
        ArrayList<SSNode> result = new ArrayList<>();
        for (int j = 0; j < packData.getPartCount(); j++) {
            SDPart partData = packData.getPart(j);
            int partType = partData.getType();
            SSPart part;
            switch (partType) {
                case SDPart.PARTTYPE_NULL:
                    part = new SSPart(partData);
                    break;
                    
                case SDPart.PARTTYPE_NORMAL:
                    part = new SSPartNormal(partData);
                    break;

                case SDPart.PARTTYPE_INSTANCE:
                    part = new SSPartInstance(partData, player);
                    break;

                default:
                    System.out.printf("Warning: Part type %1$d is not implemented.\n", partType);
                    continue;
            }
            partMap.put(SDPart.getAlias(partData), part);
            if (partData.getParentIndex() >= 0) {
                String parentRefName = SDPart.getParentAlias(partData);
                partMap.get(parentRefName).add(part);
            } else {
                result.add(part); // grand child
            }
        }
        return result;
    }
    
    @Override
    public ImageIcon getIcon() {
        return SSConfig.ICO_PKG;
    }
}
