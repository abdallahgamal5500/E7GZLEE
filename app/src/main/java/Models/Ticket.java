package Models;

public class Ticket {
    String from,to,train_class,date,day_name,price,train_number,from_time,to_time;
    int seats_number;

    public Ticket() {
    }

    public Ticket(String from, String to, String train_class, String date, String day_name, String price, String train_number, String from_time, String to_time, int seats_number) {
        this.from = from;
        this.to = to;
        this.train_class = train_class;
        this.date = date;
        this.day_name = day_name;
        this.price = price;
        this.train_number = train_number;
        this.from_time = from_time;
        this.to_time = to_time;
        this.seats_number = seats_number;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTrain_class() {
        return train_class;
    }

    public void setTrain_class(String train_class) {
        this.train_class = train_class;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public int getSeats_number() {
        return seats_number;
    }

    public void setSeats_number(int seats_number) {
        this.seats_number = seats_number;
    }
}
