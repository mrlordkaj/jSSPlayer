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

import javax.swing.ImageIcon;

/**
 *
 * @author Thinh Pham
 */
abstract class SSConfig {
    static final ImageIcon  ICO_PIC = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/picture.png")),
                            ICO_PIC_DIS = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/picture_disabled.png")),
                            ICO_PIC_EMP = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/picture_empty.png")),
                            ICO_PIC_EMP_DIS = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/picture_empty_disabled.png")),
                            ICO_ANIM = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/film.png")),
                            ICO_PKG = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/package.png")),
                            ICO_SSBP = new ImageIcon(SSConfig.class.getResource("/com/minipedia/codec/ssbp/img/control_play_blue.png"));
}
