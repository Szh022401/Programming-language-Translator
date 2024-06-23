package provided;

/**
 * Node representing a type
 */
public class TypeNode implements JottTree {
    private Token type; //the type

    /**
     * Constructor for TypeNode
     *
     * @param type the type
     */
    public TypeNode(Token type) {
        this.type = type.CloneToken();
    }

    public String getType() {
        return type.getToken();
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        return type.getToken();
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
