package equation;
import java.util.*;
public class equation{ //create this as an abstract not as a normal class
    protected String equation;
    protected String parsedEquation;
    public equation(String equation){
        this.equation = equation;
    }
    public String equationInParenthesis(String eqn){
        char temp;
        List<Integer> openParenthesis = new ArrayList<>();
        List<Integer> closedParenthesis = new ArrayList<>();

        for(int i = 0; i<eqn.length(); i++){
            temp = eqn.charAt(i);
            if(temp == 40){
                openParenthesis.add(i);
            }
            if(temp == 41){
                closedParenthesis.add(i);
            }
        }
        StringBuilder workingString = new StringBuilder();
        String output = "";
        for(int i = openParenthesis.get(openParenthesis.size()-1); i>=0; i--){
            if(eqn.charAt(i) == 40){
                j=i+1;
                startParenthesis = i;
                while(eqn.charAt(j) != 41){
                    parenthesisEquation.append(eqn.charAt(j));
                    j++;
                }
                endParenthesis = j+1;
            }
            output = workingString.toString();
            break;
        }
        return output;
    }
}
class mathematicalEquation extends equation{
    private Map<Character, Double> MathematicalVariables = new HashMap<>();
    public mathematicalEquation(String equation, Map<Character, Double> mathVars){
        super(equation);
        this.MathematicalVariables = mathVars;
    }
    public double evaluate(){
        for(int i = 0; i < equation.length(); i++){
            
        }
    }
}
class logicalEquation extends equation{
    private Map<Character, Boolean> logicalVariables = new HashMap<>();
    public logicalEquation(String equation, Map<Character, Boolean> logicVars, Boolean areValuesKnown){
        super(equation);
        this.logicalVariables = logicVars;
    }
    public Boolean evaluateKnown(){

    }
    public void evaluateUnkown(){

    }
}
