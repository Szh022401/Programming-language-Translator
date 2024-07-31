package provided;

import java.util.Objects;

/**
 * Node representing a relational expression
 */
public class RelationalExprNode implements IExprType {
    private IExprType leftExpr;      //left expression
    private Token operator;        //operator
    private IExprType rightExpr;     //right expression

    /**
     * Constructor for RelationalExprNode
     *
     * @param leftExpr left expression
     * @param operator operator of the expression
     * @param rightExpr right expression
     */
    public RelationalExprNode(IExprType leftExpr, Token operator, IExprType rightExpr) {
        this.leftExpr = leftExpr;
        this.operator = operator;
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
    public String convertToJava(String className) {
        return leftExpr.convertToJava(className) + " " + operator.getToken() + " " + rightExpr.convertToJava(className);
    }

    @Override
    public String convertToC() {
        return leftExpr.convertToC() + " " + operator.getToken() + " " + rightExpr.convertToC();
    }

    @Override
    public String convertToPython() {
        return leftExpr.convertToPython() + " " + operator.getToken() + " " + rightExpr.convertToPython();
    }


    @Override
    public boolean validateTree() {
        if (getType() == null){
            JottParser.reportError("Type Error -> " + leftExpr.getType() + " and " + rightExpr.getType(), operator, "Semantic");
            return false;
        }

        return leftExpr.validateTree() && rightExpr.validateTree();
    }

    public String getType()
    {
        if (Objects.equals(leftExpr.getType(), rightExpr.getType())){
            return leftExpr.getType();
        }
        else
            return null;
    }
}
