package com.example.selfie.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.MyPreferencesManager;
import com.example.selfie.utils.Order;

/**
 * Created by dpavlov on 14.7.2014 г..
 */
public abstract class MyMenuActivity extends Activity implements MenuFragment.OnFragmentInteractionListener {

    protected MenuFragment menuFragment;
    protected FragmentManager fragmentManager;
    protected FragmentTransaction transaction;

    protected MyPreferencesManager preferencesManager;

    protected boolean menuVisibility = false;

    protected String GENDER;
    protected String TYPE;
    protected String ORDER;

    protected void setFragmentButtonTest(){
        Button genderButton = (Button) findViewById(R.id.gender);
        Button nsfwButton = (Button) findViewById(R.id.nsfw);
        Button orderButton = (Button) findViewById(R.id.get_new);

        if(GENDER.equals("MALE")){
            genderButton.setText("Switch to female");
        } else {
            genderButton.setText("Switch to male");
        }

        if(TYPE.equals(HomeActivity.NSFW)){
            nsfwButton.setText("Switch to SFW");
        } else {
            nsfwButton.setText("Switch to NSFW");
        }

        if(ORDER.equals(Order.ORDERED.toString())){
            orderButton.setText("Randomise");
        } else {
            orderButton.setText("Get new");
        }
    }

    public void onMenuButtonClick(View v){
        transaction = fragmentManager.beginTransaction();
        if(menuVisibility){
            transaction.hide(menuFragment).commit();
        } else {
            transaction.show(menuFragment).commit();
        }
        menuVisibility = !menuVisibility;
    }

    public void onGenderButtonClick(View v){
        Intent intent = new Intent(this, GalleryActivity.class);
        String newGender = null;
        if(GENDER.equals("MALE")){
            newGender = "FEMALE";
        } else {
            newGender = "MALE";
        }
        preferencesManager.setPreferences(MyPreferencesManager.SELFIE_GENDER, newGender);
        startActivity(intent);
    }

    public void nsfwButtonClick(View v){
        Intent intent = new Intent(this, GalleryActivity.class);
        String newType = null;
        if(TYPE.equals(HomeActivity.NSFW)){
            newType = HomeActivity.SFW;
        } else {
            newType = HomeActivity.NSFW;
        }
        preferencesManager.setPreferences(MyPreferencesManager.SELFIE_TYPE, newType);
        startActivity(intent);
    }

    public void orderButtonClick(View v){
        Intent intent = new Intent(this, GalleryActivity.class);
        String newOrder = null;
        if(ORDER.equals(Order.ORDERED)){
            newOrder = Order.RANDOMIZED.toString();
        } else {
            newOrder = Order.ORDERED.toString();
        }
        preferencesManager.setPreferences(MyPreferencesManager.SELFIE_ORDER, newOrder);
        startActivity(intent);
    }

    public void openFavoritesButtonClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.favorite:
                intent = new Intent(this, FavoriteActivity.class);
                break;
            case R.id.my_profile:
                intent = new Intent(this, MyProfileActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
