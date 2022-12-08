package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece implements Parcelable{

	public static final Creator<Knight> CREATOR = new Creator<Knight>() {
		@Override
		public Knight createFromParcel(Parcel in) {
			return new Knight(in);
		}

		@Override
		public Knight[] newArray(int size) {
			return new Knight[size];
		}
	};
	public Knight(char player) {
		super(player);
	}

	public List<Position> moveset(Board game, Position pos) {
		List<Position> lst = new ArrayList<Position>();
		Piece curr = game.board[pos.vert][pos.horiz];
		
		// Lists of all possible moves such that pos.vert + vertArr[i] and pos.horiz + horizArr[i] corresponds to the i'th possible move for the Knight
		int[] vertArr = new int[]{2, 1, -1, -2, -2, -1, 1, 2};
		int[] horizArr = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
		
		for (int i = 0; i < 8; i++) {
			int vert = pos.vert + vertArr[i];
			int horiz = pos.horiz + horizArr[i];
			
			if (vert > 7 || vert < 0 || horiz > 7 || horiz < 0) continue;
			if (game.board[vert][horiz] != null && game.board[vert][horiz].player == curr.player) continue;
			
			lst.add(new Position(vert, horiz));
		}
		
		return lst;
	}

	@Override
	public String toString() {
		return super.toString() + "_knight";
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		super.writeToParcel(parcel, i);
	}

	protected Knight(Parcel in) {
		super(in);
	}
}
