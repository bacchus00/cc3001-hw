package tarea4;
import java.io.*;
import java.util.Scanner;
import java.lang.Math.*;

//Parte 1
//se define el arbol con una llave y una prioridad por cada nodo
class Arbol{
	int lla;
        double prio;
	Arbol izq;
	Arbol der;

	public Arbol(int lla){
		this.lla = lla;
		izq = null;
		der = null;
	}
        
        public Arbol(double prio){
		this.prio = prio;
		izq = null;
		der = null;
	}
        

	public Arbol(int lla, double prio, Arbol izq, Arbol der){
		this.lla = lla;
                this.prio = prio;
		this.izq = izq;
		this.der = der;
	}
    }

//utilizado para leer los archivos de texto que contienen los valores de x e y
class archivos{
     public String leerTxt(String d){
       String texto = "";
        try{
            BufferedReader bf = new BufferedReader(new FileReader(d));
            String t = "";
            String bfr;
            while((bfr = bf.readLine()) != null){
                t = t + "/" + bfr;
            }
            texto = t;
        }
        catch(Exception e){
            System.err.println("No se encontro el archivo");
        }
        return texto;
    }
}

public class Tarea4 {
    //Metodo para la insercion de los nodos
    public static Arbol insertar(Arbol AB, int x, double y){
        //caso en el que se puede insertar simplemente el nuevo nodo (como en un  abb comun)
        if (AB == null){
            return AB = new Arbol(x, y, null, null);
        }
        //caso en el que es necesario hacer una rotacion derecha
        if (AB.lla > x){
            AB.izq = insertar(AB.izq, x, y);
            if (AB.prio > AB.izq.prio)
                return rotd(AB);
        }
        //caso en el que es necesario hacer una rotacion izquerda
        else if (AB.lla < x){
            AB.der = insertar(AB.der, x, y);
            if(AB.prio > AB.der.prio){
                return roti(AB);
            }
        }
        
        return AB;
        
    }
    
    //metodo para hacer una rotacion izquierda
    public static Arbol roti(Arbol AB){
        Arbol ABd = AB.der;
        AB.der = ABd.izq;
        ABd.izq = AB;
        return ABd;
    }
    //metodo para hacer una rotacion derecha
    public static Arbol rotd(Arbol AB){
        Arbol ABi = AB.izq;
        AB.izq = ABi.der;
        ABi.der = AB;
        return ABi;
    }
    //metodo que imprime en post orden el arbol ingresado
    public static String imprimir(Arbol AB){
        String p = "";
        //caso de un nodo exterior
        if (AB == null){
            p = "[]" + p;
        }
        //caso en el que se tiene un nodo interior
        else{
            p = imprimir(AB.izq) + imprimir(AB.der) + "(" + AB.lla + "," + AB.prio + ")" + p;
        }
        return p;
    }
    //metodo para calcular el costo promedio de hacer una busqueda exitosa en un arbol     
    public static double costoPromedio(Arbol AB){
        double cp = cTotal(AB,0)/nNodos(AB);
        return cp;
    }
    
    //metodo para calcular el costo total de la busqueda de todos los nodos en el arbol
    public static double cTotal(Arbol AB, int c){
        //caso en el que se llega a un nodo exterior
        if (AB == null){
            return 0;
        }
        //caso en el que se tiene un nodo interior
        else{
            c += 1;
            c += cTotal(AB.izq, c) + cTotal(AB.der, c); 
        }
        return (double)c;
    }
    
    //metodo para calcular el  umero total de nodos en un arbol
    public static int nNodos(Arbol AB){
        int n = 0;
        //caso en el que se tiene un nodo interior
        if(AB != null){
            n = 1 + nNodos(AB.izq) + nNodos(AB.der);
        }
        return n;
    }
    //metodo para crear un arbol a partir de los el archivo en la direccion dir
    public static Arbol crearArbol(String dir){
        archivos a = new archivos();
        String t = a.leerTxt(dir);
        String[] txt = t.split("/");
        Arbol AB =new Arbol(0, 0.00, null,null);
        //iteracion para recuperar cada par ordenado x, y 
        for(int i = 1; i < txt.length; i++){
            String[] xy = txt[i].split(" ");
            //creacion de un nuevo arbol
            if (i == 1){
                int x1 = Integer.parseInt(xy[0]);
                double y1 = Double.parseDouble(xy[1]);
                AB = new Arbol(x1, y1, null, null);
            }
            //insercion de los otros valores al arbol ya existente
            else{
                int xi = Integer.parseInt(xy[0]);
                double yi = Double.parseDouble(xy[1]);
                AB = insertar(AB, xi, yi);
            }
        }
        return AB;
    }
  
    //main donde se llaman todos los metodos para obtener el resultadso buscado en la consola
    public static void main(String[] args) {
        //lugar donde insertal la direccion del archivo de texto a leer
        String dir = "C:\\Users\\Bacchus\\Desktop\\Txt\\resultP1.txt";
        //creacion de un nuevo arbol con los datos en el archivo de texto
        Arbol AB = crearArbol(dir);
        //impresion de el arbol en postorden
        String prnt = imprimir(AB);
        System.out.println(prnt);
        //impresion del costo promedio de busqueda en el arbol
        double cp = costoPromedio(AB);
        System.out.println("El costo esperado de busqueda es " + cp);
        System.out.println("");
        
//Parte 2
        //vector con los valores que toma n
        int[] N = new int[]{1024, 2048, 4096, 8192, 16384, 32768, 65536};
        //iteracion sobre los distintos valores de n
        for (int j = 0; j < 7; j++){
            int n = N[j];
            double S = 0;
            System.out.println("El costo esperado del experimento para n = " + n + " es");
            //repeticion del experimento 10 veces
            for (int i = 1; i <= 10; i++){
                Arbol E = experimento(n);
                double cpe = costoPromedio(E);
                //costo promedio en la repeticion i del experimento
                S += cpe;
                System.out.println(cpe + " ");
            }
            //promedio de los costos promedios
            double cppe = S/10;
            //impresion de los resultados
            System.out.println("El costo promedio promedio es " + cppe);
            System.out.println("");
        }
    }
        
    // metodo para la realizacion de una repeticion del experim ento con n = n
    public static Arbol experimento(int n){
        // se crea un nuevo arbol con el nodo de llave 1 y prioridad aleatoria
        Arbol E = new Arbol(1, Math.random(), null, null);
        //insersion del resto de los nodos con prioridades aleatorias
        for (int i = 2; i <= n; i++){
           E = insertar(E, i, Math.random());
        }
        return E;
    }
}
