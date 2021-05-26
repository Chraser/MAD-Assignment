package edu.curtin.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity {
    public static final int REQUEST_DETAILS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFrag = (MapFragment) fm.findFragmentById(R.id.map);
        if(mapFrag == null) // It might already be there!
        {
            mapFrag = new MapFragment();
            fm.beginTransaction().add(R.id.map, mapFrag).commit();
        }
        SelectorFragment selectorFrag = (SelectorFragment) fm.findFragmentById(R.id.selector);
        if(selectorFrag == null) // It might already be there!
        {
            selectorFrag = new SelectorFragment();
            fm.beginTransaction().add(R.id.selector, selectorFrag).commit();
        }
        mapFrag.setSelectorFrag(selectorFrag);

        Button title = (Button) findViewById(R.id.titleButton);
        Button run = (Button) findViewById((R.id.runButton));
        Button demolish = (Button) findViewById(R.id.demolishButton);
        Button details = (Button) findViewById(R.id.detailsButton);

        final TextView gameTime = (TextView) findViewById(R.id.gameTimeText);
        final TextView currentMoney = (TextView) findViewById(R.id.currentMoneyText);
        final TextView lastIncome = (TextView) findViewById(R.id.lastIncomeText);
        final TextView population = (TextView) findViewById(R.id.populationText);
        final TextView employmentRate = (TextView) findViewById(R.id.employmentRateText);

        final GameData gameData = GameData.get();

        //update the ui element's text to reflect current game data
        gameTime.setText(Integer.toString(gameData.getGameTime()));
        currentMoney.setText(Integer.toString(gameData.getMoney()));
        lastIncome.setText(Integer.toString(gameData.getLastIncome()));
        population.setText(Integer.toString(gameData.getPopulation()));
        employmentRate.setText(Integer.toString((int)(gameData.getEmploymentRate() * 100)) + "%");

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //run the game for 1 tick of time
                gameData.run();

                //update text to reflect current game data
                gameTime.setText(Integer.toString(gameData.getGameTime()));
                currentMoney.setText(Integer.toString(gameData.getMoney()));
                lastIncome.setText(Integer.toString(gameData.getLastIncome()));

                //inform player that they have lost if they have no money
                if(gameData.getMoney() < 0)
                {
                    Toast.makeText(getApplicationContext(), "You've lost. Please return to title to restart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        demolish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameData.setDemolishMode();
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameData.setDetailsMode();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        int position = intent.getIntExtra(DetailsActivity.POSITIONKEY,0);
        if(resultCode == RESULT_OK && requestCode == REQUEST_DETAILS)
        {
            //tell the map fragment to tell the map adapter to update the image at the position
            FragmentManager fm = getSupportFragmentManager();
            MapFragment mapFrag = (MapFragment) fm.findFragmentById(R.id.map);
            position = intent.getIntExtra(DetailsActivity.POSITIONKEY,0);
            mapFrag.notifyAdapter(position);
        }
    }
}
