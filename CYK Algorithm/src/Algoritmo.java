import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Algoritmo extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Generador[] generadores;
	private Casilla[][] tabla;
	
	public Algoritmo(String[][] matriz, String cadena) throws IOException {
		
		//CREAR TABLA DE COINCIDENCIAS
		
		this.tabla = new Casilla[cadena.length()][cadena.length()];
		
		// GENERAR GRAMATICA EN BASE A MATRIZ
		this.generadores = new Generador[matriz.length];
		for (int i = 0; i < matriz.length; i++) {
			ArrayList<String> producciones = new ArrayList<String>();
			for (int j = 0; j < matriz[i].length; j++) {
				producciones.add(matriz[i][j]);
			}
			this.generadores[i] = new Generador(matriz[i][0], producciones);
		}
		
		//PRODUCCIONES DE CADA GENERADOR
		/*for (int i = 0; i < this.generadores.length; i++) {
			System.out.println(this.generadores[i].getNombre() + " genera: ");
			for (int j = 1; j < this.generadores[i].getProducciones().size(); j++) {
				System.out.println(this.generadores[i].getProducciones().get(j));
			}
		}*/
		
		
		//PRIMER DIAGONAL DE LA TABLA, PARA PODER HACER BACKTRACKING
		for (int i = 0; i < tabla.length; i++) { 
			Casilla casilla = new Casilla(new ArrayList<Generador>(), new ArrayList<Par>(), i, i);
			for (int j = 0; j < this.generadores.length; j++) { //recorremos todos los generadores
				for (int k = 0; k < this.generadores[j].getProducciones().size(); k++) {
					if (cadena.substring(i, i+1).equals(this.generadores[j].getProducciones().get(k))) {
						casilla.getGeneradores().add(this.generadores[j]);
					}
				}
				
			}
			tabla[i][i] = casilla;
			
		}
		
		for (int i = 1; i < tabla.length; i++) { //se ejecuta n-1 veces para llenar el resto de la tabla (1 vez por diagonal)
			int pos1 = 0;
			int pos2 = i;
			for (int j = tabla.length - (tabla.length - i); j < tabla.length; j++) { //numero de espacios en la diagonal a insertar
				int x = pos1;
				int y = pos1;
				Casilla casillaAgregada = new Casilla(new ArrayList<Generador>(), new ArrayList<Par>(), pos1, pos2);
				for (int k = tabla.length - i; k < tabla.length; k++) { //numero de comparaciones a realizar
					String res = "";
					Par posiblePar = null;
					if (tabla[pos1][y].getGeneradores().size() > 1 && tabla[x+1][pos2].getGeneradores().size() > 1) { //el caso cuando las casillas a comparar, ambas tienen más de 1 generador
						for (int b = 0; b < this.tabla[pos1][y].getGeneradores().size(); b++) {
							for (int c = 0; c < this.tabla[x+1][pos2].getGeneradores().size(); c++) {
								res = tabla[pos1][y].getGeneradores().get(b).getNombre() + tabla[x+1][pos2].getGeneradores().get(c).getNombre();
								posiblePar = new Par(tabla[pos1][y].getGeneradores().get(b), tabla[x+1][pos2].getGeneradores().get(c), new Coordenadas(pos1, y), new Coordenadas(x+1, pos2));
								for (int n = 0; n < this.generadores.length; n++) {
									for (int m = 0;  m < this.generadores[n].getProducciones().size(); m++) { //checa todas las producciones de ese generador
										if (this.generadores[n].getProducciones().get(m).equals(res)) { //busca que sea igual al resultado del backtracking
											boolean yaEsta = false;
											for (int t = 0; t < casillaAgregada.getGeneradores().size(); t++) {
												if (this.generadores[n].getNombre().equals(casillaAgregada.getGeneradores().get(t).getNombre())){
													yaEsta = true;
												}
											}
											if (!yaEsta) {
												casillaAgregada.getGeneradores().add(this.generadores[n]); //en caso de coincidencia, lo agrega a la lista de generadores en esa casilla
												if (posiblePar.getIzquierdo().getNombre() != "" && posiblePar.getDerecho().getNombre() != "") {
													casillaAgregada.getGeneradoPor().add(posiblePar);
												}
											}
										}
									}
								}
							}
						}
					} else if (tabla[pos1][y].getGeneradores().size() == 1 && tabla[x+1][pos2].getGeneradores().size() == 1) { //caso de tener un solo generador, entonces solo comparamos con el primero de los generadores
						res = tabla[pos1][y].getGeneradores().get(0).getNombre() + tabla[x+1][pos2].getGeneradores().get(0).getNombre();
						posiblePar = new Par(tabla[pos1][y].getGeneradores().get(0), tabla[x+1][pos2].getGeneradores().get(0), new Coordenadas(pos1, y), new Coordenadas(x+1, pos2));
						for (int n = 0; n < this.generadores.length; n++) {
							for (int m = 0;  m < this.generadores[n].getProducciones().size(); m++) { //checa todas las producciones de ese generador
								if (this.generadores[n].getProducciones().get(m).equals(res)) { //busca que sea igual al resultado del backtracking
									boolean yaEsta = false;
									for (int t = 0; t < casillaAgregada.getGeneradores().size(); t++) {
										if (this.generadores[n].getNombre().equals(casillaAgregada.getGeneradores().get(t).getNombre())){
											yaEsta = true;
										}
									}
									if (!yaEsta) {
										casillaAgregada.getGeneradores().add(this.generadores[n]); //en caso de coincidencia, lo agrega a la lista de generadores en esa casilla
										if (posiblePar.getIzquierdo().getNombre() != "" && posiblePar.getDerecho().getNombre() != "") {
											casillaAgregada.getGeneradoPor().add(posiblePar);
										}
									}
								}
							}
						}
					} else if (tabla[pos1][y].getGeneradores().size() > 1) { //caso para cuando hay mas de un generador con el que comparar  AB con A =  AA y AB en el lado izquierdo
						for (int b = 0; b < this.tabla[pos1][y].getGeneradores().size(); b++) {
							if (tabla[x+1][pos2].getGeneradores().size() == 0) {
								break;
							}
							res = tabla[pos1][y].getGeneradores().get(b).getNombre() + tabla[x+1][pos2].getGeneradores().get(0).getNombre();
							posiblePar = new Par(tabla[pos1][y].getGeneradores().get(b), tabla[x+1][pos2].getGeneradores().get(0), new Coordenadas(pos1, y), new Coordenadas(x+1, pos2));
							for (int n = 0; n < this.generadores.length; n++) {
								for (int m = 0;  m < this.generadores[n].getProducciones().size(); m++) { //checa todas las producciones de ese generador
									if (this.generadores[n].getProducciones().get(m).equals(res)) { //busca que sea igual al resultado del backtracking
										boolean yaEsta = false;
										for (int t = 0; t < casillaAgregada.getGeneradores().size(); t++) {
											if (this.generadores[n].getNombre().equals(casillaAgregada.getGeneradores().get(t).getNombre())){
												yaEsta = true;
											}
										}
										if (!yaEsta) {
											casillaAgregada.getGeneradores().add(this.generadores[n]); //en caso de coincidencia, lo agrega a la lista de generadores en esa casilla
											if (posiblePar.getIzquierdo().getNombre() != "" && posiblePar.getDerecho().getNombre() != "") {
												casillaAgregada.getGeneradoPor().add(posiblePar);
											}
										}
									}
								}
							}
						}
					} else if (tabla[x+1][pos2].getGeneradores().size() > 1) {//caso para cuando hay mas de un generador con el que comparar  AB con A =  AA y AB en el lado derecho
						for (int b = 0; b < this.tabla[x+1][pos2].getGeneradores().size(); b++) {
							if (tabla[pos1][y].getGeneradores().size() == 0) {
								break;
							}
							res = tabla[pos1][y].getGeneradores().get(0).getNombre() + tabla[x+1][pos2].getGeneradores().get(b).getNombre();
							posiblePar = new Par(tabla[pos1][y].getGeneradores().get(0), tabla[x+1][pos2].getGeneradores().get(b), new Coordenadas(pos1, y), new Coordenadas(x+1, pos2));
							for (int n = 0; n < this.generadores.length; n++) {
								for (int m = 0;  m < this.generadores[n].getProducciones().size(); m++) { //checa todas las producciones de ese generador
									if (this.generadores[n].getProducciones().get(m).equals(res)) { //busca que sea igual al resultado del backtracking
										boolean yaEsta = false;
										for (int t = 0; t < casillaAgregada.getGeneradores().size(); t++) {
											if (this.generadores[n].getNombre().equals(casillaAgregada.getGeneradores().get(t).getNombre())){
												yaEsta = true;
											}
										}
										if (!yaEsta) {
											casillaAgregada.getGeneradores().add(this.generadores[n]); //en caso de coincidencia, lo agrega a la lista de generadores en esa casilla
											if (posiblePar.getIzquierdo().getNombre() != "" && posiblePar.getDerecho().getNombre() != "") {
												casillaAgregada.getGeneradoPor().add(posiblePar);
											}
										}  //yaEsta se asegura de no repetir el mismo generador en la misma casilla
									}
									
								}
							}
						}
					}
					
					x++;
					y++;
				}
				tabla[pos1][pos2] = casillaAgregada;
				pos1++;
				pos2++;
			}		
		}
		
		//IMPRIMIR TABLA
		for (int i = 0; i < this.tabla.length; i++) {
			for (int j = 0; j < this.tabla[i].length; j++) {
				if (this.tabla[i][j] == null) {
					System.out.print(" ,");
				} else {
					if (this.tabla[i][j].getGeneradores().size() > 0) {
						for (int k = 0; k < this.tabla[i][j].getGeneradores().size() ; k++) {
							if (this.tabla[i][j].getGeneradores().size() == 1) {
								System.out.print(this.tabla[i][j].getGeneradores().get(k).getNombre());
							} else {
								System.out.print(this.tabla[i][j].getGeneradores().get(k).getNombre());
							}
						}
						System.out.print(",");
					} else {
						System.out.print("" + 0 + ",");
					}
				}
			}
			System.out.println();
		}
		
		//CODIGO PARA GENERAR EL FORMATO PARA IMPRIMIR EL ARBOL EN VSC
		//NO TOCAR, ALTAMENTE VOLATIL, SPAGUETTHI CODE PERO FUNCIONA
		
		FileOutputStream fs;
		PrintWriter pw;
		fs = new FileOutputStream("Grafo.txt");
		pw = new PrintWriter(new FileWriter("D:\\Documentos\\poo-workspace\\MateCompusPractica2\\Grafo.txt"));
	
		pw.println("digraph {");
		if (tabla[0][tabla.length-1].getGeneradores().size() == 0) {
			System.out.println("La cadena no pertenece a la gramática");
		}
		for (int v = 0; v < tabla[0][tabla.length-1].getGeneradores().size(); v++) {
			if (tabla[0][tabla.length-1].getGeneradores().get(v).getNombre().equals("S")) {
				Casilla current = tabla[0][tabla.length - 1];
				boolean yaAcabe = false;
				int cont = 1;
				System.out.println("COPIE ESTE FORMATO PARA GENERAR EL ARBOL EN VSC");
				while (!yaAcabe) {
					if (current.getGeneradoPor().size() == 0) {
						yaAcabe = true;
						for (int i = 0; i < current.getGeneradores().get(0).getProducciones().size(); i++) {
							if (Character.isLowerCase(current.getGeneradores().get(0).getProducciones().get(i).charAt(0))) {
								System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
										current.getGeneradores().get(0).getProducciones().get(i));
								pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
										current.getGeneradores().get(0).getProducciones().get(i));
							}
						}
						break;
					}
					Casilla left = tabla[current.getGeneradoPor().get(0).getCordIzq().getIzquierdo()][current.getGeneradoPor().get(0).getCordIzq().getDerecho()];
					Casilla right = tabla[current.getGeneradoPor().get(0).getCordDer().getIzquierdo()][current.getGeneradoPor().get(0).getCordDer().getDerecho()];
					System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
							left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY());
					pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
							left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY());
					System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
							right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY());
					pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
							right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY());
					if (left.getCordX() == left.getCordY() && right.getCordX() == right.getCordY()) { //caso cuando los hijos son los ultimos, es decir, ya no tienen hijos
						yaAcabe = true;
						for (int j = 0; j < right.getGeneradores().get(0).getProducciones().size(); j++) {
							if (Character.isLowerCase(right.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
								System.out.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
										+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
								pw.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
										+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
								cont++;
							}
						}
						for (int j = 0; j < left.getGeneradores().get(0).getProducciones().size(); j++) {
							if (Character.isLowerCase(left.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
								System.out.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
										+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
								pw.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
										+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
								cont++;
							}
						}
					} else if (left.getCordX() == left.getCordY() && right.getCordX() != right.getCordY()) {
						for (int j = 0; j < left.getGeneradores().get(0).getProducciones().size(); j++) {
							if (Character.isLowerCase(left.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
								System.out.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
										+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
								pw.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
										+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
								cont++;
							}
						}
						current = right;
					} else if (right.getCordX() == right.getCordY() && left.getCordX() != left.getCordY()) {
						for (int j = 0; j < right.getGeneradores().get(0).getProducciones().size(); j++) {
							if (Character.isLowerCase(right.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
								System.out.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
										+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
								pw.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
										+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
								cont++;
							}
						}
						current = left;
					} else  if (right.getCordX() != right.getCordY() && left.getCordX() != left.getCordY()){ //caso para cuando izquierda y derecha no han llegado a la diagonal base
						boolean finishIzq = false;
						boolean finishDer = false;
						Casilla savedRight = right;
						Casilla savedLeft = left;
						while (!finishIzq) {
							if (left.getCordX() != left.getCordY()) {
								current = left;
							} else {
								for (int j = 0; j < left.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(left.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
												+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
												+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								current = right;
							}
							
							left = tabla[current.getGeneradoPor().get(0).getCordIzq().getIzquierdo()][current.getGeneradoPor().get(0).getCordIzq().getDerecho()];
							right = tabla[current.getGeneradoPor().get(0).getCordDer().getIzquierdo()][current.getGeneradoPor().get(0).getCordDer().getDerecho()];
							System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY());
							pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY());
							System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY());
							pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY());
							if (left.getCordX() == left.getCordY() && right.getCordX() == right.getCordY()) { //caso cuando los hijos son los ultimos, es decir, ya no tienen hijos
								yaAcabe = true;
								for (int j = 0; j < right.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(right.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								for (int j = 0; j < left.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(left.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
												+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
												+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								finishIzq = true;
							} else if (left.getCordX() == left.getCordY() && right.getCordX() != right.getCordY()) {
								current = right;
							} else if (right.getCordX() == right.getCordY() && left.getCordX() != left.getCordY()) {
								for (int j = 0; j < right.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(right.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								current = left;
							} else {
								finishIzq = true;
							}
						}
						while (!finishDer) {
							if (savedRight.getCordX() != savedRight.getCordY()) {
								current = savedRight;
							} else {
								for (int j = 0; j < savedRight.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(savedRight.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(savedRight.getGeneradores().get(0).getNombre() + savedRight.getCordX() + savedRight.getCordY() + "->"
												+ savedRight.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(savedRight.getGeneradores().get(0).getNombre() + savedRight.getCordX() + savedRight.getCordY() + "->"
												+ savedRight.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								current = savedLeft;
							}
							left = tabla[current.getGeneradoPor().get(0).getCordIzq().getIzquierdo()][current.getGeneradoPor().get(0).getCordIzq().getDerecho()];
							right = tabla[current.getGeneradoPor().get(0).getCordDer().getIzquierdo()][current.getGeneradoPor().get(0).getCordDer().getDerecho()];
							System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY());
							pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY());
							System.out.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY());
							pw.println(current.getGeneradores().get(0).getNombre() + current.getCordX() + current.getCordY() + "->" + 
									right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY());
							if (left.getCordX() == left.getCordY() && right.getCordX() == right.getCordY()) { //caso cuando los hijos son los ultimos, es decir, ya no tienen hijos
								finishDer = true;
								for (int j = 0; j < right.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(right.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								for (int j = 0; j < left.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(left.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
												+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(left.getGeneradores().get(0).getNombre() + left.getCordX() + left.getCordY() + "->"
												+ left.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
							} else if (left.getCordX() == left.getCordY() && right.getCordX() != right.getCordY()) {
								current = right;
							} else if (right.getCordX() == right.getCordY() && left.getCordX() != left.getCordY()) {
								for (int j = 0; j < right.getGeneradores().get(0).getProducciones().size(); j++) {
									if (Character.isLowerCase(right.getGeneradores().get(0).getProducciones().get(j).charAt(0))) {
										System.out.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										pw.println(right.getGeneradores().get(0).getNombre() + right.getCordX() + right.getCordY() + "->"
												+ right.getGeneradores().get(0).getProducciones().get(j) + cont);
										cont++;
									}
								}
								current = left;
							} else {
								finishDer = true;
							}
						}
						yaAcabe = true;
					}
				}
				fs.close();
				pw.println("}");
				pw.close();
				
				String dotPath = "D:\\Programas escuela\\Graphviz2.38\\bin\\dot.exe";
				String fileInputPath = "D:\\Documentos\\poo-workspace\\MateCompusPractica2\\Grafo.txt";
			    String fileOutputPath = "D:\\Documentos\\poo-workspace\\MateCompusPractica2\\Grafo1.jpg";
			      
			    String tParam = "-Tjpg";
			    String tOParam = "-o";
				String[] cmd = new String[5];
				cmd[0] = dotPath;
			    cmd[1] = tParam;
			    cmd[2] = fileInputPath;
			    cmd[3] = tOParam;
			    cmd[4] = fileOutputPath;
				Runtime rt = Runtime.getRuntime();
				rt.exec(cmd);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				BufferedImage myPicture = ImageIO.read(new File("D:\\Documentos\\poo-workspace\\MateCompusPractica2\\Grafo1.jpg"));
				setSize(myPicture.getWidth() + 50, myPicture.getHeight() + 50);
				setTitle("Arbol");
				JLabel picLabel = new JLabel(new ImageIcon(myPicture));
				add(picLabel);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				setVisible(true);
			} else if (tabla[0][tabla.length-1].getGeneradores().size() == 0) {
				System.out.println("La cadena no pertenece a la gramática");
			}
			
			if (v == tabla[0][tabla.length-1].getGeneradores().size() - 1) {
				System.out.println("La cadena pertenece a la gramática");
			}
		}
	}
	
	
	
	
	public static void main(String[] args) throws IOException {
		
		//String[][] gramatica = { {"S", "AB", "BC"}, {"A", "AB", "a"}, {"B", "CC", "b"}, {"C", "AB", "a"}};
		//Algoritmo a = new Algoritmo(gramatica, "bab");
		//String[][] gramatica2 = { {"S", "AB", "SS", "AC", "BD", "BA"}, {"A", "a"}, {"B", "b"}, {"C", "SB"}, {"D", "SA"}};
		//Algoritmo b = new Algoritmo(gramatica2, "aabbab");
		//String[][] gramatica3 = { {"S", "AB", "XB"}, {"A", "a"}, {"B", "b"}, {"X", "AS"}, {"T", "AB", "XB"}};
		//Algoritmo c = new Algoritmo(gramatica3, "aaabbb");
		//String[][] gramatica4 = { {"S", "AB"}, {"C", "a"}, {"A", "CD", "CF"}, {"B", "EB", "c"}, {"D", "b"}, {"E", "c"}, {"F", "AD"}};
		//Algoritmo d = new Algoritmo(gramatica4, "aaabbbcc");
		//String[][] gramatica5 = {{"S", "AB", "XB"}, {"T", "AB", "XB"}, {"X", "AT"}, {"A", "a"}, {"B", "b"}};
		//Algoritmo e = new Algoritmo(gramatica5, "aaabbb");
		
		
		
		//String[][] gramatica6 = {{"S", "AB", "AC", "AD"}, {"C", "SB", "SD"}, {"A", "ZA", "a"}, {"Z", "a"}, {"B", "b"}, {"D", "b"}};
		//Algoritmo f = new Algoritmo(gramatica6, "aaabb"); //primera, mas a's o igual
		//Algoritmo f1 = new Algoritmo(gramatica6, "aabb");
		//Algoritmo f2 = new Algoritmo(gramatica6, "aabbb");
		
		
		String[][] gramatica7 = {{"S", "SD", "AB", "SA", "a"}, {"B", "CB", "b"}, {"A", "a"}, {"C", "b"}, {"D", "AB"}};
		Algoritmo g = new Algoritmo(gramatica7, "abaaba"); //segunda, mas b's o igual
		//Algoritmo g = new Algoritmo(gramatica7, "ab"); //segunda, mas b's o igual
		//g = new Algoritmo(gramatica7, "aabbb"); //segunda, mas b's o igual

		
		


	}
}
