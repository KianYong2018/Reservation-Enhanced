package sg.edu.rp.c346.reservation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    TextView txName;
    EditText editTextName;
    TextView txNumber;
    EditText editTextNumber;
    TextView txGroup;
    EditText editTextGroup;
    CheckBox cbNonSmoking;
    Button buttonReservation;
    Button buttonReset;

    EditText etDay;
    EditText etTime;

    String cb;
    String time;
    String date;

    DatePicker datepicker;
    TimePicker timepicker;

    boolean check = false;
    boolean reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txName = findViewById(R.id.txName);
        editTextName = findViewById(R.id.editTextName);
        txNumber = findViewById(R.id.txNumber);
        editTextNumber = findViewById(R.id.editTextNumber);
        txGroup = findViewById(R.id.txGroup);
        editTextGroup = findViewById(R.id.editTextGroup);
        cbNonSmoking = findViewById(R.id.cbNonSmoking);
        buttonReservation = findViewById(R.id.buttonReservation);
        buttonReset = findViewById(R.id.buttonReset);

        etDay = findViewById(R.id.editTextDay);
        etTime = findViewById(R.id.editTextTime);

        datepicker = new DatePicker(MainActivity.this);
        timepicker = new TimePicker(MainActivity.this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        date = prefs.getString("Date", "No date found");
        time = prefs.getString("Time", "No Time found");

        if (date.equalsIgnoreCase("No date found") || time.equalsIgnoreCase("No Time found")) {
            etDay.setText("Date: " + String.valueOf(datepicker.getDayOfMonth()) + "/" + String.valueOf(datepicker.getMonth()+1) + "/" + String.valueOf(datepicker.getYear()));
            etTime.setText("Time: " + String.valueOf(timepicker.getCurrentHour()) + ":" + String.valueOf(timepicker.getCurrentMinute()));
        } else {
            etDay.setText(date);
            etTime.setText(time);
            check = true;
        }

        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        etDay.setText("Date: " + String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year));
                    }
                };

                //Create Date Picker Dialog
                DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this, myDateListener, datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());

                if (check == true) {
                    String[] date_split = date.split("/");
                    myDateDialog.updateDate(Integer.valueOf(date_split[2]), Integer.valueOf(date_split[1])-1, Integer.valueOf(date_split[0].substring(5).trim()));
                }
                if (reset == true) {
                    myDateDialog.updateDate(datepicker.getYear(),datepicker.getMonth(),datepicker.getDayOfMonth());
                    reset = false;
                }
                myDateDialog.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDays, int minute) {
                        etTime.setText("Time: " + String.valueOf(hourOfDays) + ":" + String.valueOf(minute));
                    }
                };

                TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this, myTimeListener, timepicker.getCurrentHour(), timepicker.getCurrentMinute(), true);

                //Create Time Picker Dialog
                if (check == true) {
                    String[] time_split = time.split(":");
                    myTimeDialog.updateTime(Integer.parseInt(time_split[1].trim()), Integer.parseInt(time_split[2]));
                }
                if (reset == true) {
                    myTimeDialog.updateTime(timepicker.getCurrentHour(), timepicker.getCurrentMinute());
                }
                myTimeDialog.show();
            }
        });

        buttonReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextGroup.getText().toString().equals("") || editTextName.getText().toString().equals("") || editTextNumber.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please do not leave any blanks", Toast.LENGTH_SHORT).show();
                } else {
                    if (cbNonSmoking.isChecked()) {
                        cb = "Yes";
                    } else {
                        cb = "No";
                    }
                }
                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                myBuilder.setTitle("Confirm Your Order");
                myBuilder.setMessage("New Reservation\nName: " + editTextName.getText().toString() + "\nSmoking: " + cb + "\nSize: " + editTextGroup.getText().toString() + "\nDate: " + etDay.getText().toString() + "\nTime: " + etTime.getText().toString());

                myBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor prefsEdit = prefs.edit();
                        prefsEdit.putString("Date", etDay.getText().toString());
                        prefsEdit.putString("Time", etTime.getText().toString());
                        prefsEdit.commit();

                        Toast.makeText(getBaseContext(), "stored", Toast.LENGTH_SHORT).show();
                    }
                });

                myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Resetting", Toast.LENGTH_SHORT).show();
                editTextName.setText(null);
                editTextNumber.setText(null);
                editTextGroup.setText(null);
                cbNonSmoking.setChecked(false);
                reset = true;
                TimePicker times = new TimePicker(MainActivity.this);
                etTime.setText("Time: " + String.valueOf(times.getCurrentHour()) + ":" + String.valueOf(times.getCurrentMinute()));
                etDay.setText("Date: " + String.valueOf(datepicker.getDayOfMonth()) + "/" + String.valueOf(datepicker.getMonth() + 1) + "/" + String.valueOf(datepicker.getYear()));
            }
        });
    }
}