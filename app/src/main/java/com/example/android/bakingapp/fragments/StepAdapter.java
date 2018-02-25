package com.example.android.bakingapp.fragments;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the Recyclerview Adapater for the Stepdetail Fragment.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private static final String TAG = StepAdapter.class.getSimpleName();

    private ArrayList<Step> mSteps;
    private Context mContext;

    private final StepAdapaterOnClickHandler mClickHandler;

    public interface StepAdapaterOnClickHandler {
        void onClick(int position);
    }

    public StepAdapter(Context context, StepAdapaterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.cv_step_card) CardView mStepCard;
        @BindView(R.id.tv_step_number) TextView mStepNumber;
        @BindView(R.id.tv_step_short_descrip) TextView mShortDescription;

        public StepAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPostion = getAdapterPosition();
            Step step = new Step(
                    mSteps.get(adapterPostion).getStepNum(),
                    mSteps.get(adapterPostion).getShortDescription(),
                    mSteps.get(adapterPostion).getDescription(),
                    mSteps.get(adapterPostion).getVideoURL(),
                    mSteps.get(adapterPostion).getThumbNameURL());
            mClickHandler.onClick(adapterPostion);
        }
    }

    @Override
    public StepAdapter.StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_list_item, parent, false);
        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepAdapterViewHolder viewHolder, int position) {

        viewHolder.mStepNumber.setText(String.valueOf(mSteps.get(position).getStepNum()));
        viewHolder.mShortDescription.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }
        return mSteps.size();
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }
}
