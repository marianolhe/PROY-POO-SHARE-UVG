class PersonaPlantilla{
    //Datos de todos los usuarios de la pagina
    protected String nombre;
    protected String apellido;
    protected String correo;
    protected String contrasena;
    protected String carrera;

    public PersonaPlantilla(String nombre, String apellido, String correo, String contrasena, String carrera) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
        this.carrera = carrera;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    // Método para convertir los datos a una línea de CSV
    public String toCSV() {
        return nombre + "," + apellido + "," + correo + "," + contrasena + "," + carrera;
    }

}