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
    public ParamNode(Token paramName, Token paramType, FunctDefNode Function) {
        this.paramName = paramName;
        this.paramType = paramType;
        Function.AllIds.add(new IdNode(paramName, paramType, Function));
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
    public String convertToJava(String className) {
        return convertTypeToJava(paramType.getToken()) + " " + paramName.getToken();
    }

    @Override
    public String convertToC() {

        return convertTypeToC(paramType.getToken()) + " " + paramName.getToken();
    }

    @Override
    public String convertToPython() {
        return paramName.getToken();
    }


    @Override
    public boolean validateTree() { return true; }

    public String getParamName() {
        return paramName.getToken();
    }

    public String getParamType() {
        return paramType.getToken();
    }
    private String convertTypeToJava(String jottType) {
        switch (jottType) {
            case "Integer":
                return "int";
            case "Double":
                return "double";
            case "String":
                return "String";
            case "Boolean":
                return "boolean";
            default:
                throw new IllegalArgumentException("Unknown Jott type: " + jottType);
        }
    }

    /**
     * Converts a Jott type to a C type
     * @param jottType the Jott type as a string
     * @return the corresponding C type as a string
     */
    private String convertTypeToC(String jottType) {
        switch (jottType) {
            case "Integer":
                return "int";
            case "Double":
                return "double";
            case "String":
                return "char*";
            case "Boolean":
                return "int";
            default:
                throw new IllegalArgumentException("Unknown Jott type: " + jottType);
        }
    }
}
