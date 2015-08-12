/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerUserComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.UserModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ViewModelModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ViewModule;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserDetailsView;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserDetailsViewModel;

import org.robobinding.ViewBinder;

import javax.inject.Inject;

/**
 * Fragment that shows details of a certain user.
 */
public class UserDetailsFragment extends BaseFragment implements UserDetailsView {

    private static final String ARGUMENT_KEY_USER_ID = "org.android10.ARGUMENT_USER_ID";

    private int userId;

    @Inject
    UserDetailsViewModel viewModel;

    private RelativeLayout rl_progress;

    public UserDetailsFragment() {
        super();
    }

    public static UserDetailsFragment newInstance(int userId) {
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(ARGUMENT_KEY_USER_ID, userId);
        userDetailsFragment.setArguments(argumentsBundle);

        return userDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewBinder viewBinder = createViewBinder(false);
        View fragmentView = viewBinder.inflateAndBindWithoutAttachingToRoot(R.layout.fragment_user_details, viewModel, container);

        this.rl_progress = (RelativeLayout) fragmentView.findViewById(R.id.rl_progress);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.viewModel.initialize(this.userId);
    }

    private UserComponent userComponent;
    private void initialize() {
        this.userId = getArguments().getInt(ARGUMENT_KEY_USER_ID);

        this.userComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .viewModelModule(new ViewModelModule())
                .userModule(new UserModule(userId))
                .viewModule(new ViewModule(this))
                .build();

        userComponent.inject(this);
    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }
}
