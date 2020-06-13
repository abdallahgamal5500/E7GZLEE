package Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentSearch extends Fragment {

    private MaterialSpinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextView textView;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int calender_day, calender_month, calender_year;
    private String date,day_name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        spinner = view.findViewById(R.id.search_from_spinner);
        textView = view.findViewById(R.id.search_date_tv);
        spinner.setItems("Cairo","Giza","Alexandria","Aswan");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String from = spinner.getText().toString();
                if (from.equals("fsd")) {
                }
//                database = FirebaseDatabase.getInstance();
//                myRef = database.getReference("Users").child("child");
//                mAuth = FirebaseAuth.getInstance();
//                myRef.setValue(from);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
//                Calendar container = Calendar.getInstance();
//                datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme , new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        calendar.set(year,month-1,dayOfMonth);
//                        int n = calendar.get(calendar.DAY_OF_WEEK);
//                        String name = String.valueOf(n);
//                        textView.setText(name);
//                    }
//                }, year, month, day);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Disable Past Date
//                datePickerDialog.show();
            }
        });
        return view;

    }
    public void showDatePickerDialog() {

//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//        int month = calendar.get(Calendar.MONTH);
//        int year = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String [] days = {"Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Monday", "Tuesday"};
                calendar = Calendar.getInstance();
                date = dayOfMonth + " - " + (month+1) + " - " + year ;
                calendar.set(year,month-1,dayOfMonth);
                day_name = days[calendar.get(calendar.DAY_OF_WEEK)-1];
                textView.setText(date);
            }
        }, calender_year, calender_month, calender_day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Disable Past Date
        datePickerDialog.show();
    }
    public String getDayName(int day, int month, int year) {
        calendar = Calendar.getInstance();
        calendar.set(year,month-1,day);
        String [] days = {"Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Monday", "Tuesday"};
        int n = calendar.get(calendar.DAY_OF_WEEK);
        return days[n-1];
    }
}