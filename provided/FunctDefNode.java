package provided;

import java.util.ArrayList;

public class FunctDefNode implements JottTree {
    private String functionName;
    private String returnType;
    private BodyNode body;
    private ArrayList<JottTree> parameters;

    public FunctDefNode(String functionName, String returnType, BodyNode body, ArrayList<JottTree> parameters) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.body = body;
        this.parameters = parameters;
    }

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
