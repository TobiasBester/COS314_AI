import java.util.ArrayList;

public class SearchMap {

    private int dimension;
    private String name;
    protected ArrayList<SearchNode> allNodes = new ArrayList<SearchNode>();
    public float[][] distArray;

    public SearchMap() {
        this.dimension = 0;
    }

    public SearchMap(int numNodes) {
        this.dimension = numNodes;
    }

    public SearchMap clone(){
        SearchMap result = new SearchMap(this.dimension);
        result.distArray = new float[result.dimension][result.dimension];

        for (int i = 0; i < this.dimension; i++){
            for (int j = 0; j < this.dimension; j++){
                
                result.distArray[i][j] = this.distArray[i][j];
            }
        }

        return result;
    }

    public String getName(){
        return this.name;
    }

    public int getDim(){
        return this.dimension;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDimension(int numNodes){
        this.dimension = numNodes;
    }

    public void addNode(SearchNode node){
        allNodes.add(node);
    }

    public SearchNode getNodeAt(int index){
        return allNodes.get(index);
    }

    public void fillDistArray(){

        int len = dimension;
        distArray = new float[len][len];

        for (int i = 0; i < dimension; i++){
            for (int j = 0; j < dimension; j++){
                
                float xD = allNodes.get(i).getX() - allNodes.get(j).getX();
                float yD = allNodes.get(i).getY() - allNodes.get(j).getY();

                if (i == j) {
                    distArray[i][j] = 0;
                } else {
                    distArray[i][j] = (float)(Math.sqrt(xD*xD + yD*yD));
                }
            }
        }
    }

}