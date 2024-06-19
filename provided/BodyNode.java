package provided;

import java.util.ArrayList;

public class BodyNode implements JottTree {
    private ArrayList<JottTree> statements;

    public BodyNode(ArrayList<JottTree> statements) {
        this.statements = statements;
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        for (JottTree statement : statements) {
            sb.append(statement.convertToJott()).append(" ");
        }
        return sb.toString().trim();
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
