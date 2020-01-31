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

import com.minipedia.codec.ssbp.SSAnimation;
import com.minipedia.codec.ssbp.SSPlayer;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Thinh Pham
 */
public class AnimationModel extends AbstractTableModel {

    static final String[] COLUMNS = new String[] { "", "Name" };
    static final int    COL_STATUS = 0,
                        COL_NAME = 1;
    
    ArrayList<SSAnimation> animations = new ArrayList<>();
    
    public void bind(SSPlayer player) {
        animations = player.getAnimations();
        this.fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return animations.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMNS[col];
    }

    @Override
    public Class<?> getColumnClass(int col) {
        switch (col) {
            case COL_STATUS:
                return ImageIcon.class;
                
            case COL_NAME:
                return String.class;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public Object getValueAt(int row, int col) {
        SSAnimation anim = animations.get(row);
        switch (col) {
            case COL_STATUS:
                return anim.isPlaying() ? PlayerConfig.ICO_PLAY : null;
                
            case COL_NAME:
                return anim.toString();
        }
        return null;
    }
    
    public SSAnimation getAnimation(int i) {
        return animations.get(i);
    }
}
