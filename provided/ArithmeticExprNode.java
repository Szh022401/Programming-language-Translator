package provided;

import java.util.Objects;

/**
 * Node representing an arithmetic expression
 */
public class ArithmeticExprNode implements IExpression {
    private final IExpression left;    // Left operand
    private final Token operator;  // Operator
    private final IExpression right;   // Right operand

    /**
     * Constructor for ArithmeticExprNode.
     *
     * @param left the left operand of the expression
     * @param operator the operator of the expression
     * @param right the right operand of the expression
     */
    public ArithmeticExprNode(IExpression left, Token operator, IExpression right) {
        this.left = left;
        this.operator = operator.CloneToken();
        this.right = right;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return left.convertToJott() + " " + operator.getToken() + " " + right.convertToJott();
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

    public String getType()
    {
        if (Objects.equals(left.getType(), right.getType())){
            return left.getType();
        }
        else
            return null;
    }
}
