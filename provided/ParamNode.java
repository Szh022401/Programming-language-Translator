package provided;

/**
 * Node representing a parameter
 */
public class ParamNode implements JottTree {
    private Token paramName;   //name of the parameter
    private Token paramType;   //type of the parameter

    /**
     * Constructor for ParamNode
     *
     * @param paramName name of the parameter
     * @param paramType the type of the parameter
     */
    public ParamNode(Token paramName, Token paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return paramName.getToken() + ":" + paramType.getToken();
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
        return paramName.getToken();
    }

    public String getParamType() {
        return paramType.getToken();
    }
}
