package spell;

public class Trie implements ITrie {
    public Trie() {
        this.root = new Node();
        wordCount = 0;
        nodeCount = 1;
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count.
     *
     * @param word the word being added to the trie
     */
    @Override
    public void add(String word) {
        StringBuilder inputWord = new StringBuilder(word);
        addHelper(root, inputWord);
    }

    private void addHelper(Node node, StringBuilder word) {
        //Check for being done
        if (word.length() == 0) {
            node.incrementValue();
            if (node.getValue() == 1) {
                ++wordCount;
            }
            return;
        }
        Node[] children = (Node[]) node.getChildren();
        char firstChar = word.charAt(0);
        if (children[indexCharConversion(firstChar)] == null) {
            children[indexCharConversion(firstChar)] = new Node();
            ++nodeCount;
        }
        word.deleteCharAt(0);
        addHelper(children[indexCharConversion(firstChar)], word);

    }

    /**
     * Searches the trie for the specified word.
     *
     * @param word the word being searched for.
     * @return a reference to the trie node that represents the word,
     * or null if the word is not in the trie
     */
    @Override
    public INode find(String word) {
        StringBuilder inputWord = new StringBuilder(word);

        return findHelper(inputWord, root);
    }

    private Node findHelper(StringBuilder word, Node node) {
        if (node == null) {
            return null;
        }
        if (word.length() == 0) {
            if (node.getValue() < 1) {
                return null;
            }
            return node;
        }
        char firstChar = word.charAt(0);
        int index = indexCharConversion(firstChar);
        word = word.deleteCharAt(0);
        Node[] children = (Node[]) node.getChildren();

        return findHelper(word, children[index]);

    }

    /**
     * Returns the number of unique words in the trie.
     *
     * @return the number of unique words in the trie
     */
    @Override
    public int getWordCount() {
        return wordCount;
    }

    /**
     * Returns the number of nodes in the trie.
     *
     * @return the number of nodes in the trie
     */
    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    public static int indexCharConversion(char letter) {
        return (int)(letter) - 97;
    }

    public static char indexCharConversion(int index) {
        return (char)(index + 97);
    }

    @Override
    public String toString() {
        StringBuilder word = new StringBuilder();
        StringBuilder words = new StringBuilder();
        toStringHelper(root, word, words);
        return words.toString();
    }
    private void toStringHelper(Node node, StringBuilder word, StringBuilder words) {
        if (node.getValue() > 0) {
            words.append(word);
            words.append('\n');
        }

        Node[] children = (Node[]) node.getChildren();
        for (int i = 0; i < children.length; ++i) {
            if (children[i] != null) {
                word.append(indexCharConversion(i));
                toStringHelper(children[i], word, words);
                word.deleteCharAt(word.length() - 1);
            }
        }
    }

    @Override
    public int hashCode() {
        int hashcode = wordCount;
        hashcode *= 31;
        hashcode += nodeCount;
        hashcode *= 31;

        Node[] children = (Node[]) root.getChildren();
        for (int i = 0; i < children.length; ++i) {
            if (children[i] != null) {
                hashcode += i;
                hashcode *= 31;
            }
        }
        return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Trie)) {
            return false;
        }

        Trie comparedTrie = (Trie) obj;

        if (this.wordCount != comparedTrie.wordCount) {
            return false;
        }
        if (this.nodeCount != comparedTrie.nodeCount) {
            return false;
        }

        return compareNodes(this.root, comparedTrie.root);
    }
    private boolean compareNodes(Node firstNode, Node secondNode) {
        if (firstNode == null && secondNode != null) {
            return false;
        }
        if (firstNode != null && secondNode == null) {
            return false;
        }
        if (firstNode == null && secondNode == null) {
            return true;
        }
        Node[] firstChildren = (Node[]) firstNode.getChildren();
        Node[] secondChildren = (Node[]) secondNode.getChildren();

        for (int i = 0; i < firstChildren.length; ++i) {
            if (firstChildren[i] == null && secondChildren[i] != null) {
                return false;
            }
            if (firstChildren[i] != null && secondChildren[i] == null) {
                return false;
            }
            if (firstChildren[i] != null && secondChildren[i] != null) {
                if (firstChildren[i].getValue() != secondChildren[i].getValue()) {
                    return false;
                }
                if (!compareNodes(firstChildren[i], secondChildren[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private Node root;
    private int wordCount;
    private int nodeCount;
}
