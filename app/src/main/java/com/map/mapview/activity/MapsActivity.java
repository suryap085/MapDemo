package com.map.mapview.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.map.mapview.R;
import com.map.mapview.Utilities.CustomInfoWindowGoogleMap;
import com.map.mapview.adapter.CompanyAdapter;
import com.map.mapview.cons.Constant;
import com.map.mapview.modal.Company;
import com.map.mapview.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    RelativeLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;
    private GoogleMap mMap;
    private List<Company> mCompany;
    private ProgressDialog pd;
    private List<Company> companies;
    private ImageView imgview;
    private TextView companyDes, txtratebar, companyName;
    private RatingBar ratebar;
    private RecyclerView recyclerView;
    private CompanyAdapter compAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        layoutBottomSheet = (RelativeLayout) findViewById(R.id.bottom_sheet);
        imgview = (ImageView) findViewById(R.id.imgview);
        companyName = (TextView) findViewById(R.id.companyName);
        companyDes = (TextView) findViewById(R.id.companyDes);
        txtratebar = (TextView) findViewById(R.id.txtratebar);
        ratebar = (RatingBar) findViewById(R.id.ratebar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        companies = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final CompanyAsyncTask queryTask = new CompanyAsyncTask();
        queryTask.execute(Constant.Url);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Company comp = (Company) marker.getTag();
                layoutBottomSheet.setVisibility(View.VISIBLE);
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    openBottomSheet(comp, marker);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    openBottomSheet(comp, marker);
                }
                return false;
            }
        });

    }

    private void openBottomSheet(Company comp, Marker marker) {
        companyName.setText(comp.getCompany_name());
        companyDes.setText(comp.getCompany_description());
        ratebar.setRating((float) Integer.parseInt(marker.getTitle()));
        txtratebar.setText(marker.getTitle());
        Picasso.get().load(comp.getCompany_image_url()).into(imgview);
        compAdapter.notifyDataSetChanged();
        recyclerView.getLayoutManager().scrollToPosition(comp.getPosition());
    }

    private class CompanyAsyncTask extends AsyncTask<String, Void, List<Company>> {
        @Override
        protected List<Company> doInBackground(String... url) {
            List<Company> result = NetworkUtils.fetchData(url[0]);
            return result;
        }


        @Override
        protected void onPostExecute(List<Company> result) {
            companies = result;
            //Iterate over each Ccmpany in list
            for (int i = 0; i < companies.size(); i++) {
                Company currentCompany = companies.get(i);

                //Fetch information of Ccmpany
                Double latitude = Double.parseDouble(currentCompany.getLatitude());
                Double longitude = Double.parseDouble(currentCompany.getLongitude());
                String name = currentCompany.getCompany_name();
                String rating = currentCompany.getAvg_rating();

                //Place Marker on Ccmpany
                LatLng coordinate = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(coordinate).title(rating).snippet(name)
                        // below line is use to add custom marker on our map.
                        .icon(NetworkUtils.BitmapFromVector(getApplicationContext(), R.drawable.ic_map_black_24dp))).setTag(currentCompany);

                CustomInfoWindowGoogleMap markerInfoWindowAdapter = new CustomInfoWindowGoogleMap(getApplicationContext());
                mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 5));
            }
            compAdapter = new CompanyAdapter(MapsActivity.this, companies);
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(compAdapter);

        }
    }


}
