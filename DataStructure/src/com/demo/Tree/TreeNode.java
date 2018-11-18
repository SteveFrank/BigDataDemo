package com.demo.Tree;

/**
 * Created by qian on 2017/4/11.
 */
public class TreeNode {
    private int data;
    private int parent;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "TreeNode{" + "data=" + data + ", parent=" + parent + '}';
    }
}
