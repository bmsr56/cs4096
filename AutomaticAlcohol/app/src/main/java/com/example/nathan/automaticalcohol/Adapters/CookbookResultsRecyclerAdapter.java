package com.example.nathan.automaticalcohol.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CookbookResultsRecyclerAdapter extends RecyclerView.Adapter<CookbookResultsRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "CookbookRecyclerAdapter";
    private Context mContext;
    private List<Drink> mData;
    private String mType;
    private RecyclerInterface recyclerInterface;
    private Dialog myDialog;

    public CookbookResultsRecyclerAdapter(Context mContext, List<Drink> mData, String type, RecyclerInterface recyclerInterface) {
        this.mContext = mContext;
        this.mData = mData;
        this.mType = type;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.e(TAG, "onCreateViewHolder");
        // this makes the stuff in the RecyclerView look the way we want it to
        View view = LayoutInflater.from(mContext).inflate(R.layout.cookbook_results, parent, false);

        final MyViewHolder vHolder = new MyViewHolder(view);

        // Dialog init
        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_search);

        //
        vHolder.cookbook_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // all of this was for the dialog (pop up)
                TextView dialog_name = myDialog.findViewById(R.id.dialog_searchDrinkName);
                TextView dialog_ing1 = myDialog.findViewById(R.id.dialog_searchIng1);
                TextView dialog_ing2 = myDialog.findViewById(R.id.dialog_searchIng2);
                TextView dialog_ing3 = myDialog.findViewById(R.id.dialog_searchIng3);

                ImageView dialog_searchImg = myDialog.findViewById(R.id.dialog_searchImg);

                // does the image handling after clicking on a drink to show ingredients
                Picasso.get()
                        .load(mData.get(vHolder.getAdapterPosition()).getImage())
                        .into(dialog_searchImg);

                // sets the name of the drink in the dialog
                dialog_name.setText(mData.get(vHolder.getAdapterPosition()).getName());

                // grabs the ingredients of the clicked drink
                HashMap<String, Float> ing = mData.get(vHolder.getAdapterPosition()).getIngredients();
                int i = 0;   // counter to know which ingredient location to insert into

                // clear what's in the dialog for no overlap
                dialog_ing1.setText("");
                dialog_ing2.setText("");
                dialog_ing3.setText("");

                // for each ingredient, put name and amount in textView
                for(String h: ing.keySet()) {
                    if(i == 0) {
                        dialog_ing1.setText(h+", "+mData.get(vHolder.getAdapterPosition()).getIngredients().get(h)+" ml");
                    } else if (i == 1) {
                        dialog_ing2.setText(h+", "+mData.get(vHolder.getAdapterPosition()).getIngredients().get(h)+" ml");
                    } else if (i == 2) {
                        dialog_ing3.setText(h+", "+mData.get(vHolder.getAdapterPosition()).getIngredients().get(h)+" ml");
                    }

                    i++;
                }

                // display the dialog
                myDialog.show();
            }
        });
        return vHolder;
    }

    // this happens when displaying the recycler view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView_name.setText(mData.get(position).getName());

        // does the image handling when displaying drinks in recyclerView
        Picasso.get()
                .load(mData.get(holder.getAdapterPosition()).getImage())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout cookbook_results;
        private TextView textView_name;
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            cookbook_results = itemView.findViewById(R.id.cookbook_results_id);
            textView_name = itemView.findViewById(R.id.tv_drink);
            img = itemView.findViewById(R.id.drink_image);
        }
    }

}