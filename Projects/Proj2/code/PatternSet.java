import java.util.ArrayList;
import java.util.Random;

public class PatternSet {

    public ArrayList<Pattern> patternSet = new ArrayList<Pattern>();
    public ArrayList<Pattern> tSet = new ArrayList<Pattern>();
    public ArrayList<Pattern> vSet = new ArrayList<Pattern>();
    public ArrayList<Pattern> gSet = new ArrayList<Pattern>();
    int numPatterns;
    int numTPats;
    int numVPats;
    int numGPats;
    int NUM_INPUTS = 16;                                        // Number of Inputs
    int NUM_HIDDEN_UNITS = 21;                                  // Average of input and output layers: 16 + 26 = 42 / 2 = 21
    int NUM_OUTPUTS = 26;                                       // Number of Classes
    double learningRate = 0.05;
    double momentum = 0.05;
    double trainError = 0.0;
    int epochNum = 0;
    int EPOCH_MAX = 3;
    double DESIRED_ACCURACY = 0.8;

    double[][] v = new double[NUM_HIDDEN_UNITS][NUM_INPUTS];           // Weights for hidden units
    double[][] w = new double[NUM_OUTPUTS][NUM_HIDDEN_UNITS];            // Weights for outputs
    double bias1;                                        // Bias for inputs
    double bias2;                                        // Bias for inputs2

    public PatternSet(int numPatterns) {
        this.numPatterns = numPatterns;
        this.numTPats = (int)(0.60 * numPatterns);
        this.numVPats = (int)(0.20 * numPatterns);
        this.numGPats = (int)(0.20 * numPatterns);
    }

    public int getNumPatterns() {
        return numPatterns;
    }

    public Pattern getPatternAt(int index) {
        return patternSet.get(index);
    }

    public void printPatternAt(int index) {
        getPatternAt(index).printPattern();
    }

    public void addPattern(Pattern newPat) {
        this.patternSet.add(newPat);
    }

    public void shufflePatterns() {

        ArrayList<Pattern> shuffled = new ArrayList<Pattern>();
        
        Random random = new Random();
        while ( shuffled.size() < numPatterns ) {
            int r = random.nextInt(numPatterns);
            Pattern rPat = patternSet.get(r);
            boolean inserted = false;

            while (!inserted) {
                if (shuffled.contains(rPat) == false) {
                    shuffled.add(rPat);
                    inserted = true;
                } else {
                    r = random.nextInt(numPatterns);
                    rPat = patternSet.get(r);
                }
            }            
        }

        patternSet = shuffled;

    }

    public void splitPatterns() {
        System.out.println("Training: " + numTPats + " Validation: " + numVPats + " Generalization: " + numGPats);
        for (int i = 0; i < numTPats; i++) {
            tSet.add(patternSet.get(i));
        }

        for (int i = 0; i < numVPats; i++) {
            vSet.add(patternSet.get(i + numTPats));
        }

        for (int i = 0; i < numGPats; i++) {
            gSet.add(patternSet.get(i + numTPats + numVPats));
        }
    }

    public void initWeights() {
        bias1 = 1.0;
        bias2 = 1.0;
        int vFanin = NUM_INPUTS;
        int wFanin = NUM_HIDDEN_UNITS;
        
        // Initialize weights for hidden units
        for (int i = 0; i < NUM_HIDDEN_UNITS; i++) {
            v[i] = new double[NUM_INPUTS];
            Random random = new Random();
            for (int j = 0; j < NUM_INPUTS; j++) {
                double rWeight = ((random.nextDouble() * 2) - 1)/ Math.sqrt(vFanin);
                v[i][j] = rWeight;
            }
        }

        // Initialize weights for outputs
        for (int i = 0; i < NUM_OUTPUTS; i++) {
            w[i] = new double[NUM_HIDDEN_UNITS];
            Random random = new Random();
            for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                double rWeight = ((random.nextDouble() * 2) - 1)/ Math.sqrt(wFanin);
                w[i][j] = rWeight;
            }
        }

    }

    public void initValues() {
        learningRate = 1.0/16.0;        // inversely proportional to fanin
        momentum = 1.0/21.0;            // should be average of weight changes
    }

    public void startExperiment(int exp) {
        
        double genError = 1.0;
        double valError = 1.0;

        // Stopping Conditions: max num epochs is exceeded OR gen. error is acceptable OR overfitting detected
        while ( (epochNum < EPOCH_MAX) || (genError <= DESIRED_ACCURACY) ) {

            trainError = 0;
            epochNum++;
            System.out.println("Starting Epoch " + epochNum);

            for (int i = 0; i < tSet.size(); i++ ) {
                // i1w11 + i2w12 + i3w13 + ...
                double[] inputs2 = new double[NUM_HIDDEN_UNITS];
                for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                    double net = 0.0;
                    
                    // Get net input to each hidden unit
                    for (int k = 0; k < NUM_INPUTS; k++) {
                        net += tSet.get(i).attributes[k] * v[j][k];
                    };

                    // Get activation of each hidden unit
                    double activation = 1/ (1 + Math.pow( Math.E, -(net - bias1) ) );    // Sigmoid activation function
                    inputs2[j] = activation;
                }

                double[] outputs = new double[NUM_OUTPUTS];
                for (int j = 0; j < NUM_OUTPUTS; j++) {
                    double net = 0.0;
                    
                    // Get net input to each output unit
                    for (int k = 0; k < NUM_HIDDEN_UNITS; k++) {
                        net += inputs2[k] * w[j][k];
                    };

                    // Get activation of each output
                    double activation = 1/ (1 + Math.pow( Math.E, -(net - bias2) ) );    // Sigmoid activation function
                    outputs[j] = activation;
                }
            }

        }

    }

}