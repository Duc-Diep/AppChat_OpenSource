package group1.appchat_opensource.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String id,username,image_url,status,gender;

    public User(String id, String username, String image_url, String status, String gender) {
        this.id = id;
        this.username = username;
        this.image_url = image_url;
        this.status = status;
        this.gender = gender;
    }

    protected User(Parcel in) {
        id = in.readString();
        username = in.readString();
        image_url = in.readString();
        status = in.readString();
        gender = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(image_url);
        dest.writeString(status);
        dest.writeString(gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
