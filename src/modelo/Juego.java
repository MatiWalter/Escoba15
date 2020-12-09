package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.observer.ObservableRemoto;

public class Juego extends ObservableRemoto implements IObservable, Serializable {

	// Attributes

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Jugador> jugadores = new ArrayList<>();
	private Jugador mesa = new Jugador("mesa");
	private Mazo mazo = new Mazo();
	private EstadoJuego estadoJuego;
	private String msgError = "";
	private int jugadorActual = 0;
	private IJugador ganador;
	private int ultimoJuego = 0;

	// Patron Observer

	private void notificar(Cambios cambio) {
		try {
			super.notificarObservadores((Object) cambio);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// Methods

	@Override
	public boolean iniciarJuego() throws RemoteException {
		boolean jugar = false;
		int i = 0;
		for (Jugador jugador : jugadores) {
			if (jugador.isReady()) {
				i++;
			}
			if (i == jugadores.size()) {
				jugar = true;
			}
		}
		if (jugadores.size() >= 2 && jugar) {
			mesa.nuevaCarta(mazo.getCarta());
			mesa.nuevaCarta(mazo.getCarta());
			mesa.nuevaCarta(mazo.getCarta());
			mesa.nuevaCarta(mazo.getCarta());
			this.notificar(Cambios.CARTAS_MESA);
			for (Jugador jugador : jugadores) {
				repartirCartas(jugador);
			}
			jugadorActual = 0;
			notificar(Cambios.TURNO);
			escobaReal();
			estadoJuego = EstadoJuego.JUGANDO;
			notificar(Cambios.ESTADO_JUEGO);
		} else {
			estadoJuego = EstadoJuego.CONFIGURANDO;
			msgError = "Necesita al menos dos jugadores para comenzar";
			notificar(Cambios.ERROR);
		}
		return jugar;
	}

	@Override
	public Jugador agregarJugador(String jugador) throws RemoteException {
		Jugador j = null;
		if (jugador.length() >= 1) {
			j = new Jugador(jugador);
			jugadores.add(j);
			notificar(Cambios.LISTA_JUGADORES);

		} else {
			msgError = "Nombre del jugador no valido";
			notificar(Cambios.ERROR);
		}
		return j;
	}

	@Override
	public void repartirCartas(Jugador jugador) throws RemoteException {
		if (jugadores.contains(jugador)) {
			jugador.nuevaCarta(mazo.getCarta());
			jugador.nuevaCarta(mazo.getCarta());
			jugador.nuevaCarta(mazo.getCarta());
			notificar(Cambios.CARTAS_JUGADOR);
		} else {
			msgError = "El jugador no se encontro";
			notificar(Cambios.ERROR);
		}
	}

	// Getters & Setters

	@Override
	public String getMsgError() throws RemoteException {
		return msgError;
	}

	@Override
	public EstadoJuego getEstadoJuego() throws RemoteException {
		return estadoJuego;
	}
	
	@Override
	public Jugador getJugador(Jugador jugador) throws RemoteException {
		Jugador j = null;
		for (Jugador jugAux : jugadores) {
			if(jugador.getNombre().equals(jugAux.getNombre())) {
				j = jugAux;
			}
		}
		return j;
	}

	@Override
	public IJugador[] getJugadores() throws RemoteException {
		IJugador[] ij = new Jugador[jugadores.size()];
		int i = 0;
		for (Jugador j : jugadores) {
			ij[i++] = j;
		}
		return ij;
	}

	@Override
	public ICarta[] getManoJugador() throws RemoteException {
		return jugadores.get(jugadorActual).getMano();
	}

	@Override
	public ICarta[] getCartasJugador(IJugador jugador) throws RemoteException {
		ICarta[] aux = null;
		int index = 0;
		for (Jugador j : jugadores) {
			if (j.getNombre().equals(jugador.getNombre())) {
				index = jugadores.indexOf(j);
				aux = jugadores.get(index).getMano();
			}
		}
		return aux;
	}

	@Override
	public int getCantidadJugadores() throws RemoteException {
		return jugadores.size();
	}

	@Override
	public void setEstadoJuego(EstadoJuego estadoJuego) throws RemoteException {
		this.estadoJuego = estadoJuego;
		this.notificar(Cambios.ESTADO_JUEGO);
	}

	@Override
	public int getJugadorActual() throws RemoteException {
		return jugadorActual;
	}

	@Override
	public void reset() throws RemoteException {
		jugadores = new ArrayList<>();
		mesa = new Jugador("Mesa");
		mazo = new Mazo();
		msgError = "";
		jugadorActual = 0;
	}

	@Override
	public ICarta[] getCartasMesa() throws RemoteException {
		return mesa.getMano();
	}

	@Override
	public void tirarCarta(int index) throws RemoteException {
		Jugador jugAux = jugadores.get(jugadorActual);
		Carta[] cartaAux = jugAux.getManoCarta();
		if (index >= 0 && index < cartaAux.length) {
			Carta aux = jugAux.tirarCarta(cartaAux[index]);
			mesa.nuevaCarta(aux);
			if (mazo.cantidadCartas() != 0 || jugAux.cantidadCartasMano() != 0) {
				this.notificar(Cambios.CARTA_TIRADA);
				this.cambiarTurno();
				this.notificar(Cambios.TURNO);
			}
		} else {
			msgError = "Numero de carta no valido";
			this.notificar(Cambios.ERROR);
		}
	}

	@Override
	public void armarJuego(ArrayList<Integer> indices) throws RemoteException {
		Jugador j = jugadores.get(jugadorActual);
		ArrayList<Carta> cartas = new ArrayList<>();
		if (indices.get(0) >= 0 && indices.get(0) <= j.cantidadCartasMano() - 1) {
			cartas.add(j.getManoCarta()[indices.get(0)]);
			j.sumarCarta();
		} else {
			msgError = "Numero de carta no valido";
			this.notificar(Cambios.ERROR);
		}
		for (int i = 1; i < indices.size(); i++) {
			if (indices.get(i) >= 0 && indices.get(i) <= mesa.cantidadCartasMano() - 1) {
				cartas.add(mesa.getManoCarta()[indices.get(i)]);
				j.sumarCarta();
			} else {
				msgError = "Numero de carta no valido";
				this.notificar(Cambios.ERROR);
			}
		}
		int suma = j.armarJuego(cartas);
		if (suma == 15) {
			Carta cartaAux = j.tirarCarta(cartas.get(0));
			puntaje(jugadores.get(jugadorActual), cartaAux);
			this.notificar(Cambios.CARTAS_JUGADOR);
			for (int i = 1; i < cartas.size(); i++) {
				cartaAux = mesa.tirarCarta(cartas.get(i));
				puntaje(jugadores.get(jugadorActual), cartaAux);
				if (mesa.cantidadCartasMano() == 0) {
					j.haveEscoba();
					this.notificar(Cambios.ESCOBA);
				}
			}
			this.notificar(Cambios.CARTAS_MESA);
			this.notificar(Cambios.QUINCE);
			ultimoJuego = jugadorActual;
			cambiarTurno();
			this.notificar(Cambios.TURNO);
		} else {
			msgError = "Las cartas no suman quince";
			notificar(Cambios.ERROR);
		}
	}

	private void puntaje(Jugador jugador, Carta carta) {
		if (carta.is7Oro()) {
			jugador.have7Oro();
		}
		if (carta.is7()) {
			jugador.have7();
		}
		if (carta.isOro()) {
			jugador.haveOro();
		}
	}

	private void escobaReal() {
		int suma = 0;
		ICarta[] cartasAux = mesa.getMano();
		for (ICarta iCarta : cartasAux) {
			suma += iCarta.getValor();
		}
		if (suma == 15) {
			jugadores.get(jugadorActual).haveEscoba();
			notificar(Cambios.ESCOBA_REAL);
			for (int i = 0; i < mesa.cantidadCartas(); i++) {
				Carta cartaAux = mesa.tirarCarta(mesa.getCarta(i));
				puntaje(jugadores.get(jugadorActual), cartaAux);
			}
			mesa.nuevaMano();
		}
	}

	@Override
	public void cambiarTurno() throws RemoteException {
		if (mazo.cantidadCartasActual() == 0 && jugadores.get(jugadores.size() - 1).cantidadCartasMano() == 0) {
			for (int i = 0; i < mesa.cantidadCartasMano(); i++) {
				Carta cartaAux = mesa.tirarCarta(mesa.getCarta(i));
				puntaje(jugadores.get(ultimoJuego), cartaAux);
				jugadores.get(ultimoJuego).sumarCarta();
			}
			ganador();
		} else {
			if (jugadorActual == jugadores.size() - 1) {
				jugadorActual = 0;
				this.notificar(Cambios.TURNO);
			} else {
				jugadorActual++;
				this.notificar(Cambios.TURNO);
			}
			if (jugadorActual == 0 && jugadores.get(0).cantidadCartasMano() == 0) {
				for (Jugador jugador : jugadores) {
					this.repartirCartas(jugador);
				}
			}
		}

	}

	
	// TODO no ganar punto si se tiene misma cantidad de cartas que otro jugador
	
	@Override
	public void ganador() throws RemoteException {
		int oroMax = jugadores.get(0).getOros();
		int indexJ = 0;
		for (int i = 1; i < jugadores.size(); i++) {
			if (jugadores.get(i).getOros() > oroMax) {
				oroMax = jugadores.get(i).getOros();
				indexJ = i;
			}
		}
		jugadores.get(indexJ).ganarPunto();
		int cartasMax = jugadores.get(0).cantidadCartas();
		indexJ = 0;
		for (int i = 1; i < jugadores.size(); i++) {
			if (jugadores.get(i).cantidadCartas() > cartasMax) {
				cartasMax = jugadores.get(i).cantidadCartas();
				indexJ = i;
			}
		}
		jugadores.get(indexJ).ganarPunto();
		int sieteMax = jugadores.get(0).getSietes();
		indexJ = 0;
		for (int i = 1; i < jugadores.size(); i++) {
			if (jugadores.get(i).getSietes() > sieteMax) {
				sieteMax = jugadores.get(i).getSietes();
				indexJ = i;
			}
		}
		jugadores.get(indexJ).ganarPunto();
		for (Jugador jugador : jugadores) {
			jugador.sumarPuntos();
		}
		int puntosMax = jugadores.get(0).getPuntos();
		indexJ = 0;
		for (int i = 1; i < jugadores.size(); i++) {
			if (jugadores.get(i).getPuntos() > puntosMax) {
				puntosMax = jugadores.get(i).getPuntos();
				indexJ = i;
			}
		}
		ganador = jugadores.get(indexJ);
		notificar(Cambios.GANADOR);
	}

	@Override
	public IJugador getGanador() throws RemoteException {
		return ganador;
	}

	@Override
	public int getCantidadCartas() throws RemoteException {
		return mazo.cantidadCartasActual();
	}

	@Override
	public String leerAyuda() throws RemoteException {
		File archivo;
		FileReader lectorArchivo;
		String texto = "";
		try {
			archivo = new File("help.txt");
			lectorArchivo = new FileReader(archivo);
			BufferedReader br = new BufferedReader(lectorArchivo);
			String aux = "";
			while (true) {
				aux = br.readLine();
				if (aux != null)
					texto = texto + aux + "\n";
				else
					break;
			}
			br.close();
			lectorArchivo.close();
			return texto;
		} catch (IOException e) {
			msgError = "Se produjo un error al leer el archivo";
			this.notificar(Cambios.ERROR);
		}
		return texto;
	}

	@Override
	public void setReady(Jugador jugador, boolean listo) throws RemoteException {
		for (Jugador jugAux : jugadores) {
			if(jugador.getNombre().equals(jugAux.getNombre())) {
				jugAux.setReady(listo);
			}
		}
		
	}
	
	public void guardar() throws RemoteException, FileNotFoundException, IOException {
		ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("juego.obj"));
		salida.writeObject(this);
		salida.close();
		this.notificar(Cambios.GUARDAR);
	}
	
	public Juego recuperar() throws RemoteException, IOException {
		ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("juego.obj"));
		Juego juego = null;
		try {
			juego = (Juego) entrada.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		entrada.close();
		return juego;
	}

	@Override
	public boolean getJuegoAnterior() throws RemoteException {
		File archivo = new File("juego.obj");
		boolean jAnterior = false;
		if (archivo.exists()) {
			jAnterior = true;
		} else {
			msgError = "El archivo no existe";
			this.notificar(Cambios.ERROR);
		}
		return jAnterior;
	}

	@Override
	public IJugador getMesa() throws RemoteException {
		IJugador m = mesa;
		return m;
	}
	

}
