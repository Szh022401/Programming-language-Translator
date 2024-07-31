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
            return  Id.getType() + " " + Id.getName() + "=" + expression.convertToJott() + ";";
        } else {
            return  Id.getType() + " " + Id.getName() + ";";
        }
    }
    @Override
    public String convertToJava(String className) {
        if (expression != null) {
            return convertTypeToJava(Id.getType()) + " " + Id.getName() + "=" + expression.convertToJava(className) + ";";
        } else {
            return convertTypeToJava(Id.getType()) + " " + Id.getName() + ";";
        }
    }

    @Override
    public String convertToC() {
        if (expression != null) {
            return  convertTypeToC(Id.getType()) + " " + Id.getName() + " = " + expression.convertToC() + ";";
        } else {
            return  convertTypeToC(Id.getType()) + " " + Id.getName() + ";";
        }
    }

    @Override
    public String convertToPython() {
        if (expression != null) {
            return  Id.getName() + " = " + expression.convertToPython();
        } else {
            return null;
        }
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
     *
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
