package com.dicoding.picodiploma.githubuserappfinal.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.dicoding.picodiploma.githubuserappfinal.view.adapter.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}
