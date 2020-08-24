package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzle.Home;
import com.example.e7gzle.R;

import java.util.ArrayList;

import Fragments.FragmentSearch;
import Fragments.FragmentSetting;
import Fragments.FragmentTicket;
import Fragments.Fragment_Booking;
import Fragments.Fragment_Result;

public class Recycler_View_Adapter extends RecyclerView.Adapter<Recycler_View_Adapter.Recycler_View_Holder> {

    private Context mContext;
    private ArrayList<String> train_number = new ArrayList<>();
    private ArrayList<String> going_time = new ArrayList<>();
    private ArrayList<String> arrival_time = new ArrayList<>();
    private ArrayList<String> train_class = new ArrayList<>();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static int item_index=0;

    public Recycler_View_Adapter(Context mContext, ArrayList<String> train_number, ArrayList<String> going_time, ArrayList<String> arrival_time, ArrayList<String> train_class) {
        this.mContext = mContext;
        this.train_number = train_number;
        this.going_time = going_time;
        this.arrival_time = arrival_time;
        this.train_class = train_class;
    }

    @NonNull
    @Override
    public Recycler_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Recycler_View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.result_recycler_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final Recycler_View_Holder holder, final int position) {
        holder.tv_train_number.setText(train_number.get(position));
        holder.tv_going_time.setText(going_time.get(position));
        holder.tv_arrival_time.setText(arrival_time.get(position));
        holder.tv_train_class.setText(train_class.get(position));
        holder.btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // These two lines to replace the fragment
                item_index = holder.getPosition();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragmentManager = activity.getSupportFragmentManager();
                Home.addFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return train_number.size();
    }

    public class Recycler_View_Holder extends RecyclerView.ViewHolder {

        TextView tv_train_number,tv_going_time,tv_arrival_time,tv_train_class;
        Button btn;

        public Recycler_View_Holder(@NonNull View itemView) {
            super(itemView);
            tv_train_number = itemView.findViewById(R.id.result_recycler_item_train_number);
            tv_going_time = itemView.findViewById(R.id.result_recycler_item_train_going);
            tv_arrival_time = itemView.findViewById(R.id.result_recycler_item_train_arrival);
            tv_train_class = itemView.findViewById(R.id.result_recycler_item_train_class);
            btn = itemView.findViewById(R.id.result_recycler_item_btn);
        }
    }
}
