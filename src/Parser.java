import java.util.Stack;

public class Parser {
    private static NFA parseRegularExpression(String regex) {
        // Remove unnecessary whitespace
        regex = regex.replaceAll("\\s", "");

        // Create a stack to hold NFAs and operators
        Stack<NFA> nfaStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        // Iterate over the characters of the regular expression
        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            // Process each character based on its type
            if (c == '(') {
                // Opening parenthesis: Push to operator stack
                operatorStack.push(c);
            } else if (c == ')') {
                // Closing parenthesis: Process operators until matching opening parenthesis is found
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    processOperator(nfaStack, operatorStack.pop());
                }

                if (!operatorStack.isEmpty() && operatorStack.peek() == '(') {
                    operatorStack.pop(); // Pop the matching opening parenthesis
                } else {
                    throw new IllegalArgumentException("Invalid regular expression: Unbalanced parentheses");
                }
            } else if (isOperator(c)) {
                // Operator: Process operators with higher precedence before pushing
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' && hasHigherPrecedence(c, operatorStack.peek())) {
                    processOperator(nfaStack, operatorStack.pop());
                }
                operatorStack.push(c);
            } else {
                // Character: Create an NFA for the character and push to the NFA stack
                NFA nfa = new NFA();
                nfa.addState();
                nfa.addState();
                nfa.addTransition(nfa.getStartState(), c, nfa.getEndState());
                nfaStack.push(nfa);
            }
        }

        // Process remaining operators
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == '(') {
                throw new IllegalArgumentException("Invalid regular expression: Unbalanced parentheses");
            }
            processOperator(nfaStack, operatorStack.pop());
        }

        if (nfaStack.size() != 1) {
            throw new IllegalArgumentException("Invalid regular expression: Failed to parse completely");
        }

        return nfaStack.pop();
    }

    private static boolean isOperator(char c) {
        return c == '*' || c == '|' || c == '.';
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        return (op1 == '*' || op1 == '.') && op2 == '|';
    }

    private static void processOperator(Stack<NFA> nfaStack, char operator) {
        if (operator == '*') {
            // Kleene star: Pop an NFA, create new start and end states, and add epsilon transitions
            NFA nfa = nfaStack.pop();
            int startState = nfa.getStartState();
            int endState = nfa.getEndState();
            nfa.addTransition(startState, NFA.EPSILON, endState);
            nfa.addTransition(startState, NFA.EPSILON, nfa.getStartState());
            nfa.addTransition(endState, NFA.EPSILON, nfa.getStartState());
            nfa.setStartState(startState);
            nfa.setEndState(endState);
            nfaStack.push(nfa);
        } else if (operator == '|') {
            // Union: Pop two NFAs, create new start and end states, and add epsilon transitions
            NFA nfa2 = nfaStack.pop();
            NFA nfa1 = nfaStack.pop();
            NFA nfa = new NFA();
            int startState = nfa.addState();
            int endState = nfa.addState();
            nfa.addEpsilonTransition(startState, nfa1.getStartState());
            nfa.addEpsilonTransition(startState, nfa2.getStartState());
            nfa.addEpsilonTransition(nfa1.getEndState(), endState);
            nfa.addEpsilonTransition(nfa2.getEndState(), endState);
            nfa.setStartState(startState);
            nfa.setEndState(endState);
            nfaStack.push(nfa);
        } else if (operator == '.') {
            // Concatenation: Pop two NFAs, connect their end and start states with epsilon transitions
            NFA nfa2 = nfaStack.pop();
            NFA nfa1 = nfaStack.pop();
            nfa1.addEpsilonTransition(nfa1.getEndState(), nfa2.getStartState());
            nfa1.setEndState(nfa2.getEndState());
            nfaStack.push(nfa1);
        }
    }

}
