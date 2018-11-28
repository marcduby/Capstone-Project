package com.doobs.invest.income;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doobs.invest.income.model.PortfolioModel;
import com.doobs.invest.income.repository.IncomeViewModel;
import com.doobs.invest.income.util.IncomeConstants;
import com.doobs.invest.income.util.IncomeException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PortfolioUpdatingActivity extends AppCompatActivity {
    // constants
    private final String TAG_NAME = this.getClass().getName();

    // instance variables
    PortfolioModel portfolioModel;
    private IncomeViewModel incomeViewModel;

    // widgets
    @BindView(R.id.portfolio_name_editview)
    protected EditText portfolioNameEditText;

    @BindView(R.id.portfolio_descriprion_editview)
    protected EditText portfolioDescriptionEditText;

    @BindView(R.id.portfolio_goal_editview)
    protected EditText portfolioGoalEditText;

    // FAB
    @BindView(R.id.portfolio_saving_fab)
    protected FloatingActionButton portfolioSavingFab;

    // title text view
    @BindView(R.id.portfolio_name_label_textview)
    protected TextView titleTextView;

    /**
     * onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_saving);

        // bind butterknife
        ButterKnife.bind(this);

        // set the activity title
        this.titleTextView.setText(this.getString(R.string.title_activity_portfolio_saving));

        // get the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // get the income view model
        this.incomeViewModel = ViewModelProviders.of(this).get(IncomeViewModel.class);

        // load the portfolio model
        Intent intent = this.getIntent();
        final Integer portfolioId = intent.getIntExtra(IncomeConstants.ExtraKeys.PORTFOLIO_ID, 0);
        Log.i(TAG_NAME, "Loaded portfolio: " + portfolioId + " for modification");

        // get the portfolio model from the view model
        this.incomeViewModel.loadPortfolioById(portfolioId).observe(this, new Observer<PortfolioModel>() {
            @Override
            public void onChanged(@Nullable PortfolioModel loadedModel) {
                portfolioModel = loadedModel;
                setPortfolioData();
            }
        });

        // get the FAB
        this.portfolioSavingFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePortfolio(portfolioModel)) {
                    // log
                    Log.i(TAG_NAME, "Saved portfolio with id: " + portfolioModel.getId() + " and name: " + portfolioModel.getName());

                    // snack bar
//                    Snackbar.make(view, "Portfolio " + portfolioModel.getName() + " saved", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                    // go back
                    finish();
                }
            }
        });
    }

    /**
     * sets the loaded portfolio data in the edit text fields
     *
     */
    private void setPortfolioData() {
        // set the name
        this.portfolioNameEditText.setText(this.portfolioModel.getName());

        // set the description
        this.portfolioDescriptionEditText.setText(portfolioModel.getDescriprion());

        // set the goal
        this.portfolioGoalEditText.setText(portfolioModel.getGoal());
    }

    /**
     * save the portfolio information
     *
     * @param portfolioModel
     * @return
     */
    private boolean savePortfolio(PortfolioModel portfolioModel) {
        // local variables
        boolean saveSuccess = false;

        // get the name
        portfolioModel.setName(this.portfolioNameEditText.getText().toString());

        // get the description
        portfolioModel.setDescriprion(this.portfolioDescriptionEditText.getText().toString());

        // get the goal
        portfolioModel.setGoal(this.portfolioGoalEditText.getText().toString());

        // make sure all the fields are filled
        try {
            portfolioModel.validityCheck();

            // save the portfolio
            this.incomeViewModel.updatePortfolio(portfolioModel);
            saveSuccess = true;

        } catch (IncomeException exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        // return
        return saveSuccess;
    }
}
