package provided;

public class VarDecStmtNode implements JottTree {
    private String type;
    private String variableName;

    public VarDecStmtNode(String type, String variableName) {
        this.type = type;
        this.variableName = variableName;
    }

    @Override
    public String convertToJott() {
        return type + " " + variableName + ";";
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
