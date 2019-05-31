import java.util.*;

public class Number{
    static class Node { 
        int data; 
        Node parent, left, right; 
        public Node(int item) { 
            data = item; 
            parent = left = right = null; 
        } 
    } 
    
    static Node root;
    public static String getStep(int x, int y) {
        // TO DO
        // Implement the getStep method
        String result = "";
        ArrayList<Node> queue = new ArrayList<Node>();
        root = new Node(x);
        
        Node tnode = root;
        int temp;
        queue.add(tnode);
        while(tnode.data != y || !queue.isEmpty()) {
            tnode = queue.remove(0);
            temp = tnode.data;
            
            //enqueue left child
            if(tnode.data > y) {
                tnode.left = new Node(temp-1);
                queue.add(tnode.left);
                tnode.left.parent = tnode;
            } else {
                tnode.left = new Node(temp+1);
                queue.add(tnode.left);
                tnode.left.parent = tnode;
            }
            
            tnode.right = new Node(temp*3);
            queue.add(tnode.right);//enqueue right
            tnode.right.parent = tnode;
            
            //already have goal state
            if(tnode.left.data == y) {
                tnode = tnode.left;
                break;
            } else if (tnode.right.data == y) {
                tnode = tnode.right;
                break;
            }
        }
        
        //look for the path to goal state
        int count=0;
        while(tnode != root) {
            tnode = tnode.parent;
            count++;
        }
        return result+count;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
//        	System.out.println(getStep(1,15));
            return;
        }
        
        System.out.println(getStep(Integer.parseInt(args[0]), Integer.parseInt(args[1])));

    }
}