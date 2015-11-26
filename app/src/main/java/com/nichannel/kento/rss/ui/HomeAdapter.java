package com.nichannel.kento.rss.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconButton;
import com.joanzapata.iconify.widget.IconTextView;
import com.nichannel.kento.rss.DetailActivity;
import com.nichannel.kento.rss.R;
import com.nichannel.kento.rss.data.Entry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kento on 15/11/22.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements View.OnClickListener {
    private static ArrayList<Entry> entries;
    private static Context context;
    private int expandedPosition = -1;

    public HomeAdapter(ArrayList<Entry> entries, Context context ){
        this.entries = entries;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView siteName;
        ImageView icon;
        IconTextView expandButton;
        IconTextView colpaseButton;
        LinearLayout expandArea;

        public ViewHolder(View v) {
            super(v);
            title = (TextView)v.findViewById(R.id.title);
            siteName = (TextView)v.findViewById(R.id.siteName);
            icon = (ImageView)v.findViewById(R.id.entry_image);
            description = (TextView)v.findViewById(R.id.description);
            expandArea = (LinearLayout) v.findViewById(R.id.expandArea);
            expandButton = (IconTextView)v.findViewById(R.id.expand_button);
            colpaseButton = (IconTextView)v.findViewById(R.id.collapse);

            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("クリック", "OnViewHolder");
            switch (v.getId()){
                case R.id.title:
                case R.id.entry_image:
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("entry",entries.get(getAdapterPosition()));
                    context.startActivity(intent);
                    break;
            }
            getAdapterPosition();
        }
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        // set the view's size, margins, paddings and layout parameter
        final ViewHolder vh = new ViewHolder(v);
        vh.title = (TextView)v.findViewById(R.id.title);
        vh.siteName = (TextView)v.findViewById(R.id.siteName);
        vh.icon = (ImageView)v.findViewById(R.id.entry_image);
        vh.description = (TextView)v.findViewById(R.id.description);
        vh.expandArea = (LinearLayout) v.findViewById(R.id.expandArea);
        vh.expandButton = (IconTextView)v.findViewById(R.id.expand_button);
        vh.colpaseButton = (IconTextView)v.findViewById(R.id.collapse);


        vh.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                Log.d("Position: ", expandedPosition + "");
                expandedPosition = vh.getAdapterPosition();
                notifyItemChanged(expandedPosition);
            }
        });

        vh.colpaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prev = expandedPosition;
                expandedPosition = -1;
                notifyItemChanged(prev);

            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        Log.d(position + "番目: ", entries.get(position).getTitle());
        Entry entry = entries.get(position);
        holder.title.setText(entry.getTitle());
        holder.description.setText(entry.getDescription());
        holder.siteName.setText(entry.getSite());
        Picasso.with(context).load(entry.getImage()).resize(64, 64)
                .centerCrop().into(holder.icon);

        if (position == expandedPosition) {
            holder.expandArea.setVisibility(View.VISIBLE);
            holder.expandButton.setVisibility(View.GONE);
        } else {
            holder.expandArea.setVisibility(View.GONE);
            holder.expandButton.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return entries.size();
    }
}
