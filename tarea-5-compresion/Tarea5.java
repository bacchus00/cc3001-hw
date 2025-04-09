package tarea5;

 //imports
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.*;

//creacion clase Arbol
class Arbol{
	char l;
        int frec;
	Arbol izq;
	Arbol der;

        //letra en el nodo
	public Arbol(char l){
		this.l = l;
		izq = null;
		der = null;
	}
        
        //frecuencia de letra en el nodo
        public Arbol(int frec){
		this.frec = frec;
		izq = null;
		der = null;
	}
        
        public Arbol(char l, int frec, Arbol izq, Arbol der){
		this.l = l;
                this.frec = frec;
		this.izq = izq;
		this.der = der;
	}
        
    }

public class Tarea5 {

    //metodo principal que ocupa a los demas
    public static void main(String[] args) {
        //scanner de la entrada del usuario
        Scanner sc = new Scanner(System.in);
        System.out.println("Nombre del archivo de entrada");
        String entrada = sc.nextLine();
        System.out.println("Nombre del archivo de salida");
        String salida = sc.nextLine();
        
        String texto = leerTxt("C:\\Users\\Bacchanalia\\Desktop\\T5\\" + entrada);//se recupera el texto como un string
        int[] frec = new int[300]; //se crea lista de frecuencias (la codificacion ISO-8859-1 llega a menos que 300)
        Arbol[] a = new Arbol[300]; // se crea la lista (heap) de Arboles
        Arbol nulo = new Arbol('\u0000', Integer.MAX_VALUE, null, null); //Arbol nulo
        int fti = 0;//numero total de caracteres
        
        for(int i = 0; i < a.length; i++){ //se inicializa como nulos toda la lista
            a[i] = nulo;
        }
        
        for(int i = 0; i < texto.length(); i++){ //se va contando la frecuencia de las letras
            char l = texto.charAt(i);
            int al = (int)l; //conversion a ASCII
            frec[al] += 1; // Se agrega uno a la frecuencia de la letra en el indice correspondiente a la letra en ASCII
        }
        
        for(int al = 0; al < frec.length; al++){
            fti += frec[al];
            if(frec[al]!= 0){//si la letra existe en el texto
                char l = (char)al; // se treansforma devuelta a char el ASCII
                if (l != '\u0000'){
                    Arbol A = new Arbol(l, frec[al], null, null); //se crea un nodo con la letra y su frecuencia
                    a = insert(a, A); // se inserta el nodo al heap
                }
            }
        }
        
        float ft = (float) fti; //numero total de caracteres pasado a float
        
        //se lleva a cabo la compresion
        while(a[1].frec != Integer.MAX_VALUE || a[1].l != '\u0000' || a[1].der != null){
            Arbol H1 = a[0];  //se saca del heap al hijo derecho
            a = extraerMin(a);
            Arbol H2 = a[0]; //se saca del heap al hijo izquierdo
            a = extraerMin(a);
            Arbol P = new Arbol('\u0000', H1.frec + H2.frec, H1, H2); //se arma un nuevo arbol con raiz nulla y la frec igual a la suma de las frecuencias de los hijos
            a = insert(a, P);
        }
        
        String sf = cod(a[0], "", "j", ft); // se llama el metodo para conseguir la codificacion
        String[][] lf = ordena(sf); // se llama al metodo para ordenar de manera decendente los caracteres segun la frecuencia
        escribir(lf, salida); //se llama al metodo para escribir el archivo de salida
    }
    
    //metodo para la lectura del archivo de entrada que retorna el texto en un string
    public static String leerTxt(String d){
       String texto = "";
        try{
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(d), "ISO-8859-1"));;
            String t = "";
            String bfr;
            while((bfr = bf.readLine()) != null){
                t = t + bfr + "\n";
            }
            texto = t;
        }
        catch(Exception e){
            System.err.println("No se encontro el archivo");
        }
        return texto;
    }    

    //metodo de insercion del elemento A en un heap a
    public static Arbol[] insert(Arbol[] a, Arbol A){
        int n = 0;
        //ciclo iterativo para encontrar el primer elemento nulo de la lista (final del heap)
        for(int i = 0; i < a.length; i++){
            if(a[i].frec == Integer.MAX_VALUE && a[i].l == '\u0000' && a[i].der == null){
                a[i] = A; //se inserta normalmente el elemento A
                n = i;//se guarda el indice del ultimo elemento no nulo ingresado
                break;
            }
        }
        //se reordena el heap para preservar la restriccion de orden (nodo A "trepa" el arbol)
        for(int j = n; j > 0 && a[j].frec < a[j/2].frec; j/=2){
            Arbol t = a[j];
            a[j] = a[j/2];
            a[j/2] = t;
        }
        return a;
    }
    
    //metodo para la extraccion y reordenamiento del arbol a
    public static Arbol[] extraerMin(Arbol[] a){
        int n = 0;
        //ciclo iterativo para encontrar el ultimo elemento no nulo
        for(int i = 0; i < a.length; i++){
            if(a[i+1].frec == Integer.MAX_VALUE && a[i+1].l == '\u0000' && a[i+1].der == null){
                n = i; //se guarda la posicion del ultimo elemento
                break;
            }
        }
        //se reordena el arbol para reestablecer la restriccion de orden tras extraer la raiz
        a[0] = a[n];
        a[n] = new Arbol('\u0000', Integer.MAX_VALUE, null, null);
        int j = 0;
        while(2*j + 1 <= n){
            int k = 2*j + 1;
            if(k < n && a[k + 1].frec < a[k].frec){
                k = k + 1;
            }
            if(a[j].frec < a[k].frec){
                break;
            }
            Arbol t = a[j];  
            a[j] = a[k];
            a[k] = t;  
            j = k;
        }
        for(int i = 0; i < a.length; i++){
        }
        //retornamos la lista modificada
        return a;
    }
    
    //metodo para la codificacion de los caracteres
    public static String cod(Arbol A, String cod, String h, float ft){
        if(A.izq == null && A.der == null){ //caso base cuando se llega a las hojas
            if(A.l == '\n'){//caso problematico se trata aparte
                return "\\n" + "°" + ((int) A.l) + "°" + cod + "°" + (100*(A.frec/ft)) + "¬";
            }
            if(A.l == '\r'){//caso problematico se trata aparte
                return "\\r" + "°" + ((int) A.l) + "°" + cod + "°" + (100*(A.frec/ft)) + "¬";
            }
            else{//caso normal donde simplemente ingresamos el caracter a un string 
               return Character.toString(A.l) + "°" + ((int) A.l) + "°" + cod + "°" + (100*(A.frec/ft)) + "¬";
            }
        }
        if(h == "d"){//caso en el que se avanzo a la derecha
            cod += "0";
            return cod(A.izq, cod, "i", ft) + cod(A.der, cod, "d", ft); //recucion con ambos hijos
        }
        else{ //caso en el que se avanzo a la izquierda
            cod += "1";
            return cod(A.izq, cod, "i", ft) + cod(A.der, cod, "d", ft); //recurcion con ambos hijos
        }
    }
    
    //metodo que ordena de manera decendiente los caracteres de acuerdo a la frecuencia
    public static String[][] ordena(String sf){
        String l1[] = sf.split("¬");//arreglo con cada uno de los caracteres y su informacion respectiva
        String[][] l2 = new String[l1.length][3];//arreglo de arreglo de los caracteres con su informacion respectiva
        String[][] lf = new String[l1.length][3];//arreglo final
        for(int i = 0; i < l1.length; i++){//ciclo iterativo para llenar l2 con los arreglos correspondientes a el caracter y su informacion respectiva
            l2[i] = l1[i].split("°");
        }
       
        //proceso de ordenamiento de los valores de acuerdo a la frecuencia de los caracteres con "lazysort" (a la mala), 
        String[] Min = {"0", "0", "0", "0"}; 
        int im = 0;
        for(int j = 0; j < l1.length; j++){//iteracion para llenar lf con los elementos ordenados
            String[] Max = Min;
            for(int i = 0; i < l1.length; i++){// iteracion para encontrar el elemento de maxima frecuencia en el arreglo
                    float x = Float.parseFloat(l2[i][3]);
                    if(x >= Float.parseFloat(Max[3])){
                        Max = l2[i];
                        im = i;
                    }
            }
            lf[j] = Max;
            l2[im] = Min;//se sobre escribe el elemento ya ordenado de la lista original
        }
        return lf;
    }
    
    //metodo para la escritura del archivo de salida
    public static void escribir(String[][] lf, String salida){
        try {
            PrintWriter lineaSalida = new PrintWriter(salida);
            lineaSalida.println("Carácter Cód ASCII(dec.) codificación frecuencia");
            for(int i = 0; i < lf.length; i++){
                lineaSalida.println("'" + lf[i][0] + "'" + " " + lf[i][1] + " " + lf[i][2] + " " + lf[i][3] + "%");
            }
            lineaSalida.close();
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(Tarea5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

