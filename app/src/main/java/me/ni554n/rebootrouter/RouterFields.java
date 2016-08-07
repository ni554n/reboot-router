package me.ni554n.rebootrouter;

public class RouterFields {

    private String gateway;
    private String username;
    private String password;

    public RouterFields(String gateway, String username, String password) {
        this.gateway = gateway;
        this.username = username;
        this.password = password;
    }

    public String getGateway() {
        return gateway;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return gateway + "/" + username + ":" + password;
    }
}
