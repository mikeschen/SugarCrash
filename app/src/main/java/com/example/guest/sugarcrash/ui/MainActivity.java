package com.example.guest.sugarcrash.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.guest.sugarcrash.R;
import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, SearchDialogFragment.SearchDialogFragmentListener {
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView mImageView;
    private Bitmap mImageBitmap;
    @Bind(R.id.searchButton)
    Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSearchButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                onLaunchCamera();
            default:
                break;
        }
        return false;
    }
    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.searchButton:
                showFoodSearchDialog();
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
}
