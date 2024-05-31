<!-- This file contains provided code, testers, and test cases.

provided shouls be a top level package in your src directory
JottParserTester and JottTokenizerTester classes should be in a package call testers.

the testCases directories should be in the working directory of your project.  -->

# Project Description
This project contains a tokenizer for the Jott language, which reads a Jott file and 
parses its content into a list of tokens. The tokenizer operates based on a 
Deterministic Finite Automaton (DFA) provided in Section 4 of the project specification.

# Project Structure
- **src/provided**: Contains the provided code and the JottTokenizer class.
- **src/testers**: Contains the JottParserTester and JottTokenizerTester classes.
- **testCases**: Contains the test cases and should be located in the working directory of the project.

# Building and Running the Project

## 1. Clone the Project
```bash
git clone <repository-url>
cd <repository-directory>
```

## 2. Compile the Code

Ensure you are in the root directory of the project, then run the following command to compile the code:

```bash
javac provided/JottTokenizer.java testers/JottTokenizerTester.java
```

## 3. Run the Tests

You can use the following commands to run the tests:
```bash
java testers.JottTokenizerTester
```
