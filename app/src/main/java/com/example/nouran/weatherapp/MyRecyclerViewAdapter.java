package com.example.nouran.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by Nouran on 2/18/2018.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    ArrayList<Event> contents;
    Context context;


    public MyRecyclerViewAdapter(ArrayList<Event> contents) {
        this.contents = contents;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,
                parent, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.ViewHolder holder, int position) {
        Event user = contents.get(position);


        holder.day.setText(getDateString(user.date));
        holder.rain.setText(user.main);
        holder.min.setText(user.min + "");
        holder.max.setText(user.max + "");

        Glide.with(context.getApplicationContext()).
                load(user.icon).
                into(holder.imageView);




    }

    private String getDateString(long timeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd");
        return formatter.format(timeInMilliseconds);

    }


    @Override
    public int getItemCount() {
        return contents.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView day;
        TextView month, min, max, rain;


        public ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.text_day);

            month = itemView.findViewById(R.id.text_month);
            rain = itemView.findViewById(R.id.text_rain);
            min = itemView.findViewById(R.id.min);
            max = itemView.findViewById(R.id.max);
            imageView = itemView.findViewById(R.id.imageView);


        }
    }
}

