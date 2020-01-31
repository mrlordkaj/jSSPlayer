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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.minipedia.codec.ssbp.data.SDAnimation;
import com.minipedia.codec.ssbp.data.SDPackage;
import com.minipedia.codec.ssbp.data.SDPart;
import com.minipedia.codec.ssbp.data.SDProject;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
class SSPartInstance extends SSPart {
    
    private final SDAnimation animData;
    private final SSAnimation anim;
    
    private final HashMap<String, SSPart> partMap = new HashMap<>();
    
    SSPartInstance(SDPart partData, SSPlayer player) {
        super(partData);
        String animRefName = partData.getInstanceReferenceName();
        SDProject projectData = partData.getAnimationPack().getProject();
        animData = SDProject.findAnimation(animRefName, projectData);
        anim = new SSAnimation(animData, partMap, player);
        player.addPlayable(anim); // register playable on player
        
        // parse child
        SDPackage animPackData = animData.getPackage();
        SSPackage animPack = new SSPackage(animPackData, player);
        super.addAll(animPack.generatePartTree(partMap));
        
        anim.play(); // TODO: control this by part
    }
    
    @Override
    void draw(SpriteBatch sb) {
        anim.draw(sb);
    }
    
    @Override
    void drawBounding(ShapeRenderer sr) {
        for (SSPart part : partMap.values())
            part.drawBounding(sr);
    }
    
    @Override
    void drawAxes(ShapeRenderer sr) {
        super.drawAxes(sr);
        for (SSPart part : partMap.values())
            part.drawAxes(sr);
    }
    
    @Override
    public ImageIcon getIcon() {
        return SSConfig.ICO_ANIM;
    }
}
