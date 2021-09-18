package spell;

public class Node implements INode {

    public Node() {
        this.count = 0;
        children = new Node[26];
    }

    /**
     * Returns the frequency count for the word represented by the node.
     *
     * @return the frequency count for the word represented by the node.
     */
    @Override
    public int getValue() {
        return count;
    }

    /**
     * Increments the frequency count for the word represented by the node.
     */
    @Override
    public void incrementValue() {
        ++count;
    }

    /**
     * Returns the child nodes of this node.
     *
     * @return the child nodes.
     */
    @Override
    public INode[] getChildren() {
        return children;
    }

    private int count;
    private Node[] children;
}
