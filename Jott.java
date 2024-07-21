import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

public class Jott {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Jott <input.jott> <output.ext> <language>");
            System.exit(1);
        }

        String inputFilename = args[0];
        String outputFilename = args[1];
        String language = args[2];

        try {
            String input = new String(Files.readAllBytes(Paths.get(inputFilename)));

            if (input.trim().isEmpty()) {
                System.err.println("Input file is empty.");
                System.exit(1);
            }

            ArrayList<Token> tokens = JottTokenizer.tokenize(inputFilename);

            if (tokens == null || tokens.isEmpty()) {
                System.err.println("Tokenization failed or no tokens found.");
                System.exit(1);
            }

            // Print out tokens for debugging
//            for (Token token : tokens) {
//                System.out.println(token);
//            }

            JottTree parseTree = JottParser.parse(tokens);

            if (parseTree == null) {
                System.err.println("Parsing failed due to syntax errors.");
                System.exit(1);
            }

            if (!validateTree(parseTree)) {
                System.err.println("Semantic validation failed.");
                System.exit(1);
            }

            String output = "";
            switch (language.toLowerCase()) {
                case "jott":
                    output = parseTree.convertToJott();
                    break;
                case "python":
                    output = parseTree.convertToPython();
                    break;
                case "java":
                    output = parseTree.convertToJava(getClassName(outputFilename));
                    break;
                case "c":
                    output = parseTree.convertToC();
                    break;
                default:
                    System.err.println("Unsupported language: " + language);
                    System.exit(1);
            }

            Files.write(Paths.get(outputFilename), output.getBytes());
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error during compilation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getClassName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(0, dotIndex);
        }
        return filename;
    }

    private static Boolean validateTree(JottTree parseTree) {
        return parseTree.validateTree();
    }
}
