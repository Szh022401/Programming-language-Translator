package provided;

import java.util.ArrayList;

/**
 * Node representing a function call
 */
public class FunctCallNode implements IExprType {
    private Token functionName;            //Name of the function
    private ArrayList<IExprType> arguments;  //Arguments of the function
    private Boolean AddSemiColon = false;

    /**
     * Constructor for FunctCallNode
     *
     * @param functionName name of the function
     * @param arguments list of arguments for the function
     */
    public FunctCallNode(Token functionName, ArrayList<IExprType> arguments, Boolean addSemiColon) {
        this.functionName = functionName;
        this.arguments = arguments;
        this.AddSemiColon = addSemiColon;
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
        if (AddSemiColon) {
            jottString.append(";");
        }
        return jottString.toString();
        }

    @Override
    public String convertToJava(String className) {
        StringBuilder javaString = new StringBuilder();
        javaString.append(functionName.getToken()).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                javaString.append(", ");
            }
            javaString.append(arguments.get(i).convertToJava(className));
        }
        javaString.append(")");
        if (AddSemiColon) {
            javaString.append(";");
        }
        return javaString.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder cString = new StringBuilder();
        cString.append(functionName.getToken()).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                cString.append(",");
            }
            cString.append(arguments.get(i).convertToC());
        }
        cString.append(")");
        if (AddSemiColon) {
            cString.append(";");
        }
        return cString.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder pythonString = new StringBuilder();
        pythonString.append(functionName.getToken()).append("(");
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                pythonString.append(", ");
            }
            pythonString.append(arguments.get(i).convertToPython());
        }
        pythonString.append(")");

        return pythonString.toString();
    }


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
        return Func.verifyParams(this);

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
