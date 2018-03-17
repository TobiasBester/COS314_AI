import java.util.ArrayList;

public class SearchSolution {

    SearchSolution() {

    }

    /*
    public static String WFIAlgo (SearchMap map){
        SearchMap weight = map.clone();
        int v = weight.getDim();
        System.out.println("WFI");
        StringBuilder matrixPrint = new StringBuilder("");

        for (int i = 0; i < v; i++){
            for (int j =0; j < v; j++) {
                for (int k = 0; k < v; k++){
                    if (weight.distArray[j][k] > weight.distArray[j][i] + weight.distArray[i][k]){
                        weight.distArray[j][k] = weight.distArray[j][i] + weight.distArray[i][k];
                    }
                }
            }
        }

        for (int i = 0; i < v; i++){
            for (int j = 0; j < v; j++){
                matrixPrint.append("[" + weight.distArray[i][j] + "]");
            }
            matrixPrint.append("\n");
        }

        return matrixPrint.toString();

    } */

    public static String bfs(SearchMap map, int startNodeIndex){
        boolean success = false;
        int totalNumCities = map.getDim();

        //open = [Start]
        State startOfJourney = new State();
        startOfJourney.addVisitedNode(map.getNodeAt(startNodeIndex));
        ArrayList<State> open = new ArrayList<State>();
        open.add(startOfJourney);

        //closed = []
        ArrayList<State> closed = new ArrayList<State>();

        int iterationOfWhile = 1;
        //while (open != []) AND (State != success) 
        while ( (open.isEmpty() == false) && (success == false) ){
            //remove leftmost state from open, call it X;
            State x = open.remove(0);
            System.out.println(x.printPath());

            //if X is the goal, then Success
            if (x.getNumVisitedNodes() == map.getDim()) {
                success = true;
                System.out.println("Found a solution at while iteration " + iterationOfWhile++);
                System.out.println("Distance Traveled: " + x.getTotalDistanceTravelled(map));
            } else {

                System.out.println("Did not find a solution at while iteration " + iterationOfWhile++);

                //GENERATE CHILDREN OF X
                //go through all current city's possible next destinations
                //total number of cities - visited cities
                int citiesRemaining = totalNumCities - x.getNumVisitedNodes();
                System.out.println("Cities remaining: " + citiesRemaining);

                ArrayList<State> childrenOfX = new ArrayList<State>();
                for (int i = 0; i < totalNumCities; i++){
                    State possibleChild = x.clone();                    //create a possible new route
                    //if this new route doesn't currently contain the next city in map, then add it to the route of possibleChild
                    if (possibleChild.contains( map.getNodeAt(i) ) == false){
                        possibleChild.addVisitedNode(map.getNodeAt(i));
                        //then add this possibleChild to the list of children
                        childrenOfX.add(possibleChild);
                        //System.out.println("Added Node " + map.getNodeAt(i).getIndex() + " of the map as a child of X");
                    }
                }

                //PUT X ON CLOSED
                closed.add(x);

                //ELIMINATE THE CHILDREN OF X ON OPEN OR CLOSED
                for (int i = 0; i < childrenOfX.size(); i++){
                    if (open.contains(childrenOfX.get(i))){
                        childrenOfX.remove(i);
                        break;
                    }
                    if (closed.contains(childrenOfX.get(i))){
                        childrenOfX.remove(i);
                        break;
                    }
                }

                //PUT REMAINING CHILDREN ON RIGHT END OF OPEN
                for (int i = 0; i < childrenOfX.size(); i++){
                    open.add(childrenOfX.get(i));
                    //System.out.println("Added child to OPEN");
                }

            }   //end else

        }   //endwhile
         
        if (success){
            return "Found it!";
        } else {
            return "Did not find it";
        }
    }

}