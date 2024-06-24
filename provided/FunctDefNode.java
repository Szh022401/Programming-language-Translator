package provided;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Node representing a function definition
 */
public class FunctDefNode implements JottTree {
    private Token functionName;                //name of the function
    private Token returnType;                  //return type of the function
    private BodyNode body;                      //body of the function
    private ArrayList<ParamNode> parameters;     //parameters for the function
    private static ArrayList<FunctDefNode> Allfunctions = new ArrayList<>();
    /**
     * Constructor for FunctDefNode
     *
     * @param functionName name of the function
     * @param returnType the return type of the function
     * @param body the node representing the body of the function
     * @param parameters a list of parameters for the function
     */
    public FunctDefNode(Token functionName, Token returnType, BodyNode body, ArrayList<ParamNode> parameters) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.body = body;
        this.parameters = parameters;
        Allfunctions.add(this);
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        sb.append("Def ").append(functionName.getToken()).append("[");
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i).convertToJott());
            if (i < parameters.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]:").append(returnType.getToken()).append("{");
        sb.append(body.convertToJott());
        sb.append("}");
        return sb.toString();
    }
    @Override
    public String convertToJava(String className) { return null; }

    @Override
    public String convertToC() { return null; }

    @Override
    public String convertToPython() { return null; }

    @Override
    public boolean validateTree() {
        //validate body
        if (!body.validateTree())
            return false;
        //Check a return exists
        if (!Objects.equals(returnType.getToken(), "Void")){
            ReturnNode returnNode = findReturnNode();
            if (returnNode == null){
                JottParser.reportError("Return missing in function " + functionName.getToken(), functionName, "Semantic");
                return false;
            }
            //Check return value type matches
            if (!Objects.equals(getType(), returnNode.getType())){
                JottParser.reportError("Return Type Error in function "+functionName.getToken()+": " + getType() + " and " + returnNode.getType(), functionName, "Semantic");
                return false;
            }
        }
        return true;

    }

    public String getType() { return returnType.getToken(); }

    public Boolean verifyParams(FunctCallNode functionCall){
        if (functionCall.getArguments().size() < parameters.size()) {
            JottParser.reportError("Missing parameters for function call " + functionCall.getFunctionName().getToken(), functionCall.getFunctionName(), "Semantic");
            return false;
        }
        if (functionCall.getArguments().size() > parameters.size()) {
            JottParser.reportError("Too many parameters for function call " + functionCall.getFunctionName().getToken(), functionCall.getFunctionName(), "Semantic");
            return false;
        }

        for (int i = 0; i < functionCall.getArguments().size(); i++) {
            if (!Objects.equals(parameters.get(i).getParamType(), functionCall.getArguments().get(i).getType())){
                JottParser.reportError("Type Error -> " + parameters.get(i).getParamType() + " and " + functionCall.getArguments().get(i).getType(), functionCall.getFunctionName(), "Semantic");
                return false;
            }
        }
        return true;

    }
    public ParamNode getParam(String ParamName) {
        for (ParamNode p : parameters) {
            if(p.getParamName().equals(ParamName)){
                return p;
            }
        }
        return null;
    }
    public static FunctDefNode findFunction(String FunctionName){
        for (FunctDefNode f : Allfunctions) {
            if(f.functionName.getToken().equals(FunctionName)){
                return f;
            }
        }
        return null;
    }

    private ReturnNode TryFindReturnNodeInStatement(JottTree node ){
        if (node instanceof ReturnNode) {
            return (ReturnNode) node;
        }
        else if (node instanceof IfNode) {
            ReturnNode newNode = TryFindReturnNodeInStatement(((IfNode) node).getIfBody());

            if (newNode == null)
                newNode = TryFindReturnNodeInStatement(((IfNode) node).getElseBody());

            return newNode;
        }
        else
            return null;
    }

    private ReturnNode findReturnNode(){
        for (JottTree s : body.getStatements()) {
            ReturnNode node = TryFindReturnNodeInStatement(s);
            if (node != null)
                return node;
        }
        return null;
    }
}
