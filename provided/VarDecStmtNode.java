package provided;

public class VarDecStmtNode implements JottTree {
    private String type;
    private String variableName;
    private JottTree expression;

    public VarDecStmtNode(String type, String variableName, JottTree expression) {
        this.type = type;
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public String convertToJott() {
        if (expression != null) {
            return type + " " + variableName + " = " + expression.convertToJott() + ";";
        } else {
            return type + " " + variableName + ";";
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
