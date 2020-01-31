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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.minipedia.codec.ssbp.data.SDAnimation;
import com.minipedia.codec.ssbp.data.SDPackage;
import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDProject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Thinh Pham
 */
public class SSExport {
    
    private final SSPlayer player;
    
    public SSExport(SSPlayer player) {
        this.player = player;
    }
    
    public void toWebGL(File out) throws IOException {
        JSONObject playerObj = new JSONObject();
        SDProject projectData = player.projectData;
        // image list
        JSONArray texArr = new JSONArray();
        playerObj.put("tex", texArr);
        for (int i = 0; i < projectData.getCellMapCount(); i++)
            texArr.put(projectData.getCellMap(i).getImagePath());
        // sprite list
        JSONArray imgArr = new JSONArray();
        playerObj.put("pie", imgArr);
        for (int i = 0; i < player.cells.size(); i++) {
            SSCell cell = player.cells.get(i);
            int texIdx = cell.cellData.getCellMap().getIndex();
            JSONArray cellDataArr = new JSONArray();
            imgArr.put(cellDataArr);
            cellDataArr.put(ff(cell.bl.x, 4));
            cellDataArr.put(ff(cell.bl.y, 4));
            cellDataArr.put(ff(cell.tl.x, 4));
            cellDataArr.put(ff(cell.tl.y, 4));
            cellDataArr.put(ff(cell.tr.x, 4));
            cellDataArr.put(ff(cell.tr.y, 4));
            cellDataArr.put(ff(cell.br.x, 4));
            cellDataArr.put(ff(cell.br.y, 4));
            cellDataArr.put(texIdx);
        }
        // prepare animation reference map
        HashMap<String, Integer> animMap = new HashMap<>();
        for (int i = 0; i < projectData.getPackageCount(); i++) {
            SDPackage packData = projectData.getPackage(i);
            for (int j = 0; j < packData.getAnimationCount(); j++) {
                String ref = SDAnimation.getReferenceName(packData.getAnimation(j));
                animMap.put(ref, animMap.size());
            }
        }
        // drawable part list
        HashMap<String, Integer> partMap = new HashMap<>();
        JSONArray partListArr = new JSONArray();
        playerObj.put("pat", partListArr);
        for (int i = 0; i < projectData.getPackageCount(); i++) {
            SDPackage packData = projectData.getPackage(i);
            for (int j = 0; j < packData.getPartCount(); j++) {
                SDPart partData = packData.getPart(j);
                int type = partData.getType();
                if (type > 0) {
                    JSONObject partDataObj = new JSONObject();
                    partListArr.put(partDataObj);
                    //String name = packData.getName() + "/" + partData.getName();
                    String partRef = SDPart.getReferenceName(partData);
                    int idx = partMap.size();
                    partMap.put(partRef, idx);
                    partDataObj.put("ref", partRef);
                    partDataObj.put("typ", type);
                    if (type == SDPart.PARTTYPE_INSTANCE) {
                        String instRef = partData.getInstanceReferenceName();
                        int animIdx = animMap.get(instRef);
                        partDataObj.put("ani", animIdx);
                    }
                }
            }
        }
        // animation list
        JSONArray animArr = new JSONArray();
        playerObj.put("ani", animArr);
        for (int i = 0; i < projectData.getPackageCount(); i++) {
            SDPackage packData = projectData.getPackage(i);
            for (int j = 0; j < packData.getAnimationCount(); j++) {
                SDAnimation animData = packData.getAnimation(j);
                String animRef = SDAnimation.getReferenceName(animData);
                SSAnimation anim = player.getAnimation(animRef);
                JSONObject animDataObj = new JSONObject();
                animArr.put(animDataObj);
                animDataObj.put("ref", animRef);
                animDataObj.put("fps", animData.getFps());
                // part list
                JSONArray partDataArr = new JSONArray();
                animDataObj.put("pat", partDataArr);
                for (int k = 0; k < packData.getPartCount(); k++) {
                    SDPart partData = packData.getPart(k);
                    String partRef = SDPart.getReferenceName(partData);
                    Integer partIdx = partMap.get(partRef);
                    if (partIdx != null)
                        partDataArr.put(partIdx);
                }
                // frame list
                JSONArray frameArr = new JSONArray();
                animDataObj.put("fra", frameArr);
                for (int k = 0; k < anim.getFrameCount(); k++) {
                    anim.setFrame(k);
                    player.update();
                    JSONArray framePartArr = new JSONArray();
                    frameArr.put(framePartArr);
                    for (SSPart part : anim.renderParts) {
                        SDPart partData = part.partData;
                        String refName = SDPart.getReferenceName(partData);
                        JSONArray framePartDataArr = new JSONArray();
                        framePartArr.put(framePartDataArr);
                        framePartDataArr.put(partMap.get(refName)); // part index
                        switch (part.partData.getType()) {
                            case SDPart.PARTTYPE_NORMAL:
                                // partId, cellId,
                                // blx, bly, tlx, tly, trx, try, brx, bry,
                                // blc, tlc, trc, brc,
                                // bluvx, bluvy, tluvx, tluvy, truvx, truvy, bruvx, bruvy
                                SSPartNormal norm = (SSPartNormal) part;
                                float[] vts = norm.vertices;
                                int cellIdx = norm.frameData.getCellIndex();
                                framePartDataArr.put(cellIdx); // cell index
                                framePartDataArr.put(ff(vts[Batch.X1], 1)); // bottom left x
                                framePartDataArr.put(ff(vts[Batch.Y1], 1)); // bottom left y
                                framePartDataArr.put(ff(vts[Batch.X2], 1)); // top left x
                                framePartDataArr.put(ff(vts[Batch.Y2], 1)); // top left y
                                framePartDataArr.put(ff(vts[Batch.X3], 1)); // top right x
                                framePartDataArr.put(ff(vts[Batch.Y3], 1)); // top right y
                                framePartDataArr.put(ff(vts[Batch.X4], 1)); // bottom right x
                                framePartDataArr.put(ff(vts[Batch.Y4], 1)); // bottom right y
                                framePartDataArr.put(Float.floatToIntBits(vts[Batch.C1])); // bottom left color
                                int flipFlags = 0;
                                if (norm.frameData.isFlipX()) flipFlags |= 0b01;
                                if (norm.frameData.isFlipY()) flipFlags |= 0b10;
                                framePartDataArr.put(flipFlags);
                                break;
                                
                            case SDPart.PARTTYPE_INSTANCE:
                                // partId, trnX, trnY, rotZ, sclX, sclY
                                String instRef = partData.getInstanceReferenceName();
                                if (animMap.containsKey(instRef)) {
                                    Matrix4 g = part.globalMat;
                                    Vector3 trn = g.getTranslation(new Vector3());
                                    float rot = g.getRotation(new Quaternion()).getAngleAround(Vector3.Z);
                                    float[] m = part.globalMat.val;
                                    // z components of cross product
                                    float cz = m[0] * m[5] - m[1] * m[4];
                                    float sclX = (cz < 0) ? -g.getScaleX() : g.getScaleX();
                                    float sclY = g.getScaleY();
                                    framePartDataArr.put(ff(trn.x, 1)); // translate x
                                    framePartDataArr.put(ff(trn.y, 1)); // translate y
                                    framePartDataArr.put(ff(rot, 1)); // rotate z
                                    framePartDataArr.put(ff(sclX, 2)); // scale x
                                    framePartDataArr.put(ff(sclY, 2)); // scale y
                                }
                                break;
                        }
                    }
                }
            }
        }
        
        if (out != null) {
            try (FileWriter fw = new FileWriter(out, false)) {
                fw.write(playerObj.toString());
            }
        } else {
            System.out.println(playerObj.toString());
        }
    }
    
    private Number ff(float value, int limit) {
        String str = String.format("%1$."+limit+"f", value);
        Float flt = Float.parseFloat(str);
        Integer rnd = Math.round(flt);
        if (Math.abs(flt - rnd) > 0.00001f)
            return flt;
        else
            return rnd;
    }
}
