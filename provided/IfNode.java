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
            return "If[" + condition.convertToJott() + "]{\n\t" + ifBody.convertToJott() + "}" + "Else{\n\t" + elseBody.convertToJott() + "\n\t}";
        }
        return  "If[" + condition.convertToJott() + "]{\n\t" + ifBody.convertToJott() + "\t}";
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(condition.convertToJava(className)).append(") {\n");
        sb.append(ifBody.convertToJava(className)).append("\n}");
        if (elseBody != null && !Objects.equals(elseBody.convertToJava(className), "")) {
            sb.append(" else {\n").append(elseBody.convertToJava(className)).append("\n}");
        }
        return sb.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (").append(condition.convertToC()).append(") {\n");
        sb.append(ifBody.convertToC()).append("\n}");
        if (elseBody != null && !Objects.equals(elseBody.convertToC(), "")) {
            sb.append(" else {\n").append(elseBody.convertToC()).append("\n}");
        }
        return sb.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder sb = new StringBuilder();
        sb.append("if ").append(condition.convertToPython()).append(":\n");
        sb.append("\t").append(ifBody.convertToPython());
        if (elseBody != null && !Objects.equals(elseBody.convertToPython(), "")) {
            sb.append("\t").append("else:\n").append(elseBody.convertToPython()).append("\n");
        }
        return sb.toString();
    }


    @Override
    public boolean validateTree() { return condition.validateTree() && ifBody.validateTree() && elseBody.validateTree(); }

    public JottTree getIfBody() { return ifBody; }

    public JottTree getElseBody() { return elseBody; }
}