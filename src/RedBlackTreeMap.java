import java.util.*;

public class RedBlackTreeMap<TKey extends Comparable<TKey>, TValue> {
    private class Node {
        private TKey mKey;
        private TValue mValue;
        private Node mParent;
        private Node mLeft;
        private Node mRight;
        private boolean mIsRed;

        public Node(TKey key, TValue data, boolean isRed) {
            mKey = key;
            mValue = data;
            mIsRed = isRed;

            mLeft = NIL_NODE;
            mRight = NIL_NODE;
        }

        @Override
        public String toString() {
            return mKey + " : " + mValue + " (" + (mIsRed ? "red)" : "black)");
        }
    }

    private Node mRoot;
    private int mCount;

    private final Node NIL_NODE = new Node(null, null, false);

    public int getCount() {
        return mCount;
    }

    public TValue find(TKey key) {
        Node n = bstFind(key, mRoot);
        if (n == null || n == NIL_NODE) {
            throw new RuntimeException("Key not found");
        }
        return n.mValue;
    }

    public void add(TKey key, TValue data) {
        Node n = new Node(key, data, true); // nodes start red

        boolean insertedNew = bstInsert(n, mRoot);
        if (!insertedNew) {
            return;
        }
        checkBalance(n);

    }

    private void checkBalance(Node n) {
        Node p = n.mParent;
        Node u = getUncle(n);
        Node g = getGrandparent(n);

        //Case 1: N is inserted as the root, and needs to be painted black
        if (n == mRoot) {
            n.mIsRed = false;
            return;
        }

        //Case 2: P is black -- everything is okay
        if (!p.mIsRed) {
            return;
        }

        //FROM HERE, P IS RED -- if it were black, then the function would have returned

        //Case 3: P and U are both red
        if (p.mIsRed && u.mIsRed) {
            p.mIsRed = false;
            u.mIsRed = false;
            g.mIsRed = true;
            if (g == mRoot || g.mParent.mIsRed) {
                checkBalance(g);
            }
        }

        //Case 4: P is red and U is black
        if (p.mIsRed && !u.mIsRed) {
            if (n == g.mLeft.mRight) {
                singleRotateLeft(p);
                Node temp = p;
                p = n;
                n = temp;
            }
            if (n == g.mRight.mLeft) {
                singleRotateRight(p);
                Node temp = p;
                p = n;
                n = temp;
            }
            if (n == g.mLeft.mLeft) {
                singleRotateRight(g);
                p.mIsRed = false;
                g.mIsRed = true;
            }
            if (n == g.mRight.mRight) {
                singleRotateLeft(g);
                p.mIsRed = false;
                g.mIsRed = true;
            }
        }
    }

    public boolean containsKey(TKey key) {
        return bstFind(key, mRoot).mKey == key;
    }

    public void printStructure() {
        printStructure(mRoot);
    }
    //Prints pre-order traversal of the tree
    private void printStructure(Node n) {
        if (n == null) {
            return;
        }
        if (n != NIL_NODE) {
            System.out.println(n.toString());
        }
        printStructure(n.mLeft);
        printStructure(n.mRight);
    }

    private Node bstFind(TKey key, Node currentNode) {
        if (currentNode == null) {                      //node is null
            return null;
        }
        if (currentNode.mKey == key) {                  //node == key
            return currentNode;
        }
        if (key.compareTo(currentNode.mKey) < 0) {      //key < node
            bstFind(key, currentNode.mLeft);
        }
        else if (key.compareTo(currentNode.mKey) > 0) { //key > node
            bstFind(key, currentNode.mRight);
        }
        return null;
    }



    //////////////// These functions are needed for insertion cases.

    private Node getGrandparent(Node n) {
        Node p = n.mParent;
        if (p == null) {
            return null;
        }
        return p.mParent;
    }

    // Gets the uncle (parent's sibling) of n.
    private Node getUncle(Node n) {
        if (n == mRoot || n.mParent == mRoot) {
            return null;
        }

        else {
            Node p = n.mParent;
            Node gp = getGrandparent(n);

            if (p == gp.mLeft) {    //Check if n's parent is the left child
                return gp.mRight;   //and if it is, return its sibling
            }
            if (p == gp.mRight) {                  //If n's parent is not the right child,
                return gp.mLeft;    //then it is the left child, and return its sibling
            }
        }
        return NIL_NODE;
    }

    private void singleRotateRight(Node n) {
        Node l = n.mLeft, lr = l.mRight, p = n.mParent;
        n.mLeft = lr;
        lr.mParent = n;
        l.mRight = n;
        n.mParent = l;

        if (p == null) { // n is root
            mRoot = l;
        }
        else if (p.mLeft == n) {
            p.mLeft = l;
        }
        else {
            p.mRight = l;
        }

        l.mParent = p;
    }

    private void singleRotateLeft(Node n) {
        Node r = n.mRight, rl = r.mLeft, p = n.mParent;
        n.mRight = rl;
        rl.mParent = n;
        r.mLeft = n;
        n.mParent = r;

        if (p == null) {
            mRoot = r;
        }
        else if (p.mLeft == n) {
            p.mLeft = r;
        }
        else {
            p.mRight = r;
        }

        r.mParent = p;
    }

    private boolean bstInsert(Node newNode, Node currentNode) {
        if (mRoot == null) {
            // case 1
            mRoot = newNode;
            return true;
        }
        else {
            int compare = currentNode.mKey.compareTo(newNode.mKey);
            if (compare < 0) {
                if (currentNode.mRight != NIL_NODE) {
                    return bstInsert(newNode, currentNode.mRight);
                }
                else {
                    currentNode.mRight = newNode;
                    newNode.mParent = currentNode;
                    mCount++;
                    return true;
                }
            }
            else if (compare > 0) {
                if (currentNode.mLeft != NIL_NODE) {
                    return bstInsert(newNode, currentNode.mLeft);
                }
                else {
                    currentNode.mLeft = newNode;
                    newNode.mParent = currentNode;
                    mCount++;
                    return true;
                }
            }
            else {
                currentNode.mValue = newNode.mValue;
                return false;
            }
        }
    }
}
