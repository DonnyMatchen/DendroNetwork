package com.donny.dendronetwork.ltree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class TreeNode<E> {
    public record SubNode<T>(TreeNode<T> NODE, BigDecimal COST) {
    }

    public final TraversalTree<E> TRUNK;
    public final TreeNode<E> PARENT;
    public final E CONTENT;
    public final String ID;
    public final HashMap<String, SubNode<E>> CHILDREN;

    TreeNode(E content, TraversalTree<E> trunk) {
        TRUNK = trunk;
        ID = "HEAD";
        PARENT = null;
        CONTENT = content;
        CHILDREN = new HashMap<>();
    }

    private TreeNode(String id, E content, TraversalTree<E> trunk, TreeNode<E> parent) {
        TRUNK = trunk;
        ID = id;
        PARENT = parent;
        CONTENT = content;
        CHILDREN = new HashMap<>();
    }

    public ArrayList<String> getChildIds() {
        return new ArrayList<>(CHILDREN.keySet());
    }

    public boolean matchesContents(TreeNode<E> b) {
        return CONTENT.equals(b.CONTENT);
    }

    boolean add(String id, E content, BigDecimal cost) {
        return CHILDREN.put(id, new SubNode<>(new TreeNode<>(id, content, TRUNK, this), cost)) != null;
    }

    public TreeNode<E> find(E check) {
        if (CHILDREN.size() > 0) {
            for (SubNode<E> child : CHILDREN.values()) {
                if (child.NODE.CONTENT.equals(check)) {
                    return child.NODE;
                }
            }
            for (SubNode<E> child : CHILDREN.values()) {
                TreeNode<E> candidate = child.NODE.find(check);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    public E getDirection(E endPoint) {
        for (SubNode<E> child : CHILDREN.values()) {
            if (child.NODE.find(endPoint) != null) {
                return child.NODE.CONTENT;
            }
        }
        return null;
    }

    public ArrayList<String> getPath() {
        Stack<String> stack = new Stack<>();
        stack.push(ID);
        TreeNode<E> cursor = PARENT;
        while (cursor != null) {
            stack.push(cursor.ID);
            cursor = cursor.PARENT;
        }
        ArrayList<String> out = new ArrayList<>();
        while (stack.size() > 0) {
            out.add(stack.pop());
        }
        return out;
    }

    public BigDecimal getTotalCost() {
        BigDecimal total = BigDecimal.ZERO;
        TreeNode<E> cursor = this;
        while (cursor.PARENT != null) {
            total = total.add(cursor.PARENT.CHILDREN.get(ID).COST);
            cursor = cursor.PARENT;
        }
        return total;
    }
}
