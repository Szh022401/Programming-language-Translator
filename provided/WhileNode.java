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
        StringBuilder sb = new StringBuilder();
        sb.append("While").append("[").append(condition.convertToJott()).append("]").append("{\n");
        for (JottTree statement : body.getStatements()){
            System.out.println(statement.convertToJott());
            sb.append("\t\t").append(statement.convertToJott()).append("\n");
        }


        return sb.toString();
    }

    @Override
    public String convertToJava(String className) {
        return "while (" + condition.convertToJava(className) + ") {\n" + body.convertToJava(className) + "\n}";
    }

    @Override
    public String convertToC() {
        return "while (" + condition.convertToC() + ") {\n" + body.convertToC() + "\n}";
    }

    @Override
    public String convertToPython() {
        return "while " + condition.convertToPython() + ":\n" + body.convertToPython();
    }


    @Override
    public boolean validateTree() { return condition.validateTree() && body.validateTree(); }

    public BodyNode getBody() {
        return body;
    }
}