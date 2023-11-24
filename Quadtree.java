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
            //System.out.println(dim);
            scanner.nextLine();
            int lumMax = Integer.parseInt(scanner.next());
            //System.out.println(lumMax);
            int[][] tabQ = new int[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    tabQ[i][j] = Integer.parseInt(scanner.next());
                }
            }
            int hmax = (int) (Math.log(dim) / Math.log(2));
            //System.out.println(hmax);

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

    public int nbNoeuds(){
        if(this.getQ1().value !=-1
        &&this.getQ2().value !=-1
        &&this.getQ3().value !=-1
        &&this.getQ4().value !=-1){
            return 4;
        }
        else{
            int nbNoeuds1 = 0;
            int nbNoeuds2 = 0;
            int nbNoeuds3 = 0;
            int nbNoeuds4 = 0;
            if(this.getQ1().value==-1){
                nbNoeuds1 = 1+this.getQ1().nbNoeuds();
            }
            else{
                nbNoeuds1 = 1;
            }
            if(this.getQ2().value==-1){
                nbNoeuds2 = 1+this.getQ2().nbNoeuds();
            }
            else{
                nbNoeuds2 = 1;
            }
            if(this.getQ3().value==-1){
                nbNoeuds3 = 1+this.getQ3().nbNoeuds();
            }
            else{
                nbNoeuds3 = 1;
            }
            if(this.getQ4().value==-1){
                nbNoeuds4 = 1+this.getQ4().nbNoeuds();
            }
            else{
                nbNoeuds4 = 1;
            }
            return 1+nbNoeuds1+nbNoeuds2+nbNoeuds3+nbNoeuds4;
        }
    }
    public int[][] treeToTab(int h){
        if(this.getQ1().getValue() != -1
        &&this.getQ2().getValue() != -1
        &&this.getQ3().getValue() != -1
        &&this.getQ4().getValue() != -1){
            int[][] tab = new int[2][2];
            tab[0][0] = this.getQ1().getValue();
            tab[0][1] = this.getQ2().getValue();
            tab[1][1] = this.getQ3().getValue();
            tab[1][0] = this.getQ4().getValue();
            return tab;
        }
        else{
            int[][] tabQ1 = new int[2][2];
            int[][] tabQ2 = new int[2][2];
            int[][] tabQ3 = new int[2][2];
            int[][] tabQ4 = new int[2][2];
            int hQ1 = 1;
            int hQ2 = 1;
            int hQ3 = 1;
            int hQ4 = 1;
            if(this.getQ1().getValue() == -1){
                hQ1 = h+1;
                tabQ1 = this.getQ1().treeToTab(hQ1);
            }
            else{
                tabQ1[0][0] = this.Q1.getValue();
                tabQ1[0][1] = this.Q1.getValue();
                tabQ1[1][1] = this.Q1.getValue();
                tabQ1[1][0] = this.Q1.getValue();
            }
            if(this.getQ2().getValue() == -1){
                hQ2 = h+1;
                tabQ2 = this.getQ2().treeToTab(hQ2);
            }
            else{
                tabQ2[0][0] = this.Q2.getValue();
                tabQ2[0][1] = this.Q2.getValue();
                tabQ2[1][1] = this.Q2.getValue();
                tabQ2[1][0] = this.Q2.getValue();
            }
            if(this.getQ3().getValue() == -1){
                hQ3 = h+1;
                tabQ3 = this.getQ3().treeToTab(hQ3);
            }
            else{
                tabQ3[0][0] = this.Q3.getValue();
                tabQ3[0][1] = this.Q3.getValue();
                tabQ3[1][1] = this.Q3.getValue();
                tabQ3[1][0] = this.Q3.getValue();
            }
            if(this.getQ4().getValue() == -1){
                hQ4 = h+1;
                tabQ4 = this.getQ4().treeToTab(hQ4);
            }
            else{
                tabQ4[0][0] = this.Q4.getValue();
                tabQ4[0][1] = this.Q4.getValue();
                tabQ4[1][1] = this.Q4.getValue();
                tabQ4[1][0] = this.Q4.getValue();
            }
            if(hQ1 == hQ2 && hQ2 == hQ3 && hQ3 == hQ4){
                int taille = tabQ1.length;
                int[][] tabFinal = new int[taille*2][taille*2];
                for(int i = 0; i < taille; i++){
                    for(int j = 0; j < taille; j++){
                        tabFinal[i][j]=tabQ1[i % tabQ1.length][j % tabQ1.length];
                        tabFinal[i][j + taille]=tabQ2[i % tabQ2.length][j % tabQ2.length];
                        tabFinal[i + taille ][j + taille]=tabQ4[i % tabQ4.length][j % tabQ4.length];
                        tabFinal[i + taille][j]=tabQ3[i % tabQ3.length][j % tabQ3.length];
                    }
                }
                return tabFinal;
            }
            else{
                int hmax = Math.max(Math.max(hQ1,hQ2),Math.max(hQ3,hQ4));
                if(hmax != hQ1){
                    int taille = (int)Math.pow(2,hmax);
                    int[][] tabFinal = new int[taille][taille];
                    int petiteT = tabQ1.length;
                    for(int i = 0; i < taille; i++){
                        for(int j = 0; j < taille; j++){
                            tabFinal[i][j] = tabQ1[i % petiteT][j % petiteT];
                        }
                    }
                }
                if(hmax != hQ2){
                    int taille = (int)Math.pow(2,hmax);
                    int[][] tabFinal = new int[taille][taille];
                    int petiteT = tabQ2.length;
                    for(int i = 0; i < taille; i++){
                        for(int j = 0; j < taille; j++){
                            tabFinal[i][j] = tabQ2[i % petiteT][j % petiteT];
                        }
                    }
                }
                if(hmax != hQ3){
                    int taille = (int)Math.pow(2,hmax);
                    int[][] tabFinal = new int[taille][taille];
                    int petiteT = tabQ3.length;
                    for(int i = 0; i < taille; i++){
                        for(int j = 0; j < taille; j++){
                            tabFinal[i][j] = tabQ3[i % petiteT][j % petiteT];
                        }
                    }
                }
                if(hmax != hQ4){
                    int taille = (int)Math.pow(2,hmax);
                    int[][] tabFinal = new int[taille][taille];
                    int petiteT = tabQ4.length;
                    for(int i = 0; i < taille; i++){
                        for(int j = 0; j < taille; j++){
                            tabFinal[i][j] = tabQ4[i % petiteT][j % petiteT];
                        }
                    }
                }
                int taille = tabQ1.length;
                int[][] tabFinal = new int[taille*2][taille*2];
                for(int i = 0; i < taille; i++){
                    for(int j = 0; j < taille; j++){
                        tabFinal[i][j]=tabQ1[i % tabQ1.length][j % tabQ1.length];
                        tabFinal[i][j + taille]=tabQ2[i % tabQ2.length][j % tabQ2.length];
                        tabFinal[i + taille ][j + taille]=tabQ4[i % tabQ4.length][j % tabQ4.length];
                        tabFinal[i + taille][j]=tabQ3[i % tabQ3.length][j % tabQ3.length];
                    }
                }
                return tabFinal;
            }
        }
    }

    public void toPGM(String nomfichier) {
        String fichier = nomfichier+".pgm";
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
            int[][] tabTree= this.treeToTab(0);
            for(int i= 0;i< dim;i++){
                for(int j = 0; j<dim;j++){
                    if((dim*i+j)%17 == 0 && j > 10){
                        writer.write(String.valueOf(tabTree[i][j]) + "\n");
                    }else{
                        writer.write(String.valueOf(tabTree[i][j]) + " ");
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compressLambdaRecu() {
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
                this.Q1.compressLambdaRecu();
            }
            if(this.getQ2().value == -1){
                this.Q2.compressLambdaRecu();
            }
            if(this.getQ3().value == -1){
                this.Q3.compressLambdaRecu();
            }
            if(this.getQ4().value == -1){
                this.Q4.compressLambdaRecu();
            }
        }
    }

    public void compressLambda(){
        this.compressLambdaRecu();
        this.verifEqui();
    }

    
    public void compressRho(int p) {
        int nbN = this.nbNoeuds();
        if (p < 0 || p > 100) {
            System.out.println("Erreur ! Valeur > 100 ou < 0");
        } 
        else{
            int noeudsACons = (int)Math.round((p*nbN)/100);
            if(noeudsACons % 4  !=1 ){
                noeudsACons = noeudsACons - (noeudsACons % 4) + 1;
            }
            this.compressRhoRecu(nbN-noeudsACons);
            this.verifEqui();
        }
    }

    public boolean isDeepest(){
        return this.getQ1().value != -1
        ||this.getQ2().value != -1
        ||this.getQ3().value != -1
        ||this.getQ4().value != -1;
    }

    public boolean isNotDeepest(){
        return this.getQ1().value == -1
        &&this.getQ2().value == -1
        &&this.getQ3().value == -1
        &&this.getQ4().value == -1;
    }

    public double epsilon(){
        double gamma = Math.exp(0.25*(Math.log(0.1+this.getQ1().value) + Math.log(0.1+this.getQ2().value)
        + Math.log(0.1+this.getQ3().value) + Math.log(0.1+this.getQ4().value)));
        double G1 = Math.abs(gamma-this.getQ1().value);
        double G2 = Math.abs(gamma-this.getQ2().value);
        double G3 = Math.abs(gamma-this.getQ3().value);
        double G4 = Math.abs(gamma-this.getQ4().value);
        return Math.max(Math.max(G1,G2),Math.max(G3,G4));
    }

    public void compressRhoRecu(int nbASuppr){
        System.out.println(nbASuppr);
        if(!this.getQ1().isDeepest()
        ||!this.getQ2().isDeepest()
        ||!this.getQ3().isDeepest()
        ||!this.getQ4().isDeepest()){
            if(nbASuppr > 0){
                double epsilon1 = this.getQ1().epsilon();
                double epsilon2 = this.getQ2().epsilon();
                double epsilon3 = this.getQ3().epsilon();
                double epsilon4 = this.getQ4().epsilon();
                double minEps = Math.min(Math.min(epsilon1,epsilon2),Math.min(epsilon3,epsilon4));
                for(int i=0;i<4;i++){
                    if(minEps == epsilon1 && nbASuppr > 0){
                        this.getQ1().value = (int)Math.round(Math.exp(0.25*(Math.log(0.1+this.getQ1().getQ1().value) + Math.log(0.1+this.getQ1().getQ2().value)
                        + Math.log(0.1+this.getQ1().getQ3().value) + Math.log(0.1+this.getQ1().getQ4().value))));
                        this.getQ1().Q1=null;
                        this.getQ1().Q2=null;
                        this.getQ1().Q3=null;
                        this.getQ1().Q4=null;
                        nbASuppr -= 4;
                        epsilon1=99999.99;
                        minEps = Math.min(Math.min(epsilon2,epsilon3),epsilon4);
                    }
                    if(minEps == epsilon2 && nbASuppr > 0){
                        this.getQ2().value = (int)Math.round(Math.exp(0.25*(Math.log(0.1+this.getQ2().getQ1().value) + Math.log(0.1+this.getQ2().getQ2().value)
                        + Math.log(0.1+this.getQ2().getQ3().value) + Math.log(0.1+this.getQ2().getQ4().value))));
                        this.getQ2().Q1=null;
                        this.getQ2().Q2=null;
                        this.getQ2().Q3=null;
                        this.getQ2().Q4=null;
                        nbASuppr -= 4;
                        epsilon2=99999.99;
                        minEps = Math.min(Math.min(epsilon1,epsilon3),epsilon4);
                    }
                    if(minEps == epsilon3 && nbASuppr > 0){
                        this.getQ3().value = (int)Math.round(Math.exp(0.25*(Math.log(0.1+this.getQ3().getQ1().value) + Math.log(0.1+this.getQ3().getQ2().value)
                        + Math.log(0.1+this.getQ3().getQ3().value) + Math.log(0.1+this.getQ3().getQ4().value))));
                        this.getQ3().Q1=null;
                        this.getQ3().Q2=null;
                        this.getQ3().Q3=null;
                        this.getQ3().Q4=null;
                        nbASuppr -= 4;
                        epsilon3=99999.99;
                        minEps = Math.min(Math.min(epsilon1,epsilon2),epsilon4);
                    }
                    if(minEps == epsilon4 && nbASuppr > 0){
                        this.getQ4().value = (int)Math.round(Math.exp(0.25*(Math.log(0.1+this.getQ4().getQ1().value) + Math.log(0.1+this.getQ4().getQ2().value)
                        + Math.log(0.1+this.getQ4().getQ3().value) + Math.log(0.1+this.getQ4().getQ4().value))));
                        this.getQ4().Q1=null;
                        this.getQ4().Q2=null;
                        this.getQ4().Q3=null;
                        this.getQ4().Q4=null;
                        nbASuppr -= 4;
                        epsilon4=99999.99;
                        minEps = Math.min(Math.min(epsilon1,epsilon2),epsilon3);
                    }
                }
            }
        }
        else{
            if(!this.getQ1().isDeepest()){
                this.Q1.compressRhoRecu(nbASuppr);
            }
            if(!this.getQ2().isDeepest()){
                this.Q2.compressRhoRecu(nbASuppr);
            }
            if(!this.getQ3().isDeepest()){
                this.Q3.compressRhoRecu(nbASuppr);
            }
            if(!this.getQ4().isDeepest()){
                this.Q4.compressRhoRecu(nbASuppr);
            }
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
        String chemin;
        int p;
        if(args.length == 2){
            chemin = args[0];
            p = Integer.parseInt(args[1]);
        }
        else{
            chemin = "test.pgm";
            p = 82;
        }
        Quadtree QLambda = new Quadtree(chemin);
        Quadtree QRho = new Quadtree(chemin);
        //QLambda.QtoString();
        System.out.print("\nHauteur de l'arbre : ");
        System.out.println(QLambda.hauteurMax());
        System.out.print("Luminosite maximum d'un pixel de l'arbre : ");
        System.out.println(QLambda.lumMax());
        System.out.print("Nombre de noeuds de l'arbre : ");
        System.out.println(QLambda.nbNoeuds());
        QLambda.compressLambda();
        System.out.println("\nCompression Lambda : ");
        //QLambda.QtoString();
        System.out.print("\nHauteur de l'arbre apres compression Lambda : ");
        System.out.println(QLambda.hauteurMax());
        System.out.print("Luminosite maximum d'un pixel de l'arbre apres compression Lambda : ");
        System.out.println(QLambda.lumMax());
        QRho.compressRho(p);
        System.out.println("\nCompression Rho : ");
        QRho.QtoString();
        System.out.print("\nHauteur de l'arbre apres compression Rho : ");
        System.out.println(QRho.hauteurMax());
        System.out.print("Luminosite maximum d'un pixel de l'arbre apres compression Rho : ");
        System.out.println(QRho.lumMax());
        QLambda.toPGM("compressionLambda");
        QRho.toPGM("compressionRho");
    }
}