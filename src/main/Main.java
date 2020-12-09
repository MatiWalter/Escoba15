package main;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import controlador.Controlador;
import modelo.Juego;
import vista.Consola;
import vista.Gui;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) {

		Juego juego = new Juego();
		Servidor srv = new Servidor("localhost", 8888);
		boolean jAnt = false;
		try {
			if (!juego.getJuegoAnterior()) {
				try {
					srv.iniciar(juego);
				} catch (RemoteException | RMIMVCException e) {
					e.printStackTrace();
				}
			} else {
				try {
					int opcion = JOptionPane.showConfirmDialog(null, "Hay un juego guardado, desea continuarlo?",
							"Confirmar", JOptionPane.YES_NO_OPTION);
					if (opcion == 0) {
						jAnt = true;
						juego = juego.recuperar();
						srv.iniciar(juego);
					} else {
						srv.iniciar(juego);
					}
				} catch (RMIMVCException | IOException e) {
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// GUI

		Controlador controlador = new Controlador();
		Controlador controlador2 = new Controlador();
		Gui p1 = new Gui(controlador);
		Gui p2 = new Gui(controlador2);

		Cliente cli = new Cliente("localhost", 9999, "localhost", 8888);
		try {
			cli.iniciar(controlador);
		} catch (RemoteException | RMIMVCException e) {
			e.printStackTrace();
		}

		Cliente cli2 = new Cliente("localhost", 9998, "localhost", 8888);
		try {
			cli2.iniciar(controlador2);
		} catch (RemoteException | RMIMVCException e) {
			e.printStackTrace();
		}

		if (jAnt) {
			p1.juegoGuardado();
			p2.juegoGuardado();
		} else {
			p1.iniciarJuego();
			p2.iniciarJuego();
		}

		// Consola

		/*
		 * Controlador controlador = new Controlador(); Consola p1 = new
		 * Consola(controlador);
		 * 
		 * Cliente cli = new Cliente("localhost", 9999, "localhost", 8888); try {
		 * cli.iniciar(controlador); } catch (RemoteException | RMIMVCException e) {
		 * e.printStackTrace(); } if (jAnt) { p1.juegoGuardado(); } else {
		 * p1.iniciarJuego(); }
		 */
	}

}
