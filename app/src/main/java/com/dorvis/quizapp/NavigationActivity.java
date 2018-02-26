package com.dorvis.quizapp;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;



import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.dorvis.quizapp.fragments.FirstHomeFragment;
import com.dorvis.quizapp.sql.SessionManager;

import java.io.File;


public class NavigationActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    private SessionManager sessionManager;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ImageView imageView;
    android.app.FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    //nav_header navigation view item
    ImageView profile_imgView;
    TextView username_txtview;
    // header.xml action bar item
    ImageView signin_img;
    TextView textViewName;

    //navigation menu floder
    ImageView imageView_Share;
    ImageView imageView_navsend;
    ImageView imageView_navNotification;


    //home fragment imageview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


       initViews();




        imageView_Share = (ImageView) findViewById(R.id.header_share);
        imageView_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });


        FragmentManager manager = this.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, new FirstHomeFragment()).commit();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);
        mNavigationView.removeHeaderView(null);

        mNavigationView.removeHeaderView(mNavigationView.getHeaderView(0));
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_navigation, null);
        mNavigationView.addHeaderView(header);


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                int id = item.getItemId();
                if (id == R.id.nav_sign_out) {
                    startActivity(new Intent(NavigationActivity.this, LoginActivity.class));

                } else if (id == R.id.nav_tech_news) {
                    startActivity(new Intent(NavigationActivity.this, NewsActivity.class));

                } else if (id == R.id.nav_notification) {



                }else if (id == R.id.nav_share){
                    showAlertDialog();

                }else if (id == R.id.nav_send){
                    showContactUsDialog();
                }else if (id == R.id.header_signout){
                    logout();
                }


                return false;
            }
        });



        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


    }

    private void logout() {
        sessionManager.setLoggedin(true);
        finish();
        startActivity(new Intent(NavigationActivity.this,LoginActivity.class));


    }

    private void showAlertDialog() {
      new AlertDialog.Builder(this)
              .setMessage("Choose action for share this app...")
              .setPositiveButton("BLUETOOTH", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      shareApplicationByBluetooth();
                  }
              }).setNegativeButton("OTHERS", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              shareApplicationByShareit();

          }
      }).create().show();

    }

    private void shareApplicationByShareit() {
        PackageManager pm = getApplicationContext().getPackageManager();
        ApplicationInfo appInfo;
        try{
             appInfo = pm.getApplicationInfo(getApplicationContext().getPackageName(),PackageManager.GET_META_DATA);
             Intent sendBt = new Intent(Intent.ACTION_SEND);
             sendBt.setType("*/*");
            sendBt.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(appInfo.publicSourceDir)));
            sendBt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(sendBt, "Share it using"));

        }catch (PackageManager.NameNotFoundException e1){
            e1.printStackTrace();
        }
    }

    private void shareApplicationByBluetooth() {
        try {
            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filepath = app.sourceDir;
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filepath)));
            startActivity(Intent.createChooser(intent,"Share app"));
            Toast.makeText(getApplicationContext(),"Share the Programming quiz by bluetooth ", Toast.LENGTH_LONG).show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initViews(){
        //signin_img = (ImageView) findViewById(R.id.header_signin);
        username_txtview = (TextView) findViewById(R.id.userName_txtview);
        profile_imgView = (ImageView) findViewById(R.id.imageView_profile);
        imageView_navsend =(ImageView)findViewById(R.id.nav_send);
        imageView_navNotification =(ImageView)findViewById(R.id.nav_notification);

    }
    private void showContactUsDialog() {
        new AlertDialog.Builder(this)
                .setMessage("For any queries contact this email \n sainathhiwale5@gmail.com")
                .setPositiveButton("send email", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:sainathhiwale5@gmail.com")));
                    }
                }).create().show();

           }






}

