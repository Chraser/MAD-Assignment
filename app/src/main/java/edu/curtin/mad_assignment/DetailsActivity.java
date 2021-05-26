package edu.curtin.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class DetailsActivity extends AppCompatActivity {
    public static final String ROWKEY = "Row";
    public static final String COLKEY = "Col";
    public static final String OWNERNAMEKEY = "OwnerName";
    public static final String STRUCTURETYPEKEY = "StructureType";
    public static final String STRUCTUREIMAGEKEY = "StructureImage";
    public static final String IMAGEKEY = "Image";
    public static final String POSITIONKEY = "Position";
    private static final int REQUEST_THUMBNAIL = 1;

    private int row;
    private int col;

    //static method to make an intent to pass to DetailsActivity
    public static Intent getIntent(Context context, int position, int row, int col, String ownerName, String structureType, int drawableID, Bitmap image) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(POSITIONKEY, position);
        intent.putExtra(ROWKEY, row);
        intent.putExtra(COLKEY, col);
        intent.putExtra(OWNERNAMEKEY, ownerName);
        intent.putExtra(STRUCTURETYPEKEY, structureType);
        intent.putExtra(STRUCTUREIMAGEKEY, drawableID);
        if(image != null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            intent.putExtra(IMAGEKEY, stream.toByteArray());
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Button back = (Button) findViewById(R.id.backButton);
        Button takeThumbnail = (Button) findViewById(R.id.takeThumbnailButton);

        ImageView structureImage = (ImageView) findViewById(R.id.structureImage);
        ImageView thumbnailImage = (ImageView) findViewById(R.id.thumbnailPhoto);

        TextView rowText = (TextView) findViewById(R.id.rowText);
        TextView columnText = (TextView) findViewById(R.id.columnText);
        TextView structureTypeText = (TextView) findViewById(R.id.structureTypeText);
        final TextView ownerNameText = (TextView) findViewById(R.id.ownerNameText);

        //get data from intent passed to DetailsActivity
        Intent intent = getIntent();
        int position = intent.getIntExtra(POSITIONKEY, 0);
        row = intent.getIntExtra(ROWKEY,0);
        col = intent.getIntExtra(COLKEY,0);
        String ownerName = intent.getStringExtra(OWNERNAMEKEY);
        String structureType = intent.getStringExtra(STRUCTURETYPEKEY);
        int drawableID = intent.getIntExtra(STRUCTUREIMAGEKEY,0);
        byte[] byteArray = intent.getByteArrayExtra(IMAGEKEY);

        //set the return intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra(POSITIONKEY, position);
        setResult(RESULT_OK, resultIntent);

        structureImage.setImageResource(drawableID);

        if(byteArray != null)
        {
            Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            thumbnailImage.setImageBitmap(image);
        }

        //update ui elements to display the MapElement's info
        rowText.setText(Integer.toString(row));
        columnText.setText(Integer.toString(col));
        ownerNameText.setText(ownerName);
        structureTypeText.setText(structureType);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameData gameData = GameData.get();
                //update the owner name of the MapElement after DetailsActivity has ended
                gameData.setElementOwnerName(row, col, ownerNameText.getText().toString());
                finish();
            }
        });

        takeThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent thumbnailPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(thumbnailPhotoIntent, REQUEST_THUMBNAIL);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(resultCode == RESULT_OK && requestCode ==  REQUEST_THUMBNAIL)
        {
            Bitmap thumbnail = (Bitmap)intent.getExtras().get("data");
            ImageView thumbnailImage = (ImageView) findViewById(R.id.thumbnailPhoto);
            //display image received from camera
            thumbnailImage.setImageBitmap(thumbnail);
            GameData gameData = GameData.get();
            //update the MapElement with the image received from camera
            gameData.setElementImage(row, col, thumbnail);
        }
    }
}
