//
//  PostsAdapter.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

import android.view.View
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import net.twister.foundation.*
import java.util.ArrayList
import java.util.regex.Pattern

class PostsAdapter(baseActivity: BaseActivity, resource: Int) : BaseAdapter(baseActivity, resource), View.OnClickListener {

    var postKeys: List<String> = ArrayList()
    var searchText = ""
    var tagText = ""

    override fun getCount(): Int {
        return postKeys.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any? {
        return postKeys[position]
    }

    private class ViewHolder {
        var postCell: LinearLayout? = null
        var avatar: ImageView? = null
        var userFullName: TextView? = null
        var userName: TextView? = null
        var time: TextView? = null
        var postText: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var vi = convertView
        val holder: ViewHolder
        if (convertView == null) {
            vi = inflater?.inflate(R.layout.post_list, null)
            holder = ViewHolder()
            holder.postCell = vi?.findViewById(R.id.post_cell)
            holder.avatar = vi?.findViewById(R.id.avatar)
            holder.userFullName = vi?.findViewById(R.id.userFullName)
            holder.userName = vi?.findViewById(R.id.username)
            holder.time = vi?.findViewById(R.id.time)
            holder.postText = vi?.findViewById(R.id.text)
            vi?.tag = holder
            vi?.setOnClickListener(this)
        } else {
            holder = vi?.tag as ViewHolder
        }
        holder.postCell?.visibility = View.VISIBLE
        holder.postCell?.tag = position
        val postKey = postKeys[position]
        var post = Post(postKey)
        db.postRepository.modelAtKey(postKey, true)?.let {
            post = it
        }
        db.userRepository.modelAtKey(post.username, true)?.let { user ->
            holder.userFullName?.text = user.fullname
        } ?: run {
            holder.userFullName?.text = post.username
        }
        holder.userName?.text = "@" + post.username
        val avatar = baseActivity.postsViewModel.avatar(post.username)
        if (avatar == null) {
            holder.avatar?.setImageResource(R.drawable.generic_person)
        } else {
            holder.avatar?.setImageBitmap(ImageHelper.getRoundedCornerBitmap(avatar, 10))
        }
        holder.avatar?.setOnClickListener {
            username = post.username
            baseActivity.startActivity(Intent(baseActivity, UserPostsActivity::class.java))
        }
        holder.userFullName?.setOnClickListener {
            username = post.username
            baseActivity.startActivity(Intent(baseActivity, UserPostsActivity::class.java))
        }
        holder.userName?.setOnClickListener {
            username = post.username
            baseActivity.startActivity(Intent(baseActivity, UserPostsActivity::class.java))
        }
        holder.time?.text = MyDate.friendlyDateStringFrom(Post.time(postKey)!!)
        var postText = post.message ?: ""
        val tagSpan = SpannableString(postText)

        val hashtagOrMentionPattern = Pattern.compile(HashtagOrMentionRepository.hashtagOrMentionRegex)
        val hashtagOrMentionMatcher = hashtagOrMentionPattern.matcher(postText)
        while (hashtagOrMentionMatcher.find()) {
            val theMatch = hashtagOrMentionMatcher.group()
            val clickSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    Log.d("PostsAdapter", "hashtagPattern theMatch: " + theMatch)
                    Log.d("PostsAdapter", "hashtagPattern thePosition: " + position)
                    Log.d("PostsAdapter", "hashtagPattern postKey: " + postKeys[position])
                    if (baseActivity.title?.text == theMatch) {
                        hashtagOrMention = ""
                        username = theMatch.replace("@", "")
                        baseActivity.startActivity(Intent(baseActivity, UserPostsActivity::class.java))
                    } else {
                        hashtagOrMention = theMatch
                        baseActivity.startActivity(Intent(baseActivity, HashtagOrMentionActivity::class.java))
                    }
                }

                override fun updateDrawState(paint: TextPaint) {
                    super.updateDrawState(paint)
                    paint.isUnderlineText = false
                    paint.color = Color.BLUE
                }
            }
            tagSpan.setSpan(clickSpan,
                    hashtagOrMentionMatcher.start(),
                    hashtagOrMentionMatcher.end(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        val urlPattern = Pattern.compile(PostsAdapter.urlRegex)
        val urlMatcher = urlPattern.matcher(postText)
        while (urlMatcher.find()) {
            val theMatch = urlMatcher.group()
            val clickSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    Log.d("PostsAdapter", "urlPattern theMatch: " + theMatch)
                    Log.d("PostsAdapter", "urlPattern thePosition: " + position)
                    Log.d("PostsAdapter", "urlPattern postKey: " + postKeys[position])
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(theMatch))
                    baseActivity.startActivity(browserIntent)
                }

                override fun updateDrawState(paint: TextPaint) {
                    super.updateDrawState(paint)
                    paint.isUnderlineText = false
                    paint.color = Color.BLUE
                }
            }
            tagSpan.setSpan(clickSpan, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        val searchTokens = this.searchText.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        for (token in searchTokens) {
            val searchPattern = Pattern.compile(token)
            val searchMatcher = searchPattern.matcher(postText.toLowerCase())
            while (searchMatcher.find()) {
                val theMatch = searchMatcher.group()
                val clickSpan = object : ClickableSpan() {
                    override fun onClick(textView: View) {
                        Log.d("PostsAdapter", "searchPattern theMatch: " + theMatch)
                        Log.d("PostsAdapter", "searchPattern thePosition: " + position)
                        Log.d("PostsAdapter", "searchPattern postKey: " + postKeys[position])
                    }

                    override fun updateDrawState(paint: TextPaint) {
                        super.updateDrawState(paint)
                        paint.isUnderlineText = false
                        paint.color = Color.MAGENTA
                    }
                }
                tagSpan.setSpan(clickSpan, searchMatcher.start(), searchMatcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            }
        }

        val tagPattern = Pattern.compile(tagText)
        val tagMatcher = tagPattern.matcher(postText.toLowerCase())
        while (tagMatcher.find()) {
            val theMatch = tagMatcher.group()
            val clickSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    Log.d("PostsAdapter", "tagPattern theMatch: " + theMatch)
                    Log.d("PostsAdapter", "tagPattern thePosition: " + position)
                    Log.d("PostsAdapter", "tagPattern postKey: " + postKeys[position])
                }

                override fun updateDrawState(paint: TextPaint) {
                    super.updateDrawState(paint)
                    paint.isUnderlineText = false
                    paint.color = context.resources.getColor(R.color.colorDesignblueDark)
                }
            }
            tagSpan.setSpan(clickSpan, tagMatcher.start(), tagMatcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        holder.postText?.text = tagSpan
        holder.postText?.movementMethod = LinkMovementMethod.getInstance()
        return vi ?: View(context)
    }

    override fun onClick(v: View) {
        (v.getTag() as? Int)?.let { position ->
            val postKey = postKeys[position]
            PostActivity.postKey = postKey
            baseActivity.startActivity(Intent(baseActivity, PostActivity::class.java))
        }
    }

    companion object {
        val urlRegex = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"

        var hashtagOrMention = ""
        var username = ""
    }

}