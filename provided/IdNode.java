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
    public String convertToJava(String className) { return null; }

    @Override
    public String convertToC() { return null; }

    @Override
    public String convertToPython() { return null; }

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

}
