package R2D_CD;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Components {
    static class SynTreeNode {
        char token;
        int index;
        SynTreeNode leftChild;
        SynTreeNode rightChild;
        Set<Integer> fisrtPos, lastPos;
        boolean nullable;

        SynTreeNode(char val, int i) {
            token = val;
            index = i;
            leftChild = rightChild = null;
            fisrtPos = new HashSet<>();
            lastPos = new HashSet<>();
            nullable = false;
        }

        @Override
        public String toString() {
            return "Node : " + token + " index : " + index + " firstpos" + fisrtPos + " lastpos : " + lastPos;
        }
    }

    static class State {
        String name;
        Set<Integer> val;
        boolean isFinal;
        boolean isTrap;

        State(String name, Set<Integer> v) {
            this.name = name;
            val = new HashSet<>(v);
            isFinal = false;
            isTrap = false;
        }

        @Override
        public String toString() {
            // return "state : " + name + " " + val.toString() + isFinal;
            return "state : " + name + " " + val.toString();
        }

        @Override
        public boolean equals(Object obj) {
            State s = (State) obj;
            return this.val.equals(s.val);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(val);
        }
    }

}