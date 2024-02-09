package R2D_CD;

import java.util.Stack;

import R2D_CD.Components.SynTreeNode;

public class Preprocessor {
    SynTreeNode root;
    char[] postFixExp;
    Stack<SynTreeNode> st;

    // --------------------------------------------------------------------------------------
    static boolean isOperator(char symbol) {
        return symbol == '+' || symbol == '.' || symbol == '*' || symbol == ')' || symbol == '(';
    }

    // --------------------------------------------------------------------------------------------
    // Creates . added exp
    String process(String exp) {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            temp.append(exp.charAt(i));
            if (!isOperator(exp.charAt(i)) || exp.charAt(i) == '*') {
                if (i != exp.length() - 1) {
                    if (!isOperator(exp.charAt(i + 1))) {
                        temp.append('.');
                    }
                }
            }
        }
        int s = temp.length();

        temp.append('.');
        temp.append('#');
        return temp.toString();
    }

    void buildSynTree(String exp) {
        int k = 1;
        String t = process(exp);
        System.out.println(t);
        postFixExp = Infix2Postfix.infixToPostfix(t).toCharArray();
        System.out.println(postFixExp);
        st = new Stack<>();
        for (int i = 0; i < postFixExp.length; i++) {
            if (!isOperator(postFixExp[i])) {
                st.push(new SynTreeNode(postFixExp[i], k++));
            } else {
                if (postFixExp[i] != '*') {
                    SynTreeNode temp = new SynTreeNode(postFixExp[i], -1);
                    temp.rightChild = st.pop();
                    temp.leftChild = st.pop();
                    st.push(temp);
                } else {
                    SynTreeNode temp = new SynTreeNode(postFixExp[i], -1);
                    temp.leftChild = st.pop();
                    temp.nullable = true;
                    st.push(temp);
                }
            }
        }
        root = st.pop();
    }

    void traverse(SynTreeNode r) {
        if (r == null) {
            return;
        }
        System.out.println(r);
        traverse(r.leftChild);
        traverse(r.rightChild);
    }

    // --------------------------------------------------------------
    // testing
    public static void main(String[] args) {
        Preprocessor p = new Preprocessor();
        String exp = "a*bab";
        p.buildSynTree(exp);
        p.traverse(p.root);
    }
}
