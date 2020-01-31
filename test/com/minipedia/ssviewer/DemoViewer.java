/*
 * Copyright 2020 Thinh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.minipedia.ssviewer;

import com.minipedia.codec.ssbp.SSExport;
import com.minipedia.codec.ssbp.SSPlayer;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Thinh
 */
public class DemoViewer {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            Viewer viewer = Viewer.getInstance();
            viewer.setVisible(true);
            try {
                SSPlayer player = viewer.openSSBP(new File("demo/Unit/ch05_28_Camilla_F_ELECTION01/ch05_28_Camilla_F_ELECTION01.ssbp"));
                SSExport exp = new SSExport(player);
                exp.toWebGL(null);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        });
    }
}
