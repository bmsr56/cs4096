package com.example.nathan.automaticalcohol.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;

import java.util.List;

public class DrinkQueueRecyclerAdapter extends RecyclerView.Adapter<DrinkQueueRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "DrinkQueueRecyclerAdapter";

    private Context mContext;
    private List<Order> mData;
    private String mType;
    private RecyclerInterface recyclerInterface;
    private Dialog myDialog;

    public DrinkQueueRecyclerAdapter(Context mContext, List<Order> mData, String type, RecyclerInterface recyclerInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.mType = type;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public DrinkQueueRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.e(TAG, this.mType);
        View view;

        // TODO: I think a separate one of thses is going to have to be made for the drink queue if we want it to look different
        view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);



        final DrinkQueueRecyclerAdapter.MyViewHolder vHolder = new DrinkQueueRecyclerAdapter.MyViewHolder(view);

        if (this.mType.equals(Constants.SPECIALS)) {

            // Dialog init
            // TODO: this will have to be removed (maybe refactored for other use)
            myDialog = new Dialog(mContext);
            myDialog.setContentView(R.layout.dialog_contact);
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);

            vHolder.item_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TextView dialog_name_tv = myDialog.findViewById(R.id.dialog_name_id);
//                    TextView dialog_phone_tv = myDialog.findViewById(R.id.dialog_phone_id);
//                    ImageView dialog_contact_img = myDialog.findViewById(R.id.dialog_img);
//                    dialog_name_tv.setText(mData.get(vHolder.getAdapterPosition()));
//                    dialog_phone_tv.setText(mData.get(vHolder.getAdapterPosition()));

                    Toast.makeText(mContext, "Test Click" + String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();


                    Bundle bundle = new Bundle();
//                    bundle.putString("drinkName", mData.get(vHolder.getAdapterPosition()));


//                    myDialog.show();
                    // TODO: figure out how to make drink then send it to pi??
                }
            });
        } else if (this.mType.equals(Constants.DRINK_QUEUE)) {
            // TODO: handle logic for creating "button" pushes for the drink queue
            // TODO: handle logic for making new "view = LayoutInflater..." stuff
        }

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkQueueRecyclerAdapter.MyViewHolder holder, int position) {
        holder.textView_name.setText(mData.get(position).getName());
        holder.textView_phone.setText(mData.get(position).getDrink().getName());

        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mData.get(index).getDrink().getName(), Toast.LENGTH_SHORT).show();
                recyclerInterface.onTagClicked(mData.get(index));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_contact;
        private TextView textView_name;
        private TextView textView_phone;
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item_id);
            textView_name = (TextView) itemView.findViewById(R.id.drink_name);
            textView_phone = (TextView) itemView.findViewById(R.id.drink_price);
            img = (ImageView) itemView.findViewById(R.id.img_drink);


        }
    }

}
