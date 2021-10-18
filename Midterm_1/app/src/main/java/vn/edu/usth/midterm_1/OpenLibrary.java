package vn.edu.usth.midterm_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class OpenLibrary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_BOOK = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_SUBJECTS = 2;

    private int mCurrentFragment = FRAGMENT_BOOK;

    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);


        abdt = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout.addDrawerListener(abdt);
        abdt.syncState();

        NavigationView navigationView2 = findViewById(R.id.nav_view2);
        navigationView2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.open_library) {
                    Toast.makeText(OpenLibrary.this, "Open Library", Toast.LENGTH_SHORT).show();
                }
                else if (id == R.id.rate_us) {
                    Toast.makeText(OpenLibrary.this, "Rate Us", Toast.LENGTH_SHORT).show();
                }
                else if (id == R.id.more_apps) {
                    Toast.makeText(OpenLibrary.this, "More Apps", Toast.LENGTH_SHORT).show();
                }
                else if (id == R.id.freebies_and_hot_deals) {
                    Toast.makeText(OpenLibrary.this, "Freebies and hot deals", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new BookFragment());
        setTitle("Open Library");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_search:
//                if (mCurrentFragment != FRAGMENT_SEARCH){
//                    replaceFragment(new SearchFragment());
//                    setTitle("Search");
//                    mCurrentFragment = FRAGMENT_SEARCH;
//                }
//                return true;
//            case R.id.settings:
//                mDrawerLayout.openDrawer(GravityCompat.END);
//                item.setChecked(true);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.author){
            Toast.makeText(OpenLibrary.this, "Get to author page", Toast.LENGTH_LONG).show();
        } else if(id == R.id.subjects){
            if (mCurrentFragment != FRAGMENT_SUBJECTS){
                replaceFragment(new SubjectsFragment());
                mCurrentFragment = FRAGMENT_SUBJECTS;
                setTitle("SUBJECTS");
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }else if (mCurrentFragment != FRAGMENT_BOOK) {
            setTitle(R.drawable.logo);
            replaceFragment(new BookFragment());
            mCurrentFragment = FRAGMENT_BOOK;
        }else {
            super.onBackPressed();
        }
    }

    //    private void changeActivity(){
//        Intent intent = new Intent(OpenLibrary.this, SearchFragment.class);
//        startActivity(intent);
//    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commit();
    }
}