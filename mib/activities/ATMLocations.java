package mv.com.bml.mib.activities;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import mv.com.bml.mib.R;
import mv.com.bml.mib.adapters.LocationDataAdapter;
import mv.com.bml.mibapi.databases.AuxDataDB;
import mv.com.bml.mibapi.models.misc.LocationData;
import org.objectweb.asm.Opcodes;

public class ATMLocations extends BasePinCodeActivity implements OnClickListener, OnItemClickListener, ConnectionCallbacks, OnConnectionFailedListener, InfoWindowAdapter, OnMapReadyCallback {
    private boolean isShowing = true;
    @BindView(2131361962)
    ListView locationList;
    private ArrayList<LocationData> mData;
    private GoogleApiClient mGoogleApiClient;
    /* access modifiers changed from: private */
    public Location mLastLocation;
    private GoogleMap map;
    SupportMapFragment mapFragment;
    private HashMap<LocationData, Marker> markers;
    @BindView(2131362077)
    View toggleHeader;
    @BindView(2131362078)
    ImageView toggleImage;

    private void addMarkers() {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            googleMap.clear();
            this.markers.clear();
            Iterator it = this.mData.iterator();
            while (it.hasNext()) {
                LocationData locationData = (LocationData) it.next();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(locationData.name);
                markerOptions.snippet(locationData.getSnippet());
                markerOptions.position(new LatLng(locationData.getLat(), locationData.getLng()));
                markerOptions.draggable(false);
                this.markers.put(locationData, this.map.addMarker(markerOptions));
            }
        }
    }

    private void centerToMale() {
        this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(4.1751681d, 73.5107806d), 14.0f));
    }

    /* access modifiers changed from: private */
    public void checkLocation() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            this.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(this.mGoogleApiClient);
            Location location = this.mLastLocation;
            if (location != null) {
                this.map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), this.mLastLocation.getLongitude()), 14.0f));
                sortList();
            } else {
                centerToMale();
                Log.i("MAPS", "FAILED ON CONNECTED");
                displayDialog();
            }
        }
    }

    private void displayDialog() {
        Builder builder = new Builder(this);
        builder.setTitle(getString(R.string.gps_disabled_title));
        builder.setMessage(getResources().getString(R.string.gps_network_not_enabled));
        builder.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ATMLocations.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void requestLocationPerms() {
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, Opcodes.LSHR);
    }

    private void sortList() {
        if (this.mLastLocation != null) {
            Collections.sort(this.mData, new Comparator<LocationData>() {
                public int compare(LocationData locationData, LocationData locationData2) {
                    String str = "";
                    Location location = new Location(str);
                    location.setLatitude(locationData.getLat());
                    location.setLongitude(locationData.getLng());
                    float distanceTo = ATMLocations.this.mLastLocation.distanceTo(location);
                    new Location(str);
                    location.setLatitude(locationData2.getLat());
                    location.setLongitude(locationData2.getLng());
                    return Float.compare(distanceTo, ATMLocations.this.mLastLocation.distanceTo(location));
                }
            });
            ((ArrayAdapter) this.locationList.getAdapter()).notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void buildGoogleApiClient() {
        this.mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }

    public View getInfoContents(Marker marker) {
        View inflate = getLayoutInflater().inflate(R.layout.fragment_info_window, null);
        TextView textView = (TextView) inflate.findViewById(R.id.dialog_body);
        ((TextView) inflate.findViewById(R.id.dialog_title)).setText(marker.getTitle());
        textView.setText(marker.getSnippet());
        return inflate;
    }

    public View getInfoWindow(Marker marker) {
        return null;
    }

    public void onBackPressed() {
        if (!this.isShowing) {
            super.onBackPressed();
        } else {
            onClick(this.toggleHeader);
        }
    }

    public void onClick(View view) {
        ImageView imageView;
        int i;
        if (this.isShowing) {
            this.locationList.setVisibility(8);
            this.isShowing = false;
            imageView = this.toggleImage;
            i = R.drawable.ic_expand_less;
        } else {
            this.locationList.setVisibility(0);
            this.isShowing = true;
            imageView = this.toggleImage;
            i = R.drawable.ic_expand_more;
        }
        imageView.setImageResource(i);
    }

    public void onConnected(Bundle bundle) {
        checkLocation();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("MAPS", "FAILED OUTRIGHT");
        displayDialog();
    }

    public void onConnectionSuspended(int i) {
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_atmlocations);
        ButterKnife.bind((Activity) this);
        this.toggleHeader.setOnClickListener(this);
        this.markers = new HashMap<>();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (this.map != null) {
            LocationData locationData = (LocationData) this.locationList.getAdapter().getItem(i);
            Marker marker = (Marker) this.markers.get(locationData);
            GoogleMap googleMap = this.map;
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationData.getLat(), locationData.getLng()), 17.0f));
            }
            if (marker != null) {
                marker.showInfoWindow();
            }
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                public boolean onMyLocationButtonClick() {
                    ATMLocations.this.checkLocation();
                    return false;
                }
            });
            checkLocation();
        } else {
            requestLocationPerms();
        }
        googleMap.setInfoWindowAdapter(this);
        addMarkers();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 123) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, "Location Permissions Denied", 0).show();
                centerToMale();
                return;
            }
            GoogleMap googleMap = this.map;
            if (googleMap != null) {
                onMapReady(googleMap);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mData = new AuxDataDB(this).getLocationData();
        this.locationList.setAdapter(new LocationDataAdapter(this, this.mData));
        this.locationList.setOnItemClickListener(this);
        this.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.mapFragment.getMapAsync(this);
        buildGoogleApiClient();
        this.mGoogleApiClient.connect();
    }
}
