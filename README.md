# CYK-Algorithm-Java
Cocke-Younger-Kasami algorithm for testing if a certain chain belongs to a context free grammar.

IDE used: Eclipse.

Input requires a matrix with the grammar you want to use: {{"S", "SD", "AB", "SA", "a"}, {"B", "CB", "b"}, {"A", "a"}, {"C", "b"}, {"D", "AB"}} is an example.
An Algoritmo object has to be created, receiving the matrix and the chain to be examined: Algoritmo g = new Algoritmo(gramatica7, "abaaba").
Things to have in mind, is that in lines 211, 426, 427, 428 and 447 in Algoritmo.java include path files that have to be changed to whatever place you locate this project.
Output will print the CYK table:
SA,SD,S,S,S,S,
 ,BC,0,0,0,0,
 , ,SA,S,S,S,
 , , ,SA,SD,S,
 , , , ,BC,0,
 , , , , ,SA,
 Plus all of the transitions done to form the recursion tree, which will be shown in a window like this:
![alt text](https://github.com/JDanielRC/CYK-Algorithm-Java/blob/main/CYK%20Algorithm/Grafo1.jpg?raw=true)
