/****************************************************************************************************************
 * org/dynamicsoft/caloriescope/dietManager/ExercisePlansActivity.java: ExercisePlans activity for CalorieScope
 ****************************************************************************************************************
 * Copyright (C) 2019 Sourav Kainth
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ****************************************************************************************************************/

package org.dynamicsoft.caloriescope.dietManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.dynamicsoft.caloriescope.R;
import org.dynamicsoft.caloriescope.activities.DietManagerActivity;

import java.util.ArrayList;

public class ExercisePlansActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final ArrayList<String> Wlistdata = new ArrayList<>();
    private final ArrayList<String> id = new ArrayList<>();
    private final int MENU_DELETE = 0;
    private DietManagerDBHelper mdatabasehelper;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listworkout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_manager_exercise_plans);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //To set Person's name in Nav Drawer
        SharedPreferences pref = getApplicationContext().getSharedPreferences("AppData", 0);
        TextView NavDrawerUserString = navigationView.getHeaderView(0).findViewById(R.id.NavDrawerUserString);
        NavDrawerUserString.setText(pref.getString("UserName", "Welcome"));

        listworkout = findViewById(R.id.listworkout);
        FloatingActionButton doneplan = findViewById(R.id.doneplan);
        mdatabasehelper = new DietManagerDBHelper(this);

        doneplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExercisePlansActivity.this, ExerciseAddPlansActivity.class);
                startActivity(i);
                finish();
            }
        });

        registerForContextMenu(listworkout);
        listpop();

    }

    private void listpop() {
        Cursor workdata = mdatabasehelper.getworkoutdata();
        while (workdata.moveToNext()) {

            Wlistdata.add("\nWorkout : " + workdata.getString(1) + "\n\n " + "Day : " + workdata.getString(2));
            id.add(workdata.getString(0));
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Wlistdata);
        listworkout.setAdapter(arrayAdapter);
    }

    //menu on list click
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listworkout) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = item.getItemId();

        if (index == MENU_DELETE) {
            int deletePost = info.position;
            if (deletePost > -1) {
                mdatabasehelper.deleteworkout(String.valueOf(id.get(deletePost)));
                Wlistdata.remove(deletePost);
                arrayAdapter.notifyDataSetChanged();
                arrayAdapter.notifyDataSetInvalidated();
            }
        } else {
            Toast.makeText(this, "Unable To Perform This Action", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_exit) {
            ExercisePlansActivity.this.moveTaskToBack(true);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_diet_manager_home) {
            Intent i = new Intent(ExercisePlansActivity.this, DietManagerActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_food_suggestions) {
            Intent i = new Intent(ExercisePlansActivity.this, FoodSuggestionsActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_exercise) {
        } else if (id == R.id.nav_fat_burning_drinks) {
            Intent i = new Intent(ExercisePlansActivity.this, DrinksActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
