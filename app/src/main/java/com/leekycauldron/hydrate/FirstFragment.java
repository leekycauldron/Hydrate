package com.leekycauldron.hydrate;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FirstFragment extends Fragment {

    EditText water;
    TextView goalView;
    ProgressBar waterProgress;
    int waterAmt = 0;
    final int DAILY_MAX = 30000;
    int DAILY_GOAL = 3700;
    String savedWater;
    Date currentTime = Calendar.getInstance().getTime();
    String currentDay = String.valueOf(currentTime.getDate());

    public void writeToFile(String data) {
        try {
            Log.d("test",getContext().getFilesDir().toString());
            File file = new File(getContext().getFilesDir(),"hydrate.txt");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.d("FILE","Written.");
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }

    public String readFile() throws IOException {
        FileInputStream fis = getContext().openFileInput("hydrate.txt");
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if (!((line = bufferedReader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
        isr.close();
        String parts[] = String.valueOf(sb).split(",");
        Log.d("D",parts[0]);
        if (Integer.parseInt(currentDay) != Integer.parseInt(parts[1])) {
            Log.d("f","Day changed..."+currentDay+parts[1]);
            // Delete/Clear File.
            writeToFile("0,"+currentDay);
            return "0";
        }
        Log.d("f","Ok");
        return parts[0].toString();
        
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

        water = view.findViewById(R.id.waterToAdd);
        TextView waterView = view.findViewById(R.id.waterView);
        goalView = view.findViewById(R.id.goalView);
        waterProgress = view.findViewById(R.id.waterProgress);


        // Retrieve water data, if available.
        try {
            savedWater = readFile();
            Log.d("HI","File Read: " + savedWater);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedWater != null) waterAmt = Integer.parseInt(savedWater);
        waterView.setText(String.format("%sml",waterAmt));

        // Set Progress Bar
        waterProgress.setMax(DAILY_GOAL);
        waterProgress.setProgress(waterAmt);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {

            final String text = "That is too much water!";
            final int duration = Toast.LENGTH_SHORT;
            final Toast overLimitToast = Toast.makeText(getContext(), text, duration);

            @Override
            public void onClick(View view) {
                try{
                if (waterAmt + Integer.parseInt(water.getText().toString()) > DAILY_MAX) {
                    overLimitToast.show();
                    return;
                }} catch (Exception e) {return;}
                waterAmt += Integer.parseInt(water.getText().toString());
                writeToFile(String.valueOf(waterAmt)+","+currentDay);
                Log.d("DBG","Added " + waterAmt  + "ml of water.");
                waterProgress.setProgress(waterAmt);
                waterView.setText(String.format("%sml",waterAmt));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}