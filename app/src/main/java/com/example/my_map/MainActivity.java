package com.example.my_map;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    ArrayAdapter adapter;
    FrameLayout listContainer;
    ListView listView;
    TextView emptyView;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDb;
    boolean isAfterCollapse = false;

    private List<City> cities = new ArrayList<>(467);
    private List<City> searchCities = new ArrayList<>(467);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listContainer = findViewById(R.id.list_container);
        listView = findViewById(R.id.listView);
        emptyView = findViewById(R.id.emptyView);

        mDBHelper = new DBHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        Cursor cursor = mDb.rawQuery("SELECT city_name, location_lat, location_lng FROM Cities", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getString(1).isEmpty()
                    || cursor.getString(2).isEmpty()) {
                cursor.moveToNext();
                continue;
            }
            City city = new City();
            cities.add(city);
            city.name = cursor.getString(0);
            city.lat = Double.parseDouble(cursor.getString(1));
            city.lng = Double.parseDouble(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();

        searchCities.addAll(cities);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, searchCities);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ;
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(searchCities.get(position).lat, searchCities.get(position).lng)));
                showCityInfoDialog(searchCities.get(position).name);
            }
        });
        listView.setEmptyView(emptyView);

    }

    private void showCityInfoDialog(String cityName) {
        CityDialogFragment dialogFragment = new CityDialogFragment();
        dialogFragment.setDatabase(mDb);
        dialogFragment.setCityName(cityName);
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.appSearchBar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Введите название города");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                searchCities.clear();
                if(isAfterCollapse)return true;
                if (newText.isEmpty()) {
                    searchCities.addAll(cities);
                    for (City city : cities) {
                        city.marker.setVisible(true);
                    }
                    adapter.notifyDataSetChanged();
                    return true;
                }
                for (City city : cities) {
                    if (city.name.replace("*", "").matches(".*(?i)" + newText + ".*")) {
                        searchCities.add(city);
                        city.marker.setVisible(true);
                        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(city.lat, city.lng)));
                    } else {
                        city.marker.setVisible(false);
                    }
                }
                adapter.notifyDataSetChanged();
                return true;
            }

        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                listContainer.setVisibility(View.VISIBLE);
                isAfterCollapse = false;
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                listContainer.setVisibility(View.INVISIBLE);
                isAfterCollapse = true;
                return true;
            }
        });

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        for (final City city : cities) {
            LatLng latLng = new LatLng(city.lat, city.lng);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(city.name);
            Marker marker = map.addMarker(markerOptions);
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    for (City city1 : cities) {
                        if (city1.marker.equals(marker)) {
                            showCityInfoDialog(city1.name);
                        }
                    }
                    return false;
                }
            });
            city.marker = marker;
        }
    }

    class City {
        String name = null;
        double lat = 0, lng = 0;
        Marker marker = null;

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

}
