package net.omnidf.omnidf;

public class Indication {
    private String trayectos;
    private int tiempo;
    private int tipoTransporte;
    private int precio;
    private String indicaciones;

    public Indication(String trayectos, int tiempo, int tipoTransporte, int precio, String indicaciones) {
        this.trayectos = trayectos;
        this.tiempo = tiempo;
        this.tipoTransporte = tipoTransporte;
        this.precio = precio;
        this.indicaciones = indicaciones;
    }

    public String getTrayectos() {
        return trayectos;
    }

    public void setTrayectos(String trayectos) {
        this.trayectos = trayectos;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(int tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
}
