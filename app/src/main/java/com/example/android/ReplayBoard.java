package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.chessboard.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReplayBoard extends AppCompatActivity {
    private GridView gridview;
    private ImageAdapter myImgAdapter;
    private Board game;

    BufferedReader read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replayboard);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString(Replays.FILE_NAME);
        System.out.println(name);

        File file = new File(getExternalFilesDir(null), name);
        try {
            read = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        game = new Board();

        Button next = (Button) findViewById(R.id.next);

        myImgAdapter = new ImageAdapter(this, game);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setBackgroundResource(R.drawable.board);
        gridview.setAdapter(myImgAdapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String line = "";
                try {
                    line = currLine();
                } catch (IOException e) {
                    return;
                }
                if (line == null || line == "") return;

                Position one = new Position(Integer.parseInt(line.substring(0,1)), Integer.parseInt(line.substring(2,3)));
                Position two = new Position(Integer.parseInt(line.substring(4,5)), Integer.parseInt(line.substring(6,7)));
                move(game, one, two);
            }
        });
    }

    public String currLine() throws IOException {
        if (read == null) return "";
        return read.readLine();
    }

    public void move(Board game, Position currCoords, Position move) {
        Piece curr = game.board[currCoords.vert][currCoords.horiz];

        // Promotion & en passant
        if (curr instanceof Pawn) {
            read_pawnHelper(game, curr, currCoords, move);
            curr = game.board[currCoords.vert][currCoords.horiz];
        }

        // Castling
        if (curr instanceof King) {
            int direction = move.horiz - currCoords.horiz;
            if (direction == -2) {
                game.board[move.vert][move.horiz + 1] = game.board[move.vert][0];
                game.board[move.vert][0] = null;
            } else if (direction == 2) {
                game.board[move.vert][move.horiz - 1] = game.board[move.vert][7];
                game.board[move.vert][7] = null;
            }
        }

        curr.firstMove = false;
        game.prev = game.duplicate(game.board);
        game.board[move.vert][move.horiz] = curr;
        game.board[currCoords.vert][currCoords.horiz] = null;
        game.turn = (game.turn == "White") ? "Black" : "White";
        game.moves.add(currCoords + " " + move);
        updateAdapter(game);
    }


    public void read_pawnHelper(Board game, Piece curr, Position currCoords, Position move) {
        Pawn pawnCurr = (Pawn) curr;

        // If the pawn is doing two-square move, mark it as valid for en passant
        if (Math.abs(currCoords.vert - move.vert) == 2) pawnCurr.enPassant = true;
        else pawnCurr.enPassant = false;

        // Promotion
        int promotion = (pawnCurr.player == 'w') ? 0 : 7;
        if (move.vert == promotion) {
            game.board[currCoords.vert][currCoords.horiz] = new Queen(curr.player);
            return;
        }

        // En passant
        int passing = (pawnCurr.player == 'w') ? move.vert + 1 : move.vert - 1;
        if (game.board[passing][move.horiz] != null &&
                game.board[passing][move.horiz].player != curr.player &&
                game.board[passing][move.horiz] instanceof Pawn) {
            Pawn candidate = (Pawn) game.board[passing][move.horiz];
            if (candidate.enPassant) game.board[passing][move.horiz] = null;
        }
    }

    public void updateAdapter(Board game) {
        myImgAdapter = new ImageAdapter(this, game);
        gridview.setAdapter(myImgAdapter);
    }
}