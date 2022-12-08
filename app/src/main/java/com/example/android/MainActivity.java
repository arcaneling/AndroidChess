package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.chessboard.*;

public class MainActivity extends AppCompatActivity {

    private Button playButton, replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.welcome);

        playButton = findViewById(R.id.play);
        replayButton = findViewById(R.id.replay);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Board game = new Board();
                startActivity(new Intent(MainActivity.this, ChessBoard.class));
            }
        });
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Replays.class));
            }
        });
    }
}