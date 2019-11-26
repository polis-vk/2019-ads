package ru.mail.polis.ads.bst;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * LLRB implementation of binary search tree.
 */

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * LLRB implementation of binary search tree.
 */
public class RedBlackBst<Key extends Comparable<Key>, Value>
        implements Bst<Key, Value>  {

    static final boolean RED = true;
    static final boolean BLACK = false;
    public Node root;
    int n=0;

    private class Node {
        Key key;
        Value value;
        Node left;
        Node right;
        int height;
        boolean color;

        Node(Key key, Value value, int height, boolean color){
            this.key = key;
            this.value = value;
            this.height = height;
            this.color = color;
        }

    }
    boolean isRed(Node x) {
        return x != null && x.color == RED;
    }


    @Nullable
    @Override
    public Value get(@NotNull Key key) {
        return get(root, key);
    }
    Value get(Node x, Key key){
        if (x == null){
            return null;
        }
        if (key.compareTo(x.key) > 0){
            return get(x.right, key);
        }
        if (key.compareTo(x.key) < 0){
            return get(x.left, key);
        }
        return x.value;
    }

    @Override
    public void put(@NotNull Key key, @NotNull Value value) {
        if (root == null){
            n+=1;
            root = new Node(key, value, 1, BLACK);
            return;
        }
        root = put(root, key, value);
        root.color = BLACK;
    }
    Node put(Node x, Key key, Value value){
        if (x == null){
            n+=1;
            return new Node(key, value, 1, RED);

        }
        int comp = key.compareTo(x.key);
        if (comp > 0){
            x.right = put(x.right, key, value);
        }
        else if (comp < 0){
            x.left = put(x.left, key, value);
        }
        else{
            x.value = value;
        }
        x = fixUp(x);
        return x;
    }
    Node fixUp(Node x){
        if (isRed(x.right) && !isRed(x.left)) {
            x = rotateLeft(x);
        }
        if (x.left != null) {
            if (isRed(x.left) && isRed(x.left.left)) {
                x = rotateRight(x);
            }
        }
        if (isRed(x.left) && isRed(x.right)) {
            flipColors(x);
        }

        return x;
    }

    private Node rotateLeft(Node x) {
        Node right = x.right;
        x.right = right.left;
        right.left = x;
        right.color = x.color;
        x.color = RED;
        return right;
    }

    private Node rotateRight(Node x) {
        Node left = x.left;
        x.left = left.right;
        left.right = x;
        left.color = x.color;
        x.color = RED;
        return left;
    }
    Node flipColors(Node x){
        if(x != null){
            x.color = !x.color;
        }
        if(x.right != null){
        x.right.color = !x.right.color;
        }
        if(x.left != null){
        x.left.color = !x.left.color;
        }
        return x;
    }


    @Nullable
    @Override
    public Value remove(@NotNull Key key)  {
        if (root == null) {
            return null;
        }
        Value res = this.get(key);
        if (res == null){
            return null;
        }
        root = remove(root, key);
        n-=1;
        return res;
    }
    Node remove(Node x, Key key)  {
        if (x == null) {
            return null;
        }
        int comp = key.compareTo(x.key);
        if (comp > 0){
            if (x.right != null){
                if (isRed(x.left))
                    x = rotateRight(x);
                if (!isRed(x.right) && !isRed(x.right.left))
                    x = moveRedRight(x);
                x.right = remove(x.right, key);
            }
        }
        else if (comp < 0){
            if (x.left != null){
                if (!isRed(x.left) && !isRed(x.left.left) )
                    x = moveRedLeft(x);
                x.left = remove(x.left, key);
            }
        }else {
            Node deleted = x;
            if (isRed(x.left)) {
                x = rotateRight(x);
                deleted = x.right;
            }
            if (deleted.right == null) {
                if (deleted.left != null) {
                    return deleted.left;
                } else {
                    return null;
                }
            }
            deleted.key = min(deleted.right).key;
            deleted.value = get(deleted.right, deleted.key);
            deleted.right = deleteMin(deleted.right);
        }
        return fixUp(x);
    }


    Node deleteMin(Node x){
        if (x.left == null){
            return null;
        }
        if (!isRed(x.left) && !isRed(x.left.left)){
            x = moveRedLeft(x);
        }
        x.left = deleteMin(x.left);
        return fixUp(x);
    }
    private Node moveRedLeft(Node x) {
        flipColors(x);
        if (x.right != null && isRed(x.right.left)) {
            x.right = rotateRight(x.right);
            x = rotateLeft(x);
            flipColors(x);
        }
        return x;
    }

    private Node moveRedRight(Node x) {
        flipColors(x);
        if (isRed(x.left.left)) {
            x = rotateRight(x);
            flipColors(x);
        }
        return x;
    }



    @Override
    public Key min() {
        if (root == null){
            return null;
        }else {
            return min(root).key;
        }
    }
    Node min(Node x){
        if (x == null) {
            return null;
        }
        if (x.left != null){
            return min(x.left);
        }else{
            return x;
        }
    }

    @Override
    public Value minValue() {
        if (root == null){
            return null;
        }else {
            return get(min(root).key);
        }
    }

    @Override
    public Key max() {
        if (root == null){
            return null;
        }else {
            return max(root).key;
        }
    }
    Node max(Node x){
        if (x.right != null){
            return max(x.right);
        }else{
            return x;
        }
    }

    @Override
    public Value maxValue() {
        if (root == null){
            return null;
        }else {
            return get(max(root).key);
        }
    }

    @Override
    public Key floor(Key key) {
        if (root == null){
            return null;
        }
        return floor(root, key);
    }
    Key floor(Node x, Key key){
        if (x == null){
            return null;
        }

        if (key.compareTo(x.key) < 0) {
            return floor(x.left, key);
        }
        Key result = floor(x.right, key);
        return (result == null) ? x.key : result;


    }

    @Override
    public Key ceil(Key key) {
        if (root == null){
            return null;
        }
        return ceil(root, key);
    }

    Key ceil(Node x, Key key){
        if (x == null){
            return null;
        }
        if (key.compareTo(x.key) == 0) {
            return x.key;
        }
        if (key.compareTo(x.key) > 0) {
            return ceil(x.right, key);
        }
        Key result = ceil(x.left, key);
        return (result == null) ? x.key : result;

    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public int height() {
        return height(root);
    }
    int height(Node x) {
        if (x==null){
            return 0;
        }
        return x.height;
    }

    void fixHeight(Node x){
        if (x.left == null){
            if (x.right == null){
                x.height = height(x);
            }else {
                x.height = height(x.right)+1;
            }
        }else if (x.right == null){
            if (!isRed(x.left))
                x.height = height(x.left)+1;
            else{
                x.height = height(x);
            }
        }
        else{
            if (!isRed(x.left))
                x.height = Math.max(height(x.left), height(x.right))+1;
            else{
                x.height = height(x.right);
            }
        }
    }
}