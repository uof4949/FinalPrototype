package kr.ac.gachon.finalprototype;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by viz on 2017. 6. 11..
 */

public class LocationItem implements Parcelable {
    private String locName;
    private String locAddress;
    private ParcelableTMapPOIItem POIItem;


    @Override
    public int describeContents() {
        return 0;
    }

    public String getLocName() {
        return locName;
    }

    public String getLocAddress() {
        return locAddress;
    }

    public ParcelableTMapPOIItem getPOIItem() {
        return POIItem;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.locName);
        dest.writeString(this.locAddress);
        //dest.writeParcelable(this.POIItem, flags);
    }

    public LocationItem(String locName, String locAddress, ParcelableTMapPOIItem POIItem) {
        this.locName = locName;
        this.locAddress = locAddress;
        this.POIItem = POIItem;
    }

    protected LocationItem(Parcel in) {
        this.locName = in.readString();
        this.locAddress = in.readString();
        this.POIItem = in.readParcelable(ParcelableTMapPOIItem.class.getClassLoader());
    }

    public static final Creator<LocationItem> CREATOR = new Creator<LocationItem>() {
        @Override
        public LocationItem createFromParcel(Parcel source) {
            return new LocationItem(source);
        }

        @Override
        public LocationItem[] newArray(int size) {
            return new LocationItem[size];
        }
    };
}
