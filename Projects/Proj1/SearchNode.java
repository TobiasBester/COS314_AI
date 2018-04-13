public class SearchNode {
    protected int index;
    protected float x, y;  

    public SearchNode(int i, float x, float y){
        this.index = i;
        this.x = x;
        this.y = y;
    }

    public int getIndex(){
        return this.index;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

}