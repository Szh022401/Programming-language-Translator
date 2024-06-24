package provided;

import java.util.ArrayList;

/**
 * Node representing the program
 */
public class ProgramNode implements JottTree {
    private ArrayList<JottTree> functions; //functions of the program

    /**
     * Constructor for ProgramNode
     *
     * @param functions a list of functions within the program
     */
    public ProgramNode(ArrayList<JottTree> functions) {
        this.functions = functions;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        for (JottTree function : functions) {
            sb.append(function.convertToJott()).append("\n");
        }
        return sb.toString().trim();
    }

    @Override
    public String convertToJava(String className) {
        // Implementation for converting to Java code
        return null;
    }

    @Override
    public String convertToC() {
        // Implementation for converting to C code
        return null;
    }

    @Override
    public String convertToPython() {
        // Implementation for converting to Python code
        return null;
    }

    @Override
    public boolean validateTree() {
        for (JottTree function : functions) {
            if (!function.validateTree()) {
                return false;
            }
        }
        return true;
    }
}
