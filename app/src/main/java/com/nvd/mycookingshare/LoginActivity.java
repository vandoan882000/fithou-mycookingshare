package com.nvd.mycookingshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText txtUserName,txtPassWord;
    Button btnLogin,btnRegister;
    DatabaseReference mData;
    Account acc;
    String keyid ;
    Boolean ktPass;
    ArrayList<String> listUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getView();

        mData = FirebaseDatabase.getInstance().getReference();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                //Toast.makeText(LoginActivity.this,"Sai tài khoản hoặc mật khẩu",Toast.LENGTH_SHORT).show();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setTitle("Đăng kí");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.activity_register);
                EditText txtUserNameDk = (EditText)dialog.findViewById(R.id.txtBuocThem);
                EditText txtPassWordDk1 = (EditText)dialog.findViewById(R.id.txtPasswordDk1);
                EditText txtPassWordDk2 = (EditText)dialog.findViewById(R.id.txtPasswordDk2);
                Button btnRegisterDk = (Button)dialog.findViewById(R.id.btnRegisterDk);
                Button btnThoatDk = (Button)dialog.findViewById(R.id.btnThoatDk);
                listUserName = new ArrayList<String>();
                loadDataUserName();
                btnRegisterDk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(listUserName.contains(txtUserNameDk.getText().toString().trim()))
                        {
                            Toast.makeText(LoginActivity.this,"Tài khoản đã tồn tại",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {

                            if(txtPassWordDk1.getText().toString().trim().equals(txtPassWordDk2.getText().toString().trim()))
                            {
                                ArrayList<String> lstFavorite = new ArrayList<String>();
                                lstFavorite.add("Mo1Hx4O0-sEdwDy2TxK");
                                lstFavorite.add("Mo1I0oQeN1Z8WtkbZtC");
                                lstFavorite.add("Mo2TCjgmpqdlpX-VsOu");
                                lstFavorite.add("Mo2Y6G75ZUkBitzXW1F");
                                Account account = new Account(txtUserNameDk.getText().toString().trim(),txtPassWordDk1.getText().toString().trim(),lstFavorite);
                                mData.child("Account").push().setValue(account, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if(error == null)
                                        {
                                            dialog.cancel();
                                            Toast.makeText(LoginActivity.this,"Đăng kí thành công",Toast.LENGTH_LONG).show();

                                        }
                                        else
                                        {
                                            Toast.makeText(LoginActivity.this,"Đăng kí thất bại",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Mật khẩu nhập lại không đúng",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                btnThoatDk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtPassWordDk1.setText("");
                        txtPassWordDk2.setText("");
                        dialog.cancel();
                    }
                });
                dialog.show();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(layoutParams);


            }
        });
    }
    public void getView()
    {
        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtPassWord = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
    }
    private void loadData()
    {

        mData.child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Account acctemp = snapshot.getValue(Account.class);

                if(txtPassWord.getText().toString().trim().equals(acctemp.getPass())&&txtUserName.getText().toString().trim().equals(acctemp.getUsername()))
                {
                    acc = snapshot.getValue(Account.class);

                    keyid = snapshot.getKey();
                    Intent i1 = new Intent(LoginActivity.this,MainTabActivity.class);
                    i1.putExtra("userid",keyid);
                    startActivity(i1);

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
    private void loadDataUserName()
    {

        mData.child("Account").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Account acctemp = snapshot.getValue(Account.class);
                listUserName.add(acctemp.username);

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