//
//  PostActivity.kt
//  Twister
//
//  Created by Amr Aboelela on 10/26/18.
//  Copyright © 2018 Twister Org. All rights reserved.
//

package net.twister.twister

open class PostActivity : BaseActivity() {
    companion object {

        //public static PostAdapter postAdapter;
        var postKey: String? = null
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView title = findViewById(R.id.title);

        int postID = db.postRepository.id(post);
        title.setText("Post #" + postID);

        db.postRepository.cache.observe(this, new Observer<Hashtable>() {
            @Override
            public void onChanged(@Nullable final Hashtable cache) {
                updateData();
            }
        });
        updateUI();
    }

    // MARK: - User interface

    private void updateUI() {
    }

    private void enableStart(Boolean enable) {
        TextView start = findViewById(R.id.start);
        if (enable) {
            start.setTextColor(Color.WHITE);
        } else {
            start.setTextColor(this.getResources().getColor(R.color.semiTransparent));
        }
        start.setEnabled(enable);
    }

    // MARK: - Actions

    @Override
    public void onClick(View v) {
        Log.d("PostActivity", "onClick");
    }
*/
}
