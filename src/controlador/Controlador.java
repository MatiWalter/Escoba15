package controlador;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ar.edu.unlu.rmimvc.cliente.IControladorRemoto;
import ar.edu.unlu.rmimvc.observer.IObservableRemoto;
import modelo.Cambios;
import modelo.EstadoJuego;
import modelo.ICarta;
import modelo.IJugador;
import modelo.IObservable;
import modelo.Jugador;
import vista.IVista;

public class Controlador implements IControladorRemoto, Serializable {

    // Attributes

    /**
     * 
     */
    private static final long serialVersionUID = 565498175387398928L;
    private IObservable juego;
    private IVista vista;

    // Methods

    public void addVista(IVista vista) {
        this.vista = vista;
    }

    public boolean iniciarJuego() {
        try {
            return juego.iniciarJuego();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Jugador agregarJugador(String jugador) {
        Jugador j = null;
        try {
            j = juego.agregarJugador(jugador);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return j;
    }

    public Jugador getJugador(Jugador jugador) {
        Jugador j = null;
        try {
            j = juego.getJugador(jugador);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j;
    }

    public IJugador[] getJugadores() {
        IJugador[] jugador = null;
        try {
            jugador = juego.getJugadores();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return jugador;
    }

    public ICarta[] getManoJugador() {
        ICarta[] carta = null;
        try {
            carta = juego.getManoJugador();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return carta;
    }

    public ICarta[] getCartasJugador(IJugador jugador) {
        ICarta[] carta = null;
        try {
            carta = juego.getCartasJugador(jugador);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return carta;
    }

    public EstadoJuego getEstadoJuego() {
        EstadoJuego estado = null;
        try {
            estado = juego.getEstadoJuego();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return estado;
    }

    public int getCantidadJugadores() {
        int cantidad = 0;
        try {
            cantidad = juego.getCantidadJugadores();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return cantidad;
    }

    public void setEstadoJuego(EstadoJuego estadoJuego) {
        try {
            juego.setEstadoJuego(estadoJuego);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getJugadorActual() {
        int jugActual = 0;
        try {
            jugActual = juego.getJugadorActual();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return jugActual;
    }

    public void reset() {
        try {
            juego.reset();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ICarta[] getCartasMesa() {
        ICarta[] carta = null;
        try {
            carta = juego.getCartasMesa();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return carta;
    }

    public void tirarCarta(int index) {
        try {
            juego.tirarCarta(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void armarJuego(ArrayList<Integer> indices) {
        try {
            juego.armarJuego(indices);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IJugador getGanador() {
        IJugador j = null;
        try {
            j = juego.getGanador();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return j;
    }

    public String leerAyuda() {
        String ayuda = "";
        try {
            ayuda = juego.leerAyuda();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ayuda;
    }

    public void setReady(Jugador jugador, boolean listo) {
        try {
            juego.setReady(jugador, listo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public IJugador getMesa() {
        IJugador mesa = null;
        try {
            mesa = juego.getMesa();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return mesa;
    }

    public boolean getJuegoAnterior() {
        boolean jAnterior = false;
        try {
            jAnterior = juego.getJuegoAnterior();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return jAnterior;
    }

    public void cargarJuego() {
        try {
            juego = juego.recuperar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarJuego() {
        try {
            juego.guardar();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actualizar(IObservableRemoto arg0, Object arg1) throws RemoteException {
        switch ((Cambios) arg1) {
            case LISTA_JUGADORES:
                vista.cambioEnJugador();
                break;
            case ERROR:
                vista.hayError(juego.getMsgError());
                break;
            case CARTAS_JUGADOR:
                vista.cartasJugador();
                break;
            case CARTAS_MESA:
                vista.cartasMesa();
                break;
            case CARTA_TIRADA:
                vista.cartasTirada();
                break;
            case TURNO:
                vista.cambioDeTurno();
                break;
            case ESTADO_JUEGO:
                vista.hayCambioDeEstado(juego.getEstadoJuego());
                break;
            case ESCOBA_REAL:
                vista.hayEscobaReal();
                break;
            case QUINCE:
                vista.hayQuince();
                break;
            case GANADOR:
                vista.hayGanador();
                break;
            case ESCOBA:
                vista.escoba();
                break;
            case LISTO:
                vista.jugadorListo();
                break;
            case GUARDAR:
                vista.hayGuardado();
                break;
        }
    }

    @Override
    public <T extends IObservableRemoto> void setModeloRemoto(T arg0) throws RemoteException {
        juego = (IObservable) arg0;
    }
}
