package provided;

public class AsmtStmtNode implements JottTree {
    private String variableName;
    private JottTree expression;

    public AsmtStmtNode(String variableName, JottTree expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

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
