import java.util.ArrayList;

public class State {

    //private float distance;
    public ArrayList<SearchNode> visitedNodes = new ArrayList<SearchNode>();
    protected int numVisitedNodes;
    protected int heuristic = 9999999;

    public State(){
        numVisitedNodes = 0;
    }

    public void addVisitedNode(SearchNode node){
        visitedNodes.add(node);
    }

    public int getNumVisitedNodes(){
        return visitedNodes.size();
    }

    public State clone(){
       State result = new State();
       result.numVisitedNodes = this.numVisitedNodes;
       for (int i = 0; i < this.visitedNodes.size(); i++){
           result.visitedNodes.add(this.visitedNodes.get(i));
       }

       return result;
       
    }

    public Boolean contains(SearchNode node){
        return visitedNodes.contains(node);
    }

    public String printPath(){
        String result = "";
        for (int i = 0; i < getNumVisitedNodes(); i++){
            result += visitedNodes.get(i).getIndex() + " ";
        }
        result += visitedNodes.get(0).getIndex();

        return result;
    }

    public float getTotalDistanceTravelled(SearchMap map){
        float result = 0;

        for (int i = 0; i < getNumVisitedNodes()-1; i++){
            result += map.distArray[i][i+1];
        }
        result += map.distArray[0][getNumVisitedNodes()-1];

        return result;
    }

    protected float calcGFunction(SearchMap map){
        float result = 0;

        for (int i = 0; i < getNumVisitedNodes()-1; i++){
            result += map.distArray[i][i+1];
        }
        
        return result;
    }

    protected float calcHeuristic() ){
        //Nearest Neighbour

    }

    public void calcEvalFunction( int gn, int hn ){
        //f(n) = g(n) + h(n)
        //g(n) is the total distance from state n to the start state
        //h(n) is the heuristic, which will be the nearest neighbour
    }

}