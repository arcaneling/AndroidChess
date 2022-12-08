package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece implements Parcelable{

	public static final Creator<Bishop> CREATOR = new Creator<Bishop>() {
		@Override
		public Bishop createFromParcel(Parcel in) {
			return new Bishop(in);
		}

		@Override
		public Bishop[] newArray(int size) {
			return new Bishop[size];
		}
	};
	public Bishop(char player) {
		super(player);
	}

	public List<Position> moveset(Board game, Position pos) {
		List<Position> lst = new ArrayList<Position>();
		Piece curr = game.board[pos.vert][pos.horiz];
		
		// Up-left
		int vert = pos.vert - 1;
		int horiz = pos.horiz + 1;
		while (vert > -1 && horiz < 8) {
			if (game.board[vert][horiz] != null) {
				if (game.board[vert][horiz].player != curr.player) lst.add(new Position(vert, horiz));
				break;
			}
					
			lst.add(new Position(vert, horiz));
			vert--;
			horiz++;
		}
		
		// Up-right
		vert = pos.vert + 1;
		horiz = pos.horiz + 1;
		while (vert < 8 && horiz < 8) {
			if (game.board[vert][horiz] != null) {
				if (game.board[vert][horiz].player != curr.player) lst.add(new Position(vert, horiz));
				break;
			}
					
			lst.add(new Position(vert, horiz));
			vert++;
			horiz++;
		}
		
		// Down-right
		vert = pos.vert + 1;
		horiz = pos.horiz - 1;
		while (vert < 8 && horiz > -1) {
			if (game.board[vert][horiz] != null) {
				if (game.board[vert][horiz].player != curr.player) lst.add(new Position(vert, horiz));
				break;
			}
					
			lst.add(new Position(vert, horiz));
			vert++;
			horiz--;
		}
		
		// Down-left
		vert = pos.vert - 1;
		horiz = pos.horiz - 1;
		while (vert > -1 && horiz > -1) {
			if (game.board[vert][horiz] != null) {
				if (game.board[vert][horiz].player != curr.player) lst.add(new Position(vert, horiz));
				break;
			}
					
			lst.add(new Position(vert, horiz));
			vert--;
			horiz--;
		}
		
		return lst;
	}

	@Override
	public String toString() {
		return super.toString() + "_bishop";
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		super.writeToParcel(parcel, i);
	}

	protected Bishop(Parcel in) { super(in); }
}
