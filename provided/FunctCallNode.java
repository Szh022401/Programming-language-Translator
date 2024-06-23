package provided;

import java.util.ArrayList;

/**
 * Node representing a function call
 */
public class FunctCallNode implements JottTree {
    private Token functionName;            //Name of the function
    private ArrayList<JottTree> arguments;  //Arguments of the function

    /**
     * Constructor for FunctCallNode
     *
     * @param functionName name of the function
     * @param argument list of arguments for the function
     */
    public FunctCallNode(Token functionName, ArrayList<JottTree> arguments) {
        this.functionName = functionName.CloneToken();
        this.arguments = arguments;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        jottString.append("::").append(functionName.getToken()).append("[");
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                jottString.append(",");
            }
            jottString.append(arguments.get(i).convertToJott());
        }
        jottString.append("]");
        return jottString.toString();
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
