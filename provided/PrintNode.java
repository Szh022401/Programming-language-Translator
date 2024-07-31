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
    public String convertToJava(String className) {
        return "\t\tSystem.out.println(" + expression.convertToJava(className) + ");";
    }

    @Override
    public String convertToC() {
        String type = getType(expression);
        String formatSpecifier = type.equals("String") ? "%s" : "%d";
        if(type.equals("String")){
            return "\tprintf(\"" + formatSpecifier + "\\n\"," + expression.convertToC() + ");";
        }else {
            return "\tprintf(\"" + formatSpecifier + "\\n\", " + expression.convertToC() + ");";
        }

    }

    @Override
    public String convertToPython() {
        return "\tprint(" + expression.convertToPython() + ")";
    }


    @Override
    public boolean validateTree() { return expression.validateTree(); }

    private String getType(JottTree expr) {
        if (expr instanceof IExprType) {
            return ((IExprType) expr).getType();
        }
        throw new UnsupportedOperationException("Expression type not supported for printing");
    }
}
