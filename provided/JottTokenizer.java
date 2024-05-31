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
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){
		ArrayList<Token> tokens = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			int lineNum = 0;
			while ((line = reader.readLine()) != null) {
				lineNum++;
				tokens = getTokenize(line, filename, lineNum);
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filename);
		} catch (IOException e) {
			System.err.println("Error reading file: " + filename);
		}
		return tokens;
	}
	private static ArrayList<Token> getTokenize(String line, String filename, int lineNum) {
		ArrayList<Token> getToken = new ArrayList<>();

		return  getToken;
	}
}