package com.epicodus.guest.sugarcrash.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.guest.sugarcrash.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    @Bind(R.id.searchFoodEditText) EditText mSearchFoodEditText;

    public interface SearchDialogFragmentListener{
        void onFinishEditDialog(String inputText);
    }

    public SearchDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_dialog, container, false);
    }

    public static SearchDialogFragment newInstance(String title){
        SearchDialogFragment fragment = new SearchDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        String title = getArguments().getString("title", "Enter new search");
        getDialog().setTitle(title);
        mSearchFoodEditText.setOnEditorActionListener(this);
        mSearchFoodEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            SearchDialogFragmentListener listener = (SearchDialogFragmentListener) getActivity();
            listener.onFinishEditDialog(mSearchFoodEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }

}
