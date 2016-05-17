package com.example.guest.sugarcrash.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.guest.sugarcrash.R;
import com.example.guest.sugarcrash.adapters.FoodPagerAdapter;
import com.example.guest.sugarcrash.models.Food;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FoodDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager) ViewPager mViewPager;
    private FoodPagerAdapter adapterViewPager;
    ArrayList<Food> mFoods = new ArrayList<>();
    private double mNumberOfServings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);
        mFoods = Parcels.unwrap(getIntent().getParcelableExtra("foods"));
        int startingPosition = Integer.parseInt(getIntent().getStringExtra("position"));
        adapterViewPager = new FoodPagerAdapter(getSupportFragmentManager(), mFoods);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.halfRadio:
                if (checked)
                    mNumberOfServings = 0.5;
                break;
            case R.id.oneRadio:
                if (checked)
                    mNumberOfServings = 1;
                break;
            case R.id.twoRadio:
                if (checked)
                    mNumberOfServings = 2;
                break;
            case R.id.threeRadio:
                if (checked)
                    mNumberOfServings = 3;
                break;
            default:
                break;
        }
        Log.v("clicked", "" + mNumberOfServings);
    }
}
