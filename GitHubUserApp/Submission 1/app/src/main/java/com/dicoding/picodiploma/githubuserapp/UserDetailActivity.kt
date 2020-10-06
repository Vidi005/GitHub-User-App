package com.dicoding.picodiploma.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var nameUser: String
    private lateinit var content: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Detail User"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val user = intent.getParcelableExtra(EXTRA_USER) as User
        val image = user.avatar
        nameUser = user.name.toString()
        content = "${user.username.toString()}\n" +
                "${user.company.toString()}\n" +
                "${user.location.toString()}\n" +
                "${user.repository.toString()}\n" +
                "${user.followers.toString()}\n" +
                user.following.toString()
        Glide.with(this).load(image).into(iv_avatar_received)
        tv_name_received.text = nameUser
        tv_object_received.text = content
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_share -> {
                val shareUser = "Github User:\n$nameUser\n$content"
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareUser)
                shareIntent.type = "text/html"
                startActivity(Intent.createChooser(shareIntent, "Share using"))
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
