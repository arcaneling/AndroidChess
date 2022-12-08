package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{
	public Rook(char player) {
		super(player);
	}

	public List<Position> moveset(Board game, Position pos) {
		List<Position> lst = new ArrayList<Position>();
		Piece curr = game.board[pos.vert][pos.horiz];
		
		// Up
		int vert = pos.vert - 1;
		while (vert > -1) {
			if (game.board[vert][pos.horiz] != null) {
				if (game.board[vert][pos.horiz].player != curr.player) lst.add(new Position(vert, pos.horiz));
				break;
			}
			
			lst.add(new Position(vert, pos.horiz));
			vert--;
		}
		
		// Right
		int horiz = pos.horiz + 1;
		while (horiz < 8) {
			if (game.board[pos.vert][horiz] != null) {
				if (game.board[pos.vert][horiz].player != curr.player) lst.add(new Position(pos.vert, horiz));
				break;
			}
					
			lst.add(new Position(pos.vert, horiz));
			horiz++;
		}
		
		// Down
		vert = pos.vert + 1;
		while (vert < 8) {
			if (game.board[vert][pos.horiz] != null) {
				if (game.board[vert][pos.horiz].player != curr.player) lst.add(new Position(vert, pos.horiz));
				break;
			}
			
			lst.add(new Position(vert, pos.horiz));
			vert++;
		}

		// Left
		horiz = pos.horiz - 1;
		while (horiz > -1) {
			if (game.board[pos.vert][horiz] != null) {
				if (game.board[pos.vert][horiz].player != curr.player) lst.add(new Position(pos.vert, horiz));
				break;
			}
					
			lst.add(new Position(pos.vert, horiz));
			horiz--;
		}
		
		
		return lst;
	}

	@Override
	public String toString() {
		return super.toString()  + "_rook";
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		super.writeToParcel(parcel, i);
	}

	protected Rook(Parcel in) { super(in); }
}
