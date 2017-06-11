package kr.ac.gachon.finalprototype;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapPOIItem;

/**
 * Created by kalios on 2017-06-11.
 */

public class LocationItem extends TMapPOIItem implements TMapGpsManager.onLocationChangedCallback, Parcelable {
    private String locName;
    private String locAddress;
    private TMapPOIItem POIItem;

    public String getLocName() {
        return locName;
    }

    public String getLocAddress() {
        return locAddress;
    }

    public TMapPOIItem getPOIItem() {
        return POIItem;
    }


    public LocationItem(String locName, String locAddress, TMapPOIItem POIItem) {
        this.locName = locName;
        this.locAddress = locAddress;
        this.POIItem = POIItem;
    }

    public LocationItem(Parcel parcel) {
        this.locName = parcel.readString();
        this.locAddress = parcel.readString();
    }
    public static final Parcelable.Creator<LocationItem> CREATOR = new Parcelable.Creator<LocationItem>() {
        @Override
        public LocationItem createFromParcel(Parcel parcel) {
            return new LocationItem(parcel);
        }
        @Override
        public LocationItem[] newArray(int size) {
            return new LocationItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locName);
        dest.writeString(this.locAddress);
        dest.writeValue(this.POIItem);
    }

    private void readFromPardel(Parcel in) {
        locName = in.readString();
        locAddress = in.readString();
    }


    @Override
    public void onLocationChange(Location location) {
    }
}
