package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * * Created by Sahil Chauhan on 11-Mar-18.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColor;

    WordAdapter(@NonNull Context context, ArrayList<Word> resource, int color) {
        super(context, 0, resource);
        this.mColor = color;
    }

    private static class ViewHolder {
        TextView miwok_word, english;
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        Word word = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.list_layout, parent, false);

            viewHolder.miwok_word = (TextView) convertView.findViewById(R.id.first_text_view);
            viewHolder.english = (TextView) convertView.findViewById(R.id.second_text_view);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        View secondLayout = convertView.findViewById(R.id.second_linear_layout);
        int color= ContextCompat.getColor(getContext(),mColor);
        secondLayout.setBackgroundColor(color);

        assert word != null;
        viewHolder.miwok_word.setText(word.getMiwokWord());
        viewHolder.english.setText(word.getDefaultWord());

        if (word.hasImage()) {
            viewHolder.imageView.setImageResource(word.getImageSource());
            viewHolder.imageView.setVisibility(View.VISIBLE);
        }else
            viewHolder.imageView.setVisibility(View.GONE);

        return convertView;
    }
}
