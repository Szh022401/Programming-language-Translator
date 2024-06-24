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
        String returnValue;
        if (value.getTokenType() == TokenType.STRING)
            returnValue= "String";
        else if (value.getTokenType() == TokenType.NUMBER) {
            if (value.getToken().contains("."))
                returnValue= "Double";
            else
                returnValue= "Integer";
        }
        else if (value.getTokenType() == TokenType.ID_KEYWORD) {
            IdNode Id = IdNode.findId(value.getToken());
            if (Id != null){
                returnValue= Id.getType();
            }
            else
                returnValue= null;
        }
        else
            returnValue= null;
        /*if (returnValue != null)
            System.out.println(value.getLineNum() + ": Type -> " + returnValue);
        else
            System.out.println(value.getLineNum() + ": Type -> null");*/
        return returnValue;
    }
}
