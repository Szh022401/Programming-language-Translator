package provided;

import java.util.ArrayList;

/**
 * Node representing a function call
 */
public class FunctCallNode implements IExprType {
    private Token functionName;            //Name of the function
    private ArrayList<IExprType> arguments;  //Arguments of the function

    /**
     * Constructor for FunctCallNode
     *
     * @param functionName name of the function
     * @param arguments list of arguments for the function
     */
    public FunctCallNode(Token functionName, ArrayList<IExprType> arguments) {
        this.functionName = functionName;
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
    public boolean validateTree() {
        FunctDefNode Func = FunctDefNode.findFunction(functionName.getToken());
        if (Func == null) {
            JottParser.reportError("Cannot resolve function " + functionName.getToken(), functionName, "Semantic");
            return false;
        }
        for (IExprType arg : arguments) {
            if (!arg.validateTree()) {
                return false;
            }
        }
        return !Func.verifyParams(this);

    }

    public ArrayList<IExprType> getArguments() {
        return arguments;
    }

    public Token getFunctionName() {return functionName;}

    public String getType()
    {
        FunctDefNode func = FunctDefNode.findFunction(functionName.getToken());
        if (func != null)
            return func.getType();
        else
            return null;
    }
}
