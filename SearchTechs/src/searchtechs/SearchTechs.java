/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchtechs;

import java.util.ArrayList;

/**
 *
 * @author tbest
 */
public class SearchTechs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Agasd");
        
        //Possible moves
        searchNode aNode = new searchNode(1);
        searchNode bNode = new searchNode(2);
        searchNode cNode = new searchNode(3);
        searchNode dNode = new searchNode(4);
        searchNode eNode = new searchNode(5);
        searchNode fNode = new searchNode(6);
        searchNode gNode = new searchNode(7);
        searchNode iNode = new searchNode(9);
        searchNode jNode = new searchNode(10);
        searchNode kNode = new searchNode(11);
        searchNode lNode = new searchNode(12);
        
        aNode.left = bNode;
        aNode.right = cNode;
        bNode.left = dNode;
        bNode.right = eNode;
        cNode.left = fNode;
        cNode.right = gNode;
        dNode.left = iNode;
        dNode.right = jNode;
        iNode.left = kNode;
        iNode.right = lNode;
        
        searchNode start = aNode;
        searchNode goal = jNode;
        System.out.println(dfs(start, goal));
    }
    
    public static Boolean dfs(searchNode start, searchNode end) {
        ArrayList<searchNode> open = new ArrayList();
        open.add(0, start);
        
        ArrayList<searchNode> closed = new ArrayList();
        
        Boolean found = false;
        searchNode x;
        
        while (!(open.isEmpty()) && found == false ){
            //remove leftmost state from open, call it X
            x = open.remove(0);
            System.out.println(x.value());
            
            //if x is the goal....
            if (x.equals(end)){
                found = true;
            } else {
                //generate children of x
                searchNode leftChild = x.left;
                searchNode rightChild = x.right;
                
                //put x on closed
                closed.add(x);
                
                //eliminate the children of x on open or closed
                if (!(open.contains(rightChild) || (closed.contains(rightChild)) || rightChild == null)  ) {
                    open.add(0, rightChild);
                }
                if (!(open.contains(leftChild) || (closed.contains(leftChild))  || leftChild == null)  ) {
                    open.add(0, leftChild);
                }
            }
        }
        
        return found;
    }
    
}
