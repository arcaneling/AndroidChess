package com.example.android;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chessboard.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard extends AppCompatActivity {
    private GridView gridview;
    private ImageAdapter myImgAdapter;
    private Board game;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView result;
    private EditText titlePrompt;
    private Button saveButton;
    private Button quitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        game = new Board();
        Position[] moves = new Position[2];

        Button undo = (Button) findViewById(R.id.undo);
        Button AI = (Button) findViewById(R.id.next);
        Button draw = (Button) findViewById(R.id.draw);
        Button resign = (Button) findViewById(R.id.resign);

        myImgAdapter = new ImageAdapter(this, game);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setBackgroundResource(R.drawable.board);
        gridview.setAdapter(myImgAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int i = (int) Math.floor(position / 8);
                int j = position % 8;
                Piece curr = game.board[i][j];

                if (moves[0] == null) {
                    if (curr == null) return;
                    if (curr.player != Character.toLowerCase(game.turn.charAt(0))) {
                        Toast.makeText(parent.getContext(), game.turn + "'s Move", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    moves[0] = new Position(i,j);
                }
                else {
                    moves[1] = new Position(i,j);
                    Piece start = game.board[moves[0].vert][moves[0].horiz];

                    List<Position> moveset = start.moveset(game, moves[0]);
                    if (!moveset.contains(moves[1])) {
                        Toast.makeText(parent.getContext(), "Invalid move", Toast.LENGTH_SHORT).show();
                    }
                    else move(game, moves[0], moves[1]);

                    moves[0] = null;
                    moves[1] = null;
                }

                if (game.inCheckmate(Character.toLowerCase(game.turn.charAt(0)))) {
                    gameEndScreen((game.turn == "White") ? "Black wins" : "White wins");
                }
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (game.equals()) {
                    Toast.makeText(view.getContext(), "Cannot undo", Toast.LENGTH_SHORT).show();
                    return;
                }
                game.board = game.duplicate(game.prev);
                game.turn = (game.turn == "White") ? "Black" : "White";
                game.moves.remove(game.moves.size() - 1);
                updateAdapter(game);
            }
        });

        AI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Position> pieces = game.getPieces(Character.toLowerCase(game.turn.charAt(0)));
                List<Position> moveset = new ArrayList<Position>();

                int piece = 0;
                int nextMove = 0;

                while (moveset.size() == 0) {
                    piece = (int) (Math.random() * pieces.size());
                    Piece curr = game.board[pieces.get(piece).vert][pieces.get(piece).horiz];

                    moveset = curr.moveset(game, pieces.get(piece));
                    nextMove = (int) (Math.random() * moveset.size());
                }

                move(game, pieces.get(piece), moveset.get(nextMove));
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameEndScreen("Draw");
            }
        });

        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameEndScreen((game.turn == "White") ? "Black wins" : "White wins");
            }
        });
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
            }
            else if (direction == 2) {
                game.board[move.vert][move.horiz - 1] = game.board[move.vert][7];
                game.board[move.vert][7] = null;
            }
        }

        // Move the pieces
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

    public void gameEndScreen(String res) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popup = getLayoutInflater().inflate(R.layout.popup, null);

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();

        titlePrompt = (EditText) popup.findViewById(R.id.enterTitle);
        saveButton = (Button) popup.findViewById(R.id.saveGame);
        quitButton = (Button) popup.findViewById(R.id.quitGame);
        result = (TextView) popup.findViewById(R.id.result);
        result.setText(res);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTitle = titlePrompt.getText().toString();
                if (newTitle.length() == 0) {
                    Toast.makeText(view.getContext(), "Invalid Title", Toast.LENGTH_SHORT).show();
                    return;
                }

                File dir = getExternalFilesDir(null);
                System.out.println(dir);
                File file = new File(dir, newTitle + ".txt");
                if (file.exists()) {
                    Toast.makeText(view.getContext(), "Title already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    FileOutputStream str = new FileOutputStream(file);
                    for (String move: game.moves) {
                        try {
                            str.write((move + "\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    str.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(ChessBoard.this, MainActivity.class));
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ChessBoard.this, MainActivity.class));
            }
        });
    }
}