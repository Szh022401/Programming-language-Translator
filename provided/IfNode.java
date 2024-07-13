package provided;

import java.util.Objects;

/**
 * Node representing an if statement
 */
public class IfNode implements JottTree {
    private JottTree condition;      // Expression for the 'if' condition
    private JottTree ifBody;         // Block of statements executed if condition is true
    private JottTree elseBody;       // Optional block of statements executed if condition is false

    /**
     * Constructor for IfNode.
     *
     * @param condition The conditional expression determining whether to enter the 'if' or 'else' branch.
     * @param ifBody The body of the 'if' branch.
     * @param elseBody The body of the 'else' branch, which can be null if there is no 'else'.
     */
    public IfNode(JottTree condition, JottTree ifBody, JottTree elseBody) {
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        if (elseBody != null && !Objects.equals(elseBody.convertToJott(), "")) {
            return "If[" + condition.convertToJott() + "]{" + ifBody.convertToJott() + "}" + "Else{" + elseBody.convertToJott() + "}";
        }
        return "If[" + condition.convertToJott() + "]{" + ifBody.convertToJott() + "}";
    }

    @Override
    public String convertToJava(String className) { return null; }

    @Override
    public String convertToC() { return null; }

    @Override
    public String convertToPython() { return null; }

    @Override
    public boolean validateTree() { return condition.validateTree() && ifBody.validateTree() && elseBody.validateTree(); }

    public JottTree getIfBody() { return ifBody; }

    public JottTree getElseBody() { return elseBody; }
}