package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece implements Parcelable{

	public static final Creator<Pawn> CREATOR = new Creator<Pawn>() {
		@Override
		public Pawn createFromParcel(Parcel in) {
			return new Pawn(in);
		}

		@Override
		public Pawn[] newArray(int size) {
			return new Pawn[size];
		}
	};

	public boolean enPassant;

	public Pawn(char player) {
		super(player);
		this.enPassant = false;
	}

	public List<Position> moveset(Board game, Position pos) {
		List<Position> lst = new ArrayList<Position>();
		Piece curr = game.board[pos.vert][pos.horiz];
		
		int oneInFront = (curr.player == 'w') ? pos.vert - 1 : pos.vert + 1;
		
		if (pos.horiz > 0) {
			if (game.board[oneInFront][pos.horiz - 1] != null && game.board[oneInFront][pos.horiz - 1].player != curr.player) { 
				lst.add(new Position(oneInFront, pos.horiz - 1));
			}
			
			// En passant
			else if (game.board[pos.vert][pos.horiz - 1] != null && game.board[pos.vert][pos.horiz - 1] instanceof Pawn) {
				Pawn candidate = (Pawn) game.board[pos.vert][pos.horiz - 1];
				if (candidate.enPassant && candidate.player != curr.player && game.board[oneInFront][pos.horiz - 1] == null) {
					lst.add(new Position(oneInFront, pos.horiz - 1));
				}
			}
		}
		
		if (game.board[oneInFront][pos.horiz] == null) lst.add(new Position(oneInFront, pos.horiz));		
		
		if (pos.horiz < 7 ) {
			if (game.board[oneInFront][pos.horiz + 1] != null && game.board[oneInFront][pos.horiz + 1].player != curr.player) {
				lst.add(new Position(oneInFront, pos.horiz + 1));
			}
			
			// En passant
			else if (game.board[pos.vert][pos.horiz + 1] != null && game.board[pos.vert][pos.horiz + 1] instanceof Pawn) {
				Pawn candidate = (Pawn) game.board[pos.vert][pos.horiz + 1];
				if (candidate.enPassant && candidate.player != curr.player && game.board[oneInFront][pos.horiz + 1] == null) {
					lst.add(new Position(oneInFront, pos.horiz + 1));
				}
			}
		}
		
		// Two-square move
		if (curr.firstMove) {
			int twoInFront = (curr.player == 'w') ? pos.vert - 2 : pos.vert + 2;
			
			if (game.board[oneInFront][pos.horiz] == null && game.board[twoInFront][pos.horiz] == null)  {
				lst.add(new Position(twoInFront, pos.horiz));
			}
		}
		
		return lst;
	}

	@Override
	public String toString() {
		return super.toString() + "_pawn";
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		super.writeToParcel(parcel, i);
		parcel.writeInt(enPassant ? 1 : 0);
	}

	protected Pawn(Parcel in) {
		super(in);
		enPassant = in.readInt() == 1;
	}
}
