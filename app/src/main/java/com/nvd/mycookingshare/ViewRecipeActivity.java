package com.nvd.mycookingshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewRecipeActivity extends AppCompatActivity {

    ImageView imgMonAn;
    TextView txtTenMon,txtThanhPhan;
    ListView lstChuanbiShow;
    ListView lstStep;
    CheckBox checkFavorite;
    DatabaseReference mData;
    Account acc;
    ArrayList<String> arr;

    String tenMon,urlImg,uid,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        ArrayList<String> chuanBi;
        ArrayList<String> st;
        tenMon =  bundle.getString("name");
        urlImg =  bundle.getString("img");
        uid =  bundle.getString("uid");
        id =  bundle.getString("id");

        chuanBi =  bundle.getStringArrayList("tp");
        List<MyStep> mySteps = (ArrayList<MyStep>) bundle.getSerializable("mystep");
//        String k = keyRecipe.get(i);
//        Toast.makeText(this,""+k,Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> chuanbiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,chuanBi);
        lstChuanbiShow.setAdapter(chuanbiAdapter);

        MyStepListAdapter myStepListAdapter = new MyStepListAdapter(mySteps,ViewRecipeActivity.this);
        lstStep.setAdapter(myStepListAdapter);
        setListViewHeightBasedOnChildren(lstChuanbiShow);
        setListViewHeightBasedOnChildren1(lstStep);

        txtTenMon.setText(tenMon);
        Picasso.get().load(urlImg).into(imgMonAn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mData = FirebaseDatabase.getInstance().getReference();
         loadData();

        checkFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkFavorite.isChecked())
                {
                    if(arr.contains(id))
                    {

                    }
                    else
                    {
                        arr.add(id);
                        mData.child("Account/"+uid+"/listFavoryte").setValue(arr, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if(error == null)
                                {
                                    //Toast.makeText(PersonalActivity.this,"Lưu dữ liệu thành công",Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    // Toast.makeText(PersonalActivity.this,"Lưu dữ liệu thất bại",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                }
                else
                {
                    arr.remove(id);
                    mData.child("Account/"+uid+"/listFavoryte").setValue(arr, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if(error == null)
                            {
                                //Toast.makeText(PersonalActivity.this,"Lưu dữ liệu thành công",Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                // Toast.makeText(PersonalActivity.this,"Lưu dữ liệu thất bại",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getView()
    {
        imgMonAn = (ImageView)findViewById(R.id.imgMonAn);
        txtTenMon = (TextView)findViewById(R.id.txtTenMon);
        txtThanhPhan = (TextView)findViewById(R.id.txtThanhPhan);
        lstStep = (ListView)findViewById(R.id.lstStep);
        lstChuanbiShow = (ListView)findViewById(R.id.lstChuanBiShow);
        checkFavorite = (CheckBox)findViewById(R.id.checkFavorite);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 150;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += 150;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static void setListViewHeightBasedOnChildren1(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 700;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += 650;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private void loadData()
    {

        mData.child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                String accid = snapshot.getKey();
                if(accid.equals(uid))
                {
                    acc  = snapshot.getValue(Account.class);
                    arr = acc.listFavoryte;
                    if(arr.contains(id))
                    {
                        checkFavorite.setChecked(true);
                    }
                    else
                    {
                        checkFavorite.setChecked(false);
                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}