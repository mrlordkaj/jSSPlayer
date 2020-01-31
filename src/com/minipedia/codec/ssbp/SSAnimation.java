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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.minipedia.codec.ssbp.data.SDAnimation;
import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDFrame;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Thinh Pham
 */
public class SSAnimation {
    
    final SDAnimation animData;
    
    private boolean playing;
    private float speed = 1f;
    private final SSPlayer player;
    private long startTime;
    private int curFrame;
    private final int period;
    
    private final HashMap<String, SSPart> partMap;
    ArrayList<SSPart> renderParts = new ArrayList<>();
    
    SSAnimation(SDAnimation animData, HashMap<String, SSPart> partMap, SSPlayer player) {
        this.animData = animData;
        this.partMap = partMap;
        this.player = player;
        this.period = 1000 / animData.getFps();
    }
    
    void draw(SpriteBatch sb) {
        for (SSPart part : renderParts)
            part.draw(sb);
    }
    
    void setFrame(int frameNo) {
        renderParts = new ArrayList<>();
        for (int i = 0; i < animData.getPackage().getPartCount(); i++) {
            SDFrame frameData = animData.getFramePart(frameNo, i);
            SDPart partData = animData.getPackage().getPart(frameData.getPartIndex());
            SSPart part = partMap.get(SDPart.getAlias(partData));
            switch (partData.getType()) {
                case SDPart.PARTTYPE_NORMAL:
                    SSPartNormal norm = (SSPartNormal) part;
                    norm.frameData = frameData;
                    int cellIdx = frameData.getCellIndex();
                    if (cellIdx >= 0 && frameData.isVisible()) {
                        renderParts.add(part);
                        norm.cell = player.cells.get(cellIdx);
                    }
                    break;
                    
                case SDPart.PARTTYPE_INSTANCE:
                    renderParts.add(part);
                    // TODO: control instance here
                    SSPartInstance inst = (SSPartInstance) part;
                    break;
            }
            part.idt()
                .translate(frameData.getPositionX(), frameData.getPositionY(), frameData.getPositionZ())
                .rotate(Vector3.Z, frameData.getRotationZ())
                .scale(frameData.getScaleX(), frameData.getScaleY(), 1);
        }
        curFrame = frameNo;
    }
    
    void play() {
        startTime = System.currentTimeMillis();
        setFrame(0);
        playing = true;
    }
    
    void stop() {
        playing = false;
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    boolean nextFrame() {
        long curTime = System.currentTimeMillis();
        int totalFrame = (int)((curTime - startTime) / period * speed);
        int nextFrame = totalFrame % animData.getFrameCount();
        if (nextFrame != curFrame) {
            setFrame(nextFrame);
            return true;
        }
        return false;
    }
    
    void setSpeed(float speed) {
        this.speed = speed;
    }
    
    float getSpeed() {
        return speed;
    }
    
    public SSPlayer getPlayer() {
        return player;
    }
    
    @Override
    public String toString() {
        return SDAnimation.getReferenceName(animData);
    }
    
    public int getFrameCount() {
        return animData.getFrameCount();
    }
}
