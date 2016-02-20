package cn.datapad.lifenote.one.lifenote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private StringBuilder mydbcontent;
    private SQLiteDatabase mydb;
    private DbOpenHelper myDBHelper;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//in app_bar_main.xml
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mContext = MainActivity.this;
        myDBHelper = new DbOpenHelper(mContext,"note.db",null,1);

        TextView note_main_content = (TextView) findViewById(R.id.content_main_textView_1);

        mydb = myDBHelper.getWritableDatabase();
        mydbcontent = new StringBuilder();

        Cursor cursor = mydb.query("note", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int noteid = cursor.getInt(cursor.getColumnIndex("noteid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                mydbcontent.append("id：" + noteid + "：" + title + "\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        note_main_content.setText(mydbcontent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
       // menu.add(1, RED, 0, "红色");
        //menu.add(1, GREEN, 1, "绿色");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //右上角行为菜单控件
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //右上角actionbar
        switch (id) {
            case R.id.action_settings:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, NoteAddActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_addnote:
                // app icon in action bar clicked; go home
                Intent intent_NoteAddActivity = new Intent(this, NoteAddActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_NoteAddActivity);
                return true;
            case R.id.action_findnote:
                // app icon in action bar clicked; go home
                TextView note_main_content = (TextView) findViewById(R.id.content_main_textView_1);

                mydb = myDBHelper.getWritableDatabase();
                mydbcontent = new StringBuilder();

                Cursor cursor = mydb.query("note", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        int noteid = cursor.getInt(cursor.getColumnIndex("noteid"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        mydbcontent.append("id：" + noteid + "：" + title + "\n");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                note_main_content.setText(mydbcontent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
