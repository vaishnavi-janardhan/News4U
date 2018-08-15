package com.vaishjanardhan.news4u;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>{
    NewsAdapter(Activity context, List<News> news) {
        super(context,0,news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        News currNews = getItem(position);

        TextView sectionTextView = convertView.findViewById(R.id.section_text_view);
        TextView titleTextView = convertView.findViewById(R.id.title_text_view);
        TextView dateTextView = convertView.findViewById(R.id.date_text_view);

        assert currNews != null;
        String section = currNews.getSection();
        String title = currNews.getTitle();
        String date = currNews.getDate();;

        sectionTextView.setText(section);
        titleTextView.setText(title);

        //If date is available set it on the date text view
        if(date != null){
            dateTextView.setText(date.substring(0,10));
        }
        //else set the visibility of the date text view to GONE
        else
            dateTextView.setVisibility(View.GONE);

        return convertView;
    }
}
