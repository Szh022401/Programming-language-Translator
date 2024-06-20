package provided;

public class ParamNode implements JottTree {
    private String paramName;
    private String paramType;

    public ParamNode(String paramName, String paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    @Override
    public String convertToJott() {
        return paramName + ":" + paramType;
    }
    @Override
    public String convertToJava(String className) { return null; }

    @Override
    public String convertToC() { return null; }

    @Override
    public String convertToPython() { return null; }

    @Override
    public boolean validateTree() { return true; }

    public String getParamName() {
        return paramName;
    }

    public String getParamType() {
        return paramType;
    }
}
