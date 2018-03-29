import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

////Tobias Bester - u14041368

public class searches {

    public static void main(String[] args) {

        startMessage();
        String piChoice = getProblemInstanceInput();
        System.out.println("You chose " + piChoice);

        SearchMap map1 = readFile(piChoice);

        System.out.println("Map " + map1.getName() + " has " + map1.getDim() + " cities.");

        searchMessage();
        String searchChoice = getSearchInput();
        
        Random randomNum = new Random();
        int randomNode = randomNum.nextInt(map1.getDim());

        long startTime = System.nanoTime();
        long stopTime;

        switch (searchChoice) {
            case "DFSID" :
                System.out.println("You chose " + searchChoice);
                System.out.println(SearchSolution.dfsid(map1, randomNode));
                stopTime = System.nanoTime();
                System.out.println("Execution completed in " + (stopTime - startTime) + " nanoseconds");
                break;
            case "BFS" :
                System.out.println("You chose " + searchChoice);
                System.out.println(SearchSolution.bfs(map1, randomNode));
                stopTime = System.nanoTime();
                System.out.println("Execution completed in " + (stopTime - startTime) + " nanoseconds");
                break;
            case "AStar" :
                System.out.println("You chose " + searchChoice); 
                System.out.println(SearchSolution.astar(map1, randomNode)) ;
                stopTime = System.nanoTime();
                System.out.println("Execution completed in " + (stopTime - startTime) + " nanoseconds");
                break;
            default : 
                System.out.println("The default search technique is the depth first search");
                System.out.println(SearchSolution.dfs(map1, randomNode));
                stopTime = System.nanoTime();
                System.out.println("Execution completed in " + (stopTime - startTime) + " nanoseconds");
                break;
        }

        //System.out.println(SearchSolution.WFIAlgo(map1));
    }

    public static void startMessage(){
        System.out.println("===============WELCOME==============");
        System.out.println("Pick a Problem Instance");
        System.out.println("Default is ex05.tsp");
        System.out.println("=======================");
        System.out.println("Enter 1 for dj38.tsp");
        System.out.println("Enter 2 for eil51.tsp");
        System.out.println("Enter 3 for wi29.tsp");
        System.out.println("=======================");
    }

    public static void searchMessage(){
        System.out.println("=======================");
        System.out.println("Pick a Search to apply");
        System.out.println("Default is Depth first search");
        System.out.println("=======================");
        System.out.println("Enter 1 for Depth first search with iterative deepening");
        System.out.println("Enter 2 for Breadth first search");
        System.out.println("Enter 3 for A* Algorithm");
        System.out.println("=======================");
    }

    public static String getProblemInstanceInput(){
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int i = 1;
        i = Integer.parseInt(s);

        switch (i) {
            case 1 : return "dj38.tsp";
            case 2 : return "eil51.tsp";
            case 3 : return "wi29.tsp";
            default : return "ex05.tsp";
        }
    }

    public static String getSearchInput(){
        Scanner in2 = new Scanner(System.in);
        String s = in2.nextLine();
        int i = 1;
        i = Integer.parseInt(s);
        in2.close();

        switch (i) {
            case 1 : return "DFSID";
            case 2 : return "BFS";
            case 3 : return "AStar";
            default : return "DFS";
        }

        
    }

    public static SearchMap readFile(String inFile) {
        String line = null;
        SearchMap resultMap = new SearchMap();

        try {
            FileReader fr = new FileReader(inFile);

            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                if (line.contains("NAME")){
                    String name = line.substring( line.indexOf(":")+2, line.length() );
                    resultMap.setName(name);
                }

                if (line.contains("DIMENSION")){
                    String dim = line.substring( line.indexOf(":")+2, line.length() );
                    resultMap.setDimension(Integer.parseInt(dim));
                }

                if (Character.isDigit(line.charAt(0))){
                    //get index of the two spaces
                    int space1 = line.indexOf(" ");
                    int space2 = line.indexOf(" " , space1 + 1);
                    int index = Integer.parseInt(line.substring(0, space1));
                    float xCoord = Float.parseFloat(line.substring(space1 + 1, space2));
                    float yCoord = Float.parseFloat(line.substring(space2 + 1, line.length()));

                    SearchNode newNode = new SearchNode(index, xCoord, yCoord);
                    resultMap.addNode(newNode);
                }
            }

            br.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file");
        } catch (IOException ex) {
            System.out.println("Error reading file");
        }

        resultMap.fillDistArray();
        return resultMap;
    }
}
