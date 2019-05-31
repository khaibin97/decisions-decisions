import java.util.*;

public class Hanoi {
    
    public static List<String> getSuccessor(String[] hanoi) {
        // TO DO
        // Implement the getSuccessor method
        List<String> result = new ArrayList<>();
        String disc;
        String peg;
        String print = "";
        for(int i = 0; i < hanoi.length; i++) {
            disc = Character.toString(hanoi[i].charAt(0));
            
            if(disc.compareTo("0") != 0) {
                int i2,j2;
                if (i == 0) {i2 = i;} //bound checking
                else {i2 = i-1;}
                if (i+1>=hanoi.length) {j2 = hanoi.length-1;}
                else {j2 = i+1;}
                for( int j = i2 ; j <= j2; j++) {//ternary operator for bound checking
                    //not same rod & valid move
                    if(i != j) {
                        if (hanoi[j].compareTo("0") == 0 ||  disc.compareTo(Character.toString(hanoi[j].charAt(0))) < 0) {
                            
                            for(int k = 0; k < hanoi.length; k++) {
                                if(k == i) { //where the disc is from
                                    peg = hanoi[i].substring(1);//remove disc
                                    if(peg == null || peg.isEmpty()) peg = "0";
                                    print += " " + peg;
                                }else if( k == j) { //where the disc go
                                    if(hanoi[j].compareTo("0") == 0) //empty peg
                                        print += " " + disc;
                                    else
                                        print += " " + disc + hanoi[j]; //add disc
                                }else{
                                    print += " " + hanoi[k]; //store untouched peg
                                }
                                
                            }
                            
                            result.add(print.trim());
                            print = "";
                            
                        }
                    }
                    
                }
            }
            
        }
        
        return result;    
    }

    public static void main(String[] args) {
        if (args.length < 3) {
//            String [] argsq = {"24","0","1","0","56","0","9"}; //test case
//            List<String> sucessors = getSuccessor(argsq);
//            for (int i = 0; i < sucessors.size(); i++) {
//                System.out.println(sucessors.get(i));
//            }
//            return;
        }
        
        List<String> sucessors = getSuccessor(args);
        for (int i = 0; i < sucessors.size(); i++) {
            System.out.println(sucessors.get(i));
        }    
    }
 
}