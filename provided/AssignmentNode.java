package provided;

import java.util.Objects;

/**
 * Node representing an assignment
 */
public class AssignmentNode implements JottTree {
    private Token variableName;    //Name of the variable to be assigned a value
    private IExprType expression;    //Expression to be assigned
    private Boolean AddSemiColon = true;

    /**
     * Constructor for AssignmentNode
     *
     * @param variableName Name of the variable to be assigned a value
     * @param expression Expression to be assigned
     */
    public AssignmentNode(Token variableName, IExprType expression, Boolean addSemiColon) {
        this.variableName = variableName;
        this.expression = expression;
        this.AddSemiColon = addSemiColon;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        String result = variableName.getToken() + " = " + expression.convertToJott();
        if (AddSemiColon) {
            result = result + "; ";
        }
        return result;
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
        IdNode _IdNode = IdNode.findId(variableName.getToken());

        if (_IdNode == null){
            JottParser.reportError("Cannot resolve symbol " + variableName.getToken(), variableName, "Semantic");
            return false;
        }
        if (!Objects.equals(_IdNode.getType(), expression.getType())) {
            JottParser.reportError("Type Error -> " + _IdNode.getType() + " and " + expression.getType(), variableName, "Semantic");
            return false;
        }
        return expression.validateTree();
    }
}
