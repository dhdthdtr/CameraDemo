package com.example.camerademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_camera:
                showCamera();
                return true;
            case R.id.action_email:
                emailPicture();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCamera(){
        String FILE_NAME = "photo";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            File imageFile = File.createTempFile(FILE_NAME, ".jpg", storageDir);
            currentPhotoPath = imageFile.getAbsolutePath();
            fileUri = FileProvider.getUriForFile(MainActivity.this, "com.example.camerademo.fileprovider", imageFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void emailPicture(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/image");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nguyenvubang011001hiie@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New photo");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "From My App");
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            ImageView imageView = findViewById(R.id.imageView);
            Bitmap avatarImg = BitmapFactory.decodeFile(currentPhotoPath);
            imageView.setImageBitmap(avatarImg);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}