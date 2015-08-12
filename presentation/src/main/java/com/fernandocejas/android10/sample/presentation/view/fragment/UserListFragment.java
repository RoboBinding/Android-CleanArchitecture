/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerUserComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ViewModelModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ViewModule;
import com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserListView;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserListViewModel;

import org.robobinding.ViewBinder;

import javax.inject.Inject;

/**
 * Fragment that shows a list of Users.
 */
public class UserListFragment extends BaseFragment implements UserListView {

    @Inject
    UserListViewModel viewModel;

    /**
     * Interface for listening user list events.
     */
    public interface UserListListener {
        void onUserClicked(final UserModel userModel);
    }


    private RelativeLayout rl_progress;

    private UserListListener userListListener;

    public UserListFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UserListListener) {
            this.userListListener = (UserListListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewBinder viewBinder = createViewBinder(true);
        View fragmentView = viewBinder.inflateAndBind(R.layout.fragment_user_list, viewModel);
        this.rl_progress = (RelativeLayout) fragmentView.findViewById(R.id.rl_progress);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.viewModel.initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
//    this.userListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
//    this.userListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//    this.userListPresenter.destroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private UserComponent userComponent;

    private void initialize() {
        this.userComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .viewModelModule(new ViewModelModule())
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
    public void viewUser(UserModel userModel) {
        if (this.userListListener != null) {
            this.userListListener.onUserClicked(userModel);
        }
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }
}
