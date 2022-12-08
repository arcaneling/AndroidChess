package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;


public class King extends Piece implements Parcelable{

	public static final Creator<King> CREATOR = new Creator<King>() {
		@Override
		public King createFromParcel(Parcel in) {
			return new King(in);
		}

		@Override
		public King[] newArray(int size) {
			return new King[size];
		}
	};
	public King(char player) {
		super(player);
	}

	public List<Position> moveset(Board game, Position pos) {
		List<Position> lst = new ArrayList<Position>();
		Piece curr = game.board[pos.vert][pos.horiz];
		
		for (int i = -1; i < 2; i++) {
			int vert = pos.vert + i;
			
			if (vert < 0 || vert > 7) continue;
			for (int j = -1; j < 2; j++) {
				int horiz = pos.horiz + j;
				
				if (horiz < 0 || horiz > 7) continue;
				if (vert == pos.vert && horiz == pos.horiz) continue;
				
				if (game.board[vert][horiz] != null) {
					if (game.board[vert][horiz].player != curr.player) lst.add(new Position(vert, horiz));
					continue;
				}
						
				lst.add(new Position(vert, horiz));
			}
		}
		
		// Castling
		if (curr.firstMove) {
			// Queenside castling
			if (game.board[pos.vert][0] != null &&
					game.board[pos.vert][0].firstMove &&
					game.board[pos.vert][1] == null &&
					game.board[pos.vert][2] == null &&
					game.board[pos.vert][3] == null) {
				lst.add(new Position(pos.vert, 2));
			}
			// Kingside castling
			if (game.board[pos.vert][7] != null &&
					game.board[pos.vert][7].firstMove &&
					game.board[pos.vert][6] == null &&
					game.board[pos.vert][5] == null) {
				lst.add(new Position(pos.vert, 6));
			}
		}
		
		return lst;
	}

	@Override
	public String toString() {
		return super.toString() + "_king";
	}


	@Override
	public void writeToParcel(Parcel parcel, int i) {
		super.writeToParcel(parcel, i);
	}

	protected King(Parcel in) {
		super(in);
	}
}
