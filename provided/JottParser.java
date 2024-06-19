package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author
 */

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        int[] index = {0};
        JottTree root = parseProgram(tokens, index);
        if (index[0] < tokens.size()) {
            reportError("Unexpected tokens at the end of the program", tokens.get(index[0]));
            return null;
        }
        return root;
    }

    private static JottTree parseProgram(ArrayList<Token> tokens, int[] index) {
        ArrayList<JottTree> functions = new ArrayList<>();
        while (index[0] < tokens.size()) {
            JottTree function = parseFunctDef(tokens, index);
            if (function == null) {
                return null;
            }
            functions.add(function);
        }
        return new ProgramNode(functions);
    }

    private static JottTree parseFunctDef(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("Def")) {
            reportError("Expected 'Def' keyword", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected function name", tokens.get(index[0]));
            return null;
        }
        String functionName = tokens.get(index[0]).getToken();
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '['", tokens.get(index[0]));
            return null;
        }
        index[0]++;


        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.COLON) {
            reportError("Expected ':'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected return type", tokens.get(index[0]));
            return null;
        }
        String returnType = tokens.get(index[0]).getToken();
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
            reportError("Expected '{'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        ArrayList<JottTree> body = new ArrayList<>();
        while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
            JottTree statement = parseStatement(tokens, index);
            if (statement == null) {
                return null;
            }
            body.add(statement);
        }

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
            reportError("Expected '}'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        return new FunctDefNode(functionName, returnType, new BodyNode(body));
    }

    private static JottTree parseStatement(ArrayList<Token> tokens, int[] index) {
        if (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() == TokenType.FC_HEADER) {
            return parsePrint(tokens, index);
        } else {
            reportError("Expected a statement", tokens.get(index[0]));
            return null;
        }
    }

    private static JottTree parsePrint(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("::")) {
            reportError("Expected '::' for print statement", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("print")) {
            reportError("Expected 'print' keyword", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '['", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        JottTree expression = parseExpression(tokens, index);
        if (expression == null) {
            return null;
        }


        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        return new PrintNode(expression);
    }

    private static JottTree parseExpression(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            reportError("Unexpected end of input in expression", null);
            return null;
        }

        Token token = tokens.get(index[0]);
        if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.STRING) {
            index[0]++;
            return new ExpressionNode(token.getToken());
        } else {
            reportError("Expected a number or string", token);
            return null;
        }
    }

    private static void reportError(String message, Token token) {
        System.err.println("Syntax Error: " + message);
        if (token != null) {
            System.err.println(token.getFilename() + ":" + token.getLineNum());
        }
    }
}
