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

import com.badlogic.gdx.Gdx;
import com.minipedia.codec.ssbp.SSAnimation;
import com.minipedia.codec.ssbp.SSNode;
import com.minipedia.codec.ssbp.SSPlayer;
import com.minipedia.codec.ssbp.SSPopupItem;
import com.minipedia.codec.ssbp.SSExport;
import com.minipedia.core.CoreConfig;
import com.minipedia.helper.FileHelper;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Thinh Pham
 */
public class Viewer extends javax.swing.JFrame {
    
    static {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            javax.swing.UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) { }
    }
    
    private static Viewer instance;
    public static Viewer getInstance() {
        if (instance == null) {
            instance = new Viewer();
            CoreConfig.mainWindow = instance;
        }
        return instance;
    }
    
    private SSPlayer player;
    private AnimationModel animModel;
    private File curFile;
    
    private Viewer() {
        initComponents();
        refineComponents();
        viewport.init();
    }
    
    private void refineComponents() {
        animModel = (AnimationModel) animTable.getModel();
        TableColumnModel tcm = animTable.getTableHeader().getColumnModel();
        tcm.getColumn(0).setMaxWidth(28);
        nodeTree.setCellRenderer(new javax.swing.tree.DefaultTreeCellRenderer() {
            @Override
            public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                SSNode node = (SSNode)value;
                setIcon(node.getIcon());
                if (row > 0 && !node.isVisible())
                    setForeground(sel ? Color.LIGHT_GRAY : Color.GRAY);
                return this;
            }
        });
        nodeTree.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                if (e.getButton() == MouseEvent.BUTTON3) {
//                    int row = nodeTree.getRowForLocation(e.getX(), e.getY());
//                    if (row >= 0)
//                        nodeTree.setSelectionRow(row);
//                }
//            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && nodeTree.getSelectionCount() == 1) {
                    TreePath path = nodeTree.getSelectionPath();
                    if (path != null) {
                        SSNode node = (SSNode) path.getLastPathComponent();
                        SSPopupItem[] pop = node.getPopupMenu();
                        if (pop.length > 0) {
                            JPopupMenu menu = new JPopupMenu();
                            for (SSPopupItem i : pop) {
                                if (i == null) {
                                    menu.add(new JPopupMenu.Separator());
                                } else {
                                    JMenuItem mnu = new JMenuItem(i.getTitle());
                                    mnu.addActionListener(i);
                                    menu.add(mnu);
                                }
                            }
                            menu.show(nodeTree, e.getX(), e.getY());
                        }
                    }
                    e.consume();
                }
            }
        });
        nodeTree.addTreeSelectionListener((TreeSelectionEvent e) -> {
            for (TreePath path : e.getPaths()) {
                SSNode node = (SSNode) path.getLastPathComponent();
                node.setSelected(e.isAddedPath(path));
            }
        });
        viewport.addViewportListener(new ViewportListener() {
            @Override
            public void onViewportRendered(int deltaTime) {
                mainMenu.revalidate();
            }
            @Override
            public void onViewportResized(int width, int height) {
            }
        });
        animTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isConsumed())
                    return;
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2
                        && animTable.getSelectedRowCount() == 1) {
                    int row = animTable.getSelectedRow();
                    AnimationModel model = (AnimationModel) animTable.getModel();
                    SSAnimation anim = model.getAnimation(row);
                    anim.getPlayer().playAnimation(anim);
                    animTable.repaint();
                    e.consume();
                }
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainSplit = new javax.swing.JSplitPane();
        viewport = new com.minipedia.ssviewer.Viewport();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        nodeTree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        animTable = new javax.swing.JTable();
        mainMenu = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuOpen = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuWebGL = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnuToDo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mnuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sprite Studio 5 Player");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(712, 512));

        mainSplit.setResizeWeight(1.0);

        viewport.setMinimumSize(new java.awt.Dimension(480, 0));
        viewport.setLayout(new java.awt.BorderLayout());
        mainSplit.setLeftComponent(viewport);

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.35);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(200, 57));

        jScrollPane1.setBorder(null);
        jScrollPane1.setMinimumSize(new java.awt.Dimension(23, 140));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(74, 200));

        nodeTree.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        nodeTree.setModel(null);
        nodeTree.setToggleClickCount(0);
        jScrollPane1.setViewportView(nodeTree);

        jSplitPane1.setBottomComponent(jScrollPane1);

        jScrollPane2.setBorder(null);
        jScrollPane2.setMinimumSize(new java.awt.Dimension(23, 140));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(452, 200));

        animTable.setModel(new AnimationModel());
        animTable.setGridColor(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        animTable.setRowHeight(20);
        animTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        animTable.setShowHorizontalLines(false);
        animTable.setShowVerticalLines(false);
        animTable.getTableHeader().setResizingAllowed(false);
        animTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(animTable);

        jSplitPane1.setLeftComponent(jScrollPane2);

        mainSplit.setRightComponent(jSplitPane1);

        getContentPane().add(mainSplit, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");

        mnuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mnuOpen.setText("Open...");
        mnuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOpenActionPerformed(evt);
            }
        });
        jMenu1.add(mnuOpen);
        jMenu1.add(jSeparator1);

        mnuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExit);

        mainMenu.add(jMenu1);

        jMenu2.setText("Tools");

        mnuWebGL.setText("WebGL Exporter");
        mnuWebGL.setEnabled(false);
        mnuWebGL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuWebGLActionPerformed(evt);
            }
        });
        jMenu2.add(mnuWebGL);

        mainMenu.add(jMenu2);

        jMenu3.setText("Help");

        mnuToDo.setText("To-Do List");
        jMenu3.add(mnuToDo);
        jMenu3.add(jSeparator2);

        mnuAbout.setText("About");
        jMenu3.add(mnuAbout);

        mainMenu.add(jMenu3);

        setJMenuBar(mainMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOpenActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle("Open Sprite Studio Binary");
        fc.setFileFilter(new FileNameExtensionFilter("Sprite Studio Binary Project (*.ssbp)", "ssbp"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            openSSBP(fc.getSelectedFile());
    }//GEN-LAST:event_mnuOpenActionPerformed

    private void mnuWebGLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuWebGLActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setDialogTitle("Save Sprite Studio Json");
        fc.setFileFilter(new FileNameExtensionFilter("Minipedia Sprite Player for WebGL (*.json)", "json"));
        fc.setSelectedFile(FileHelper.changeExtension(curFile, "json"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                SSExport exp = new SSExport(player);
                exp.toWebGL(fc.getSelectedFile());
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }//GEN-LAST:event_mnuWebGLActionPerformed

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        dispose();
    }//GEN-LAST:event_mnuExitActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            Viewer viewer = Viewer.getInstance();
            viewer.setVisible(true);
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-o":
                    case "--open":
                        viewer.openSSBP(new File(args[++i]));
                        break;
                        
                    default:
                        System.err.printf("Undefined command '%1$s'. Type '-help' for more information.\n", args[i]);
                        break;
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable animTable;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JSplitPane mainSplit;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenuItem mnuOpen;
    private javax.swing.JMenuItem mnuToDo;
    private javax.swing.JMenuItem mnuWebGL;
    private javax.swing.JTree nodeTree;
    private com.minipedia.ssviewer.Viewport viewport;
    // End of variables declaration//GEN-END:variables
    
    SSPlayer openSSBP(File in) {
        try {
            player = new SSPlayer(Gdx.files.absolute(in.getAbsolutePath()));
            viewport.setPlayer(player);
            nodeTree.setModel(new DefaultTreeModel(player));
            animModel.bind(player);
            mnuWebGL.setEnabled(true);
            curFile = in;
//            player.scale(2f, 2f, 1);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return player;
    }
}
