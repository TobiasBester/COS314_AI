import java.util.*;

public class Chromosome implements Comparator<Chromosome> {

    char[] chrom;
    int chromLen = 0;
    int fitness = 0;

    public Chromosome(){

    }

    public Chromosome(int inputLen){
        this.chromLen = inputLen;
        generateInitChrom();
    }

    public int getFitness() {
        return this.fitness;
    }

    public String toString() {
        return new String(chrom);
    }

    private void generateInitChrom(){
        chrom = new char[chromLen]; 

        for (int i = 0; i < chromLen; i++) {
            Random rand = new Random();
            int ascii = rand.nextInt(26) + 1;
            char letter = (char)(ascii + 64); 

            chrom[i] = letter;
        }
        printLine("Generated chromosome: " + toString());
    }

    public void calcFitness(char[] answer) {
        int numMatches = 0;
        for (int i = 0; i < chrom.length; i++ ) {
            if (answer[i] == chrom[i]){
                numMatches++;
            }
        }

        this.fitness = numMatches;
        printLine("Chromosome " + toString() + " has fitness of " + this.fitness);
    }

    public Chromosome mutate(char[] answer) {
        boolean match = true;
        int numLetter = 0;

        if (chrom.equals(answer)) {
            // Then what are we still doing here??
            Chromosome copy = copy(this);
            return copy;
        }
        
        // We don't want to change a letter that already is correct
        while (match){
            // Which letter to change
            Random rand = new Random();
            numLetter = rand.nextInt(chromLen);
            if (chrom[numLetter] == answer[numLetter]){
                printLine("Letter is already a match!");
            } else {
                match = false;
            }
        }
        
        // Which letter to change it to
        Random rando = new Random();
        int ascii = rando.nextInt(26) + 1;
        char letter = (char)(ascii + 64);
        
        this.chrom[numLetter] = letter;
        printLine("Mutated chromosome to: " + toString());
        Chromosome copy = copy(this);
        return copy;
    }

    private Chromosome copy(Chromosome obj) {
        Chromosome newCopy = new Chromosome();
        newCopy.fitness = obj.fitness;
        newCopy.chromLen = obj.chromLen;
        newCopy.chrom = new char[newCopy.chromLen];
        for (int i = 0; i < obj.chromLen; i++) {
            newCopy.chrom[i] = obj.chrom[i];
        }
        return newCopy;
    }

    public void printLine(String line){
        System.out.println(line);
    }

    public int compare(Chromosome c, Chromosome c1) {
        return c.fitness - c1.fitness;
    }
}