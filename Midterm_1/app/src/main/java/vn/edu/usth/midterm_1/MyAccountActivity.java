package vn.edu.usth.midterm_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class MyAccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_BOOK = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_SUBJECTS = 2;
    private static final int FRAGMENT_MY_BOOK = 4;
    private int mCurrentFragment = FRAGMENT_MY_BOOK;
    

    private ImageView logoImage;
    private boolean search;

    private ActionBarDrawerToggle abdt;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);


        abdt = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout.addDrawerListener(abdt);
        abdt.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        logoImage = (ImageView) findViewById(R.id.logo);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, OpenLibrary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_search:
//                if (mCurrentFragment != FRAGMENT_SEARCH) {
//                    replaceFragment(new SearchFragment());
//                    setTitle("Search");
//                    mCurrentFragment = FRAGMENT_SEARCH;
//                }
                Intent intent = new Intent(MyAccountActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_menu:
                mDrawerLayout.openDrawer(GravityCompat.END);
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        else if(id == R.id.subjects){
//            if (mCurrentFragment != FRAGMENT_SUBJECTS){
//                replaceFragment(new SubjectsFragment());
//                mCurrentFragment = FRAGMENT_SUBJECTS;
//                setTitle("SUBJECTS");
//            }
//        }
        if (id == R.id.subjects) {
            Intent intent = new Intent(MyAccountActivity.this, SubjectActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.my_books) {
            Intent intent = new Intent(MyAccountActivity.this, MyBookActivity.class);
            startActivity(intent);
        }
//        else if (id == R.id.favorites) {
//            Intent intent = new Intent(MyAccountActivity.this, FavoriteActivity.class);
//            startActivity(intent);
//        }
        else if (id == R.id.my_account) {
            Intent intent = new Intent(MyAccountActivity.this, MyAccountActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.log_out) {
            Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            super.onBackPressed();
        }
    }
}