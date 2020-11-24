package com.dicoding.picodiploma.githubuserappfinal.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_USERNAME
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubuserappfinal.helper.MappingHelper
import com.dicoding.picodiploma.githubuserappfinal.model.entity.UserFavorite
import com.dicoding.picodiploma.githubuserappfinal.view.widget.FavoritesStackWidget

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory{

    private var mWidgetItems = ArrayList<UserFavorite>()
    private val TAG = StackRemoteViewsFactory::class.java.simpleName

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        val cursor = mContext.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            "$COLUMN_USERNAME ASC")
        if (cursor != null) {
            val list = MappingHelper.mapCursorToArrayList(cursor)
            mWidgetItems.apply {
                clear()
                addAll(list)
            }
            Log.d(TAG, "$list")
            cursor.close()
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].avatar)
            .apply(RequestOptions().centerCrop())
            .submit()
            .get()
        remoteViews.apply {
            setImageViewBitmap(R.id.iv_user_favorite_widget, bitmap)
            setTextViewText(R.id.tv_username_favorite_widget, mWidgetItems[position].username)
        }
        val extras = bundleOf(FavoritesStackWidget.EXTRA_ITEM to position)
        val fillIntent = Intent()
        fillIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.iv_user_favorite_widget, fillIntent)
        return remoteViews
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}