package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class PostDetailsActivity extends AppCompatActivity {

    ImageView ivProfileImageDetails;
    TextView tvUsernameDetails;
    ImageView ivImageDetails;
    TextView tvDescriptionDetails;
    TextView tvTimeStampDetails;

    Post post;

    private static final String TAG = "PostDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ivProfileImageDetails = findViewById(R.id.ivProfileImageDetails);
        tvUsernameDetails = findViewById(R.id.tvUsernameDetails);
        ivImageDetails = findViewById(R.id.ivImageDetails);
        tvDescriptionDetails = findViewById(R.id.tvDescriptionDetails);
        tvTimeStampDetails = findViewById(R.id.tvTimeStampDetails);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("Post"));

        tvDescriptionDetails.setText(post.getDescription());
        tvUsernameDetails.setText(post.getUser().getUsername());
        tvTimeStampDetails.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImageDetails);
        }

    }
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            Log.i("TweetAdapter", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }
}
