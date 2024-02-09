package R2D_CD;

import java.util.HashMap;
import java.util.HashSet;
// import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import R2D_CD.Components.SynTreeNode;

public class FollowPos {
    SynTreeNode root;
    Map<Integer, Map<Character, Set<Integer>>> followPosEntries;
    Map<Integer, Character> map;

    void operate(String exp) {
        followPosEntries = new HashMap<>();
        map = new HashMap<>();
        Preprocessor p = new Preprocessor();
        p.buildSynTree(exp);
        root = p.root;
        createMap(root);
        fisrtPosLastPos(root);
        followPosTable(root);
        // traverse(root);
        // System.out.println(map);
        followPosEntries.put(6, new HashMap<>());
        Map<Character, Set<Integer>> temp = followPosEntries.get(6);
        Set<Integer> terminate_set = new HashSet<>();
        terminate_set.add(-1);
        temp.put('#', terminate_set);
        // System.out.println(followPosEntries);
    }

    void createMap(SynTreeNode root) {
        if (root.leftChild == null && root.rightChild == null) {
            map.put(root.index, root.token);
            return;
        }
        // System.out.println("Hi" + root.token);
        if (root.leftChild != null)
            createMap(root.leftChild);
        if (root.rightChild != null)
            createMap(root.rightChild);
    }

    void fisrtPosLastPos(SynTreeNode root) {
        if (root.leftChild == null && root.rightChild == null) {
            root.fisrtPos.add(root.index);
            root.lastPos.add(root.index);
            return;
        }
        SynTreeNode c1 = root.leftChild;
        SynTreeNode c2 = root.rightChild;
        fisrtPosLastPos(c1);
        if (root.rightChild != null)
            fisrtPosLastPos(c2);
        if (root.token == '+') {
            // System.out.println("FPLP traverse : " + root.token);
            // System.out.println("First Pos of Left Child : " + c1.fisrtPos);
            root.fisrtPos.addAll(c1.fisrtPos);
            // System.out.println("First Pos of Right Child : " + c2.fisrtPos);
            root.fisrtPos.addAll(c2.fisrtPos);
            // System.out.println("Last Pos of Left Child : " + c1.lastPos);
            root.lastPos.addAll(c1.lastPos);
            // System.out.println("First Pos of Right Child : " + c2.lastPos);
            root.lastPos.addAll(c2.lastPos);
        } else if (root.token == '*') {
            // System.out.println("FPLP traverse : " + root.token);
            // System.out.println("First Pos of Left Child : " + c1.fisrtPos);
            root.fisrtPos.addAll(c1.fisrtPos);
            // System.out.println("Last Pos of Left Child : " + c1.lastPos);
            root.lastPos.addAll(c1.lastPos);
        } else if (root.token == '.') {
            // System.out.println("FPLP traverse : " + root.token);
            if (c1.nullable == true) {
                // System.out.println("First Pos of Left Child : " + c1.fisrtPos);
                root.fisrtPos.addAll(c1.fisrtPos);
                // System.out.println("First Pos of Right Child : " + c2.fisrtPos);
                root.fisrtPos.addAll(c2.fisrtPos);
            } else {
                // System.out.println("First Pos of Left Child : " + c1.fisrtPos);
                root.fisrtPos.addAll(c1.fisrtPos);
            }
            // System.out.println("Last Pos of right Child : " + c2.lastPos);
            root.lastPos.addAll(c2.lastPos);
        }
    }

    void followPosTable(SynTreeNode root) {
        if (root == null)
            return;
        if (root.leftChild != null) {
            followPosTable(root.leftChild);
        }
        followPosTable(root.rightChild);
        char token = root.token;
        if (token == '*') {
            for (Integer in : root.lastPos) {
                if (!followPosEntries.containsKey(in)) {
                    followPosEntries.put(in, new HashMap<>());
                    followPosEntries.get(in).put(map.get(in), root.fisrtPos);
                } else {
                    followPosEntries.get(in).get(map.get(in)).addAll(root.fisrtPos);
                }
            }
        } else if (token == '.') {
            for (Integer in : root.leftChild.lastPos) {
                if (!followPosEntries.containsKey(in)) {
                    followPosEntries.put(in, new HashMap<>());
                    followPosEntries.get(in).put(map.get(in), root.rightChild.fisrtPos);
                } else {
                    followPosEntries.get(in).get(map.get(in)).addAll(root.rightChild.fisrtPos);
                }
            }
        }
    }

    void traverse(SynTreeNode r) {
        if (r == null) {
            return;
        }
        traverse(r.leftChild);
        System.out.println(r);
        traverse(r.rightChild);
    }

    SynTreeNode getRoot() {
        return root;
    }

    Map<Integer, Character> getMap() {
        return map;
    }

    Map<Integer, Map<Character, Set<Integer>>> getFP() {
        return followPosEntries;
    }

    public static void main(String[] args) {
        FollowPos fp = new FollowPos();
        fp.operate("a*bab");
        // fp.traverse(fp.root);
        // System.out.println(fp.getMap());
        System.out.println(fp.getFP());
    }
}
