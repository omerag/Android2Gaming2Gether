package hit.android2.Database;

import android.os.Parcel;
import android.os.Parcelable;

public class ChildData implements Parcelable{

    private String massage;
    private String time;
    private String id;

    public ChildData(Parcel parcel){

        massage = parcel.readString();
    }

    public ChildData(String name) {
        this.massage = name;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public static final Parcelable.Creator<ChildData> CREATOR = new Parcelable.Creator<ChildData>() {
        @Override
        public ChildData createFromParcel(Parcel in) {
            return new ChildData(in);
        }

        @Override
        public ChildData[] newArray(int size) {
            return new ChildData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(massage);
    }

    public static Creator<ChildData> getCREATOR() {
        return CREATOR;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
