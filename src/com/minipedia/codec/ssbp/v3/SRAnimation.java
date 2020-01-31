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
package com.minipedia.codec.ssbp.v3;

import com.minipedia.codec.ssbp.data.SDAnimation;
import com.minipedia.codec.ssbp.data.SDReader;
import java.nio.ByteBuffer;
import java.util.TreeMap;
import com.minipedia.codec.ssbp.data.SDFrame;
import com.minipedia.codec.ssbp.data.SDPackage;

/**
 *
 * @author Thinh Pham
 */
class SRAnimation extends SDReader implements SDAnimation {
    
    String                  name;           // ss_offset const char*
    SRInitial[]             initData;       // ss_offset const AnimationInitialData*
    SRFrame[][]             frameData;      // ss_offset const ss_s16*
    short[]                 userData;       // ss_offset const ss_s16* TODO
    TreeMap<String, Short>  labelData;      // ss_offset const ss_s16*
    short                   numFrames,      // ss_s16
                            fps,            // ss_s16
                            numLabels,      // ss_s16
                            canvasWidth,    // ss_s16 Reference frame width
                            canvasHeight;   // ss_s16 + ss_s16 dummy
    
    final SRPackage _animPack;
    
    SRAnimation(ByteBuffer bb, int ptr, SRPackage animPack) {
        super(bb, ptr);
        labelData = new TreeMap<>();
        _animPack = animPack;
    }

    @Override
    protected int getData() {
        int namePtr = bb.getInt();
        int initDataArrPtr = bb.getInt();
        int frameDataPtrPtr = bb.getInt();
        int userDataPtr = bb.getInt();
        int labelDataArrPtr = bb.getInt();
        numFrames = bb.getShort();
        fps = bb.getShort();
        numLabels = bb.getShort();
        canvasWidth = bb.getShort();
        canvasHeight = (short)bb.getInt();
        int end = bb.position();
        
        // name
        name = getString(namePtr);
        // initData
        initData = new SRInitial[_animPack.numParts];
        for (int i = 0; i < _animPack.numParts; i++) {
            initData[i] = new SRInitial(bb, initDataArrPtr);
            initDataArrPtr = initData[i].read();
        }
        // frameData
        int[] frameDataPtr = new int[numFrames];
        bb.position(frameDataPtrPtr);
        for (int i = 0; i < numFrames; i++) {
            frameDataPtr[i] = bb.getInt();
        }
        frameData = new SRFrame[numFrames][_animPack.numParts];
        for (int i = 0; i < numFrames; i++) {
            int framePartPtr = frameDataPtr[i];
            for (int j = 0; j < _animPack.numParts; j++) {
                SRFrame frame = frameData[i][j] = new SRFrame(bb, framePartPtr, this);
                framePartPtr = frame.read();
            }
        }
        // TODO: userData
        if (0 < userDataPtr/* && userDataPtr < bb.capacity()*/) {
            // https://github.com/SpriteStudio/ssbpLib/blob/1.2.3_SS5.6.1/Players/ssbpLib/SS5Player.cpp#L2984
            System.out.printf("TODO: Anim %1$s have custom userdata!\n", name);
        }
        // labelData
        int[] labelDataPtr = new int[numLabels];
        bb.position(labelDataArrPtr);
        for (int i = 0; i < numLabels; i++) {
            labelDataPtr[i] = bb.getInt();
        }
        for (int i = 0; i < numLabels; i++) {
            bb.position(labelDataPtr[i]);
            int labelPtr = bb.getInt();
            String label = getString(labelPtr);
            short frameIndex = bb.getShort();
            labelData.put(label, frameIndex);
        }
        return end;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getFrameCount() {
        return numFrames;
    }
    
    @Override
    public SDFrame getFramePart(int frameNo, int partNo) {
        return frameData[frameNo][partNo];
    }

    @Override
    public int getFps() {
        return fps;
    }
    
    @Override
    public int getCanvasWidth() {
        return canvasWidth;
    }

    @Override
    public int getCanvasHeight() {
        return canvasHeight;
    }

    @Override
    public SDPackage getPackage() {
        return _animPack;
    }
}
