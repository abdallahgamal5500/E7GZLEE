package Fragments;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzle.R;

import java.util.ArrayList;

import Adapters.Recycler_View_Adapter;

import static androidx.core.content.ContextCompat.getSystemService;

public class Fragment_Result extends Fragment {

    private ArrayList<String> train_number;
    private ArrayList<String> going_time;
    private ArrayList<String> arrival_time;
    private ArrayList<String> train_class;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.result_recycler);

        train_number = new ArrayList<String>();
        going_time = new ArrayList<String>();
        arrival_time = new ArrayList<String>();
        train_class = new ArrayList<String>();

        for (int i=0;i<FragmentSearch.arraylist.size();i++) {
            train_number.add(FragmentSearch.arraylist.get(i).getTrain_number());
            going_time.add(FragmentSearch.arraylist.get(i).getFrom_time());
            arrival_time.add(FragmentSearch.arraylist.get(i).getTo_time());
            train_class.add(FragmentSearch.arraylist.get(i).getTrain_class());
        }

        Recycler_View_Adapter adapter = new Recycler_View_Adapter(getContext(),train_number,going_time,arrival_time,train_class);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}