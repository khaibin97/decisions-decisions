
import java.util.*;
import java.io.*;

public class Chatbot{
    private static String filename = "./corpus.txt";
    private static ArrayList<Integer> readCorpus(){
        ArrayList<Integer> corpus = new ArrayList<Integer>();
        try{
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while(sc.hasNext()){
                if(sc.hasNextInt()){
                    int i = sc.nextInt();
                    corpus.add(i);
                }
                else{
                    sc.next();
                }
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        }
        return corpus;
    }
    /*
    private static String vocabfilename = "./vocabulary.txt";
	private static ArrayList<String> readVocabulary() {
		ArrayList<String> vocabulary = new ArrayList<String>();
		vocabulary.add("OOV"); // 0 -> OOV

		try (Scanner fin = new Scanner(vocabfilename)) {
			while (fin.hasNextLine()) {
				vocabulary.add(fin.nextLine());
			}
        }
         catch (FileNotFoundException ex) {
			System.out.println("File not found.");
        }
        return vocabulary;
    }
    */
    private static int[] countInCorpus(ArrayList<Integer> corpus){
        int[] count =new int [4700];
        for (Integer index: corpus){
            count[index] = count[index] + 1;
        }
        return count;
    }
    private static double[] laplace(int size, int count[]){
        double[] laplace = new double [4700];
        // double sum = 0; //test laplace 
        for(int i=0; i<4700; i++){
            laplace[i] = ( (count[i]+1.0) / (size+4700.0) );
            // sum += laplace[i]; //test laplace
        }
        // System.out.println(sum); //test laplace
        return laplace;
    }

    private static int[] trigramcount(ArrayList<Integer> corpus, int h1, int h2){
        int[] count = new int[4700];
        try{
            for(int i = 0; i<corpus.size(); i++){
                if(corpus.get(i) == h1){
                    if(corpus.get(i+1) == h2){
                        count[corpus.get(i+2)]++;
                    }
                }
            }
        }catch (IndexOutOfBoundsException e){

        }
        return count;
    }
    
    private static int generatewBigram(ArrayList<Integer> corpus, int h, double r){
        int [] countbigram = new int[4700];
        int sum = 0;
        for(int i=0; i<corpus.size(); i++){
            if(corpus.get(i) == h){
                try{
                    countbigram[corpus.get(i+1)]++;
                    sum++;
                } catch (ArrayIndexOutOfBoundsException e){}
            }
        }
            
        ArrayList<Double> laplacebigram = new ArrayList<Double>();
        int index = 0;
        try{
            //search and calculate
            laplacebigram.add(0.0);
            boolean loop = true;
            while (loop){
                laplacebigram.add(laplacebigram.get(index)+((countbigram[index]+1)/(sum+4700.0)));
                if(laplacebigram.get(index) > r ){
                    index--;
                    loop = false;
                    break;
                }
                index++;
            }
            
        }catch (IndexOutOfBoundsException e){}
        return index;

    }

    private static int generatewTrigram(ArrayList<Integer> corpus, int h1, int h2, double r){
        int [] count = trigramcount(corpus, h1, h2);
        int sum = 0;
        for(int i : count){
            sum+=i;
        }

        ArrayList<Double> laplacetrigram = new ArrayList<Double>();
        int index = 0;
        try{
            //search and calculate
            laplacetrigram.add(0.0);
            boolean loop = true;
            while (loop){
                laplacetrigram.add(laplacetrigram.get(index)+((count[index]+1)/(sum+4700.0)));
                if(laplacetrigram.get(index) > r ){
                    index--;
                    loop = false;
                    break;
                }
                index++;
            }

        }catch (IndexOutOfBoundsException e){}

        return index;
    }

    static public void main(String[] args){
        ArrayList<Integer> corpus = readCorpus();
        //ArrayList<String> vocab = readVocabulary();
        int flag = Integer.valueOf(args[0]);

        if(flag == 100){
			int w = Integer.valueOf(args[1]);
            //count all
            int[] countCorp = countInCorpus(corpus);
            //laplace all
            double[] laplace = laplace(corpus.size(), countCorp);

            System.out.println(countCorp[w]);
            System.out.printf("%.7f\n",laplace[w]);
            
            
        }
        else if(flag == 200){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            //TODO generate
            //count all
            int[] countCorp = countInCorpus(corpus);
            //laplace all
            double[] laplace = laplace(corpus.size(), countCorp);
            ArrayList<Double> randomSample = new ArrayList<Double>();
            randomSample.add(0.0);//left index
            //segmenting
            for(int i = 0; i<4700; i++){
                randomSample.add(randomSample.get(i)+laplace[i]);
            }
            double r = (double) n1/n2;
            int index = 0;
            boolean loop = true;
            try{
                //search
                while (loop){
                        if(randomSample.get(index) > r ){
                            index--;
                            loop = false;
                            break;
                        }
                        index++;
                }

                System.out.printf("%d\n%.07f\n%.07f\n",index,randomSample.get(index), randomSample.get(index+1));
            }catch (IndexOutOfBoundsException e){

            }
        }
        else if(flag == 300){
            int h = Integer.valueOf(args[1]);
            int w = Integer.valueOf(args[2]);
            int count = 0;
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();
            ArrayList<Integer> countbigram = new ArrayList<Integer>();

            for(int i=0; i<corpus.size(); i++){
                if(corpus.get(i) == h){
                    try{
                        if(!words_after_h.contains(corpus.get(i+1))){
                            words_after_h.add(corpus.get(i+1));
                            countbigram.add(1);
                        }else{
                            int z = words_after_h.indexOf(corpus.get(i+1));
                            int add = countbigram.get(z) + 1;
                            countbigram.set(z,add);
                        }
                    } catch (ArrayIndexOutOfBoundsException e){}
                }
            }
            int index;
            try{
                index = countbigram.get(words_after_h.indexOf(w));
            } catch (IndexOutOfBoundsException e){
                index = 0;
            }
            int sum = 0;
            for (int i : countbigram)
                sum += i;

            double laplace = (index+1) /(sum+4700.0);
            System.out.printf("%d\n%d\n%.7f\n",index,sum,laplace);

        }
        else if(flag == 400){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h = Integer.valueOf(args[3]);
            //TODO
            
            ArrayList<Integer> words_after_h = new ArrayList<Integer>();
            int [] countbigram = new int[4700];
            int sum = 0;
            for(int i=0; i<corpus.size(); i++){
                if(corpus.get(i) == h){
                    try{
                        countbigram[corpus.get(i+1)]++;
                        sum++;
                    } catch (ArrayIndexOutOfBoundsException e){}
                }
            }
            
            ArrayList<Double> laplacebigram = new ArrayList<Double>();
            try{
                //search and calculate
                int index=0;
                laplacebigram.add(0.0);
                double r = (double) n1/n2;
                boolean loop = true;
                while (loop){
                    laplacebigram.add(laplacebigram.get(index)+((countbigram[index]+1)/(sum+4700.0)));
                    if(laplacebigram.get(index) > r ){
                        index--;
                        loop = false;
                        break;
                    }
                    index++;
                }

                System.out.printf("%d\n%.07f\n%.07f\n",index,laplacebigram.get(index), laplacebigram.get(index+1));
            }catch (IndexOutOfBoundsException e){}

            
        }
        else if(flag == 500){
            int h1 = Integer.valueOf(args[1]);
            int h2 = Integer.valueOf(args[2]);
            int w = Integer.valueOf(args[3]);
            int count[] = trigramcount(corpus, h1, h2);
            ArrayList<Integer> words_after_h1h2 = new ArrayList<Integer>();
            //TODO
            int sum = 0;
            for(int i : count){
                sum+=i;
            }
            double laplace = (double)(count[w]+1)/(sum+4700);
            System.out.printf("%d\n%d\n%.7f\n",count[w],sum,laplace);
			
        }
        else if(flag == 600){
            int n1 = Integer.valueOf(args[1]);
            int n2 = Integer.valueOf(args[2]);
            int h1 = Integer.valueOf(args[3]);
            int h2 = Integer.valueOf(args[4]);
            //TODO
            int [] count = trigramcount(corpus, h1, h2);
            int sum = 0;
            for(int i : count){
                sum+=i;
            }

            ArrayList<Double> laplacetrigram = new ArrayList<Double>();
            try{
                //search and calculate
                int index=0;
                laplacetrigram.add(0.0);
                double r = (double) n1/n2;
                boolean loop = true;
                while (loop){
                    laplacetrigram.add(laplacetrigram.get(index)+((count[index]+1)/(sum+4700.0)));
                    if(laplacetrigram.get(index) > r ){
                        index--;
                        loop = false;
                        break;
                    }
                    index++;
                }

                System.out.printf("%d\n%.07f\n%.07f\n",index,laplacetrigram.get(index), laplacetrigram.get(index+1));
            }catch (IndexOutOfBoundsException e){}

        }
        else if(flag == 700){
            int seed = Integer.valueOf(args[1]);
            int t = Integer.valueOf(args[2]);
            int h1=0,h2=0;

            Random rng = new Random();
            if (seed != -1) rng.setSeed(seed);

            if(t == 0){
                // TODO Generate first word using r
                int[] countCorp = countInCorpus(corpus);
                //laplace all
                double[] laplace = laplace(corpus.size(), countCorp);
                ArrayList<Double> randomSample = new ArrayList<Double>();
                randomSample.add(0.0);//left index
                //segmenting
                for(int i = 0; i<4700; i++){
                    randomSample.add(randomSample.get(i)+laplace[i]);
                }
                double r = rng.nextDouble();
                int index = 0;
                boolean loop = true;
                try{
                    //search
                    while (loop){
                        if(randomSample.get(index) > r ){
                            index--;
                            loop = false;
                            break;
                        }
                        index++;
                    }
                }catch (IndexOutOfBoundsException e){}
                h1 = index;
                System.out.println(h1);
                if(h1 == 9 || h1 == 10 || h1 == 12){
                    return;
                }

                // TODO Generate second word using r
                r = rng.nextDouble();
                h2 = generatewBigram(corpus, h1, r);
                System.out.println(h2);
            }
            else if(t == 1){
                h1 = Integer.valueOf(args[3]);
                // TODO Generate second word using r
                double r = rng.nextDouble();
                h2 = generatewBigram(corpus, h1, r);
                System.out.println(h2);
            }
            else if(t == 2){
                h1 = Integer.valueOf(args[3]);
                h2 = Integer.valueOf(args[4]);
            }

            while(h2 != 9 && h2 != 10 && h2 != 12){
                double r = rng.nextDouble();
                int w  = generatewTrigram(corpus, h1, h2, r);
                System.out.println(w);
                h1 = h2;
                h2 = w;
            }
        }

        return;
    }
}
