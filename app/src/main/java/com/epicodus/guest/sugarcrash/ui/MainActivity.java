package com.epicodus.guest.sugarcrash.ui;

import android.content.res.Configuration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.epicodus.guest.sugarcrash.Constants;
import com.epicodus.guest.sugarcrash.R;
import com.epicodus.guest.sugarcrash.models.SavedFood;
import com.epicodus.guest.sugarcrash.models.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.communication.IOnBarClickedListener;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.searchButton) Button mSearchButton;
    @Bind(R.id.upcButton) Button mUpcButton;
    @Bind(R.id.maxDaily) TextView mMaxDaily;
    private ValueEventListener mUserRefListener;
    private ValueEventListener mSavedFoodListener;
    private Firebase mUserRef;
    private Query mQuery;
    private Firebase mFirebaseSavedFoodRef;
    @Bind(R.id.welcomeTextView) TextView mWelcomeTextView;
    private int mOrientation;
    private Map<Integer, ArrayList> mFoodDataMap;
    private int[] mColorArray = {0xFF681dc0, 0xFF44bfeb, 0xFFf5b61a, 0xFF60b017, 0xFFeb5da7, 0xFFed393b, 0xFF10a8f1};
    @Bind(R.id.barchart) BarChart mBarChart;
    @Bind(R.id.piechart) PieChart mPieChart;
    private int mSelectedBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSearchButton.setOnClickListener(this);
        mUpcButton.setOnClickListener(this);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);
        mFirebaseSavedFoodRef = new Firebase(Constants.FIREBASE_URL_SAVEDFOOD);

        setUpFirebaseQuery();

        mWelcomeTextView.setTypeface(myCustomFont);
        mSearchButton.setTypeface(myCustomFont);
        mMaxDaily.setTypeface(myCustomFont);
        mUpcButton.setTypeface(myCustomFont);


        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mWelcomeTextView.setText(user.getName() + "'s Sugar");
                String userSex = user.getSex();
                if (user.findAge() <= 4 ) {
                    mMaxDaily.setText("Recommended Daily Sugar: 12.5g");
                }
                else if (user.findAge() <= 8){
                    mMaxDaily.setText("Recommended Daily Sugar: 16.7g");
                }
                else if (user.findAge() <= 12) {
                    mMaxDaily.setText("Recommended Daily Sugar: 21g");
                }
                else if (user.findAge() <= 16 ) {
                    mMaxDaily.setText("Recommended Daily Sugar: 25g");
                }
                else if (userSex.equals("male")) {
                    mMaxDaily.setText("Recommended Daily Sugar: 37.5g");
                }
                else {
                    mMaxDaily.setText("Recommended Daily Sugar: 25g");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mOrientation = this.getResources().getConfiguration().orientation;
        if(mOrientation == Configuration.ORIENTATION_LANDSCAPE){
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            getSupportActionBar().hide();
        }

        mBarChart.setOnBarClickedListener(new IOnBarClickedListener() {
            @Override
            public void onBarClicked(int _Position) {
                mSelectedBar = _Position;
                setUpPieChart();
            }
        });
    }

    private void setUpBarChart(){
        mSelectedBar = 6;
        Set dataDays = mFoodDataMap.keySet();
        if(dataDays.size() < 7){
            mSelectedBar = dataDays.size() - 1;
        }
        mBarChart.clearChart();
        Calendar c = Calendar.getInstance();
        for(int i = 7; i > 0 ; i--){
            int position = dataDays.size() - i;
            if(position >= 0){
                Integer today = (Integer) dataDays.toArray()[position];
                ArrayList<SavedFood> todaysFoods = mFoodDataMap.get(today);
                Integer year = Integer.parseInt(today.toString().substring(0, 4));
                Integer month = Integer.parseInt(today.toString().substring(4, 6)) - 1;
                Integer day = Integer.parseInt(today.toString().substring(6));
                c.set(year, month, day);
                double summedSugars = 0;
                for(SavedFood thisFood : todaysFoods){
                    summedSugars += thisFood.getSugars();
                }
                mBarChart.addBar(new BarModel(c.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) + "-" + c.get(Calendar.DATE), (float) summedSugars,  mColorArray[i-1]));
            }
        }

        mBarChart.startAnimation();
    }

    private void setUpPieChart(){
        mPieChart.clearChart();
        Calendar c = Calendar.getInstance();
        Set dataKeys = mFoodDataMap.keySet();
        int selectedPosition;
        if(dataKeys.size() > 6){
            int offset = mSelectedBar - 7;
            selectedPosition = dataKeys.size() + offset;
        } else {
            selectedPosition = mSelectedBar;
        }

        if(selectedPosition >= 0){
            Integer today = (Integer) dataKeys.toArray()[selectedPosition];
            ArrayList<SavedFood> selectedDayFoods = mFoodDataMap.get(today);
            Integer year = Integer.parseInt(today.toString().substring(0, 4));
            Integer month = Integer.parseInt(today.toString().substring(4, 6)) - 1;
            Integer day = Integer.parseInt(today.toString().substring(6));
            c.set(year, month, day);
            int i = 0;
            for(SavedFood thisFood : selectedDayFoods){
                String pieSliceLabel = thisFood.getBrandName() + " - " + thisFood.getItemName();
                if(pieSliceLabel.length() > 40){
                    pieSliceLabel = pieSliceLabel.substring(0, 40) + "...";
                }
                mPieChart.addPieSlice(new PieModel(pieSliceLabel, (float) thisFood.getSugars(), mColorArray[i % 7]));
                i++;
            }
        }
        mPieChart.startAnimation();
    }

    private void setUpFirebaseQuery() {
        String location = mFirebaseSavedFoodRef.toString();
        mQuery = new Firebase(location).child(mUId);
        mFoodDataMap = new TreeMap<>();
        mSavedFoodListener = mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot daySnapshot: dataSnapshot.getChildren()){
                    Integer eatenDate = Integer.parseInt(daySnapshot.getKey());
                    ArrayList<SavedFood> dbFoods = new ArrayList<>();
                    for (DataSnapshot foodSnapshot: daySnapshot.getChildren()){
                        SavedFood dbFood = foodSnapshot.getValue(SavedFood.class);
                        dbFoods.add(dbFood);
                    }
                    mFoodDataMap.put(eatenDate, dbFoods);
                }
                setUpBarChart();
                setUpPieChart();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.searchButton:
                showFoodSearchDialog();
                break;
            case R.id.upcButton:
                scanUPC();
                break;
            default:
                break;
        }

    }
}
