package compilerdesign;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Main {

    static Stack<String> stack = new Stack<>();
    static String inputString;
    static String[][] parsingTable =
            {  //  action                                    goto
               //  state  id    +   *   (     )   $              E    T    F
                    {"0", "S5", "", "", "S4", "", "",           "1", "2", "3"},
                    {"1", "", "S6", "", "", "", "accept",       "", "", ""},
                    {"2", "", "R2", "S7", "", "R2", "R2",       "", "", ""},
                    {"3", "", "R4", "R4", "", "R4", "R4",       "", "", ""},
                    {"4", "S5", "", "", "S4", "", "",           "8", "2", "3"},
                    {"5", "", "R6", "R6", "", "R6", "R6",       "", "", ""},
                    {"6", "S5", "", "", "S4", "", "",           "", "9", "3"},
                    {"7", "S5", "", "", "S4", "", "",           "", "", "10"},
                    {"8", "", "S6", "", "", "S11", "",          "", "", ""},
                    {"9", "", "R1", "S7", "", "R1", "R1",       "", "", ""},
                    {"10", "", "R3", "R3", "", "R3", "R3",      "", "", ""},
                    {"11", "", "R5", "R5", "", "R5", "R5",      "", "", ""}
            };

    public static void main(String[] args) {
        Scanner terminal = new Scanner(System.in);
        System.out.println("Enter file path: ");
        String filePath = terminal.next();
        stack.push("0");
        File file = new File(filePath);
        try {
            Scanner fileScanner = new Scanner(file);
            inputString = fileScanner.next();
            System.out.println("Input string is: " + inputString);
            for(;;) {
                System.out.println("InputString: " + inputString + "\nStack: " + stack.toString());
                if (Character.isDigit(inputString.charAt(0))) {
                    parser(stack.peek(), "id");
                } else {
                    parser(stack.peek(), Character.toString(inputString.charAt(0)));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void parser(String stackCurrent, String input) {
        String action = "";
        String goTo;
        char nextState;
        if (input.equals("id")) {
            action = parsingTable[Integer.parseInt(stack.peek())][1];
        } else if (input.equals("+")) {
            action = parsingTable[Integer.parseInt(stack.peek())][2];
        } else if (input.equals("*")) {
            action = parsingTable[Integer.parseInt(stack.peek())][3];
        } else if (input.equals("(")) {
            action = parsingTable[Integer.parseInt(stack.peek())][4];
        } else if (input.equals(")")) {
            action = parsingTable[Integer.parseInt(stack.peek())][5];
        } else if (input.equals("$")) {
            action = parsingTable[Integer.parseInt(stack.peek())][6];
        } else {
            System.out.println("Unrecognized input character");
            error();
        }

        if (action.equals("")) {
            error();
        } else if (action.charAt(0) == 'S') {
            nextState = action.charAt(1);
            stack.push(input);
            stack.push(String.valueOf(nextState));
            inputString = inputString.substring(1);

        } else if (action.charAt(0) == 'R') {
            stack.pop();
            if (action.charAt(1) == '1') {
                stack.pop();
                stack.pop();
                stack.pop();
                stack.pop();
                stack.pop();
                goTo = parsingTable[Integer.parseInt(stack.peek())][7];
                stack.push("E");
                stack.push(goTo);
            } else if (action.charAt(1) == '2') {
                stack.pop();
                goTo = parsingTable[Integer.parseInt(stack.peek())][7];
                stack.push("E");
                stack.push(goTo);
            } else if (action.charAt(1) == '3') {
                stack.pop();
                stack.pop();
                stack.pop();
                stack.pop();
                stack.pop();
                goTo = parsingTable[Integer.parseInt(stack.peek())][8];
                stack.push("T");
                stack.push(goTo);
            } else if (action.charAt(1) == '4') {
                stack.pop();
                goTo = parsingTable[Integer.parseInt(stack.peek())][8];
                stack.push("T");
                stack.push(goTo);
            } else if (action.charAt(1) == '5') {
                stack.pop();
                stack.pop();
                stack.pop();
                goTo = parsingTable[Integer.parseInt(stack.peek())][9];
                stack.push("F");
                stack.push(goTo);
            } else if (action.charAt(1) == '6') {
                stack.pop();
                goTo = parsingTable[Integer.parseInt(stack.peek())][9];
                stack.push("F");
                stack.push(goTo);
            }
        }
        else if (action.equals("accept")) {
            System.out.println("Parsing was successful.");
            System.exit(0);
        } else {
            System.out.println("Lord knows what happened");
        }
    }
    public static void error() {
        System.out.println("Syntax error encountered. Cannot continue parsing. :(");
        System.exit(0);
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
