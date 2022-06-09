package com.donny.dendronetwork.ltree;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class TraversalTree<E> {
    public final TreeNode<E> HEAD;

    public TraversalTree(E headObject) {
        HEAD = new TreeNode<>(headObject, this);
    }

    public TreeNode<E> add(E content, BigDecimal cost, TreeNode<E> target) {
        TreeNode<E> existing = find(content);
        if (existing != null) {
            BigDecimal a = existing.getTotalCost();
            BigDecimal b = target.getTotalCost().add(cost);
            if (a.compareTo(b) > 0) {
                prune(existing);
            } else {
                return null;
            }
        }
        Random rand = new Random();
        boolean flag = true;
        byte[] cand = new byte[3];
        String newId = "";
        while (flag) {
            rand.nextBytes(cand);
            String candidate = Base64.getEncoder().encodeToString(cand);
            if (!target.getChildIds().contains(candidate)) {
                newId = candidate;
                flag = false;
            }
        }
        if (target.add(newId, content, cost)) {
            return target.CHILDREN.get(newId).NODE();
        }
        return null;
    }

    public TreeNode<E> add(E content, BigDecimal cost) {
        return add(content, cost, HEAD);
    }

    public boolean prune(TreeNode<E> discard) {
        if (discard.TRUNK == this) {
            if (discard.PARENT != null) {
                TreeNode<E> node = discard.PARENT;
                TreeNode.SubNode<E> bad = null;
                String badId = "";
                for (String key : node.CHILDREN.keySet()) {
                    TreeNode.SubNode<E> child = node.CHILDREN.get(key);
                    if (child.NODE().equals(discard)) {
                        bad = child;
                        badId = key;
                    }
                }
                if (bad != null) {
                    node.CHILDREN.remove(badId);
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public TreeNode<E> get(ArrayList<String> path) {
        if (path.get(0).equals("HEAD")) {
            TreeNode<E> cursor = HEAD;
            for (int i = 1; i < path.size(); i++) {
                if (cursor.CHILDREN.get(path.get(i)) != null) {
                    cursor = cursor.CHILDREN.get(path.get(i)).NODE();
                } else {
                    return null;
                }
            }
            return cursor;
        } else {
            return null;
        }
    }

    public TreeNode<E> get(String path) {
        return get(new ArrayList<>(Arrays.asList(path.split("\\."))));
    }

    public TreeNode<E> find(E check) {
        if (HEAD.CONTENT.equals(check)) {
            return HEAD;
        } else {
            return HEAD.find(check);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (String path : HEAD.getAllPaths()) {
            builder.append("\n  ").append(path);
        }
        builder.append("\n}");
        return builder.toString();
    }
}
