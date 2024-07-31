package provided;

import java.util.ArrayList;

/**
 * Node representing the program
 */
public class ProgramNode implements JottTree {
    private ArrayList<JottTree> functions; //functions of the program
    private Token startToken;

    /**
     * Constructor for ProgramNode
     *
     * @param functions a list of functions within the program
     */
    public ProgramNode(Token startToken, ArrayList<JottTree> functions) {
        this.functions = functions;
        this.startToken = startToken;
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
        StringBuilder sb = new StringBuilder();
        sb.append("public class ").append(className).append(" {\n");
        for (JottTree function : functions) {
            sb.append(function.convertToJava(className)).append("\n");
        }
        sb.append("}");
        return sb.toString().trim();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder();
        sb.append("#include <stdio.h>\n#include <string.h>\n#include <stdlib.h>\n");
        for (JottTree function : functions) {
            sb.append(function.convertToC()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder sb = new StringBuilder();
        for (JottTree function : functions) {
            sb.append(function.convertToPython()).append("\n");
        }
        return sb.toString().trim();
    }


    @Override
    public boolean validateTree() {
        for (JottTree function : functions) {
            if (!function.validateTree()) {
                return false;
            }
        }
        if (FunctDefNode.findFunction("main") == null){
            JottParser.reportError("Missing main Function in program", startToken, "Semantic");
            return false;
        }
        //todo verify main has no parameters
        //todo verify no other main exists
        //todo verify main is a Void type
        //todat
        return true;
    }
}
