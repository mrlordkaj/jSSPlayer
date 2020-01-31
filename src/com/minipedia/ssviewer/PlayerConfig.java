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

import com.badlogic.gdx.graphics.Color;
import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
public abstract class PlayerConfig {
    public static final ImageIcon ICO_PLAY = new ImageIcon(PlayerConfig.class.getResource("/com/minipedia/ssviewer/img/right-arrow.png"));
    
    public static final Color   COLOR_ABSTRACT = new Color(0x5778ccff),
                                COLOR_VIEWPORT_BG = new Color(0x393939ff);
                                
    public static final float   GRID_CELL_UNIT = 20;
    public static final Color   GRID_COLOR_X = new Color(0x841616ff),
                                GRID_COLOR_Y = new Color(0x168416ff),
                                GRID_COLOR = new Color(0x464646ff);
    
    public static boolean drawViewportGrid = true;
    public static boolean drawSpriteBounding = true;
    public static boolean drawSpriteAxes = true;
    public static boolean drawAnimationCanvas = false;
}
