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
        sb.append("\t}");
        return sb.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder sb = new StringBuilder();
        sb.append("while(").append(condition.convertToJava(className)).append("){\n");
        for (JottTree statement : body.getStatements()){
            sb.append("\t\t").append(statement.convertToJava(className)).append("\n");
        }
        sb.append("\t}");
        return sb.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder();
        sb.append("while (").append(condition.convertToC()).append("){\n");
        for(JottTree statement : body.getStatements()){
            sb.append("\t\t").append(statement.convertToC()).append("\n");
        }
        sb.append("\t}");
        return sb.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder sb = new StringBuilder();
        sb.append("while (").append(condition.convertToPython()).append("){\n");
       for(JottTree statement : body.getStatements()){
           sb.append("\t\t").append(statement.convertToPython()).append("\n");
       }
       sb.append("\t}");
       return sb.toString();
    }


    @Override
    public boolean validateTree() { return condition.validateTree() && body.validateTree(); }

    public BodyNode getBody() {
        return body;
    }
}