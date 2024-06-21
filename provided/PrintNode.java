package provided;

/**
 * Node representing a print statement
 */
public class PrintNode implements JottTree {
    private JottTree expression;    //expression to be printed

    /**
     * Constructor for PrintNode
     *
     * @param expression expression to be printed
     */
    public PrintNode(JottTree expression) {
        this.expression = expression;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
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
