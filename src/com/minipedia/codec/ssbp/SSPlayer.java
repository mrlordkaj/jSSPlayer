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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.minipedia.codec.ssbp.data.SDAnimation;
import com.minipedia.codec.ssbp.data.SDCell;
import com.minipedia.codec.ssbp.data.SDCellMap;
import com.minipedia.codec.ssbp.data.SDPackage;
import com.minipedia.codec.ssbp.v3.SRProject;
import com.minipedia.helper.FileHelper;
import com.minipedia.helper.GraphicHelper;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
public final class SSPlayer extends SSNode {
    
    // Data components
    final SRProject projectData;
    
    // Library components
    final HashMap<String, Texture> textureMap = new HashMap<>(); // referenceName, texture
    final HashMap<String, Dimension> textureSizeMap = new HashMap<>(); // for offscreen converter, when native library is not loaded
    final ArrayList<SSCell> cells = new ArrayList<>();
    private final HashMap<String, SSPart> partMap = new HashMap<>();
    private final ArrayList<SSAnimation> animations = new ArrayList<>();
    private final ArrayList<SSAnimation> playables = new ArrayList<>();
    
    private SSAnimation playingAnim;
    
    public SSPlayer(FileHandle file) throws IOException {
        super(file.name());
        // parse data
        ByteBuffer bb = ByteBuffer.wrap(file.readBytes())
                .order(ByteOrder.LITTLE_ENDIAN);
        projectData = new SRProject(bb);
        projectData.read();
        // prepare images
        for (int i = 0; i < projectData.getCellMapCount(); i++) {
            SDCellMap map = projectData.getCellMap(i);
            String imgPath = map.getImagePath();
            String absPath = FileHelper.convertToAbsolutePath(file.parent().path(), imgPath, "/");
            BufferedImage img = ImageIO.read(new File(absPath));
            try {
    //            Texture tex = new Texture(FileHelper.convertToAbsolutePath(file.parent().path(), imgPath, "/"));
                Pixmap pix = GraphicHelper.imageToPixmap(img);
                Texture tex = new Texture(pix);
                tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                textureMap.put(imgPath, tex);
            } catch (/*java.lang.UnsatisfiedLinkError | */NullPointerException ex) {
                // offscreen, when pixmap could not be generated
                textureSizeMap.put(imgPath, new Dimension(img.getWidth(), img.getHeight()));
            }
        }
        // prepare cells
        for (int i = 0; i < projectData.getCellCount(); i++) {
            SDCell cellData = projectData.getCell(i);
            Texture tex = textureMap.get(cellData.getCellMap().getImagePath());
            if (tex == null) {
                // offscreen, load empty cell instead
                Dimension siz = textureSizeMap.get(cellData.getCellMap().getImagePath());
                cells.add(new SSCell(cellData, siz));
            } else {
                cells.add(new SSCell(cellData, tex));
            }
        }
        for (int i = 0; i < projectData.getPackageCount(); i++) {
            // build parts tree
            SDPackage packData = projectData.getPackage(i);
            SSPackage packNode = new SSPackage(packData, this);
            ArrayList<SSNode> packNodes = packNode.generatePartTree(partMap);
            packNode.addAll(packNodes);
            super.add(packNode);
            // animations
            for (int j = 0; j < packData.getAnimationCount(); j++) {
                SDAnimation animData = packData.getAnimation(j);
                SSAnimation anim = new SSAnimation(animData, partMap, this);
                animations.add(anim);
                playables.add(anim);
            }
        }
        // auto play first animation
        if (animations.size() > 0)
            playAnimation(animations.get(0));
    }
    
    /* Drawing */
    public void draw(SpriteBatch sb) {
        if (playingAnim != null) {
            // schedule update
            boolean dirty = false;
            for (SSAnimation play : playables) {
                if (play.isPlaying() && play.nextFrame())
                    dirty = true;
            }
            if (dirty) update();
            // drawing
            playingAnim.draw(sb);
        }
    }
    
    public void drawBounding(ShapeRenderer sr, boolean drawCanvas) {
        for (SSPart part : partMap.values())
            part.drawBounding(sr);
        if (drawCanvas && playingAnim != null) {
            Vector3 scl = globalMat.getScale(new Vector3());
            float width = playingAnim.animData.getCanvasWidth() * scl.x;
            float height = playingAnim.animData.getCanvasHeight() * scl.y;
            sr.rect(width * -0.5f, 0, width, height);
        }
    }
    
    public void drawAxes(ShapeRenderer sr) {
        for (SSPart part : partMap.values())
            part.drawAxes(sr);
    }
    
    /* Control Playables */
    void addPlayable(SSAnimation play) {
        if (!playables.contains(play))
            playables.add(play);
    }
    
    public void playAnimation(String animRefName) {
        SSAnimation anim = getAnimation(animRefName);
        if (anim != null)
            playAnimation(anim);
    }
    
    public void playAnimation(SSAnimation anim) {
        if (playingAnim != null)
            playingAnim.stop();
        playingAnim = anim;
        anim.play();
        update();
    }
    
    public ArrayList<SSAnimation> getAnimations() {
        return animations;
    }
    
    public SSAnimation getAnimation(String animRefName) {
        for (SSAnimation anim : animations) {
            if (anim.toString().equals(animRefName))
                return anim;
        }
        return null;
    }
    
    public SSAnimation getPlayingAnimation() {
        return playingAnim;
    }
    
    /* User Interface */
    @Override
    public ImageIcon getIcon() {
        return SSConfig.ICO_SSBP;
    }
}
