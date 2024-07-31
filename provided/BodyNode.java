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
            System.out.println(statement);
            sb.append("\t").append(statement.convertToJott()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String convertToJava(String className) {
        StringBuilder sb = new StringBuilder();
        for (JottTree statement : statements) {
            System.out.println(statement);
            sb.append("\t\t").append(statement.convertToJava(className)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder();
        for (JottTree statement : statements) {
            System.out.println(statement.convertToC());
            sb.append("\t").append(statement.convertToC()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder sb = new StringBuilder();
        for (JottTree statement : statements) {
//            System.out.println(statement);
            if(statement.convertToPython() != null){
                sb.append("\t").append(statement.convertToPython()).append("\n");
            }
        }
        return sb.toString();
    }


    @Override
    public boolean validateTree() {
        for (JottTree statement : statements) {
            if (!statement.validateTree()) return false;
        }
        return true;
    }

    public ArrayList<JottTree> getStatements(){
        return statements;
    }
}
