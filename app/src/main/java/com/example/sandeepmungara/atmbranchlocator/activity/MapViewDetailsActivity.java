package com.example.sandeepmungara.atmbranchlocator.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sandeepmungara.atmbranchlocator.R;
import com.example.sandeepmungara.atmbranchlocator.constants.ConstantDataClass;

public class MapViewDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_details_activity);

        final TextView titleTextView = findViewById(R.id.details_title);
        final TextView latLonTextView = findViewById(R.id.details_latlong);
        final ImageView detialsImageview = findViewById(R.id.details_icon);
        final TextView fullAddressTextView = findViewById(R.id.details_address);
        final ImageButton navigateImageButton = findViewById(R.id.navigate);
        navigateImageButton.setOnClickListener(this);

        titleTextView.setText(ConstantDataClass.getResultsModels().getTitle());
        latLonTextView.setText(ConstantDataClass.getResultsModels().getPosition().toString());
        fullAddressTextView.setText(ConstantDataClass.getResultsModels().getSnippet());
        detialsImageview.setImageAlpha((int) ConstantDataClass.getResultsModels().getAlpha());
    }


    @Override
    public void onClick(View view) {
        Double latitude = ConstantDataClass.getResultsModels().getPosition().latitude;
        Double longitude = ConstantDataClass.getResultsModels().getPosition().longitude;
        String label = ConstantDataClass.getResultsModels().getTitle();
        String format = "geo:0,0?q=" + Double.toString(latitude) + "," + Double.toString(longitude) + "(" + label + ")";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
