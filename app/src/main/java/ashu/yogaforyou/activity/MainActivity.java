package ashu.yogaforyou.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ashu.yogaforyou.R;
import ashu.yogaforyou.fragment.About;
import ashu.yogaforyou.fragment.BreathingFragment;
import ashu.yogaforyou.fragment.HomeFragment;
import ashu.yogaforyou.fragment.NormalFragment;
import ashu.yogaforyou.fragment.ShopFragment;
import ashu.yogaforyou.fragment.StressFragment;
import ashu.yogaforyou.global.AppController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private SimpleDraweeView simpleDraweeView;

    private Tracker mTracker;

    private String urlOfApp = "https://play.google.com/store/apps/" +
            "details?id=ashu.yogaforyou";
    private String latestVersion;
    private String currentVersion;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initializeLayoutVariables();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new HomeFragment())
                    .commit();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerLayout = navigationView.getHeaderView(0);

        simpleDraweeView = (SimpleDraweeView) headerLayout.findViewById(R.id.my_image_view);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.raw.giphy).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imageRequest.getSourceUri())
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);

        AppController appController = (AppController) getApplication();
        mTracker = appController.getDefaultTracker();

        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetCurrentVersion().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Open")
                .build());

    }

    private void initializeLayoutVariables(){
        // Layout First
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStackImmediate();
            else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new HomeFragment()).addToBackStack("Home")
                    .commit();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new StressFragment()).addToBackStack("Stress")
                    .commit();

        } else if (id == R.id.nav_slideshow) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new BreathingFragment()).addToBackStack("Breathing")
                    .commit();

        } else if (id == R.id.shop) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new ShopFragment()).addToBackStack("Shopping")
                    .commit();

        }
//        else if (id == R.id.nav_manage) {
//
//
//        }
        else if (id == R.id.nav_share) {

            shareWithFriends();

        } else if (id == R.id.nav_send) {
            rateApp();
        } else  if(id == R.id.more){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main, new About()).addToBackStack("About")
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void rateApp(){
        String url = "market://details?id=ashu.yogaforyou";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    private void shareWithFriends(){
        String str = "Share with your friends this Awesome app for staying fit and do yoga for peace of mind, soul and body \n"+
                "https://play.google.com/store/apps/details?id=ashu.yogaforyou";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        startActivityForResult(Intent.createChooser(
                sendIntent,"Share With Your Friends"),
                Activity.RESULT_OK);
    }

    public class GetCurrentVersion extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(urlOfApp).get();
                latestVersion = doc.getElementsByAttributeValue
                        ("itemprop", "softwareVersion").first().text();
            } catch (Exception e) {

            }
            return null;
        }

        //TODO: Remove isNetConnected before release

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!(currentVersion.equalsIgnoreCase(latestVersion) || latestVersion == null))
                showUpdateDialog();
            super.onPostExecute(aVoid);

        }
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=ashu.yogafor you")));
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }
}
