import java.io.*;
import java.util.Scanner;

public class test{
    public static void main(String[] args) {
        try{
            Scanner scanner = new Scanner(new File("flower_small.pgm"));
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

            int tailleSousTableau = tabQ.length / 2;
            int[][] sousTableau1 = new int[tailleSousTableau][tailleSousTableau];
            int[][] sousTableau2 = new int[tailleSousTableau][tailleSousTableau];
            int[][] sousTableau3 = new int[tailleSousTableau][tailleSousTableau];
            int[][] sousTableau4 = new int[tailleSousTableau][tailleSousTableau];

            for (int i = 0; i < tailleSousTableau; i++) {
                for (int j = 0; j < tailleSousTableau; j++) {
                    sousTableau1[i][j] = tabQ[i][j];
                    sousTableau2[i][j] = tabQ[i][j + tailleSousTableau];
                    sousTableau3[i][j] = tabQ[i + tailleSousTableau][j];
                    sousTableau4[i][j] = tabQ[i + tailleSousTableau][j + tailleSousTableau];
                }
            }

            for (int[] ligne : sousTableau4) {
                for (int valeur : ligne) {
                    System.out.print(valeur + " ");
                }
                System.out.println();
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        
    }
}
