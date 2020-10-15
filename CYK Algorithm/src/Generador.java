import java.util.ArrayList;

public class Generador {
	
	String nombre;
	ArrayList<String> producciones;
	
	public Generador(String nombre, ArrayList<String> producciones) {
		this.producciones = producciones;
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ArrayList<String> getProducciones() {
		return producciones;
	}

	public void setProducciones(ArrayList<String> producciones) {
		this.producciones = producciones;
	}
	
	
}
