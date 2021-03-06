import java.util.Arrays;
import java.util.Stack;

/*
This program evulates a math expression given in a string format and prints out
the final answer. 

It assumes that the string is a valid expression.
Valid operators : 
x (multiply), 
/ (divide),
- (minus),
+ (plus),
() (parantheses)
*/
class EvaluateMathExpressionString {
    // 1 stack of integers to keep all operands.
    private static Stack<Integer> operands = new Stack<>();
    // variable that will be used to append multiple digits together during parsing
    // of
    // multi-digit number strings.
    private static String operandToPush = "";
    // 1 stack of characters to keep all operators.
    private static Stack<Character> operators = new Stack<>();

    public static void main(String[] args) {
        String expression = "   5 x ((144 x 2) / (12 + 12)) ";
        System.out.println("Answer: " + evaluateExpression(expression));
    }

    private static String evaluateExpression(String exp) {
        String answer = "";
        exp = exp.replace(" ", "");
        char[] characters = exp.toCharArray();

        // step 1 : parse and pre-evaluate if neccessary
        for (int index = 0; index < characters.length;) {
            // How to parse numbers:
            // Going from left to right, if we hit a single digit number, assume
            // that the next character is also part of that number (its next digit),
            // until you encounter a non-digit. Then, push this number
            // into the operand stack.
            if (Character.isDigit(characters[index])) {
                operandToPush += characters[index];
                // keep getting next digit
                int next = index + 1;
                while (Character.isDigit(characters[next])) {
                    operandToPush += characters[next];
                    // the last "next" will be a non-digit, hence must be
                    // the next location for the outer loop
                    next++;
                }
                index = next;
                // push final number
                System.out.println("pushing operand '" + operandToPush + "' ...");
                pushOperand(Integer.parseInt(operandToPush));
                System.out.println("operands: " + Arrays.toString(operands.toArray()));
                System.out.println("operators: " + Arrays.toString(operators.toArray()));

                // reset
                operandToPush = "";
            } else {
                // How to parse operators:
                // All operators are single characters. so if we come across anything that is
                // not a digit, means its an operator. Only closing brackets require
                // special treatment before pushing to stack.
                System.out.println("pushing operator '" + characters[index] + "' ...");
                pushOperator(characters[index]);
                System.out.println("operands: " + Arrays.toString(operands.toArray()));
                System.out.println("operators: " + Arrays.toString(operators.toArray()));
                index++;
            }
        }
        System.out.println("After step 1: ");
        System.out.println("operands: " + Arrays.toString(operands.toArray()));
        System.out.println("operators: " + Arrays.toString(operators.toArray()));
        // step 2: exhaust operator stack and evaluate along the way
        while (!operators.isEmpty()) {
            evaluate();
            System.out.println("operands: " + Arrays.toString(operands.toArray()));
            System.out.println("operators: " + Arrays.toString(operators.toArray()));
        }
        answer = operands.peek().toString();
        return answer;
    }

    // this helper method handles the pushing of new operands, perform
    // pre-evaluation
    // if necessary.
    private static void pushOperand(int operand) {
        int toPush = operand;

        // if current top operator is x or /, pre-evaluate and pop that operator.
        if (!operators.isEmpty()) {
            int firstOp = 0;
            char topOperator = operators.peek();
            switch (topOperator) {
            case 'x':
                firstOp = operands.pop();
                toPush = firstOp * operand;
                operators.pop();
                break;
            case '/':
                firstOp = operands.pop();
                toPush = firstOp / operand;
                operators.pop();
                break;
            }
        }

        // finally, push for real
        operands.push(toPush);
    }

    // pushOperator handles the pushing of an operator to the operator stack.
    // it first performs pre-evaluation if the operator to be pushed is a closing
    // bracket.
    private static void pushOperator(char operator) {
        // if closing bracket, don't need to push!
        if (operator == ')') {
            // evaluate all expressions in parantheses
            while (!operators.isEmpty()) {
                char top = operators.peek();
                // if we hit an opening bracket, stop evaluating.
                if (top == '(') {
                    operators.pop();
                    break;
                }
                evaluate();
            }
        } else {
            operators.push(operator);
        }
    }

    // evaluate will calculate the result of the following expression :
    // 2nd top element in operand stack followed by top element in operator stack,
    // followed by top element in operand stack. (e.g. 3x2)
    // 2 topmost operands and the topmost operator will be popped.
    // Final result is then pushed to operand stack. (i.e. 6)
    private static void evaluate() {
        char operator = operators.pop();
        if (operands.size() <= 1) {
            return;
        }
        int op2 = operands.pop();
        int op1 = operands.pop();
        switch (operator) {
        case 'x':
            operands.push(op1 * op2);
            break;
        case '/':
            operands.push(op1 / op2);
            break;
        case '-':
            operands.push(op1 - op2);
            break;
        case '+':
            operands.push(op1 + op2);
            break;
        }
    }
}
