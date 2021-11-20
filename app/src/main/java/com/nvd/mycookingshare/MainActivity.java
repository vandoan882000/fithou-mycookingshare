package com.nvd.mycookingshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    EditText txtName,txtType,txtThemTP;
    ImageView imgCook,imgTemp;
    Button btnAdd,btnGetImg1,btnGetImg2,btnThanhPhan,btnThemBuoc;
    ListView lstThanhPhan,lstStep;
    MyStepListAdapter myStepListAdapter;
    DatabaseReference mData;
    int REQUESTCODE_IMAGE_CAP=1;
    int REQUESTCODE_IMAGE=1;
    ArrayList<String> thanhPhan;
    List<MyStep> stepList;
    Bitmap bit1;
    Boolean imgBit;
    Spinner spinnerType;
    String txtTypes;
    int heigh = 0;
    LinearLayout layoutStep;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://mycookingshare.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database
        getview();
        Intent igetId = getIntent();
        String userid = igetId.getStringExtra("userid");
        thanhPhan = new ArrayList<String>();

        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,thanhPhan);

        lstThanhPhan.setAdapter(arrayAdapter);
        btnGetImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                  startActivityForResult(intent,REQUESTCODE_IMAGE);
            }
        });
        btnGetImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(intent,100);
            }
        });
        ArrayList<String> listType = new ArrayList<String>();
        listType.add("Nấu");
        listType.add("Xào");
        listType.add("Rán");
        listType.add("Luộc");
        listType.add("Nướng");
        ArrayAdapter adapterType = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item,listType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtTypes = listType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // When the user clicks on the ListItem
        stepList = new ArrayList<MyStep>();
        myStepListAdapter = new MyStepListAdapter(stepList,MainActivity.this);
        lstStep.setAdapter(myStepListAdapter);

        btnThanhPhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tp = txtThemTP.getText().toString();
                thanhPhan.add(tp);
                arrayAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(lstThanhPhan);
            }
        });
        btnThemBuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("Thêm bước");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.add_recipe_step);
                EditText txtBuocThem = (EditText)dialog.findViewById(R.id.txtBuocThem);
                Button btnThemB = (Button)dialog.findViewById(R.id.btnThemBuoc);
                Button btnGetImgStep1 = (Button)dialog.findViewById(R.id.btnGetImgStep1);
                Button btnGetImgStep2 = (Button)dialog.findViewById(R.id.btnGetImgStep2);
                ImageView imgThemBuoc = (ImageView)dialog.findViewById(R.id.imgBuocLam);


                btnGetImgStep1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgBit = true;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,123);
                        imgTemp = (ImageView)dialog.findViewById(R.id.imgBuocLam);

                    }
                });
                btnGetImgStep2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                        startActivityForResult(intent,1234);
                        imgTemp = (ImageView)dialog.findViewById(R.id.imgBuocLam);
                    }
                });
                dialog.show();
                btnThemB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        StorageReference mountainsRef = storageRef.child("img_"+calendar.getTimeInMillis()+"_recipe_step.jpg");
                        // Get the data from an ImageView as bytes
                        imgTemp.setDrawingCacheEnabled(true);
                        imgTemp.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) imgTemp.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = mountainsRef.putBytes(data);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child("img_"+calendar.getTimeInMillis()+"_recipe_step.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        MyStep step = new MyStep(task.getResult()+"",txtBuocThem.getText().toString());
                                        stepList.add(step);
                                        myStepListAdapter.notifyDataSetChanged();
                                        setListViewHeightBasedOnChildren1(lstStep);

//                                        LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                                        lstStep.setLayoutParams (mParam);
//                                        LinearLayout.LayoutParams mParam1 = new LinearLayout.LayoutParams (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                                        layoutStep.setLayoutParams(mParam1);
                                        dialog.cancel();
                                    }
                                });


                            }
                        });
                    }
                });
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(layoutParams);



            }

        });
        mData = FirebaseDatabase.getInstance().getReference();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("img_"+calendar.getTimeInMillis()+"_recipe.jpg");
                // Get the data from an ImageView as bytes
                imgCook.setDrawingCacheEnabled(true);
                imgCook.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imgCook.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageRef.child("img_"+calendar.getTimeInMillis()+"_recipe.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                Recipe recipe = new Recipe("id",txtName.getText().toString(),task.getResult()+"",thanhPhan,txtTypes,stepList,userid);
                                mData.child("Recipe").push().setValue(recipe, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if(error == null)
                                        {
                                            Toast.makeText(MainActivity.this,"Lưu dữ liệu thành công",Toast.LENGTH_LONG).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Lưu dữ liệu thất bại",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void getview()
    {
        txtName = (EditText)findViewById(R.id.txtName);
        txtThemTP = (EditText)findViewById(R.id.txtThemTP);
        imgCook = (ImageView)findViewById(R.id.imgCook);
        btnGetImg1 = (Button)findViewById(R.id.btnGetImg1);
        btnGetImg2 = (Button)findViewById(R.id.btnGetImg2);
        btnThanhPhan = (Button)findViewById(R.id.btnThanhPhan);
        btnThemBuoc = (Button)findViewById(R.id.btnThemBuoc);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        lstThanhPhan = (ListView)findViewById(R.id.lstThanhPhan);
        lstStep = (ListView)findViewById(R.id.lstCacBuoc);
        spinnerType = (Spinner)findViewById(R.id.spType);
        layoutStep = (LinearLayout)findViewById(R.id.layoutStep);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUESTCODE_IMAGE && resultCode == RESULT_OK && data != null)
        {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imgCook.setImageBitmap(bitmap);

        }
        if (requestCode==100 && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            imgCook.setImageURI(uri);

        }
        if(requestCode == 123 && resultCode == RESULT_OK && data != null)
        {
            bit1 = (Bitmap)data.getExtras().get("data");
            imgTemp.setImageBitmap(bit1);
        }
        if (requestCode==1234 && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            imgTemp.setImageURI(uri);

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
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

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += 500;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }




}