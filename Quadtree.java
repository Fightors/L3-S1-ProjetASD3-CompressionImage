import java.io.*;
import java.util.Scanner;

class Quadtree {

    public int value;
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
            this.creerQuad(tabQ);
            this.verifEqui();

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void creerQuad(int[][] tabVal) {
        if (tabVal.length == 2) {
            Quadtree q1 = new Quadtree(tabVal[0][0], null, null, null, null);
            Quadtree q2 = new Quadtree(tabVal[0][1], null, null, null, null);
            Quadtree q3 = new Quadtree(tabVal[1][1], null, null, null, null);
            Quadtree q4 = new Quadtree(tabVal[1][0], null, null, null, null);
            this.Q1 = q1;
            this.Q2 = q2;
            this.Q3 = q3;
            this.Q4 = q4;
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
            NQ1.creerQuad(sousTableau1);
            NQ2.creerQuad(sousTableau2);
            NQ3.creerQuad(sousTableau3);
            NQ4.creerQuad(sousTableau4);
            this.Q1 = NQ1;
            this.Q2 = NQ2;
            this.Q3 = NQ3;
            this.Q4 = NQ4;
        }
    }

    public void verifEqui(){
        if (this.getQ1().getValue() == this.getQ2().getValue() 
        && this.getQ2().getValue() == this.getQ3().getValue()
        && this.getQ3().getValue() == this.getQ4().getValue()
        && this.getQ1().getValue() != -1){
            this.value = this.getQ1().getValue();
            this.Q1=null;
            this.Q2=null;
            this.Q3=null;
            this.Q4=null;
        }
        else{
            if(this.getQ1().value == -1){
                this.Q1.verifEqui();
            }
            if(this.getQ2().value == -1){
                this.Q2.verifEqui();
            }
            if(this.getQ3().value == -1){
                this.Q3.verifEqui();
            }
            if(this.getQ4().value == -1){
                this.Q4.verifEqui();
            }
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

    public int getValue() {
        return this.value;
    }

    public int setValue(int v) {
        return this.value = v;
    }

    public int hauteurMax(){
        if(this.getQ1() == null
        &&this.getQ2() == null
        &&this.getQ3() == null
        &&this.getQ4() == null){
            return 0;
        }
        else{
            int maxQ1 = 1+this.getQ1().hauteurMax();
            int maxQ2 = 1+this.getQ2().hauteurMax();
            int maxQ3 = 1+this.getQ3().hauteurMax();
            int maxQ4 = 1+this.getQ4().hauteurMax();
            return Math.max(Math.max(maxQ1,maxQ2),Math.max(maxQ3,maxQ4));
        }
    }

    public int lumMax(){
        if(this.getQ1() == null
        &&this.getQ2() == null
        &&this.getQ3() == null
        &&this.getQ4() == null){
            return this.value;
        }
        else{
            int maxQ1 = this.getQ1().lumMax();
            int maxQ2 = this.getQ2().lumMax();
            int maxQ3 = this.getQ3().lumMax();
            int maxQ4 = this.getQ4().lumMax();
            return Math.max(Math.max(maxQ1,maxQ2),Math.max(maxQ3,maxQ4));
        }
    }

    public void toPGM() {
        String fichier = "dessin.pgm";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))){
            writer.write("P2");
            writer.newLine();
            writer.write("# commentaire photo");
            writer.newLine();
            int dim = (int)Math.pow(2,this.hauteurMax());
            writer.write(dim + " " + dim);
            writer.newLine();
            int lum = this.lumMax();
            writer.write(String.valueOf(lum));
            writer.newLine();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressLambda() {
        if(this.getQ1().value != -1
        &&this.getQ2().value != -1
        &&this.getQ3().value != -1
        &&this.getQ4().value != -1){
            this.value = (int)Math.round(Math.exp(0.25*(Math.log(0.1+this.getQ1().value) + Math.log(0.1+this.getQ2().value)
             + Math.log(0.1+this.getQ3().value) + Math.log(0.1+this.getQ4().value))));
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
        Quadtree Q = new Quadtree("flower_small.pgm");
        Q.QtoString();
        System.out.print("\nHauteur de larbre : ");
        System.out.println(Q.hauteurMax());
        System.out.print("Luminosité maximum d'un pixel de larbre : ");
        System.out.println(Q.lumMax());
        //Q.compressLambda();
        //Q.verifEqui();
        //System.out.println("\nCompression Lambda : ");
        Q.QtoString();
        System.out.print("\nHauteur de larbre : ");
        System.out.println(Q.hauteurMax());
        System.out.print("Luminosité maximum d'un pixel de l'arbre : ");
        System.out.println(Q.lumMax());
        Q.toPGM();
    }
}