package com.vishugahlot.kcians;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class community_category_adapter extends RecyclerView.Adapter<community_category_adapter.ViewHolder> {



    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    //vars
    private ArrayList<Community_data> community_data;
    private Context context;
    private final OnItemClickListener listener;

    public community_category_adapter(Context context, ArrayList<Community_data> community_data, OnItemClickListener listener){
        this.context=context;
        this.listener=listener;
        this.community_data=community_data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_single_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.category.setText(community_data.get(position).getCName());
        holder.bind(community_data.get(position).getCName(), listener);
        Picasso.get()
                .load(String.valueOf(community_data.get(position).getCImage()))
                .into(holder.category_image);
    }

    @Override
    public int getItemCount() {
        return community_data.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        ImageView category_image;
        public ViewHolder(View itemView){
            super(itemView);
            category = itemView.findViewById(R.id.text_view);
            category_image=itemView.findViewById(R.id.image_view);

        }

        public void bind(String s,OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(s);
                }
            });
        }
    }
}
