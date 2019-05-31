import java.util.*;
import java.lang.*;
import java.io.*;


public class NeuralNet{

    private static double zFunc(double [] a, double[] w){
        double sumOfAW = 0.0;
        for(int i = 0; i < w.length; i++){
                sumOfAW += (a[i] * w[i]);
        }
        return sumOfAW;
    }

    private static double gFunc(double z){
        return 1/(1+Math.pow(Math.E, (-z)));
    }

    private static List<List<Double>> readCSV(String filename){
        //read .csv and convert it to double
        List<List<Double>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                List<Double> values = new ArrayList<>();
                for(String s: temp){
                    values.add(Double.valueOf(s));
                }
                records.add(values);
            }
        }catch(IOException e){
            System.out.println("IOException when reading " + filename);
        }
        return records;
    }

    public static void main(String[] args){
        int flag = 0;
        try{
            flag = Integer.valueOf(args[0]);
        } catch (Exception e){
            System.out.println("Exception occured FLAG not set");
            return;
        }

        //2d-array storing weights
        double[][] w = {
                        { 0 },
                        { Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]), Double.valueOf(args[4]), Double.valueOf(args[5])}, //w10(2) ... w14(2)
                        { Double.valueOf(args[6]), Double.valueOf(args[7]), Double.valueOf(args[8]), Double.valueOf(args[9]), Double.valueOf(args[10])}, //w20(2) ... w24(2)
                        { Double.valueOf(args[11]), Double.valueOf(args[12]), Double.valueOf(args[13])} //w10(3) ... w12(3)
                       };


        //variable declarations
        double[] a1;//hidden layer 1
        double[] a2;//hidden layer 2
        double a3;//output layer
        double y, delta3;//for flag 400 
        double delta21, delta22;//for flag 500 
        double[][] pD; // partial deravatives 
        double n; //learning rate
        String s = "";//for printing

        switch (flag){
            case 100://                                a (1)                    a (1)
                //a layer 1       bias                  1                        2            ...
                a1 = new double[] { 1.0, Double.valueOf(args[14]), Double.valueOf(args[15]), Double.valueOf(args[16]), Double.valueOf(args[17])};
                //layer 2
                a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                //layer 3
                a3 = gFunc( zFunc(a2, w[3]) );

                System.out.printf("%.5f %.5f\n%.5f\n", a2[1], a2[2], a3);
                break;

            case 200:
                //a layer 1       bias                 a1(1)                     a2(1) ... //same format as above
                a1 = new double[] { 1.0, Double.valueOf(args[14]), Double.valueOf(args[15]), Double.valueOf(args[16]), Double.valueOf(args[17])};
                //layer 2 
                a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                //layer 3
                a3 = gFunc( zFunc(a2, w[3]) );

                y = Double.valueOf(args[18]);

                delta3 = (a3-y)*a3*(1-a3); //delta1(3)

                System.out.printf("%.5f\n", delta3);
                break;
            
            case 300:
                //a layer 1   bias                 a1(1)                     a2(1) ...
                a1 = new double[] { 1.0, Double.valueOf(args[14]), Double.valueOf(args[15]), Double.valueOf(args[16]), Double.valueOf(args[17])};
                //layer 2 
                a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                //layer 3
                a3 = gFunc( zFunc(a2, w[3]) );

                y = Double.valueOf(args[18]);

                delta3 = (a3-y)*a3*(1-a3);

                delta21 = delta3 * w[3][1] * a2[1] * (1-a2[1]); //delta1(2) or delta21 = delta, layer 2, 1
                delta22 = delta3 * w[3][2] * a2[2] * (1-a2[2]); //delta2(2) or delta22 = delta, layer 2, 2

                System.out.printf("%.5f %.5f\n", delta21, delta22);                
                break;

            case 400:
                //a layer 1   bias                 a1(1)                     a2(1) ...
                a1 = new double[] { 1.0, Double.valueOf(args[14]), Double.valueOf(args[15]), Double.valueOf(args[16]), Double.valueOf(args[17])};
                //layer 2 
                a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                //layer 3
                a3 = gFunc( zFunc(a2, w[3]) );

                y = Double.valueOf(args[18]);

                delta3 = (a3-y)*a3*(1-a3);

                delta21 = delta3 * w[3][1] * a2[1] * (1-a2[1]);
                delta22 = delta3 * w[3][2] * a2[2] * (1-a2[2]);

                pD = new double[][] {
                                        {0.0},
                                        { (delta3*a2[0]), (delta3*a2[1]), (delta3*a2[2]) }, //pD w(3)10, w(3)11 ...
                                        { (delta21*a1[0]), (delta21*a1[1]), (delta21*a1[2]), (delta21*a1[3]), (delta21*a1[4]) },//pD w(2)10 ...
                                        { (delta22*a1[0]), (delta22*a1[1]), (delta22*a1[2]), (delta22*a1[3]), (delta22*a1[4]) }//pD w(2)20 ...
                                    };
                
                for(int i = 1; i < pD.length; i++){
                    for(int j = 0; j < pD[i].length; j++){
                        s += String.format("%.5f ", pD[i][j]);
                    }
                    s = s.trim();//remove trailing whitespace
                    System.out.println(s);
                    s = "";//refresh
                }
                break;

            case 500:
            case 600://so that flag 600 can use the trained weights
                List<List<Double>> trainSet = readCSV("train.csv");
                List<List<Double>> evalSet = readCSV("eval.csv");
                n = Double.valueOf(args[14]);
                
                for(List<Double> train: trainSet){
                    //a layer 1        bias     a1(1)          a2(1) ...
                    a1 = new double[] { 1.0, train.get(0), train.get(1), train.get(2), train.get(3)};
                    //layer 2 
                    a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                    //layer 3
                    a3 = gFunc( zFunc(a2, w[3]) );

                    y = train.get(4);

                    delta3 = (a3-y)*a3*(1-a3);

                    delta21 = delta3 * w[3][1] * a2[1] * (1-a2[1]);
                    delta22 = delta3 * w[3][2] * a2[2] * (1-a2[2]);

                    //edited to match the format of w[][]
                    pD = new double[][] {
                                            {0.0},
                                            { (delta21*a1[0]), (delta21*a1[1]), (delta21*a1[2]), (delta21*a1[3]), (delta21*a1[4]) },//pD w(2)10 ...
                                            { (delta22*a1[0]), (delta22*a1[1]), (delta22*a1[2]), (delta22*a1[3]), (delta22*a1[4]) },//pD w(2)20 ...
                                            { (delta3*a2[0]), (delta3*a2[1]), (delta3*a2[2]) } //pD w(3)10, w(3)11 ...
                                        };
                    
                    //updating weights
                    for(int i = 1; i < w.length; i++){
                        for(int j = 0; j < w[i].length; j++){
                            w[i][j] = w[i][j] - (n * pD[i][j]);
                            if(flag ==500)
                                s += String.format("%.5f ", w[i][j]);
                        }
                    }
                    if(flag == 500){
                        s = s.trim();//remove trailing whitespace
                        System.out.println(s);
                        s = "";//refresh
                    }

                    double sumError = 0.0;
                    //error evaluation 
                    for(List<Double> eval: evalSet){
                        a1 = new double[] { 1.0, eval.get(0), eval.get(1), eval.get(2), eval.get(3)};
                        a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                        a3 = gFunc( zFunc(a2, w[3]) );
                        y = eval.get(4);

                        sumError += Math.pow((a3-y),2)/2;
                    }
                    if(flag == 500)
                        System.out.printf("%.5f\n",sumError);
                }

                if(flag == 500)
                    break;
                
                //case 600 
                List<List<Double>> testSet = readCSV("test.csv");
                double accuracy = 0;
                double x = 0;

                for(List<Double> test: testSet){
                    a1 = new double[] { 1.0, test.get(0), test.get(1), test.get(2), test.get(3)};
                    a2 = new double [] { 1.0 , gFunc( zFunc(a1, w[1]) ), gFunc( zFunc(a1, w[2]) )};
                    a3 = gFunc( zFunc(a2, w[3]) );//confidence of prediction
                    y = test.get(4);//Actual label
                    
                    //prediction
                    if(a3 <= 0.5){
                        x = 0;
                    }else{
                        x = 1;
                    }

                    System.out.printf("%.0f %.0f %.5f\n", y, x, a3);

                    //count number of hits
                    if(x == y){
                        accuracy++;
                    }                    
                }

                accuracy = accuracy/testSet.size();
                System.out.printf("%.2f\n", accuracy);
                break;

            default:
                System.out.println("Flag not Recognized, Flag = " + flag);
        }

    }
}