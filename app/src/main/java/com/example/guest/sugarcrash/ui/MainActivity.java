package com.example.guest.sugarcrash.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        Log.d("muid", mUserRef + "");
        mWelcomeTextView.setTypeface(myCustomFont);
        mSearchButton.setTypeface(myCustomFont);
        mMaxDaily.setTypeface(myCustomFont);
        mUpcButton.setTypeface(myCustomFont);

        setUpBarChart();
        setUpPieChart();

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
        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.addBar(new BarModel("Sun", (float) x, 0xFF123456));
        mBarChart.addBar(new BarModel("Mon", 8,  0xFF21166a));
        mBarChart.addBar(new BarModel("Tue", 3, 0xFF563456));
        mBarChart.addBar(new BarModel("Wed", 28, 0xFF873F56));
        mBarChart.addBar(new BarModel("Thur", 40, 0xFF56B7F1));
        mBarChart.addBar(new BarModel("Fri", 10,  0xFF343456));
        mBarChart.addBar(new BarModel("Sat", 4, 0xFF1F04AC));

        mBarChart.startAnimation();
    }

    private void setUpPieChart(){
        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("NameOfFood1", 10, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("NameOfFood2", 20, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();
    }

    private void setUpFirebaseQuery() {
        String location = mFirebaseSavedFoodRef.toString();
        mQuery = new Firebase(location).child(mUId);
        mFoodDataMap = new HashMap<>();
        Log.d("FIREBASS", location);
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
