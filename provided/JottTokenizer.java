package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/
import java.io.*;
import java.util.ArrayList;

public class JottTokenizer {

	/**
	 * Takes in a filename and tokenizes that file into Tokens
	 * based on the rules of the Jott Language
	 *
	 * @param filename the name of the file to tokenize; can be relative or absolute path
	 * @return an ArrayList of Jott Tokens
	 */
	public static ArrayList<Token> tokenize(String filename) {
		ArrayList<Token> tokens = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			int lineNum = 0;
			while ((line = reader.readLine()) != null) {
				lineNum++;
				ArrayList<Token> lineTokens = getTokenize(line, filename, lineNum);
				if (lineTokens != null) {
					tokens.addAll(lineTokens);
				}else {
					return null;
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
		} catch (IOException e) {
			System.err.println("Error reading file: " + filename);
		}
		return tokens;
	}

	private static ArrayList<Token> getTokenize(String lines, String filename, int lineNum) {
		ArrayList<Token> tokens = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		char[] chars = lines.toCharArray();
		System.out.println(lines);

		for (int i = 0; i < chars.length; i++) {
			char currentChar = chars[i];
			if(lines.charAt(i) == '#'){
				break;
			}else if (Character.isWhitespace(currentChar)) {
				if (sb.length() > 0) {
					tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
					sb.setLength(0);
				}
				continue;
			}else if (Character.isDigit(currentChar)) {
				sb.append(currentChar);
			} else if (Character.isLetter(lines.charAt(i))) {
				sb.append(currentChar);
				while (i + 1 < lines.length() && Character.isLetterOrDigit(lines.charAt(i + 1)) && lines.charAt(i) != ' ') {
					i++;
					sb.append(lines.charAt(i));
				}
				if (sb.length() > 0) {
					tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
					sb.setLength(0);
				}
			}
			else if (currentChar == '.') {
				if (i + 1 < chars.length && (Character.isDigit(chars[i + 1]) )) {
					sb.append(currentChar);
				}else if(i - 1 > 0 && (Character.isDigit(chars[i - 1]))){
					sb.append(currentChar);
				}
				else {
					if (sb.length() > 0) {
						tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
						sb.setLength(0);
					}
					return null;
				}

			} else if (currentChar == '<' || currentChar == '>' || currentChar == '=' || currentChar == '!') {
				sb.append(currentChar);
				if (i + 1 < chars.length && chars[i + 1] == '=') {
					i++;
					sb.append(chars[i]);
				}else if (currentChar == '!' && !(i + 1 < chars.length && chars[i + 1] == '=')) {
					return null;
				}
				tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
				sb.setLength(0);
			}else if(currentChar == '"'){
				sb.append(currentChar);
				i++;
				while(i < lines.length() && lines.charAt(i) != '"'){
					sb.append(lines.charAt(i));
					i++;
				}
				if (i < lines.length() && lines.charAt(i) == '"') {
					sb.append(lines.charAt(i));
					i++;
					tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
				}else{
					return null;
				}

				sb.setLength(0);
			}else if(currentChar == ':'){
				sb.append(lines.charAt(i));
				i++;
				if(i < lines.length() && i == ':'){
					sb.append(lines.charAt(i));
				}
				tokens.add(new Token(sb.toString(), filename,lineNum,getType(sb.toString())));
			}
			else {
				if (sb.length() > 0) {
					tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
					sb.setLength(0);
				}
				if (!Character.isWhitespace(currentChar)) {
					tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, getType(String.valueOf(currentChar))));
				}

			}
		}

		if (sb.length() > 0) {
			tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
		}
		int test = 0;
		for (Token token : tokens) {
			test ++;
			System.out.println(test + token.getToken());
		}


		return tokens;
	}

	private static TokenType getType(String word) {

		if (word.matches("\\d+\\.\\d*") || word.matches("\\.\\d+") || word.matches("\\d+")) {
			return TokenType.NUMBER;
		}


		if (word.matches("\"[^\"]*\"")) {
			return TokenType.STRING;
		}


		if (word.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
			return TokenType.ID_KEYWORD;
		}


		switch (word) {
			case ";":
				return TokenType.SEMICOLON;
			case "{":
				return TokenType.L_BRACE;
			case "}":
				return TokenType.R_BRACE;
			case "[":
				return TokenType.L_BRACKET;
			case "]":
				return TokenType.R_BRACKET;
			case ",":
				return TokenType.COMMA;
			case ":":
				return TokenType.COLON;
			case "=":
				return TokenType.ASSIGN;
			case "\"":
				return TokenType.STRING;
			case "+":
			case "-":
			case "*":
			case "/":
				return TokenType.MATH_OP;
			case "<":
			case "<=":
			case ">":
			case ">=":
			case "!=":
			case "==":
				return TokenType.REL_OP;
			default:
				return null;
		}
	}
}