package provided;

/**
 * Node representing a variable declaration statement
 */
public class VarDecStmtNode implements JottTree {
    private Token type;            //variable type
    private Token variableName;    //name of the variable
    private JottTree expression;    //expression to be assigned

    /**
     * Constructor for VarDecStmtNode
     *
     * @param type type of the variable
     * @param variableName name of the variable
     * @param expression expression that is assigned to the variable
     */
    public VarDecStmtNode(Token type, Token variableName, JottTree expression) {
        this.type = type.CloneToken();
        this.variableName = variableName.CloneToken();
        this.expression = expression;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        if (expression != null) {
            return type.getToken() + " " + variableName.getToken() + " = " + expression.convertToJott() + ";";
        } else {
            return type.getToken() + " " + variableName.getToken() + ";";
        }
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
