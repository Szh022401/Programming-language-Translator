package provided;

/**
 * Node representing a return statement
 */
public class ReturnNode implements IExprType {
    private IExprType expression; //the expression to be returned
    private Token ReturnToken;
    /**
     * Constructor for ReturnNode
     *
     * @param expression expression of the return statement
     */
    public ReturnNode(IExprType expression, Token ReturnToken) {
        this.expression = expression;
        this.ReturnToken = ReturnToken;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return "Return " + expression.convertToJott() + ";";
    }
    @Override
    public String convertToJava(String className) {
        // Implementation for converting to Java code
        return null;
    }

    @Override
    public String convertToC() {
        // Implementation for converting to C code
        return null;
    }

    @Override
    public String convertToPython() {
        // Implementation for converting to Python code
        return null;
    }

    @Override
    public boolean validateTree() {
        // Validate the program node
        return expression.validateTree();
    }

    @Override
    public String getType() {
        return expression.getType();
    }

    public Token getReturnToken() {
        return ReturnToken;
    }
}
