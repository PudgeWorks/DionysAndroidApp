package com.example.h8951.android_http_request_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Aleksi on 6.12.2016.
 */

public class VenuesAdapter extends RecyclerView.Adapter<VenuesAdapter.ViewHolder>{

    // adapter data
    private List<Venue> venuesList;
    private View _view;

    private Bitmap rsEsc;
    private Bitmap rsBra2;
    private Bitmap rsHemmari;
    private Bitmap rsMutka;


    // adapter constructor, get data from activity
    public VenuesAdapter(List<Venue> venuesList) {
        this.venuesList = venuesList;
    }

    // return the size of employeeList (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return venuesList.size();
    }

    // create a view for this card
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_card, parent, false);
        _view = view;
        return new ViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    // - get element from employeelist at this position
    // - replace the contents of the view with that element
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        convertDrawablesToBitmap();

        Venue venue = venuesList.get(position);

        switch(venue.getName()){
            case "Hemingway's":
                viewHolder.venueImageView.setImageBitmap(rsHemmari);
                break;
            case "Escape Club":
                viewHolder.venueImageView.setImageBitmap(rsEsc);
                break;
            case "Mutka":
                viewHolder.venueImageView.setImageBitmap(rsMutka);
                break;
            case "Bra2":
                viewHolder.venueImageView.setImageBitmap(rsBra2);
                break;

        }

        //viewHolder.venueImageView.setImageResource(venue.getImageId());
        viewHolder.venueNameTextView.setText(venue.getName());
        viewHolder.venueAddressTextView.setText(venue.getAddress());
        viewHolder.venueDescriptionTextView.setText(venue.getDescription());
    }

    private void convertDrawablesToBitmap(){
        Bitmap image = BitmapFactory.decodeResource(_view.getContext().getResources(), R.drawable.esc);
        rsEsc = Bitmap.createScaledBitmap(image, 150, 150, true);
        image = BitmapFactory.decodeResource(_view.getContext().getResources(), R.drawable.bra2);
        rsBra2 = Bitmap.createScaledBitmap(image, 150, 150, true);
        image = BitmapFactory.decodeResource(_view.getContext().getResources(), R.drawable.hemppari);
        rsHemmari = Bitmap.createScaledBitmap(image, 150, 150, true);
        image = BitmapFactory.decodeResource(_view.getContext().getResources(), R.drawable.mutka);
        rsMutka = Bitmap.createScaledBitmap(image, 150, 150, true);
    }

    // view holder class to specify card UI objects
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView venueImageView;
        public TextView venueNameTextView;
        public TextView venueAddressTextView;
        public TextView venueDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // get layout ids
            venueImageView = (ImageView) itemView.findViewById(R.id.venueImageView);
            venueNameTextView = (TextView) itemView.findViewById(R.id.venueNameTextView);
            venueAddressTextView = (TextView) itemView.findViewById(R.id.venueAddressTextView);
            venueDescriptionTextView = (TextView) itemView.findViewById(R.id.venueDescriptionTextView);

            // add click listner for a card
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String name = venuesList.get(position)._name;
                    Toast.makeText(view.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
