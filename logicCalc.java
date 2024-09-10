import java.util.*;
import java.io.*;
public class logicCalc{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("How many variables are there");
        int varAmount = input.nextInt();
        Map<Character, Boolean> variableMap = new HashMap<>();
        System.out.println("are the variable values known");
        Boolean varKnown = input.nextBoolean();
        char inputBuffer = '\u0000';
        Boolean booleanBuffer = null;
        int repeatX = 0;
        System.out.println("input the equation (^ for xor, > is conditional, < is biconditional)");
        input.nextLine();
        String stringInputBuffer = "(" + input.nextLine() + ")";
        if(varKnown){
            System.out.println("write the variable then a space then true or false(ex. p true)");
            variableMap.put('T', true);
            variableMap.put('F', false);
            for(int i = 0; i<varAmount; i++){
                inputBuffer = input.next().charAt(0);
                booleanBuffer = input.nextBoolean();
                variableMap.put(inputBuffer, booleanBuffer);
            }
            Boolean test = parseEquation(stringInputBuffer, variableMap);
            System.out.println("the answer for your equation is " + test);
        } else {
            System.out.println("write the variable and click enter");
            for(int i = 0; i<varAmount; i++){
                inputBuffer = input.next().charAt(0);
                variableMap.put(inputBuffer, true);
            }
            unknownValues(stringInputBuffer, variableMap);
        }
    }
    public static void unknownValues(String eqn, Map<Character, Boolean> variables){
        //generate values
        LinkedHashSet<Character> variableSet = new LinkedHashSet<>(variables.keySet());
        variables.put('T', true);
        variables.put('F', false);
        Map<Integer, String> result = new TreeMap<>(Comparator.reverseOrder());
        Boolean test = parseEquation(eqn, variables);
        String variableStatus = variableSet.toString();
        StringBuilder workingString = new StringBuilder();
        for (int i = 0; i<variableStatus.length(); i++){
            if(Character.isLetter(variableStatus.charAt(i))){
                workingString.append(Character.toUpperCase(variableStatus.charAt(i)));
            }
        }
        variableStatus = workingString.toString();
        workingString.setLength(0);
        int n = variableStatus.length();
        int totalCombinations =(int) Math.pow(2,n);
        result.put((totalCombinations), printUVAnswers(variables, test));
        int identifier = 0;
        int identifierHelp = variableStatus.length()-1;
        char currentChar;
        for(int i = 1; i < totalCombinations-1; i++){
            for(int j = 0; j < n; j++){
                currentChar = variableStatus.charAt(j);
                if ((i & (1 << j)) == 0){
                    workingString.append(Character.toLowerCase(currentChar));
                } else {
                    workingString.append(Character.toUpperCase(currentChar));
                }
            }
            variableStatus = workingString.toString();
            workingString.setLength(0);
            for(int j = 0; j < variableStatus.length(); j++){
                if(Character.isUpperCase(variableStatus.charAt(j))){
                    variables.replace(Character.toLowerCase(variableStatus.charAt(j)), true);
                    identifier += Math.pow(2,identifierHelp);
                } else {
                    variables.replace(Character.toLowerCase(variableStatus.charAt(j)), false);
                }
                identifierHelp -= 1;
            }
            identifierHelp = variableStatus.length()-1;
            test = parseEquation(eqn, variables);
            result.put(identifier, printUVAnswers(variables, test));
            identifier = 0;
        }
        variableStatus=variableStatus.toLowerCase();
        for(int j = 0; j < variableStatus.length(); j++){
            if(Character.isUpperCase(variableStatus.charAt(j))){
                variables.replace(Character.toLowerCase(variableStatus.charAt(j)), true);
            } else {
                variables.replace(Character.toLowerCase(variableStatus.charAt(j)), false);
            }
        }
        test = parseEquation(eqn, variables);
        result.put(identifier, printUVAnswers(variables, test));
        for(Map.Entry<Integer, String> entry : result.entrySet()){
            System.out.println(entry.getValue());
        }

    }
    private static String printUVAnswers(Map<Character, Boolean> variables, Boolean result){
        char excludeTrue = 'T';
        char excludeFalse = 'F';
        StringBuilder workingString = new StringBuilder();
        for(Map.Entry<Character, Boolean> entry : variables.entrySet()){
            if(!entry.getKey().equals(excludeTrue) && !entry.getKey().equals(excludeFalse)){
                workingString.append(entry.getKey() + " = " + entry.getValue() + "   ");
            }
        }
        workingString.append("output = " + result);
        return workingString.toString();
    }
    private static Boolean evaluate(String eqn, Map<Character, Boolean> variables){
        Boolean working;
        char ans = '\u0000';
        int i = 0;
        while(eqn.length() > 1 || i < eqn.length()){
            if(!variables.containsKey(eqn.charAt(i))){
                switch (eqn.charAt(i)) {
                    case 124:
                        working = variables.get(eqn.charAt(i-1)) | variables.get(eqn.charAt(i+1));
                        if(working){
                            ans = 'T';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        } else {
                            ans = 'F';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        }
                        break;

                    case 38:
                        working = variables.get(eqn.charAt(i-1)) & variables.get(eqn.charAt(i+1));
                        if(working){
                            ans = 'T';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        } else {
                            ans = 'F';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        }
                        break;

                    case 94:
                        working = variables.get(eqn.charAt(i-1)) ^ variables.get(eqn.charAt(i+1));
                        if(working){
                            ans = 'T';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        } else {
                            ans = 'F';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        }
                        break;

                    case 45:
                        working = !variables.get(eqn.charAt(i+1));
                        if(working){
                            ans = 'T';
                            if(i>0){
                                eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                            } else {
                                eqn = ans + eqn.substring(i+2); 
                            }
                        } else {
                            ans = 'F';
                            if(i>0){
                                eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                            } else {
                                eqn = ans + eqn.substring(i+2); 
                            }
                        }
                        break;

                    case 62:
                        working = !variables.get(eqn.charAt(i-1)) | variables.get(eqn.charAt(i+1));
                        if(working){
                            ans = 'T';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        } else {
                            ans = 'F';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        }

                        break;

                    case 60:
                        working = (!variables.get(eqn.charAt(i-1)) | variables.get(eqn.charAt(i+1))) & (!variables.get(eqn.charAt(i+1)) | variables.get(eqn.charAt(i-1)));
                        if(working){
                            ans = 'T';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        } else {
                            ans = 'F';
                            eqn = eqn.substring(0, i-1) + ans + eqn.substring(i+2); 
                        }
                        break;
                }
            } 
            i = i + 1;
        }
        if(eqn.equals("T")){
            return true;
        } else {
            if(eqn.equals("F")){
                return false;
            } 
        }        
        return null;   
    }
    public static Boolean parseEquation(String eqn, Map<Character, Boolean> variables){
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
        StringBuilder parenthesisEquation = new StringBuilder();
        String working;
        int j = 0;
        int startParenthesis;
        int endParenthesis;
        char ans;
        Boolean workingBool;
        for(int i = openParenthesis.get(openParenthesis.size()-1); i >= 0; i--){
            if(eqn.charAt(i) == 40){
                j=i+1;
                startParenthesis = i;
                while(eqn.charAt(j) != 41){
                    parenthesisEquation.append(eqn.charAt(j));
                    j++;
                }
                endParenthesis = j+1;
                workingBool = evaluate(parenthesisEquation.toString(), variables);
                if(workingBool){
                    ans = 'T';
                    eqn = eqn.substring(0, startParenthesis) + ans + eqn.substring(endParenthesis);
                } else {
                    ans = 'F';
                    eqn = eqn.substring(0, startParenthesis) + ans + eqn.substring(endParenthesis); 
                }
                parenthesisEquation.setLength(0);
            }
        }
        if(eqn.equals("T")){
            return true;
        } else {
            if(eqn.equals("F")){
                return false;
            } 
        }        
        return null;
    }
}
