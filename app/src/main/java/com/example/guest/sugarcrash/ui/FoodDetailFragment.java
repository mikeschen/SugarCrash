package com.example.guest.sugarcrash.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.R;
import com.example.guest.sugarcrash.models.Food;
import com.example.guest.sugarcrash.models.SavedFood;
import com.firebase.client.Firebase;

import org.parceler.Parcels;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodDetailFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.brandNameTextView) TextView mBrandNameTextView;
    @Bind(R.id.foodNameTextView) TextView mFoodNameTextView;
    @Bind(R.id.caloriesTextView) TextView mCaloriesTextView;
    @Bind(R.id.sugarTextView) TextView mSugarContentTextView;
    @Bind(R.id.servingsPerContainerTextView) TextView mServingsPerContainerTextView;
    @Bind(R.id.servingSizeTextView) TextView mServingSizeTextView;
    @Bind(R.id.datePickerButton) Button mDatePickerButton;
    @Bind(R.id.saveFoodButton) Button mSaveFoodButton;
    @Bind(R.id.servingsRadioGroup) RadioGroup mServingsRadioGroup;
    @Bind(R.id.oneRadio) RadioButton mOneRadio;
    @Bind(R.id.currentDateTextView) TextView mCurrentDateTextView;
    private double mNumberOfServings;
    private int mSelectedDate;
    private SimpleDateFormat mDatabaseDateFormatter;
    private SimpleDateFormat sdf;

    private Food mFood;

    public static FoodDetailFragment newInstance(Food food) {
        FoodDetailFragment foodDetailFragment = new FoodDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("food", Parcels.wrap(food));
        foodDetailFragment.setArguments(args);
        return foodDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFood = Parcels.unwrap(getArguments().getParcelable("food"));
    }

    public FoodDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_detail, container, false);
        ButterKnife.bind(this, view);
        mBrandNameTextView.setTypeface(myCustomFont);
        mFoodNameTextView.setTypeface(myCustomFont);
        mBrandNameTextView.setText(mFood.getBrandName());
        mFoodNameTextView.setText(mFood.getItemName());
        mCaloriesTextView.setText("Calories " + mFood.getCalories());
        mSugarContentTextView.setText("Sugars " + mFood.getSugars() + "g");
        mServingSizeTextView.setText("Serving Size " + mFood.getServingSizeQuantity() + " " + mFood.getServingSizeUnit());
        mServingsPerContainerTextView.setText("Servings Per Container " + mFood.getServingsPerContainer());

        mDatePickerButton.setOnClickListener(this);
        mSaveFoodButton.setOnClickListener(this);
        mServingsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
            // Check which radio button was clicked
            switch(i) {
                case R.id.halfRadio:
                    mNumberOfServings = 0.5;
                    break;
                case R.id.oneRadio:
                    mNumberOfServings = 1;
                    break;
                case R.id.twoRadio:
                    mNumberOfServings = 2;
                    break;
                case R.id.threeRadio:
                    mNumberOfServings = 3;
                    break;
                default:
                    break;
            }
            Log.v("clicked", "" + mNumberOfServings);
            }

        });

        mOneRadio.setChecked(true);
        Calendar c = Calendar.getInstance();
        sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        String formattedDate = sdf.format(c.getTime());
        mDatabaseDateFormatter = new SimpleDateFormat("yyyyMMdd");
        mSelectedDate = Integer.parseInt(mDatabaseDateFormatter.format(c.getTime()));
        Log.v("calendar time", c.getTime() + "");
        mCurrentDateTextView.setText(getResources().getString(R.string.dateConsumed) + formattedDate);
        return view;


    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.saveFoodButton:
                saveThisFood();
                break;
            case R.id.datePickerButton:
                showDatePickerDialog();
                break;
            default:
                break;
        }

    }

    public void showDatePickerDialog(){
        DialogFragment newDateFragment = new DatePickerFragment();
        newDateFragment.setTargetFragment(this, 222);
        newDateFragment.show(getFragmentManager(), "timePicker");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 222){
            Integer day = data.getIntExtra("new_day", 0);
            Integer month = data.getIntExtra("new_month", 0);
            Integer year = data.getIntExtra("new_year", 0);
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            mSelectedDate = Integer.parseInt(mDatabaseDateFormatter.format(c.getTime()));
            String formattedDate = sdf.format(c.getTime());
            mCurrentDateTextView.setText(getResources().getString(R.string.dateConsumed) + formattedDate);
            Log.v("date", mSelectedDate + "");
        }
    }

    public void saveThisFood(){
        String itemName = mFood.getItemName();
        String brandName = mFood.getBrandName();
        double sugars = mFood.getSugars();
        double mySugars = sugars * mNumberOfServings;
        SavedFood newSavedFood = new SavedFood(itemName, brandName, mySugars, mSelectedDate);

        Firebase userDateSavedFoodRef = new Firebase(Constants.FIREBASE_URL_SAVEDFOOD).child(mUId).child(mSelectedDate + "");
        Firebase pushRef = userDateSavedFoodRef.push();
        String savedFoodPushId = pushRef.getKey();
        newSavedFood.setPushId(savedFoodPushId);
        pushRef.setValue(newSavedFood);
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

}
