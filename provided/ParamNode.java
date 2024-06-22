package provided;

/**
 * Node representing a parameter
 */
public class ParamNode implements JottTree {
    private String paramName;   //name of the parameter
    private String paramType;   //type of the parameter

    /**
     * Constructor for ParamNode
     *
     * @param paramName name of the parameter
     * @param paramType the type of the parameter
     */
    public ParamNode(String paramName, String paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
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
