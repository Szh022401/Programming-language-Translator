package provided;

/**
 * Node representing a while loop
 */
public class WhileNode implements JottTree {
    private JottTree condition;     //conditional for the while loop
    private BodyNode body;          //body of the loop

    /**
     * Constructor for WhileNode
     *
     * @param condition the conditional for the while loop
     * @param body the node representing the body of the loop
     */
    public WhileNode(JottTree condition, BodyNode body) {
        this.condition = condition;
        this.body = body;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
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