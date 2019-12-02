package ru.mail.polis.ads.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * AVL implementation of binary search tree.
 */
public class RedBlackBst<Key extends Comparable<Key>, Value> implements Bst<Key, Value> {

    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        int height;
        boolean color;

        Node (Key key, Value value, int height, boolean color) {
            this.key = key;
            this.value = value;
            this.height = height;
            this.color = color;
        }
    }

    RedBlackBst() {
        size = 0;
    }
    private Node topNode;
    private Value deletedValue;
    private int size;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    @Nullable
    @Override
    public Value get(@NotNull Key key) {
        if (topNode == null) return null;
        Node n = get(topNode, key);
        return n != null ? n.value : null;
    }

    private Node get(Node node, Key key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            return get(node.right, key);
        } else if (cmp < 0) {
            return get(node.left, key);
        } else {
            return node;
        }
    }

    @Override
    public void put(@NotNull Key key, @NotNull Value value) {
        topNode = put(topNode, key, value);
        topNode.color = BLACK;
    }

    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            size++;
            return new Node(key, value, 1, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp > 0) {
            node.right = put(node.right, key, value);
        } else if (cmp < 0) {
            node.left = put(node.left, key, value);
        } else {
            node.value = value;
        }
        node = fixUp(node);
        fixHeight(node);
        return node;
    }

    private void fixHeight(Node node) {
        if (node.right == null && node.left != null)
            node.height = isRed(node.left) ? node.left.height : node.left.height + 1;
        else if (node.right != null && node.left == null)
            node.height = node.right.height + 1;
        else if (node.right == null)
            node.height = 1;
        else
            node.height = Math.max(node.left.height, node.right.height) + 1;
    }

    @Nullable
    @Override
    public Value remove(@NotNull Key key) {
        deletedValue = null;

        if (topNode != null)
            topNode = remove(topNode, key);
        else
            return null;

        if (deletedValue != null) size--;
        return deletedValue;
    }

    private Node remove(Node node, Key key) {
        if (node == null) return null;

        int cmp = key.compareTo(node.key);

        if (cmp < 0) {
            if (node.left != null) {
                if (!isRed(node.left) && !isRed(node.left.left))
                    node = moveRedLeft(node);
                node.left = remove(node.left, key);
            }
        } else if (cmp > 0) {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            if (node.right != null) {
                if (!isRed(node.right) && !isRed(node.right.left)) {
                    node = moveRedRight(node);
                }
                node.right = remove(node.right, key);
            }

        } else {
            deletedValue = node.value;

            if (node.right != null) {
                Node minNode = min(node.right);
                node.key = minNode.key;
                node.value = minNode.value;
                node.right = deleteMin(node.right);
            } else if (node.left != null) {
                Node maxNode = max(node.left);
                node.key = maxNode.key;
                node.value = maxNode.value;
                node.left = deleteMax(node.left);
            } else {
                return null;
            }
        }
        return fixUp(node);
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (node.left != null && isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (node.right != null && isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.left == null)
            return null;
        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);
        node.left = deleteMin(node.left);
        return fixUp(node);
    }

    private Node deleteMax(Node node) {
        if (node.right == null)
            return null;
        if (isRed(node.left)) {
            node = rotateRight(node);
        }
        if (!isRed(node.right) && !isRed(node.right.right))
            node = moveRedRight(node);
        node.right = deleteMin(node.right);
        return fixUp(node);
    }

    @Nullable
    @Override
    public Key min() {
        if (topNode == null) return null;
        Node node = min(topNode);
        return node.key;
    }

    private Node min(Node node) {
        if (node.left == null) return node;
        return min(node.left);
    }

    @Nullable
    @Override
    public Value minValue() {
        if (topNode == null) return null;
        Node minNode = min(topNode);
        return minNode.value;
    }

    @Nullable
    @Override
    public Key max() {
        if (topNode == null) return null;
        Node node = max(topNode);
        return node.key;
    }

    private Node max(Node node) {
        if (node.right == null) return node;
        return max(node.right);
    }

    @Nullable
    @Override
    public Value maxValue() {
        if (topNode == null) return null;
        Node maxNode = max(topNode);
        return maxNode.value;
    }

    @Nullable
    @Override
    public Key floor(@NotNull Key key) {
        if (topNode == null) return null;
        return findFloor(topNode, key);
    }

    private Key findFloor(Node node, Key key) {
        int cmp = key.compareTo(node.key);

        if (cmp > 0) {
            if (node.right == null)
                return node.key;
            else {
                // Если ключ, который мы ищем меньше минимального у правого сына текущего нода, то
                // 1) такого ключа в дереве нет.
                // 2) ключ текущего нода есть ближайший меньший к тому, которого мы ищем
                return min(node.right).key.compareTo(key) > 0 ? node.key : findFloor(node.right, key);
            }

        } else if (cmp < 0) {
            if (node.left == null)
                return null;
            else
                return findFloor(node.left, key);
        } else {
            return node.left != null ? node.left.key : node.key;
        }
    }

    @Nullable
    @Override
    public Key ceil(@NotNull Key key) {
        if (topNode == null) return null;
        return findCeil(topNode, key);
    }

    private Key findCeil(Node node, Key key) {
        int cmp = key.compareTo(node.key);

        if (cmp > 0) {
            if (node.right == null)
                return null;
            else
                return max(node.left).key.compareTo(key) < 0 ? node.key : findCeil(node.right, key);
        } else if (cmp < 0) {
            //
            if (node.left == null)
                return node.key;
            else
                return findCeil(node.left, key);
        } else {
            return node.right != null ? node.right.key : node.key;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int height() {
        return topNode != null ? topNode.height : 0;
    }

    private boolean isRed(Node node) {
        return node != null && node.color == RED;
    }

    private Node rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        right.color = node.color;
        node.color = RED;
        return right;
    }

    private Node rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        left.color = node.color;
        node.color = RED;
        return left;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        if (node.right != null)
            node.right.color = !node.right.color;
        if (node.left != null)
            node.left.color = !node.left.color;
    }

    private Node fixUp(Node node) {
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.right) && isRed(node.left)) {
            flipColors(node);
        }
        return node;
    }
}
