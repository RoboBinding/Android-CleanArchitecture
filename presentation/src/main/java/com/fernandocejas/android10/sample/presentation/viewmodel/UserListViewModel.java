package com.fernandocejas.android10.sample.presentation.viewmodel;

import com.fernandocejas.android10.sample.domain.User;
import com.fernandocejas.android10.sample.domain.exception.DefaultErrorBundle;
import com.fernandocejas.android10.sample.domain.exception.ErrorBundle;
import com.fernandocejas.android10.sample.domain.interactor.DefaultSubscriber;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.exception.ErrorMessageFactory;
import com.fernandocejas.android10.sample.presentation.mapper.UserModelDataMapper;
import com.fernandocejas.android10.sample.presentation.model.UserModel;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.robobinding.widget.adapterview.ItemClickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cheng Wei on 2014/9/29.
 */
@PresentationModel
public class UserListViewModel implements HasPresentationModelChangeSupport {
    private final UserListView userListView;
    private final UseCase getUserListUseCase;
    private final UserModelDataMapper userModelDataMapper;

    private boolean retryVisible;
    private List<UserModel> userModels;

    private final PresentationModelChangeSupport changeSupport;

    public UserListViewModel(UserListView userListView, UseCase getUserListUserCase,
                             UserModelDataMapper userModelDataMapper) {
        this.userListView = userListView;
        this.getUserListUseCase = getUserListUserCase;
        this.userModelDataMapper = userModelDataMapper;

        userModels = Collections.emptyList();
        changeSupport = new PresentationModelChangeSupport(this);
    }

    /**
     * Initializes the presenter by start retrieving the user list.
     */
    public void initialize() {
        loadUserList();
    }

    /**
     * Loads all users.
     */
    private void loadUserList() {
        hideViewRetry();
        showViewLoading();
        getUserList();
    }

    private void hideViewRetry() {
        setRetryVisible(false);
    }

    private void setRetryVisible(boolean retryVisible) {
        this.retryVisible = retryVisible;
        changeSupport.firePropertyChange("retryVisible");
    }

    public boolean isRetryVisible() {
        return retryVisible;
    }

    private void showViewLoading() {
        this.userListView.showLoading();
    }

    private void getUserList() {
        this.getUserListUseCase.execute(new UserListSubscriber());
    }

    private final class UserListSubscriber extends DefaultSubscriber<List<User>> {

        @Override public void onCompleted() {
            UserListViewModel.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            UserListViewModel.this.hideViewLoading();
            UserListViewModel.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            UserListViewModel.this.showViewRetry();
        }

        @Override public void onNext(List<User> users) {
            UserListViewModel.this.showUsersCollectionInView(users);
        }
    }

    private void showUsersCollectionInView(Collection<User> usersCollection) {
        final Collection<UserModel> userModelsCollection =
                this.userModelDataMapper.transform(usersCollection);
        renderUserList(userModelsCollection);
    }

    private void renderUserList(Collection<UserModel> newUserModels) {
        userModels = new ArrayList<UserModel>(newUserModels);
        changeSupport.firePropertyChange("userModels");
    }

    @ItemPresentationModel(value = UserItemViewModel.class)
    public List<UserModel> getUserModels() {
        return userModels;
    }

    private void hideViewLoading() {
        this.userListView.hideLoading();
    }

//    private void showErrorMessage(ErrorBundle errorBundle) {
//        this.userListView.showError(errorBundle);
//    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.userListView.getContext(),
                errorBundle.getException());
        this.userListView.showError(errorMessage);
    }

    private void showViewRetry() {
        setRetryVisible(true);
    }

    public void onUserClicked(ItemClickEvent event) {
        UserModel userModel = userModels.get(event.getPosition());
        this.userListView.viewUser(userModel);
    }

    public void onRetryClicked() {
        loadUserList();
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }
}
