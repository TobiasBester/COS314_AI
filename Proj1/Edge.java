public class Edge {

    private int distance = 0;
    private SearchNode node1, node2;

    public Edge(SearchNode node1, SearchNode node2){
        this.node1 = this.node2;
    }

    public void setDistance(float distance){
        this.distance = (int)(Math.ceil(distance));
    }

    public int getDistance(){
        return distance;
    }

    public SearchNode getNode1(){
        return node1;
    }

    public SearchNode getNode2(){
        return node2;
    }

}