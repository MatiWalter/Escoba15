package rmi;

import java.rmi.RemoteException;
import javax.swing.JOptionPane;

import ar.edu.unlu.rmimvc.RMIMVCException;
import ar.edu.unlu.rmimvc.servidor.Servidor;
import modelo.Juego;

public class AppServidor {

	public static void main(String[] args) {
		System.out.println("Esperando clientes...");
		String port = (String) JOptionPane.showInputDialog(null,
				"Seleccione el puerto en el que escuchará peticiones el servidor", "Puerto del servidor",
				JOptionPane.QUESTION_MESSAGE, null, null, 8888);
		Juego juego = new Juego();
		Servidor servidor = new Servidor("localhost", Integer.parseInt(port));
		try {
			servidor.iniciar(juego);
		} catch (RemoteException | RMIMVCException e) {
			e.printStackTrace();
		}
	}

}
