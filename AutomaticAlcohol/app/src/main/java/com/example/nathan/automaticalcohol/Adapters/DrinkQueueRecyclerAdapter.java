package com.example.nathan.automaticalcohol.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkQueueRecyclerAdapter extends RecyclerView.Adapter<DrinkQueueRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "DrinkQueueRecyclerAdapt";

    private Context mContext;
    private List<Order> mData;   // this is a list of all the orders in the RecyclerView (passed in when created/updated)
    private String mType;
    private RecyclerInterface recyclerInterface;
    private Dialog myDialog;

    /**
     *
     * @param mContext  - this is basically the Activity I think...
     * @param mData     - lost of all orders in RecyclerView
     * @param type
     * @param recyclerInterface
     */
    public DrinkQueueRecyclerAdapter(Context mContext, List<Order> mData, String type, RecyclerInterface recyclerInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.mType = type;
        this.recyclerInterface = recyclerInterface;
    }


    /**
     * I think this is called before (??) onBindViewHolder and is what dumps the stuff onto the
     * screen for us to see
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.e(TAG, "onCreateViewHolder");
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(view);

        // initialize the dialog (popup)
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_contact);

        // this is called when an entry in the Drink Queue is clicked
        vHolder.item_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onCreateViewHolder - clicked");
                // initialize buttons and such that are on the dialog
                Button dialog_remove = myDialog.findViewById(R.id.dialog_remove);
                TextView dialog_cust_name = myDialog.findViewById(R.id.dialog_cust_name);
                TextView dialog_drink_name = myDialog.findViewById(R.id.dialog_drink_name);
                ImageView dialog_img = myDialog.findViewById(R.id.dialog_img);

                // when this button is clicked, the drink still has not been made and will be removed from the queue and the order history
                dialog_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /**
                         * when grabbing info from a specific entry in the RecyclerView (ie. the one that has been clicked)
                         * mData    - list of entries in RecyclerView
                         * .get()   - getter for a list
                         * vHolder.getAdapterPosition()     - the location in the list that was clicked on
                         *
                         * // the rest of it are class functions of an order
                         * // making, IN THIS CASE,  "mData.get(vHolder.getAdapterPosition())" of type Order.class (an Order object)
                         */
                        mData.get(vHolder.getAdapterPosition()).removeOrder(mData.get(vHolder.getAdapterPosition()).getOrderNumber());
                        myDialog.dismiss();
                    }
                });

                // does the image handling
                Picasso.get()
                        .load("https://www.chowstatic.com/assets/recipe_photos/10207_highball.jpg")
                        .into(dialog_img);

                // sets the names and opens the dialog
                dialog_cust_name.setText(mData.get(vHolder.getAdapterPosition()).getName());
                dialog_drink_name.setText(mData.get(vHolder.getAdapterPosition()).getDrink().getName());
                myDialog.show();
            }
        });

        return vHolder;
    }


    /**
     * "onBindViewHolder" helps handle what is displayed in the recyclerView. So when you scroll
     *  up and down on it, this is what gets called so figure out what to show next.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DrinkQueueRecyclerAdapter.MyViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder");

        // sets the customerName and drinkName of each entry in the RecyclerView
        // happens iteratively (implicitly) for each entry in a RecyclerView
        holder.textView_name.setText(mData.get(position).getName());
        holder.textView_price.setText(mData.get(position).getDrink().getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout item_contact;
        private TextView textView_name;
        private TextView textView_price;
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            // initializes the buttons and such on each RecyclerView entry
            item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item_id);
            textView_name = (TextView) itemView.findViewById(R.id.drink_name);
            textView_price = (TextView) itemView.findViewById(R.id.drink_price);
            img = (ImageView) itemView.findViewById(R.id.img_drink);


        }
    }

}
