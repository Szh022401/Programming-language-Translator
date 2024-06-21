package provided;


public class ArithmeticExprNode implements JottTree {
    private final JottTree left;
    private final String operator;
    private final JottTree right;

    public ArithmeticExprNode(JottTree left, String operator, JottTree right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String convertToJott() {
        return left.convertToJott() + " " + operator + " " + right.convertToJott();
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
