/*	
	<<<<<<<< DOCUMENTACIÓN >>>>>>>>>
	Ariel Bravo
	Computación 3er semestre
	16 octubre 2016

	[UPDATED]
	11vo semestre (en el instituto)
	21 noviembre 2020

	Nota:
	Varias de las cosas listadas en la "documentación" siguiente las quité y aparecen
	con la etiqueta [DEPRECATED]

	Nota 2:
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
		[DEPRECATED]
		words	: conjuntos de letras disponibles para el usuario.
		
		exp 	: almacena el valor de 'e' en BigInteger.
		nbig	: almacena el valor de 'n' en BigIntefer.
		fi_n 	: almacena el producto de (p - 1)(q - 1) en BigInteger.

		[DEPRECATED]
		blocks 		: pila string que almacena en bloques de 4 los índices respectivos a cada letra.
		[DEPRECATED]
		crypstack	: pila BigInteger que almacena en "bloques de 4" el contenido de blocks cifrado.
		
	Métodos:
		check() 	: revisa que la entrada sea válida, si no lo es se cierra el programa.
		
		fi_nor()	: calcula el valor de p y q. Uso un teorema que no recuerdo si vimos en clase o no
					  pero, que tenía en mis hojas con las que estudié, el teorema dice:

					  "Si n no tiene divisores primos ≤ sqrt(n), entonces n es primo."

					  Entonces, lo que yo hago es crear dos objetos BigInteger a, m; una pila llamada 'stack', 
					  un objeto 'criba' (para esto tuve que crear una clase Criba que regresa todos los primos 
					  menores al parámetro dado), calculo la raíz de 'n' y se la paso al método getNum de criba.

					  Dentro del while, busco un múltiplo de 'n' con los primos que dio criba, usando el método
					  mod() de BigInteger, cuando lo encuentro le doy ese valor a 'p'. Luego, 'q' será igual al
					  cociende de 'n' y 'p'; asigno a fi_n el producto de (p - 1)(q - 1) porque después lo nece-
					  sitaré.

		checkmcd()	: reviso que el mcd(e, (p - 1)(q - 1)) = 1 usando el método gcd() de BigInteger 
					  (había hecho una clase que me calculara esto pero la descarté porque me ahorraba líneas),
					  si el mcd es != 1 se cierra el programa.
		
		[DEPRECATED]
		toBlock()	: obtiene el valor del índice de cada letra de 'word' y los agrupa en bloques de 4 que 
					  posteriormente mete a la pila 'blocks'.

		encrypter()	: crea dos objetos BigInteger r, b y usa el método modPow() de BigInteger para calcular
					  b^exp(mod nbig) que almacena en 'r'. Luego, meto a 'r' a la pila crypstack.

		decrypter()	: crea tres objetos BigInteger b, d, r y usa el método modInverse() de BigInteger para
					  calcular exp^-1(mod fi_n) que almacena en 'r'. Luego, mete 'r' a la pila blocks (ya que
					  blocks se vacío cuando sacabamos los bloques de 4 para encriptarlos).

		wordparsed(): convierte cada caracter a su representación ascii. Sustituye al método toBlock() que no
					  tengo idea de porqué hice eso la primera vez

		asciiToString(): deshace lo que el método anterior hizo


*/

import java.util.Scanner;
import java.util.*;
import java.lang.Math;
import java.math.BigInteger;

public class RSA{
	static int n, e;
	static String word;
	static BigInteger nbig, exp;
	static BigInteger fi_n;
	

	public static void main(String[] args){
		
		n = Integer.parseInt(args[0]);
		e = Integer.parseInt(args[1]);

		nbig = new BigInteger(args[0]);
		exp = new BigInteger(args[1]);
		
		word = args[2];

		List<String> parsed = wordparsed();
		// check();
		fi_nor();
		// checkmcd();
		List<BigInteger> cr = encrypter(parsed);
		decrypter(cr);
	}

	static void fi_nor(){
		BigInteger a, m;
		Stack <Integer> stack;
		Criba criba = new Criba();
		int root, p = 0, q = 0;;

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

		System.out.println("p: " + p);
		System.out.println("q: " + q);

		fi_n = new BigInteger(String.valueOf((p - 1)*(q - 1)));

		System.out.println("fi_n: " + fi_n);
	}

	static void checkmcd(){
		BigInteger x;
		x = exp.gcd(fi_n);
		
		if(Integer.parseInt(x.toString()) != 1){
			System.out.println("Error: MCD is not 1.");
			System.exit(1);
		}
	}

	static List<String> wordparsed(){
		List<String> parsed = new ArrayList<String>();

		for(int i = 0; i < word.length(); i++)
			parsed.add(String.valueOf((int) word.charAt(i)));

		return parsed;
	}

	static List<BigInteger> encrypter(List<String> toCrypt){
		BigInteger r, b;
		List<BigInteger> crypted = new ArrayList<BigInteger>();

		for (int i = 0; i < toCrypt.size(); i++){
			b = new BigInteger(toCrypt.get(i));
			r = b.modPow(exp, nbig);
			crypted.add(r);
		}

		System.out.println();
		System.out.println("Crypted: \t" + crypted);
		System.out.println("Crypted: \t" + asciiToString(crypted));

		return crypted;
	}

	static void decrypter(List<BigInteger> crypted){
		BigInteger b, d, r;
		List<BigInteger> decrypted = new ArrayList<BigInteger>();
		d = exp.modInverse(fi_n);
		
		for (int i = 0; i < crypted.size(); i++){
			b = crypted.get(i);
			r = b.modPow(d, nbig);
			decrypted.add(r);
		}

		System.out.println();
		System.out.println("Decrypted: \t" + decrypted);
		System.out.println("Decrypted: \t" + asciiToString(decrypted));
		System.out.println();
	}

	static String asciiToString(List<BigInteger> list){
		String temp = "";

		for (int i = 0; i < list.size(); i++){
			temp += Character.toString ((char) list.get(i).intValue());
		}

		return temp;
	}
}