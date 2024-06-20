package provided;

public class ReturnNode implements JottTree {
    private JottTree expression;

    public ReturnNode(JottTree expression) {
        this.expression = expression;
    }

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
        return true;
    }
}
