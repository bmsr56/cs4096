package com.example.nathan.automaticalcohol.Adapters;

import android.app.Dialog;
import android.content.Context;
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

import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

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
    public CookbookResultsRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.e("fart", "onCreateViewHolder");
        View view;

        // this makes the stuff in the RecyclerView look the way we want it to
        view = LayoutInflater.from(mContext).inflate(R.layout.cookbook_results, parent, false);

        final CookbookResultsRecyclerAdapter.MyViewHolder vHolder = new CookbookResultsRecyclerAdapter.MyViewHolder(view);

//        if (this.mType.equals(Constants.DRINKS)) {

            // Dialog init
            // TODO: this will have to be removed (maybe refactored for other use)
            myDialog = new Dialog(mContext);
            myDialog.setContentView(R.layout.cookbook_results);
            view = LayoutInflater.from(mContext).inflate(R.layout.cookbook_results, parent, false);

            vHolder.cookbook_results.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // all of this was for the dialog (pop up)
//                    TextView dialog_name_tv = myDialog.findViewById(R.id.dialog_name_id);
//                    TextView dialog_phone_tv = myDialog.findViewById(R.id.dialog_phone_id);
//                    ImageView dialog_contact_img = myDialog.findViewById(R.id.dialog_img);
//                    dialog_name_tv.setText(mData.get(vHolder.getAdapterPosition()));
//                    dialog_phone_tv.setText(mData.get(vHolder.getAdapterPosition()));

                    ImageView dialog_img = myDialog.findViewById(R.id.drink_image);

                    Toast.makeText(mContext, "Test Click" + String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();


//                    Bundle bundle = new Bundle();
//                    bundle.putString("drinkName", mData.get(vHolder.getAdapterPosition()));

                    // does the image handling
                    Picasso.get()
                            .load(mData.get(vHolder.getAdapterPosition()).getImage())
                            .into(dialog_img);

                    myDialog.show();
                    // TODO: figure out how to make drink then send it to pi??
                }
            });
//        }

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CookbookResultsRecyclerAdapter.MyViewHolder holder, int position) {

        Log.e(TAG, "size: "+Integer.toString(mData.size()));


        holder.textView_name.setText(mData.get(position).getName());
        // might use string.format instead
       // holder.textView_price.setText(String.format(Locale.US, "$ %.2f", mData.get(position).getPrice()));

        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, mData.get(index).getName(), Toast.LENGTH_SHORT).show();
                //recyclerInterface.onTagClicked(mData.get(index));
            }
        });

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
            textView_name = itemView.findViewById(R.id.btn_drink);
            img = itemView.findViewById(R.id.drink_image);


        }
    }

}