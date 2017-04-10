package com.example.mayankchauhan.nanochat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button3);
        final EditText text = (EditText) findViewById(R.id.edittext);
        ListView listview = (ListView) findViewById(R.id.listview);

        final DatabaseReference ref = FireApp.databaseReference;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatMesssage chat = new ChatMesssage("puf", text.getText().toString());
                ref.push().setValue(chat);
                text.setText("");

            }
        });
        final List<ChatMesssage> message = new LinkedList<>();

        final ArrayAdapter<ChatMesssage>  adapter = new ArrayAdapter<ChatMesssage>(this,android.R.layout.two_line_list_item,message)

        {

            @NonNull
            @Override
            public View getView(int position, View view, ViewGroup parent) {

                if(view == null)
                {
                    view = getLayoutInflater().inflate(android.R.layout.two_line_list_item,parent,false);
                }
                return view;
            }
        };
        listview.setAdapter(adapter);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ChatMesssage chat = dataSnapshot.getValue(ChatMesssage.class);
                message.add(chat);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
