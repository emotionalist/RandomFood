package com.cookandroid.suwonrandomfood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecyclerViewFragment extends Fragment {
    ArrayList<Store> stores = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    String uid = firebaseUser.getUid();
    SearchView searchView;

    Button sortname, sortrecent, sortstar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        adapter = new RecyclerViewAdapter(stores);
        searchView = (SearchView) v.findViewById(R.id.searchView);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(uid+"/Store");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stores.clear();
                for(DataSnapshot sn : snapshot.getChildren()){
                    Store store = sn.getValue(Store.class);
                    stores.add(store);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        sortname = v.findViewById(R.id.sortname);
        sortrecent = v.findViewById(R.id.sortrecent);
        sortstar = v.findViewById(R.id.sortstar);

        sortname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<Store> nameASC = new Comparator<Store>() {
                    @Override
                    public int compare(Store item1, Store item2) {
                        return item1.getName().compareTo(item2.getName());
                    }
                };
                Collections.sort(stores, nameASC);
                adapter.notifyDataSetChanged();
            }
        });

        sortrecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<Store> recentASC = new Comparator<Store>() {
                    @Override
                    public int compare(Store item1, Store item2) {
                        return item1.getNum() - item2.getNum();
                    }
                };
                Collections.sort(stores, recentASC);
                adapter.notifyDataSetChanged();
            }
        });

        sortstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comparator<Store> starDESC = new Comparator<Store>() {
                    @Override
                    public int compare(Store item1, Store item2) {
                        return item2.getStar().compareTo(item1.getStar());
                    }
                };
                Collections.sort(stores, starDESC);
                adapter.notifyDataSetChanged();
            }
        });


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void filterList(String text){
        ArrayList<Store> filteredList = new ArrayList<>();
        for (Store store : stores) {
            if (store.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(store);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getActivity(), "찾는 음식점이 없습니다", Toast.LENGTH_LONG).show();
        }
        else {
            adapter.setFilteredList(filteredList);
        }
    }
}