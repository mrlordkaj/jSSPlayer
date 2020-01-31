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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.NumberUtils;
import com.minipedia.codec.ssbp.data.SDCell;
import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDFrame;
import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
class SSPartNormal extends SSPart {
    
    private static final int VERTEX_SIZE = 2 + 1 + 2;
    private static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
    
    // Render components
    SSCell cell;
    
    // Data components
    final float[] vertices = new float[SPRITE_SIZE];
    SDFrame frameData;
    
    SSPartNormal(SDPart partData) {
        super(partData);
    }
    
    @Override
    void update() {
        super.update();
        // update sprite state
        if (frameData != null && cell != null) {
            computeVertices();
            computeColors();
            computeTexCoords();
        }
    }
    
    private void computeVertices() {
        Vector3 bl = new Vector3();  // bottom left
        Vector3 tl = new Vector3(0, frameData.getSizeY(), 0);  // top left
        Vector3 tr = new Vector3(frameData.getSizeX(), frameData.getSizeY(), 0);  // top right
        Vector3 br = new Vector3(frameData.getSizeX(), 0, 0);  // bottom right
        // vertex transform
        tl.x += frameData.getVertexTransform(SDFrame.VERTEX_LT, 0);
        tl.y += frameData.getVertexTransform(SDFrame.VERTEX_LT, 1);
        tr.x += frameData.getVertexTransform(SDFrame.VERTEX_RT, 0);
        tr.y += frameData.getVertexTransform(SDFrame.VERTEX_RT, 1);
        bl.x += frameData.getVertexTransform(SDFrame.VERTEX_LB, 0);
        bl.y += frameData.getVertexTransform(SDFrame.VERTEX_LB, 1);
        br.x += frameData.getVertexTransform(SDFrame.VERTEX_RB, 0);
        br.y += frameData.getVertexTransform(SDFrame.VERTEX_RB, 1);
        // align pivot to origin
        SDCell cellData = cell.cellData;
        float cpx = frameData.isFlipX() ? -cellData.getPivotX() : cellData.getPivotX();
        float cpy = frameData.isFlipY() ? -cellData.getPivotY() : cellData.getPivotY();
        float pvx = (frameData.getPivotX() + cpx + 0.5f) * frameData.getSizeX();
        float pvy = (1 - frameData.getPivotY() - cpy - 0.5f) * frameData.getSizeY();
        tl.sub(pvx, pvy, 0);
        tr.sub(pvx, pvy, 0);
        bl.sub(pvx, pvy, 0);
        br.sub(pvx, pvy, 0);
        // apply part transform
        bl.mul(globalMat);
        tl.mul(globalMat);
        tr.mul(globalMat);
        br.mul(globalMat);
        // send to batch
        vertices[Batch.X1] = bl.x;
        vertices[Batch.Y1] = bl.y;
        vertices[Batch.X2] = tl.x;
        vertices[Batch.Y2] = tl.y;
        vertices[Batch.X3] = tr.x;
        vertices[Batch.Y3] = tr.y;
        vertices[Batch.X4] = br.x;
        vertices[Batch.Y4] = br.y;
    }
    
    private void computeColors() {
        int intBits = (frameData.getOpacity() << 24) | 0xffffff;
        float color = NumberUtils.intToFloatColor(intBits);
        // TODO: color blend
        // send to batch
        vertices[Batch.C1] = color;
        vertices[Batch.C2] = color;
        vertices[Batch.C3] = color;
        vertices[Batch.C4] = color;
    }
    
    private void computeTexCoords() {
        Vector2 bl = new Vector2(); // bottom left
        Vector2 tl = new Vector2(); // top left
        Vector2 tr = new Vector2(); // top right
        Vector2 br = new Vector2(); // bottom right
        if (frameData.isFlipX()) {
            bl.x = tl.x = cell.br.x;
            br.x = tr.x = cell.bl.x;
        } else {
            bl.x = tl.x = cell.bl.x;
            br.x = tr.x = cell.br.x;
        }
        if (frameData.isFlipY()) {
            tl.y = tr.y = cell.bl.y;
            bl.y = br.y = cell.tl.y;
        } else {
            tl.y = tr.y = cell.tl.y;
            bl.y = br.y = cell.bl.y;
        }
        // TODO: uv alternative from there, coded but need a test
        // translate
        Vector2 uvMove = new Vector2(frameData.getUVMoveX(), frameData.getUVMoveY());
        bl.add(uvMove);
        tl.add(uvMove);
        tr.add(uvMove);
        br.add(uvMove);
        // calculate center position (percentage of 1)
        float pvu = (bl.x + br.x) * 0.5f;
        float pvv = (tl.y + bl.y) * 0.5f;
        bl.sub(pvu, pvv);
        tl.sub(pvu, pvv);
        tr.sub(pvu, pvv);
        br.sub(pvu, pvv);
        // scale
        Vector2 uvScale = new Vector2(frameData.getUVScaleX(), frameData.getUVScaleY());
        bl.scl(uvScale);
        tl.scl(uvScale);
        tr.scl(uvScale);
        br.scl(uvScale);
        // rotate
        float uvRotation = frameData.getUVRotation();
        bl.rotate(uvRotation);
        tl.rotate(uvRotation);
        tr.rotate(uvRotation);
        br.rotate(uvRotation);
        // restore pivot position
        bl.add(pvu, pvv);
        tl.add(pvu, pvv);
        tr.add(pvu, pvv);
        br.add(pvu, pvv);
        // send to batch
        vertices[Batch.U1] = bl.x;
        vertices[Batch.V1] = bl.y;
        vertices[Batch.U2] = tl.x;
        vertices[Batch.V2] = tl.y;
        vertices[Batch.U3] = tr.x;
        vertices[Batch.V3] = tr.y;
        vertices[Batch.U4] = br.x;
        vertices[Batch.V4] = br.y;
    }
    
    @Override
    void draw(SpriteBatch sb) {
        if (visible && cell != null)
            sb.draw(cell.texture, vertices, 0, SPRITE_SIZE);
    }
    
    @Override
    void drawBounding(ShapeRenderer sr) {
        if (selected) {
            sr.line(vertices[Batch.X1], vertices[Batch.Y1], vertices[Batch.X2], vertices[Batch.Y2]);
            sr.line(vertices[Batch.X2], vertices[Batch.Y2], vertices[Batch.X3], vertices[Batch.Y3]);
            sr.line(vertices[Batch.X3], vertices[Batch.Y3], vertices[Batch.X4], vertices[Batch.Y4]);
            sr.line(vertices[Batch.X4], vertices[Batch.Y4], vertices[Batch.X1], vertices[Batch.Y1]);
        }
    }
    
    @Override
    public ImageIcon getIcon() {
        return visible ? SSConfig.ICO_PIC : SSConfig.ICO_PIC_DIS;
    }
}
