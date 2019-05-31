import java.util.*;
import java.lang.Math;

class State {
    char[] board;
    int heur;
    static List<State> succ = null;

    public State(char[] arr) {
        this.board = Arrays.copyOf(arr, arr.length);
        this.heur = heuristic();
    }
    
    public void printState(int option, int iteration, int seed) {

        // TO DO: print output based on option (flag)
        switch(option) {
            case 1:
                System.out.println(heur);
                break;
            case 2:
                getSucc();
                if(heur != 0) {
                    for(State s : succ)
                        System.out.println(s.print());
                    System.out.println(succ.get(0).heur);
                }
                break;
            case 3:
                System.out.printf("%d:%s %d\n",0,print(),heur);//initial board
                if(heur == 0) {//initial is goal
                    System.out.println("Solved");
                    return;
                }
                
                getSucc();
                Random rng = new Random();
                if(seed!=-1) rng.setSeed(seed);
                for (int i = 1; i<=iteration; i++) {
                    int r = rng.nextInt(succ.size());
                    State move = succ.get(r);
                    System.out.println(i+":"+move.print()+" "+move.heur);
                    if(move.heur == 0) {
                        System.out.println("Solved");
                        return;
                    }
                    succ = null;//clear the successor list
                    move.getSucc();
                }
                break;
            case 4:
                System.out.printf("%d:%s %d\n",0,print(),heur);//initial board
                if(heur == 0) {//initial is goal
                    System.out.println("Solved");
                    return;
                }
                
                succ = fchc();
                int currHeur = this.heur;
                for (int i = 1; i<=iteration; i++) {
                    State move = succ.get(succ.size() -1);//last element == first heuristic < current
                    if(move.heur == 0) {
                        System.out.println(i+":"+move.print()+" "+move.heur);
                        System.out.println("Solved");
                        return;
                    } else if(move.heur < currHeur) {
                        System.out.println(i+":"+move.print()+" "+move.heur);
                        succ = move.fchc();
                        currHeur = move.heur;
                    } else {
                        System.out.println("Local optimum");
                        break;
                    }
                }
                break;
            case 5:
                simulatedAnnealing(iteration,seed);
        }
    }
    private String print() {
        return Arrays.toString(board).replace("[","").replace("]","").replace(",","").replace(" ","");
    }
    private void getSucc() {
        char[] temp = Arrays.copyOf(board, board.length);
        for(int col = 0; col<board.length; col++) {
            for(int i = 0; i < board.length; i++) {
                if(i==Character.getNumericValue(board[col]))
                    continue;
                temp[col] = (char) (i + '0');
                State s = new State(temp);
                if(succ == null) {
                    succ = new ArrayList<State>();
                    succ.add(s);
                }else {
                    if(s.heur < succ.get(0).heur) {
                        succ = new ArrayList<State>();
                        succ.add(s);
                    }else if(s.heur == succ.get(0).heur){
                        succ.add(s);
                    }
                }
            }
            temp[col] = board[col];
        }
        
    }
    
    private ArrayList<State> fchc(){
        ArrayList<State> arr = new ArrayList<State>();
        char[] temp = Arrays.copyOf(board, board.length);
        for(int col = 0; col<board.length; col++) {
            for(int i = 0; i < board.length; i++) {
                if(i==Character.getNumericValue(board[col]))
                    continue;
                temp[col] = (char) (i + '0');
                State s = new State(temp);
                arr.add(s);
                if(s.heur < heur) return arr;//already found first choice
            }
            temp[col] = board[col];
        }
        return arr;
    }
    
    private void simulatedAnnealing(int iteration, int seed) {
        System.out.printf("%d:%s %d\n",0,print(),heur);//initial board
        if(heur == 0) {//initial is goal
            System.out.println("Solved");
            return;
        }
        Random rng = new Random();
        if(seed!=-1) rng.setSeed(seed);
        double T = 100;
        int currH = heur;
        char[] temp = Arrays.copyOf(board, board.length);
        for (int i = 1; i <= iteration; i ++) {
            if (T < 1) { //T == 0
                return;
            }
            int index = rng.nextInt(7);
            int value = rng.nextInt(7);
            double prob = rng.nextDouble();
            //next random state
            temp[index] = (char) (value+'0');
            State next = new State(temp);

            int delta = next.heur - currH;
            if(delta < 0) {
                System.out.println(i+":"+next.print()+" "+next.heur);
                currH = next.heur;
                if(next.heur == 0) {
                    System.out.println("Solved");
                    return;
                }
            }else {
                if(prob < Math.exp((delta/T))) {
                    System.out.println(i+":"+next.print()+" "+next.heur);
                    currH = next.heur;
                    if(next.heur == 0) {    
                        System.out.println("Solved");
                        return;
                    }
                }else {
                    i--;
                }
            }
        }
    }
    
    //return heuristic score of this board
    private int heuristic() {
        int conflict = 0;
        //same column impossible
        //check same row
        for(int col1 = 0 ; col1 < board.length; col1++) {//first point
            for(int col2 = col1+1 ; col2 < board.length; col2++) {//second point
                if(board[col1] == board[col2]) {//same row
                    conflict++;
                    continue;
                }
                if( Math.abs(col1-col2) == Math.abs(board[col1]-board[col2]) ) {//check diagonals |x1-x2|==|y1-y2|
                    conflict++;
                }
            }
        }
        return conflict;
    }
}

public class EightQueen {
    public static void main(String args[]) {
        if (args.length != 2 && args.length != 3) {
//            int flag = 599;                                             //flag
//            int option = flag / 100;
//            int iteration = flag % 100;
//            char[] board = new char[8];
//            int seed = 1;                                              //seed
//            String s = "36073152";                                      //board
//            for (int i = 0; i < 8; i++) {
//                board[i] = s.charAt(i);
//                int pos = board[i] - '0';
//                if (pos < 0 || pos > 7) {
//                    System.out.println("Invalid input: queen position(s)");
//                    return;
//                }
//            }
//            State init = new State(board);
//            init.printState(option, iteration, seed);
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        int flag = Integer.valueOf(args[0]);
        int option = flag / 100;
        int iteration = flag % 100;
        char[] board = new char[8];
        int seed = -1;
        int board_index = -1;

        if (args.length == 2 && (option == 1 || option == 2 || option == 4)) {
            board_index = 1;
        } else if (args.length == 3 && (option == 3 || option == 5)) {
            seed = Integer.valueOf(args[1]);
            board_index = 2;
        } else {
            System.out.println("Invalid Number of Input Arguments");
            return;
        }

        if (board_index == -1) return;
        for (int i = 0; i < 8; i++) {
            board[i] = args[board_index].charAt(i);
            int pos = board[i] - '0';
            if (pos < 0 || pos > 7) {
                System.out.println("Invalid input: queen position(s)");
                return;
            }
        }

        State init = new State(board);
        init.printState(option, iteration, seed);
    }
}