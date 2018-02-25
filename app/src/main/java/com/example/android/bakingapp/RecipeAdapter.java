package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * This is the RecyclerView to layout the Recipe Cards for the MainBakingActivity
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private ArrayList<Recipe> mRecipes;
    private Context mContext;

    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView mCardView;
        public TextView mRecipeName;
        public TextView mServings;

        public RecipeAdapterViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.cv_recipe_card);
            mRecipeName = (TextView) view.findViewById(R.id.tv_recipe_name);
            mServings = (TextView) view.findViewById(R.id.tv_recipe_serving_size);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = new Recipe(
                    mRecipes.get(adapterPosition).getName(),
                    mRecipes.get(adapterPosition).getIngredients(),
                    mRecipes.get(adapterPosition).getSteps(),
                    mRecipes.get(adapterPosition).getServingSize(),
                    mRecipes.get(adapterPosition).getImage());
            mClickHandler.onClick(recipe);

        }
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIDForGridItem = R.layout.recipe_card_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIDForGridItem, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder viewHolder, int position) {

        viewHolder.mRecipeName.setText(mRecipes.get(position).getName());
        viewHolder.mServings.setText(String.valueOf(mRecipes.get(position).getServingSize()));


    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

}
