package provided;

/**
 * Node representing an assignment
 */
public class AssignmentNode implements JottTree {
    private String variableName;    //Name of the variable to be assigned a value
    private JottTree expression;    //Expression to be assigned

    /**
     * Constructor for AssignmentNode
     *
     * @param variableName Name of the variable to be assigned a value
     * @param expression Expression to be assigned
     */
    public AssignmentNode(String variableName, JottTree expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return variableName + " = " + expression.convertToJott() + ";";
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
