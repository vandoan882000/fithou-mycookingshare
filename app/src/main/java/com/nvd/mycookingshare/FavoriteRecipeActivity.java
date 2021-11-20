package com.nvd.mycookingshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteRecipeActivity extends AppCompatActivity {

    RecyclerView listViewRecipe;
    List<Recipe> recipes;
    DatabaseReference mData;
    RecipeListsAdapter adapter;
    String userid;
    Account acc;
    ArrayList<String> arr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_recipe);
        mData = FirebaseDatabase.getInstance().getReference();
        loadDataFavorite();
        listViewRecipe = (RecyclerView) findViewById(R.id.lstFavorite);
        Intent igetId = getIntent();
        userid = igetId.getStringExtra("userid");
        // When the user clicks on the ListItem
        recipes = new ArrayList<Recipe>();
        adapter = new RecipeListsAdapter(getApplicationContext(),recipes,userid);
        listViewRecipe.setHasFixedSize(true);
        listViewRecipe.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        listViewRecipe.setAdapter(adapter);

        loadData();
//        listViewRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Recipe a = (Recipe) parent.getItemAtPosition(position);
//                Intent intent = new Intent(FavoriteRecipeActivity.this, ViewRecipeActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("img", a.img);
//                bundle.putString("name", a.ten);
//                bundle.putStringArrayList("tp",a.chuanbi);
//                bundle.putSerializable("mystep",(Serializable) a.mystep);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    private void loadData()
    {

        mData.child("Recipe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Recipe recipe = snapshot.getValue(Recipe.class);
                String idTemp = snapshot.getKey();
                if(arr.contains(idTemp))
                {
                    Collections.reverse(recipes);
                    recipes.add(recipe);
                    Collections.reverse(recipes);
                    adapter.notifyDataSetChanged();
                    mData.child("Recipe/"+idTemp).setValue(recipe, new DatabaseReference.CompletionListener() {
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

    private void loadDataFavorite()
    {

        mData.child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChildName) {
                String accid = snapshot.getKey();
                if(accid.equals(userid))
                {
                    acc  = snapshot.getValue(Account.class);
                    arr = acc.listFavoryte;


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