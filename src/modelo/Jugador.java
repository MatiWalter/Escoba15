package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Jugador implements IJugador, Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Carta> mano = new ArrayList<>();
	private String nombre;
	private int puntos = 0;
	private int sietes = 0;
	private int escoba = 0;
	private int oro = 0;
	private boolean oro7 = false;
	private int cantidadCartas = 0;
	private boolean ready = false;

	public Jugador(String nombre) {
		this.nombre = nombre;
	}
	
	public void nuevaCarta(Carta carta) {
		mano.add(carta);
	}

	public int cantidadCartasMano() {
		return mano.size();
	}

	public ICarta[] getMano() {
		ICarta[] cartas = new ICarta[mano.size()];
		int i = 0;
		for (Carta c : mano) {
			cartas[i++] = (ICarta) c;
		}
		return cartas;
	}

	public Carta[] getManoCarta() {
		Carta[] cartas = new Carta[mano.size()];
		int i = 0;
		for (Carta c : mano) {
			cartas[i++] = c;
		}
		return cartas;
	}

	public Carta getCarta(int index) {
		return mano.get(index);
	}

	public void deselect() {
		for (Carta carta : mano) {
			carta.setSelected(false);
		}
	}

	public void setSelected(Carta carta) {
		int index = mano.indexOf(carta);
		Carta cartaAux = mano.get(index);
		if (cartaAux.isSelected()) {
			cartaAux.setSelected(false);
		} else if (cartasSeleccionadas() == 0) {
			cartaAux.setSelected(true);
		} else {
			for (Carta c : mano) {
				if (c.isSelected()) {
					c.setSelected(false);
				}
			}
			cartaAux.setSelected(true);
		}
	}

	public Carta getCartaSeleccionada() {
		Carta carta = null;
		for (Carta c : mano) {
			if (c.isSelected()) {
				carta = c;
			}
		}
		return carta;
	}

	public Carta tirarCarta(Carta carta) {
		mano.remove(carta);
		return carta;
	}

	public int armarJuego(ArrayList<Carta> cartas) {
		int suma = 0;
		for (Carta carta : cartas) {
			suma += carta.getValor();
		}
		return suma;
	}

	public void nuevaMano() {
		mano = new ArrayList<>();
	}

	public void sumarPuntos() {
		int suma = 0;
		suma += getEscoba();
		if (getOro7()) {
			suma += 1;
		}
		this.puntos += suma;
	}

	public int cartasSeleccionadas() {
		int cantidad = 0;
		for (Carta carta : mano)
			if (carta.isSelected())
				cantidad++;
		return cantidad;
	}

	// Getters

	public String getNombre() {
		return nombre;
	}

	public String toString() {
		return getNombre();
	}

	public int cantidadCartas() {
		return cantidadCartas;
	}

	public int getPuntos() {
		return puntos;
	}

	public int getSietes() {
		return sietes;
	}

	public int getOros() {
		return oro;
	}

	public int getEscoba() {
		return escoba;
	}

	public boolean getOro7() {
		return oro7;
	}

	// Sumar puntos

	public void have7Oro() {
		this.oro7 = true;
	}

	public void have7() {
		this.sietes += 1;
	}

	public void haveOro() {
		this.oro += 1;
	}

	public void haveEscoba() {
		this.escoba += 1;
	}

	public void ganarPunto() {
		this.puntos += 1;
	}

	public void sumarCarta() {
		this.cantidadCartas += 1;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

}
