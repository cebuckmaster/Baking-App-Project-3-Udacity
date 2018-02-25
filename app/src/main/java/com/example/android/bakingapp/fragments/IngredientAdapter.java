package com.example.android.bakingapp.fragments;

 import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Ingredient;

import java.util.ArrayList;

 import butterknife.BindView;
 import butterknife.ButterKnife;

/**
 * This is the Recyclerview Adapter for the Ingredient Fragment.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private static final String TAG = IngredientAdapter.class.getSimpleName();
    private ArrayList<Ingredient> mIngredients;
    private Context mContext;

    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient_quantity) TextView mIngredientQuantity;
        @BindView(R.id.tv_measurement_type) TextView mMeasureType;
        @BindView(R.id.tv_recipe_ingredient) TextView mRecipeIngredient;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View ingredientView = inflater.inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(ingredientView);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientViewHolder holder, int position) {

        holder.mIngredientQuantity.setText(String.valueOf(mIngredients.get(position).getQuantity()));
        holder.mMeasureType.setText(mIngredients.get(position).getMeasurementType());
        holder.mRecipeIngredient.setText(mIngredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        if(mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }
}
