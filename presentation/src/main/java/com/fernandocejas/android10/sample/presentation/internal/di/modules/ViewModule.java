package com.fernandocejas.android10.sample.presentation.internal.di.modules;
/**
 * copyright (C) 2014 form Voviv Tech (Shenzhen) Ltd science and technology projects
 * <p/>
 * you must abide by the form of mountain technology license.
 * you can get a copy of the license
 * <p/>
 * http://www.voviv.com/
 * <p/>
 * unless applicable law requires or written consent of the software
 * license released under distributed on an "as is" basis,
 * without any form of guarantee or conditions, express or implied.
 * view license management authority and specific language
 * under the limit of permission.
 */

import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserDetailsView;
import com.fernandocejas.android10.sample.presentation.viewmodel.UserListView;

import dagger.Module;
import dagger.Provides;

/**
 * Title:ViewModule
 * Description:
 * User:  Hunter
 * Author: 2015-08-12 
 * Create Time: 12:36 
 * Modify the person:  
 * Modified: 
 * Modify Note:
 * @version
 */
@Module
public class ViewModule {
    UserListView userListView;
    UserDetailsView userDetailsView;
    public ViewModule(UserListView view) {
        this.userListView = view;
    }

    public ViewModule(UserDetailsView view) {
        this.userDetailsView = view;
    }



    @Provides
    @PerActivity
    public UserListView getUserListView() {
        return userListView;
    }

    @Provides
    @PerActivity
    public UserDetailsView getUserDetailsView() {
        return userDetailsView;
    }
} 