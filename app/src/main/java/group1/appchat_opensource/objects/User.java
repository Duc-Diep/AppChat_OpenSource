package group1.appchat_opensource.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String gender;
    private String id;
    private String image_url;
    private String username;
    private String status;

    public User() {
    }

    public User(String gender, String id, String image_url, String username, String status) {
        this.gender = gender;
        this.id = id;
        this.image_url = image_url;
        this.username = username;
        this.status = status;
    }

    protected User(Parcel in) {
        gender = in.readString();
        id = in.readString();
        image_url = in.readString();
        username = in.readString();
        status = in.readString();
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "gender='" + gender + '\'' +
                ", id='" + id + '\'' +
                ", image_url='" + image_url + '\'' +
                ", username='" + username + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gender);
        dest.writeString(id);
        dest.writeString(image_url);
        dest.writeString(username);
        dest.writeString(status);
    }
}
