package com.example.storingimageinsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ImageView image, imagecheck;
    Button btnsave,btnshow;
    EditText name;
    int IMAGE_REQUEST_CODE=100;
    public static Bitmap bitmap;
    DatabaseHelper db;
    byte [] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.image);
        //imagecheck = findViewById(R.id.imagecheck);
        btnsave=findViewById(R.id.btnsave);
        btnshow=findViewById(R.id.btnshow);
        name=findViewById(R.id.name);
        db=new DatabaseHelper(this);


        File sdcard= Environment.getExternalStorageDirectory();
        File directory=new File(sdcard.getAbsolutePath()+"/YourFolderName");
        directory.mkdir();

        if(!directory.exists()){
            Log.d("TAG","NOOOOOOOOOOOOOOOOOOOO");
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagechooser();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImageToGallery(bitmap);

                /*Log.d("TAG","Inside SAVE...........1:  "+directory);
                //FileOutputStream outputStream;
                String filename=String.format("%d.jpg",System.currentTimeMillis());
                File outFile=new File(directory,filename);
                Log.d("TAG","Inside SAVE...........2222222222222"+filename);
                try {
                    FileOutputStream outputStream = new FileOutputStream(outFile);
                    Log.d("TAG","Inside SAVE...........3333333333333333");


                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();

                    Toast.makeText(MainActivity.this, "GOOD JOB", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    Log.d("TAG","Inside SAVE...........4444444444");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("TAG","Inside SAVE...........555555555555");
                    e.printStackTrace();
                }*/
                /*boolean inserted = db.insert(name.getText().toString(),img);
                if (inserted==true){
                    Toast.makeText(MainActivity.this, "Data SAVED", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Data not saved", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Inside Show...........1");
                Cursor cur = db.ViewData();
                Log.d("TAG","22222222222222222222222222");
                while(cur.moveToNext()){
                    Log.d("TAG","Inside Show...........3");
                    if(cur.getString(0)==String.valueOf(name.getText())){
                        Log.d("TAG","Inside Show...........4");
                        img=cur.getBlob(1);
                    }
                }
                Log.d("TAG","Inside Show...........5");
                bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

            }
        });

    }

    private void saveImageToGallery(Bitmap bitmap) {
        OutputStream fos;
        try {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                ContentResolver resolver=getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES+File.separator+"TestFolder");

                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues);


                fos= (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageUri));

                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Objects.requireNonNull(fos);

                Log.d("TAG","File is saved................");
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("TAG","File cannot be saved................");
        }
    }

    public void imagechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Select Picture"),IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==IMAGE_REQUEST_CODE){
                Uri selectedimageuri=data.getData();
                if(selectedimageuri!=null){
                    //image.setImageURI(selectedimageuri);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                selectedimageuri);
                        image.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
/*                    ByteArrayOutputStream byteArray =new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
                    img = byteArray.toByteArray();*/
                }
            }
        }


    }
}