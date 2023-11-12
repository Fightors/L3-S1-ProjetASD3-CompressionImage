import java.io.*;
import java.util.Scanner;

class Quadtree {

    public long value;
    public Quadtree Q1;
    public Quadtree Q2;
    public Quadtree Q3;
    public Quadtree Q4;

    public Quadtree(int v, Quadtree q1, Quadtree q2, Quadtree q3, Quadtree q4) {
        this.value = v;
        this.Q1 = q1;
        this.Q2 = q2;
        this.Q3 = q3;
        this.Q4 = q4;
    }

    public Quadtree() {
        this.value = -1;
        this.Q1 = null;
        this.Q2 = null;
        this.Q3 = null;
        this.Q4 = null;
    }

    public Quadtree(String chemin) {
        try {
            Scanner scanner = new Scanner(new File(chemin));
            scanner.nextLine();
            scanner.nextLine();
            int dim = Integer.parseInt(scanner.next());
            System.out.println(dim);
            scanner.nextLine();
            int lumMax = Integer.parseInt(scanner.next());
            System.out.println(lumMax);
            int[][] tabQ = new int[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    tabQ[i][j] = Integer.parseInt(scanner.next());
                }
            }
            int hmax = (int) (Math.log(dim) / Math.log(2));
            System.out.println(hmax);

            this.value = -1;
            this.Q1 = null;
            this.Q2 = null;
            this.Q3 = null;
            this.Q4 = null;
            creerQuad(this, tabQ);

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void creerQuad(Quadtree Q, int[][] tabVal) {
        if (tabVal.length == 2) {

            Quadtree q1 = new Quadtree(tabVal[0][0], null, null, null, null);
            Quadtree q2 = new Quadtree(tabVal[0][1], null, null, null, null);
            Quadtree q3 = new Quadtree(tabVal[1][1], null, null, null, null);
            Quadtree q4 = new Quadtree(tabVal[1][0], null, null, null, null);
            Q.Q1 = q1;
            Q.Q2 = q2;
            Q.Q3 = q3;
            Q.Q4 = q4;

        } 
        else {
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

            Quadtree NQ1 = new Quadtree();
            Quadtree NQ2 = new Quadtree();
            Quadtree NQ3 = new Quadtree();
            Quadtree NQ4 = new Quadtree();
            creerQuad(NQ1, sousTableau1);
            creerQuad(NQ2, sousTableau2);
            creerQuad(NQ3, sousTableau3);
            creerQuad(NQ4, sousTableau4);
            Q.Q1 = NQ1;
            Q.Q2 = NQ2;
            Q.Q3 = NQ3;
            Q.Q4 = NQ4;
        }
    }

    public Quadtree getQ1() {
        return this.Q1;
    }

    public Quadtree getQ2() {
        return this.Q2;
    }

    public Quadtree getQ3() {
        return this.Q3;
    }

    public Quadtree getQ4() {
        return this.Q4;
    }

    public long getValue() {
        return this.value;
    }

    public long setValue(int v) {
        return this.value = v;
    }

    public void toPGM() {

    }

    public void compressLambda() {
        if(this.getQ1().value != -1
        &&this.getQ2().value != -1
        &&this.getQ3().value != -1
        &&this.getQ4().value != -1){
            this.value = Math.round(Math.exp((1/4)*(Math.log(0.1+this.getQ1().value)) + (Math.log(0.1+this.getQ2().value))
             + (Math.log(0.1+this.getQ3().value)) + (Math.log(0.1+this.getQ4().value))));
            this.Q1=null;
            this.Q2=null;
            this.Q3=null;
            this.Q4=null;
        }
        else{
            if(this.getQ1().value == -1){
                this.Q1.compressLambda();
            }
            if(this.getQ2().value == -1){
                this.Q2.compressLambda();
            }
            if(this.getQ3().value == -1){
                this.Q3.compressLambda();
            }
            if(this.getQ4().value == -1){
                this.Q4.compressLambda();
            }
        }
    }

    public void compressRho(int p) {
        if (p < 0 || p > 100) {
            System.out.println("Erreur ! Valeur > 100 ou < 0");
        } 
        else{

        }
    }

    public void QtoString() {
        if (this != null) {
            if (this.getQ1() != null) {
                System.out.print("(");
                this.getQ1().QtoString();
                System.out.print(" ");
            } 
            if (this.getQ2() != null) {
                this.getQ2().QtoString();
            }
            if (this.getValue() == -1) {
                System.out.print(" ");
            }
            else{
                System.out.print(this.getValue());
            }
            if (this.getQ3() != null) {
                this.getQ3().QtoString();
            }
            if (this.getQ4() != null) {
                System.out.print(" ");
                this.getQ4().QtoString();
                System.out.print(")");
            }   
        }
    }

    public static void main(String[] args) {
        Quadtree Q = new Quadtree("test.pgm");
        /*
         * Quadtree Q1 = new Quadtree(1,null,null,null,null);
         * Quadtree Q2 = new Quadtree(2,null,null,null,null);
         * Quadtree Q3 = new Quadtree(3,null,null,null,null);
         * Quadtree Q4 = new Quadtree(4,null,null,null,null);
         * Quadtree Q0 = new Quadtree(0, Q1, Q2, Q3, Q4);
         * Q0.QtoString();
         */
        Q.QtoString();
        Q.compressLambda();
        System.out.println("");
        System.out.println("Compression Lambda : ");
        Q.QtoString();
    }
}