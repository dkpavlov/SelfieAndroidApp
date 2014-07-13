package com.example.selfie.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.selfie.utils.data.SelfieDataSource;
import com.example.selfie.utils.file.FileUtil;
import com.example.selfie.utils.profile.PostSelfie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends Activity {

    ImageView imageView;
    ImageView maleCheckIcon;
    ImageView femaleCheckIcon;
    ImageButton removeImage;
    EditText emailValidation;

    Bitmap bitmap;

    private Uri selectedImageUri = null;
    private Uri outputFileUri;
    private String gender = "";

    SelfieDataSource dataSource;

    private static final int PICTURE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        imageView = (ImageView) findViewById(R.id.preview_selfie);
        maleCheckIcon = (ImageView) findViewById(R.id.men_gender_check);
        femaleCheckIcon = (ImageView) findViewById(R.id.woman_gender_check);
        removeImage = (ImageButton) findViewById(R.id.remove_image);
        emailValidation = (EditText) findViewById(R.id.validate_email);
        dataSource = new SelfieDataSource(this);
        dataSource.open();
    }

    public void onButtonClick(View v){
        switch (v.getId()){
            case R.id.pick_button:
                openImageIntent();
                break;
            case R.id.send_button:
                String jpgBase64 = null;
                try{
                    jpgBase64 = FileUtil.uriToBase64(selectedImageUri, getApplicationContext());

                } catch (IOException e) {
                   Log.e("Profile-Activity", e.getMessage());
                }
                if(selectedImageUri == null || gender.equals("") || !isValidEmailAddress(emailValidation.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Place select picture, gender and provide valid email", Toast.LENGTH_LONG).show();
                } else {
                    new PostSelfie(bitmap, getApplicationContext())
                            .execute(jpgBase64, gender, "SFW", GalleryActivity.WEB_SERVICE);
                }
        }
    }

    public void genderSelect(View v){
        switch (v.getId()){
            case R.id.men_gender:
                gender = "MALE";
                break;
            case R.id.woman_gender:
                gender = "FEMALE";
                break;
        }
        drawGenderSelectChecks();
    }

    public void removeImage(View v){
        imageView.setImageDrawable(null);
        removeImage.setVisibility(View.INVISIBLE);
    }

    private void openImageIntent() {

        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "temp.jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));

        startActivityForResult(chooserIntent, PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == PICTURE_REQUEST_CODE){
            final boolean isCamera;
            if(data == null){
                isCamera = true;
            } else {
                final String action = data.getAction();
                if(action == null){
                    isCamera = false;
                }
                else{
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }
            if(isCamera){
                selectedImageUri = outputFileUri;
            }
            else{
                selectedImageUri = data == null ? null : data.getData();
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
                removeImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void backUpload(View v){
        onBackPressed();
    }

    private void drawGenderSelectChecks(){
        if(gender.equals("MALE")){
            maleCheckIcon.setVisibility(View.VISIBLE);
            femaleCheckIcon.setVisibility(View.INVISIBLE);
        } else {
            maleCheckIcon.setVisibility(View.INVISIBLE);
            femaleCheckIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        String emailRegEx = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        try {
            result = email.matches(emailRegEx);
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
}
