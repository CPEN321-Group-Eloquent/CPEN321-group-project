package com.example.eloquent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    LayoutInflater inflater;
    public List<Presentation> presentations;
    public List<Presentation> getPresentationsListFilter = new ArrayList<>();
    public OnPresListener mOnPresListener;

    public interface OnPresListener {
        void selectedPres(Presentation presentation);
    }

    public Adapter(Context ctx, List<Presentation> presentations, OnPresListener onPresListener){
        this.inflater = LayoutInflater.from(ctx);
        this.getPresentationsListFilter = presentations;
        this.presentations = presentations;
        this.mOnPresListener = onPresListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_layout,parent,false);
        return new ViewHolder(view,mOnPresListener);
    }

    //create a templete of view holder for the recyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data

        Presentation presentation = presentations.get(position);
        holder.presentationTitle.setText(presentations.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnPresListener.selectedPres(presentation);
            }
        });

    }

    // get the number of presentations
    @Override
    public int getItemCount() {
        return presentations.size();
    }

    // filter the presentation list and show the response corresponding to user's input
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0) {
                    filterResults.values = getPresentationsListFilter;
                    filterResults.count = getPresentationsListFilter.size();
                } else {
                    String searchStr = constraint.toString().toLowerCase();
                    List<Presentation> filteredPresentations = new ArrayList<>();
                    for(Presentation presentation: getPresentationsListFilter) {
                        if(presentation.getTitle().toLowerCase().contains(searchStr)) {
                            filteredPresentations.add(presentation);
                        }
                    }

                    filterResults.values = filteredPresentations;
                    filterResults.count = filteredPresentations.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                presentations = (List<Presentation>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    // detects the on click on presentation object
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView presentationTitle;
        OnPresListener onPresListener;

        public ViewHolder(@NonNull View itemView, OnPresListener onPresListener) {
            super(itemView);

            presentationTitle = itemView.findViewById(R.id.presentationTitle);
            this.onPresListener = onPresListener;

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

        }
    }


}
