package com.example.h8951.android_http_request_test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Aleksi on 7.12.2016.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> usersList;
    private View _view;

    // adapter constructor, get data from activity
    public UsersAdapter(List<User> usersList) {
        this.usersList = usersList;
    }

    // return the size of employeeList (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    // create a view for this card
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        _view = view;
        return new UsersAdapter.ViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    // - get element from employeelist at this position
    // - replace the contents of the view with that element
    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder viewHolder, int position) {

        User user = usersList.get(position);

        //viewHolder.userImageView.setImageResource(user.getImageId());
        if(user != null) {
            viewHolder.userNickTextView.setText(user.getNick());
            viewHolder.userAgeTextView.setText(user.getAge());
            viewHolder.userDescriptionTextView.setText(user.getBio());

            if (user.getSex()) {
                viewHolder.userSexTextView.setText("Mies");
            } else {
                viewHolder.userSexTextView.setText("Nainen");
            }
        }
    }

    // view holder class to specify card UI objects
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView userImageView;
        public TextView userNickTextView;
        public TextView userAgeTextView;
        public TextView userSexTextView;
        public TextView userDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            // get layout ids
            userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
            userNickTextView = (TextView) itemView.findViewById(R.id.userNickTextView);
            userAgeTextView = (TextView) itemView.findViewById(R.id.userAgeTextView);
            userSexTextView = (TextView) itemView.findViewById(R.id.userSexTextView);
            userDescriptionTextView = (TextView) itemView.findViewById(R.id.userDescriptionTextView);

            // add click listner for a card
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    String name = usersList.get(position).getNick();
                    Toast.makeText(view.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
