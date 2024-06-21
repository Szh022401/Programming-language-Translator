package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Zehua Sun
 * @author Sean Hopkins
 * @author Adam Harnish
 * @author Jerry Lay
 * @author Beining Zhou
 * @author Joseph Esposito
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

	/**
	 * Parses a single line of Jott code and produces a list of tokens.
	 * Comments and unsupported syntax cause the parsing of the line to stop.
	 *
	 * @param lines the line of text to parse
	 * @param filename the name of the file being parsed (for error reporting)
	 * @param lineNum the line number being parsed (for error reporting)
	 * @return a list of tokens from the line; returns null if a parsing error occurs
	 */
	private static ArrayList<Token> getTokenize(String lines, String filename, int lineNum) {
		ArrayList<Token> tokens = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		char[] chars = lines.toCharArray();
		//System.out.println(lines);

		for (int i = 0; i < chars.length; i++) {
			char currentChar = chars[i];
			// Stop processing the line on encountering a comment symbol.
			if(lines.charAt(i) == '#'){
				break;
			}
			// Skip processing whitespace but finalize any tokens being built.
			else if (Character.isWhitespace(currentChar)) {
				if (sb.length() > 0) {
					tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
					sb.setLength(0);
				}
				continue;
			}
			// Append numeric characters to the current token
			else if (Character.isDigit(currentChar)) {
				sb.append(currentChar);
//				while (i + 1 < chars.length && (Character.isLetterOrDigit(chars[i + 1]))) {
//					i++;
//					sb.append(chars[i]);
//				}
//				tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
//				sb.setLength(0);

			}// Build token strings for identifiers or keywords.
			else if (Character.isLetter(lines.charAt(i))) {
				sb.append(currentChar);
				// Include subsequent alphanumeric characters in the identifier.
				while (i + 1 < lines.length() && Character.isLetterOrDigit(lines.charAt(i + 1)) && lines.charAt(i) != ' ') {
					i++;
					sb.append(lines.charAt(i));
				}
				// Handle specific keywords like While, If, and Else.
				String keyword = sb.toString();
				if (keyword.equals("While") || keyword.equals("If") || keyword.equals("Else") || keyword.equals("Elseif")) {
					tokens.add(new Token(keyword, filename, lineNum, getType(keyword)));
				} else {
					tokens.add(new Token(keyword, filename, lineNum, getType(keyword)));
				}
				sb.setLength(0);
			}
			// Handle period used in floating point numbers or ellipsis.
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
			}
			// Process operators, particularly looking for combinations like '!='.
			else if (currentChar == '<' || currentChar == '>' || currentChar == '=' || currentChar == '!') {
				sb.append(currentChar);
				if (i + 1 < chars.length && chars[i + 1] == '=') {
					i++;
					sb.append(chars[i]);
				}
				tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
				sb.setLength(0);
			}
			// Process and validate string literals enclosed in quotes.
			else if(currentChar == '"'){
				sb.append(currentChar);
				i++;
				while(i < lines.length() && lines.charAt(i) != '"'){
					sb.append(lines.charAt(i));
					i++;
				}
				if (i < lines.length() && lines.charAt(i) == '"') {
					sb.append(lines.charAt(i));
					tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
				}else{
					return null;
				}

				sb.setLength(0);
			}
			// Handle colon and possible double colon '::'.
			else if(currentChar == ':') {
				if (i + 1 < chars.length && chars[i + 1] == ':') {
					sb.append(currentChar);
					i++;
					sb.append(chars[i]);
					tokens.add(new Token(sb.toString(), filename, lineNum, TokenType.FC_HEADER));
					sb.setLength(0);
				} else {
					tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, getType(String.valueOf(currentChar))));
				}
			}
			else if (currentChar == '[') {
				tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, TokenType.L_BRACKET));
				i++;
				while (i < chars.length && chars[i] != ']') {
					currentChar = chars[i];
					if (Character.isWhitespace(currentChar)) {
						i++;
						continue;
					}
					if (Character.isLetterOrDigit(currentChar) || currentChar == '"' || currentChar == '<' || currentChar == '>' || currentChar == '=' || currentChar == '!' || currentChar == ':' || currentChar == '.') {
						sb.append(currentChar);
						if (currentChar == '"') {
							i++;
							while (i < chars.length && chars[i] != '"') {
								sb.append(chars[i]);
								i++;
							}
							if (i < chars.length && chars[i] == '"') {
								sb.append(chars[i]);
								tokens.add(new Token(sb.toString(), filename, lineNum, TokenType.STRING));
								sb.setLength(0);
							} else {
								return null;
							}
						} else if (currentChar == ':') {
							if (i + 1 < chars.length && chars[i + 1] == ':') {
								sb.append(chars[++i]);
								tokens.add(new Token(sb.toString(), filename, lineNum, TokenType.FC_HEADER));
								sb.setLength(0);
							} else {
								tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, TokenType.COLON));
								sb.setLength(0);
								i++;

								while (i < chars.length && Character.isLetter(chars[i])) {
									sb.append(chars[i]);
									i++;
								}
								if (sb.length() > 0) {
									tokens.add(new Token(sb.toString(), filename, lineNum, getType(sb.toString())));
									sb.setLength(0);
								}
								i--;
							}
						} else if ((currentChar == '=' || currentChar == '!' || currentChar == '<' || currentChar == '>') && i + 1 < chars.length && chars[i + 1] == '=') {
							sb.append(chars[++i]);
							tokens.add(new Token(sb.toString(), filename, lineNum, TokenType.REL_OP));
							sb.setLength(0);
						}
						else if (currentChar == '<' || currentChar == '>') {
							tokens.add(new Token(sb.toString(), filename, lineNum, TokenType.REL_OP));
							sb.setLength(0);
						}
						else if (Character.isDigit(currentChar) || currentChar == '.') {
							boolean isDecimal = currentChar == '.';
							while (i + 1 < chars.length && (Character.isDigit(chars[i + 1]) || (!isDecimal && chars[i + 1] == '.'))) {
								if (chars[i + 1] == '.') {
									isDecimal = true;
								}
								sb.append(chars[++i]);
							}
							tokens.add(new Token(sb.toString(), filename, lineNum, TokenType.NUMBER));
							sb.setLength(0);
						} else {
							while (i + 1 < chars.length && (Character.isLetterOrDigit(chars[i + 1]) || chars[i + 1] == '.')) {
								sb.append(chars[++i]);
							}
							TokenType type = getType(sb.toString());
							if (type != null) {
								tokens.add(new Token(sb.toString(), filename, lineNum, type));
							} else {
								return null;
							}
							sb.setLength(0);
						}
					} else if (currentChar == ':') {
						tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, TokenType.COLON));
						sb.setLength(0);
					} else if (currentChar == ',') {
						tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, TokenType.COMMA));
						sb.setLength(0);
					} else if (currentChar == '[') {
						tokens.add(new Token(String.valueOf(currentChar), filename, lineNum, TokenType.L_BRACKET));
						sb.setLength(0);
						i++;
						continue;
					} else {
						return null;
					}
					i++;
				}
				if (i < chars.length && chars[i] == ']') {
					tokens.add(new Token(String.valueOf(chars[i]), filename, lineNum, TokenType.R_BRACKET));
				} else {
					return null;
				}
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
		for (Token token : tokens) {
			System.out.println(token.getToken() + token.getTokenType());
		}


		return tokens;
	}
	/**
	 * Determines the type of a token based on its textual representation.
	 *
	 * @param word the string token to classify
	 * @return the TokenType of the token
	 */
	private static TokenType getType(String word) {

		if (word.matches("\\d+\\.\\d*") || word.matches("\\.\\d+") || word.matches("\\d+")) {
			return TokenType.NUMBER;
		}


		if (word.matches("\"[^\"]*\"")) {
			return TokenType.STRING;
		}


		if (word.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
			switch (word) {
				case "While":
				case "If":
				case "Else":
				case "Elseif":
					return TokenType.ID_KEYWORD;
				default:
					return TokenType.ID_KEYWORD;
			}
		}
		if (word.matches("\\d+[a-zA-Z_]+[a-zA-Z0-9_]*")) {
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
