package com.example.sandeepmungara.atmbranchlocator.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sandeepmungara.atmbranchlocator.R;
import com.example.sandeepmungara.atmbranchlocator.viewmodel.ResultsModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class RecylerListAdapter extends RecyclerView.Adapter<RecylerListAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<ResultsModel> resultsModels;

    public RecylerListAdapter(final Context context, final List<ResultsModel> resultsModels) {
        this.context = context;
        this.resultsModels = resultsModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        Double latitude = Double.parseDouble(resultsModels.get(i).geometry.location.lat);
        Double longitude = Double.parseDouble(resultsModels.get(i).geometry.location.lng);
        LatLng latLng = new LatLng(latitude, longitude);
        myViewHolder.name.setText(resultsModels.get(i).name);
        myViewHolder.latLong.setText(latLng.toString());
        myViewHolder.address.setText(resultsModels.get(i).getFormatted_address());
        myViewHolder.icon.setImageBitmap(resultsModels.get(i).bitmap);
        myViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setFlags( FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, resultsModels.get(i).geometry.location);
                sendIntent.putExtra(Intent.EXTRA_TEXT, resultsModels.get(i).opening_hours.open_now);
                sendIntent.setType("text/plain");
                view.getContext().startActivity(sendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsModels.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView icon;
        private TextView address;
        private TextView latLong;
        private ImageButton shareButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            address = itemView.findViewById(R.id.address);
            latLong = itemView.findViewById(R.id.latlong);
            shareButton = itemView.findViewById(R.id.share_button);
        }
    }
}
