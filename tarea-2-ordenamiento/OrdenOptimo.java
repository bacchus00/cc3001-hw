
package ordenoptimo;

import java.util.Scanner;

public class OrdenOptimo {
	
    //pide y recupera la informacion de las dimensiones entregada dentro del arreglo dims
    public static void main(String[] args) {

        System.out.println("Dimensiones de las Matrices?");
        Scanner sc = new Scanner(System.in);
        
        //este while permite que el programa pueda recibir multiples consultas
        while (sc.hasNextInt()){
            String si = sc.nextLine();
            String[] sp = si.split(" ");
            int l = sp.length;
            int[] dims = new int[l];
            
            for(int i = 0; i < l; i++) {
            	dims[i] = Integer.parseInt(sp[i]);
            }
            
            int[][] s = matrizmys(dims);   
            parenOpti(1, dims.length - 1, s);  // paren Opti porfavor xdxdxd
            System.out.println(" ");
        }
        sc.close();
    }
    
    //resuelve el problema de orden otimo dando las distintas posiciones k en la matriz s
    public static int[][] matrizmys(int[] dims){
    	
    	//se crean la matriz de costos m y de posiciones para parentizar s
    	int d = dims.length - 1;
        int[][] m = new int[d+1][d+1];
        int[][] s = new int[d+1][d+1];
        
        //cadenas de tamaño 2 hasta d
        for (int c = 2; c <= d; c++){
        	
        	//parte desde la matriz i hasta la matriz j para resolver los sub problemas
            for (int i = 1; i <= d - c + 1; i++){
                int j = i + c - 1;
                m[i][j] = Integer.MAX_VALUE;
                
                //costo de posicionar la parentizacion en k en la sub cadena entre i y j 
                for (int k = i; k < j; k++){
                    int cst = m[i][k] + m[k + 1][j] + dims[i-1]*dims[k]*dims[j];
                    if (cst <= m[i][j]){
                        m[i][j] = cst;
                        s[i][j] = k;
                    }
                    
                }
                
            }
        }
        //retorna matriz de posiciones k de los distintos sub problemas
        return s;
    }
    
	//retorna la parentizacion optima de ((A*B)*C) en el formato ((..).) 
    public static void parenOpti(int i, int j, int[][] s){
    	
        if(i != j){
        	System.out.print("("); parenOpti(i, s[i][j], s); parenOpti(j, s[i][j] + 1, s); System.out.print(")");
        }
        
        else{
        	System.out.print(".");
        }
        
    }
}


