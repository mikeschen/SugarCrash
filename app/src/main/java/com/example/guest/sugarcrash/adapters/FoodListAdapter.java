package com.example.guest.sugarcrash.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guest.sugarcrash.R;
import com.example.guest.sugarcrash.models.Food;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 5/17/16.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder>{
    private ArrayList<Food> mFoods = new ArrayList<>();
    private Context mContext;

    public FoodListAdapter(Context context, ArrayList<Food> foods) {
        mContext = context;
        mFoods = foods;
    }

    @Override
    public FoodListAdapter.FoodViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        FoodViewHolder viewHolder = new FoodViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FoodListAdapter.FoodViewHolder holder, int position) {
        holder.bindFood(mFoods.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.brandNameTextView) TextView mBrandNameTextView;
        @Bind(R.id.foodNameTextView) TextView mFoodNameTextView;
        @Bind(R.id.sugarContentTextView) TextView mSugarContentTextView;
        @Bind(R.id.caloriesTextView) TextView mCaloriesTextView;
        @Bind(R.id.servingSizeTextView) TextView mServingSizeTextView;
        @Bind(R.id.servingsPerContainerTextView) TextView mServingsPerContainerTextView;
        @Bind(R.id.descriptionTextView) TextView mDescriptionTextView;
        private Context mContext;
        public Typeface myCustomFont;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindFood(Food food) {
            myCustomFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/SpicyRice-Regular.ttf");
            mBrandNameTextView.setText(food.getBrandName());
            mFoodNameTextView.setText(food.getItemName());
            mSugarContentTextView.setText("Sugars " + food.getSugars() + "g");
            mCaloriesTextView.setText("Calories " + food.getCalories());
            mServingSizeTextView.setText("Serving Size " +food.getServingSizeQuantity() + " " + food.getServingSizeUnit());
            mServingsPerContainerTextView.setText("Servings Per Container " + food.getServingsPerContainer());
            if(!food.getItemDescription().equals("null")){
                mDescriptionTextView.setText(food.getItemDescription());
            } else {
                mDescriptionTextView.setText("");
            }
            mBrandNameTextView.setTypeface(myCustomFont);
            mFoodNameTextView.setTypeface(myCustomFont);
        }
    }
}
