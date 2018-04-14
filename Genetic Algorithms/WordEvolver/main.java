import java.util.Scanner;
import java.util.ArrayList;

public class main{

    public static void main(String[] args){

        // Evolutionary Algorithm
        // ======================
        // 1. Create an Initial Population                  DONE
        // 2. Repeat
        // 2.1  Evaluate the population                     DONE
        // 2.2  Select parents for the next generation      DONE
        // 2.3  Apply genetic operators to chosen parents
        // 3. Until termination criteria is met

        printLine("Welcome to Word Evolver");
        printLine("");
        printLine("Enter an English word for the population to evolve to");
        String input = getInput();
        printLine("You entered the word: " + input);
        char[] inputChars = stringToChars(input);
        printLine("Enter the size of the population");
        int popSize = getPopSize();
        printLine("Generating Initial Population (Generation 0)");
        printLine("");

        Population pop1 = new Population(popSize, inputChars);
        printLine("");
        pop1.regeneration();

    }

    public static void printLine(String line){
        System.out.println(line);
    }

    private static String getInput(){
        Scanner in = new Scanner(System.in);
        return (in.nextLine()).toUpperCase();
    }

    private static char[] stringToChars(String in){
        return in.toCharArray();
    }

    private static int getPopSize(){
        Scanner in = new Scanner(System.in);
        return Integer.parseInt(in.nextLine());
    }

}