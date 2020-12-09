package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controlador.Controlador;
import modelo.Carta;
import modelo.CartaGrafica;
import modelo.EstadoJuego;
import modelo.ICarta;
import modelo.IJugador;
import modelo.Jugador;
import net.miginfocom.swing.MigLayout;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.GridLayout;

public class Gui implements IVista, Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel contentPane;
	private JLabel lblMsgEstado;
	private IJugador[] jugadores;
	private static IJugador mesa;
	private Controlador controlador;
	private JTable table;
	private JPanel panelSur;
	private JPanel panelCentral;
	private JButton btnJugar;
	private JButton btnAbandonarJuego;
	private JButton btnTirarCarta;
	private JButton btnArmarJuego;
	private JButton btnAtras;
	private JPanel panelJugando = new JPanel();
	private JPanel panelConfig = new JPanel();
	private JMenuBar menuBar;
	private JMenu mnJuego;
	private JMenuItem mntmSalir;
	private JMenuItem mntmJugar;
	private JMenuItem mntmAyuda;
	private JButton btnAgregarJugador;
	private Jugador jugador;
	private JButton btnListo;
	private ICarta[] cartasMesa;
	private ICarta[] cartasJugador;
	private JTextArea texto = new JTextArea();
	private JMenu mnArchivo;
	private JMenuItem mntmGuardar;
	private static int i = 0;

	public Gui(Controlador controlador) {
		this.controlador = controlador;
		this.controlador.addVista(this);
		frame = new JFrame("Escoba de 15");
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		//frame.setResizable(false);

		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnJuego = new JMenu("Juego");
		menuBar.add(mnJuego);

		mntmJugar = new JMenuItem("Jugar");
		mntmJugar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controlador.iniciarJuego()) {
					controlador.setEstadoJuego(EstadoJuego.JUGANDO);
				}
			}
		});
		mnJuego.add(mntmJugar);

		mntmAyuda = new JMenuItem("Ayuda");
		mntmAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ayuda = controlador.leerAyuda();
				if (ayuda != "") {
					UIManager.put("JOptionPane.messageFont",
							new FontUIResource(new Font("Calibri", Font.PLAIN, Font.PLAIN)));
					JOptionPane.showMessageDialog(null, ayuda, "Ayuda", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnJuego.add(mntmAyuda);

		mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(mnJuego, "Desea salir del juego?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {
					System.exit(0);
				}
			}
		});
		mnJuego.add(mntmSalir);

		mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.setEnabled(false);
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(mnJuego, "Desea guardar el juego?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {
					controlador.guardarJuego();
				}
			}
		});
		mnArchivo.add(mntmGuardar);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);

		JPanel panelNorte = new JPanel();
		contentPane.add(panelNorte, BorderLayout.NORTH);
		panelNorte.setLayout(new MigLayout("", "[150px][grow]", "[]"));

		JLabel lblEstadoActual = new JLabel("Estado Actual");
		panelNorte.add(lblEstadoActual, "cell 0 0");

		lblMsgEstado = new JLabel("Configurando");
		lblMsgEstado.setForeground(Color.RED);
		panelNorte.add(lblMsgEstado, "cell 1 0");

		JPanel tblJugadores = new JPanel();
		contentPane.add(tblJugadores, BorderLayout.EAST);
		tblJugadores.setLayout(new MigLayout("", "[200px:n:200px,grow]", "[grow][fill]"));

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(datosJugador(jugadores));
		table.setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(22);
		table.getColumnModel().getColumn(0).setMinWidth(22);
		table.getColumnModel().getColumn(0).setMaxWidth(22);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(75);
		table.getColumnModel().getColumn(1).setMinWidth(75);
		table.getColumnModel().getColumn(1).setMaxWidth(75);

		JScrollPane scroll = new JScrollPane(table);
		tblJugadores.add(scroll, "cell 0 0,grow");

		JPanel panel = new JPanel();
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		tblJugadores.add(panel, "flowx,cell 0 1");

		btnAgregarJugador = new JButton("Agregar jugador");
		btnAgregarJugador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarJugador();
			}
		});
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		btnAgregarJugador.setPreferredSize(new Dimension(50, 29));
		btnAgregarJugador.setMaximumSize(new Dimension(50, 29));
		panel.add(btnAgregarJugador);

		btnListo = new JButton("Listo");
		btnListo.setEnabled(false);
		btnListo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!jugador.isReady()) {
					controlador.setReady(jugador, true);
					btnListo.setText("Cancelar");
				} else {
					controlador.setReady(jugador, false);
					btnListo.setText("Listo");
				}
			}
		});
		tblJugadores.add(btnListo, "cell 0 1,growx");

		panelCentral = new JPanel();
		panelCentral.setBackground(new Color(0, 100, 0));
		contentPane.add(panelCentral, BorderLayout.CENTER);
		panelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		panelSur = new JPanel();
		panelSur.setBackground(new Color(0, 100, 0));
		panelSur.setLayout(new MigLayout("", "[:95:95]", "[150px:n:150px]"));
		panelSur.setBounds(new Rectangle(0, 0, 0, 80));
		JScrollPane scrollPS = new JScrollPane(panelSur);
		contentPane.add(scrollPS, BorderLayout.SOUTH);

		panelJugando = new JPanel();
		panelConfig = new JPanel();

		panelJugando.setLayout(new MigLayout("", "[]", "[][][]"));
		btnTirarCarta = new JButton("Tirar carta");
		btnTirarCarta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ICarta[] cartas = jugador.getMano();
				for (int i = 0; i < cartas.length; i++) {
					if (cartas[i].isSelected()) {
						controlador.tirarCarta(i);
					}
				}
			}
		});
		panelJugando.add(btnTirarCarta, "cell 0 0,growx");

		btnArmarJuego = new JButton("Armar juego");
		btnArmarJuego.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				armarJuego();
			}
		});
		panelJugando.add(btnArmarJuego, "cell 0 1,growx");

		btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(btnAtras, "Desea salir de la partida?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {
					controlador.setEstadoJuego(EstadoJuego.CONFIGURANDO);
				}
			}
		});
		panelJugando.add(btnAtras, "cell 0 4,growx");

		contentPane.add(panelConfig, BorderLayout.WEST);
		panelConfig.setLayout(new MigLayout("", "[]", "[][][]"));

		btnJugar = new JButton("Jugar");
		btnJugar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controlador.iniciarJuego();
			}

		});
		panelConfig.add(btnJugar, "cell 0 0,growx");

		btnAbandonarJuego = new JButton("Salir");
		btnAbandonarJuego.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(btnAbandonarJuego, "Desea salir del juego?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (opcion == 0) {
					System.exit(0);
				}
			}
		});
		panelConfig.add(btnAbandonarJuego, "cell 0 1,growx");
	}

	public void armarJuego() {
		ArrayList<Integer> cartasJugada = new ArrayList<>();
		cartasJugador = jugador.getMano();
		for (int i = 0; i < cartasJugador.length; i++) {
			if (cartasJugador[i].isSelected()) {
				cartasJugada.add(i);
			}
		}
		for (int i = 0; i < cartasMesa.length; i++) {
			if (cartasMesa[i].isSelected()) {
				cartasJugada.add(i);
			}
		}
		controlador.armarJuego(cartasJugada);
	}

	private void configurarEstado(EstadoJuego estadoJuego) {
		switch (estadoJuego) {
		case CONFIGURANDO:
			btnTirarCarta.setEnabled(false);
			panelJugando.setVisible(false);
			lblMsgEstado.setText("Esperando configuracion");
			panelConfig.setVisible(true);
			contentPane.add(panelConfig, BorderLayout.WEST);
			controlador.reset();
			jugadores = controlador.getJugadores();
			table.setModel(datosJugador(jugadores));
			btnAgregarJugador.setVisible(true);
			panelSur.removeAll();
			panelSur.repaint();
			panelCentral.removeAll();
			panelCentral.repaint();
			mntmJugar.setEnabled(true);
			btnListo.setText("Listo");
			btnListo.setVisible(true);
			btnAgregarJugador.setEnabled(true);
			break;
		case JUGANDO:
			if(jugador.getNombre().equals(jugadores[controlador.getJugadorActual()].getNombre())) {
				btnTirarCarta.setEnabled(true);
			} else {
				btnTirarCarta.setEnabled(false);
			}
			frame.setTitle("Escoba de 15 - " + jugador.getNombre());
			btnListo.setVisible(false);
			mntmJugar.setEnabled(false);
			panelConfig.setVisible(false);
			lblMsgEstado.setText("Turno de: " + jugadores[controlador.getJugadorActual()]);
			panelJugando.setVisible(true);
			contentPane.add(panelJugando, BorderLayout.WEST);
			btnAgregarJugador.setVisible(false);
			break;
		case TERMINO:
			mntmGuardar.setEnabled(false);
			mostrarCartasMesa(null);
			mostrarCartas(null);
			btnTirarCarta.setEnabled(false);
			btnArmarJuego.setEnabled(false);
			lblMsgEstado.setText("Partida terminada");
			break;
		}
	}

	public void iniciarJuego() {
		frame.setVisible(true);
	}

	public void juegoGuardado() {
		jugadores = controlador.getJugadores();
		table.setModel(datosJugador(jugadores));
		jugador = (Jugador) jugadores[i++];
		cartasJugador = jugador.getMano();
		mostrarCartas(cartasJugador);	
		if (!jugador.getNombre().equals(jugadores[controlador.getJugadorActual()].getNombre())) {
			btnTirarCarta.setEnabled(false);
			btnArmarJuego.setEnabled(false);
		} else {
			btnTirarCarta.setEnabled(true);
			btnArmarJuego.setEnabled(true);
		}
		mesa = controlador.getMesa();
		cartasMesa = mesa.getMano();
		mostrarCartasMesa(cartasMesa);
		this.configurarEstado(EstadoJuego.JUGANDO);
		frame.setVisible(true);
	}

	private void agregarJugador() {
		String nombre = JOptionPane.showInputDialog("Ingrese nombre de Jugador", "");
		if (nombre != null) {
			if (!nombre.equals("")) {
				jugador = controlador.agregarJugador(nombre);
				btnListo.setEnabled(true);
				btnAgregarJugador.setEnabled(false);
			}
		}
	}

	private TableModel datosJugador(IJugador[] jugadores) {

		Object[][] datos;
		if (jugadores == null) {
			datos = new Object[][] {};
		} else {
			datos = new Object[jugadores.length][4];
			int i = 0;
			for (IJugador j : jugadores) {
				datos[i][0] = i + 1;
				datos[i][1] = j.getNombre();
				datos[i++][2] = j.getEscoba();
			}
		}

		@SuppressWarnings("serial")
		DefaultTableModel dtm = new DefaultTableModel(datos, new String[] { "#", "Jugador", "Escobas" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { Integer.class, String.class, Integer.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		};

		return dtm;
	}

	private void mostrarCartas(ICarta[] cartas) {
		String cols = "";
		panelSur.removeAll();
		panelSur.repaint();
		if (cartas != null) {
			for (int i = 0; i < cartas.length; i++) {
				cols += "[95px:n:95px]";
			}
			panelSur.setLayout(new MigLayout("", cols, "[150px:n:150px]"));
			for (int i = 0; i < cartas.length; i++) {
				final int index = i;
				CartaGrafica cg = new CartaGrafica(cartas[i]);
				if (jugador.getNombre().equals(jugadores[controlador.getJugadorActual()].getNombre())) {
					cg.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							MouseEvent ev = e;
							if (ev.getButton() == MouseEvent.BUTTON1) {
								Carta cartaAux = (Carta) cartas[index];
								jugador.setSelected(cartaAux);
							} else if (ev.getButton() == MouseEvent.BUTTON3) {
								controlador.tirarCarta(index);
							}
						}
					});
				}
				cg.setImagen("/img/cartas/" + cartas[i].getValor() + cartas[i].getPalo() + ".png");
				cg.setAutoscrolls(true);
				cg.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY,
						Color.DARK_GRAY));
				cg.setBounds(new Rectangle(0, 0, 14, 60));
				cg.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
				panelSur.add(cg, "cell " + i + " 0 ,grow");

			}
		}
	}

	private void mostrarCartasMesa(ICarta[] cartas) {
		panelCentral.removeAll();
		panelCentral.repaint();
		if (cartas != null) {
			for (int i = 0; i < cartas.length; i++) {
				final int index = i;
				CartaGrafica cg = new CartaGrafica(cartas[i]);
				cg.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						final MouseEvent ev = e;
						if (ev.getButton() == MouseEvent.BUTTON1) {
							Carta cartaAux = (Carta) cartas[index];
							if (!cartaAux.isSelected()) {
								cartaAux.setSelected(true);
							} else {
								cartaAux.setSelected(false);
							}
						}
					}
				});
				cg.setImagen("/img/cartas/" + cartas[i].getValor() + cartas[i].getPalo() + ".png");
				cg.setPreferredSize(new Dimension(95, 150));
				cg.setBounds(new Rectangle(0, 0, 15, 60));
				cg.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY,
						Color.DARK_GRAY));
				cg.setAutoscrolls(true);
				panelCentral.add(cg);
			}
		}
	}

	@Override
	public void cambioEnJugador() {
		jugadores = controlador.getJugadores();
		table.setModel(datosJugador(jugadores));
	}

	@Override
	public void cambioDeTurno() {
		if (!jugador.getNombre().equals(jugadores[controlador.getJugadorActual()].getNombre())) {
			btnTirarCarta.setEnabled(false);
			btnArmarJuego.setEnabled(false);
			mntmGuardar.setEnabled(false);
		} else {
			mntmGuardar.setEnabled(true);
			btnTirarCarta.setEnabled(true);
			btnArmarJuego.setEnabled(true);
		}
		jugador = controlador.getJugador(jugador);
		cartasJugador = jugador.getMano();
		mostrarCartas(cartasJugador);
		lblMsgEstado.setText("Turno de: " + jugadores[controlador.getJugadorActual()]);
	}

	@Override
	public void hayCambioDeEstado(EstadoJuego estadoJuego) {
		configurarEstado(estadoJuego);

	}

	@Override
	public void hayError(String string) {
		//JOptionPane.showMessageDialog(frame, string);
		lblMsgEstado.setText(string);
	}

	@Override
	public void cartasJugador() {
		cartasMesa = controlador.getCartasMesa();
		mostrarCartasMesa(cartasMesa);
		jugador = controlador.getJugador(jugador);
		cartasJugador = jugador.getMano();
		mostrarCartas(cartasJugador);
	}

	@Override
	public void cartasMesa() {
		mesa = controlador.getMesa();
		cartasMesa = mesa.getMano();
		mostrarCartasMesa(cartasMesa);
	}

	@Override
	public void hayEscobaReal() {
		JOptionPane.showMessageDialog(null,
				"Escoba real! " + jugadores[controlador.getJugadorActual()] + " ha ganado un punto");
		cartasMesa = controlador.getCartasMesa();
		mostrarCartasMesa(cartasMesa);
	}

	@Override
	public void hayQuince() {
		cartasMesa = controlador.getCartasMesa();
		mostrarCartasMesa(cartasMesa);
	}

	@Override
	public void cartasTirada() {
		cartasMesa = controlador.getCartasMesa();
		mostrarCartasMesa(cartasMesa);
	}

	@Override
	public void hayGanador() {
		mntmJugar.setEnabled(false);
		mostrarCartasMesa(null);
		mostrarCartas(null);
		mntmGuardar.setEnabled(false);
		btnTirarCarta.setEnabled(false);
		btnArmarJuego.setEnabled(false);
		jugadores = controlador.getJugadores();
		table.setModel(datosJugador(jugadores));
		lblMsgEstado.setText("Juego finalizado");
		IJugador ganador = controlador.getGanador();
		texto.setText("Ganador: " + ganador.getNombre() + "\n");
		String s = "Puntaje: \n";
		for (IJugador j : jugadores) {
			s += j.getNombre() + "\nPuntos: " + j.getPuntos() + "\nEscobas: " + j.getEscoba() + "\nSietes: "
					+ j.getSietes() + "\n7 oro: " + j.getOro7() + "\nCantiad de oro: " + j.getOros()
					+ "\nCantidad de cartas: " + j.cantidadCartas() + "\n";
		}
		texto.append(s);
		panelCentral.add(texto);
	}

	@Override
	public void escoba() {
		jugadores = controlador.getJugadores();
		table.setModel(datosJugador(jugadores));
	}

	@Override
	public void jugadorListo() {
		jugador = controlador.getJugador(jugador);
	}

	@Override
	public void hayGuardado() {
		lblMsgEstado.setText("Juego Guardado");
	}

}
