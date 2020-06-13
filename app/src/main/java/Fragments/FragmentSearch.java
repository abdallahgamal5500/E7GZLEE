package Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private MaterialSpinner spinner_from, spinner_to, spinner_class;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextView textView;
    Button btn;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int calender_day, calender_month, calender_year;
    private String from_value,to_value,class_value,date_value,day_name_value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        spinner_from = view.findViewById(R.id.search_from_spinner);
        spinner_to = view.findViewById(R.id.search_to_spinner);
        spinner_class = view.findViewById(R.id.search_class_spinner);
        textView = view.findViewById(R.id.search_date_tv);
        btn = view.findViewById(R.id.search_btn);

        spinner_from.setItems("Alexandria","Aswan","Asyut","Banha","Bani Seuf","Cairo","Damanhour","Domyat","Giza","Ismailia","Luxor","Mansora","Menia","Port Said","Qena","Sohag","Tanta");
        spinner_to.setItems("Alexandria","Aswan","Asyut","Banha","Bani Seuf","Cairo","Damanhour","Domyat","Giza","Ismailia","Luxor","Mansora","Menia","Port Said","Qena","Sohag","Tanta");
        spinner_class.setItems("A","B","C","D");

        database = FirebaseDatabase.getInstance();
//                myRef = database.getReference("Users").child("child");
//                mAuth = FirebaseAuth.getInstance();
//                myRef.setValue(from);

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
            public void onClick(View v) {
                if (!validationFrom()) {
                } else if (!validationTo()) {
                } else if (!validationClass()) {
                } else if (!validationDate()) {
                } else {
                    Toast.makeText(getContext(), "Fuck u", Toast.LENGTH_SHORT).show();
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Disable Past Date
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