package com.example.lab4;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    PopupMenu popup;
    private Button popupMenuButton;
    TextView textView;
    TextView task2View;
    private ActionMode mActionMode;
    ConstraintLayout constraintLayout;
    private static final String LOG_TAG = "OptionMenuExample";
    private SearchView searchView;

    private String lastQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popupMenuButton = findViewById(R.id.popUpBtnShow);
        popup = new PopupMenu(this, popupMenuButton);
        popup.getMenu().add(Menu.NONE, 0, 0, "Item 1");
        popup.getMenu().add(Menu.NONE, 1, 1, "Item 2");
        popup.getMenu().add(Menu.NONE, 2, 2, "Item 3");
        popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (id) {
                    case 0:
                        popup.getMenu().clear();

                        popup.getMenu().add(Menu.NONE, 0, 0, "Item 1");
                        popup.getMenu().add(Menu.NONE, 1, 1, "Item 2");
                        popup.getMenu().add(Menu.NONE, 2, 2, "Item 3");
                        return true;
                    case 1:
                        popup.getMenu().clear();

                        popup.getMenu().add(Menu.NONE, 0, 0, "Item 2");
                        popup.getMenu().add(Menu.NONE, 1, 1, "Item 3");
                        popup.getMenu().add(Menu.NONE, 2, 2, "Item 1");
                        return true;
                    case 2:
                        popup.getMenu().clear();

                        popup.getMenu().add(Menu.NONE, 0, 0, "Item 3");
                        popup.getMenu().add(Menu.NONE, 1, 1, "Item 1");
                        popup.getMenu().add(Menu.NONE, 2, 2, "Item 2");
                        return true;
                }
                return false;
            }
        });


        textView = (TextView) findViewById(R.id.textView);

        registerForContextMenu(textView);

        TextView textView = findViewById(R.id.text_view);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null) {
                    return false;
                }
                mActionMode = startSupportActionMode(mActionModeCallback);
                return true;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // you can set menu header with title icon etc
        menu.setHeaderTitle("Choose a color");
        // add menu items
        menu.add(0, v.getId(), 0, "Black");
        menu.add(0, v.getId(), 0, "Gray");
        menu.add(0, v.getId(), 0, "Blue");
        menu.add(0, v.getId(), 0, "Red");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle() == "Black") {
            textView.setTextColor(Color.BLACK);
        } else if (item.getTitle() == "Gray") {
            textView.setTextColor(Color.GRAY);
        } else if (item.getTitle() == "Blue") {
            textView.setTextColor(Color.BLUE);
        } else if (item.getTitle() == "Red") {
            textView.setTextColor(Color.RED);
        }
        return true;
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            mode.setTitle("Choose:");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
            switch (item.getItemId()) {
                case R.id.option_1:
                    constraintLayout.setBackgroundColor(Color.LTGRAY);
                    Toast.makeText(MainActivity.this, "Gray", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.option_2:
                    constraintLayout.setBackgroundColor(Color.MAGENTA);
                    Toast.makeText(MainActivity.this, "Magenta", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.option_3:
                    constraintLayout.setBackgroundColor(Color.CYAN);
                    Toast.makeText(MainActivity.this, "Cyan", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        // If you want Icon display in Overflow Menu.
        // https://stackoverflow.com/questions/19750635/icon-in-menu-not-showing-in-android
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        this.searchView = (SearchView) menu.findItem(R.id.menuItem_search).getActionView();

        this.searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // Need click "search" icon to expand SearchView.
        this.searchView.setIconifiedByDefault(true);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // Typing search text.
            public boolean onQueryTextChange(String newText) {
                // This is your adapter that will be filtered
                Log.i(LOG_TAG, "onQueryTextChange: " + newText);
                return true;
            }

            // Press Enter to search (Or something to search).
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                Log.i(LOG_TAG, "onQueryTextSubmit: " + query);
                return doSearch(query);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "SearchView.onSearchClickListener!");
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private boolean doSearch(String query) {
        if (query == null || query.isEmpty()) {
            return false; // Cancel search.
        }
        this.lastQuery = query;

        Toast.makeText(this, "Search: " + query, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.information:
                Toast.makeText(this, "Information", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_history:
                Toast.makeText(this, "History", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_save_as:
                Toast.makeText(this, "Save as", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_shortcut:
                Toast.makeText(this, "Create shortcut", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuItem_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuItem_search:
                Log.i(LOG_TAG, "onOptionsItemSelected (R.id.menuItem_search)");
                Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        SubMenu subMenu = menu.getItem(0).getSubMenu();
        List<String> contacts = getContacts(getApplicationContext());
        for (String contact: contacts) {
            subMenu.add(contact);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public List<String> getContacts(Context ctx) {
        List<String> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                list.add(name);
            }
            cursor.close();
        }
        return list;
    }

}