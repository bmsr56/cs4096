package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.R;

public class TabCookbookFragment extends Fragment {
    private static final String TAG = "TabCookbookFragment";

    private Button btn_advancedSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_cookbook, container, false);


        /*btn_advancedSearch = view.findViewById(R.id.btn_advancedSearch);
        btn_advancedSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "This button works!!", Toast.LENGTH_SHORT).show();
            }
        });*/

        return view;
    }


    /**
     * more stuff needed by android to make the screen and connecting logic
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");



    }
}
