package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.e7gzle.Home;
import com.example.e7gzle.MainActivity;
import com.example.e7gzle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.Recycler_View_Adapter;

public class Fragment_Booking extends Fragment {

    private TextView booking_train_number1,booking_train_number2,booking_going_station1,booking_going_station2,booking_arrival1,booking_arrival2,booking_train_class1,booking_train_class2,booking_seat1,booking_seat2,booking_date1,booking_date2,booking_day1,booking_day2,booking_going_time1,booking_going_time2,booking_arrival_time1,booking_arrival_time2,booking_price1,booking_price2;
    private Button booking_btn_confirm,booking_btn_cancel;
    private FirebaseDatabase database;
    private DatabaseReference myRef1;
    private FirebaseAuth mAuth;
    private int index,seats;
    private String uid;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_booking, container, false);

        booking_train_number1 = view.findViewById(R.id.booking_train_number1);
        booking_train_number2 = view.findViewById(R.id.booking_train_number2);
        booking_going_station1 = view.findViewById(R.id.booking_going_station1);
        booking_going_station2 = view.findViewById(R.id.booking_going_station2);
        booking_arrival1 = view.findViewById(R.id.booking_arrival1);
        booking_arrival2 = view.findViewById(R.id.booking_arrival2);
        booking_train_class1 = view.findViewById(R.id.booking_train_class1);
        booking_train_class2 = view.findViewById(R.id.booking_train_class2);
        booking_seat1 = view.findViewById(R.id.booking_seat1);
        booking_seat2 = view.findViewById(R.id.booking_seat2);
        booking_date1 = view.findViewById(R.id.booking_date1);
        booking_date2 = view.findViewById(R.id.booking_date2);
        booking_day1 = view.findViewById(R.id.booking_day1);
        booking_day2 = view.findViewById(R.id.booking_day2);
        booking_going_time1 = view.findViewById(R.id.booking_going_time1);
        booking_going_time2 = view.findViewById(R.id.booking_going_time2);
        booking_arrival_time1 = view.findViewById(R.id.booking_arrival_time1);
        booking_arrival_time2 = view.findViewById(R.id.booking_arrival_time2);
        booking_price1 = view.findViewById(R.id.booking_price1);
        booking_price2 = view.findViewById(R.id.booking_price2);
        booking_btn_confirm = view.findViewById(R.id.booking_btn_confirm);
        booking_btn_cancel = view.findViewById(R.id.booking_btn_cancel);

        database = FirebaseDatabase.getInstance();
        myRef1 = database.getReference();
        mAuth = FirebaseAuth.getInstance();
        index = Recycler_View_Adapter.item_index;

        booking_train_number2.setText(FragmentSearch.arraylist.get(index).getTrain_number());
        booking_going_station2.setText(FragmentSearch.arraylist.get(index).getFrom());
        booking_arrival2.setText(FragmentSearch.arraylist.get(index).getTo());
        booking_train_class2.setText(FragmentSearch.arraylist.get(index).getTrain_class());
        booking_seat2.setText(""+FragmentSearch.arraylist.get(index).getSeats_number());
        booking_date2.setText(FragmentSearch.arraylist.get(index).getDate());
        booking_day2.setText(FragmentSearch.arraylist.get(index).getDay_name());
        booking_going_time2.setText(FragmentSearch.arraylist.get(index).getFrom_time());
        booking_arrival_time2.setText(FragmentSearch.arraylist.get(index).getTo_time());
        booking_price2.setText(FragmentSearch.arraylist.get(index).getPrice());
        booking_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                fragmentManager = activity.getSupportFragmentManager();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new FragmentSearch()).commit();
            }
        });

        booking_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seats = FragmentSearch.arraylist.get(index).getSeats_number()-1;
                myRef1.child("Ways").child(FragmentSearch.firebase_direction).child("Stations").child("Classes").child("Days").child(FragmentSearch.firebase_class_days).child("Trains").child(FragmentSearch.arraylist.get(index).getTrain_number()).child("Seats").setValue(seats);
                FragmentSearch.arraylist.get(index).setSeats_number(seats);

                // Here I push the confirmed ticket to firebase
                uid = mAuth.getUid();
                myRef1.child("Users").child(uid).child("Tickets").push().setValue(FragmentSearch.arraylist.get(index));

                // Here to show the next fragment
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                fragmentManager = activity.getSupportFragmentManager();
                Home.addFragment();
            }
        });
        return view;
    }
}
