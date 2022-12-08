package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece implements Parcelable{

	public static final Creator<Queen> CREATOR = new Creator<Queen>() {
		@Override
		public Queen createFromParcel(Parcel in) {
			return new Queen(in);
		}

		@Override
		public Queen[] newArray(int size) {
			return new Queen[size];
		}
	};
	public Queen(char player) {
		super(player);
	}

	public List<Position> moveset(Board game, Position pos) {
		List<Position> lst = new ArrayList<Position>();
		Piece curr = game.board[pos.vert][pos.horiz];
		
		Rook temp1 = new Rook(curr.player);
		Bishop temp2 = new Bishop(curr.player);
		
		lst = temp1.moveset(game, pos);
		lst.addAll(temp2.moveset(game, pos));
		
		return lst;
	}

	@Override
	public String toString() {
		return super.toString() + "_queen";
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		super.writeToParcel(parcel, i);
	}

	protected Queen(Parcel in) {
		super(in);
	}
}
