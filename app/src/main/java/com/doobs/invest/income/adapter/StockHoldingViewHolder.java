package com.doobs.invest.income.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.doobs.invest.income.R;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;

/**
 * View holder class to display the portfolio recycler view rows
 *
 * Created by mduby on 11/22/18.
 */

public class StockHoldingViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private StockHoldingRecyclerAdapter parent;
    TextView stockHoldingNameTextView;

    /**
     * default constructor
     *
     * @param itemView
     */
    public StockHoldingViewHolder(View itemView, StockHoldingRecyclerAdapter stockHoldingRecyclerAdapter) {
        super(itemView);

        // get the text view
        this.parent = stockHoldingRecyclerAdapter;
        this.stockHoldingNameTextView = (TextView) itemView.findViewById(R.id.portfolio_name_textview);

        // set the listener
        itemView.setOnClickListener(this);
    }

    /**
     * binds the portfolio to the view
     *
     * @param stockHoldingModel
     */
    protected void bind(StockHoldingModel stockHoldingModel) {
        // get the name
        String name = stockHoldingModel.getDescription();

        // log
        Log.i(TAG_NAME, "Inflating view for stock holding: " + name);

        // set the name text
        this.stockHoldingNameTextView.setText(name + ": " + stockHoldingModel.getId());
    }

    @Override
    /**
     * handle clicks on the view holder
     *
     */
    public void onClick(View view) {
        // get the index clicked
        int clickedPosition = this.getAdapterPosition();

        // get the movie
        StockHoldingModel stockHoldingModel = this.parent.getStockHoldingModelList().get(clickedPosition);

        // call the movie item listener with the position
        this.parent.getStockHoldingItemClickListener().onListItemClick(stockHoldingModel);
    }
}
