package com.example.chessboard;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Parcelable{

	public static final Creator<Board> CREATOR = new Creator<Board>() {
		@Override
		public Board createFromParcel(Parcel in) {
			return new Board(in);
		}

		@Override
		public Board[] newArray(int size) {
			return new Board[size];
		}
	};

	public Piece[][] board;
	public Piece[][] prev;
	public String turn;
	public List<String> moves;

	public Board() {
		this.board = generate();
		this.prev = generate();
		this.turn = "White";
		this.moves = new ArrayList<String>();
	}

	public Piece[][] generate() {
		Piece[][] board = new Piece[8][8];
		
		for (int i = 0; i < 8; i += 7) {
			char player = (i == 0) ? 'b' : 'w';
			
			board[i][0] = new Rook(player);
			board[i][1] = new Knight(player);
			board[i][2] = new Bishop(player);
			board[i][3] = new Queen(player);
			board[i][4] = new King(player);
			board[i][5] = new Bishop(player);
			board[i][6] = new Knight(player);
			board[i][7] = new Rook(player);
			
			for (int j = 0; j < 8; j++) {
				int k = (i == 0) ? i+1 : i-1;
				board[k][j] = new Pawn(player);
			}
		}
		
		return board;
	}

	public List<Position> getPieces(Character player) {
		List<Position> lst = new ArrayList<Position>();
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null && board[i][j].player == player) lst.add(new Position(i, j));
			}
		}
		
		return lst;
	}

	public Integer[] getState() {
		Integer[] state = new Integer[64];

		int counter = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				String temp = "R.drawable.";
				temp += (board[i][j] == null) ? "blank" : board[i][j].toString();

				state[counter] = Integer.parseInt(temp);
				counter++;
			}
		}
		return state;
	}

	public Piece[][] duplicate(Piece[][] arr) {
		Piece[][] dup = new Piece[8][8];

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if (arr[i][j] != null) dup[i][j] = arr[i][j];
			}
		}

		return dup;
	}

	public boolean equals() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null && prev[i][j] == null) continue;
				if (board[i][j] == null || prev[i][j] == null) return false;
				if (!board[i][j].toString().equals(prev[i][j].toString())) return false;
			}
		}
		return true;
	}

	public boolean inCheckmate(Character player) {
		if (!inCheck(player)) return false;

		Piece[][] temp = duplicate(board);
		List<Position> pieces = getPieces(player);

		for (Position pos: pieces) {
			List<Position> moves = board[pos.vert][pos.horiz].moveset(this, new Position(pos.vert, pos.horiz));
			for (Position mov: moves) {
				board[mov.vert][mov.horiz] = board[pos.vert][pos.horiz];
				board[pos.vert][pos.horiz] = null;
				if (!inCheck(player)) {
					board = duplicate(temp);
					return false;
				}

				board = duplicate(temp);
			}
		}

		board = duplicate(temp);
		return true;
	}

	public boolean inCheck(Character player) {
		Position pos = findKing(player);

		Character opponent = (board[pos.vert][pos.horiz].player == 'w') ? 'b' : 'w';
		List<Position> pieces = getPieces(opponent);

		for (Position curr: pieces) {
			List<Position> moves = board[curr.vert][curr.horiz].moveset(this, new Position(curr.vert, curr.horiz));
			if (moves.contains(pos)) return true;
		}

		return false;
	}

	public Position findKing(Character player) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != null &&
						board[i][j].player == player &&
						board[i][j] instanceof King) {
					return new Position(i, j);
				}
			}
		}

		return null;
	}

	@Override
	public String toString() {
		String str = "\n";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == null) {
					str += ((i + j) % 2 == 1) ? "   " : "## ";
				}
				else str += board[i][j] + " ";
			}
			str += (board.length - i + "\n");
		}
		str += " a  b  c  d  e  f  g  h\n"; 
		return str;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		int n = board.length;
		parcel.writeInt(n); // save number of arrays
		for (int i = 0; i < n; i++) {
			parcel.writeParcelableArray(board[i], flags);
		}
		parcel.writeString(turn);
	}

	protected Board(Parcel in) {
		int n = in.readInt();
		board = new Piece[n][];
		for (int i = 0; i < n; i++) {
			board[i] = (Piece[]) in.readParcelableArray(Piece.class.getClassLoader());
		}
		turn = in.readString();
	}
}
