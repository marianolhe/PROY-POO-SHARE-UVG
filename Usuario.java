public class Usuario {
    private String user;
    private  String password;

    public Usuario(String user, String  password) {
        this.user = user;
        this.password = password;
    }

    public boolean validarCredenciales(String  user, String password) {
        return this.user.equals(user) && this.password.equals(password);
    }

}
