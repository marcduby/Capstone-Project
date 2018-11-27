package com.doobs.invest.income;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doobs.invest.income.adapter.PortfolioRecyclerAdapter;
import com.doobs.invest.income.adapter.StockHoldingRecyclerAdapter;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.model.StockHoldingModel;
import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.repository.StockHoldingViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * activity class to display stock holdings of a portfolio
 *
 */
public class StockHoldingListActivity extends AppCompatActivity implements StockHoldingRecyclerAdapter.StockHoldingItemClickListener {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private StockHoldingRecyclerAdapter stockHoldingRecyclerAdapter;
    private StockHoldingViewModel stockHoldingViewModel;
    private LinearLayoutManager stockHoldingListLayoutManager;
    private PortfolioModel portfolioModel;

    // widgets
    @BindView(R.id.portfolio_name_textview)
    protected TextView portfolioNameTextView;

    @BindView(R.id.stock_holding_add_button)
    protected Button addStockHoldingButton;

    @BindView(R.id.stock_holding_list_total_cost)
    protected TextView portfolioTotalCostTextView;

    @BindView(R.id.stock_holding_list_total_dividend)
    protected TextView portfolioTotalDividendTextView;

    @BindView(R.id.stock_holding_recyclerview)
    protected RecyclerView stockHoldingRecyclerView;

    @BindView(R.id.industry_pie_chart)
    protected PieChart industryPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_holding_list);

        // bind butterknife
        ButterKnife.bind(this);

        // get the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // log
        Log.i(this.TAG_NAME, "In onCreate");

        // get the views
//        this.addStockHoldingButton = this.findViewById(R.id.stock_holding_add_button);
        this.addStockHoldingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStockHolding();
            }
        });

        // get the recycler view
        this.stockHoldingRecyclerView = (RecyclerView) this.findViewById(R.id.stock_holding_recyclerview);
        this.stockHoldingRecyclerView.setHasFixedSize(true);

        // set the layout manager for the recycler view
        this.stockHoldingListLayoutManager = new LinearLayoutManager(this);
        this.stockHoldingRecyclerView.setLayoutManager(this.stockHoldingListLayoutManager);

        // create the adapter
        this.stockHoldingRecyclerAdapter = new StockHoldingRecyclerAdapter(this);

        // set the adapter on the recycler view
        this.stockHoldingRecyclerView.setAdapter(this.stockHoldingRecyclerAdapter);

        // get the income view model
        this.stockHoldingViewModel = ViewModelProviders.of(this).get(StockHoldingViewModel.class);

        // load the portfolio from the intent
        Integer portfolioId = this.getIntent().getIntExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, 0);
        this.stockHoldingViewModel.getPortfolioModelLiveData(portfolioId).observe(this, new Observer<PortfolioModel>() {
            @Override
            public void onChanged(@Nullable PortfolioModel portfolioModelReturn) {
                portfolioModel = portfolioModelReturn;
                portfolioNameTextView.setText(portfolioModel.getName() + getString(R.string.portfolio_snippet));

                portfolioTotalCostTextView.setText(IncomeUtils.getCurrencyString(portfolioModel.getCostBasis()));
                portfolioTotalDividendTextView.setText(IncomeUtils.getCurrencyString(portfolioModel.getTotalDividend()));
            }
        });
//        this.portfolioModel = this.stockHoldingViewModel.loadPortfolio(portfolioId);

        // if there is state saved, reset it
        if (savedInstanceState != null) {
        }

        // set the observer on the database live data portfolio list used for display
        this.stockHoldingViewModel.getStockHoldingsForPortfolioId(portfolioId).observe(this, new Observer<List<StockHoldingModel>>() {
            @Override
            public void onChanged(@Nullable List<StockHoldingModel> stockHoldingModelList) {
                stockHoldingRecyclerAdapter.setStockHoldingModelList(stockHoldingModelList);

                // refresh the pie chart
                refreshPieChart();
            }
        });
    }

    /**
     * handles list item clicks
     *
     */
    @Override
    public void onListItemClick(StockHoldingModel stockHoldingModel) {
        Toast.makeText(this, "clicked on item: " + stockHoldingModel.getDescription(), Toast.LENGTH_LONG).show();
    }

    /**
     * handles the add portfolio option
     *
     */
    private void addStockHolding() {
        // handle the add a portfolio button
        Toast.makeText(this, "adding stock holding", Toast.LENGTH_LONG).show();

        // call the view model
//        PortfolioModel portfolioModel = new PortfolioModel();
//        portfolioModel.setName("portfolio: " + new Date().getTime());
//        portfolioModel.setGoal("goal test");
//        portfolioModel.setDescriprion("test decsrition");
//        this.incomeViewModel.insertPortfolio(portfolioModel);

        // create intent and send to new activity
        Intent intent = new Intent(this, StockHoldingSavingActivity.class);
        intent.putExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, this.portfolioModel.getId());
        this.startActivity(intent);
    }

    private void refreshPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(10.5f, "Green"));
        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(40.8f, "Blue"));

        PieDataSet set = new PieDataSet(entries, "Election Results");

        // set the colors
        final int[] MY_COLORS = {
                Color.rgb(255,0,255),       // purple
                Color.rgb(255,0,0),         // red
                Color.rgb(255,153,51),       // orange
                Color.rgb(204,204,0),     // yellow
                Color.rgb(0,255,0),        // green
                Color.rgb(0,0,255)};        // blue

        int[] pieColors = new int[] {R.color.colorAccent, R.color.colorPrimary, R.color.doobsPrimary, R.color.doobsAccent};
        int[] pieColors2 = new int[] {R.color.red, R.color.orange, R.color.yellow, R.color.green, R.color.blue, R.color.purple};

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : MY_COLORS) {
            colors.add(color);
        }

        set.setColors(colors);
        PieData data = new PieData(set);
        data.setValueTextSize(15);

        // set display of pie chart
        this.industryPieChart.setHoleRadius(15);
        this.industryPieChart.setTransparentCircleRadius(10);
        this.industryPieChart.setEntryLabelTextSize(15);
        this.industryPieChart.setEntryLabelColor(R.color.doobsPrimaryDark);
        this.industryPieChart.setDrawEntryLabels(true);
        this.industryPieChart.setUsePercentValues(true);
        this.industryPieChart.getLegend().setWordWrapEnabled(true);

        // set the data
        this.industryPieChart.setData(data);

        // refresh
        industryPieChart.invalidate(); // refresh

    }

}
