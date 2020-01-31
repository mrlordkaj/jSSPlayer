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
package com.minipedia.ssviewer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.minipedia.codec.ssbp.SSPlayer;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Thinh Pham
 */
public class Viewport extends JPanel implements
        ApplicationListener {
    
    long prevTime;
    final ArrayList<ViewportListener> viewportListeners = new ArrayList<>();

    ShapeRenderer sr;
    SpriteBatch sb;
    SSPlayer player;
    
    public void init() {
        LwjglApplicationConfiguration.disableAudio = true;
        LwjglCanvas canvas = new LwjglCanvas(this);
        super.setLayout(new java.awt.BorderLayout());
        super.add(canvas.getCanvas());
    }
    
    public void setPlayer(SSPlayer player) {
        this.player = player;
    }
        
    @Override
    public void create() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        Color bg = PlayerConfig.COLOR_VIEWPORT_BG;
        GL11.glClearColor(bg.r, bg.g, bg.b, bg.a);
        prevTime = System.currentTimeMillis();
    }

    @Override
    public void render() {
        // delta time
        long curTime = System.currentTimeMillis();
        int deltaTime = (int) (curTime - prevTime);
        prevTime = curTime;
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT/* | GL11.GL_DEPTH_BUFFER_BIT
                /* | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0)*/);
        
        if (PlayerConfig.drawViewportGrid) {
            GL11.glLineWidth(1);
            float halfWidth = Gdx.graphics.getWidth() * 0.5f;
            float halfHeight = Gdx.graphics.getHeight() * 0.5f;
            sr.begin(ShapeRenderer.ShapeType.Line);
            float cu = PlayerConfig.GRID_CELL_UNIT;
            sr.setColor(PlayerConfig.GRID_COLOR);
            for (float x = -cu; x > -halfWidth; x -= cu)
                sr.line(x, -halfHeight, x, halfHeight);
            for (float x = cu; x < halfWidth; x += cu)
                sr.line(x, -halfHeight, x, halfHeight);
            for (float y = -cu; y > -halfHeight; y -= cu)
                sr.line(-halfWidth, y, halfWidth, y);
            for (float y = cu; y < halfHeight; y += cu)
                sr.line(-halfWidth, y, halfWidth, y);
            sr.setColor(PlayerConfig.GRID_COLOR_X);
            sr.line(-halfWidth, 0, halfWidth, 0);
            sr.setColor(PlayerConfig.GRID_COLOR_Y);
            sr.line(0, -halfHeight, 0, halfHeight);
            sr.end();
        }
        
        if (player != null) {
            sb.begin();
            player.draw(sb);
            sb.end();
            if (PlayerConfig.drawSpriteBounding) {
                GL11.glLineWidth(1);
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.WHITE);
                player.drawBounding(sr, PlayerConfig.drawAnimationCanvas);
                sr.end();
            }
            if (PlayerConfig.drawSpriteAxes) {
                GL11.glLineWidth(2);
                sr.begin(ShapeRenderer.ShapeType.Line);
                player.drawAxes(sr);
                sr.end();
            }
        }
        
        for (ViewportListener l : viewportListeners)
            l.onViewportRendered(deltaTime);
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }
    
    @Override
    public void resize(int width, int height) {
        Matrix4 prjMat = new Matrix4().setToOrtho2D(width * -0.5f, height * -0.5f, width, height);
        sb.setProjectionMatrix(prjMat);
        sr.setProjectionMatrix(prjMat);
        for (ViewportListener l : viewportListeners)
            l.onViewportResized(width, height);
    }
    
    @Override
    public void dispose() {
        sb.dispose();
        sr.dispose();
    }
    
    public void addViewportListener(ViewportListener l) {
        if (!viewportListeners.contains(l))
            viewportListeners.add(l);
    }
    
    public void removeViewportListener(ViewportListener l) {
        viewportListeners.remove(l);
    }
}
