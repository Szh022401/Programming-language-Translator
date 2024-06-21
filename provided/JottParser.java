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
        if (!isValidIdentifier(functionName)) {
            reportError("Expected valid function name", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '['", tokens.get(index[0]));
            return null;
        }
        index[0]++;
        ArrayList<JottTree> parameters = new ArrayList<>();
        while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
                reportError("Expected parameter name", tokens.get(index[0]));
                return null;
            }
            String paramName = tokens.get(index[0]).getToken();
            index[0]++;

            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.COLON) {
                reportError("Expected ':'", tokens.get(index[0]));
                return null;
            }
            index[0]++;

            if (index[0] >= tokens.size() || !isTypeKeyword(tokens.get(index[0]).getToken())) {
                reportError("Expected parameter type", tokens.get(index[0]));
                return null;
            }
            String paramType = tokens.get(index[0]).getToken();
            index[0]++;

            parameters.add(new ParamNode(paramName, paramType));

            if (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() == TokenType.COMMA) {
                index[0]++;
            } else {
                break;
            }
        }
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

        return new FunctDefNode(functionName, returnType, new BodyNode(body), parameters);
    }



    private static JottTree parseType(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            return null;
        }
        Token token = tokens.get(index[0]);
        if (isTypeKeyword(token.getToken())){
            index[0]++;
            return new TypeNode(token.getToken());
        }
        reportError("Expected a type", token);
        return null;
    }
    private static JottTree parseVarDec(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected a type", tokens.get(index[0]));
            return null;
        }
        String type = tokens.get(index[0]).getToken();
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected an identifier", tokens.get(index[0]));
            return null;
        }
        String varName = tokens.get(index[0]).getToken();
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        return new VarDecStmtNode(type, varName, null);
    }




    private static JottTree parseStatement(ArrayList<Token> tokens, int[] index) {
        if (index[0] < tokens.size()) {
            Token currentToken = tokens.get(index[0]);

            if (isTypeKeyword(currentToken.getToken())) {
                return parseVarDec(tokens, index);
            } else if (currentToken.getToken().equals("Return")) {
                return parseReturn(tokens, index);
            } else if (currentToken.getToken().equals("While")) {
                return parseWhile(tokens, index);
            } else if (currentToken.getToken().equals("If")) {
                return parseIf(tokens, index);
            } else if (currentToken.getTokenType() == TokenType.ID_KEYWORD) {
                return parseAssignment(tokens, index);
            } else if (currentToken.getTokenType() == TokenType.FC_HEADER) {
                return parsePrint(tokens, index);
            } else {
                reportError("Expected a statement", currentToken);
                return null;
            }
        } else {
            reportError("Unexpected end of input", null);
            return null;
        }
        //return null;
    }

    private static JottTree parseAssignment(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected an identifier", tokens.get(index[0]));
            return null;
        }
        String varName = tokens.get(index[0]).getToken();
        index[0]++;

        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("=")) {
            reportError("Expected '='", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        JottTree expression = parseExpression(tokens, index);
        if (expression == null) {
            return null;
        }

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        return new AssignmentNode(varName, expression);
    }

    private static JottTree parseIf(ArrayList<Token> tokens, int[] index) {
        // Check for 'If' keyword
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("If")) {
            reportError("Expected 'If' keyword", tokens.get(index[0]));
            return null;
        }
        index[0]++;
        // Check for opening bracket '['
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '[' after 'If'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        // Parse the condition expression
        JottTree condition = parseExpression(tokens, index);
        if (condition == null) {
            return null;
        }
        // Check for closing bracket ']'
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']' after condition in 'If'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        // Check for opening brace '{'
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
            reportError("Expected '{' to start 'If' body", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        ArrayList<JottTree> ifBody = new ArrayList<>();
        while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
            JottTree statement = parseStatement(tokens, index);
            if (statement == null) {
                return null;
            }
            ifBody.add(statement);
        }

        // Check for closing brace '}'
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
            reportError("Expected '}' to end 'If' body", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        ArrayList<JottTree> elseBody = new ArrayList<>();
        // Check for 'Else' keyword
        if (index[0] < tokens.size() && tokens.get(index[0]).getToken().equals("Else")) {
            index[0]++;

            // Check for opening brace '{' for Else
            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
                reportError("Expected '{' to start 'Else' body", tokens.get(index[0]));
                return null;
            }
            index[0]++;

            while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
                JottTree statement = parseStatement(tokens, index);
                if (statement == null) {
                    return null;
                }
                elseBody.add(statement);
            }

            // Check for closing brace '}'
            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
                reportError("Expected '}' to end 'Else' body", tokens.get(index[0]));
                return null;
            }
            index[0]++;
        }

        return new IfNode(condition, new BodyNode(ifBody), new BodyNode(elseBody));
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

        JottTree leftExpr = parsePrimary(tokens, index);
        if (leftExpr == null) {
            return null;
        }

        while (index[0] < tokens.size() && (isRelationalOperator(tokens.get(index[0])) || isArithmeticOperator(tokens.get(index[0])))) {
            Token operator = tokens.get(index[0]);
            index[0]++;
            JottTree rightExpr = parsePrimary(tokens, index);
            if (rightExpr == null) {
                return null;
            }
            if (isRelationalOperator(operator)) {
                leftExpr = new RelationalExprNode(leftExpr, operator.getToken(), rightExpr);
            } else if (isArithmeticOperator(operator)) {
                leftExpr = new ArithmeticExprNode(leftExpr, operator.getToken(), rightExpr);
            }
        }

        return leftExpr;
    }

    private static JottTree parsePrimary(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            reportError("Unexpected end of input in primary expression", null);
            return null;
        }

        Token token = tokens.get(index[0]);
        JottTree expr;


        if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.ID_KEYWORD || token.getTokenType() == TokenType.STRING) {
            expr = new ExpressionNode(token.getToken());
            index[0]++;
        }


        else if (token.getTokenType() == TokenType.FC_HEADER) {
            expr = parseFunctCall(tokens, index);
            if (expr == null) {
                return null;
            }
        } else {
            reportError("Expected a number, string, identifier, or function call", token);
            return null;
        }

        return expr;
    }
    private static JottTree parseReturn(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("Return")) {
            reportError("Expected 'Return' keyword", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        JottTree expression = parseExpression(tokens, index);
        if (expression == null) {
            return null;
        }

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
            reportError("Expected '}'", tokens.get(index[0]));
            return null;
        }

        return new ReturnNode(expression);
    }
    private static JottTree parseWhile(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("While")) {
            reportError("Expected 'While' keyword", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '[' after 'While'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        JottTree condition = parseExpression(tokens, index);
        if (condition == null) {
            return null;
        }

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']' after condition in 'While'", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
            reportError("Expected '{' to start 'While' body", tokens.get(index[0]));
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
            reportError("Expected '}' to end 'While' body", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        return new WhileNode(condition, new BodyNode(body));

    }
    private static JottTree parseFunctCall(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("::")) {
            reportError("Expected '::' for function call", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected function name after '::'", tokens.get(index[0]));
            return null;
        }
        String functionName = tokens.get(index[0]).getToken();
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '[' after function name", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        ArrayList<JottTree> args = new ArrayList<>();
        while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            JottTree arg = parseExpression(tokens, index);
            if (arg == null) {
                return null;
            }
            args.add(arg);

            if (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() == TokenType.COMMA) {
                index[0]++;
            } else {
                break;
            }
        }
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']' after function call arguments", tokens.get(index[0]));
            return null;
        }
        index[0]++;

        return new FunctCallNode(functionName, args);
    }


    private static boolean isTypeKeyword(String token) {
        return token.equals("Double") || token.equals("Integer") || token.equals("String") || token.equals("Boolean");
    }
    private static boolean isRelationalOperator(Token token) {
        return token.getTokenType() == TokenType.REL_OP;
    }
    private static boolean isArithmeticOperator(Token token) {
        return token.getTokenType() == TokenType.MATH_OP;
    }
    private static boolean isValidIdentifier(String name) {
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
    private static void reportError(String message, Token token) {
        System.err.println("Syntax Error: " + message);
        if (token != null) {
            System.err.println(token.getFilename() + ":" + token.getLineNum());
        }
    }
}
