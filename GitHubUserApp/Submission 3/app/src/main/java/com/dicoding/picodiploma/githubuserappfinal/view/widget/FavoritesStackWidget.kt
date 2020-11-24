package com.dicoding.picodiploma.githubuserappfinal.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkBuilder
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.COLUMN_USERNAME
import com.dicoding.picodiploma.githubuserappfinal.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubuserappfinal.helper.MappingHelper
import com.dicoding.picodiploma.githubuserappfinal.model.entity.UserFavorite
import com.dicoding.picodiploma.githubuserappfinal.service.StackWidgetService
import com.dicoding.picodiploma.githubuserappfinal.view.DetailUserFragment.Companion.EXTRA_AVATAR
import com.dicoding.picodiploma.githubuserappfinal.view.DetailUserFragment.Companion.EXTRA_TYPE
import com.dicoding.picodiploma.githubuserappfinal.view.DetailUserFragment.Companion.EXTRA_USERNAME
import com.dicoding.picodiploma.githubuserappfinal.view.MainActivity

class FavoritesStackWidget : AppWidgetProvider() {

    companion object {
        private const val INTENT_ACTION = "com.dicoding.picodiploma.INTENT_ACTION"
        private const val WIDGET_CLICK = "widget_click"
        private const val WIDGET_ID_EXTRA = "widget_id_extra"
        private var list = ArrayList<UserFavorite>()
        const val EXTRA_ITEM = "com.dicoding.picodiploma.EXTRA_ITEM"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = toUri(Intent.URI_INTENT_SCHEME).toUri()
            }
            val views =
                RemoteViews(context.packageName, R.layout.favorites_stack_widget).apply {
                setRemoteAdapter(R.id.stack_view, intent)
                setEmptyView(R.id.stack_view, R.id.empty_view)
            }
            val favoriteUserIntent = Intent(context, FavoritesStackWidget::class.java)
            favoriteUserIntent.action = INTENT_ACTION
            favoriteUserIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val favoriteUserPendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    favoriteUserIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, favoriteUserPendingIntent)
            val listFavoriteUserPendingIntent = NavDeepLinkBuilder(context)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.userFavoritesFragment)
                .createPendingIntent()
            views.setOnClickPendingIntent(R.id.photo_caption_text, listFavoriteUserPendingIntent)
            views.setOnClickPendingIntent(
                R.id.img_btn_refresh_user,
                getPendingSelfIntent(context, appWidgetId, WIDGET_CLICK))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingSelfIntent(context: Context, appWidgetId: Int, action: String):
            PendingIntent {
            val intent = Intent(context, FavoritesStackWidget::class.java).apply {
                this.action = action
                putExtra(WIDGET_ID_EXTRA, appWidgetId)
            }
            return PendingIntent.getBroadcast(context, appWidgetId, intent, 0)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val cursor = context.contentResolver.query(
                CONTENT_URI,
                null,
                null,
                null,
                "$COLUMN_USERNAME ASC")
            if (cursor != null) {
                val listUser = MappingHelper.mapCursorToArrayList(cursor)
                list.apply {
                    clear()
                    addAll(listUser)
                }
                cursor.close()
            }
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {}

    override fun onDisabled(context: Context) {}

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (WIDGET_CLICK == intent?.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetId = intent.getIntExtra(WIDGET_ID_EXTRA, 0)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view)
        }
        if (intent != null) {
            if (intent.action == INTENT_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                val mBundle = Bundle().apply {
                    putString(EXTRA_USERNAME, list[viewIndex].username)
                    putString(EXTRA_AVATAR, list[viewIndex].avatar)
                    putString(EXTRA_TYPE, list[viewIndex].type)
                }
                val detailUserFavoriteFragment = context?.let {
                    NavDeepLinkBuilder(it)
                        .setComponentName(MainActivity::class.java)
                        .setGraph(R.navigation.main_navigation)
                        .setDestination(R.id.detailUserFragment)
                        .setArguments(mBundle)
                        .createPendingIntent()
                }
                val views =
                    RemoteViews(context?.packageName, R.layout.favorites_stack_widget).apply {
                        setRemoteAdapter(R.id.stack_view, intent)
                        setEmptyView(R.id.stack_view, R.id.empty_view)
                    }
                views.setOnClickPendingIntent(R.id.iv_user_favorite_widget, detailUserFavoriteFragment)
            }
        }
    }
}