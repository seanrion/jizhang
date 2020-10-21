package com.jizhang.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "incomeCat")
public class IncomeCat implements Parcelable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int mId;        //主键
    @DatabaseField(canBeNull = false, columnName = "name")
    private String mName;   //名称
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "userId")
    private User mUser;     //用户

    public IncomeCat() {
    }

    public IncomeCat(int id, String name,User user) {
        mId = id;
        mName = name;
        mUser = user;
    }

    public IncomeCat(String name,User user) {
        mName = name;
        mUser = user;
    }

    public IncomeCat(String name) {
        mName = name;

    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeParcelable(this.mUser, 0);
    }

    protected IncomeCat(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<IncomeCat> CREATOR = new Creator<IncomeCat>() {
        public IncomeCat createFromParcel(Parcel source) {
            return new IncomeCat(source);
        }

        public IncomeCat[] newArray(int size) {
            return new IncomeCat[size];
        }
    };
}
