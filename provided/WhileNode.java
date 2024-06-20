package provided;

import java.util.ArrayList;

public class WhileNode implements JottTree {
    private JottTree condition;
    private BodyNode body;

    public WhileNode(JottTree condition, BodyNode body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String convertToJott() {
        return "While[" + condition.convertToJott() + "]{" + body.convertToJott() + "}";
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