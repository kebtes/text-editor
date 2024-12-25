package com.texteditor.editor;

/**
 * Represents a node in a Rope data structure, which is a binary tree-like structure used to efficiently manage
 * and manipulate large strings. Each node in the Rope can either be:
 * <p>
 * 1. A LEAF NODE: Contains a string of characters as its data, with no further child nodes.
 * 2. An INTERNAL NODE: Contains no data itself, but instead has two child nodes (left and right), each of which
 *    may be either a leaf node or another internal node. The weight of an internal node is the sum of the total weights
 *    of its left and right child nodes.
 * <p>
 * The Rope data structure is commonly used for string manipulation tasks such as concatenation, insertion, deletion,
 * and substring extraction, allowing these operations to be performed efficiently without copying large amounts of data.
 */
public class RopeNode {
    // Left and right child nodes
    private RopeNode left;
    private RopeNode right;

    // Data held in the node (only for leaf node)
    private String data;

    // Weight of the node (the length of the string for leaf nodes or the total weight for internal nodes)
    private final int weight;

    /**
     * Constructs a leaf node with the specified string data.
     *
     * @param data The string data for the leaf node.
     */
    public RopeNode(String data){
        this.data = data != null ? data : "";
        this.weight = this.data.length();
    }

    /**
     * Constructs an internal node with the specified left and right child nodes.
     *
     * @param left The left child node.
     * @param right The right child node.
     */
    public RopeNode(RopeNode left, RopeNode right){
        this.left = left;
        this.right = right;
        this.weight = (left != null) ? left.getTotalWeight() : 0;
    }

    /**
     * Returns the weight of the node.
     * For leaf nodes, the weight is the length of the string. For internal nodes, the weight is the total weight of its children.
     *
     * @return The weight of the node.
     */
    public int getWeight(){
        return this.weight;
    }

    /**
     * Returns the left child node of the current node.
     *
     * @return The left child node.
     */
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

}





























