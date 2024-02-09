package R2D_CD;

import java.util.Stack;

public class Infix2Postfix {

    // Function to check if the given character is an operator
    private static boolean isOperator(char ch) {
        return (ch == '+' || ch == '.' || ch == '*');
    }

    // Function to get the precedence of an operator
    private static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
                return 1;
            case '.':
                return 2;
            case '*':
                return 3;
            default:
                return -1;
        }
    }

    // Function to convert infix expression to postfix expression
    public static String infixToPostfix(String input) {
        StringBuilder res = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (Character.isLetterOrDigit(currentChar) || currentChar == '#') {
                res.append(currentChar);
            } else if (currentChar == '(') {
                stack.push(currentChar);
            } else if (currentChar == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    res.append(stack.pop());
                }
                stack.pop(); // Pop the opening parenthesis from the stack
            } else if (isOperator(currentChar)) {
                while (!stack.isEmpty() && getPrecedence(currentChar) <= getPrecedence(stack.peek())) {
                    res.append(stack.pop());
                }
                stack.push(currentChar);
            }
        }

        while (!stack.isEmpty()) {
            res.append(stack.pop());
        }

        return res.toString();
    }

    public static void main(String[] args) {
        String exp = "(a+b)*";
        // System.out.println(infixToPostfix(exp));
    }
}