//
//  BaseActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

import net.twister.twister.*
import net.twister.foundation.*

open class BaseActivity : AppCompatActivity(), LifecycleOwner {

    private var mLifecycleRegistry: LifecycleRegistry? = null

    var title: TextView? = null
    lateinit var db: TwisterDatabase
    lateinit var postsViewModel: PostsViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry?.markState(Lifecycle.State.CREATED)

        db = TwisterDatabase.getDatabase(this)
        postsViewModel = PostsViewModel.getInstance(this)
    }

    public override fun onStart() {
        super.onStart()
        mLifecycleRegistry?.markState(Lifecycle.State.STARTED)
        updateData()
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry!!
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransitionEnter()
    }

    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    internal open fun updateData() {}

    internal open fun updateUI() {}

    fun logout() {}

    fun showAlert(message: String) {
        try {
            val builder: AlertDialog.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                builder = AlertDialog.Builder(this)
            }
            builder.setTitle("")
                    .setMessage(message)
                    .setNeutralButton(android.R.string.ok) { _, _ ->
                        // continue with delete
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showErrorAlert(message: String) {
        try {
            val builder: AlertDialog.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
            } else {
                builder = AlertDialog.Builder(this)
            }
            builder.setTitle("Error")
                    .setMessage(message)
                    .setNeutralButton(android.R.string.ok) { _, _ ->
                        // continue with delete
                    }
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
