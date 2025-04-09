package P1;
import java.util.Scanner;
public class PilaArena {

    public static void main(String[] args) {
        System.out.println("Numero de granos de arena?");
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int size = (int) Math.sqrt(N);
        
        Ventana vnt = new Ventana(600,"ventanita");
        
        if (N%2 == 0){
            size += 2;
        }
        
        int[][] tablero = new int[size][size];
        tablero[size/2][size/2] = N;
        int veces = 0;
        boolean t = true;
        
        
        while (t == true ){
            t = false;
            for (int i = 0; i < size; i++){
                for(int j = 0; j < size; j++){
                    if (tablero[i][j] >= 4){
                        t = true;
                        veces += 1;
                        tablero[i][j] -= 4;
                        tablero[i+1][j] += 1;
                        tablero[i-1][j] += 1;
                        tablero[i][j+1] += 1;
                        tablero[i][j-1] += 1;
                    }
                }
            }
        }
        System.out.print("Los granos se derrumbaron " + veces + " veces.");
        vnt.mostrarMatriz(tablero);
        
    }
    
}
