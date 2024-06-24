package provided;

/**
 * Node representing an expression
 */
public class ExpressionNode implements IExprType {
    private Token value; // value of the expression

    /**
     * Constructor for ExpressionNode
     *
     * @param value value of the expression
     */
    public ExpressionNode(Token value) {
        this.value = value;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return value.getToken();
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
        if (value.getTokenType() == TokenType.STRING)
            return "String";
        else if (value.getTokenType() == TokenType.NUMBER) {
            if (value.getToken().contains("."))
                return "Double";
            else
                return "Integer";
        }
        else
            return null;
    }
}
