package Fragments;

import android.app.DatePickerDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e7gzle.Home;
import com.example.e7gzle.R;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Calendar;

import Models.Ticket;

import static androidx.core.content.ContextCompat.getSystemService;

public class FragmentSearch extends Fragment {

    private MaterialSpinner spinner_from, spinner_to, spinner_class;
    private FirebaseDatabase database;
    private DatabaseReference myRef1;
    private TextView textView;
    Button btn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int calender_day, calender_month, calender_year;
    private String from_value,to_value,class_value,date_value,day_name_value,ticket_price,from_time,to_time;
    public static String firebase_direction,firebase_class_days;
    private int seats_number;
    public static ArrayList <Ticket> arraylist;
    private Ticket ticket;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search,container,false);

        spinner_from = view.findViewById(R.id.search_from_spinner);
        spinner_to = view.findViewById(R.id.search_to_spinner);
        spinner_class = view.findViewById(R.id.search_class_spinner);
        textView = view.findViewById(R.id.search_date_tv);
        btn = view.findViewById(R.id.search_btn);

        spinner_from.setItems("Alexandria","Aswan","Asyut","Banha","Bani Seuf","Cairo","Damanhour","Domyat","Giza","Ismailia","Luxor","Mansora","Menia","Port Said","Qena","Sohag","Tanta");
        spinner_to.setItems("Alexandria","Aswan","Asyut","Banha","Bani Seuf","Cairo","Damanhour","Domyat","Giza","Ismailia","Luxor","Mansora","Menia","Port Said","Qena","Sohag","Tanta");
        spinner_class.setItems("A","B","C");

        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference("Ways");

        arraylist = new ArrayList<Ticket>();

        spinner_from.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                from_value = spinner_from.getText().toString();
            }
        });

        spinner_to.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                to_value = spinner_to.getText().toString();
            }
        });

        spinner_class.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                class_value = spinner_class.getText().toString();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!validationFrom()) {
                } else if (!validationTo()) {
                } else if (!validationClass()) {
                } else if (!validationDate()) {
                } else {
                    myRef1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.child("Cairo_Alex").child("Stations").getChildren()) {
                                String from = snapshot1.getValue().toString();
                                if (from.equals(from_value)) {
                                    for (DataSnapshot snapshot2 : dataSnapshot.child("Cairo_Alex").child("Stations").getChildren()) {
                                        String to = snapshot2.getValue().toString();
                                        if (to.equals(to_value)) {
                                            for (DataSnapshot snapshot3 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").getChildren()) {
                                                String class_name = snapshot3.getValue().toString();
                                                if (class_name.equals(class_value)) {
                                                    if (class_name.equals("A")) {
                                                        for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("A_days").getChildren()) {
                                                            String day = snapshot4.getValue().toString();
                                                            if (day.equals(day_name_value)) {
                                                                ticket_price = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("A_days").child("Price").child("Ticket_price").getValue().toString();
                                                                for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").getChildren()) {
                                                                    String train_number = snapshot5.getKey();
                                                                    String seats = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                    seats_number = Integer.parseInt(seats);
                                                                    if (seats_number > 0) {
                                                                        from_time = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                        to_time = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                        float f_time = Float.parseFloat(from_time);
                                                                        float t_time = Float.parseFloat(to_time);
                                                                        if (f_time < t_time) {
                                                                            ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                            arraylist.add(ticket);
                                                                            firebase_direction = "Cairo_Alex";
                                                                            firebase_class_days = "A_days";
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else if (class_name.equals("B")) {
                                                        for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("B_days").getChildren()) {
                                                            String day = snapshot4.getValue().toString();
                                                            if (day.equals(day_name_value)) {
                                                                ticket_price = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("B_days").child("Price").child("Ticket_price").getValue().toString();
                                                                for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").getChildren()) {
                                                                    String train_number = snapshot5.getKey();
                                                                    String seats = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                    seats_number = Integer.parseInt(seats);
                                                                    if (seats_number > 0) {
                                                                        from_time = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                        to_time = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                        float f_time = Float.parseFloat(from_time);
                                                                        float t_time = Float.parseFloat(to_time);
                                                                        if (f_time < t_time) {
                                                                            ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                            arraylist.add(ticket);
                                                                            firebase_direction = "Cairo_Alex";
                                                                            firebase_class_days = "B_days";
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else if (class_name.equals("C")) {
                                                        for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("C_days").getChildren()) {
                                                            String day = snapshot4.getValue().toString();
                                                            if (day.equals(day_name_value)) {
                                                                ticket_price = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("C_days").child("Price").child("Ticket_price").getValue().toString();
                                                                for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").getChildren()) {
                                                                    String train_number = snapshot5.getKey();
                                                                    String seats = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                    seats_number = Integer.parseInt(seats);
                                                                    if (seats_number > 0) {
                                                                        from_time = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                        to_time = dataSnapshot.child("Cairo_Alex").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                        float f_time = Float.parseFloat(from_time);
                                                                        float t_time = Float.parseFloat(to_time);
                                                                        if (f_time < t_time) {
                                                                            ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                            arraylist.add(ticket);
                                                                            firebase_direction = "Cairo_Alex";
                                                                            firebase_class_days = "C_days";
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (arraylist.size() == 0) {
                                for (DataSnapshot snapshot1 : dataSnapshot.child("Cairo_Ismailia").child("Stations").getChildren()) {
                                    String from = snapshot1.getValue().toString();
                                    if (from.equals(from_value)) {
                                        for (DataSnapshot snapshot2 : dataSnapshot.child("Cairo_Ismailia").child("Stations").getChildren()) {
                                            String to = snapshot2.getValue().toString();
                                            if (to.equals(to_value)) {
                                                for (DataSnapshot snapshot3 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").getChildren()) {
                                                    String class_name = snapshot3.getValue().toString();
                                                    if (class_name.equals(class_value)) {
                                                        if (class_name.equals("A")) {
                                                            for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("A_days").getChildren()) {
                                                                String day = snapshot4.getValue().toString();
                                                                if (day.equals(day_name_value)) {
                                                                    ticket_price = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("A_days").child("Price").child("Ticket_price").getValue().toString();
                                                                    for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").getChildren()) {
                                                                        String train_number = snapshot5.getKey();
                                                                        String seats = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                        seats_number = Integer.parseInt(seats);
                                                                        if (seats_number > 0) {
                                                                            from_time = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                            to_time = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                            float f_time = Float.parseFloat(from_time);
                                                                            float t_time = Float.parseFloat(to_time);
                                                                            if (f_time < t_time) {
                                                                                ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                                arraylist.add(ticket);
                                                                                firebase_direction = "Cairo_Ismailia";
                                                                                firebase_class_days = "A_days";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else if (class_name.equals("B")) {
                                                            for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("B_days").getChildren()) {
                                                                String day = snapshot4.getValue().toString();
                                                                if (day.equals(day_name_value)) {
                                                                    ticket_price = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("B_days").child("Price").child("Ticket_price").getValue().toString();
                                                                    for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").getChildren()) {
                                                                        String train_number = snapshot5.getKey();
                                                                        String seats = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                        seats_number = Integer.parseInt(seats);
                                                                        if (seats_number > 0) {
                                                                            from_time = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                            to_time = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                            float f_time = Float.parseFloat(from_time);
                                                                            float t_time = Float.parseFloat(to_time);
                                                                            if (f_time < t_time) {
                                                                                ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                                arraylist.add(ticket);
                                                                                firebase_direction = "Cairo_Ismailia";
                                                                                firebase_class_days = "B_days";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else if (class_name.equals("C")) {
                                                            for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("C_days").getChildren()) {
                                                                String day = snapshot4.getValue().toString();
                                                                if (day.equals(day_name_value)) {
                                                                    ticket_price = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("C_days").child("Price").child("Ticket_price").getValue().toString();
                                                                    for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").getChildren()) {
                                                                        String train_number = snapshot5.getKey();
                                                                        String seats = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                        seats_number = Integer.parseInt(seats);
                                                                        if (seats_number > 0) {
                                                                            from_time = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                            to_time = dataSnapshot.child("Cairo_Ismailia").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                            float f_time = Float.parseFloat(from_time);
                                                                            float t_time = Float.parseFloat(to_time);
                                                                            if (f_time < t_time) {
                                                                                ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                                arraylist.add(ticket);
                                                                                firebase_direction = "Cairo_Ismailia";
                                                                                firebase_class_days = "C_days";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (arraylist.size() == 0) {
                                for (DataSnapshot snapshot1 : dataSnapshot.child("Cairo_Aswan").child("Stations").getChildren()) {
                                    String from = snapshot1.getValue().toString();
                                    if (from.equals(from_value)) {
                                        for (DataSnapshot snapshot2 : dataSnapshot.child("Cairo_Aswan").child("Stations").getChildren()) {
                                            String to = snapshot2.getValue().toString();
                                            if (to.equals(to_value)) {
                                                for (DataSnapshot snapshot3 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").getChildren()) {
                                                    String class_name = snapshot3.getValue().toString();
                                                    if (class_name.equals(class_value)) {
                                                        if (class_name.equals("A")) {
                                                            for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("A_days").getChildren()) {
                                                                String day = snapshot4.getValue().toString();
                                                                if (day.equals(day_name_value)) {
                                                                    ticket_price = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("A_days").child("Price").child("Ticket_price").getValue().toString();
                                                                    for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").getChildren()) {
                                                                        String train_number = snapshot5.getKey();
                                                                        String seats = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                        seats_number = Integer.parseInt(seats);
                                                                        if (seats_number > 0) {
                                                                            from_time = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                            to_time = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("A_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                            float f_time = Float.parseFloat(from_time);
                                                                            float t_time = Float.parseFloat(to_time);
                                                                            if (f_time < t_time) {
                                                                                ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                                arraylist.add(ticket);
                                                                                firebase_direction = "Cairo_Aswan";
                                                                                firebase_class_days = "A_days";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else if (class_name.equals("B")) {
                                                            for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("B_days").getChildren()) {
                                                                String day = snapshot4.getValue().toString();
                                                                if (day.equals(day_name_value)) {
                                                                    ticket_price = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("B_days").child("Price").child("Ticket_price").getValue().toString();
                                                                    for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").getChildren()) {
                                                                        String train_number = snapshot5.getKey();
                                                                        String seats = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                        seats_number = Integer.parseInt(seats);
                                                                        if (seats_number > 0) {
                                                                            from_time = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                            to_time = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("B_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                            float f_time = Float.parseFloat(from_time);
                                                                            float t_time = Float.parseFloat(to_time);
                                                                            if (f_time < t_time) {
                                                                                ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                                arraylist.add(ticket);
                                                                                firebase_direction = "Cairo_Aswan";
                                                                                firebase_class_days = "B_days";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else if (class_name.equals("C")) {
                                                            for (DataSnapshot snapshot4 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("C_days").getChildren()) {
                                                                String day = snapshot4.getValue().toString();
                                                                if (day.equals(day_name_value)) {
                                                                    ticket_price = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("C_days").child("Price").child("Ticket_price").getValue().toString();
                                                                    for (DataSnapshot snapshot5 : dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").getChildren()) {
                                                                        String train_number = snapshot5.getKey();
                                                                        String seats = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Seats").getValue().toString();
                                                                        seats_number = Integer.parseInt(seats);
                                                                        if (seats_number > 0) {
                                                                            from_time = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Time_on_station").child(from_value).getValue().toString();
                                                                            to_time = dataSnapshot.child("Cairo_Aswan").child("Stations").child("Classes").child("Days").child("C_days").child("Trains").child(train_number).child("Time_on_station").child(to_value).getValue().toString();
                                                                            float f_time = Float.parseFloat(from_time);
                                                                            float t_time = Float.parseFloat(to_time);
                                                                            if (f_time < t_time) {
                                                                                ticket = new Ticket(from_value,to_value,class_value,date_value,day_name_value,ticket_price,train_number,from_time,to_time,seats_number);
                                                                                arraylist.add(ticket);
                                                                                firebase_direction = "Cairo_Aswan";
                                                                                firebase_class_days = "C_days";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (arraylist.size() == 0) {
                                Toast.makeText(getContext(), "No Result", Toast.LENGTH_LONG).show();
                            }
                            if (arraylist.size() > 0) {
                                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                fragmentManager = activity.getSupportFragmentManager();
                                Home.addFragment();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        return view;
    }

    public void showDatePickerDialog() {
        datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String [] days = {"Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Monday", "Tuesday"};
                calendar = Calendar.getInstance();
                date_value = dayOfMonth + " - " + (month+1) + " - " + year ;
                calendar.set(year,month-1,dayOfMonth);
                day_name_value = days[calendar.get(calendar.DAY_OF_WEEK)-1];
                textView.setText(date_value);
            }
        }, calender_year, calender_month, calender_day);
        // This line to disable the last date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public boolean validationFrom() {

        if (from_value == null) {
            spinner_from.setError("");
            Toast.makeText(getContext(), "Please choose your going destination", Toast.LENGTH_LONG).show();;
            return false;
        } else {
            spinner_from.setError(null);
            return true;
        }
    }

    public boolean validationTo() {

        if (to_value == null) {
            spinner_to.setError("");
            Toast.makeText(getContext(), "Please choose your arrival destination", Toast.LENGTH_LONG).show();;
            return false;
        } else if (from_value.equals(to_value)) {
            spinner_from.setError("");
            spinner_to.setError("");
            Toast.makeText(getContext(), "Please choose different two destinations", Toast.LENGTH_LONG).show();
            return false;
        } else {
            spinner_to.setError(null);
            return true;
        }
    }

    public boolean validationClass() {

        if (class_value == null) {
            spinner_class.setError("");
            Toast.makeText(getContext(), "Please choose your train class", Toast.LENGTH_LONG).show();;
            return false;
        } else {
            spinner_class.setError(null);
            return true;
        }
    }

    public boolean validationDate() {

        if (date_value == null) {
            textView.setError("");
            Toast.makeText(getContext(), "Please choose your booking date", Toast.LENGTH_LONG).show();
            return false;
        } else {
            textView.setError(null);
            return true;
        }
    }

}