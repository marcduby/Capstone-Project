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
import com.doobs.invest.income.model.StockHoldingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler adapter class to manage portfolio item list recyclers
 *
 * Created by mduby on 11/22/18.
 */

public class StockHoldingRecyclerAdapter extends RecyclerView.Adapter<StockHoldingViewHolder> {
    // constants
    private String TAG_NAME = this.getClass().getName();

    // instance variables
    private List<StockHoldingModel> stockHoldingModelList = new ArrayList<StockHoldingModel>();
    private StockHoldingItemClickListener stockHoldingItemClickListener;

    /**
     * default constructor
     *
     * @param listener
     */
    public StockHoldingRecyclerAdapter(StockHoldingItemClickListener listener) {
        this.stockHoldingItemClickListener = listener;
    }

    @NonNull
    @Override
    /**
     * called when creating new view holder instance
     *
     */
    public StockHoldingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get the context
        Context context = parent.getContext();
        StockHoldingViewHolder stockHoldingViewHolder = null;

        // get the movie item layout
        int movieLayoutId = R.layout.list_item_stock_holding;

        // inflate the layout
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(movieLayoutId, parent, false);

        // create the view holder
        stockHoldingViewHolder = new StockHoldingViewHolder(view, this);

        // log
        Log.i(this.TAG_NAME, "Create view holder");

        // return
        return stockHoldingViewHolder;
    }

    @Override
    /**
     * called when binding new data from the movie list
     *
     */
    public void onBindViewHolder(@NonNull StockHoldingViewHolder holder, int position) {
        // populate the view holder with a portfolio at the position
        holder.bind(this.stockHoldingModelList.get(position));

        // log
        Log.i(this.TAG_NAME, "Bound view holder for position: " + position);
    }

    @Override
    /**
     * returns the number of items in the movie list
     *
     */
    public int getItemCount() {
        return this.stockHoldingModelList.size();
    }

    /**
     * sets the movie list
     *
     * @param stockHoldingModels
     */
    public void setStockHoldingModelList(List<StockHoldingModel> stockHoldingModels) {
        // set the movie list
        this.stockHoldingModelList = stockHoldingModels;

        // propogate event that data changed
        this.notifyDataSetChanged();
    }

    public List<StockHoldingModel> getStockHoldingModelList() {
        return this.stockHoldingModelList;
    }

    public StockHoldingItemClickListener getStockHoldingItemClickListener() {
        return this.stockHoldingItemClickListener;
    }

    /**
     * interface to handle item clicks
     *
     */
    public interface StockHoldingItemClickListener {
        void onListItemClick(StockHoldingModel stockHoldingModel);
    }
}
