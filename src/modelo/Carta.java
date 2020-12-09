package modelo;

import java.io.Serializable;

public class Carta implements ICarta, Serializable {
    private static final long serialVersionUID = 1L;
    private Palo palo;
    private int valor;
    private String figura;
    private boolean selected;

    public Carta(Palo palo, int valor, String figura) {
        this.palo = palo;
        this.valor = valor;
        this.figura = figura;
        selected = false;
    }

    public String getPalo() {
        return palo.name();
    }

    public int getValor() {
        return valor;
    }

    public String getFigura() {
        return figura;
    }

    public String toString() {
        return getFigura() + " de " + getPalo();
    }

    public boolean isOro() {
        boolean isOro = false;
        if (getPalo().equals("ORO")) {
            isOro = true;
        }
        return isOro;
    }

    public boolean is7() {
        boolean is7 = false;
        if (getFigura().equals("7")) {
            is7 = true;
        }
        return is7;
    }

    public boolean is7Oro() {
        boolean oro = false;
        if (isOro() && is7()) {
            oro = true;
        }
        return oro;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }
}
