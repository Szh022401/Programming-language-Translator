package provided;

import java.util.ArrayList;

/**
 * Node representing a function definition
 */
public class FunctDefNode implements JottTree {
    private String functionName;                //name of the function
    private String returnType;                  //return type of the function
    private BodyNode body;                      //body of the function
    private ArrayList<JottTree> parameters;     //parameters for the function

    /**
     * Constructor for FunctDefNode
     *
     * @param functionName name of the function
     * @param returnType the return type of the function
     * @param BodyNode the node representing the body of the function
     * @param parameters a list of parameters for the function
     */
    public FunctDefNode(String functionName, String returnType, BodyNode body, ArrayList<JottTree> parameters) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.body = body;
        this.parameters = parameters;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        sb.append("Def ").append(functionName).append("[");
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i).convertToJott());
            if (i < parameters.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]:").append(returnType).append("{");
        sb.append(body.convertToJott());
        sb.append("}");
        return sb.toString();
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
