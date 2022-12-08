package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Piece implements Parcelable{

	public static final Creator<Piece> CREATOR = new Creator<Piece>() {
		@Override
		public Piece createFromParcel(Parcel in) {
			return new Piece(in);
		}

		@Override
		public Piece[] newArray(int size) {
			return new Piece[size];
		}
	};

	public char player;
	public boolean firstMove;

	public Piece(char player) {
		this.player = player;
		this.firstMove = true;
	}

	public List<Position> moveset(Board game, Position pos) {
		return new ArrayList<Position>();
	}

	@Override
	public String toString() {
		return player + "";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(player);
		parcel.writeInt(firstMove ? 1 : 0);
	}

	protected Piece(Parcel in) {
		player = (char) in.readInt();
		firstMove = in.readInt() == 1;
	}
}
