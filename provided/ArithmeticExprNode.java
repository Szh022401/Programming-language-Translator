package provided;

/**
 * Node representing an arithmetic expression
 */
public class ArithmeticExprNode implements JottTree {
    private final JottTree left;    // Left operand
    private final String operator;  // Operator
    private final JottTree right;   // Right operand

    /**
     * Constructor for ArithmeticExprNode.
     *
     * @param left the left operand of the expression
     * @param operator the operator of the expression
     * @param right the right operand of the expression
     */
    public ArithmeticExprNode(JottTree left, String operator, JottTree right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return left.convertToJott() + " " + operator + " " + right.convertToJott();
    }
    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return true;
    }
}
