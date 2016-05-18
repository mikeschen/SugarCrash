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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, SearchDialogFragment.SearchDialogFragmentListener {
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    @Bind(R.id.searchButton) Button mSearchButton;
    @Bind(R.id.upcButton) Button mUpcButton;
    @Bind(R.id.maxDaily) TextView mMaxDaily;
    private ValueEventListener mUserRefListener;
    private Firebase mUserRef;
    private Query mQuery;
    private Firebase mFirebaseSavedFoodRef;
    private String mUId;
    @Bind(R.id.welcomeTextView) TextView mWelcomeTextView;
    private double x = 16.7;
    private int mOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSearchButton.setOnClickListener(this);
        mUpcButton.setOnClickListener(this);
        mUId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mUId);
        mFirebaseSavedFoodRef = new Firebase(Constants.FIREBASE_URL_SAVEDFOOD);

        setUpFirebaseQuery();

        Log.d("muid", mUserRef + "");
        mWelcomeTextView.setTypeface(myCustomFont);
        mSearchButton.setTypeface(myCustomFont);
        mMaxDaily.setTypeface(myCustomFont);
        mUpcButton.setTypeface(myCustomFont);

        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.addBar(new BarModel("Sun", (float) x, 0xFF123456));
        mBarChart.addBar(new BarModel("Mon", 8,  0xFF21166a));
        mBarChart.addBar(new BarModel("Tue", 3, 0xFF563456));
        mBarChart.addBar(new BarModel("Wed", 28, 0xFF873F56));
        mBarChart.addBar(new BarModel("Thur", 40, 0xFF56B7F1));
        mBarChart.addBar(new BarModel("Fri", 10,  0xFF343456));
        mBarChart.addBar(new BarModel("Sat", 4, 0xFF1F04AC));

        mBarChart.startAnimation();

        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);

        mPieChart.addPieSlice(new PieModel("NameOfFood1", 10, Color.parseColor("#56B7F1")));
        mPieChart.addPieSlice(new PieModel("NameOfFood2", 20, Color.parseColor("#FED70E")));

        mPieChart.startAnimation();


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

    private void setUpFirebaseQuery() {
        String location = mFirebaseSavedFoodRef.toString();
        mQuery = new Firebase(location);
        Log.d("FIREBASS", location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
        }
        return false;
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
    protected void logout() {
        mFirebaseRef.unauth();
        takeUserToLoginScreenOnUnAuth();
    }
    private void takeUserToLoginScreenOnUnAuth() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
