package com.texteditor.editor;

public class Rope {
    RopeNode root;

    public Rope(String text){
        root = new RopeNode(text != null ? text : "");
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
        if (indexToInsert < 0 || indexToInsert > getStringSize()){
            throw new IndexOutOfBoundsException("IndexToInsert out of bounds.");
        }

        if (newString.isEmpty()) return;

        if (root == null || root.isEmpty()) {
            root = new RopeNode(newString);
            return;
        }

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

        if (root == null || root.isEmpty()){
            return new Rope("");
        }

        RopeNode[] leftSplit = split(startingIndex);
        RopeNode[] rightSplit = split(endingIndex + 1);

        return new Rope(leftSplit[1]);
    }

    public RopeNode[] split(int splitIndex){
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

        }

        if (idx < node.getWeight()){
            RopeNode[] leftSplit = splitRecursive(node.getLeftNode(), idx);

            return new RopeNode[]{leftSplit[0], new RopeNode(leftSplit[1], node.getRightNode())};

        } else{
            idx -= node.getWeight();
            RopeNode[] rightSplit = splitRecursive(node.getRightNode(), idx);

            return new RopeNode[]{new RopeNode(node.getLeftNode(), rightSplit[0]), rightSplit[1]};
        }

    }

    public String getRopeData(){
        return root != null ? getRopeDataRecursive(root) : "";
    }

    private String getRopeDataRecursive(RopeNode node) {
        if (node == null) {
            return "";
        }

        if (node.isLeaf()) {
            return node.getData();
        }

        return getRopeDataRecursive(node.getLeftNode()) + getRopeDataRecursive(node.getRightNode());
    }

    public int getStringSize(){
        return root != null ? root.getTotalWeight() : 0;
    }

    public void append(String newString){
        if (newString == null || newString.isEmpty()) {
             return;
        }

        RopeNode newRopeNode = new RopeNode(newString);

        if (isEmpty()){
            root = newRopeNode;
        } else {
            root= concat(root, newRopeNode);
        }
    }

    public boolean isEmpty(){
        return root == null;
    }

    public void backspace(){
        if (!isEmpty()){
            if (getStringSize() == 1){
                root = new RopeNode("");
            }else{
                int newLength = getStringSize() - 1;
                RopeNode[] splitNodes = split(newLength);
                root = splitNodes[0];
            }
        }
    }

    public String peakLastChar(){
        if (isEmpty()){
            return "";
        }

        String data = getRopeData();
        return data.substring(Math.max(0, data.length() - 1));
    }
}
