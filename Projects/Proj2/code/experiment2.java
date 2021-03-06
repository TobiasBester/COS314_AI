import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class experiment2 {

    static int NUM_PATTERNS = 20000;

    public static void main(String args[]) {
        int experiment = 2;
        System.out.println("Welcome to the Letter Recognition Software Suite");
        System.out.println("================================================");
        
        // Retrieve Patterns from data file, scale, and put into Patterns ArrayList
        System.out.println("Retrieving " + NUM_PATTERNS + " Patterns");
        PatternSet patternSet = new PatternSet(NUM_PATTERNS); 
        patternSet = retrievePatterns("letter-recognition.data");
        System.out.println("Patterns Retrieved and Scaled");
        System.out.println();

        // Shuffle Patterns
        System.out.println("Shuffling Patterns");
        patternSet.shufflePatterns();
        System.out.println("Patterns Shuffled");
        System.out.println();

        // Split data into sets
        System.out.println("Splitting Patterns into Sets");
        patternSet.splitPatterns();
        System.out.println("Patterns Split");
        System.out.println();

        // Initialize values
        System.out.println("Initialize Other Values");
        patternSet.initValues(experiment);
        System.out.println("Other Values Initialized");
        System.out.println();

        // Begin Experiment
        System.out.println("Starting Experiment 2");
        patternSet.startExperiment(experiment, 'A');
        System.out.println("======================");
        System.out.println();
    }

    public static PatternSet retrievePatterns(String fileName) {
        PatternSet result = new PatternSet(NUM_PATTERNS);
        String line = null;
        
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null ) {
                char letter = line.charAt(0);
                String[] attributes = line.split(",");
                int a1 = Integer.parseInt(attributes[1]);
                int a2 = Integer.parseInt(attributes[2]);
                int a3 = Integer.parseInt(attributes[3]);
                int a4 = Integer.parseInt(attributes[4]);
                int a5 = Integer.parseInt(attributes[5]);
                int a6 = Integer.parseInt(attributes[6]);
                int a7 = Integer.parseInt(attributes[7]);
                int a8 = Integer.parseInt(attributes[8]);
                int a9 = Integer.parseInt(attributes[9]);
                int a10 = Integer.parseInt(attributes[10]);
                int a11 = Integer.parseInt(attributes[11]);
                int a12 = Integer.parseInt(attributes[12]);
                int a13 = Integer.parseInt(attributes[13]);
                int a14 = Integer.parseInt(attributes[14]);
                int a15 = Integer.parseInt(attributes[15]);
                int a16 = Integer.parseInt(attributes[16]);

                Pattern newPat = new Pattern(letter, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
                result.addPattern(newPat);                
            }

            br.close();

        } catch (FileNotFoundException fe) {
            System.out.println("Unable to open File");
        } catch (IOException ex) {
            System.out.println("Error Reading File");
        }

        return result;
    }

    public static char getLetter() {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        s = s.toUpperCase();
        
        return s.charAt(0);
    }

}