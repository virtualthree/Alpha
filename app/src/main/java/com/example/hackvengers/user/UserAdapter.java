package com.example.hackvengers.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hackvengers.AllChatsActivity;
import com.example.hackvengers.CreateSingleChatActivity;
import com.example.hackvengers.ImageViewActivity;
import com.example.hackvengers.R;
import com.example.hackvengers.UserDetailsActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {


    ArrayList<UserObject> mUserList;

    boolean isSingleChatActivity,isGroupDetailsActivity,isCurrentUserProfileActivity;

    Context context;


    public UserAdapter(ArrayList<UserObject> userList, Context context,boolean isSingleChatActivity,boolean isGroupDetailsActivity,boolean isCurrentUserProfileActivity){
        mUserList=userList;
        this.context=context;
        this.isSingleChatActivity=isSingleChatActivity;
        this.isGroupDetailsActivity=isGroupDetailsActivity;
        this.isCurrentUserProfileActivity=isCurrentUserProfileActivity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_new,null,false);

        RecyclerView.LayoutParams layoutParams= new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final UserObject user = mUserList.get(position);
        holder.mName.setText(user.getName());


        String curUserKey = AllChatsActivity.curUser.getUid();
        if (user.getUid().equals(curUserKey)) {
            holder.mName.setText("You");
        }

        if (!user.getProfileImageUri().equals("")) {
            holder.mProfilePhoto.setClipToOutline(true);
            Glide.with(context).load(Uri.parse(user.getProfileImageUri())).into(holder.mProfilePhoto);
        }
        holder.mProfilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("URI", user.getProfileImageUri());
            context.startActivity(intent);
        });

        if(isCurrentUserProfileActivity){
            holder.isSelected.setVisibility(View.GONE);
            holder.mGiveRating.setVisibility(View.VISIBLE);


            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("userKey", user.getUid());
                context.startActivity(intent);
            });

            //TODO: Add click listener to this give rating
        }

        if (isSingleChatActivity) {
            holder.isSelected.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, CreateSingleChatActivity.class);
                intent.putExtra("userObject", user);
                context.startActivity(intent);
            });

        } else if (isGroupDetailsActivity) {
            holder.isSelected.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("userObject", user);
                context.startActivity(intent);
            });
        } else {
            holder.isSelected.setOnCheckedChangeListener((buttonView, isChecked) -> user.setSelected(isChecked));
        }

    }


    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName,
                mGiveRating;

        ImageView mProfilePhoto;

        CheckBox isSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.userName);
            mGiveRating=itemView.findViewById(R.id.giveRating);


            mProfilePhoto=itemView.findViewById(R.id.profileImage);

            isSelected=itemView.findViewById(R.id.userSelected);
        }
    }
}
