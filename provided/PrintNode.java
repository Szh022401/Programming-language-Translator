package provided;

public class PrintNode implements JottTree {
    private JottTree expression;

    public PrintNode(JottTree expression) {
        this.expression = expression;
    }

    @Override
    public String convertToJott() {
        return "::print[" + expression.convertToJott() + "];";
    }

    @Override
    public String convertToJava(String className) { return null; }

    @Override
    public String convertToC() { return null; }

    @Override
    public String convertToPython() { return null; }

    @Override
    public boolean validateTree() { return true; }
}
