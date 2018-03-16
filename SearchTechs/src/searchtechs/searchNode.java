/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchtechs;

/**
 *
 * @author tbest
 */
public class searchNode {
    protected int val;
    protected searchNode left, right;
    
    public searchNode(int val) {
        this.val = val;
        left = null;
        right = null;
    }
    
    public int value(){
        return this.val;
    }
}
