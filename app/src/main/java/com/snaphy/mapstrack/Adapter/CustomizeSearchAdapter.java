package com.snaphy.mapstrack.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.snaphy.mapstrack.Model.SearchSuggestions;
import com.snaphy.mapstrack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi-Gupta on 3/9/2016.
 */
public class CustomizeSearchAdapter extends BaseAdapter implements Filterable {

    private ArrayList<SearchSuggestions> data;
    private List<SearchSuggestions> suggestions;
    private Drawable suggestionIcon;
    private LayoutInflater inflater;
    private boolean ellipsize;

    public CustomizeSearchAdapter(Context context, List<SearchSuggestions> suggestions) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
    }

    public CustomizeSearchAdapter(Context context, List<SearchSuggestions> suggestions, Drawable suggestionIcon, boolean ellipsize) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
        this.suggestionIcon = suggestionIcon;
        this.ellipsize = ellipsize;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<SearchSuggestions> searchData = new ArrayList<>();

                    for (SearchSuggestions searchSuggestions : suggestions) {
                        if (searchSuggestions.getSuggestion().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            searchData.add(new SearchSuggestions( searchSuggestions.isEvent() ,searchSuggestions.getSuggestion()));
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (ArrayList<SearchSuggestions>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SearchSuggestions getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_suggestion, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        SearchSuggestions currentListData =  getItem(position);

        viewHolder.textView.setText(currentListData.getSuggestion());
        if(currentListData.isEvent()){
            viewHolder.isEvent.setText("Event");
        } else {
            viewHolder.isEvent.setText("Location");
        }

        if (ellipsize) {
            viewHolder.textView.setSingleLine();
            viewHolder.textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        return convertView;
    }

    private class SuggestionsViewHolder {

        TextView textView;
        TextView isEvent;
        ImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(R.id.search_suggestion_textview2);
            isEvent = (TextView) convertView.findViewById(R.id.search_suggestion_textview1);
            if (suggestionIcon != null) {
                imageView = (ImageView) convertView.findViewById(R.id.suggestion_icon);
                imageView.setImageDrawable(suggestionIcon);
            }
        }
    }
}
