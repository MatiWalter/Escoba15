package modelo;

import java.io.Serializable;
import java.util.Random;

public class Mazo implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Carta[] cartas = new Carta[40];
	private int cartaActual = 0;
	private int cantidadActual = 40;

	public Mazo() {
		Palo[] palos = new Palo[4];
		palos = Palo.values();
		int carta = 0;
		for (int palo = 0; palo < palos.length; palo++) {
			for (int valor = 1; valor <= 10; valor++) {
				cartas[carta++] = new Carta(palos[palo], valor, getFigura(valor));
			}
		}
		mezclar();
	}

	private String getFigura(int valor) {
		String respuesta = "";
		switch (valor) {
		case 1:
			respuesta = "As";
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			respuesta = Integer.toString(valor);
			break;
		case 8:
			respuesta = "Sota(10)";
			break;
		case 9:
			respuesta = "Caballo(11)";
			break;
		case 10:
			respuesta = "Rey(12)";
			break;
		}
		return respuesta;
	}

	public void mezclar() {
		Random r = new Random();
		for (int i = 0; i < cartas.length; i++) {
			int pos = r.nextInt(40);
			Carta aux = cartas[i];
			cartas[i] = cartas[pos];
			cartas[pos] = aux;
		}
		cartaActual = 0;
	}
	
	/**
	 * Este metodo saca una carta del ArrayList de cartas, simular repartir una carta
	 * @return Retorna una carta del mazo
	 */
	public Carta getCarta() {
		if (cartaActual >= cartas.length) 
			return null;
		else {
			cantidadActual -= 1;
			return cartas[cartaActual ++];	
		}
			
	}
	
	/**
	 * Este metodo devuelve el ArrayList de cartas por completo
	 * @return ArrayList de cartas
	 */
	public Carta[] getCartas() {
		return cartas;
	}
	
	/**
	 * Metodo para controlar la cantidad de cartas disponibles en el mazo
	 * @return Cantidad de cartas en el mazo
	 */
	public int cantidadCartas() {
		return cartas.length;
	}

	public int cantidadCartasActual() {
		return cantidadActual;
	}
}
