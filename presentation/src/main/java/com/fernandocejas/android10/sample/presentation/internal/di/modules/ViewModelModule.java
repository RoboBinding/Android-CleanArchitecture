package com.fernandocejas.android10.sample.presentation.internal.di.modules;
/**
 * copyright (C) 2014 form Voviv Tech (Shenzhen) Ltd science and technology projects
 * <p>
 * you must abide by the form of mountain technology license.
 * you can get a copy of the license
 * <p>
 * http://www.voviv.com/
 * <p>
 * unless applicable law requires or written consent of the software
 * license released under distributed on an "as is" basis,
 * without any form of guarantee or conditions, express or implied.
 * view license management authority and specific language
 * under the limit of permission.
 */

import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.mapper.UserModelDataMapper;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserDetailsView;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserDetailsViewModel;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserListView;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserListViewModel;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Title:ViewModelModule
 * Description:
 * User:  Hunter
 * Author: 2015-08-12 
 * Create Time: 11:50 
 * Modify the person:  
 * Modified: 
 * Modify Note:
 * @version
 */
@Module
public class ViewModelModule {
    @Provides @PerActivity
    public UserListViewModel getUserListViewModel(UserListView userListView, @Named("userList") UseCase getUserListUserCase, UserModelDataMapper userModelDataMapper) {
        UserListViewModel vm = new UserListViewModel(userListView, getUserListUserCase, userModelDataMapper);
        return vm;
    }

    @Provides @PerActivity
    public UserDetailsViewModel getUserDetailsViewModel(UserDetailsView userDetailsView,
                                                        @Named("userDetails") UseCase getUserDetailsUseCase, UserModelDataMapper userModelDataMapper) {
        UserDetailsViewModel vm = new UserDetailsViewModel(userDetailsView, getUserDetailsUseCase, userModelDataMapper);
        return vm;
    }
} 