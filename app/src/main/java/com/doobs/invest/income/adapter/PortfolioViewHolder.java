package com.doobs.invest.income.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doobs.invest.income.R;
import com.doobs.invest.income.model.PortfolioModel;

/**
 * View holder class to display the portfolio recycler view rows
 *
 * Created by mduby on 11/22/18.
 */

public class PortfolioViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private PortfolioRecyclerAdapter parent;
    TextView portfolioNameTextView;

    /**
     * default constructor
     *
     * @param itemView
     */
    public PortfolioViewHolder(View itemView, PortfolioRecyclerAdapter portfolioRecyclerAdapter) {
        super(itemView);

        // get the text view
        this.parent = portfolioRecyclerAdapter;
        this.portfolioNameTextView = (TextView) itemView.findViewById(R.id.portfolio_name_textview);

        // set the listener
        itemView.setOnClickListener(this);
    }

    /**
     * binds the portfolio to the view
     *
     * @param portfolioModel
     */
    protected void bind(PortfolioModel portfolioModel) {
        // get the name
        String name = portfolioModel.getName();

        // log
        Log.i(TAG_NAME, "Inflating view for portfolio: " + name);

        // set the name text
        this.portfolioNameTextView.setText(name);
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
        PortfolioModel portfolioModel = this.parent.getPortfolioModelList().get(clickedPosition);

        // call the movie item listener with the position
        this.parent.getPortfolioItemClickListener().onListItemClick(portfolioModel);
    }
}
