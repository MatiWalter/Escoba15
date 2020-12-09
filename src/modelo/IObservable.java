package modelo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import ar.edu.unlu.rmimvc.observer.IObservableRemoto;

public interface IObservable extends IObservableRemoto {

	boolean iniciarJuego() throws RemoteException;

	Jugador agregarJugador(String jugador) throws RemoteException;

	void repartirCartas(Jugador jugador) throws RemoteException;

	String getMsgError() throws RemoteException;

	EstadoJuego getEstadoJuego() throws RemoteException;

	IJugador[] getJugadores() throws RemoteException;

	ICarta[] getManoJugador() throws RemoteException;

	ICarta[] getCartasJugador(IJugador jugador) throws RemoteException;

	int getCantidadJugadores() throws RemoteException;

	void setEstadoJuego(EstadoJuego estadoJuego) throws RemoteException;

	int getJugadorActual() throws RemoteException;

	void reset() throws RemoteException;

	ICarta[] getCartasMesa() throws RemoteException;

	void tirarCarta(int index) throws RemoteException;

	void armarJuego(ArrayList<Integer> indices) throws RemoteException;

	void cambiarTurno() throws RemoteException;

	void ganador() throws RemoteException;

	IJugador getGanador() throws RemoteException;

	int getCantidadCartas() throws RemoteException;

	String leerAyuda() throws RemoteException;

	Jugador getJugador(Jugador jugador) throws RemoteException;

	void setReady(Jugador jugador, boolean listo) throws RemoteException;

	boolean getJuegoAnterior() throws RemoteException;
	
	Juego recuperar() throws RemoteException, IOException;
	
	void guardar() throws RemoteException, FileNotFoundException, IOException;

	IJugador getMesa() throws RemoteException;

}