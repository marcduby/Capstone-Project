package com.doobs.invest.income;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doobs.invest.income.adapter.PortfolioRecyclerAdapter;
import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.repository.IncomeViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PortfolioRecyclerAdapter.PortfolioItemClickListener {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    private PortfolioRecyclerAdapter portfolioRecyclerAdapter;
    private RecyclerView portfolioRecyclerView;
    private IncomeViewModel incomeViewModel;
    private LinearLayoutManager portfolioListLayoutManager;
    private Button addPortfolioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // log
        Log.i(this.TAG_NAME, "In onCreate");

        // get the views
        this.addPortfolioButton = this.findViewById(R.id.portfolio_add_button);
        this.addPortfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPortfolio();
            }
        });

        // get the recycler view
        this.portfolioRecyclerView = (RecyclerView) this.findViewById(R.id.portfolio_recyclerview);
        this.portfolioRecyclerView.setHasFixedSize(true);

        // set the layout manager for the recycler view
        this.portfolioListLayoutManager = new LinearLayoutManager(this);
        this.portfolioRecyclerView.setLayoutManager(this.portfolioListLayoutManager);

        // create the adapter
        this.portfolioRecyclerAdapter = new PortfolioRecyclerAdapter(this);

        // set the adapter on the recycler view
        this.portfolioRecyclerView.setAdapter(this.portfolioRecyclerAdapter);

        // get the movie view model
        this.incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);

        // if there is state saved, reset it
        if (savedInstanceState != null) {
        }

        // set the observer on the database live data portfolio list used for display
        this.incomeViewModel.getPortfolioList().observe(this, new Observer<List<PortfolioModel>>() {
            @Override
            public void onChanged(@Nullable List<PortfolioModel> portfolioModelList) {
                // set the data on the adapter
                portfolioRecyclerAdapter.setPortfolioModelList(portfolioModelList);
            }
        });
    }

    /**
     * handles list item clicks
     *
     */
    @Override
    public void onListItemClick(PortfolioModel portfolioModel) {
        Toast.makeText(this, "clicked on item: " + portfolioModel.getName(), Toast.LENGTH_LONG).show();
    }

    /**
     * handles the add portfolio option
     *
     */
    private void addPortfolio() {
        // handle the add a portfolio button
        Toast.makeText(this, "adding portfolio", Toast.LENGTH_LONG).show();

        // call the view model
        PortfolioModel portfolioModel = new PortfolioModel();
        portfolioModel.setName("portfolio: " + new Date().getTime());
        portfolioModel.setGoal("goal test");
        portfolioModel.setDescriprion("test decsrition");
        this.incomeViewModel.insertPortfolio(portfolioModel);
    }

}
