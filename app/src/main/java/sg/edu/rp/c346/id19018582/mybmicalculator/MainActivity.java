package sg.edu.rp.c346.id19018582.mybmicalculator;

import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etWeight;
    EditText etHeight;
    Button btnCalculate;
    Button btnResetData;
    TextView tvDate;
    TextView tvBMI;
    TextView tvStat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        btnCalculate = findViewById(R.id.buttonCalculate);
        btnResetData = findViewById(R.id.buttonResetData);
        tvDate = findViewById(R.id.textViewDate);
        tvBMI = findViewById(R.id.textViewBMI);
        tvStat = findViewById(R.id.textViewStatus);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                float weight = Float.parseFloat(etWeight.getText().toString());
                float height = Float.parseFloat(etHeight.getText().toString());
                float BMI = (weight/(height*height));

                Calendar now = Calendar.getInstance();
                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        (now.get(Calendar.MONTH)+1) + "/" +
                        now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);

                tvDate.setText(datetime);
                tvBMI.setText(String.format("%.3f", BMI));
                etWeight.setText("");
                etHeight.setText("");

                //Enhancement
                if(BMI<18.5){
                    tvStat.setText("You are underweight");
                }
                else if(BMI>= 18.5 && BMI<= 24.9){
                    tvStat.setText("Your BMI is normal");
                }
                else if(BMI>=25 && BMI<= 29.9){
                    tvStat.setText("Your are overweight");
                }
                else if(BMI > 30){
                    tvStat.setText("Your should start doing exercise");
                }
                else{
                    tvStat.setText("Invalid");
                }

            }
        });

        btnResetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDate.setText("Last Calculated Date:");
                tvBMI.setText("Last Calculated BMI:");
                tvStat.setText("");
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = pref.edit();
                prefEdit.clear();
                prefEdit.commit();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();

        //Get the user input from the EditText and store it in a variable
        String datetime = tvDate.getText().toString();
        float BMI = Float.parseFloat(tvBMI.getText().toString());
        String stat = tvStat.getText().toString();

        //Obtain an instance of the SharedPreferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        //Obtain an instance of the SharedPreferences Editor for update later
        SharedPreferences.Editor prefEdit = pref.edit();

        //Add the key_value pair
        prefEdit.putString("date", datetime);
        prefEdit.putFloat("BMI", BMI);
        prefEdit.putString("stat", stat);

        //Call commit() to save changes into Shared preferences
        prefEdit.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Obtain instance of the SharedPreferences
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        //Retrieve the saved data from the SharedPreferences object
        String datetime = pref.getString("date", "");
        float BMI = pref.getFloat("BMI", 0.0f);
        String stat = pref.getString("stat", "");

        //Update the UI element with the value
        tvDate.setText("Last Calculated Date:" + datetime);
        tvBMI.setText("Last Calculated BMI:" +BMI+"");
        tvStat.setText(stat);
    }
}
