package provided;

import java.util.ArrayList;

/**
 * Node representing the body of a function
 */
public class BodyNode implements JottTree {
    private ArrayList<JottTree> statements; //List of statements within the body

    /**
     * Constructor for BodyNode
     *
     * @param statements list of statements within the body of the function
     */
    public BodyNode(ArrayList<JottTree> statements) {
        this.statements = statements;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
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
