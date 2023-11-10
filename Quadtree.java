import java.io.*;
import java.util.Scanner;

class Quadtree{

    private int value; 
    private Quadtree Q1;
    private Quadtree Q2;
    private Quadtree Q3;
    private Quadtree Q4;

    public Quadtree(String chemin){
        try{
            Scanner scanner = new Scanner(new File(chemin));
            scanner.nextLine();
            scanner.nextLine();
            int dim = Integer.parseInt(scanner.next());
            System.out.println(dim);
            scanner.nextLine();
            int lumMax= Integer.parseInt(scanner.next());
            System.out.println(lumMax);
            int[][] tabQ = new int[dim][dim];
            for(int i = 0; i < dim; i++){
                for(int j = 0; j < dim; j++){
                    tabQ[i][j] = Integer.parseInt(scanner.next());
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        
/*      File file = new file (chemin);
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        StringBuffer = new StringBuffer();
        String line = br.readLine();
        if(line != "P2"){
            throw new IllegalArgumentException("Erreur ! L'image n'est pas au format P2");
        }
        br.readLine();
        br.readLine();
*/   
    }

    public Quadtree getQ1(){
        return this.Q1;
    }

    public Quadtree getQ2(){
        return this.Q2;
    }

    public Quadtree getQ3(){
        return this.Q3;
    }

    public Quadtree getQ4(){
        return this.Q4;
    }

    public int getValue(){
        return this.value;
    }

    public int setValue(int v){
        return this.value = v;
    }

    public void QtoString(){
        if (this != null){
            System.out.print("(");
            this.getQ1().QtoString();
            this.getQ2().QtoString();
            System.out.print(this.getValue());
            this.getQ3().QtoString();
            this.getQ4().QtoString();
            System.out.print(")");
        }
    }

    public void toPGM(){

    }

    public void compressLambda(){

    }
    public boolean isSameColor(Quadtree Q){
        return (Q.getQ1().getValue() == Q.getQ2().getValue() && 
        Q.getQ2().getValue() == Q.getQ3().getValue() && 
        Q.getQ3().getValue() == Q.getQ4().getValue());
    }

    public void compressRho(int p){
        if(p<0 || p>100){
            System.out.println("Erreur ! Valeur > 100 ou < 0");
        }
        else{

        }
        
    }
    public static void main(String[] args){

    }
}