package com.example.gameexplorer.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.gameexplorer.R;
import com.example.gameexplorer.firebaseHelper.FirebaseStorageHelper;
import com.example.gameexplorer.firebaseHelper.RealTimeDatabaseHelper;
import com.example.gameexplorer.firebaseHelper.UserAuthenticationHelper;
import com.example.gameexplorer.fragment.creatorFragments.CreatorFragment;
import com.example.gameexplorer.fragment.developerFragments.DevelopersFragment;
import com.example.gameexplorer.fragment.gamesFragment.FavoriteFragment;
import com.example.gameexplorer.fragment.gamesFragment.GamesFragment;
import com.example.gameexplorer.fragment.mainFragment.HomeFragment;
import com.example.gameexplorer.fragment.platformFragment.PlatformFragment;
import com.example.gameexplorer.fragment.publisherFragments.PublishersFragment;
import com.example.gameexplorer.fragment.settingFragment.SettingsFragment;
import com.example.gameexplorer.networkHelper.NetworkUtils;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        FirebaseStorageHelper.UserImageFinisher, RealTimeDatabaseHelper.NameFinished {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mDrawerView;
    private ActionBarDrawerToggle mDrawerToggle;
    boolean isInFront = false;
    private boolean isGameFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //replace action bar with toolbar
        if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //set up toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = setupDrawerToggle();

        //setup toggle to display hamburger menu with animation
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerView = findViewById(R.id.drawerView);

        setUpDrawerContent(mDrawerView);

        new RealTimeDatabaseHelper(this);
        RealTimeDatabaseHelper.loadName();
        if(!isInFront){
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment homeFragment = HomeFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_homeContainer, homeFragment)
                    .commit();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        if(searchManager!= null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(
                    this,SearchableActivity.class)));
        }
        return true;
    }

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onGotImage(Bitmap bitmap) {
        setUpHeaderImage(mDrawerView, bitmap);

    }

    @Override
    public void OnNamePost(String _name) {
        setUpHeaderName(mDrawerView ,_name);
        new FirebaseStorageHelper(this);
        FirebaseStorageHelper.downloadImage();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setUpDrawerContent(NavigationView nv){
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void setUpHeaderImage(NavigationView nv, Bitmap bitmap){
        View headerView = nv.getHeaderView(0);
        CircleImageView civ = headerView.findViewById(R.id.iv_userImage_header);
        if(bitmap != null) {
            civ.setImageBitmap(bitmap);
        }else {
            civ.setImageResource(R.drawable.person_placeholder);
        }
    }

    private void setUpHeaderName(NavigationView nv, String name){
        View headerView = nv.getHeaderView(0);
        TextView tv_name = headerView.findViewById(R.id.tv_userName_header);
        if(name != null) {
            tv_name.setText(name);
        }else {
            tv_name.setText(R.string.user);
        }
    }

    private void selectDrawerItem(MenuItem menuItem){
        Class fragmentClass = null;
        String url = null;
        switch(menuItem.getItemId()) {
            case R.id.Home_fragment:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.allGames_fragment:
                fragmentClass = GamesFragment.class;
                url = "https://api.rawg.io/api/games?";
                isGameFragment = true;
                break;
            case R.id.topRated_fragment:
                fragmentClass = GamesFragment.class;
                url = NetworkUtils.getTopRatedYear();
                isGameFragment = true;
                break;
            case R.id.mostPopular_fragment:
                fragmentClass = GamesFragment.class;
                url =  NetworkUtils.getPopularGames();
                isGameFragment = true;
                break;
            case R.id.upcomingReleases_fragment:
                fragmentClass = GamesFragment.class;
                url =  NetworkUtils.getUpcomingUrl();
                isGameFragment = true;
                break;
            case R.id.recentlyReleased_fragment:
                fragmentClass = GamesFragment.class;
                url =  NetworkUtils.getRecentlyUrl();
                isGameFragment = true;
                break;
            case R.id.Platforms_fragment:
                fragmentClass = PlatformFragment.class;
                url = null;
                isGameFragment = false;
                break;
            case R.id.publishers_fragment:
                fragmentClass = PublishersFragment.class;
                url = null;
                break;
            case R.id.developers_fragment:
                fragmentClass = DevelopersFragment.class;
                url = null;
                break;
            case R.id.creators_fragment:
                fragmentClass = CreatorFragment.class;
                url = null;
                break;
            case R.id.favorite_fragment:
                fragmentClass = FavoriteFragment.class;
                url = null;
                break;
            case R.id.settings_fragment:
                fragmentClass = SettingsFragment.class;
                url = null;
                break;
            case R.id.signOut_fragment:
                SignOut();
                break;
        }
        if(fragmentClass != null) {
            replaceFragments(fragmentClass,url);
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            mDrawerLayout.close();
        }
    }

    public void replaceFragments(Class fragmentClass,String url) {
        Fragment fragment = null;
        try {
            if(isGameFragment || url != null){
                fragment =  GamesFragment.newInstance(url);
                isGameFragment = false;
            }
            else{
                fragment = (Fragment) fragmentClass.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
         if(fragment != null) {
             fragmentManager.beginTransaction().replace(R.id.fl_homeContainer,
                     fragment).addToBackStack(null)
                     .commit();
         }
    }

    private void SignOut(){
        AlertDialog.Builder signOutDialog = new AlertDialog.Builder(this);

        signOutDialog.setTitle("Sign Out");
        signOutDialog.setMessage("are you sure you want to sign out?");
        signOutDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int arg1)
            {
                dialog.cancel();
            }
        });
        signOutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserAuthenticationHelper.signOutUser();
                Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });


        signOutDialog.show();
    }


}
