import java.io.*;

class Quadtree{

    private int value; 
    private Quadtree Q1;
    private Quadtree Q2;
    private Quadtree Q3;
    private Quadtree Q4;

    public Quadtree(String chemin){
        File file = new file (chemin);
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        StringBuffer = new StringBuffer();
        String line = br.readLine();
        if(line != "P2"){
            throw new IllegalArgumentException("Erreur ! L'image n'est pas au format P2");
        }
        br.readLine();
        br.readLine();
        
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

    public void toString(){
        if (this != null){
            System.out.print("(");
            this.getQ1().toString();
            this.getQ2().toString();
            System.out.print(this.getValue());
            this.getQ3().toString();
            this.getQ4().toString();
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
}