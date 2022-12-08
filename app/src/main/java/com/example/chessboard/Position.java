package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;

public class Position implements Parcelable {

	public static final Creator<Position> CREATOR = new Creator<Position>() {
		@Override
		public Position createFromParcel(Parcel in) {
			return new Position(in);
		}

		@Override
		public Position[] newArray(int size) {
			return new Position[size];
		}
	};
	public int vert;
	public int horiz;

	public Position(int vert, int horiz) {
		this.vert = vert;
		this.horiz = horiz;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Position)) return false;
		Position other = (Position) o;
		return vert == other.vert && horiz == other.horiz;
	}

	@Override
	public String toString() {
		return "" + vert + "," + horiz + "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(vert);
		parcel.writeInt(horiz);
	}

	protected Position(Parcel in) {
		vert = in.readInt();
		horiz = in.readInt();
	}
}
