import java.util.ArrayList;
import java.util.Collections;

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

    public static String dfsid(SearchMap map, int startNodeIndex){
        int searchDepth = 1;
        String response = "";
        while (response.equals("Found it!") == false) {
            System.out.println("");
            System.out.println("search depth = " + searchDepth);
            response = dfid(map, startNodeIndex, searchDepth);
            searchDepth++;
        }
        return response;
    }

    public static String dfid(SearchMap map, int startNodeIndex, int searchDepth){

        //This "depth first search" algorithm is actually a breadth first search tailored for iterative deepening
        //due to it stopping after expanding a certain amount of states. Ultimately, if the "dfsid" function is run,
        //the same solution will be found at the same time as an actual DFS with iterative deeping function.

        boolean success = false;
        int totalNumCities = map.getDim();
        int currentDepth = 1;

        //open = [Start]
        State startOfJourney = new State();
        startOfJourney.addVisitedNode(map.getNodeAt(startNodeIndex));
        ArrayList<State> open = new ArrayList<State>();
        open.add(startOfJourney);

        //closed = []
        ArrayList<State> closed = new ArrayList<State>();

        int iterationOfWhile = 0;
        int citiesLeft = totalNumCities - 1;
        int stopAtIteration = citiesLeft;
        int factorial = citiesLeft;

        //stopAtIteration is used to determine at which depth it should stop at by calculating its factorial
        //because each branching factor will decrease as the depth increases
        for (int i = 1; i < searchDepth; i++){
            factorial *= citiesLeft - i; 
            stopAtIteration += factorial;
        }

        //while (open != []) AND (State != success) 
        while ( (open.isEmpty() == false) && (success == false) && (iterationOfWhile <= stopAtIteration) ){
            //remove leftmost state from open, call it X;
            State x = open.remove(0);
            System.out.println(x.printPath());

            //if X is the goal, then Success
            if (x.getNumVisitedNodes() == map.getDim()) {
                success = true;
                System.out.println("Found a solution at state expansion " + iterationOfWhile++);
                System.out.println("Distance Traveled: " + x.getTotalDistanceTravelled(map));
            } else {

                System.out.println("Did not find a solution at state expansion " + iterationOfWhile++);

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

    public static String dfs(SearchMap map, int startNodeIndex){
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
            System.out.println("Path explored: " + x.printPath());

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
                    } else {
                        //System.out.println("Did not add Node " + map.getNodeAt(i).getIndex() + " as a child of X");
                    }
                }

                //PUT X ON CLOSED
                closed.add(x);

                //ELIMINATE THE CHILDREN OF X ON OPEN OR CLOSED
                for (int i = 0; i < childrenOfX.size(); i++){
                    if (open.contains(childrenOfX.get(i))){
                       // System.out.println("Eliminated child " + childrenOfX.get(i) + " from open");
                        childrenOfX.remove(i);
                        break;
                    }
                    if (closed.contains(childrenOfX.get(i))){
                        //System.out.println("Eliminated child " + childrenOfX.get(i) + " from closed");
                        childrenOfX.remove(i);
                        break;
                    }
                }

                //PUT REMAINING CHILDREN ON LEFT END OF OPEN
                for (int i = childrenOfX.size() - 1; i >= 0 ; i--){
                    open.add(0, childrenOfX.get(i));
                    //System.out.println("Added child " + childrenOfX.get(i) + " to OPEN");
                }

            }   //end else

        }   //endwhile
         
        if (success){
            return "Found it!";
        } else {
            return "Did not find it";
        }
    }

    public static String astar(SearchMap map, int startNodeIndex){
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
            System.out.println("Path explored: " + x.printPath());

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
                    } else {
                        //System.out.println("Did not add Node " + map.getNodeAt(i).getIndex() + " as a child of X");
                    }
                }

                //for each child of X do
                for (int i = 0; i < childrenOfX.size(); i++){
                    State child = childrenOfX.get(i);

                    //////if the child is not on open or closed
                    if (open.contains(child) == false && closed.contains(child) == false){
                        //assign the child a heuristic value
                        //add the child to open
                        child.calcEvalFunction(map);
                        
                        //if a state with the same number of visited nodes is in the OPEN list and has a lower evalFunc
                        //than child, then don't add the child

                        Boolean worthyChild = true;

                        /*
                        for (int j = 0; j < open.size(); j++){
                            //if the child has visited fewer nodes and has a higher eval func, then ignore it
                            if ( (child.getNumVisitedNodes() <= open.get(j).getNumVisitedNodes()) && ( child.getEvalFunc() > open.get(j).getEvalFunc()) ){
                                worthyChild = false;
                            }
                        }
                        
                        //if a state with the same number of visited nodes is in the CLOSED list and has a lower evalFunc than the child
                        //then don't add the child
                        for (int j = 0; j < closed.size(); j++){
                            //if the child has visited fewer nodes and has a higher eval func, then ignore it
                           /* if ( (child.getNumVisitedNodes() <= closed.get(j).getNumVisitedNodes()) && ( child.getEvalFunc() > closed.get(j).getEvalFunc()) ){
                                worthyChild = false;
                            }
                            if ( (child.getNumVisitedNodes() <= closed.get(j).getNumVisitedNodes()) && ( child.getEvalFunc() > closed.get(j).getEvalFunc()) ){
                                worthyChild = false;
                            }

                    }*/

                        if (worthyChild){
                            System.out.println("Child added to open has distance of : " + child.getGFunc());
                            open.add(child);
                        }
                    }

                }   //end for

                //put X on closed
                closed.add(x);

                //re-order states on open by evalFunc
                Collections.sort(open);

            }   //end else

        }   //endwhile
         
        if (success){
            return "Found it!";
        } else {
            return "Did not find it";
        }
    }

}