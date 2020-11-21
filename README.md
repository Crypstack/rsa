# RSA algorithm

It is necessary to install Java SDK

To compile the code follow:

	javac Criba.java
	javac RSA.java

Then execute:

	java RSA n e wordToCrypt
	e.g:
	java RSA 2537 13 ariel

Output example:

	p: 43
	q: 59
	fi_n: 2436

	Crypted:        [1552, 1453, 794, 1342, 1550]
	Crypted:        ?????

	Decrypted:      [97, 114, 105, 101, 108]     
	Decrypted:      ariel