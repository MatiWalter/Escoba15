package vista;

import modelo.EstadoJuego;

public interface IVista {

	void cambioEnJugador();
	void cambioDeTurno();
	void hayCambioDeEstado(EstadoJuego estadoJuego);
	void hayError(String string);
	void cartasJugador();
	void cartasMesa();
	void cartasTirada();
	void hayEscobaReal();
	void hayQuince();
	void hayGanador();
	void escoba();
	void jugadorListo();
	void hayGuardado();
}
