package com.example.selfie.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.selfie.utils.comments.AddComment;
import com.example.selfie.utils.comments.CommentAdapter;
import com.example.selfie.utils.comments.GetComments;


public class CommentActivity extends Activity {

    Button commentButtonSend;
    EditText commentText;
    ListView commentList;

    CommentAdapter commentAdapter;

    String currentSelfieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        currentSelfieId = getIntent().getStringExtra(GalleryActivity.SELFIE_ID_KAY);

        Typeface exoTf = Typeface.createFromAsset(getAssets(), "fonts/Exo2-BoldItalic.ttf");

        /*commentButtonSend = (Button) findViewById(R.id.commentButtonSend);
        commentButtonSend.setTypeface(exoTf);
        commentButtonSend.setTextColor(Color.WHITE);
        commentButtonSend.setBackgroundColor(Color.parseColor("#18A432"));*/

        commentText = (EditText) findViewById(R.id.commentTextField);
        commentList = (ListView) findViewById(R.id.commentList);

        commentAdapter = new CommentAdapter(null, this);
        commentList.setAdapter(commentAdapter);

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
    }


}
