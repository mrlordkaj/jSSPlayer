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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.minipedia.codec.ssbp.data.SDPart;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
public class SSPart extends SSNode {
    
    private final Vector2 ao = new Vector2(); // axes
    private final Vector2 ax = new Vector2();
    private final Vector2 ay = new Vector2();
    
    // Data components
    final SDPart partData;
    
    SSPart(SDPart partData) {
        super(partData.getName());
        this.partData = partData;
    }
    
    @Override
    void update() {
        super.update();
        // update axes state
        ao.set(globalMat.val[M03], globalMat.val[M13]);
        ax.set(globalMat.val[M00], globalMat.val[M10]).nor().scl(60).add(ao);
        ay.set(globalMat.val[M01], globalMat.val[M11]).nor().scl(60).add(ao);
    }
    
    void draw(SpriteBatch sb) {
    
    };
    
    void drawBounding(ShapeRenderer sr) {
    
    };
    
    void drawAxes(ShapeRenderer sr) {
        if (selected) {
            sr.setColor(Color.RED);
            sr.line(ao.x, ao.y, ax.x, ax.y);
            sr.setColor(Color.GREEN);
            sr.line(ao.x, ao.y, ay.x, ay.y);
        }
    }
    
    @Override
    public ImageIcon getIcon() {
        return visible ? SSConfig.ICO_PIC_EMP : SSConfig.ICO_PIC_EMP_DIS;
    }
    
    @Override
    public SSPopupItem[] getPopupMenu() {
        SSPopupItem mnuVisible = new SSPopupItem(visible ? "Hide this node" : "Show this node") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(!visible);
                ((Component) e.getSource()).repaint();
            }
        };
        SSPopupItem mnuVisibleRecursive = new SSPopupItem(visible ? "Hide nodes recursively" : "Show nodes recursively") {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(!visible, true);
                ((Component) e.getSource()).repaint();
            }
        };
        return new SSPopupItem[] {
            mnuVisible,
            mnuVisibleRecursive,
        };
    }
}
