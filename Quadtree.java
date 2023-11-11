import java.io.*;
import java.util.Scanner;

class Quadtree{

    public int value; 
    public Quadtree Q1;
    public Quadtree Q2;
    public Quadtree Q3;
    public Quadtree Q4;

    public Quadtree( int v,Quadtree q1, Quadtree q2,Quadtree q3, Quadtree q4){
        this.value = v;
        this.Q1 =q1;
        this.Q2 =q2;
        this.Q3 =q3;
        this.Q4 =q4;
    }

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
            int hmax = (int)(Math.log(dim) / Math.log(2));
            System.out.println(hmax);

            Quadtree Q = new Quadtree(-1, null, null, null, null);
            this.creerQuad(Q, tabQ);

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    Quadtree creerQuad(Quadtree Q, int[][] tabVal){
        if(tabVal.length == 2){
            System.out.print("oui");
            Quadtree q1 = new Quadtree(tabVal[0][0],null, null, null, null);
            Quadtree q2 = new Quadtree(tabVal[0][1],null, null, null, null);
            Quadtree q3 = new Quadtree(tabVal[1][1],null, null, null, null);
            Quadtree q4 = new Quadtree(tabVal[1][0],null, null, null, null);
            Quadtree q = new Quadtree(-1,q1,q2, q3, q4);

            return q;
        }else{
            int tailleSousTableau = tabVal.length / 2;
            int[][] sousTableau1 = new int[tailleSousTableau][tailleSousTableau];
            int[][] sousTableau2 = new int[tailleSousTableau][tailleSousTableau];
            int[][] sousTableau3 = new int[tailleSousTableau][tailleSousTableau];
            int[][] sousTableau4 = new int[tailleSousTableau][tailleSousTableau];

            for (int i = 0; i < tailleSousTableau; i++) {
                for (int j = 0; j < tailleSousTableau; j++) {
                    sousTableau1[i][j] = tabVal[i][j];
                    sousTableau2[i][j] = tabVal[i][j + tailleSousTableau];
                    sousTableau3[i][j] = tabVal[i + tailleSousTableau][j];
                    sousTableau4[i][j] = tabVal[i + tailleSousTableau][j + tailleSousTableau];
                }
            }
            Quadtree Q0 = new Quadtree(-1,creerQuad(Q1, sousTableau1),creerQuad(Q1, sousTableau2), creerQuad(Q3, sousTableau3), creerQuad(Q4, sousTableau4));
            return Q0;
        }
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
            if(this.getQ1() != null){
                this.getQ1().QtoString();
            }
            if(this.getQ2() != null){
                this.getQ2().QtoString();
            }
            System.out.print(this.getValue());
            if(this.getQ3() != null){
                this.getQ3().QtoString();
            }
            if(this.getQ4() != null){
                this.getQ4().QtoString();
            }
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
        Quadtree Q = new Quadtree("test.pgm");
        Q.QtoString();
    }
}