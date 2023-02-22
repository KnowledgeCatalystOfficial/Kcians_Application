package com.vishugahlot.kcians;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class home_posts_recycler extends RecyclerView.Adapter<home_posts_recycler.Holder> {
    Context context;
    ArrayList<home_post_data> home_post_d;
    Calendar c;
    String date="";
    long followers_count,followings_count;
    int shares,k;
    public static String urlsd=" j";

    public home_posts_recycler(Context context, ArrayList<home_post_data> home_post_d) {
        this.context=context;
        this.home_post_d=home_post_d;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.posts,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {




        if(!home_post_d.get(position).getImageurl().equals("null"))
        {
            Picasso.get()
                    .load(String.valueOf(home_post_d.get(position).getImageurl()))
                    .into(holder.postimg);
        }
        else {
            holder.postimg.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(String.valueOf(home_post_d.get(position).getProfileurl()))
                .into(holder.profileimg);

        holder.posttext.setText(home_post_d.get(position).getPost_link());
        urlsd=holder.posttext.getText().toString();
        holder.posttext.setText(home_post_d.get(position).getPost_text());


        holder.itemView.setTag(home_post_d.get(position).getPost_link());
        holder.postdate.setText(home_post_d.get(position).getDate());
        holder.postby.setText("by "+home_post_d.get(position).getName());
        holder.postlikestext.setText(String.valueOf(home_post_d.get(position).getLike()));
        holder.save_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy  'at' HHmmss");
                date = sdf.format(new Date());
                try
                {
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("Saved Posts").child(date).setValue(home_post_d.get(position));

                }
                catch (Exception e)

                {
                }
            }
        });
        holder.share_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try
                        {
                            shares=snapshot.child("Profile info").child(home_post_d.get(position).getUid()).child("Likes").getValue(Integer.class);
                            int k=shares+1;
                            FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {


                                    String myuid=snapshot.child("users").child(FirebaseAuth.getInstance().getUid()).child("College UID").getValue(String.class);
                                    if(!myuid.equals(home_post_d.get(position).getUid()))
                                    {
                                        FirebaseDatabase.getInstance().getReference().child("Profile info").child(home_post_d.get(position).getUid()).child("Likes").setValue(k);

                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                holder.postimg.setDrawingCacheEnabled(true);
                holder.postimg.buildDrawingCache();
                Bitmap image = holder.postimg.getDrawingCache();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                if(image!=null)
                {
                    share.putExtra(Intent.EXTRA_STREAM, getImageUri(context,image));
                }
                else
                {
                    share.setType("text/plain");
                }

                String s=(home_post_d.get(position).getPost_link().equals("null"))?" ":"Link : "+home_post_d.get(position).getPost_link()+"\n";
                String shareMessage= home_post_d.get(position).getPost_text()+"\n\n"+s+"\nConnect to cu on::\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                share.putExtra(Intent.EXTRA_TEXT, shareMessage);
                context.startActivity(Intent.createChooser(share,"Share via"));
            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  'at' HH:mm:ss");
                date = sdf.format(new Date());

                FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        String myuid=snapshot.child("users").child(FirebaseAuth.getInstance().getUid()).child("College UID").getValue(String.class);
                        if(!myuid.equals(home_post_d.get(position).getUid()))
                        {
                            followdata f=new followdata(home_post_d.get(position).getUid(),date);
                            followdata f2=new followdata(myuid,date);
                            FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("Following_list").child(home_post_d.get(position).getUid()).setValue(f);
                            FirebaseDatabase.getInstance().getReference().child("Profile info").child(home_post_d.get(position).getUid()).child("Followers_list").child(myuid).setValue(f2);

                            FirebaseDatabase.getInstance().getReference().child("Profile info").child(home_post_d.get(position).getUid()).child("Followers_list").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    followers_count=snapshot.getChildrenCount();
                                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("Following_list").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            followings_count=snapshot.getChildrenCount();
                                            FirebaseDatabase.getInstance().getReference().child("Profile info").child(myuid).child("Following").setValue(Integer.valueOf((int) followings_count));
                                            FirebaseDatabase.getInstance().getReference().child("Profile info").child(home_post_d.get(position).getUid()).child("Followers").setValue(Integer.valueOf((int) followers_count));

                                            Toast.makeText(context, "you started following "+home_post_d.get(position).getUid(), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                        else
                        {
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        File imagersfolder=new File(context.getCacheDir(),"images");
        Uri uri=null;


        imagersfolder.mkdir();
        File file=new File(imagersfolder,"capturedimage.jpg");
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            uri = FileProvider.getUriForFile(context.getApplicationContext(),"com.abhishektiwari.cu_connect"+".provider",file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  uri;
    }
    @Override
    public int getItemCount() {
        return home_post_d.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView postimg,profileimg;
        ImageButton save_post,follow,share_post;
        TextView posttext,postlikestext,postdate,postby;
        public Holder(@NonNull View itemView) {
            super(itemView);
            postlikestext=itemView.findViewById(R.id.postliketext);
            postimg=itemView.findViewById(R.id.postimg);
            posttext=itemView.findViewById(R.id.posttext);
            postdate=itemView.findViewById(R.id.date);
            postby=itemView.findViewById(R.id.postby);
            follow=itemView.findViewById(R.id.postfollow);
            save_post=itemView.findViewById(R.id.post_save);
            share_post=itemView.findViewById(R.id.postshare);
            profileimg=itemView.findViewById(R.id.profileimg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    urlsd=itemView.getTag().toString();
                    if(urlsd.toString().equals("null"))
                    {
                    }
                    else
                    {

                    }
                }
            });

        }


    }


}
