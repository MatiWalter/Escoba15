package modelo;

public interface IJugador {
	String getNombre();
	ICarta[] getMano();
	String toString();
	int getPuntos();
	boolean getOro7();
	int getSietes();
	int cantidadCartas();
	int getEscoba();
	int getOros();
	boolean isReady();
}
