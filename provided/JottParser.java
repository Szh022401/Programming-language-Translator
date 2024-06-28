package provided;

/**
 * This class is responsible for paring Jott Tokens
 * into a Jott parse tree.
 *
 * @author Zehua Sun
 * @author Sean Hopkins
 * @author Adam Harnish
 * @author Jerry Lay
 * @author Beining Zhou
 * @author Joseph Esposito
 */

import java.util.ArrayList;

public class JottParser {
public static Boolean DoValidate;
    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens) {
        int[] index = {0};
        IdNode.ClearIdList();
        FunctDefNode.ClearFunctionsList();
        JottTree root = parseProgram(tokens, index);
        if (index[0] < tokens.size()) {
            reportError("Unexpected tokens at the end of the program", tokens.get(index[0]), "Syntax");
            return null;
        }

        if (root != null && DoValidate){
            if (!root.validateTree())
                return null;
        }

        return root;
    }

    /**
     * parses the program and creates the root node
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return program node
     */
    private static JottTree parseProgram(ArrayList<Token> tokens, int[] index) {
        ArrayList<JottTree> functions = new ArrayList<>();
        Token startToken = tokens.get(index[0]);
        while (index[0] < tokens.size()) {
            JottTree function = parseFunctDef(tokens, index);
            if (function == null) {
                return null;
            }
            functions.add(function);
        }
        return new ProgramNode(startToken, functions);
    }

    /**
     * parses a defined function in the program
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseFunctDef(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || (!tokens.get(index[0]).getToken().equals("Def"))) {
            reportError("Expected 'Def' keyword", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected function name", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token functionName = tokens.get(index[0]);
        if (!isValidIdentifier(functionName.getToken())) {
            reportError("Expected valid function name", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '['", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;
        ArrayList<ParamNode> parameters = new ArrayList<>();
        while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
                reportError("Expected parameter name", tokens.get(index[0]), "Syntax");
                return null;
            }
            Token paramName = tokens.get(index[0]);
            index[0]++;

            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.COLON) {
                reportError("Expected ':'", tokens.get(index[0]), "Syntax");
                return null;
            }
            index[0]++;

            if (index[0] >= tokens.size() || !isTypeKeyword(tokens.get(index[0]).getToken())) {
                reportError("Expected parameter type", tokens.get(index[0]), "Syntax");
                return null;
            }
            Token paramType = tokens.get(index[0]);
            index[0]++;

            parameters.add(new ParamNode(paramName, paramType));

            if (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() == TokenType.COMMA) {
                index[0]++;
            } else {
                break;
            }
        }
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']'", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.COLON) {
            reportError("Expected ':'", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected return type", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token returnType = tokens.get(index[0]);
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
            reportError("Expected '{'", tokens.get(index[0]), "Syntax");
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
            return null;
        }
        index[0]++;

        return new FunctDefNode(functionName, returnType, new BodyNode(body), parameters);
    }


    /**
     * parses a return type for a function
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseType(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            return null;
        }
        Token token = tokens.get(index[0]);
        if (isTypeKeyword(token.getToken())){
            index[0]++;
            return new TypeNode(token);
        }
        reportError("Expected a type", token, "Syntax");
        return null;
    }

    /**
     * parses a variable definition
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseVarDec(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected a type", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token type = tokens.get(index[0]);
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected an identifier", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token varName = tokens.get(index[0]);
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';' after variable declaration", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        return new VarDecStmtNode(new IdNode(varName, type), null);
    }

    private static JottTree parsePrintOrFuncCall(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("::")) {
            reportError("Expected '::' for print statement", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (tokens.get(index[0]).getToken().equals("print")) {
            index[0]--;
            return parsePrint(tokens, index);
        }
        else {
            index[0]--;
            return parseFunctCall(tokens, index);
        }

    }

    /**
     * parses a statement, such as: if, while, returning, etc
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseStatement(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            reportError("Unexpected end of input", null, "Syntax");
            return null;
        }


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
                return parsePrintOrFuncCall(tokens, index);
            } else {
                reportError("Expected a statement", currentToken, "Syntax");
                return null;
            }
        } else {
            reportError("Unexpected end of input", null, "Syntax");
            return null;
        }
        //return null;
    }

    /**
     * parses an assignment of a variable
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseAssignment(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected an identifier", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token varName = tokens.get(index[0]);
        index[0]++;

        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("=")) {
            reportError("Expected '='", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        IExprType expression = parseExpression(tokens, index);
        if (expression == null) {
            return null;
        }
        Boolean AddSemiColon=true;
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            index[0]--;
            //Adding this to account for a function call eating the semi color (this was a quick fix to stop from re-doing way more of the parser)                                                              //May want to remove
            if (tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON){
                index[0]++;
                reportError("Expected ';', after assignment", tokens.get(index[0]), "Syntax");
                return null;
            }
            AddSemiColon = false;
            index[0]++;
        }
        else{
            index[0]++;
        }

        return new AssignmentNode(varName, expression, AddSemiColon);
    }

    /**
     * parses an if statement
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseIf(ArrayList<Token> tokens, int[] index) {
        // Check for 'If' keyword
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("If")) {
            reportError("Expected 'If' keyword", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;
        // Check for opening bracket '['
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '[' after 'If'", tokens.get(index[0]), "Syntax");
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
            reportError("Expected ']' after condition in 'If'", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        // Check for opening brace '{'
        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
            reportError("Expected '{' to start 'If' body", tokens.get(index[0]), "Syntax");
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
            reportError("Expected '}' to end 'If' body", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        ArrayList<JottTree> elseBody = new ArrayList<>();
        // Check for 'Else' keyword
        if (index[0] < tokens.size() && tokens.get(index[0]).getToken().equals("Else")) {
            index[0]++;

            // Check for opening brace '{' for Else
            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
                reportError("Expected '{' to start 'Else' body", tokens.get(index[0]), "Syntax");
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
                reportError("Expected '}' to end 'Else' body", tokens.get(index[0]), "Syntax");
                return null;
            }
            index[0]++;
        }

        return new IfNode(condition, new BodyNode(ifBody), new BodyNode(elseBody));
    }

    /**
     * parses a print statement
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */

    private static JottTree parsePrint(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("::")) {
            reportError("Expected '::' for print statement", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("print")) {
            reportError("Expected 'print' keyword", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '['", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        JottTree expression = parseExpression(tokens, index);
        if (expression == null) {
            return null;
        }


        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']'", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';' after print command", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        return new PrintNode(expression);
    }

    /**
     * parses an expression
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static IExprType parseExpression(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            reportError("Unexpected end of input in expression", null, "Syntax");
            return null;
        }

        IExprType leftExpr = parsePrimary(tokens, index);
        if (leftExpr == null) {
            return null;
        }

        while (index[0] < tokens.size() && (isRelationalOperator(tokens.get(index[0])) || isArithmeticOperator(tokens.get(index[0])))) {
            Token operator = tokens.get(index[0]);
            index[0]++;
            IExprType rightExpr = parsePrimary(tokens, index);
            if (rightExpr == null) {
                return null;
            }
            if (isRelationalOperator(operator)) {
                leftExpr = new RelationalExprNode(leftExpr, operator, rightExpr);
            } else if (isArithmeticOperator(operator)) {
                leftExpr = new ArithmeticExprNode(leftExpr, operator, rightExpr);
            }
        }

        return leftExpr;
    }
    /**
     * parses primary expression
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static IExprType parsePrimary(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size()) {
            reportError("Unexpected end of input in primary expression", null, "Syntax");
            return null;
        }

        Token token = tokens.get(index[0]);
        IExprType expr;


        if (token.getTokenType() == TokenType.NUMBER || token.getTokenType() == TokenType.ID_KEYWORD || token.getTokenType() == TokenType.STRING) {
            expr = new ExpressionNode(token);
            index[0]++;
        }


        else if (token.getTokenType() == TokenType.FC_HEADER) {
            expr = parseFunctCall(tokens, index);
        } else {
            reportError("Expected a number, string, identifier, or function call", token, "Syntax");
            return null;
        }

        return expr;
    }

    /**
     * parses a return statement
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseReturn(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("Return")) {
            reportError("Expected 'Return' keyword", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token returnToken = tokens.get(index[0]);
        index[0]++;

        IExprType expression = parseExpression(tokens, index);
        if (expression == null) {
            return null;
        }

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
            reportError("Expected ';' after return statement", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (tokens.get(index[0]).getTokenType() != TokenType.R_BRACE) {
            reportError("Expected '}'", tokens.get(index[0]), "Syntax");
            return null;
        }

        return new ReturnNode(expression, returnToken);
    }

    /**
     * parses a while loop
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static JottTree parseWhile(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("While")) {
            reportError("Expected 'While' keyword", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '[' after 'While'", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        JottTree condition = parseExpression(tokens, index);
        if (condition == null) {
            return null;
        }

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            reportError("Expected ']' after condition in 'While'", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACE) {
            reportError("Expected '{' to start 'While' body", tokens.get(index[0]), "Syntax");
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
            reportError("Expected '}' to end 'While' body", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        return new WhileNode(condition, new BodyNode(body));

    }
    /**
     * parses a function statement (besides print)
     * @param tokens the ArrayList of Jott tokens to parse
     * @param index pointer to current location
     * @return updated program node
     */
    private static FunctCallNode parseFunctCall(ArrayList<Token> tokens, int[] index) {
        if (index[0] >= tokens.size() || !tokens.get(index[0]).getToken().equals("::")) {
            reportError("Expected '::' for function call", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.ID_KEYWORD) {
            reportError("Expected function name after '::'", tokens.get(index[0]), "Syntax");
            return null;
        }
        Token functionName = tokens.get(index[0]);
        index[0]++;

        if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.L_BRACKET) {
            reportError("Expected '[' after function name", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;

        ArrayList<IExprType> args = new ArrayList<>();
        while (index[0] < tokens.size() && tokens.get(index[0]).getTokenType() != TokenType.R_BRACKET) {
            IExprType arg = parseExpression(tokens, index);
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
            reportError("Expected ']' after function call arguments", tokens.get(index[0]), "Syntax");
            return null;
        }
        index[0]++;
        Token LastToken = tokens.get(index[0]);

        if (!(LastToken.getTokenType() == TokenType.REL_OP || LastToken.getTokenType() == TokenType.MATH_OP || LastToken.getTokenType() == TokenType.R_BRACKET)) {
            if (index[0] >= tokens.size() || tokens.get(index[0]).getTokenType() != TokenType.SEMICOLON) {
                reportError("Expected ';' after function call", tokens.get(index[0]), "Syntax");
                return null;
            }
            index[0]++;
        }

        return new FunctCallNode(functionName, args, LastToken.getTokenType() == TokenType.SEMICOLON);
    }

    /**
     * tests if the value is a type
     * @param token the ArrayList of Jott tokens to parse
     * @return boolean determining validity
     */
    private static boolean isTypeKeyword(String token) {
        return token.equals("Double") || token.equals("Integer") || token.equals("String") || token.equals("Boolean");
    }
    /**
     * tests if the value is a relational operator
     * @param token the ArrayList of Jott tokens to parse
     * @return boolean determining validity
     */
    private static boolean isRelationalOperator(Token token) {
        return token.getTokenType() == TokenType.REL_OP;
    }
    /**
     * tests if the value is an arithmetic operator
     * @param token the ArrayList of Jott tokens to parse
     * @return boolean determining validity
     */
    private static boolean isArithmeticOperator(Token token) {
        return token.getTokenType() == TokenType.MATH_OP;
    }
    /**
     * tests if the value is a proper string
     * @param name string of characters
     * @return boolean determining validity
     */
    private static boolean isValidIdentifier(String name) {
        return name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }

    /**
     * reports a syntax error depending on the reason of error
     * @param message message to be displayed
     * @param token location of error
     */
    public static void reportError(String message, Token token, String ErrorType) {
        //Syntax and Semantic
        System.err.println(ErrorType + " Error: " + message);
        if (token != null) {
            System.err.println(token.getFilename() + ":" + token.getLineNum());
        }
        else
            System.err.println(token.getFilename() + ":" + token.getLineNum());
        System.err.println("---------------");
    }
}
