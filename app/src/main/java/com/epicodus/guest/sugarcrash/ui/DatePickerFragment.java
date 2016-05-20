package com.epicodus.guest.sugarcrash.ui;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


    public DatePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this, year, month, day);
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        return datePicker;
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        Intent intent = new Intent();
        intent.putExtra("new_day", day);
        intent.putExtra("new_month", month);
        intent.putExtra("new_year", year);
        getTargetFragment().onActivityResult(getTargetRequestCode(), 222, intent);
    }

}
