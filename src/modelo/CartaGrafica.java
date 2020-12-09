package modelo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CartaGrafica extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image imagen;
	private int numero;
	private boolean proporcional = true;
	private JLabel texto = new JLabel("");
	private ICarta carta;

	public CartaGrafica(ICarta carta) {
		super();
		this.setLayout(null);
		texto.setAlignmentX(CENTER_ALIGNMENT);
		texto.setAlignmentY(CENTER_ALIGNMENT);
		this.add(texto);
		this.carta = carta;
	}

	public CartaGrafica(Image imagenInicial) {
		if (imagenInicial != null) {
			imagen = imagenInicial;
		}
	}

	public int getNumero() {
		return numero;
	}

	public void setCarta(ICarta carta) {
		if (carta == null) {
			this.setImagen("/img/cartas/Carta.png");
		} else {
			try {
				this.setImagen("/img/cartas/" + carta.getFigura() + carta.getPalo() + ".png");
			} catch (Exception e) {
				this.setImagen("/img/cartas/Carta.png");
			}
		}
	}

	public void setNumero(int numero) {
		this.numero = numero;
		texto.setText(" " + numero + " ");
	}

	public void setTexto(String texto) {
		this.texto.setText(texto);
	}

	public String getTexto() {
		return this.texto.getText();
	}

	public void setProporcional(boolean valor) {
		proporcional = valor;
	}

	public boolean getProporcional() {
		return proporcional;
	}

	public void setImagen(String nombreImagen) {
		if (nombreImagen != null) {
			imagen = new ImageIcon(getClass().getResource(nombreImagen)).getImage();
		} else {
			imagen = null;
		}

		repaint();
	}

	public void setImagen(Image nuevaImagen) {
		imagen = nuevaImagen;

		repaint();
	}

	@Override
	public void paint(Graphics g) {
		if (imagen != null) {

			int largo = imagen.getHeight(null);
			int ancho = getHeight() * imagen.getWidth(null) / largo;
			int vpLargo = getHeight();
			int vpAncho = ancho;

			if (ancho >= getWidth()) {
				ancho = imagen.getWidth(null);
				vpLargo = getWidth() * imagen.getHeight(null) / ancho;
				vpAncho = getWidth();
			}

			g.drawImage(imagen, 0, 0, vpAncho, vpLargo, this);

			texto.setFont(new java.awt.Font("Tahoma", 0, vpLargo * 20 / 100));
			texto.setHorizontalAlignment(SwingConstants.CENTER);
			texto.setBounds(0, 0, vpAncho, vpLargo);
			setOpaque(false);
		} else {
			setOpaque(true);
		}
		if (carta.isSelected()) {
			setBorder(BorderFactory.createLineBorder(Color.RED, 5));
		} else {
			setBorder(null);
		}
		super.paint(g);
	}

}
