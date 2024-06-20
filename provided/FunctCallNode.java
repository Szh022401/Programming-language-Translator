package provided;

import java.util.ArrayList;

public class FunctCallNode implements JottTree {
    private String functionName;
    private ArrayList<JottTree> arguments;

    public FunctCallNode(String functionName, ArrayList<JottTree> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public String convertToJott() {
        StringBuilder jottString = new StringBuilder();
        jottString.append("::").append(functionName).append("[");
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
