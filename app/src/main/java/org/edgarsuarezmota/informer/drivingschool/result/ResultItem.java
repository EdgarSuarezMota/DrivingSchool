package org.edgarsuarezmota.informer.drivingschool.result;

public class ResultItem {
    private String texto;
    private int numero;
    private String etiqueta;

    public ResultItem() {
        // Constructor vacío requerido por Firebase
    }

    public ResultItem(String texto, int numero, String etiqueta) {
        this.texto = texto;
        this.numero = numero;
        this.etiqueta = etiqueta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }
}
