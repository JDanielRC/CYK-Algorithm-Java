
public class Par {
	
	Generador izquierdo, derecho;
	Coordenadas cordIzq, cordDer;

	public Par(Generador izquierdo, Generador derecho, Coordenadas cordIzq, Coordenadas cordDer) { //clase que guarde dos ints como coordenadas, para no tener tantos ints en el objeto
		super();
		this.izquierdo = izquierdo;
		this.derecho = derecho;
		this.cordIzq = cordIzq;
		this.cordDer = cordDer;
	}
	
	public Par(Generador izquierdo, Generador derecho) { //clase que guarde dos ints como coordenadas, para no tener tantos ints en el objeto
		super();
		this.izquierdo = izquierdo;
		this.derecho = derecho;
	}

	public Generador getIzquierdo() {
		return izquierdo;
	}

	public void setIzquierdo(Generador izquierdo) {
		this.izquierdo = izquierdo;
	}

	public Generador getDerecho() {
		return derecho;
	}

	public void setDerecho(Generador derecho) {
		this.derecho = derecho;
	}

	public Coordenadas getCordIzq() {
		return cordIzq;
	}

	public void setCordIzq(Coordenadas cordIzq) {
		this.cordIzq = cordIzq;
	}

	public Coordenadas getCordDer() {
		return cordDer;
	}

	public void setCordDer(Coordenadas cordDer) {
		this.cordDer = cordDer;
	}
	
	
}
