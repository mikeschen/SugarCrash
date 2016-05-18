package com.example.guest.sugarcrash.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import com.example.guest.sugarcrash.Constants;
import com.example.guest.sugarcrash.R;
import com.example.guest.sugarcrash.models.SavedFood;
import com.example.guest.sugarcrash.models.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.core.Context;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, SearchDialogFragment.SearchDialogFragmentListener {

    @Bind(R.id.searchButton) Button mSearchButton;
    @Bind(R.id.upcButton) Button mUpcButton;
    @Bind(R.id.maxDaily) TextView mMaxDaily;
    private ValueEventListener mUserRefListener;
    private ValueEventListener mSavedFoodListener;
    private Firebase mUserRef;
    private Query mQuery;
    private Firebase mFirebaseSavedFoodRef;
    @Bind(R.id.welcomeTextView) TextView mWelcomeTextView;
    private double x = 16.7;
    private int mOrientation;
    private Map<Integer, ArrayList> mFoodDataMap;
    private int[] mColorArray = {0xFF123456, 0xFF21166a, 0xFF563456, 0xFF873F56, 0xFF56B7F1, 0xFF343456, 0xFF1F04AC};

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
                mWelcomeTextView.setText(user.getName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("Login", "Read failed");
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
    }

    private void setUpBarChart(){
        Set dataDays = mFoodDataMap.keySet();
        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.clearChart();
        for(int i = 7; i > 0 ; i--){
            int position = dataDays.size() - i;
            Integer today = (Integer) dataDays.toArray()[position];
            if(today != null && today >= 0){
                ArrayList<SavedFood> todaysFoods = mFoodDataMap.get(today);
                double summedSugars = 0;
                for(SavedFood thisFood : todaysFoods){
                    summedSugars += thisFood.getSugars();
                }
                mBarChart.addBar(new BarModel(today.toString(), (float) summedSugars,  mColorArray[i-1]));
            }
        }

        mBarChart.startAnimation();
    }

    private void setUpPieChart(){
        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart.clearChart();

        mPieChart.addPieSlice(new PieModel("NameOfFood1", 10, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("NameOfFood2", 20, Color.parseColor("#FED70E")));

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
                Log.v("array keys", mFoodDataMap.keySet() + "");
                setUpBarChart();
                setUpPieChart();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("saved data", "Read failed");
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

    private void showFoodSearchDialog(){
        FragmentManager fm = getSupportFragmentManager();
        SearchDialogFragment searchFragment = SearchDialogFragment.newInstance("Input Search Term");
        searchFragment.setStyle(SearchDialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        searchFragment.show(fm, "fragment_search_dialog");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        addSearchTypeToSharedPreferences("string");
        Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
        intent.putExtra("inputText", inputText);
        startActivity(intent);
    }

    public void scanUPC(){
        addSearchTypeToSharedPreferences("upc");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a food barcode");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            String scanContent = scanningResult.getContents();
            Intent searchIntent = new Intent(MainActivity.this, SearchResultsActivity.class);
            searchIntent.putExtra("inputText", scanContent);
            startActivity(searchIntent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
