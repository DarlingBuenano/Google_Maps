package software.googlemaps;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Camera;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements OnMapReadyCallback {

    LatLng[] puntosLatLong;
    GoogleMap mapa;
    PolylineOptions poligonos;
    int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //--- Inicializando las variables globales
        this.poligonos = new PolylineOptions();
        this.mapa = googleMap;
        this.contador = 1;

        this.puntosLatLong = new LatLng[]{
                new LatLng(-1.1861515, -79.5119436), //Mocache
                new LatLng(-1.0286300, -79.4635200), //Quevedo
                new LatLng(-1.831239, -78.183406)};  //Ecuador

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marcarPunto(latLng);
            }
        });
        CameraUpdate camara = CameraUpdateFactory.newLatLngZoom(this.puntosLatLong[2], 6);
        googleMap.moveCamera(camara);
    }

    public void marcarPunto(LatLng punto){
        this.poligonos.add(punto);
        this.mapa.addMarker(new MarkerOptions().position(punto).title("Punto " + this.contador++));
    }

    public void btnMoverCamara(View view){

        String[] item = {"Mocache", "Quevedo", "Ecuador"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("Ciudades")
                .setItems(item, (dialog, which) -> {
                    cambiarLugar(which);
                }).show();
    }
    public void cambiarLugar(int lugar){
        int zoom = 14;
        if(lugar == 2)
            zoom = 6;
        CameraPosition posiCamara = new CameraPosition.Builder()
                .target(this.puntosLatLong[lugar])
                .zoom(zoom)
                .bearing(45)
                .build();
        CameraUpdate camara = CameraUpdateFactory.newCameraPosition(posiCamara);
        mapa.animateCamera(camara);
    }

    public void btndibujarPoligono(View view){
        List<LatLng> listaLatLng = this.poligonos.getPoints();
        this.poligonos.add( new LatLng(listaLatLng.get(0).latitude, listaLatLng.get(0).longitude));
        this.poligonos.width(6);
        this.poligonos.color(Color.rgb(150, 150, 150));
        this.mapa.addPolyline(this.poligonos);
    }

    public void limpiarTodo(View view){
        this.mapa.clear();
        this.poligonos = new PolylineOptions();
    }
}