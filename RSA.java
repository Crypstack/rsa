/*	
	<<<<<<<< DOCUMENTACIÓN >>>>>>>>>
	Ariel Bravo
	Computación 3er semestre
	16 octubre 2016
	
	Nota:
	Dado que estoy haciendo uso de la clase BigInteger, el programa funciona adecuadamente con números
	relativamente grandes. Por lo que si se usa algo como (21, 5) el resultado no será el esperado

	Try with (n, e):
	2537, 13
	3337, 79
	143, 7
	943, 7
	3233, 17
	
	Variables / Objetos:
		n 		: módulo definido como el producto de dos primos.
		e 		: exponente tal que mcd(e, (p - 1)(q - 1)) = 1.
		p, q 	: factorización de n.
		i 		: variable que uso para iterar.

		words 	: variable en donde se almacenará la entrada del usuario.
		words	: conjuntos de letras disponibles para el usuario.
		exp 	: almacena el valor de 'e' en BigInteger.
		nbig	: almacena el valor de 'n' en BigIntefer.
		fact 	: almacena el producto de (p - 1)(q - 1) en BigInteger.

		blocks 		: pila string que almacena en bloques de 4 los índices respectivos a cada letra.
		crypstack	: pila BigInteger que almacena en "bloques de 4" el contenido de blocks cifrado.
		
	Métodos:
		check() 	: revisa que la entrada sea válida, si no lo es se cierra el programa.
		
		factor()	: calcula el valor de p y q. Uso un teorema que no recuerdo si vimos en clase o no
					  pero, que tenía en mis hojas con las que estudié, el teorema dice:

					  "Si n no tiene divisores primos ≤ sqrt(n), entonces n es primo."

					  Entonces, lo que yo hago es crear dos objetos BigInteger a, m; una pila llamada 'stack', 
					  un objeto 'criba' (para esto tuve que crear una clase Criba que regresa todos los primos 
					  menores al parámetro dado), calculo la raíz de 'n' y se la paso al método getNum de criba.

					  Dentro del while, busco un múltiplo de 'n' con los primos que dio criba, usando el método
					  mod() de BigInteger, cuando lo encuentro le doy ese valor a 'p'. Luego, 'q' será igual al
					  cociende de 'n' y 'p'; asigno a fact el producto de (p - 1)(q - 1) porque después lo nece-
					  sitaré.

		checkmcd()	: reviso que el mcd(e, (p - 1)(q - 1)) = 1 usando el método gcd() de BigInteger 
					  (había hecho una clase que me calculara esto pero la descarté porque me ahorraba líneas),
					  si el mcd es != 1 se cierra el programa.

		toBlock()	: obtiene el valor del índice de cada letra de 'word' y los agrupa en bloques de 4 que 
					  posteriormente mete a la pila 'blocks'.

		encrypter()	: crea dos objetos BigInteger r, b y usa el método modPow() de BigInteger para calcular
					  b^exp(mod nbig) que almacena en 'r'. Luego, meto a 'r' a la pila crypstack.

		decrypter()	: crea tres objetos BigInteger b, d, r y usa el método modInverse() de BigInteger para
					  calcular exp^-1(mod fact) que almacena en 'r'. Luego, mete 'r' a la pila blocks (ya que
					  blocks se vacío cuando sacabamos los bloques de 4 para encriptarlos).



*/

import java.util.Scanner;
import java.util.Stack;
import java.lang.Math;
import java.math.BigInteger;

public class RSA{
	static int n, e;
	static int p = 0, q = 0;
	static int i = 0;
	static String word, words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static BigInteger exp, nbig;
	static BigInteger fact;
	static Stack <String> blocks = new Stack <String> ();
	static Stack <BigInteger> crypstack = new Stack <BigInteger> ();
	

	public static void main(String[] args){
		
		n = Integer.parseInt(args[0]);
		e = Integer.parseInt(args[1]);

		nbig = new BigInteger(args[0]);
		exp = new BigInteger(args[1]);

		word = args[2].toUpperCase();

		check();
		factor();
		checkmcd();
		toBlock();
		encrypter();
		decrypter();
	}

	static void check(){
		for(i = 0; i < word.length(); i++){
			if(words.indexOf(word.charAt(i)) == -1){
				System.out.println("Error: Character not found.");
				System.exit(1);
			}
		}
	}

	static void factor(){
		BigInteger a, m;
		Stack <Integer> stack;
		Criba criba = new Criba();
		int root;

		root = (int) Math.sqrt((double)(n));
		stack = criba.getNum(root);

		while(!stack.empty()){
			m = new BigInteger(String.valueOf(stack.peek()));
			a = nbig.mod(m);
			if(Integer.parseInt(a.toString()) == 0)
				p = stack.pop();
			else
				stack.pop();
		}
		
		q = n/p;

		fact = new BigInteger(String.valueOf((p - 1)*(q - 1)));
	}

	static void checkmcd(){
		BigInteger x;
		x = exp.gcd(fact);
		
		if(Integer.parseInt(x.toString()) != 1){
			System.out.println("Error: MCD is not 1.");
			System.exit(1);
		}
	}

	static void toBlock(){
		String wordencrypted = "";

		for(int i = 0; i < word.length(); i++){
			
			if(words.indexOf(word.charAt(i)) < 10){
				wordencrypted += '0';
				wordencrypted += words.indexOf(word.charAt(i));
			}
			else{
				wordencrypted += words.indexOf(word.charAt(i)); 
			}
		
			if(wordencrypted.length() == 4){
				blocks.push(wordencrypted);
				wordencrypted = "";
			}
		}

		if(wordencrypted.length() > 1){
			wordencrypted += '0';
			wordencrypted += '0';
			blocks.push(wordencrypted);
			wordencrypted = "";
		}

		System.out.println();
		System.out.println("Normal: \t" + blocks);
	}

	static void encrypter(){
		BigInteger r, b;

		while(!blocks.empty()){
			b = new BigInteger(blocks.pop());
			r = b.modPow(exp, nbig);
			crypstack.push(r);
		}

		System.out.println();
		System.out.println("Crypted: \t" + crypstack);
	}

	static void decrypter(){
		BigInteger b, d, r;

		d = exp.modInverse(fact);
		
		while(!crypstack.empty()){
			b = crypstack.pop();
			r = b.modPow(d, nbig);
			blocks.push(r.toString());
		}

		System.out.println();
		System.out.println("Decrypted: \t" + blocks);
		System.out.println();
	}
}