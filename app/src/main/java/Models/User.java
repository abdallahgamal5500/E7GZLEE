package Models;

public class User {
    String full_name, first_name,last_name,email;

    public User() {
    }

    public User(String full_name, String first_name, String last_name, String email) {
        this.full_name = full_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

