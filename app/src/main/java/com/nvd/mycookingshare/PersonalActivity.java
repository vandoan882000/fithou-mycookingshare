package com.nvd.mycookingshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class PersonalActivity extends AppCompatActivity {

    TextView txtUserId;
    ListView lstPersonalOption;
    DatabaseReference mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        getView();

        Intent iget = getIntent();
        String userId = iget.getStringExtra("userid");
        txtUserId.setText("Tài khoản : "+userId);
        ArrayList<String> option = new ArrayList<String>();
        option.add("Công thức yêu thích");
        option.add("Công thức của tôi");
        option.add("Cài đặt");
        option.add("Đăng xuất");
        ArrayAdapter<String> chuanbiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,option);
        lstPersonalOption.setAdapter(chuanbiAdapter);
        lstPersonalOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Intent intent1 = new Intent(PersonalActivity.this, FavoriteRecipeActivity.class);
                    intent1.putExtra("userid",userId);
                    startActivity(intent1);
                }
                else if(position==1)
                {
                    Intent intent2 = new Intent(PersonalActivity.this, MyRecipeActivity.class);
                    intent2.putExtra("userid",userId);
                    startActivity(intent2);
                }
                else if(position==2)
                {

                }
                else if(position==3)
                {
                    Intent intent3 = new Intent(PersonalActivity.this, LoginActivity.class);
                    intent3.putExtra("userid",userId);
                    startActivity(intent3);
                }
            }
        });
//        mData = FirebaseDatabase.getInstance().getReference();
//        ArrayList<String> lstFavorite = new ArrayList<String>();
//        lstFavorite.add("b");
//        lstFavorite.add("Mo1I0oQeN1Z8WtkbZtC");
//        lstFavorite.add("Mo2TCjgmpqdlpX-VsOu");
//        lstFavorite.add("Mo2Y6G75ZUkBitzXW1F");
//        mData.child("Account/-Mo8X9iVCyOpmIypQ1nT/listFavoryte").setValue(lstFavorite, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                if(error == null)
//                {
//                    Toast.makeText(PersonalActivity.this,"Lưu dữ liệu thành công",Toast.LENGTH_LONG).show();
//
//                }
//                else
//                {
//                    Toast.makeText(PersonalActivity.this,"Lưu dữ liệu thất bại",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
    public void getView()
    {
        txtUserId = (TextView)findViewById(R.id.txtIdUser);
        lstPersonalOption = (ListView)findViewById(R.id.lstPersonalOption);
    }
    private void loadData()
    {

        mData.child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Account acc= snapshot.getValue(Account.class);
                if(acc.listFavoryte.contains(""))
                {

                }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}