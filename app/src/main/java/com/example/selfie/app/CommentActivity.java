package com.example.selfie.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.selfie.app.fragments.MenuFragment;
import com.example.selfie.utils.MyPreferencesManager;
import com.example.selfie.utils.Order;
import com.example.selfie.utils.comments.AddComment;
import com.example.selfie.utils.comments.CommentAdapter;
import com.example.selfie.utils.comments.GetComments;


public class CommentActivity extends MyMenuActivity {

    EditText commentText;
    ListView commentList;

    CommentAdapter commentAdapter;

    String currentSelfieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        currentSelfieId = getIntent().getStringExtra(GalleryActivity.SELFIE_ID_KAY);
        preferencesManager = new MyPreferencesManager(this);

        Typeface exoTf = Typeface.createFromAsset(getAssets(), "fonts/Exo2-BoldItalic.ttf");

        /*commentButtonSend = (Button) findViewById(R.id.commentButtonSend);
        commentButtonSend.setTypeface(exoTf);
        commentButtonSend.setTextColor(Color.WHITE);
        commentButtonSend.setBackgroundColor(Color.parseColor("#18A432"));*/

        GENDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_GENDER, "FEMALE");
        TYPE = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_TYPE, "SFW");
        ORDER = preferencesManager.getPreferences(MyPreferencesManager.SELFIE_ORDER, Order.RANDOMIZED.toString());

        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menuFragment);
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment);
        transaction.commit();
        setFragmentButtonTest();

        commentText = (EditText) findViewById(R.id.commentTextField);
        commentList = (ListView) findViewById(R.id.commentList);
        background = (ImageButton) findViewById(R.id.menu_background_comments);

        commentAdapter = new CommentAdapter(null, this);
        commentList.setAdapter(commentAdapter);

        View v = findViewById(R.id.empty_comments_list);
        commentList.setEmptyView(v);

        new GetComments(commentAdapter)
                .execute(GalleryActivity.WEB_SERVICE, currentSelfieId);
    }

    public void onCommentSendClick(View v){
        String commentBody = commentText.getText().toString();
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Account[] list = manager.getAccountsByType("com.google");
        String acc = list[0].name;
        new AddComment(commentAdapter, getApplicationContext())
                .execute(GalleryActivity.WEB_SERVICE, commentBody, acc, currentSelfieId);
        commentText.setText("");

        //Hide V keyboard
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void backComments(View v){
        onBackPressed();
    }

    @Override
    protected void onResume() {
        transaction = fragmentManager.beginTransaction();
        transaction.hide(menuFragment).commit();
        menuVisibility = false;
        super.onResume();
    }
}
