package provided;

import java.util.Objects;

/**
 * Node representing an assignment
 */
public class AssignmentNode implements JottTree {
    private Token variableName;    //Name of the variable to be assigned a value
    private IExprType expression;    //Expression to be assigned
    private Boolean AddSemiColon = true;
    private FunctDefNode Function; //Parent Function

    /**
     * Constructor for AssignmentNode
     *
     * @param variableName Name of the variable to be assigned a value
     * @param expression Expression to be assigned
     */
    public AssignmentNode(Token variableName, IExprType expression, Boolean addSemiColon, FunctDefNode Function) {
        this.variableName = variableName;
        this.expression = expression;
        this.AddSemiColon = addSemiColon;
        this.Function = Function;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        String result =  variableName.getToken() + "=" + expression.convertToJott();
        if (AddSemiColon) {
            result = result + ";";
        }
        return result;
    }

    @Override
    public String convertToJava(String className) {
        String result = "\t\t" + variableName.getToken() + " = " + expression.convertToJava(className);
        if (AddSemiColon) {
            result += ";";
        }
        return result;
    }

    @Override
    public String convertToC() {
        String result = "\t" + variableName.getToken() + "=" + expression.convertToC();
        if (AddSemiColon) {
            result += ";";
        }
        return result;
    }

    @Override
    public String convertToPython() {
        return "\t" + variableName.getToken() + " = " + expression.convertToPython();
    }


    @Override
    public boolean validateTree() {
        IdNode _IdNode = Function.findId(variableName.getToken());

        if (_IdNode == null){
            JottParser.reportError("Cannot resolve symbol " + variableName.getToken(), variableName, "Semantic");
            return false;
        }

        if (!expression.validateTree()){
            return false;
        }
        if (!Objects.equals(_IdNode.getType(), expression.getType())) {
            JottParser.reportError("Type Error -> " + _IdNode.getType() + " and " + expression.getType(), variableName, "Semantic");
            return false;
        }
        return true;
    }
}
