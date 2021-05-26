package edu.curtin.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.curtin.mad_assignment.GameDataSchema.GameStatsTable;

public class MainActivity extends AppCompatActivity {

    private GameData gameData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.startButton);
        final Button resumeButton = (Button) findViewById(R.id.resumeButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        final MAD_AssignmentDbHelper dbHelper = new MAD_AssignmentDbHelper(MainActivity.this);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Settings settings = Settings.get();
        gameData = GameData.get();

        //check if there is existing game data so that you can resume that game
        GameStatsCursor gameStatsCursor = new GameStatsCursor((db.query(GameStatsTable.NAME, null, null, null, null, null, null)));
        try {

            if (gameStatsCursor.getCount() > 0)
            {
                resumeButton.setEnabled(true);
            }
            else
            {
                resumeButton.setEnabled(false);
            }
        }
        finally {
            gameStatsCursor.close();
        }

        settings.load(db);
        gameData.load(db);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make a clean new game data tables
                dbHelper.resetGameDataTables(db);
                //resets the variable gameData to its default values and a fresh map
                gameData.reset();
                resumeButton.setEnabled(true);
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear the game data tables so that you can't resume on a game that is using old settings if you were to just go into Settings activity and restart the app
                dbHelper.resetGameDataTables(db);
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                //going into settings activity means you are starting a new game so resume is disabled
                resumeButton.setEnabled(false);
            }
        });
    }
}
