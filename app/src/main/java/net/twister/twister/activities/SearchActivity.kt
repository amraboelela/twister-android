//
//  SearchActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.util.ArrayList

open class SearchActivity : PostsBaseActivity(), TextWatcher {

    internal var searchEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchEditText = findViewById(R.id.search)
        searchEditText?.addTextChangedListener(this)
        postList.setOnTouchListener { _, _ ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText?.windowToken, 0)

            false
        }
    }

    override fun onStart() {
        super.onStart()
        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText?.windowToken, 0)
        }, 300)
    }

    override fun updateUI() {
        var searchText = ""
        if (searchEditText != null) {
            searchText = searchEditText?.text.toString().toLowerCase().trim({ it <= ' ' })
            postsAdapter.searchText = searchText
        }
        if (searchText.isEmpty()) {
            this.filteredPostKeys = postKeys
        }
    }

    internal fun searchUpdateUI() {
        val searchText = searchEditText?.text.toString().toLowerCase().trim({ it <= ' ' })
        //Log.d("SearchActivity", "searchUpdateUI searchText: " + searchText)
        if (searchText.isEmpty()) {
            this.filteredPostKeys = postKeys
            updateUI()
        } else {
            db.executor.execute {
                performSearch(searchText)
            }
        }
    }

    fun performSearch(searchText: String) {
        val searchTextArray = searchText.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        var postKeysSet = setOf<String>()
        for (word in searchTextArray) {
            val wordKeys = db.wordRepository.keysWithPrefix(word.toLowerCase())
            if (postKeysSet.size == 0) {
                postKeysSet = postKeysSet.plus(db.wordRepository.postKeysFromWordKeys(wordKeys))
            } else {
                postKeysSet = postKeysSet.intersect(db.wordRepository.postKeysFromWordKeys(wordKeys))
            }
        }
        Log.d("SearchAsyncTask", "searchText: " + searchText + " postKeysSet.size: " + postKeysSet.size + " keys: " + postKeysSet)
        Handler(Looper.getMainLooper()).post {
            updateWithKeys(ArrayList(postKeysSet))
        }
    }

    open fun updateWithKeys(keys: ArrayList<String>) {
        filteredPostKeys = ArrayList(keys.sortedWith(compareByDescending { it } ))
        updateUI()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        Log.d("SearchActivity", "afterTextChanged s: " + s.toString())
        searchUpdateUI()
    }

}
