package Models;

public class Spinner_Items {
    String item_name;
    int id;

    public Spinner_Items() {
    }

    public Spinner_Items(String item_name, int id) {
        this.item_name = item_name;
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

