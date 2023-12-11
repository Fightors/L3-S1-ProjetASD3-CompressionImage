/**
* Universite de Nantes
* 2023 / 2024
* Projet d'ASD3 - X31I020
* GODEFROY Theotime
* BOTANS Enzo
* 584J
*/

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/**
* Classe mere de tous les Quadtrees
* Implemente les methodes de compression Lambda et Rho
*/
public class Quadtree {

    /**
    * La valeur du noeud (value = -1 -> branche | value != -1 -> feuille)
    */
    public int value;
    
    /**
    * Premier noeud du Quadtree courant
    */
    public Quadtree Q1;

    /**
    * Deuxième noeud du Quadtree courant
    */
    public Quadtree Q2;

    /**
    * Troisième noeud du Quadtree courant
    */
    public Quadtree Q3;

    /**
    * Quatrième noeud du Quadtree courant
    */
    public Quadtree Q4;

    /**
    * Variable statique
    * ArrayList des noeuds compressibles lors de la compression Rho
    */
    public static ArrayList<Quadtree> noeudComp;

    /**
    * Variable statique
    * ArrayList des Epsilons correspondant aux noeuds compressibles lors de la compression Rho
    */
    public static ArrayList<Double> epsilonComp;

    public static int hmax;

    /**
    * Valeur statique 
    * Utilisee pour la suppression de noeuds lors de la compression Rho
    */
    public static int nbASuppr;

    /**
    * Constructeur d'une instance de Quadtree
     * @param v La valeur du noeud courant
     * @param q1 Le premier noeud descendant
     * @param q2 Le deuxieme noeud descendant
     * @param q3 Le troisieme noeud descendant
     * @param q4 Le quatrieme noeud descendant
     */
    public Quadtree(int v, Quadtree q1, Quadtree q2, Quadtree q3, Quadtree q4) {
        this.value = v;
        this.Q1 = q1;
        this.Q2 = q2;
        this.Q3 = q3;
        this.Q4 = q4;
    }

    /**
    * Constructeur d'une instance de Quadtree avec 4 noeuds inexistants
    * Constructeur d'une feuille de Quadtree
    */
    public Quadtree() {
        this.value = -1;
        this.Q1 = null;
        this.Q2 = null;
        this.Q3 = null;
        this.Q4 = null;
    }

    /**
    * Constructeur d'une instance de Quadtree
    * Recolte les inforamtions dans le fichier PGM et le transforme en tableau 2D
    * puis en Quadtree grace a la fonction creerQuad
    * @param chemin Le chemin et nom du fichier PGM a compresser
    */
    public Quadtree(String chemin) {
        try {
            Scanner scanner = new Scanner(new File(chemin));
            scanner.nextLine();
            scanner.nextLine();
            int dim = Integer.parseInt(scanner.next());
            scanner.nextLine();
            int lumMax = Integer.parseInt(scanner.next());
            int[][] tabQ = new int[dim][dim];
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    tabQ[i][j] = Integer.parseInt(scanner.next());
                }
            }
            hmax = (int) (Math.log(dim) / Math.log(2));
            this.value = -1;
            this.Q1 = null;
            this.Q2 = null;
            this.Q3 = null;
            this.Q4 = null;
            this.creerQuad(tabQ);
            this.verifEqui();
            noeudComp = new ArrayList<Quadtree>();
            epsilonComp = new ArrayList<Double>();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
    * Creation du Quadtree a partir d'un tableau 2D
    * @param tabVal Tableau 2D contenant les informations des pixels du fichier PGM
    */
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

    /**
    * Equilibrage du Quadtree 
    * Si quatre feuille d'un noeud courant ont la même valeur, alors ils sont supprimes et la valeur remonte au noeud courant
    */
    public void verifEqui(){
        if(this.areNotNull()){
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
    }

    /**
    * Getter d'un attribut de Quadtree
    * Premier noeud descendant
    * @return Q1
    */
    public Quadtree getQ1() {
        return this.Q1;
    }

    /**
    * Getter d'un attribut de Quadtree
    * Deuxieme noeud descendant
    * @return Q2
    */
    public Quadtree getQ2() {
        return this.Q2;
    }

    /**
    * Getter d'un attribut de Quadtree
    * Troisieme noeud descendant
    * @return Q3
    */
    public Quadtree getQ3() {
        return this.Q3;
    }

    /**
    * Getter d'un attribut de Quadtree
    * Quatrieme noeud descendant
    * @return Q4
    */
    public Quadtree getQ4() {
        return this.Q4;
    }

    /**
    * Getter d'un attribut de Quadtree
    * Valeur du noeud courant
    * @return value
    */
    public int getValue() {
        return this.value;
    }

    /**
    * Verifie si le noeud courant n'est relie qu'a des feuilles
    * @return boolean
    */
    public boolean isDeepest(){
        return this.getQ1().value != -1
        && this.getQ2().value != -1
        && this.getQ3().value != -1
        && this.getQ4().value != -1;
    }

    /**
    * Verifie si le noeud courant n'est relie qu'a des branches
    * @return boolean
    */
    public boolean areDeeper(){
        return this.getQ1().value == -1
        && this.getQ2().value == -1
        && this.getQ3().value == -1
        && this.getQ4().value == -1;
    }

    /**
    * Verifie si le noeud courant est relie a une branche au moins
    * @return boolean
    */
    public boolean deeper(){
        return this.getQ1().value == -1
        || this.getQ2().value == -1
        || this.getQ3().value == -1
        || this.getQ4().value == -1;
    }

    /**
    * Verifie si le noeud courant n'est pas une feuille
    * @return boolean
    */
    public boolean areNotNull(){
        return this.getQ1() != null
        && this.getQ2() != null
        && this.getQ3() != null
        && this.getQ4() != null;
    }

    /**
    * Verifie si le noeud courant est une feuille
    * @return boolean
    */
    public boolean areNull(){
        return this.getQ1() == null
        &&this.getQ2() == null
        &&this.getQ3() == null
        &&this.getQ4() == null;
    }

    /**
    * Calcule la hauteur maximum du Quadtree
    * @return int
    */
    public int hauteurMax(){
        if(this.areNull()){
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

    /**
    * Calcule la luminosite maximum du Quadtree
    * @return int
    */
    public int lumMax(){
        if(this.areNull()){
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

    /**
    * Calcule le nombre de noeuds du Quadtree
    * @return int
    */
    public int nbNoeuds(){
        if(this.areNull()){
            return 1;
        }
        else if(this.isDeepest()){
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

    /**
    * Transforme un Quadtree en tableau 2D
    * @param h La hauteur actuelle dans le Quadtree
    * @return tabFinal, le tableau représentant le Q
    */
    public int[][] treeToTab(int h){
        if(this.getValue() != -1 && (hmax >h)){ //On est dans une feuille mais pas a la hauteur max 
            int[][] tab = new int[(int)Math.pow(2,hmax-h)][(int)Math.pow(2,hmax-h)];
            for(int i = 0; i<tab.length;i++){
                for(int j = 0; j<tab.length;j++){
                    tab[i][j]=this.getValue();
                }
            }
            return tab;
        }
        else if (this.getValue() != -1 && (hmax == h)){
            int[][] tab = new int[1][1];
            tab[0][0]=this.getValue();
            return tab;
        }
        else if(this.isDeepest() && h+1 == hmax){ //On est dans un noeud et à la hauteur max 
            int[][] tabFinal = new int[2][2];
            tabFinal[0][0] = this.getQ1().getValue();
            tabFinal[0][1] = this.getQ2().getValue();
            tabFinal[1][1] = this.getQ3().getValue();
            tabFinal[1][0] = this.getQ4().getValue();
            return tabFinal;
        }
        else{
            int[][] tabFinal = new int[(int)Math.pow(2,hmax-h)][(int)Math.pow(2,hmax-h)];
            int[][] tabQ1 = this.getQ1().treeToTab(h+1);
            int[][] tabQ2 = this.getQ2().treeToTab(h+1);
            int[][] tabQ3 = this.getQ3().treeToTab(h+1);
            int[][] tabQ4 = this.getQ4().treeToTab(h+1);
            // Fusion des tableaux dans tabFinal
            int halfSize = tabFinal.length / 2;
            for (int i = 0; i < halfSize; i++) {
                for (int j = 0; j < halfSize; j++) {
                    // tabQ1 en haut à gauche
                    tabFinal[i][j] = tabQ1[i][j];
                    // tabQ2 en haut à droite
                    tabFinal[i][j + halfSize] = tabQ2[i][j];
                    // tabQ3 en bas à droite
                    tabFinal[i + halfSize][j + halfSize] = tabQ4[i][j];
                    // tabQ4 en bas à gauche
                    tabFinal[i + halfSize][j] = tabQ3[i][j];
                }
            }
            return tabFinal;
        }
    }

    /**
    * Creation du fichier PGM a partir d'un Quadtree
    * On appelera treeToTab pour passer de Quadtree a tableau 2D
    * On transformera ensuite ce tableau en fichier PGM
    * @param nomFichier Nom du fichier PGM qui sera cree
    */
    public void toPGM(String nomFichier){
        String fichier = nomFichier+".pgm";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))){
            writer.write("P2");
            writer.newLine();
            writer.write("# Photo genere apres compression");
            writer.newLine();
            int dim = (int)Math.pow(2,hmax);
            writer.write(dim + " " + dim);
            writer.newLine();
            int lum = this.lumMax();
            writer.write(String.valueOf(lum));
            writer.newLine();
            int[][] tabTree;
            if(this.areNotNull()){
                tabTree= this.treeToTab(0);
            }
            else{
                tabTree = new int[1][1];
                tabTree[0][0] = this.getValue();
            }
            for(int i= 0;i<tabTree.length;i++){
                for(int j = 0;j<tabTree.length;j++){
                    if((tabTree.length*i+j)%17 == 0 && j > 10){
                        writer.write(String.valueOf(tabTree[i][j]) + "\n");
                    }
                    else{
                        writer.write(String.valueOf(tabTree[i][j]) + " ");
                    }
                }
            }
            tabTree=null;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Creation du fichier texte a partir d'un Quadtree
    * On appelera stringToTxtRecu pour ecrire chaque valeur des feuilles du Quadtree
    * @param nomFichier Nom du fichier texte qui sera cree
    */
    public void stringToTxt(String nomFichier){
        String fichier = nomFichier + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))) {
            this.stringToTxtRecu(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Ecriture des feuilles d'un Quadtree ou appel recursif en cas de branche dans un fichier texte
    * @param bw Buffer d'edcriture utilise pour ecrire dans le fichier texte
    */
    public void stringToTxtRecu(BufferedWriter bw){
        try{
            if (this != null) {
                if (this.getQ1() != null) {
                    bw.write("(");
                    this.getQ1().stringToTxtRecu(bw);
                    bw.write(" ");
                } 
                if (this.getQ2() != null) {
                    this.getQ2().stringToTxtRecu(bw);
                }
                if (this.getValue() == -1) {
                    bw.write(" ");
                }
                else{
                    bw.write(String.valueOf(this.getValue()));
                }
                if (this.getQ3() != null) {
                    this.getQ3().stringToTxtRecu(bw);
                }
                if (this.getQ4() != null) {
                    bw.write(" ");
                    this.getQ4().stringToTxtRecu(bw);
                    bw.write(")");
                }   
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Compression Lambda d'un Quadtree
    * On appelera compressLambdaRecu pour la compression de manière recursive
    * On appelera verifEqui pour re-equilibrer l'arbre apres la compression 
    */
    public void compressLambda(){
        this.compressLambdaRecu();
        this.verifEqui();
    }

    /**
    * Compression d'un Quadtree avec la methode Lambda
    */
    public void compressLambdaRecu() {
        if(this.areNotNull()){
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
    }

    /**
    * Insertion de manière dichotomique de l'epsilon dans Epsiloncomp
    * @return L'indice où l'Epsilon a été inséré
    */
    public int insertDicho(ArrayList<Double> liste, double element) {
        int debut = 0;
        int fin = liste.size() - 1;
        
        while (debut <= fin) {
            int milieu = (debut + fin) / 2;
            if (liste.get(milieu) == element) {
                // Si l'élément est déjà présent, on l'insère à la droite de celui-ci
                liste.add(milieu + 1, element);
                return milieu +1;
            } 
            else if (liste.get(milieu) < element) {
                debut = milieu + 1;
            } 
            else {
                fin = milieu - 1;
            }
        }
        
        // Si l'élément n'existe pas dans la liste, on l'insère à la bonne position
        liste.add(debut, element);
        return debut;
    }

    /**
     * Stocke les noeuds compressibles dans le tableau noeudComp
     * Stocke les epsilons des noeuds compressibles dans epsilonComp
     * Les deux sont stockes aux mêmes indices
     */
    public void stockNoeudComp(){
        if(this.areNotNull()){
            if(this.isDeepest()){
                int indEps = this.insertDicho(epsilonComp, this.epsilon());
                noeudComp.add(indEps,this);
            }
            else{
                this.getQ1().stockNoeudComp();
                this.getQ2().stockNoeudComp();
                this.getQ3().stockNoeudComp();
                this.getQ4().stockNoeudComp();
            }
        }
    }

    public Quadtree getParent(Quadtree Q){
        if(this.areNotNull()){
            if(this.getQ1() == Q){
                return this;
            }
            else{
                return this.getQ1().getParent(Q);
            }
            if(this.getQ2() == Q){
                return this;
            }
            else{
                return this.getQ2().getParent(Q);
            }
            if(this.getQ3() == Q){
                return this;
            }
            else{
                return this.getQ3().getParent(Q);
            }
            if(this.getQ4() == Q){
                return this;
            }
            else{
                return this.getQ4().getParent(Q);
            }
        }
        else{
            return this;
        }
    }

    /**
    * Compression Rho d'un Quadtree
    * On appelera nbNoeuds pour trouver le nombre de noeuds a supprimer
    * On appelera compressRhoRecu pour la compression de manière recursive
    * On appelera verifEqui pour re-equilibrer l'arbre apres la compression 
    * @param p Pourcentage maximum de l'image qui doit subsister apres la compression
    */
    public void compressRho(int p) {
        if (p < 0 || p > 100) {
            System.out.println("Erreur ! Valeur > 100 ou < 0");
        } 
        else{
            int nbN = this.nbNoeuds();
            int noeudsACons = (int)Math.round((p*nbN)/100);
            if(noeudsACons % 4  !=1 ){
                noeudsACons = noeudsACons - (noeudsACons % 4) + 1;
            }
            nbASuppr = nbN - noeudsACons;
            this.stockNoeudComp();
            while(nbASuppr>0){
                /*if(epsilonComp.size()==1){
                    this.stockNoeudComp();
                }*/
                System.out.println(nbASuppr);
                this.compressRhoIte();
            }
            if(this.areNotNull()){
                this.verifEqui();
            }
        }
    }

    /**
    * Calcule de l'Espilon maximum d'un Quadtree
    * @return Epsilon maximum parmis les quatres calcules
    */
    public double epsilon(){
        double gamma = Math.exp(0.25*(Math.log(0.1+this.getQ1().value) + Math.log(0.1+this.getQ2().value)
        + Math.log(0.1+this.getQ3().value) + Math.log(0.1+this.getQ4().value)));
        double G1 = Math.abs(gamma-this.getQ1().value);
        double G2 = Math.abs(gamma-this.getQ2().value);
        double G3 = Math.abs(gamma-this.getQ3().value);
        double G4 = Math.abs(gamma-this.getQ4().value);
        return Math.max(Math.max(G1,G2),Math.max(G3,G4));
    }

    /**
    * Compression d'un Quadtree avec la methode Rho
    */
    public void compressRhoIte(){
        noeudComp.get(0).compressLambda();
        nbASuppr-=4;
        Quadtree Q = this.getParent(noeudComp.get(0));
        noeudComp.remove(0);
        epsilonComp.remove(0);
    }

    /**
    * Affichage du Quadtree avec chaque quatuor de noeuds entre parentheses
    */
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

    /**
    * Programme principal
    * @param args Liste des arguments entres au demarrage de l'execution du programme
    */
    public static void main(String[] args) {
        String chemin;
        int p;
        int nbNoeudInitial;
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
        nbNoeudInitial = QLambda.nbNoeuds();
        System.out.println("\n=====Arbre initial======");
        //QLambda.QtoString();
        System.out.print("\nHauteur : ");
        System.out.println(QLambda.hauteurMax());
        System.out.print("Luminosite Max : ");
        System.out.println(QLambda.lumMax());
        System.out.print("Nombre de Noeuds : ");
        System.out.println(nbNoeudInitial);
        System.out.println("========================");

        QLambda.compressLambda();
        System.out.println("\n===Compression Lambda===");
        //QLambda.QtoString();
        System.out.print("\nHauteur : ");
        System.out.println(QLambda.hauteurMax());
        System.out.print("Luminosite Max : ");
        System.out.println(QLambda.lumMax());
        System.out.print("Nombre de Noeuds : ");
        System.out.println(QLambda.nbNoeuds());
        System.out.print("Taux de compression : ");
        System.out.print((int)(QLambda.nbNoeuds()*100/nbNoeudInitial));
        System.out.println("%");
        QLambda.stringToTxt("treeCompressionLambda");
        QLambda.toPGM("compressionLambda");
        System.out.println("========================");

        QRho.compressRho(p);
        System.out.println("\n====Compression Rho=====");
        //QRho.QtoString();
        System.out.print("\nHauteur : ");
        System.out.println(QRho.hauteurMax());
        System.out.print("Luminosite Max: ");
        System.out.println(QRho.lumMax());
        System.out.print("Nombre de Noeuds : ");
        System.out.println(QRho.nbNoeuds());
        System.out.print("Taux de compression : ");
        System.out.print(p);
        System.out.println("%");
        QRho.stringToTxt("treeCompressionRho");
        QRho.toPGM("compressionRho");
        System.out.println("========================\n");
    }
}