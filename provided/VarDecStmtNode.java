package provided;

import java.util.Objects;

/**
 * Node representing a variable declaration statement
 */
public class VarDecStmtNode implements JottTree {
    private IdNode Id;            //variable type
    private IExprType expression;    //expression to be assigned

    /**
     * Constructor for VarDecStmtNode
     *
     * @param Id Name and Type of the variable
     * @param expression expression that is assigned to the variable
     */
    public VarDecStmtNode(IdNode Id, IExprType expression) {
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
        if (expression != null) {
            if (!Objects.equals(Id.getType(), expression.getType()))
            {
                JottParser.reportError("Type Error -> " + Id.getType() + " and " + expression.getType(), Id.getNameToken(), "Semantic");
                return false;
            }
            return expression.validateTree();
        }
        return true;
    }
}
