package com.jizhang.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "user")
public class User implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int mId;
    @DatabaseField(columnName = "name")  //账户名
    private String mName;
    @DatabaseField(columnName = "passwordfirst")  //口令密码
    private String passfirst;
    @DatabaseField(columnName = "passwordsecond")  //手势密码
    private String passsecond;
    public User() {
    }

    public User(String name,String passfirst,String passsecond) {
        mName = name;
        this.passfirst = passfirst;
        this.passsecond = passsecond;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getPassfirst() {
        return passfirst;
    }

    public void setPassfirst(String pass) {
        this.passfirst = pass;
    }

    public String getPasssecond() {
        return passsecond;
    }

    public void setPasssecond(String pass) {
        this.passsecond = pass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.passfirst);
        dest.writeString(this.passsecond);
    }

    protected User(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.passfirst = in.readString();
        this.passsecond = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}