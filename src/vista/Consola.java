package vista;

import java.util.ArrayList;

import controlador.Controlador;
import modelo.EstadoJuego;
import modelo.ICarta;
import modelo.IJugador;
import modelo.Jugador;

public class Consola implements IVista {

	private Controlador controlador;
	private IJugador[] jugadores;
	private ICarta[] cartasJugador;
	private ICarta[] cartasMesa;

	public Consola(Controlador controlador) {
		this.controlador = controlador;
		this.controlador.addVista(this);
	}

	public void iniciarJuego() {
		controlador.setEstadoJuego(EstadoJuego.CONFIGURANDO);
		menuConfiguracion();
	}

	private void menuConfiguracion() {
		char opcion = 0;
		while (opcion != 'S' && controlador.getEstadoJuego() != EstadoJuego.TERMINO) {
			opcion = tomarOpcion();
			switch (opcion) {
			case '1':
				agregarJugador();
				EntradaConsola.pausarConsola();
				configurarEstado(controlador.getEstadoJuego());
				break;
			case '2':
				mostrarJugadores();
				EntradaConsola.pausarConsola();
				configurarEstado(EstadoJuego.CONFIGURANDO);
				break;
			case '9':
				if (controlador.iniciarJuego()) {
					menuJuego();
				}
				opcion = 'a';
				break;
			case 'S':
				controlador.setEstadoJuego(EstadoJuego.TERMINO);
				System.exit(0);
				break;
			}
		}
	}
	
	private void menuConfGuardado() {
		char opcion = 0;
		while (opcion != 'S' && controlador.getEstadoJuego() != EstadoJuego.TERMINO) {
			opcion = tomarOpcion();
			switch (opcion) {
			case '1':
				agregarJugador();
				EntradaConsola.pausarConsola();
				configurarEstado(controlador.getEstadoJuego());
				break;
			case '2':
				mostrarJugadores();
				EntradaConsola.pausarConsola();
				configurarEstado(EstadoJuego.CONFIGURANDO);
				break;
			case '9':
				controlador.setEstadoJuego(EstadoJuego.JUGANDO);
				menuJuego();
				opcion = 'a';
				break;
			case 'S':
				controlador.setEstadoJuego(EstadoJuego.TERMINO);
				System.exit(0);
				break;
			}
		}
	}

	private void menuJuego() {
		char opcion = 0;
		separacion();
		while (opcion != 'S' && controlador.getEstadoJuego() != EstadoJuego.CONFIGURANDO) {
			opcion = tomarOpcion();
			switch (opcion) {
			case '1':
				mostrarCartasJugador();
				EntradaConsola.pausarConsola();
				configurarEstado(controlador.getEstadoJuego());
				break;
			case '2':
				mostrarCartasMesa();
				EntradaConsola.pausarConsola();
				configurarEstado(controlador.getEstadoJuego());
				break;
			case '3':
				tirarCarta();
				break;
			case '4':
				armarJuego();
				break;
			case '5':
				guardarJuego();
				break;
			case 'S':
				controlador.setEstadoJuego(EstadoJuego.CONFIGURANDO);
				break;
			}
		}
	}

	private void guardarJuego() {
		controlador.guardarJuego();
	}

	private void mostrarMenuJuego() {
		jugadores = controlador.getJugadores();
		System.out.println("##################################");
		System.out.println("##    Turno de: " + turnoJugador() + "    ##");
		System.out.println("##################################");
		System.out.println("#######      Menu juego    #######");
		System.out.println("## 1- Mostrar cartas jugador    ##");
		System.out.println("## 2- Mostrar cartas mesa       ##");
		System.out.println("## 3- Tirar carta               ##");
		System.out.println("## 4- Armar juego               ##");
		System.out.println("## 5- Guardar juego             ##");		
		System.out.println("##################################");
		System.out.println("## S- Salir                     ##");
		System.out.println("##################################");
	}

	private void mostrarMenuConfiguracion() {
		System.out.println("##################################");
		System.out.println("######  Menu Configuracion #######");
		System.out.println("## 1- Agregar jugador           ##");
		System.out.println("## 2- Mostrar jugadores         ##");
		System.out.println("## 9- Jugar                     ##");
		System.out.println("## S- Salir                     ##");
		System.out.println("##################################");
	}

	private void armarJuego() {
		ArrayList<Integer> indices = new ArrayList<>();
		System.out.println("Seleccione una carta de su mano");
		mostrarCartasJugador();
		System.out.print("Eleccion: ");
		char index = EntradaConsola.tomarChar();
		indices.add(Character.getNumericValue(index));
		separacion();
		System.out.println("Seleccione una o mas cartas de la mesa, ingrese S para finalizar");
		mostrarCartasMesa();
		while (index != 'S') {
			System.out.print("Eleccion: ");
			index = EntradaConsola.tomarUpperChar();
			int indexAux = Character.getNumericValue(index);
			if (index != 'S') {
				indices.add(indexAux);
			}
		}
		controlador.armarJuego(indices);
	}

	private void tirarCarta() {
		mostrarCartasJugador();
		System.out.println("Ingrese el numero de carta que desea tirar");
		int index = EntradaConsola.tomarEntero();
		controlador.tirarCarta(index);
	}

	private void separacion() {
		System.out.println("----------------------------------------");
	}

	private void mostrarCartasMesa() {
		int i = 0;
		System.out.println("Cartas en mesa: ");
		for (ICarta iCarta : cartasMesa) {
			System.out.println(i++ + " - " + iCarta.toString());
		}
		separacion();
	}

	private void mostrarCartasJugador() {
		int i = 0;
		separacion();
		System.out.println("Cartas en mano: ");
		for (ICarta iCarta : cartasJugador) {
			System.out.println(i++ + " - " + iCarta.toString());
		}
		separacion();
	}

	private String turnoJugador() {
		jugadores = controlador.getJugadores();
		String s = jugadores[controlador.getJugadorActual()].getNombre();
		return s;
	}

	private char tomarOpcion() {
		return EntradaConsola.tomarUpperChar();
	}

	private void agregarJugador() {
		separacion();
		System.out.println("Ingrese nombre de jugador");
		String nombre = EntradaConsola.tomarString();
		while (nombre.length() < 1) {
			nombre = EntradaConsola.tomarString();
		}
		Jugador j = new Jugador(nombre);
		controlador.agregarJugador(nombre);
		controlador.setReady(j, true);
	}

	private void mostrarJugadores() {
		int i = 1;
		separacion();
		System.out.println("Jugadores en esta partida: ");
		if (jugadores.length != 0) {
			for (IJugador iJugador : jugadores) {
				System.out.println(i++ + " - " + iJugador.getNombre());
			}
		}
		separacion();
	}

	private void configurarEstado(EstadoJuego estadoJuego) {
		switch (estadoJuego) {
		case CONFIGURANDO:
			mostrarMenuConfiguracion();
			break;
		case JUGANDO:
			mostrarMenuJuego();
			break;
		case TERMINO:
			System.out.println("Partida terminada");
			break;
		}
	}

	public void juegoGuardado() {
		jugadores = controlador.getJugadores();
		cartasJugador = controlador.getManoJugador();
		cartasMesa = controlador.getCartasMesa();
		mostrarMenuConfiguracion();
		menuConfGuardado();
	}

	@Override
	public void cambioEnJugador() {
		jugadores = controlador.getJugadores();
		System.out.println("Jugador agregado");
		separacion();
	}

	@Override
	public void cambioDeTurno() {
		cartasJugador = controlador.getManoJugador();
		if (controlador.getEstadoJuego() != EstadoJuego.CONFIGURANDO) {
			controlador.setEstadoJuego(controlador.getEstadoJuego());
		}
	}

	@Override
	public void hayCambioDeEstado(EstadoJuego estado) {
		switch (estado) {
		case JUGANDO:
			jugadores = controlador.getJugadores();
			mostrarMenuJuego();
			menuJuego();
			break;
		case CONFIGURANDO:
			controlador.reset();
			jugadores = controlador.getJugadores();
			mostrarMenuConfiguracion();
			break;
		case TERMINO:
			System.out.println("Juego finalizado ");
			break;
		}

	}

	@Override
	public void hayError(String error) {
		System.out.println(error);
		separacion();
		controlador.setEstadoJuego(controlador.getEstadoJuego());

	}

	@Override
	public void cartasJugador() {
		cartasJugador = controlador.getManoJugador();

	}

	@Override
	public void cartasTirada() {
		cartasJugador = controlador.getManoJugador();
		cartasMesa = controlador.getCartasMesa();
		System.out.println("Carta tirada");
		System.out.println(cartasMesa[cartasMesa.length - 1].toString());
	}

	@Override
	public void hayEscobaReal() {
		System.out.println("Escoba real!!");
		System.out.println(jugadores[controlador.getJugadorActual()].getNombre() + " ha ganado un punto");
	}

	@Override
	public void hayQuince() {
		System.out.println("Bien hecho has sumado 15");
		separacion();
	}

	@Override
	public void cartasMesa() {
		cartasMesa = controlador.getCartasMesa();
	}

	@Override
	public void hayGanador() {
		System.out.println("Ganador " + controlador.getGanador().getNombre() + " ha hecho "
				+ controlador.getGanador().getPuntos() + " puntos");
		EntradaConsola.pausarConsola();
		configurarEstado(EstadoJuego.TERMINO);
	}

	@Override
	public void escoba() {
		System.out.println(jugadores[controlador.getJugadorActual()].getNombre() + " hizo escoba");
	}

	@Override
	public void jugadorListo() {
	}

	@Override
	public void hayGuardado() {
		System.out.println("Juego guardado correctamente");
		EntradaConsola.pausarConsola();
		configurarEstado(EstadoJuego.JUGANDO);
	}

}
