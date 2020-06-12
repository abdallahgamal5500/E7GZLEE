package Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e7gzle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;

public class FragmentSearch extends Fragment {

    private MaterialSpinner spinner;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextView textView;
    private DatePickerDialog.OnDateSetListener setListener;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int year,month,day;

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
            }
        });
        return view;

    }
    public void showDatePickerDialog() {

        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        datePickerDialog = new DatePickerDialog(getContext(), R.style.MyDatePickerDialogTheme , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
//                calendar.set(Calendar.YEAR , year);
//                calendar.set(Calendar.MONTH , month);
//                calendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
//                date.setText(currentDate);
                textView.setText(dayOfMonth + " - " + (month + 1) + " - " + year);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Disable Past Date
        datePickerDialog.show();

    }
}