package edu.curtin.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    private Settings settings;
    private EditText mapWidthText;
    private EditText mapHeightText;
    private EditText initialMoneyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = Settings.get();
        mapWidthText = (EditText) findViewById(R.id.mapWidthText);
        mapHeightText = (EditText) findViewById(R.id.mapHeightText);
        initialMoneyText = (EditText) findViewById(R.id.initialMoneyText);

        Button backToTitle = (Button) findViewById(R.id.backToTitleButton);
        Button revertToDefault = (Button) findViewById((R.id.revertToDefaultButton));

        //update the ui elements to show current values from settings object
        mapWidthText.setText(Integer.toString(settings.getMapWidth()));
        mapHeightText.setText(Integer.toString(settings.getMapHeight()));
        initialMoneyText.setText(Integer.toString(settings.getInitialMoney()));

        backToTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update the values entered in the text element into the settings object
                settings.setMapHeight(Integer.valueOf(mapHeightText.getText().toString()));
                settings.setMapWidth(Integer.valueOf(mapWidthText.getText().toString()));
                settings.setInitialMoney(Integer.valueOf(initialMoneyText.getText().toString()));
                settings.updateDb();
                finish();
            }
        });

        revertToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.resetToDefault();
                //update the ui elements to show default values
                mapWidthText.setText(Integer.toString(settings.getMapWidth()));
                mapHeightText.setText(Integer.toString(settings.getMapHeight()));
                initialMoneyText.setText(Integer.toString(settings.getInitialMoney()));
            }
        });
    }
}
