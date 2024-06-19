package provided;

public class ExpressionNode implements JottTree {
    private String value;

    public ExpressionNode(String value) {
        this.value = value;
    }

    @Override
    public String convertToJott() {
        return value;
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
