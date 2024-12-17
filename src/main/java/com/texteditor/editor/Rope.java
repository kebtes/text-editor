package com.texteditor.editor;

public class Rope {
    RopeNode root;

    public Rope(String text){
        root = new RopeNode(text);
    }

    public Rope(RopeNode node){
        this.root = node;
    }

    public char search(int cursorIndex){
        if (cursorIndex < 0 || cursorIndex > root.getTotalWeight()){
            throw new IndexOutOfBoundsException("Cursor position out of bounds.");
        }

        return searchCharRecursive(root, cursorIndex);
    }

    private char searchCharRecursive(RopeNode node, int idx){
        if (node.isLeaf()){
            return node.getData().charAt(idx);
        }

        if (idx < node.getWeight()){
            return searchCharRecursive(node.getLeftNode(), idx);
        }
        else {
            return searchCharRecursive(node.getRightNode(), idx - node.getWeight());
        }

    }

    public static RopeNode concat(RopeNode node_1, RopeNode node_2){
        return new RopeNode(node_1, node_2);
    }

    public void concat(Rope other){
        this.root = new RopeNode(this.root, other.root);
    }

    public void insert(int indexToInsert, String newString){
        if (indexToInsert < 0 || indexToInsert > root.getTotalWeight()){
            throw new IndexOutOfBoundsException("IndexToInsert out of bounds.");
        }

//        Split the rope
//        Create a new node for the string
//        concat the new string with the right side of the split rope
//        concat the left side of the split rope with the modified right side

        RopeNode[] splitRopes = split(indexToInsert);
        RopeNode newStringRopeNode = new RopeNode(newString);

        RopeNode rightSideRope = concat(newStringRopeNode, splitRopes[1]);
        root = concat(splitRopes[0], rightSideRope);
    }

    public void delete(int startingIndex, int endingIndex){
        if (startingIndex < 0 || endingIndex > root.getTotalWeight() || startingIndex > endingIndex){
            throw new IndexOutOfBoundsException("Invalid index range");
        }

        RopeNode[] rope1 = split(startingIndex);
        RopeNode[] rope2 = split(endingIndex + 1);

        root = concat(rope1[0], rope2[1]);
    }

    public Rope substring(int startingIndex, int endingIndex){
        if (startingIndex < 0 || endingIndex > root.getTotalWeight() || startingIndex > endingIndex) {
            throw new IndexOutOfBoundsException("Invalid index range to perform substring.");
        }

//        RopeNode[] rope1 = split(startingIndex);
//
//        if (startingIndex == endingIndex){
//            return new Rope(rope1[1].getData().substring(0, 1));
//        }
//
//        RopeNode[] rope2 = split(endingIndex + 1);
//
//        return new Rope(concat(rope1[1], rope2[0]));

        String sub = root.getData().substring(startingIndex, endingIndex);
        return new Rope(sub);
    }

    private RopeNode[] split(int splitIndex){
        if (splitIndex < 0 || splitIndex > root.getTotalWeight()){
            throw new IndexOutOfBoundsException("SplitIndex out of bounds");
        }

        return splitRecursive(root, splitIndex);
    }

    private RopeNode[] splitRecursive(RopeNode node, int idx){
        if (node.isLeaf()){
            String leftPartString = node.getData().substring(0, idx);
            String rightPartString = node.getData().substring(idx);

            return new RopeNode[]{new RopeNode(leftPartString), new RopeNode(rightPartString)};
        } else{
            if (idx < node.getWeight()){
                RopeNode[] leftSplit = splitRecursive(node.getLeftNode(), idx);

                return new RopeNode[]{leftSplit[0], new RopeNode(leftSplit[1], node.getRightNode())};
            } else{
                idx -= node.getWeight();
                RopeNode[] rightSplit = splitRecursive(node.getRightNode(), idx);

                return new RopeNode[]{new RopeNode(node.getLeftNode(), rightSplit[0]), rightSplit[1]};
            }
        }
    }

    public String getRopeData(){
        return getRopeDataRecursive(root);
    }

    private String getRopeDataRecursive(RopeNode node){
        if (node.isLeaf()){
            return node.getData();
        }
        return getRopeDataRecursive(node.getLeftNode()) + getRopeDataRecursive(node.getRightNode());
    }

}
