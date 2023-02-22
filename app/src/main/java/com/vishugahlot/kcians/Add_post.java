package com.vishugahlot.kcians;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Add_post extends AppCompatActivity {
    ImageView imagepicker,cancel,profile;
    EditText et_post;
    CardView lottieAnimationView;
    int i = 0,count,p;
    int countitem;
    ArrayList<String  > cate;
    TextView my_uid;
    LinearLayout bt_submit;
    TextView category_button;
    String reference;
    String link="null"
            ,text="null"
            ,image_link="null"
            ,date,email,profileurl,name;

    FirebaseStorage storage;
    StorageReference storageReference;
    String[] cameraPermission;
    String[] storagePermission;
    Uri imageuri;
    boolean a = false;
    int k=0;
    String[] listItems;
    boolean[] checkedItems;
    List<String> selectedItems;
    Calendar c;
    EditText linktext;


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        imageuri=null;
        linktext=findViewById(R.id.link);
        lottieAnimationView=findViewById(R.id.uploading);
        lottieAnimationView.setVisibility(View.VISIBLE);
        profile=findViewById(R.id.profile);
        cancel=findViewById(R.id.cancel_bth);
        imagepicker = findViewById(R.id.imagepickerimage);
        et_post = findViewById(R.id.et_post);
        bt_submit = findViewById(R.id.bt_submit);
        category_button = findViewById(R.id.category_post);
        my_uid=findViewById(R.id.my_uid);
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(Add_post.this);
        email=account.getEmail();

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listItems=new String[((int) snapshot.child("Category").getChildrenCount())];
                for(DataSnapshot snapshot1:snapshot.child("Category").getChildren())
                {


                    listItems[k]=snapshot1.child("CName").getValue(String.class);
                    k++;
                }
                selectedItems= Arrays.asList(listItems);
                checkedItems = new boolean[listItems.length];

                Picasso.get()
                        .load(account.getPhotoUrl()).into(profile);
                name = snapshot.child("users").child(FirebaseAuth.getInstance().getUid()).child("Name").getValue(String.class);
                my_uid.setText(name.toString());
                lottieAnimationView.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cate=new ArrayList<>();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link=null;
                linktext.setText("");
            }
        });

        category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_button.setText(null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_post.this);
                builder.setTitle("Choose Items");
                builder.setIcon(R.drawable.logo);
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                        String currentItem = selectedItems.get(which);
                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        cate.clear();

                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                category_button.setText(category_button.getText() + selectedItems.get(i) + "  ");
                                cate.add(selectedItems.get(i));
                            }
                        }
                        if (selectedItems.size() > 0) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cate=new ArrayList<>();
                        cate.clear();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                category_button.setText(category_button.getText() + selectedItems.get(i) + "  ");
                                cate.add(selectedItems.get(i));

                            }
                        }
                        if (selectedItems.size() > 0) {

                        } else {
                            i = 0;
                        }
                    }
                });
                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cate.clear();
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                        }
                        i = 0;
                    }
                });
                builder.create();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        imagepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Add_post.this).compress(300)
                        .crop().maxResultSize(1080,1080).start(101);
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=false;
                lottieAnimationView.setVisibility(View.VISIBLE);
                text=et_post.getText().toString();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        a=true;
                    }
                }
                if(!text.isEmpty() && a==true)
                {

                    lottieAnimationView.setVisibility(View.VISIBLE);
                    uploadImage();
                }
                else {
                    Toast.makeText(Add_post.this, "category and explaining something is mandatory", Toast.LENGTH_SHORT).show();
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                }


            }
        });


    }
    private void uploadImage()
    {
        if(!linktext.getText().toString().isEmpty())
        {
            link=linktext.getText().toString();

        }
        else
        {
            link="null";
        }

        if (imageuri != null) {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            StorageReference ref = storageReference.child("images"+ UUID.randomUUID().toString());

            ref.putFile(imageuri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {



                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            image_link=uri.toString();
                                            savepost(text,link,image_link);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            lottieAnimationView.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            lottieAnimationView.setVisibility(View.INVISIBLE);
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress=(100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                                }
                            });
        }
        else
        {
            savepost(text,link,image_link);
        }
    }

    private void savepost(String text, String link, String image_link) {

        if(link==null && image_link==null)
        {
            link="null";
            image_link="null";
        }
        else if(image_link==null)
        {
            image_link="null";
        }
        else if(link==null)
        {
            link="null";
        }
        else
        {}
        c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy  'at' HH:mm:ss ");
        date = sdf.format(new Date());

        String finalImage_link = image_link;
        String finalLink = link;

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                FirebaseUser user=auth.getCurrentUser();
                assert user != null;
                String uid= user.getUid();
                countitem=snapshot.child("Total_Posts").getValue(Integer.class);
                int k=countitem-1;
                count=snapshot.child("users").child(uid).child("Total Posts").getValue(Integer.class);

                if(user!=null)
                {
                    GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(Add_post.this);
                    reference=String.valueOf(countitem)+uid+":::"+String.valueOf(count + 1);
                    home_post_data home_post_data=new home_post_data(finalImage_link,text, finalLink,date,name,account.getPhotoUrl().toString(),FirebaseAuth.getInstance().getUid(),0,0 ,reference);

                    home_post_data_for_profile home_post_data_for_profile=new home_post_data_for_profile(finalImage_link,text, finalLink,reference);
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Posts").child(reference).setValue(home_post_data_for_profile);
                    FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Total Posts").setValue(count+1);
                    for (int i = 0; i <cate.size(); i++) {
                        countitem=0;
                        // Toast.makeText(Post_for_home.this, cate.get(i), Toast.LENGTH_SHORT).show();
                        p=i;
                        FirebaseDatabase.getInstance().getReference().child(cate.get(p)).child(reference).setValue(home_post_data);
                    }
                    FirebaseDatabase.getInstance().getReference().child("All").child(reference).setValue(home_post_data);
                   FirebaseDatabase.getInstance().getReference().child("Profile_Search_Post").child(uid).child(String.valueOf(reference)).setValue(home_post_data);
                    FirebaseDatabase.getInstance().getReference().child("Total_Posts").setValue(k);
                    lottieAnimationView.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageuri=data.getData();
        imagepicker.setImageURI(imageuri);

    }

    private void changescr() {
        Intent intent=new Intent(Add_post.this, NavigtionActivity.class);
        startActivity(intent);
    }
}