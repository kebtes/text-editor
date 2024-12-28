package com.texteditor.editor;

/*
 * The Rope class implements a rope data structure for efficient string manipulation.
 * It supports operations like search, insert, delete, split, and concatenation.
 */
public class Rope {
    RopeNode root;

    public Rope(String text){
        root = new RopeNode(text != null ? text : "");
    }

    @SuppressWarnings("all")
    public Rope(RopeNode node){
        this.root = node;
    }

    /**
     * Searches for the character at the specified index.
     *
     * @param cursorIndex The index of the character to search for.
     * @return The character at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public char search(int cursorIndex){
        if (cursorIndex < 0 || cursorIndex > root.getTotalWeight()){
            throw new IndexOutOfBoundsException("Cursor position out of bounds.");
        }

        return searchCharRecursive(root, cursorIndex);
    }

    /**
     * Recursive helper for searching a character at a specific index.
     *
     * @param node The current node.
     * @param idx The index of the character to search for.
     * @return The character at the specified index.
     */
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

    /**
     * Concatenates two RopeNodes and returns a new RopeNode.
     *
     * @param node_1 The first RopeNode.
     * @param node_2 The second RopeNode.
     * @return A new RopeNode resulting from concatenating the two nodes.
     */
    public static RopeNode concat(RopeNode node_1, RopeNode node_2){
        return new RopeNode(node_1, node_2);
    }

    /**
     * Concatenates another Rope to the current Rope.
     *
     * @param other The other Rope to concatenate.
     */
    public void concat(Rope other){
        this.root = new RopeNode(this.root, other.root);
    }

    /**
     * Inserts a string at a specified index in the Rope.
     *
     * @param indexToInsert The index where the string should be inserted.
     * @param newString The string to insert.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void insert(int indexToInsert, String newString){
        if (indexToInsert < 0 || indexToInsert > getStringSize()){
            System.out.println(indexToInsert);
            throw new IndexOutOfBoundsException("IndexToInsert out of bounds.");
        }

        if (newString.isEmpty()) return;

        if (root == null) {
            root = new RopeNode(newString);
            return;
        }

        RopeNode[] splitRopes = split(indexToInsert);
        RopeNode newStringRopeNode = new RopeNode(newString);

        RopeNode rightSideRope = concat(newStringRopeNode, splitRopes[1]);
        root = concat(splitRopes[0], rightSideRope);
    }

    /**
     * Deletes a range of characters from the Rope.
     *
     * @param startingIndex The starting index of the range.
     * @param endingIndex The ending index of the range.
     * @throws IndexOutOfBoundsException If the range is invalid.
     */
    public void delete(int startingIndex, int endingIndex){
        if (startingIndex < 0 || endingIndex > root.getTotalWeight() || startingIndex > endingIndex){
            throw new IndexOutOfBoundsException("Invalid index range");
        }

        RopeNode[] rope1 = split(startingIndex);
        RopeNode[] rope2 = split(endingIndex);

        root = concat(rope1[0], rope2[1]);
    }

    /**
     * Extracts a substring from the Rope.
     *
     * @param startingIndex The starting index of the substring.
     * @param endingIndex The ending index of the substring.
     * @return A new Rope containing the substring.
     * @throws IndexOutOfBoundsException If the range is invalid.
     */
    public Rope substring(int startingIndex, int endingIndex) {
        if (startingIndex < 0 || endingIndex > root.getTotalWeight() || startingIndex > endingIndex) {
            throw new IndexOutOfBoundsException("Invalid index range to perform substring.");
        }

        if (root == null) {
            return new Rope("");
        }

        String data = getRopeData();
        return new Rope(data.substring(startingIndex, endingIndex));
    }

    /**
     * Splits the Rope at a specified index into two RopeNodes.
     *
     * @param splitIndex The index where the Rope should be split.
     * @return An array containing the left and right RopeNodes.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public RopeNode[] split(int splitIndex){
        if (splitIndex < 0 || splitIndex > root.getTotalWeight()){
            throw new IndexOutOfBoundsException("SplitIndex out of bounds");
        }

        return splitRecursive(root, splitIndex);
    }

    /**
     * Recursive helper for splitting a RopeNode.
     *
     * @param node The current RopeNode.
     * @param idx The split index.
     * @return An array containing the left and right RopeNodes.
     */
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

    /**
     * Retrieves the entire string stored in the Rope.
     *
     * @return The string representation of the Rope.
     */
    public String getRopeData(){
        return root != null ? getRopeDataRecursive(root) : "";
    }

    /**
     * Recursive helper for retrieving the string stored in a RopeNode.
     *
     * @param node The current RopeNode.
     * @return The string stored in the node.
     */
    private String getRopeDataRecursive(RopeNode node) {
        if (node == null) {
            return "";
        }

        if (node.isLeaf()) {
            return node.getData();
        }

        return getRopeDataRecursive(node.getLeftNode()) + getRopeDataRecursive(node.getRightNode());
    }

    /**
     * Deletes the last character from the Rope.
     * If the Rope contains only one character, it resets the Rope to an empty state.
     * If the Rope is empty, no operation is performed.
     */
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

    /**
     * Deletes the character at the specified cursor position.
     * If the Rope is empty, no operation is performed.
     *
     * @param cursor The index of the character to delete.
     */
    public void backspace(int cursor) {
        if (!isEmpty()) {
            RopeNode[] split1 = split(cursor - 1);
            RopeNode[] split2 = split(cursor);
            root = new RopeNode(split1[0], split2[1]);
        }
    }

    /**
     * Retrieves the last character in the Rope without modifying it.
     * If the Rope is empty, returns an empty string.
     *
     * @return The last character as a string, or an empty string if the Rope is empty.
     */
    @Deprecated
    public String peakLastChar(){
        if (isEmpty()){
            return "";
        }

        String data = getRopeData();
        return data.substring(Math.max(0, data.length() - 1));
    }

    public String peakLastChar(int cursor) {
        if (cursor == 0) return "";

        return substring(cursor - 1, cursor).getRopeData();
    }

    /**
     * Retrieves the last word in the Rope without modifying it.
     * A word is defined as a sequence of non-space characters.
     * If the Rope is empty, returns an empty string.
     *
     * @return The last word as a string, or an empty string if the Rope is empty.
     */
    public String peakLastWord() {
        if (isEmpty()) {
            return "";
        }

        int totalWeight = root.getTotalWeight();
        StringBuilder lastWord = new StringBuilder();

        for (int i = totalWeight - 1; i >= 0; i--) {
            char currentChar = search(i);

            if (currentChar == ' ' && !lastWord.isEmpty()) {
                break;
            }

            lastWord.insert(0, currentChar);
        }

        return lastWord.toString();
    }

    /**
     * Retrieves the characters of the last "word" before the given cursor position in the text,
     * stopping at a space (' ') or newline ('\n') character.
     *
     * @param cursor The position in the text where the search for the last word begins (inclusive).
     * @return A string representing the last sequence of non-whitespace characters (a "word")
     *         found before the given cursor position. If the text is empty or no characters exist
     *         before the cursor, it returns an empty string ("").
     */
    public String peakLastCharsBeforeCursor(int cursor) {
        if (isEmpty()) {
            return "";
        }

        StringBuilder lastWordFromCursor = new StringBuilder();

        for (int idx = cursor; idx >= 0; idx--) {
            char idxChar = search(idx);

            if (idxChar == ' ' || idxChar == '\n') break;

            lastWordFromCursor.append(idxChar);
        }

        lastWordFromCursor.reverse();
        return lastWordFromCursor.toString();
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
}
