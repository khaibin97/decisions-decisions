import java.util.*;
import java.lang.*;
import java.io.*;

public class BodyVsBrain{

    //calculate mean
    private static double mean(ArrayList<Double> list){
        double sum = 0;
        for(Double num : list){
            sum+=num;
        }
        return sum/list.size();
    }
    //calculate sd
    private static double sd(ArrayList<Double> list){
        double mean = mean(list);
        double sd = 0;
        for(Double num : list){
            sd += Math.pow(num-mean, 2);
        }
        return Math.sqrt(sd/(list.size()-1));
    }
    //calculate MSE based on flags, w/ gradient descent etc.
    private static double MSE(double b0, double b1, ArrayList<Double> list1, ArrayList<Double> list2,int f,double n){//f for gradient descent
        double sumsquare = 0;
        double mse = 0;
        if(f == 0){ //MSE
            for(int i = 0; i < list1.size(); i++){
                sumsquare += Math.pow((b0 + b1*list1.get(i) - list2.get(i)),2);
            }
            mse = sumsquare/list1.size();

        } else if(f == 1){ //b0
            for(int i = 0; i < list1.size(); i++){
                sumsquare += b0 + b1*list1.get(i) - list2.get(i);
            }
            mse = 2*sumsquare/list1.size();

        } else if(f == 2){ //b1
            for(int i = 0; i < list1.size(); i++){
                sumsquare += (b0 + b1*list1.get(i) - list2.get(i))*list1.get(i);
            }
            mse = 2*sumsquare/list1.size();

        } else if(f == 3){ //b0t
            for(int i = 0; i < list1.size(); i++){
                sumsquare += b0 + b1*list1.get(i) - list2.get(i);
            }
            mse = 2*sumsquare/list1.size();
            mse = b0 - n*mse;

        } else if(f == 4){ //b1t
            for(int i = 0; i < list1.size(); i++){
                sumsquare += (b0 + b1*list1.get(i) - list2.get(i))*list1.get(i);
            }
            mse = 2*sumsquare/list1.size();
            mse = b1 - n*mse;

        }
        
        return mse;
    }

    public static void main (String args[]){
        int flag;
        try{
            flag = Integer.valueOf(args[0]);
        } catch (Exception e){
            System.out.println("Exception occured FLAG not set");
            return;
        }

        //read .csv
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
        }catch(IOException e){
            System.out.println("IOException when reading data.csv");
        }
        //convert to Double arrays 
        ArrayList<Double> body = new ArrayList<Double>();
        ArrayList<Double> brain = new ArrayList<Double>();
        records.remove(0);
        for(List<String> r : records){
            body.add(Double.parseDouble(r.get(0)));
            brain.add(Double.parseDouble(r.get(1)));
        }

        //"real" start of program
        double b1,b2,n;
        int T;
        switch(flag){
            case 100: // part 1
                double bodyMean = mean(body);
                double bodySD = sd(body);
                double brainMean = mean(brain);
                double brainSD = sd(brain);
                System.out.println(body.size());
                System.out.printf("%.4f %.4f\n",bodyMean,bodySD);
                System.out.printf("%.4f %.4f\n",brainMean,brainSD);
                break;

            case 200: //part 2
                b1 = Double.valueOf(args[1]);
                b2 = Double.valueOf(args[2]);
                System.out.printf("%.4f\n", MSE(b1, b2, body, brain, 0,0));//param n is 0 if not used
                break;
            
            case 300: //part3
                b1 = Double.valueOf(args[1]);
                b2 = Double.valueOf(args[2]);
                System.out.printf("%.4f\n", MSE(b1, b2, body, brain, 1,0));
                System.out.printf("%.4f\n", MSE(b1, b2, body, brain, 2,0));
                break;
            
            case 400: //part 4
                n = Double.valueOf(args[1]);
                T = Integer.valueOf(args[2]);

                b1 = 0; b2 = 0;
                double newb1 = 0;
                double newb2 = 0;
                for(int i = 1; i <= T; i++){
                    newb1 = MSE(b1, b2, body, brain, 3, n);
                    newb2 = MSE(b1, b2, body, brain, 4, n);
                    
                    System.out.printf("%d %.4f %.4f %.4f\n",i,newb1,newb2,MSE(newb1, newb2, body, brain, 0, 0));
                    b1 = newb1;
                    b2 = newb2;
                }
                break;

            case 500: //part 5
                bodyMean = mean(body);
                brainMean = mean(brain);
                double temp1 = 0; double temp2 = 0;
                for(int i = 0 ; i < body.size(); i++){
                    temp1 += (body.get(i)-bodyMean)*(brain.get(i)-brainMean);
                    temp2 += Math.pow((body.get(i)-bodyMean),2);
                }
                b2 = temp1/temp2;
                b1 = brainMean - b2*bodyMean;
                System.out.printf("%.4f %.4f %.4f\n",b1,b2,MSE(b1, b2, body, brain, 0, 0));
                break;
            
            case 600: //part 6
                double weight = Double.valueOf(args[1]);
                bodyMean = mean(body);
                brainMean = mean(brain);
                temp1 = 0; temp2 = 0;
                for(int i = 0 ; i < body.size(); i++){
                    temp1 += (body.get(i)-bodyMean)*(brain.get(i)-brainMean);
                    temp2 += Math.pow((body.get(i)-bodyMean),2);
                }
                b2 = temp1/temp2;
                b1 = brainMean - b2*bodyMean;
                System.out.printf("%.4f\n", b1+b2*weight);
                break;

            case 700: //part 7
                bodyMean = mean(body);
                bodySD = sd(body);
                //normalize
                ArrayList<Double> newbody = new ArrayList<Double>();
                for(int i = 0; i < body.size(); i++){
                    newbody.add((body.get(i)-bodyMean)/bodySD);
                }

                //from part 4(modified for newbody)
                n = Double.valueOf(args[1]);
                T = Integer.valueOf(args[2]);

                b1 = 0; b2 = 0;
                newb1 = 0; newb2 = 0;
                for(int i = 1; i <= T; i++){
                    newb1 = MSE(b1, b2, newbody, brain, 3, n);
                    newb2 = MSE(b1, b2, newbody, brain, 4, n);
                    System.out.printf("%d %.4f %.4f %.4f\n",i,newb1,newb2,MSE(newb1, newb2, newbody, brain, 0, 0));

                    b1 = newb1;
                    b2 = newb2;
                }
                break;
            
            case 800: //part 8
                Random rand = new Random();
                //from part 7(modified newb1 and newb2 for respective new gradient equations)
                bodyMean = mean(body);
                bodySD = sd(body);
                //normalize
                newbody = new ArrayList<Double>();
                for(int i = 0; i < body.size(); i++){
                    newbody.add((body.get(i)-bodyMean)/bodySD);
                }
                n = Double.valueOf(args[1]);
                T = Integer.valueOf(args[2]);

                b1 = 0; b2 = 0;
                newb1 = 0; newb2 = 0;
                int j = rand.nextInt(62);//random entry 
                for(int i = 1; i <= T; i++){
                    newb1 = b1 - n*(2*(b1 + b2*newbody.get(j) - brain.get(j)));
                    newb2 = b2 - n*(2*(b1 + b2*newbody.get(j) - brain.get(j))*newbody.get(j));
                    System.out.printf("%d %.4f %.4f %.4f\n",i,newb1,newb2,MSE(newb1, newb2, newbody, brain, 0, 0));

                    b1 = newb1;
                    b2 = newb2;
                    j = rand.nextInt(62);//next random entry
                }
                break;

            default:
                System.out.println("FLAG not recognized");
                break;
        }


    }
}   