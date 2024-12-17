package com.texteditor.editor;

public class RopeNode {
    private RopeNode left , right;
    private String data;
    private int weight;

    public RopeNode(String data){
        this.data = data;
        this.weight = data.length();
        this.left = null;
        this.right = null;
    }

    public RopeNode(RopeNode left, RopeNode right){
        this.left = left;
        this.right = right;
        this.weight = (left != null) ? left.getTotalWeight(): 0;
        this.data = null;
    }

    public int getWeight(){
        return this.weight;
    }

    public RopeNode getLeftNode(){
        return this.left;
    }

    public RopeNode getRightNode(){
        return this.right;
    }

    public boolean isLeaf(){
        return this.left == null && this.right == null;
    }

    public int getTotalWeight(){
        if (isLeaf()) return data.length();
        return (this.left != null ? this.left.getTotalWeight() : 0) + (this.right != null ? this.right.getTotalWeight(): 0);
    }

    public String getData(){
        return this.data;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}





























