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

import com.badlogic.gdx.math.Matrix4;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.ImageIcon;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Thinh Pham
 */
public abstract class SSNode extends Matrix4 implements MutableTreeNode {
    
    final Matrix4 globalMat = new Matrix4();
    final ArrayList<SSNode> children = new ArrayList();
    SSNode parent;
    String name;
    Object userObject;
    
    // UI components
    boolean visible = true;
    boolean selected = false;
    
    SSNode(String name) {
        this.name = name;
    }
    
    SSNode() {
        name = String.format("%1$s@0x%2$08x", SSNode.class.getSimpleName(),
                super.hashCode());
    }
    
    void update() {
        // update transform
        if (parent == null) globalMat.set(this);
        else globalMat.set(parent.globalMat).mul(this);
        // recursive update
        for (SSNode child : children)
            child.update();
    }
    
    /* Tree Template */
    @Override
    public void insert(MutableTreeNode child, int index) throws IllegalStateException {
        if (child instanceof SSNode) {
            for (SSNode p = this; p != null; p = p.parent) {
                if (p == child)
                    throw new IllegalStateException("Cannot add a parent as a child.");
            }
            SSNode cast = (SSNode) child;
            // change child's parent
            cast.removeFromParent();
            cast.parent = this;
            // register to children list
            if (index < 0 || index > children.size())
                children.add(cast);
            else
                children.add(index, cast);
        } else {
            throw new IllegalStateException("Invalid node type.");
        }
    }
    
    public void add(SSNode child) {
        insert(child, -1);
    }
    
    public void addAll(ArrayList<SSNode> children) {
        for (SSNode child : children)
            insert(child, -1);
    }

    @Override
    public void remove(int index) {
        children.get(index).removeFromParent();
    }

    @Override
    public void remove(MutableTreeNode node) {
        if (node instanceof SSNode && this.children.contains((SSNode)node))
            node.removeFromParent();
    }

    @Override
    public void setUserObject(Object object) {
        userObject = object;
    }

    @Override
    public void removeFromParent() {
        if (parent != null) {
            parent.children.remove(this);
            parent = null;
        }
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
        newParent.insert(this, -1);
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }
    
    /* User Interface */
    @Override
    public String toString() {
        return name;
    }
    
    public void setVisible(boolean visible, boolean recursive) {
        this.visible = visible;
        if (recursive) {
            for (SSNode child : children)
                child.setVisible(visible, true);
        }
    }
    
    public void setVisible(boolean b) {
        visible = b;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public abstract ImageIcon getIcon();
    
    public SSPopupItem[] getPopupMenu() {
        return new SSPopupItem[0];
    }
}
