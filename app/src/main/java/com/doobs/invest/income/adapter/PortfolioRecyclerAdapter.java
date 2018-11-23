package com.doobs.invest.income.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doobs.invest.income.R;
import com.doobs.invest.income.model.PortfolioModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler adapter class to manage portfolio item list recyclers
 *
 * Created by mduby on 11/22/18.
 */

public class PortfolioRecyclerAdapter extends RecyclerView.Adapter<PortfolioViewHolder> {
    // constants
    private String TAG_NAME = this.getClass().getName();

    // instance variables
    private List<PortfolioModel> portfolioModelList = new ArrayList<PortfolioModel>();
    private PortfolioItemClickListener portfolioItemClickListener;

    /**
     * default constructor
     *
     * @param listener
     */
    public PortfolioRecyclerAdapter(PortfolioItemClickListener listener) {
        this.portfolioItemClickListener = listener;
    }

    @NonNull
    @Override
    /**
     * called when creating new view holder instance
     *
     */
    public PortfolioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get the context
        Context context = parent.getContext();
        PortfolioViewHolder portfolioViewHolder = null;

        // get the movie item layout
        int movieLayoutId = R.layout.list_item_portfolio;

        // inflate the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(movieLayoutId, parent, false);

        // create the view holder
        portfolioViewHolder = new PortfolioViewHolder(view, this);

        // log
        Log.i(this.TAG_NAME, "Create view holder");

        // return
        return portfolioViewHolder;
    }

    @Override
    /**
     * called when binding new data from the movie list
     *
     */
    public void onBindViewHolder(@NonNull PortfolioViewHolder holder, int position) {
        // populate the view holder with a portfolio at the position
        holder.bind(this.portfolioModelList.get(position));

        // log
        Log.i(this.TAG_NAME, "Bound view holder for position: " + position);
    }

    @Override
    /**
     * returns the number of items in the movie list
     *
     */
    public int getItemCount() {
        return this.portfolioModelList.size();
    }

    /**
     * sets the movie list
     *
     * @param portfolioModels
     */
    public void setPortfolioModelList(List<PortfolioModel> portfolioModels) {
        // set the movie list
        this.portfolioModelList = portfolioModels;

        // propogate event that data changed
        this.notifyDataSetChanged();
    }

    public List<PortfolioModel> getPortfolioModelList() {
        return this.portfolioModelList;
    }

    public PortfolioItemClickListener getPortfolioItemClickListener() {
        return portfolioItemClickListener;
    }

    /**
     * interface to handle item clicks
     *
     */
    public interface PortfolioItemClickListener {
        void onListItemClick(PortfolioModel portfolioModel);
    }
}
