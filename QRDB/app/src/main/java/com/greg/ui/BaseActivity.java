package com.greg.ui;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.greg.ui.CreateNewQrCodeActivity;
import com.greg.ui.MyQrCodesActivity;
import com.greg.qrdb.R;
import com.greg.ui.AboutActivity;
import com.greg.ui.ScanActivity;
import com.greg.ui.ScannedQrCodesActivity;

/**
 * Created by Greg on 23-10-2016.
 */
public class BaseActivity  extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    public void initBaseActivity(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id){
                   case R.id.about:
                       drawerLayout.closeDrawers();
                       Intent goToAboutActivity = new Intent(getApplicationContext(), AboutActivity.class);
                       startActivity(goToAboutActivity);
                       finish();
                       break;
                    case R.id.create_qr_code:
                        drawerLayout.closeDrawers();
                        Intent goToCreateNewQrCodeActivity = new Intent(getApplicationContext(), CreateNewQrCodeActivity.class);
                        startActivity(goToCreateNewQrCodeActivity);
                        finish();
                        break;
                    case R.id.my_qr_codes:
                        drawerLayout.closeDrawers();
                        Intent goToMyQrCodesActivity = new Intent(getApplicationContext(), MyQrCodesActivity.class);
                        startActivity(goToMyQrCodesActivity);
                        finish();
                        break;
                    case R.id.scan_qr_code:
                        drawerLayout.closeDrawers();
                        Intent goToScanActivity = new Intent(getApplicationContext(), ScanActivity.class);
                        startActivity(goToScanActivity);
                        finish();
                        break;
                    case R.id.scanned_qr_codes:
                        drawerLayout.closeDrawers();
                        Intent goToScannedQrCodesActivity = new Intent(getApplicationContext(), ScannedQrCodesActivity.class);
                        startActivity(goToScannedQrCodesActivity);
                        finish();
                        break;

                }
                return true;
            }
        });
        /*View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        tv_email.setText("raj.amalw@learn2crack.com");
        */
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


}
