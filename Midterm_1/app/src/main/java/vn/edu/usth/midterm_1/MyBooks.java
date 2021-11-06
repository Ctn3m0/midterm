package vn.edu.usth.midterm_1;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MyBooks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    private static final int FRAGMENT_BOOK = 0;
    private static final int FRAGMENT_SEARCH = 1;
    private static final int FRAGMENT_SUBJECTS = 2;

    private int mCurrentFragment = FRAGMENT_BOOK;
    private ImageView menuImage;
    private ImageView searchImage;

    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);


        abdt = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout.addDrawerListener(abdt);
        abdt.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new BookFragment());
        setTitle("My Books");

        menuImage = (ImageView) findViewById(R.id.menu_toolbar);
        menuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        searchImage = (ImageView) findViewById(R.id.menu_search);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentFragment != FRAGMENT_SEARCH){
                    replaceFragment(new SearchFragment());
                    setTitle("Search");
                    mCurrentFragment = FRAGMENT_SEARCH;
                }
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_navigation_header, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }

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
            Toast.makeText(MyBooks.this, "Get to author page", Toast.LENGTH_LONG).show();
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
            setTitle("Open Library");
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