package Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Fragments.FragmentSearch;
import Fragments.FragmentTicket;
import Models.Ticket;

import static android.content.Intent.getIntent;

public class Ticket_Recycler_View_Adapter extends RecyclerView.Adapter<Ticket_Recycler_View_Adapter.Ticket_Recycler_View_Holder>{

    private Context mContext;
    private ArrayList <Ticket> tickets = new ArrayList<Ticket>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String uid;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public Ticket_Recycler_View_Adapter(Context mContext, ArrayList<Ticket> tickets) {
        this.mContext = mContext;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public Ticket_Recycler_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Ticket_Recycler_View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_recycler_items,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull Ticket_Recycler_View_Holder holder, final int position) {

        holder.ticket_train_number2.setText(tickets.get(position).getTrain_number());
        holder.ticket_going_station2.setText(tickets.get(position).getFrom());
        holder.ticket_arrival2.setText(tickets.get(position).getTo());
        holder.ticket_train_class2.setText(tickets.get(position).getTrain_class());
        holder.ticket_seat2.setText(""+tickets.get(position).getSeats_number());
        holder.ticket_date2.setText(tickets.get(position).getDate());
        holder.ticket_day2.setText(tickets.get(position).getDay_name());
        holder.ticket_going_time2.setText(tickets.get(position).getFrom_time());
        holder.ticket_arrival_time2.setText(tickets.get(position).getTo_time());
        holder.ticket_price2.setText(tickets.get(position).getPrice());

        holder.ticket_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference();
                mAuth = FirebaseAuth.getInstance();
                uid = mAuth.getUid();
                myRef.child("Users").child(uid).child("Tickets").child(FragmentTicket.push_id.get(position)).removeValue();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragmentManager = activity.getSupportFragmentManager();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new FragmentTicket()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class Ticket_Recycler_View_Holder extends RecyclerView.ViewHolder {

        TextView ticket_train_number1,ticket_train_number2,ticket_going_station1,ticket_going_station2,ticket_arrival1,ticket_arrival2,ticket_train_class1,ticket_train_class2,ticket_seat1,ticket_seat2,ticket_date1,ticket_date2,ticket_day1,ticket_day2,ticket_going_time1,ticket_going_time2,ticket_arrival_time1,ticket_arrival_time2,ticket_price1,ticket_price2;
        Button ticket_btn_cancel;

        public Ticket_Recycler_View_Holder(@NonNull View itemView) {
            super(itemView);
            ticket_train_number1 = itemView.findViewById(R.id.ticket_recycler_item_train_number1);
            ticket_train_number2 = itemView.findViewById(R.id.ticket_recycler_item_train_number2);
            ticket_going_station1 = itemView.findViewById(R.id.ticket_recycler_item_going_station1);
            ticket_going_station2 = itemView.findViewById(R.id.ticket_recycler_item_going_station2);
            ticket_arrival1 = itemView.findViewById(R.id.ticket_recycler_item_arrival1);
            ticket_arrival2 = itemView.findViewById(R.id.ticket_recycler_item_arrival2);
            ticket_train_class1 = itemView.findViewById(R.id.ticket_recycler_item_train_class1);
            ticket_train_class2 = itemView.findViewById(R.id.ticket_recycler_item_train_class2);
            ticket_seat1 = itemView.findViewById(R.id.ticket_recycler_item_seat1);
            ticket_seat2 = itemView.findViewById(R.id.ticket_recycler_item_seat2);
            ticket_date1 = itemView.findViewById(R.id.ticket_recycler_item_date1);
            ticket_date2 = itemView.findViewById(R.id.ticket_recycler_item_date2);
            ticket_day1 = itemView.findViewById(R.id.ticket_recycler_item_day1);
            ticket_day2 = itemView.findViewById(R.id.ticket_recycler_item_day2);
            ticket_going_time1 = itemView.findViewById(R.id.ticket_recycler_item_going_time1);
            ticket_going_time2 = itemView.findViewById(R.id.ticket_recycler_item_going_time2);
            ticket_arrival_time1 = itemView.findViewById(R.id.ticket_recycler_item_arrival_time1);
            ticket_arrival_time2 = itemView.findViewById(R.id.ticket_recycler_item_arrival_time2);
            ticket_price1 = itemView.findViewById(R.id.ticket_recycler_item_price1);
            ticket_price2 = itemView.findViewById(R.id.ticket_recycler_item_price2);
            ticket_btn_cancel = itemView.findViewById(R.id.ticket_recycler_item_btn_cancel);
        }
    }
}
