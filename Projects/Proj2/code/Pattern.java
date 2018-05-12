public class Pattern {

    char letterCat;

    // pattern attributes
    int at1;    // lettr
    int at2;    // x-box
    int at3;    // y-box
    int at4;    // width
    int at5;    // high
    int at6;    // onpix
    int at7;    // xbar
    int at8;    // ybar
    int at9;    // x2bar
    int at10;   // y2bar
    int at11;   // x2ybar
    int at12;   // xy2bar
    int at13;   // x-edge
    int at14;   // xegvy
    int at15;   // y-edge
    int at16;   // yegvx

    public Pattern(char letter, int at1, int at2, int at3, int at4, int at5,
                    int at6, int at7, int at8, int at9, int at10, int at11, 
                    int at12, int at13, int at14, int at15, int at16) 
    {
        this.letterCat = letter;
        this.at1 = at1;
        this.at2 = at2;
        this.at3 = at3;
        this.at4 = at4;
        this.at5 = at5;
        this.at6 = at6;
        this.at7 = at7;
        this.at8 = at8;
        this.at9 = at9;
        this.at10 = at10;
        this.at11 = at11;
        this.at12 = at12;
        this.at13 = at13;
        this.at14 = at14;
        this.at15 = at15;
        this.at16 = at16;

    }

    public void printPattern() {
        String result = "";
        result += letterCat;
        result += "," + this.at1;
        result += "," + this.at2;
        result += "," + this.at3;
        result += "," + this.at4;
        result += "," + this.at5;
        result += "," + this.at6;
        result += "," + this.at7;
        result += "," + this.at8;
        result += "," + this.at9;
        result += "," + this.at10;
        result += "," + this.at11;
        result += "," + this.at12;
        result += "," + this.at13;
        result += "," + this.at14;
        result += "," + this.at15;
        result += "," + this.at16;
        System.out.println(result);
    }

}