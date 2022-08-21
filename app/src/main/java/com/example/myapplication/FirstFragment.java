package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    EditText water;
    int waterAmt = 0;
    final int DAILY_MAX = 30000;




    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {



        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_first,container,false);
        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        water = view.findViewById(R.id.waterAmt);
        TextView waterView = view.findViewById(R.id.waterView);

        waterView.setText(String.format("Today: %sml",waterAmt));


        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {

            String text = "That is too much water!";
            int duration = Toast.LENGTH_SHORT;
            Toast overLimitToast = Toast.makeText(getContext(), text, duration);

            @Override
            public void onClick(View view) {
                if (waterAmt + Integer.parseInt(water.getText().toString()) > DAILY_MAX) {
                    overLimitToast.show();
                    return;
                }
                waterAmt += Integer.parseInt(water.getText().toString());
                Log.d("DBG","Added " + waterAmt  + "ml of water.");

                waterView.setText(String.format("Today: %sml",waterAmt));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}