package provided;

import java.util.ArrayList;

/**
 * Node representing an ID, variable and the type of variable
 */
public class IdNode implements IExprType {
    private Token Name;   //name of the parameter
    private Token Type;   //type of the parameter

    /**
     * Constructor for IdNode
     *
     * @param paramName name of the ID
     * @param paramType the type of the IDl
     */
    public IdNode(Token paramName, Token paramType, FunctDefNode Function) {
        this.Name = paramName;
        this.Type = paramType;
        Function.AllIds.add(this);
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return Name.getToken() + ":" + Type.getToken();
    }
    @Override
    public String convertToJava(String className) {
        return convertTypeToJava(Type.getToken()) + " " + Name.getToken();
    }

    @Override
    public String convertToC() {
        return convertTypeToC(Type.getToken()) + " " + Name.getToken();
    }

    @Override
    public String convertToPython() {
        return Name.getToken();
    }


    @Override
    public boolean validateTree() {
        //todo verify this is not a reserved word
        return true; }

    public String getName() {
        return Name.getToken();
    }

    public Token getNameToken() {
        return Name;
    }

    public String getType() {
        return Type.getToken();
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
                return "int";  // C doesn't have a boolean type; typically 0 is false and non-zero is true
            default:
                throw new IllegalArgumentException("Unknown Jott type: " + jottType);
        }
    }

}
