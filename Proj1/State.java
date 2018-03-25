import java.util.*;

public class State implements Comparable<State> {

    //private float distance;
    public ArrayList<SearchNode> visitedNodes = new ArrayList<SearchNode>();
    protected int numVisitedNodes;
    protected int evalFunc = 9999999;
    protected int gFunc = 9999999;
    protected int heuristic = 99999999;

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

    public int getTotalDistanceTravelled(SearchMap map){
        float result = 0;
        int firstI = visitedNodes.get(0).getIndex() - 1;
        int lastI = visitedNodes.get(getNumVisitedNodes() - 1).getIndex() - 1;

        for (int i = 0; i < getNumVisitedNodes()-1; i++){
            int i1 = visitedNodes.get(i).getIndex() - 1;
            int i2 = visitedNodes.get(i+1).getIndex() - 1;
            result += map.distArray[i1][i2];
        }
        result += map.distArray[firstI][lastI];

        return (int)(Math.ceil(result));
    }

    //This function is so that arraylist.sort can be used
    @Override
    public int compareTo(State compareState){
        int compareEvalFunc=((State) compareState).getEvalFunc();

        return this.evalFunc - compareEvalFunc;
    }

    protected float calcGFunction(SearchMap map){
        float result = 0;

        for (int i = 0; i < getNumVisitedNodes()-1; i++){
            int i1 = visitedNodes.get(i).getIndex() - 1;
            int i2 = visitedNodes.get(i+1).getIndex() - 1;
            result += map.distArray[i1][i2];
            //System.out.println("Distance between city " + map.getNodeAt(i1).getIndex() + " and city " + map.getNodeAt(i2).getIndex() + " is: " + map.distArray[i1][i2]);
        }
        
        this.gFunc = (int)(Math.ceil(result));
        
        return this.gFunc;
    }

    public int getGFunc(){
        return this.gFunc;
    }

    protected int calcHeuristic(SearchMap map) {
        //h(n) = (currentCity to nearestNeighbour) + (estimated total to travel all unvisited cities) + (lastCity to startCity)
        //1. Dist(Nearest Neighbour) + 2. Dist(Nearest unvisited city to start city) +3. estimated distance to travel all unvisited cities
 
        float nearestCurrDist = 999999;
        float nearestStartDist = 999999;
        int indexCurr = 0;
        int indexStart = 0;
        int firstI = visitedNodes.get(0).getIndex()-1;
        int currentI = visitedNodes.get(getNumVisitedNodes()-1).getIndex()-1;
        
        for (int i = 0; i < map.getDim(); i++){

            //if the city is unvisited
            if ( visitedNodes.contains(map.getNodeAt(i)) == false) {

                //1. if the dist between this unvisited city and the current city is lower than the current lowest, then...
                if (map.distArray[currentI][i] < nearestCurrDist) {
                    nearestCurrDist = map.distArray[currentI][i];
                    indexCurr = i;
                }

                //2. if the dist between this unvisited city and the start city is lower than the current lowest, then...
                if (map.distArray[firstI][i] < nearestStartDist) {
                    nearestStartDist = map.distArray[firstI][i];
                    indexStart = i;
                }

            }

        }

        //3. Get MST of unvisited cities
        ArrayList<Edge> minEdges = new ArrayList<Edge>();
        float minimum = 9999999;
        int minI = 0, minJ = 0;

        //go through entire map distArray
        for (int i=0; i < map.getDim(); i++){
            //mapCity number i
            if ( visitedNodes.contains(map.getNodeAt(i)) == false){
                //map city number j
                //j = i + 1 because [i][i] would be zero
                //this loop ensures only the top right diagonal half of the distance array is checked
                for (int j=i+1; j < map.getDim(); j++){
                    if ( visitedNodes.contains(map.getNodeAt(j)) == false) {
                        if (minimum > map.distArray[i][j]){

                            minimum = map.distArray[i][j];
                            minI = i;
                            minJ = j;
                        }
                        
                    }
                } //end for
                Edge edge1 = new Edge(map.getNodeAt(minI), map.getNodeAt(minJ));
                edge1.setDistance(minimum);
                System.out.println("Edge with distance "+ minimum);
                if (minEdges.isEmpty()){
                    minEdges.add(edge1);
                    System.out.println("Edge has been added to minEdges");
                } else {

                    if (minEdges.contains(edge1) == false){
                        minEdges.add(edge1);
                    }
                    /*
                    for (int k = 0; k < minEdges.size(); k++){
                        System.out.println("Map node index: " + map.getNodeAt(j).getIndex());

                        /*
                        //for each existing edge that exists
                        //if the edge i is equal to that edge's first node and the edge j is equal to that edge's second node, then don't make this new edge
                        if ( (minEdges.get(k).getNode1().equals(map.getNodeAt(i)) == false) && ( minEdges.get(k).getNode2().equals(map.getNodeAt(j))==false ) ){        
                            minEdges.add(edge1);
                            System.out.println("Edge has been added to minEdges");
                        }
                        
                    }*/
                }
            }

                 // end if
            
            //reset minimum so that the second, third, etc... lowest edges can be added
            minimum = 9999999;
        }

        int mstDist = 0;
        //get length of minEdges
        for (int i = 0; i < minEdges.size(); i++){
            mstDist += minEdges.get(i).getDistance();
        }
        System.out.println("mstDist: " + mstDist);

        this.heuristic = (int)(Math.ceil(nearestCurrDist + mstDist + nearestStartDist));

        return this.heuristic;
    }

    public int getHeuristic(){
        return heuristic;
    }

    public void calcEvalFunction(SearchMap map){
        //f(n) = g(n) + h(n)
        //g(n) is the total distance from state n to the start state
        //h(n) is the heuristic, which will be the nearest neighbour
        float result = calcGFunction(map);
        result += calcHeuristic(map);
        
        this.evalFunc = (int)(Math.ceil(result));
    }

    public int getEvalFunc(){
        return this.evalFunc;
    }


}