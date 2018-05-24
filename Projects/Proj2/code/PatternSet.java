import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.BufferedWriter;

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
    int EPOCH_MAX = 1000;
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
                NUM_HIDDEN_UNITS = 8;                               // Average of input and output layers: 16 + 26 = 42 / 2 = 21 
                learningRate = 0.5;                      // Inversely proportional to fanin
                momentum = 0.05;                                     // Inversely proportional to NUM_HIDDEN_UNITS
                EPOCH_MAX = 100;
                DESIRED_ACCURACY = 99.0;
                break;
            }
            case 2: {
                NUM_OUTPUTS = 1;
                NUM_HIDDEN_UNITS = (NUM_INPUTS + NUM_OUTPUTS) / 2;
                learningRate = 1.0/NUM_INPUTS;
                momentum = 0.05;
                EPOCH_MAX = 200;
                DESIRED_ACCURACY = 99.0;
                break;
            }
            case 3: {
                NUM_OUTPUTS = 26;
                NUM_HIDDEN_UNITS = (NUM_INPUTS + NUM_OUTPUTS) / 2; 
                learningRate = 1.0/NUM_INPUTS;
                momentum = 1.0/NUM_HIDDEN_UNITS;
                EPOCH_MAX = 1000;
                break;
            }

        }
        System.out.println("Num. Outputs: " + NUM_OUTPUTS + " | Num. Hidden Units: " + NUM_HIDDEN_UNITS + 
                            " | Learning Rate: " + learningRate + " | Momentum: " + momentum);

        initWeights();
        
    }

    public void startExperiment(int exp, char chosenLetter) {
        
        double genError = 1.0;
        double valError = 1.0;
        boolean overfit = false;
        ArrayList<Double> valErrors = new ArrayList<Double>();

        // Stopping Conditions: max num epochs is exceeded OR gen. error is acceptable OR overfitting detected
        while ( (epochNum < EPOCH_MAX) && (genError < DESIRED_ACCURACY) && (overfit == false) ) {

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

            int numAsInorrect = 0;
            int numNonAsIncorrect = 0;

            // For each training example
            for (int i = 0; i < tSet.size(); i++ ) {

                ///////////////////////////////////
                ///////////FEED FORWARD////////////
                ///////////////////////////////////

                // Get outputs of hidden units
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
                // boolean[] classifError = new boolean[NUM_OUTPUTS];
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
                    // System.out.println("Output for " + tSet.get(i).letterCat + ": " + outputs[j]*26);
                    
                    // Determine if the actual output should be 0 or 1.
                    int letterNum = getLetterNum(tSet.get(i).letterCat);
                    actualOutputs[j] = determineActualOutput(exp, j, outputs[j], letterNum, chosenLetter);

                    // Determine if the target output has been correctly predicted.
                    if (actualOutputs[j] == 0) {
                        e = 0;
                    }

                    ///////////////////////////////////
                    ///////BACK PROPAGATION////////////
                    ///////////////////////////////////

                    // Calculate the OUTPUT error signals
                    // tk is the target output value
                    double tk = 1.0;

                    // Depending on the experiment, a different tk will be used
                    if (exp == 1) {
                        // If cat letter chosen
                        if (tSet.get(i).letterCat == chosenLetter) {
                            tk = 1.0;
                        } else {
                            tk = 0.0;
                        }

                    } else if (exp == 2) {
                        // Check if the letter is a vowel or non-vowel letter
                        if (tSet.get(i).letterCat == 'A' || tSet.get(i).letterCat == 'E' || tSet.get(i).letterCat == 'I' 
                        || tSet.get(i).letterCat == 'O' || tSet.get(i).letterCat == 'U') {
                            tk = 1.0;
                        } else {
                            tk = 0.0;
                        }
                    } else {
                        // Experiment 3 - Get exact letter
                    }
                    
                    //System.out.println("Actual Letter: " + tSet.get(i).letterCat);
                    //System.out.println("Guessed Letter: " + (outputs[j]*26));

                    output_error_signal[j] = -(tk - outputs[j]) * (1 - outputs[j]) * outputs[j];

                    double total_hidden_change = 0;

                    // Calculate new weight values
                    for (int k = 0; k < NUM_HIDDEN_UNITS; k++) {
                        double prev_weight_change = 0.0;

                        // learningRate = 1.0/NUM_HIDDEN_UNITS;
                        // hidden_weight_change[epochNum] = new double[NUM_OUTPUTS][NUM_HIDDEN_UNITS];
                        // hidden_weight_change[epochNum][j] = new double[NUM_HIDDEN_UNITS];
                        hidden_weight_change[epochNum][j][k] = -(learningRate * output_error_signal[j] * hid_activation[k]);
                        // System.out.println("hidden weight change: " + hidden_weight_change[epochNum][j][k]);

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
                    // momentum = avg_hidden_change;
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

                        // learningRate = 1.0/NUM_INPUTS;

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
                    // momentum = avg_input_change;
                    // System.out.println("avginput: " + avg_input_change);
                }

                trainError += e;
            }

            trainError = (trainError / tSet.size()) * 100;

            // Calculate Accuracy on Validation Set
            double prevValError = valError;

            valError = validateData(exp, chosenLetter);
            valErrors.add(valError);

            double valErrorStdev = getValErrorStdev(valErrors);
            double valErrorAvg = getValErrorAvg(valErrors);

            // Calculate Accuracy on Generalization Set
            genError = generalizeData(exp, chosenLetter);

            System.out.println("Training Accuracy: " + trainError);
            System.out.println("Validation Accuracy: " + valError);
            System.out.println("Generalization Accuracy: " + genError);
            System.out.println();
            
            if (valError < valErrorAvg - 4*valErrorStdev) {
                overfit = true;
                System.out.println("Overfitting detected: Validation Error Decreased Too Much");
            }

            if (genError >= DESIRED_ACCURACY) {
                System.out.println("Generalization Accuracy is at the desired percentage");
            }

            writeToOutput(epochNum, trainError, valError, genError);
            
        }

   }

   private int determineActualOutput(int exp, int outputIt, double outputValue, int letterNum, char chosenLetter) {
        if (exp == 1) {
            // If the pattern is of class A
            if (letterNum == getLetterNum(chosenLetter)) {
                // If the output value is more than 0.7 then correct prediction
                if (outputValue >= 0.7) {
                    return 1;
                } else {        
                    return 0;       // Else wrong prediction
                }
            } else {    // Else the pattern should not be of class A
                if (outputValue <= 0.3) {   // So the output value should be less than 0.3
                    return 1;
                } else {
                    return 0;      // Else wrong prediction
                }
            }
        } else if (exp == 2) {
            // If the pattern is a vowel
            if (letterNum == 1 || letterNum == 5 || letterNum == 9 || letterNum == 15 || letterNum == 21) {
                // If the output value is more than 0.7 then correct prediction
                if (outputValue >= 0.7) {
                    return 1;
                } else {        
                    return 0;       // Else wrong prediction
                } 
            } else {
                if (outputValue <= 0.3) {   // So the output value should be less than 0.3
                    return 1;
                } else {
                    return 0;      // Else wrong prediction
                }
            }
        } else {    // exp == 3
            return 1;
        }
   }

   private double getValErrorStdev(ArrayList<Double> valErrors) {
        
        int numOfValErrors  = valErrors.size();
        double x1 = 0.0;

        double average = getValErrorAvg(valErrors);

        for (int i = 0; i < numOfValErrors; i++) {
            x1 += Math.pow( valErrors.get(i) - average , 2);
        }

        return Math.sqrt( x1 / numOfValErrors );
    }

    private double getValErrorAvg(ArrayList<Double> valErrors) {
        
        int numOfValErrors  = valErrors.size();
        double total = 0.0;
        double average = 0.0;

        // Get total valErrors
        for (int i = 0; i < numOfValErrors; i++) {
            total += valErrors.get(i);
        }

        average = total/numOfValErrors;

        return average;
    }

   private int getLetterNum(char letter) {
        //System.out.println("Letter: " + letter + " - " + ((int)(letter) - 64));
        return ((int)(letter) - 64); 
   }

   private double validateData(int exp, char chosenLetter) {

        double valPredictions = 0;
        for (int i = 0; i < vSet.size(); i++) {                    
            // Get net input to each hidden unit
            double[] input_activation = new double[NUM_HIDDEN_UNITS];
            for (int j = 0; j < NUM_HIDDEN_UNITS; j++) {
                double net = 0.0;
                for (int k = 0; k < NUM_INPUTS; k++) {
                    net += vSet.get(i).attributes[k] * v[j][k];
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

                // Determine if the actual output should be 0 or 1.
                int letterNum = getLetterNum(vSet.get(i).letterCat);
                actualOutputs[j] = determineActualOutput(exp, j, outputs[j], letterNum, chosenLetter);

                // Determine if the target output has been correctly predicted.
                if (actualOutputs[j] == 0) {
                    e = 0;
                }
            }
            valPredictions += e;
        };
        valPredictions = (valPredictions / vSet.size()) * 100;

        return valPredictions;
    }

   private double generalizeData(int exp, char chosenLetter) {

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

                // Determine if the actual output should be 0 or 1.
                int letterNum = getLetterNum(gSet.get(i).letterCat);
                actualOutputs[j] = determineActualOutput(exp, j, outputs[j], letterNum, chosenLetter);

                // Determine if the target output has been correctly predicted.
                if (actualOutputs[j] == 0) {
                    e = 0;
                }
            }
            genPredictions += e;
        };
        genPredictions = (genPredictions / gSet.size()) * 100;

        return genPredictions;
    }

    private void writeToOutput(int epochNum, double trainError, double valError, double genError) {
        // Clear textfile
        if (epochNum == 1) {
            clearFile();
        }

        try {
            FileWriter f = new FileWriter("Results.txt", true);
            BufferedWriter output = new BufferedWriter(f);
            output.write("Epoch Number " + epochNum + " - Training Accuracy: " + trainError 
                        + " | Validation Error: " + valError + " | Generalization Accuracy: " + genError ); 
            output.newLine();
            output.close();
        } catch(Exception e) {
            e.getMessage();
        }
    }

    private void clearFile() {
        try {
            FileWriter f = new FileWriter("Results.txt", false);
            BufferedWriter output = new BufferedWriter(f);
            output.write("");
            output.close();
        } catch(Exception e) {
            e.getMessage();
        }
    }

}