public class Pattern {

    char letterCat;
    double letterVal;

    // pattern attributes
    double at1;    // lettr
    double at2;    // x-box
    double at3;    // y-box
    double at4;    // width
    double at5;    // high
    double at6;    // onpix
    double at7;    // xbar
    double at8;    // ybar
    double at9;    // x2bar
    double at10;   // y2bar
    double at11;   // x2ybar
    double at12;   // xy2bar
    double at13;   // x-edge
    double at14;   // xegvy
    double at15;   // y-edge
    double at16;   // yegvx
    double[] attributes = new double[16];

    public Pattern(char letter, int at1, int at2, int at3, int at4, int at5,
                    int at6, int at7, int at8, int at9, int at10, int at11, 
                    int at12, int at13, int at14, int at15, int at16) 
    {
        // Scale values
        this.letterCat = letter;
        this.letterVal = ((double)(letter) - 64) / 26;
        this.at1 = at1 / 15.0;
        this.attributes[0] = this.at1;
        this.at2 = at2 / 15.0;
        this.attributes[1] = this.at2;
        this.at3 = at3 / 15.0;
        this.attributes[2] = this.at3;
        this.at4 = at4 / 15.0;
        this.attributes[3] = this.at4;
        this.at5 = at5 / 15.0;
        this.attributes[4] = this.at5;
        this.at6 = at6 / 15.0;
        this.attributes[5] = this.at6;
        this.at7 = at7 / 15.0;
        this.attributes[6] = this.at7;
        this.at8 = at8 / 15.0;
        this.attributes[7] = this.at8;
        this.at9 = at9 / 15.0;
        this.attributes[8] = this.at9;
        this.at10 = at10 / 15.0;
        this.attributes[9] = this.at10;
        this.at11 = at11 / 15.0;
        this.attributes[10] = this.at11;
        this.at12 = at12 / 15.0;
        this.attributes[11] = this.at12;
        this.at13 = at13 / 15.0;
        this.attributes[12] = this.at13;
        this.at14 = at14 / 15.0;
        this.attributes[13] = this.at14;
        this.at15 = at15 / 15.0;
        this.attributes[14] = this.at15;
        this.at16 = at16 / 15.0;
        this.attributes[15] = this.at16;
    }

    public void printPattern() {
        String result = "";
        result += letterCat + "," + letterVal;
        for (int i = 0; i < attributes.length; i++) {
            result += "," + attributes[i];
        }
        System.out.println(result);
    }

}