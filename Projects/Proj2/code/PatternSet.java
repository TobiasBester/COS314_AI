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

}