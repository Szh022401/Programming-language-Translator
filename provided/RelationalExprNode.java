package provided;

import java.util.ArrayList;
public class RelationalExprNode implements JottTree {
    private JottTree leftExpr;
    private String operator;
    private JottTree rightExpr;

    public RelationalExprNode(JottTree leftExpr, String operator, JottTree rightExpr) {
        this.leftExpr = leftExpr;
        this.operator = operator;
        this.rightExpr = rightExpr;
    }
    @Override
    public String convertToJott() {
        return leftExpr.convertToJott() + " " + operator + " " + rightExpr.convertToJott();
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
