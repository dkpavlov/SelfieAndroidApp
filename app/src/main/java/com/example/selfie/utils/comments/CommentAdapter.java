package com.example.selfie.utils.comments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dpavlov on 18.6.2014 г..
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    private Context context;
    private List<Comment> commentList;


    public CommentAdapter(List<Comment> itemList, Context ctx) {
        super(ctx, android.R.layout.simple_list_item_2, itemList);
        this.commentList = itemList;
        this.context = ctx;
    }

    @Override
    public int getCount() {
        if(commentList != null){
            return commentList.size();
        }
        return 0;
    }

    @Override
    public Comment getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (commentList != null) {
            return commentList.get(i).hashCode();
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_2, null);

        }
        Comment comment = getItem(i);

        TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
        textView1.setText(comment.getText());
        textView2.setText(comment.getUser());

        return view;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
