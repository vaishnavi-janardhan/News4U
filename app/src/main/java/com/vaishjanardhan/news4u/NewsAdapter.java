package com.vaishjanardhan.news4u;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    NewsAdapter(Activity context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currNews = getItem(position);

        TextView sectionTextView = convertView.findViewById(R.id.section_text_view);
        TextView titleTextView = convertView.findViewById(R.id.title_text_view);
        TextView dateTextView = convertView.findViewById(R.id.date_text_view);
        TextView authorTextView = convertView.findViewById(R.id.author_text_view);
        ImageView imageView = convertView.findViewById(R.id.image_view);

        assert currNews != null;
        String section = currNews.getSection();
        String title = currNews.getTitle();
        String date = currNews.getDate();
        String author = currNews.getAuthor();
        String imageUrl = currNews.getImageUrl();

        sectionTextView.setText(section);
        titleTextView.setText(title);

        //If date is available set it on the date text view
        if (date != null) {
            dateTextView.setText(date.substring(0, 10));
        }
        //else set the visibility of the date text view to GONE
        else
            dateTextView.setVisibility(View.GONE);

        if (author != null) {
            authorTextView.setText(author);
        } else {
            authorTextView.setVisibility(View.GONE);
        }

        if (imageUrl != null)
            Glide.with(getContext())
                    .load(imageUrl)
                    .into(imageView);
        else
            imageView.setVisibility(View.GONE);

        final ToggleButton favToggleButton = convertView.findViewById(R.id.fav_toggle_button);
        favToggleButton.setChecked(false);
        favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_grey));
        favToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_white));
                    Toast.makeText(getContext(), "Bookmarked!", Toast.LENGTH_SHORT).show();
                } else
                    favToggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bookmark_grey));
            }
        });

        return convertView;
    }

}
