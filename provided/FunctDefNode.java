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
    private Token defType;
    public ArrayList<IdNode> AllIds = new ArrayList<>();
    private static ArrayList<FunctDefNode> Allfunctions = new ArrayList<>();
    /**
     * Constructor for FunctDefNode
     *
     * @param functionName name of the function
     * @param returnType the return type of the function
     * @param body the node representing the body of the function
     * @param parameters a list of parameters for the function
     */
    public FunctDefNode(Token defType,Token functionName, Token returnType, BodyNode body, ArrayList<ParamNode> parameters) {
        this.defType = defType;
        this.functionName = functionName;
        this.returnType = returnType;
        this.body = body;
        this.parameters = parameters;
        Allfunctions.add(this);
    }
    public FunctDefNode() {
        Allfunctions.add(this);
    }
    public void setVariables(Token defType,Token functionName, Token returnType, BodyNode body, ArrayList<ParamNode> parameters) {
        this.defType = defType;
        this.functionName = functionName;
        this.returnType = returnType;
        this.body = body;
        this.parameters = parameters;
    }

    /**
     * @brief converts the node into Jott language
     * @return the Jott language as a string
     */
    @Override
    public String convertToJott() {
        StringBuilder sb = new StringBuilder();
        String defKeyword = defType.getToken();
        sb.append(defKeyword).append(" ").append(functionName.getToken()).append("[");
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i).convertToJott());
            if (i < parameters.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]:").append(returnType.getToken()).append("{\n");
        sb.append(body.convertToJott());
        sb.append("}");
        return sb.toString();
    }
    @Override
    public String convertToJava(String className) {
        StringBuilder sb = new StringBuilder();
        if(functionName.getToken().equals("main")){
            sb.append("\tpublic static void ").append(functionName.getToken()).append("(").append("String args[])");
            for (int i = 0; i < parameters.size(); i++) {
                sb.append(parameters.get(i).convertToJava(className));
                if (i < parameters.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("{\n");
            sb.append(body.convertToJava(className));
            sb.append("\t}");
        }else
        {
            sb.append("\tstatic ").append(returnType.getToken().toLowerCase()).append(" ").append(functionName.getToken());
            sb.append("(");
            for (int i = 0; i < parameters.size(); i++) {
                sb.append(parameters.get(i).convertToJava(className));
                if (i < parameters.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append("){\n");
            sb.append(body.convertToJava(className));
            sb.append("\t}");
        }
        return sb.toString();
    }

    @Override
    public String convertToC() {
        StringBuilder sb = new StringBuilder();
        if(functionName.getToken().equals("main")){
            sb.append("int ").append(functionName.getToken()).append("(").append(returnType.getToken().toLowerCase()).append(")");
            for (int i = 0; i < parameters.size(); i++) {
                sb.append(parameters.get(i).convertToC());
                if (i < parameters.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("{\n");
            sb.append(body.convertToC());
            sb.append("    return 1;");
            sb.append("\n}");
        }else
        {
            String type = returnType.getToken();
            if(returnType.getToken().equals("String")){
                    type = "char *";
            }
            sb.append(type.toLowerCase()).append(" ").append(functionName.getToken());
            sb.append("(");
            for (int i = 0; i < parameters.size(); i++) {
                sb.append(parameters.get(i).convertToC());
                if (i < parameters.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append("){\n");
            sb.append(body.convertToC());
            sb.append("}");
        }


        return sb.toString();
    }

    @Override
    public String convertToPython() {
        StringBuilder sb = new StringBuilder();
        sb.append("def ").append(functionName.getToken()).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i).convertToPython());
            if (i < parameters.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("):\n");
        sb.append(body.convertToPython());
        sb.append("\n");
        sb.append("main()");
        return sb.toString();
    }


    @Override
    public boolean validateTree() {
        //validate body
        if (!body.validateTree())
            return false;
        //Check a return exists
        if (!Objects.equals(returnType.getToken(), "Void")){
            ArrayList<ReturnNode> returnNodes = findReturnNodes();
            if (returnNodes.isEmpty()){
                JottParser.reportError("Missing return statement in function " + functionName.getToken(), functionName, "Semantic");
                return false;
            }
            //Check return value type matches
            for (ReturnNode returnNode : returnNodes) {
                if (!Objects.equals(getType(), returnNode.getType())){
                    JottParser.reportError("Return Type Error in function "+functionName.getToken()+" -> " + getType() + " and " + returnNode.getType(), returnNode.getReturnToken(), "Semantic");
                    return false;
                }
            }

        }
        else {
            if (!findReturnNodes().isEmpty()){
                JottParser.reportError("Void type function, return not allowed" + functionName.getToken(), functionName, "Semantic");
                return false;
            }
        }
        //todo verify the function does not share a name with a built in function (print and concat)
        return true;

    }

    public String getType() {


        return returnType.getToken();
    }

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

    private void TryFindReturnNodesInStatement(JottTree node, ArrayList<ReturnNode> listOfReturnNodes, Boolean RestrictIfStatements, Boolean checkAllNodes){
        if (node instanceof ReturnNode) {
            listOfReturnNodes.add((ReturnNode) node);
        }
        else if (node instanceof IfNode && (RestrictIfStatements || checkAllNodes)) {
            if (RestrictIfStatements){
                ArrayList<ReturnNode> IfReturns = new ArrayList<>();

                TryFindReturnNodesInStatement(((IfNode) node).getIfBody(), IfReturns, true, false);

                if (!IfReturns.isEmpty()){
                    int Before = IfReturns.size();
                    TryFindReturnNodesInStatement(((IfNode) node).getElseBody(), IfReturns, true, false);
                    int After = IfReturns.size();

                    if (After > Before){
                        listOfReturnNodes.addAll(IfReturns);
                    }
                }
            }
            else {
                TryFindReturnNodesInStatement(((IfNode) node).getIfBody(), listOfReturnNodes, false, true);
                TryFindReturnNodesInStatement(((IfNode) node).getElseBody(), listOfReturnNodes, false, true);
            }

        }
        else if (node instanceof WhileNode && checkAllNodes) {
            TryFindReturnNodesInStatement(((WhileNode) node).getBody(), listOfReturnNodes, RestrictIfStatements, true);
        }
        else if (node instanceof  BodyNode){
            for (JottTree s : ((BodyNode) node).getStatements()) {
                TryFindReturnNodesInStatement(s, listOfReturnNodes, RestrictIfStatements, checkAllNodes);
            }
        }
    }

    public IdNode findId(String Name){
        for (IdNode f : AllIds) {
            if(f.getName().equals(Name)){
                return f;
            }
        }
        return null;
    }

    private ArrayList<ReturnNode> findReturnNodes(){
        ArrayList<ReturnNode> result = new ArrayList<>();

        TryFindReturnNodesInStatement(body, result, false, false);
        if (result.isEmpty()){
            TryFindReturnNodesInStatement(body, result, true, false);
            if (!result.isEmpty()){
                result.clear();
                TryFindReturnNodesInStatement(body, result, false, true);
            }
        }
        else{
            result.clear();
            TryFindReturnNodesInStatement(body, result, false, true);
        }

        return result;
    }
    public static void ClearFunctionsList(){
        Allfunctions.clear();
    }
}
