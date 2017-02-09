package com.mss.firebasedummy.FireBaseDatabase.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mss.firebasedummy.FireBaseDatabase.activity.CreateUserActivity;
import com.mss.firebasedummy.FireBaseDatabase.activity.UpdateUserActivity;
import com.mss.firebasedummy.FireBaseDatabase.model.UserModel;
import com.mss.firebasedummy.R;

import java.util.List;

/**
 * Created by deepakgupta on 9/2/17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    Context mContext;
    List<UserModel> userList;
    DatabaseReference database;

    public UserListAdapter(Context context, List<UserModel> userList) {
        mContext = context;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_item, parent, false);
        return new ViewHolder(contactsView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.txtName.setText(user.getName());
        holder.txtDesc.setText(user.getDesc());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDesc;
        UserModel item;

        public ViewHolder(View itemView) {
            super(itemView);
            database = FirebaseDatabase.getInstance().getReference();
            txtName = (TextView) itemView.findViewById(R.id.user_title);
            txtDesc = (TextView) itemView.findViewById(R.id.user_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    UserModel userModel = userList.get(position);
                    Intent update = new Intent(mContext, UpdateUserActivity.class);
                    update.putExtra("update", userModel);
                    mContext.startActivity(update);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    deleteUser(getAdapterPosition());
                    return false;
                }
            });
        }

        private void deleteUser(int adapterPosition) {
            UserModel userModel = userList.get(adapterPosition);
            userList.remove(adapterPosition);
            database.child("users").child(userModel.getUid()).removeValue();
            notifyDataSetChanged();
        }
    }
}
