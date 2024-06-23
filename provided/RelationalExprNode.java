package provided;

/**
 * Node representing a relational expression
 */
public class RelationalExprNode implements JottTree {
    private JottTree leftExpr;      //left expression
    private Token operator;        //operator
    private JottTree rightExpr;     //right expression

    /**
     * Constructor for RelationalExprNode
     *
     * @param leftExpr left expression
     * @param operator operator of the expression
     * @param rightExpr right expression
     */
    public RelationalExprNode(JottTree leftExpr, Token operator, JottTree rightExpr) {
        this.leftExpr = leftExpr;
        this.operator = operator.CloneToken();
        this.rightExpr = rightExpr;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return leftExpr.convertToJott() + " " + operator.getToken() + " " + rightExpr.convertToJott();
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
