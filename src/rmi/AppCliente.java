package rmi;

import java.rmi.RemoteException;
import javax.swing.JOptionPane;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.cliente.Cliente;
import controlador.Controlador;
import vista.Consola;
import vista.Gui;

@SuppressWarnings("unused")
public class AppCliente {

	public static void main(String[] args) {

		String port = (String) JOptionPane.showInputDialog(null,
				"Seleccione el puerto en el que escuchará peticiones el cliente", "Puerto del cliente",
				JOptionPane.QUESTION_MESSAGE, null, null, 9999);
		String portServidor = (String) JOptionPane.showInputDialog(null,
				"Seleccione el puerto en el que corre el servidor", "Puerto del servidor", JOptionPane.QUESTION_MESSAGE,
				null, null, 8888);
		Controlador controlador = new Controlador();
		Gui vista = new Gui(controlador);
		// Consola vista = new Consola(controlador);
		Cliente c = new Cliente("localhost", Integer.parseInt(port), "localhost", Integer.parseInt(portServidor));

		try {
			c.iniciar(controlador);
		} catch (RemoteException | RMIMVCException e) {
			e.printStackTrace();
		}
		vista.iniciarJuego();

	}

}
