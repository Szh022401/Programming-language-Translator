package provided;

import java.util.ArrayList;

public class FunctDefNode implements JottTree {
    private String functionName;
    private String returnType;
    private BodyNode body;

    public FunctDefNode(String functionName, String returnType, BodyNode body) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        sb.append("Def ").append(functionName).append("[]:").append(returnType).append("{");
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
