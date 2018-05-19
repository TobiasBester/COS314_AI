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
    int EPOCH_MAX = 10;
    double DESIRED_ACCURACY = 99.0;

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

    public void initValues(int exp) {

        switch (exp) {
            case 1: {
                NUM_OUTPUTS = 1;                                    // Dependent on Experiment
                NUM_HIDDEN_UNITS = (NUM_INPUTS + NUM_OUTPUTS) / 2;  // Average of input and output layers: 16 + 26 = 42 / 2 = 21 
                learningRate = 1.0/NUM_INPUTS;                      // Inversely proportional to fanin
                momentum = 1.0/NUM_HIDDEN_UNITS;                    // Inversely proportional to NUM_HIDDEN_UNITS
                break;
            }
            case 2: {
                NUM_OUTPUTS = 5;
                NUM_HIDDEN_UNITS = (NUM_INPUTS + NUM_OUTPUTS) / 2;
                learningRate = 1.0/NUM_INPUTS;
                momentum = 1.0/NUM_HIDDEN_UNITS;
                break;
            }
            case 3: {
                NUM_OUTPUTS = 26;
                NUM_HIDDEN_UNITS = (NUM_INPUTS + NUM_OUTPUTS) / 2; 
                learningRate = 1.0/NUM_INPUTS;
                momentum = 1.0/NUM_HIDDEN_UNITS;
                break;
            }

        }
        System.out.println("Num. Outputs: " + NUM_OUTPUTS + " | Num. Hidden Units: " + NUM_HIDDEN_UNITS + 
                            " | Learning Rate: " + learningRate + " | Momentum: " + momentum);
        
    }

    public void startExperiment(int exp) {
        
        double genError = 1.0;
        double valError = 1.0;

        // Stopping Conditions: max num epochs is exceeded OR gen. error is acceptable OR overfitting detected
        while ( (epochNum < EPOCH_MAX) && (genError <= DESIRED_ACCURACY) ) {

            trainError = 0;
            epochNum++;
            System.out.println("Starting Epoch " + epochNum);

            // The weight change off an epoch
            double avg_hidden_change = 1.0;
            double[][][] hidden_weight_change = new double[EPOCH_MAX + 1][][];
            double avg_input_change = 1.0;
            double[][][] input_weight_change = new double[EPOCH_MAX + 1][][];

            // Instantiate Arrays for weight_changes
            for (int i = 0; i < EPOCH_MAX + 1; i++) {
                hidden_weight_change[i] = new double[NUM_OUTPUTS][NUM_HIDDEN_UNITS];
                for (int j = 0; j < NUM_OUTPUTS; j++) {
                    hidden_weight_change[i][j] = new double[NUM_HIDDEN_UNITS];
                    for (int k = 0; k < NUM_HIDDEN_UNITS; k++) {
                        hidden_weight_change[i][j][k] = 0.0;
                    }
                }
            }
            for (int i = 0; i < EPOCH_MAX + 1; i++) {
                input_weight_change[i] = new double[NUM_HIDDEN_UNITS][NUM_INPUTS];
                for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                    input_weight_change[i][j] = new double[NUM_INPUTS];
                    for (int k = 0; k < NUM_INPUTS; k++) {
                        input_weight_change[i][j][k] = 0.0;
                    }
                }
            }

            // For each training example
            for (int i = 0; i < tSet.size(); i++ ) {

                ///////////////////////////////////
                ///////////FEED FORWARD////////////
                ///////////////////////////////////
                // i1w11 + i2w12 + i3w13 + ...
                double[] hid_activation = new double[NUM_HIDDEN_UNITS];    //y_j
                for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                    double net = 0.0;
                    
                    // Get net input to each hidden unit
                    for (int k = 0; k < NUM_INPUTS; k++) {
                        net += tSet.get(i).attributes[k] * v[j][k];
                    };

                    // Get activation of each hidden unit
                    double activation = 1/ (1 + Math.pow( Math.E, -(net - bias1) ) );    // Sigmoid activation function
                    hid_activation[j] = activation;
                }

                double[] outputs = new double[NUM_OUTPUTS];     //O_k
                int[] actualOutputs = new int[NUM_OUTPUTS];     //a_k
                int e = 1;
                double[] output_error_signal = new double[NUM_OUTPUTS];
                double[] hidden_error_signal = new double[NUM_HIDDEN_UNITS];

                for (int j = 0; j < NUM_OUTPUTS; j++) {
                    double net = 0.0;
                
                    // Get net input to each output unit
                    for (int k = 0; k < NUM_HIDDEN_UNITS; k++) {
                        net += hid_activation[k] * w[j][k];
                    };

                    // Get activation of each output
                    double activation = 1/ (1 + Math.pow( Math.E, -(net - bias2) ) );    // Sigmoid activation function
                    outputs[j] = activation;
                    // System.out.println("Output " + outputs[j]);
                    
                    // Determine if the actual output should be 0 or 1.
                    /* if (tSet.get(i).letterCat == ('A')) {
                        if (outputs[j] <= 1/26) {
                            actualOutputs[j] = 1;
                        } else {
                            actualOutputs[j] = 0;
                        }
                    } else {
                        if (outputs[j] >= 1/26) {
                            actualOutputs[j] = 1;
                        } else {
                            actualOutputs[j] = 0;
                        }
                    } */
                    if (outputs[j] >= 0.7) {
                        actualOutputs[j] = 1;
                    } else {
                        actualOutputs[j] = 0;
                    }

                    
                    // Determine if the target output has been correctly predicted.
                    if (actualOutputs[j] == 0) {
                        e = 0;
                    }
                    // System.out.println("Actual Output " + actualOutputs[j]);

                    ///////////////////////////////////
                    ///////BACK PROPAGATION////////////
                    ///////////////////////////////////

                    // Calculate the OUTPUT error signals
                    double tk;
                    if (tSet.get(i).letterCat == ('A')) {
                        tk = 1/26;
                    } else {
                        tk = 1.0;
                    }
                    
                    output_error_signal[j] = -(tk - outputs[j]) * (1 - outputs[j]) * outputs[j];

                    double total_hidden_change = 0;

                    // Calculate new weight values
                    for (int k = 0; k < NUM_HIDDEN_UNITS; k++) {
                        double prev_weight_change = 0.0;

                        // hidden_weight_change[epochNum] = new double[NUM_OUTPUTS][NUM_HIDDEN_UNITS];
                        // hidden_weight_change[epochNum][j] = new double[NUM_HIDDEN_UNITS];
                        hidden_weight_change[epochNum][j][k] = -(learningRate * output_error_signal[j] * hid_activation[k]);

                        if (epochNum == 0) {
                            prev_weight_change = 0.0;
                        } else {
                            prev_weight_change = hidden_weight_change[epochNum - 1][j][k];
                        }

                        total_hidden_change += Math.abs((hidden_weight_change[epochNum][j][k] - prev_weight_change));
                        // System.out.println("Previous weight: " + w[j][k]);
                        w[j][k] = w[j][k] + hidden_weight_change[epochNum][j][k] + momentum*(prev_weight_change);
                        // System.out.println("Updated weight: " + w[j][k]);

                    };

                    avg_hidden_change = total_hidden_change / NUM_HIDDEN_UNITS;
                }

                // Calculate the error signal for each hidden unit
                for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                    hidden_error_signal[j] = 0.0;
                    for (int k = 0; k < NUM_OUTPUTS; k++) {
                        hidden_error_signal[j] += output_error_signal[k] * w[k][j] * (1 - hid_activation[j]) * hid_activation[j];
                    }
                    
                    double total_input_change = 0;

                    // Calculate the new weight values for input values
                    for (int n = 0; n < NUM_INPUTS; n++) {
                        double prev_weight_change = 0.0;

                        input_weight_change[epochNum][j][n] = -(learningRate * hidden_error_signal[j] * tSet.get(i).attributes[n]);
                        if (epochNum == 0) {
                            prev_weight_change = 0.0;
                        } else {
                            prev_weight_change = input_weight_change[epochNum - 1][j][n];
                        }

                        total_input_change += Math.abs((input_weight_change[epochNum][j][n] - prev_weight_change));

                        // System.out.println("Previous weight: " + v[j][n]);
                        v[j][n] = v[j][n] + input_weight_change[epochNum][j][n] + momentum*(prev_weight_change);
                        // System.out.println("Updated weight: " + v[j][n]);
                    }

                    avg_input_change = total_input_change / NUM_INPUTS;
                    // System.out.println("avginput: " + avg_input_change);
                }

                if (e == 1) {
                    // System.out.println("Test Example " + i + " was correctly predicted ");
                }
                trainError += e;
            }

            trainError = (trainError / tSet.size()) * 100;
            System.out.println("Training Accuracy: " + trainError);

            // Calculate Accuracy on Generalization Set
            genError = generalizeData();
            System.out.println("Generalization Accuracy: " + genError);
        }

   }

   private double generalizeData() {

        double genPredictions = 0;
        for (int i = 0; i < gSet.size(); i++) {                    
            // Get net input to each hidden unit
            double[] input_activation = new double[NUM_HIDDEN_UNITS];
            for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                double net = 0.0;
                for (int k = 0; k < NUM_INPUTS; k++) {
                    net += gSet.get(i).attributes[k] * v[j][k];
                };

                double activation = 1/ (1 + Math.pow( Math.E, -(net - bias1) ) );
                input_activation[j] = activation;
            }
            
            double[] outputs = new double[NUM_OUTPUTS];     //O_k
            int[] actualOutputs = new int[NUM_OUTPUTS];     //a_k
            int e = 1;

            for (int j = 0; j < NUM_OUTPUTS; j++) {
                double net = 0.0;
                for (int k = 0; k < NUM_HIDDEN_UNITS; k++) {
                    net += input_activation[k] * w[j][k];
                };

                double activation = 1/ (1 + Math.pow( Math.E, -(net - bias2) ) );
                outputs[j] = activation;

                if (outputs[j] >= 0.7) {
                    actualOutputs[j] = 1;
                } else {
                    actualOutputs[j] = 0;
                }

                if (actualOutputs[j] == 0) {
                    e = 0;
                }
            }
            genPredictions += e;
        };
        genPredictions = (genPredictions / gSet.size()) * 100;

        return genPredictions;
    }

}