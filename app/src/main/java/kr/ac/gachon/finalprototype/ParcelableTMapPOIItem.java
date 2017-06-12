package kr.ac.gachon.finalprototype;

import com.skp.Tmap.TMapPOIItem;

/**
 * Created by viz on 2017. 6. 12..
 */
/*
public class ParcelableTMapPOIItem extends TMapPOIItem implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.telNo);
        dest.writeString(this.frontLat);
        dest.writeString(this.frontLon);
        dest.writeString(this.noorLat);
        dest.writeString(this.noorLon);
        dest.writeString(this.upperAddrName);
        dest.writeString(this.middleAddrName);
        dest.writeString(this.lowerAddrName);
        dest.writeString(this.detailAddrName);
        dest.writeString(this.firstNo);
        dest.writeString(this.secondNo);
        dest.writeString(this.upperBizName);
        dest.writeString(this.middleBizName);
        dest.writeString(this.lowerBizName);
        dest.writeString(this.detailBizName);
        dest.writeString(this.rpFlag);
        dest.writeString(this.parkFlag);
        dest.writeString(this.detailInfoFlag);
        dest.writeString(this.desc);
        dest.writeString(this.distance);
        dest.writeString(this.roadName);
        dest.writeString(this.buildingNo1);
        dest.writeString(this.buildingNo2);
        dest.writeString(this.merchanFlag);
        dest.writeString(this.radius);
        dest.writeParcelable(this.Icon, flags);
        dest.writeString(this.bizCatName);
        dest.writeString(this.address);
        dest.writeString(this.zipCode);
        dest.writeString(this.homepageURL);
        dest.writeString(this.routeInfo);
        dest.writeString(this.additionalInfo);
    }


    protected ParcelableTMapPOIItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.telNo = in.readString();
        this.frontLat = in.readString();
        this.frontLon = in.readString();
        this.noorLat = in.readString();
        this.noorLon = in.readString();
        this.upperAddrName = in.readString();
        this.middleAddrName = in.readString();
        this.lowerAddrName = in.readString();
        this.detailAddrName = in.readString();
        this.firstNo = in.readString();
        this.secondNo = in.readString();
        this.upperBizName = in.readString();
        this.middleBizName = in.readString();
        this.lowerBizName = in.readString();
        this.detailBizName = in.readString();
        this.rpFlag = in.readString();
        this.parkFlag = in.readString();
        this.detailInfoFlag = in.readString();
        this.desc = in.readString();
        this.distance = in.readString();
        this.roadName = in.readString();
        this.buildingNo1 = in.readString();
        this.buildingNo2 = in.readString();
        this.merchanFlag = in.readString();
        this.radius = in.readString();
        this.Icon = in.readParcelable(Bitmap.class.getClassLoader());
        this.bizCatName = in.readString();
        this.address = in.readString();
        this.zipCode = in.readString();
        this.homepageURL = in.readString();
        this.routeInfo = in.readString();
        this.additionalInfo = in.readString();
    }

    public static final Creator<ParcelableTMapPOIItem> CREATOR = new Creator<ParcelableTMapPOIItem>() {
        @Override
        public ParcelableTMapPOIItem createFromParcel(Parcel source) {
            return new ParcelableTMapPOIItem(source);
        }

        @Override
        public ParcelableTMapPOIItem[] newArray(int size) {
            return new ParcelableTMapPOIItem[size];
        }
    };
}*/

public class ParcelableTMapPOIItem extends TMapPOIItem {


}
