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
import com.doobs.invest.income.util.NetworkUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private List<StockHoldingModel> stockHoldingModelList;

    // widgets
    @BindView(R.id.portfolio_name_textview)
    protected TextView portfolioNameTextView;

    @BindView(R.id.stock_holding_add_button)
    protected Button addStockHoldingButton;

    @BindView(R.id.stock_holding_list_total_value)
    protected TextView portfolioTotalValueTextView;

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
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                portfolioNameTextView.setText(getString(R.string.portfolio_snippet) + portfolioModel.getName());

                portfolioTotalValueTextView.setText(IncomeUtils.getCurrencyString(portfolioModel.getCurrentValue()));
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
            public void onChanged(@Nullable List<StockHoldingModel> modelList) {
                // sort the list
                Collections.sort(modelList, new Comparator<StockHoldingModel>() {
                    @Override
                    public int compare(StockHoldingModel aModel, StockHoldingModel bModel) {
                        return aModel.getStockSymbol().compareTo(bModel.getStockSymbol());
                    }
                });

                // update the display
                stockHoldingRecyclerAdapter.setStockHoldingModelList(modelList);

                // update the instance variable
                stockHoldingModelList = modelList;

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
        Map<String, Double> industryMap = null;
        String industry = null;

        // get the industry map
        industryMap = IncomeUtils.getIndustryMap(this.stockHoldingModelList);

        // for all the entries, add to the chart
        Iterator<String> stringIterator = industryMap.keySet().iterator();
        while (stringIterator.hasNext()) {
            // get the key
            industry = stringIterator.next();

            // set the pie chart entry
            entries.add(new PieEntry(industryMap.get(industry).floatValue(), industry));
        }

        // create the pie chart set
        PieDataSet pieChartSet = new PieDataSet(entries, "");

        // set the colors
        pieChartSet.setColors(IncomeUtils.getChartColors());
        PieData data = new PieData(pieChartSet);

        // set the text size
        data.setValueTextSize(15);

        // set display of pie chart
        this.industryPieChart.setHoleRadius(15);
        this.industryPieChart.setTransparentCircleRadius(10);
        this.industryPieChart.setEntryLabelTextSize(15);
        this.industryPieChart.setEntryLabelColor(R.color.black);
        this.industryPieChart.setDrawEntryLabels(false);
        this.industryPieChart.setUsePercentValues(true);

        // set legend display
        this.industryPieChart.getLegend().setWordWrapEnabled(true);
        this.industryPieChart.getLegend().setEnabled(true);
        this.industryPieChart.getLegend().setTextSize(15);
        this.industryPieChart.getDescription().setEnabled(false);
//        this.industryPieChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);


        // set the data
        this.industryPieChart.setData(data);

        // refresh
        industryPieChart.invalidate(); // refresh
    }
}
