package cl.duoc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "oportunidades")
public class OportunidadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private String descripcion;
    private String fuente;
    private String prioridad;
    private String potencial;
    private String estado;
    private String observaciones;
    private String fotoPath;
    private Double latitud;
    private Double longitud;
    private Long creadoEn;
    private Long actualizadoEn;

    public OportunidadModel() {
    }

    public OportunidadModel(Long id, String nombre, String tipo, String descripcion, String fuente,
                            String prioridad, String potencial, String estado, String observaciones,
                            String fotoPath, Double latitud, Double longitud, Long creadoEn, Long actualizadoEn) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.prioridad = prioridad;
        this.potencial = potencial;
        this.estado = estado;
        this.observaciones = observaciones;
        this.fotoPath = fotoPath;
        this.latitud = latitud;
        this.longitud = longitud;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getPotencial() { return potencial; }
    public void setPotencial(String potencial) { this.potencial = potencial; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Long getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Long creadoEn) { this.creadoEn = creadoEn; }

    public Long getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Long actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
