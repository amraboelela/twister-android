//
//  BaseAdapter.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import net.twister.foundation.*

open class BaseAdapter(internal var context: Context, resource: Int) : ArrayAdapter<Any>(context, resource), LifecycleOwner {
    private val mLifecycleRegistry: LifecycleRegistry

    internal var baseActivity: BaseActivity
    var db: TwisterDatabase

    init {
        baseActivity = context as BaseActivity
        db = TwisterDatabase.getDatabase(context)
        inflater = baseActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    companion object {
        var inflater: LayoutInflater? = null
    }

}