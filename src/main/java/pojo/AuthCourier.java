package pojo;

public class AuthCourier {

    private String login;
    private String password;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AuthCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AuthCourier() {
    }

    @Override
    public String toString() {
        return "Courier{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
