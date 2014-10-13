/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mainpackage;

/**
 *
 * @author yashmsawant
 */
public class MainNFASimulation {

    
    public final static char EPSILON = ' ';
    public static int idCounter = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         *  Creating a NFA for the given regular expression
         */
        /**
         *  @BTW its an expr to generate combination of even a and even b 
         * 
         *  We have to maintain 3 pointers to viz.
         *  1) Redundant Epsilon node
         *  2) Starting node for subexpr
         *  3) Ending node of subexpr
         */
        /**
         *  @expr must be valid regular expression
         *  space between token is assumed to differ whether the symbols has 
         *  semantic meaning or it is constituents of expr
         */
        String expr = " (aa +bb ) * ( (ab +ba ) * "
                + "(ab +ba ) (aa +bb ) * ) *";
        /**
         *  Redundant starting Epsilon node 
         *
         **/
        Node start = new Node(idCounter);
        createGraph(expr, start);
    }

    /**
     *
     * @param expr
     * @param start
     * @return
     */
    public static Node createGraph(String expr, Node start) {
        int index = 0;
        Node epsilonNode = new Node(idCounter++);
        start.mapTo(epsilonNode, EPSILON);
        Node current = epsilonNode;
        while(index <= expr.length()) {
            if(index >= 1 && expr.charAt(index) == '(' && expr.charAt(index - 1) == ' ') {
                String subexpr = "";
                index = index + 1; // To skip current '('
                if(current != epsilonNode) {
                    epsilonNode = new Node(idCounter++);
                    current.mapTo(epsilonNode, EPSILON);
                    start = current;
                }
                //code for handling subexpr
                while(expr.charAt(index) != ')') {
                    
                    if(expr.charAt(index + 2) == ')' && 
                            expr.charAt(index + 1) == ' ') {
                        index = index + 2;
                        break;
                    }
                    else {
                        subexpr = subexpr + expr.charAt(index);
                    }
                    index = index + 1;
                }
                current = createGraph(subexpr, start);
            }
            if(index >= 1 && expr.charAt(index) == ')' && expr.charAt(index - 1) == ' ') {

                //code for returning the end pointer to caller
                assert(current != null);
                return current;
            }
            if(index >= 1 && expr.charAt(index) == '+' && expr.charAt(index - 1) == ' ') {
              
                //code for handling + operator
                start.mapTo(current, expr.charAt(index + 1));
                index = index + 1;
               
            }
            if(index >= 1 && expr.charAt(index) == '*' && expr.charAt(index - 1) == ' ') {

                //code for handling * operator
                current.mapTo(epsilonNode, EPSILON);
                epsilonNode.mapTo(current, EPSILON);
                index = index + 1;

            }
            if(Character.isAlphabetic(expr.charAt(index))) {
                //code for handling alphabet symbols
                // else part is handled at '+' itself
                if(index >= 1 && expr.charAt(index - 1) != '+') {
                    Node node = new Node(idCounter++);
                    current.mapTo(node, expr.charAt(index));
                    current = node;
                }
                else if(index == 0) {
                    Node node = new Node(idCounter++);
                    current.mapTo(node, expr.charAt(0));
                    current = node;
                }
                index = index + 1;
            }
            
        }
        return null;
    }
}
class Node {
    private final int id;
    Node(int id) {
        this.id = id;
    }
    public void mapTo(Node node, Character transitionAlphabet) {
        
    }
}