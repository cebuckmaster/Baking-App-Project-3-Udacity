package com.example.android.bakingapp.fragments;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.data.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This the StepDetailFragment used to display the ExoPlayer, the Detail step instruction
 * and for phones allows the users to hit previouse/next buttons to jump though steps
 */

public class StepDetailFragment extends Fragment {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullScreen";
    private final String STATE_STEPS = "steps";
    private final String STATE_CURRENT_STEP = "currentStep";
    private final String STATE_CURRENT_STEP_NUM = "currentStepNum";

    private Step mDisplayStep;
    private ArrayList<Step> mSteps;


    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
    private SimpleExoPlayer mExoPlayer;

    private boolean mVideoFound;

    private int mResumeWindow;
    private long mResumePostion;

    private View mView;

    private int currentStepNum;
    private boolean mTwoPane;

    @BindView(R.id.btn_previous_step) Button mPreviousButton;
    @BindView(R.id.btn_next_step) Button mNextButton;
    @BindView(R.id.main_media_frame) FrameLayout mMediaFrameLayout;
    @BindView(R.id.exo_media_player) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.tv_no_video_message) TextView mNoVideoMessage;
    @BindView(R.id.tv_step_description) TextView mStepDescription;
    @BindView(R.id.iv_step_img) ImageView mThumbnailImg;
    @BindView(R.id.thumbnail_divider) View mViewThumnailDivider;

    //required constructor for the fragment
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, mView);

        mTwoPane = getActivity().getResources().getBoolean(R.bool.is_two_pane);

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STATE_STEPS);
            mDisplayStep = savedInstanceState.getParcelable(STATE_CURRENT_STEP);
            currentStepNum = savedInstanceState.getInt(STATE_CURRENT_STEP_NUM);
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePostion = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }


        displayThumbNailImage();


        mVideoFound = false;
        if (mTwoPane) {
            mPreviousButton.setVisibility(View.GONE);
            mNextButton.setVisibility(View.GONE);
        } else {

            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentStepNum == 0) {
                        return;
                    }
                    currentStepNum--;
                    mDisplayStep = mSteps.get(currentStepNum);
                    clearResumePosition();
                    releasePlayer();
                    initExoPlayer();
                    displayThumbNailImage();
                    showDescription();
                }
            });
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentStepNum >= (mSteps.size()-1)) {
                        return;
                    }
                    currentStepNum++;
                    mDisplayStep = mSteps.get(currentStepNum);
                    clearResumePosition();
                    releasePlayer();
                    initExoPlayer();
                    displayThumbNailImage();
                    showDescription();
                }
            });
        }

        setupPlayer();
        initExoPlayer();
        showDescription();

        int orientation= getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && !mTwoPane) {
            if (mVideoFound) {
                openFullscreenDialog();
            }
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mVideoFound) {
                closeFullscreenDialog();
            }

        }

        return mView;
    }

    public void setCurrentStep(Step step) {
        mDisplayStep = step;
        currentStepNum = mDisplayStep.getStepNum();
    }

    public void displayThumbNailImage() {
        if (!TextUtils.isEmpty(mDisplayStep.getThumbNameURL())) {
            mThumbnailImg.setVisibility(View.VISIBLE);
            mViewThumnailDivider.setVisibility(View.VISIBLE);
            Picasso.with(mView.getContext()).load(mDisplayStep.getThumbNameURL()).into(mThumbnailImg);
        } else {
            mThumbnailImg.setVisibility(View.GONE);
            mViewThumnailDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_STEPS, mSteps);
        outState.putParcelable(STATE_CURRENT_STEP, mDisplayStep);
        outState.putInt(STATE_CURRENT_STEP_NUM, currentStepNum);
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePostion);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
    }


    private void setupPlayer() {
        initFullscreenDialog();
        initFullscreenButton();
    }


    //This is the inital method to setup the exoplayer in full screen mode.
    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen) {
                    closeFullscreenDialog();
                }
                super.onBackPressed();
            }
        };
    }

    //this is used to display the Exoplayer in fullscreen mode.
    private void openFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    //this is used to close the Exoplayer out of fullscreen mode
    private void closeFullscreenDialog() {
        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mMediaFrameLayout.addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    //this setup the lower right hand corner buttons on the Exoplayer to allow the users to go in and out of full screen mode.
    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mExoPlayerFullscreen){
                    openFullscreenDialog();
                } else {
                    closeFullscreenDialog();
                }
            }
        });
    }

    //This method setup the Exoplayer and prepares the video to be played.
    private void initExoPlayer() {

        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);

            if (TextUtils.isEmpty(mDisplayStep.getVideoURL())) {
                mVideoFound = false;
                noVideoFound();
            } else {
                mVideoFound = true;
                videoFound();
                boolean haveResumePostion = mResumeWindow != C.INDEX_UNSET;
                if (haveResumePostion) {
                    mExoPlayer.seekTo(mResumeWindow, mResumePostion);
                }
                String userAgent = Util.getUserAgent(getContext(), getContext().getApplicationContext().getApplicationInfo().packageName);
                Uri videoUri = Uri.parse(mDisplayStep.getVideoURL()).buildUpon().build();
                mVideoSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mVideoSource, !haveResumePostion, false);
                mExoPlayer.setPlayWhenReady(true);
            }
        }
    }


    private void showDescription() {
        if (mDisplayStep.getDescription() != null) {
            mStepDescription.setText(mDisplayStep.getDescription());
        } else {
            mStepDescription.setText(getResources().getString(R.string.no_description_found));
        }
    }

    private void videoFound() {
        mMediaFrameLayout.setVisibility(View.VISIBLE);
        mNoVideoMessage.setVisibility(View.GONE);

    }

    private void noVideoFound() {
        mMediaFrameLayout.setVisibility(View.GONE);
        mNoVideoMessage.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null) {
            initExoPlayer();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void updateResumePosition() {
        mResumeWindow = mExoPlayer.getCurrentWindowIndex();
        mResumePostion = mExoPlayer.isCurrentWindowSeekable() ? Math.max(0, mExoPlayer.getContentPosition()) : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        mResumeWindow = C.INDEX_UNSET;
        mResumePostion = C.TIME_UNSET;
    }

    //This release the exoplayer resources.
    private void releasePlayer() {
        if (mExoPlayer != null) {
            updateResumePosition();
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mFullScreenDialog != null) {
            mFullScreenDialog.dismiss();
        }
    }

}