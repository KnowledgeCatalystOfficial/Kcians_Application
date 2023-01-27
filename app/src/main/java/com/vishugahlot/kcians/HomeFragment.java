package com.vishugahlot.kcians;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    RecyclerView community_recycler;
    community_category_adapter community_category_adapter;
    ArrayList<Community_data> community_data;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        community_recycler=view.findViewById(R.id.cat_recycler);
        community_recycler.setHasFixedSize(true);
        community_data=new ArrayList<>();
        community_recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        community_category_adapter = new community_category_adapter(getContext(), community_data, new community_category_adapter.OnItemClickListener() {

            public void onItemClick(String item) {
                try {
                    // updateRecycler(item);
                }
                catch (Exception e)
                {

                }
            }
        });
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot dataSnapshot : snapshot.child("Category").getChildren()) {

                        community_data.add(dataSnapshot.getValue(Community_data.class));
                    }

                }catch (Exception e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
              //  Toast.makeText(getContext(), community_data.get(0).getCImage(), Toast.LENGTH_SHORT).show();

                community_category_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

        community_recycler.setAdapter(community_category_adapter);
        community_recycler.getRecycledViewPool().setMaxRecycledViews(0, 0);

        return view;
    }
}