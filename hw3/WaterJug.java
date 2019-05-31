import java.util.*;

class State {
    int capjug1;
    int capjug2;
    int currjug1;
    int currjug2;
    int goal;
    int depth;
    State parentPt;
    static List<State> open = new ArrayList<State>();
    static List<State> close = new ArrayList<State>();

    public State(int cap_jug1, int cap_jug2, int curr_jug1, int curr_jug2, int goal, int depth) {
        this.capjug1 = cap_jug1;
        this.capjug2 = cap_jug2;
        this.currjug1 = curr_jug1;
        this.currjug2 = curr_jug2;
        this.goal = goal;
        this.depth = depth;
        this.parentPt = null;
    }
    
    //calculate how much to pour from left to right
    private int pour(int left, int right, int max) {
        int fill = max-right;
        if(left<=fill) {
            return left;
        }else {
            return fill;
        }
    }
    //better comparator
    private boolean contains(List<State> list, State s) {
        for(State c : list) {
            if(c.currjug1 == s.currjug1 && c.currjug2 == s.currjug2) {
                return true;
            }
        }
        return false;
    }
    
    //build private global stack(list) //1 for queue, 2 for stack
    public void buildStack(State [] sa) {
        for(State s : sa) {
            if(s != null && !(contains(open,s)||contains(close,s))) {
                open.add(s);
            }
        }
    }
    
    public State[] getSuccessors(State curr) {
        // TO DO: get all successors and return them in proper order
        State [] successors = new State [6];
        
        if(currjug1!=0) {
        //e1
            successors[0] = new State(capjug1, capjug2, 0, currjug2, goal,depth);
            successors[0].parentPt = curr;
            successors[0].depth = curr.depth+1;
        //p12
            int pour = pour(currjug1, currjug2, capjug2);
            successors[1] = new State(capjug1, capjug2, currjug1-pour, currjug2+pour, goal,depth);
            successors[1].parentPt = curr;
            successors[1].depth = curr.depth+1;
        }
        
        if(currjug2!=0) {
            //e2
                successors[2] = new State(capjug1, capjug2, currjug1, 0, goal,depth);
                successors[2].parentPt = curr;
                successors[2].depth = curr.depth+1;
        }
        
        //f2
        successors[3] = new State(capjug1, capjug2, currjug1, capjug2, goal,depth);
        successors[3].parentPt = curr;
        successors[3].depth = curr.depth+1;
        
        if(currjug2!=0) {
            //p21
                int pour = pour(currjug2, currjug1, capjug1);
                successors[4] = new State(capjug1, capjug2, currjug1+pour, currjug2-pour, goal,depth);
                successors[4].parentPt = curr;
                successors[4].depth = curr.depth+1;
        }
        
        //f1
        successors[5] = new State(capjug1, capjug2, capjug1, currjug2, goal,depth);
        successors[5].parentPt = curr;
        successors[5].depth = curr.depth+1;
        
        return successors;
    }

    public boolean isGoalState() {
        // TO DO: determine if the state is a goal node or not and return boolean
        boolean isGoal = false;
        if(currjug1 == goal || currjug2 == goal)
            isGoal = true;
        return isGoal;
    }
    
    private String printList() {
        String list = "[";
        for(State s: open) {
            list += (s.getOrderedPair()+",");
        }
        if(open.isEmpty())  list = list + "] [";
        else                list = list.substring(0,list.length()-1) + "] [";//remove last comma
        
        for(State s: close) {
            list += (s.getOrderedPair()+",");
        }
        if(close.isEmpty()) list = list+ "] [";
        else                list = list.substring(0,list.length()-1) + "]";//remove last comma
        
        return list;
    }
    
    public void printHelp(State s,int flag) {
        if(s != null) {
            String print = (s.getOrderedPair()+" ");

            switch (flag) {
                case 1:
                    System.out.println(print);
                    break;
                case 2:
                    System.out.println(print+isGoalState());
                    break;
                case 3:
                case 4:
                case 5:
                    System.out.println(print+printList());
                    break;
            }
        }
    }
    public void printState(int option, int depth) {

        // TO DO: print a State based on option (flag)
        State thisState = new State(capjug1,capjug2,currjug1,currjug2,goal,this.depth);
        State[] sa= getSuccessors(thisState);
        
        switch (option) {
            case 1:
                for(State s: sa)
                    printHelp(s,option);
                break;
                
            case 2:
                for(State s: sa)
                    printHelp(s,option);
                break;
                
            case 3:
            case 4:
            case 5:
                UninformedSearch.run(thisState,option,depth);
                break;
                
            default:
        }

    }

    public String getOrderedPair() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.currjug1);
        builder.append(this.currjug2);
        return builder.toString().trim();
    }
    
    public void printPath(State s) {
        State curr = s;
        String str = "";
        while(curr != null) {
            str = curr.getOrderedPair()+ " " + str;
            curr = curr.parentPt;
        }
        System.out.println("Path " + str);
    }

    // TO DO: feel free to add/remove/modify methods/fields

}

class UninformedSearch {
    private static void bfs(State curr) {
        // TO DO: run breadth-first search algorithm
        curr.buildStack(curr.getSuccessors(curr));
        
        if(curr.isGoalState()) 
            System.out.println(curr.getOrderedPair()+" Goal");//initial is goal state
        else
            System.out.println(curr.getOrderedPair());//initial
        
        curr.close.add(curr);
        while(!curr.open.isEmpty()&&!curr.isGoalState()) {
            curr.buildStack(curr.getSuccessors(curr));
            curr.printHelp(curr, 3);
            curr = curr.open.get(0);
            curr.close.add(curr.open.remove(0));
        }
        
        if(curr.isGoalState()) {
            System.out.println(curr.getOrderedPair() + " Goal");
            curr.printPath(curr);
        }
    }

    private static void dfs(State curr) {
        // TO DO: run depth-first search algorithm
        curr.buildStack(curr.getSuccessors(curr));
        
        if(curr.isGoalState()) 
            System.out.println(curr.getOrderedPair()+" Goal");//initial is goal state
        else
            System.out.println(curr.getOrderedPair());//initial
        
        curr.close.add(curr);
        while(!curr.open.isEmpty()&&!curr.isGoalState()) {
            curr.buildStack(curr.getSuccessors(curr));
            curr.printHelp(curr, 4);
            curr = curr.open.get(curr.open.size()-1);
            curr.close.add(curr.open.remove(curr.open.size()-1));
        }
        
        if(curr.isGoalState()) {
            System.out.println(curr.getOrderedPair() + " Goal");
            curr.printPath(curr);
        }
    }

    private static void iddfs(State curr, int depth) {
        // TO DO: run IDDFS search algorithm
        for(int i = 0; i <= depth; i++) {

            if(curr.isGoalState()) 
                System.out.println(i+":"+curr.getOrderedPair()+" Goal");//initial is goal state
            else
                System.out.println(i+":"+curr.getOrderedPair());//initial

            curr.close.add(curr);
            State currState = curr;
            while(!currState.isGoalState()) {
                if(currState.depth < i)
                    curr.buildStack(currState.getSuccessors(currState));
                System.out.print(i+":");
                curr.printHelp(currState, 5);
                if(!curr.open.isEmpty()) {
                    currState = curr.open.get(curr.open.size()-1);
                    curr.close.add(curr.open.remove(curr.open.size()-1));
                } else {
                    break;
                }
            }

            if(currState.isGoalState()) {
                System.out.println(i+":"+currState.getOrderedPair() + " Goal");
                curr.printPath(currState);
                return;
            }
            curr.open.clear(); curr.close.clear();
        }
    }

    public static void run(State curr_state, int option, int depth) {
        // TO DO: run either bfs, dfs or iddfs according to option (flag)
        switch(option) {
            case 3:
                bfs(curr_state);
                break;
            case 4:
                dfs(curr_state);
                break;
            case 5:
                iddfs(curr_state, depth);
                break;
            default:
        }
    }
}

public class WaterJug {
    public static void main(String args[]) {
        if (args.length != 6) {
//            State init = new State(4, 3, 0, 0, 2, 0);
//            init.printState(5, 55);
            System.out.println("Invalid Number of Input Arguments");
            return;
        }
        int flag = Integer.valueOf(args[0]);
        int cap_jug1 = Integer.valueOf(args[1]);
        int cap_jug2 = Integer.valueOf(args[2]);
        int curr_jug1 = Integer.valueOf(args[3]);
        int curr_jug2 = Integer.valueOf(args[4]);
        int goal = Integer.valueOf(args[5]);

        int option = flag / 100;
        int depth = flag % 100;

        if (option < 1 || option > 5) {
            System.out.println("Invalid flag input");
            return;
        }
        if (cap_jug1 > 9 || cap_jug2 > 9 || curr_jug1 > 9 || curr_jug2 > 9) {
            System.out.println("Invalid input: 2-digit jug volumes");
            return;
        }
        if (cap_jug1 < 0 || cap_jug2 < 0 || curr_jug1 < 0 || curr_jug2 < 0) {
            System.out.println("Invalid input: negative jug volumes");
            return;
        }
        if (cap_jug1 < curr_jug1 || cap_jug2 < curr_jug2) {
            System.out.println("Invalid input: jug volume exceeds its capacity");
            return;
        }
        State init = new State(cap_jug1, cap_jug2, curr_jug1, curr_jug2, goal, 0);
        init.printState(option, depth);
    }
}