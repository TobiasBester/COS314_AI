import java.util.*;

public class Population{

    int popSize = 0;
    int inputLen = 0;
    char[] answer;
    ArrayList<Chromosome> population;
    ArrayList<Chromosome> parents = new ArrayList<Chromosome>();
    int currGenNumber = 0;

    public Population(int popSize, char[] inputChars){
        this.popSize = popSize;
        this.inputLen = inputChars.length;
        this.answer = inputChars;
        generateInitPop();
    }

    private void generateInitPop(){
        population = new ArrayList<Chromosome>();

        for (int i = 0; i < popSize; i++) {
            Chromosome newChromo = new Chromosome(inputLen);
            population.add(newChromo);
        }
    }

    public void regeneration(){
        boolean terminate = false;
        while (!terminate && currGenNumber < 50){
            printLine("=================");
            printLine("Evolving to Generation " + ++currGenNumber);
            evaluatePop();
            printLine("");
            selectionMethod();
            printLine("");
            printParents();
            printLine("");
            geneticOperators();
            printLine("");
            terminate = checkForAnswer();
            printLine("=================");
        }
    
    }

    private void evaluatePop() {
        for (Chromosome chrom: population) {
            chrom.calcFitness(answer);
        }
    }

    private void selectionMethod(){
        parents.clear();

        //Tournament Selection
        for (int i = 0; i < population.size(); i++) {
            printLine("Tournament " + (i + 1));
            ArrayList<Chromosome> tourney = new ArrayList<Chromosome>(); 
            Random rand = new Random();
            for (int j = 0; j < Math.ceil(population.size()/2) + 1; j++) {
                int randomIndex = rand.nextInt(population.size());
                Chromosome competitor = population.get(randomIndex);
                tourney.add( competitor );
                printLine("Chromosome " + competitor.toString() + " qualified for the tournament");
            }

            // Find fittest chromosome
            Collections.sort(tourney, new Chromosome());
            parents.add( tourney.get(tourney.size() - 1) );
            printLine("Added Chromosome " + parents.get(i).toString()  + " with fitness " + parents.get(i).getFitness()  + " as parent");
        }

        copy(parents);
    }

    private void printParents(){
        for (int i = 0; i < population.size(); i++){
            printLine("Parent " + i + ": " + population.get(i).toString());
        }
    };

    private void geneticOperators() {
        for (int i = 0; i < population.size(); i++ ) {
            Random rand = new Random();
            int whichOperator = rand.nextInt(2);
            whichOperator = 0;
            if (whichOperator == 0) {
                // Mutation
                printLine("Mutating Chromosome: " + population.get(i).toString());
                Chromosome mutant = population.get(i).mutate(answer); 
                population.set(i , mutant);
            } else {
                // Crossover
            }
        }
    }

    private boolean checkForAnswer(){
        boolean found = false;
        String answerString = new String(answer);
        for (int i = 0; i < population.size(); i++ ){
            if (population.get(i).toString().equals(answerString)){
                found = true;
                printLine("Evolved to the answer!");
            }
        }

        return found;
    }

    private void copy(ArrayList<Chromosome> obj){
        for (int i = 0; i < obj.size(); i++) {
            this.population.set(i, obj.get(i));
        }
    }

    public void printLine(String line){
        System.out.println(line);
    }

}