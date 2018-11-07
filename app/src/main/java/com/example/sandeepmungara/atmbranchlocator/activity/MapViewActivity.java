package com.example.sandeepmungara.atmbranchlocator.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sandeepmungara.atmbranchlocator.R;
import com.example.sandeepmungara.atmbranchlocator.constants.ConstantDataClass;
import com.example.sandeepmungara.atmbranchlocator.viewmodel.ResultsModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<List<ResultsModel>>, Response.ErrorListener, GoogleMap.OnMarkerClickListener {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = MapViewActivity.class.getSimpleName();
    private static final float DEFAULT_ZOOM = 15;
    private static final int M_MAX_ENTRIES = 5;
    private static final int OPERATION_SEARCH_LOADER = 22;
    private boolean mLocationPermissionGranted = false;

    private FusedLocationProviderClient mFusedLocationClient;
    private SupportMapFragment mapFragment;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private GoogleMap mMap;
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private LatLng markerLatLng;
    private RequestQueue requestQueue;
    private List<ResultsModel> resultsModel;
    private boolean isNetWorkCallMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLocationPermissionGranted)
            getLocationPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        updateLocationUI();
        getDeviceLocation();
        showCurrentPlace();
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<List<ResultsModel>> onCreateLoader(final int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<List<ResultsModel>>(this) {
            @Nullable
            @Override
            public List<ResultsModel> loadInBackground() {
                final Gson gson = new Gson();
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("results");
                                    if (jsonArray.length() > 0) {
                                        resultsModel = Arrays.asList(gson.fromJson(jsonArray.toString(), ResultsModel[].class));
                                    }
                                    publishMapList(resultsModel);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        MapViewActivity.this);
                requestQueue.add(jsonObjectRequest);
                return resultsModel;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.list_view) {
            Intent intent = new Intent(MapViewActivity.this, ListActivity.class);
            ConstantDataClass.setData(resultsModel);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<ResultsModel>> loader, List<ResultsModel> resultsModels) {
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<ResultsModel>> loader) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapViewActivity.this, MapViewDetailsActivity.class);
        ConstantDataClass.setDataForOneItem(marker);
        startActivity(intent);
        return false;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("TAG", error.getMessage());
    }


    private void publishMapList(final List<ResultsModel> resultsModel) {
        getLoaderManager().restartLoader(23, null, new android.app.LoaderManager.LoaderCallbacks<Object>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public android.content.Loader<Object> onCreateLoader(int i, Bundle bundle) {
                return new android.content.AsyncTaskLoader<Object>(getApplicationContext()) {
                    @Override
                    public Object loadInBackground() {
                        for (int j = 0; j < resultsModel.size(); j++) {
                            resultsModel.get(j).setBitmap(getBitmapFromURL(resultsModel.get(j).icon));
                        }
                        return null;
                    }

                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }
                };
            }

            @Override
            public void onLoadFinished(android.content.Loader<Object> loader, Object o) {
                for (int j = 0; j < resultsModel.size(); j++) {
                    Double latitude = Double.parseDouble(resultsModel.get(j).geometry.location.lat);
                    Double longitude = Double.parseDouble(resultsModel.get(j).geometry.location.lng);
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .title(resultsModel.get(j).name)
                            .position(latLng)
                            .snippet(resultsModel.get(j).formatted_address))
                            .setIcon(BitmapDescriptorFactory.fromBitmap(resultsModel.get(j).bitmap));
                }
                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        5));
            }

            @Override
            public void onLoaderReset(android.content.Loader<Object> loader) {

            }
        });
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        updateLocationUI();
    }

    private void updateLocationUI() {

        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new String[count];
                                mLikelyPlaceAddresses = new String[count];
                                mLikelyPlaceAttributions = new String[count];
                                mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();

                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                markerLatLng = mLikelyPlaceLatLngs[0];
                                String markerSnippet = mLikelyPlaceAddresses[0];
                                if (mLikelyPlaceAttributions[0] != null) {
                                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[0];
                                }
                                mMap.addMarker(new MarkerOptions()
                                        .title(mLikelyPlaceNames[0])
                                        .position(markerLatLng)
                                        .snippet(markerSnippet));

                                // Position the map's camera at the location of the marker.
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                                        DEFAULT_ZOOM));
                                getSupportLoaderManager().initLoader(OPERATION_SEARCH_LOADER, null, MapViewActivity.this);

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private String getUrl() {
        String googleApiKey = getString(R.string.google_maps_key);
        return "https://maps.googleapis.com/maps/api/place/textsearch/json?query=BBVA+Compass&location=" + markerLatLng.latitude + "," + markerLatLng.latitude + "&radius=10000&key=" + googleApiKey;
    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
