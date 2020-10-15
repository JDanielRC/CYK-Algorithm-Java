import java.util.ArrayList;

public class Casilla {
	
	private ArrayList<Generador> generadores;
	private ArrayList<Par> generadoPor;
	private int cordX, cordY;
	
	public Casilla(ArrayList<Generador> generadores, ArrayList<Par> generadoPor, int cordX, int cordY) {
		super();
		this.generadores = generadores;
		this.generadoPor = generadoPor;
		this.cordX = cordX;
		this.cordY = cordY;
	}
	public ArrayList<Generador> getGeneradores() {
		return generadores;
	}
	public void setGeneradores(ArrayList<Generador> generadores) {
		this.generadores = generadores;
	}
	public ArrayList<Par> getGeneradoPor() {
		return generadoPor;
	}
	public void setGeneradoPor(ArrayList<Par> generadoPor) {
		this.generadoPor = generadoPor;
	}
	public int getCordX() {
		return cordX;
	}
	public void setCordX(int cordX) {
		this.cordX = cordX;
	}
	public int getCordY() {
		return cordY;
	}
	public void setCordY(int cordY) {
		this.cordY = cordY;
	}
	
	
	
}
