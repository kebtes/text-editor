package com.texteditor.editor;

public class RopeNode {
    private RopeNode left;
    private RopeNode right;
    private String data;
    private int weight;

    public RopeNode(String data){
        this.data = data != null ? data : "";
        this.weight = this.data.length();
    }

    public RopeNode(RopeNode left, RopeNode right){
        this.left = left;
        this.right = right;
        this.weight = (left != null) ? left.getTotalWeight() : 0;
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
        return left == null && right == null;
    }

    public int getTotalWeight(){
        if (isLeaf()) return data.length();
        return (this.left != null ? this.left.getTotalWeight() : 0) + (this.right != null ? this.right.getTotalWeight(): 0);
    }

    public String getData(){
        return data != null ? data : "";
    }

    public boolean isEmpty(){
        return getData().isEmpty();
    }
}





























