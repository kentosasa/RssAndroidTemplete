package com.nichannel.kento.rss.ui;

import android.content.Context;
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

import com.nichannel.kento.rss.R;
import com.nichannel.kento.rss.data.Entry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kento on 15/11/22.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<Entry> entries;
    private Context context;
    private int expandedPosition = -1;

    public HomeAdapter(ArrayList<Entry> entries, Context context ){
        this.entries = entries;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView siteName;
        ImageView icon;
        ImageButton expandButton;
        LinearLayout expandArea;


        public ViewHolder(View itemView) {
            super(itemView);
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
        vh.expandArea = (LinearLayout) v.findViewById(R.id.llExpandArea);
        vh.expandButton = (ImageButton)v.findViewById(R.id.expand_button);

        vh.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click: ", "されてる！");
                if (expandedPosition >= 0){
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                Log.d("Position: ", expandedPosition+"");
                expandedPosition = vh.getAdapterPosition();
                notifyItemChanged(expandedPosition);
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
        } else {
            holder.expandArea.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
