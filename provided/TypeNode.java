package provided;

public class TypeNode implements JottTree {
    private String type;

    public TypeNode(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String convertToJott() {
        return type;
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
