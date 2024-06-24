package provided;

/**
 * Node representing a variable declaration statement
 */
public class VarDecStmtNode implements JottTree {
    private IdNode Id;            //variable type
    private JottTree expression;    //expression to be assigned

    /**
     * Constructor for VarDecStmtNode
     *
     * @param Id Name and Type of the variable
     * @param expression expression that is assigned to the variable
     */
    public VarDecStmtNode(IdNode Id, JottTree expression) {
        this.Id = Id;
        this.expression = expression;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        if (expression != null) {
            return Id.getType() + " " + Id.getName() + " = " + expression.convertToJott() + ";";
        } else {
            return Id.getType() + " " + Id.getName() + ";";
        }
    }
    @Override
    public String convertToJava(String className) {
        return null;
    }

    @Override
    public String convertToC() {
        return null;
    }

    @Override
    public String convertToPython() {
        return null;
    }

    @Override
    public boolean validateTree() {
        return true;
    }
}
