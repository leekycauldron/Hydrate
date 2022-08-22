package com.example.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;



import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.File;

public class FirstFragment extends Fragment {

    EditText water;
    int waterAmt = 0;
    final int DAILY_MAX = 30000;

    public void writeToFile(String data) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"hydrate.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d("FILE","Written.");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_first,container,false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);

        water = view.findViewById(R.id.waterAmt);
        TextView waterView = view.findViewById(R.id.waterView);

        waterView.setText(String.format("Today: %sml",waterAmt));


        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {

            final String text = "That is too much water!";
            final int duration = Toast.LENGTH_SHORT;
            final Toast overLimitToast = Toast.makeText(getContext(), text, duration);

            @Override
            public void onClick(View view) {
                if (waterAmt + Integer.parseInt(water.getText().toString()) > DAILY_MAX) {
                    overLimitToast.show();
                    return;
                }
                waterAmt += Integer.parseInt(water.getText().toString());
                writeToFile(String.valueOf(waterAmt));
                Log.d("DBG","Added " + waterAmt  + "ml of water.");

                waterView.setText(String.format("Today: %sml",waterAmt));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}