package tarea3;

import java.util.Scanner;
import java.util.Stack;

//constructor clase arbol 
class Arbol{
	String valor;
	Arbol izq;
	Arbol der;

	public Arbol(String valor){
		this.valor = valor;
		izq = null;
		der = null;
	}

	public Arbol(String valor, Arbol izq, Arbol der){
		this.valor = valor;
		this.izq = izq;
		this.der = der;
	}
    }

public class Tarea3 {
    
    //main del programa   
    public static void main(String[] args) {
        //se recupera el input del usuario y se transforma en un arreglo
        Scanner sc = new Scanner(System.in);
        System.out.println("Expresion a derivar en notacion polaca");
        String expp = sc.nextLine();
        String[] expl = expp.split(" ");
        System.out.println("Variable con respecto a la cual derivar");
        String var = sc.nextLine();
        
        //se utiliza el stack (pila) y el arreglo anterior para armar el arbol binario que contendra la expresion
        Stack st = new Stack();
        for (int i = 0; i < expl.length; i++){
            //se arma un arbol en caso de que se lea un operador con los siguientes elementos en la cola como hijos
            if(expl[i].equals("+") || expl[i].equals("-") || expl[i].equals("*") || expl[i].equals("/")){
                Arbol der = (Arbol) st.pop();
                Arbol izq = (Arbol) st.pop();
                Arbol AB = new Arbol(expl[i], izq, der);
                st.push(AB);
            }
            //en caso contrario se ingresa el valor leido como un nodo
            else{
                Arbol AB = new Arbol(expl[i], null, null);
                st.push(AB);
            }      
        }   
        //se recupera el arbol final
        Arbol ABf = (Arbol) st.pop();
        
        //se llama al metodo encargado de derivar el arbol
        Arbol dAB = d(ABf, var);
        
        //se llama al metodo que simplifica el arbol tantas veces como terminos siendo operados haya (para deshacerse de los ceros molestosos)
        for (int i = 1; i < 2*expl.length; i++){
            dAB = simp(dAB);
        }
        
        //se llama al metodo que crea el string que representa la expresion casi final
        String expf = expf(dAB, "");
        
        //un pequeño arreglo  para un caso patologico
        expf = expf.replace("+-", "-");
        
        //se imprime en pantalla la expresion final
        System.out.println(expf);
    }
    
    //metodo para derivar el arbol de la expresion
    public static Arbol d(Arbol AB, String var){
        String val = AB.valor;      
        //caso en el que el nodo raiz contiene la variable con respecto a la cual se quiere derivar
        if(val.equals(var)){
            Arbol dAB = new Arbol("1", null, null);
            return (dAB);
        }
        //caso en el que el nodo raiz contiene el opreador suma (regla de la suma de derivadas)
        if(val.equals("+")){
            Arbol dAB = new Arbol("+", d(AB.izq, var), d(AB.der, var));
            return (dAB);
        }
        //caso en el que el nodo raiz contiene el opreador resta (regla de la resta de derivadas)
        if(val.equals("-")){
            Arbol dAB = new Arbol("-", d(AB.izq, var), d(AB.der, var));
            return (dAB);
        }
        //caso en el que el nodo raiz contiene el opreador multiplicacion (regla de la multiplicacion de derivadas)
        if(val.equals("*")){
            Arbol dAB1 = new Arbol("*", d(AB.izq, var), AB.der);
            Arbol dAB2 = new Arbol("*", AB.izq, d(AB.der, var));
            Arbol dAB = new Arbol("+", dAB1, dAB2);
            return (dAB);
        }
        //caso en el que el nodo raiz contiene el opreador division (regla de la division de derivadas)
        if(val.equals("/")){
            Arbol dAB1 = new Arbol("*", d(AB.izq, var), AB.der);
            Arbol dAB2 = new Arbol("*", AB.izq, d(AB.der, var));
            Arbol dAB3 = new Arbol("-", dAB1, dAB2);
            Arbol dAB4 = new Arbol("*", AB.der, AB.der);
            Arbol dAB = new Arbol("/", dAB3, dAB4);
            return (dAB);
        }
        //caso en el que el nodo raiz contiene un numero o una variable que no es con respecto a la cual se quiere derivar
        else{
            Arbol dAB = new Arbol("0", null, null);
            return (dAB);     
        }
    }
    
    //metodo para simplificar el arbol ya derivado
    public static Arbol simp(Arbol AB){
        //caso en el que se llega a las hojas
        if(AB.izq == null){
            return AB;
        }
        if(AB == null){
            return null;
        }
        //casos anteiores
        else{
            //caso en el que se multiplica un termino por cero
            if((AB.izq.valor.equals("0") || AB.der.valor.equals("0")) && (AB.valor.equals("*") || AB.valor.equals("/"))){
                AB.valor = "0";
                AB.der = null;
                AB.izq = null;
                return (AB);
            }
            //caso 1 en el que se multiplica un termino por 1
            if(AB.izq.valor.equals("1") && (AB.valor.equals("*") || AB.valor.equals("/"))){
                AB = AB.der;
                return (AB);
            }
            //caso 2 en el que se multiplica un termino por 1
            if(AB.der.valor.equals("1") && (AB.valor.equals("*") || AB.valor.equals("/"))){
                AB = AB.izq;
                return (AB);
            }
            //caso 1 en el que se suma 0 a otro termino
            if(AB.izq.valor.equals("0") && AB.valor.equals("+")){
                AB = AB.der;
                return (AB);
            }
            //caso 2 en el que se suma 0 a otro termino
            if(AB.der.valor.equals("0") && (AB.valor.equals("+") || AB.valor.equals("-"))){
                AB = AB.izq;
                return (AB);
            }
            //se sigue recorriendo el arbol
            else{
                Arbol ABf = new Arbol(AB.valor,simp(AB.izq),simp(AB.der));
                return (ABf);
            }
        }
    }
    
    //metodo que convierte el arbol resultante en la expresion final in-fijo
    public static String expf(Arbol AB, String op){
        String d = "";
       //caso en el que se llega a las hojas del arbol
        if(AB.izq == null){
            return (AB.valor);
        }
        //caso patologico en el que a cero se le resta un termino
        if(AB.valor.equals("-") && AB.izq.valor.equals("0")){
            d = AB.valor + expf(AB.der, AB.valor);
            return d;            
        }
        //casos en los que no es necesario usar parentesis (relacion padre a hijo)
        if((AB.valor.equals("+") && AB.der.valor.equals("+")) || (AB.valor.equals("+") && AB.izq.valor.equals("+")) || 
                (AB.valor.equals("+") && AB.der.valor.equals("-")) || (AB.valor.equals("-") && AB.izq.valor.equals("+")) || 
               (AB.valor.equals("*") && AB.der.valor.equals("*")) || (AB.valor.equals("*") && AB.izq.valor.equals("*")) || 
                (AB.valor.equals("*") && AB.der.valor.equals("/")) || (AB.valor.equals("/") && AB.izq.valor.equals("*"))){
            d = expf(AB.izq, AB.valor) + AB.valor + expf(AB.der, AB.valor);
            return d;
        }
        //casos en los que no es necesario usar parentesis (relacion hijo a padre)
        if(op.equals("") || 
        (op.equals("+") && AB.valor.equals(op)) || (op.equals("+") && AB.valor.equals("-")) || (op.equals("-") && AB.valor.equals("+")) || 
        (op.equals("*") && AB.valor.equals(op)) || (op.equals("*") && AB.valor.equals("/")) || (op.equals("1") && AB.valor.equals(op))){
            d = expf(AB.izq, AB.valor) + AB.valor + expf(AB.der, AB.valor);
            return d;
        }
        //casos en los que los parentesis son necesarios
        else{
            d = "(" + expf(AB.izq, AB.valor) + AB.valor + expf(AB.der, AB.valor) + ")";
            return d;
        }
    }
}

