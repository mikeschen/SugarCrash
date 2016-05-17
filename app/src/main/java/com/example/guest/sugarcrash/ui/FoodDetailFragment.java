package com.example.guest.sugarcrash.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.sugarcrash.R;
import com.example.guest.sugarcrash.models.Food;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodDetailFragment extends Fragment {
    @Bind(R.id.brandNameTextView) TextView mBrandNameTextView;
    @Bind(R.id.foodNameTextView) TextView mFoodNameTextView;
    @Bind(R.id.caloriesTextView) TextView mCaloriesTextView;
    @Bind(R.id.sugarTextView) TextView mSugarContentTextView;
    @Bind(R.id.servingsPerContainerTextView) TextView mServingsPerContainerTextView;
    @Bind(R.id.servingSizeTextView) TextView mServingSizeTextView;

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

        mBrandNameTextView.setText(mFood.getBrandName());
        mFoodNameTextView.setText(mFood.getItemName());
        mCaloriesTextView.setText("Calories " + mFood.getCalories());
        mSugarContentTextView.setText("Sugars " + mFood.getSugars() + "g");
        mServingSizeTextView.setText("Serving Size " + mFood.getServingSizeQuantity() + " " + mFood.getServingSizeUnit());
        mServingsPerContainerTextView.setText("Servings Per Container " + mFood.getServingsPerContainer());
        return view;
    }

}
