package com.example.coinflip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView coinImageView;
    private TextView flipCountTextView;
    private int flipCount = 0;
    private int gameCount = 0;
    private int winCount = 0;
    private int loseCount = 0;
    private boolean isFlipping = false;
    private final Handler handler = new Handler();
    private final Random random = new Random();

    private final int[] coinImages = new int[80];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coinImageView = findViewById(R.id.coinImageView);
        flipCountTextView = findViewById(R.id.flipCountTextView);
        Button headsButton = findViewById(R.id.headsButton);
        Button tailsButton = findViewById(R.id.tailsButton);

        for (int i = 0; i < 80; i++) {
            String imageName = "silver_" + (i + 1);
            @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(imageName, "drawable", getPackageName());
            coinImages[i] = resID;
        }

        headsButton.setOnClickListener(v -> {
            if (!isFlipping) {
                playGame(1);
            }
        });

        tailsButton.setOnClickListener(v -> {
            if (!isFlipping) {
                playGame(0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void playGame(int userGuess) {
        isFlipping = true;
        flipCount++;
        flipCountTextView.setText("Fordulat: " + flipCount);

        int programChoice = random.nextInt(2);

        if (programChoice == 1) {
            runFlipAnimation(71, userGuess, programChoice);
        } else {
            runFlipAnimation(61, userGuess, programChoice);
        }
    }

    private void runFlipAnimation(int totalFrames, int userGuess, int programChoice) {
        final int frameDuration = 100;
        final int[] currentFrame = {0};

        final Runnable flipRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentFrame[0] < totalFrames) {
                    coinImageView.setImageResource(coinImages[currentFrame[0]]);
                    currentFrame[0]++;
                    handler.postDelayed(this, frameDuration);
                } else {
                    if (programChoice == 1) {
                        coinImageView.setImageResource(coinImages[70]);
                    } else {
                        coinImageView.setImageResource(coinImages[60]);
                    }

                    if (userGuess == programChoice) {
                        winCount++;
                        Toast.makeText(MainActivity.this, "Eltaláltad!", Toast.LENGTH_SHORT).show();
                    } else {
                        loseCount++;
                        Toast.makeText(MainActivity.this, "Nem találtad el!", Toast.LENGTH_SHORT).show();
                    }

                    gameCount++;

                    if (gameCount == 5) {
                        showEndGameDialog();
                    }

                    isFlipping = false;
                }
            }
        };

        handler.post(flipRunnable);
    }

    private void showEndGameDialog() {
        String resultMessage;
        if (winCount > loseCount) {
            resultMessage = "Nyertél! Összesen " + winCount + " nyertes játékod volt.";
        } else if (loseCount > winCount) {
            resultMessage = "Vesztettél! Összesen " + loseCount + " vesztett játékod volt.";
        } else {
            resultMessage = "Döntetlen! Összesen " + winCount + " nyertes játékod és " + loseCount + " vesztett játékod volt.";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Játék vége");
        builder.setMessage(resultMessage + "\nSzeretnél új játékot kezdeni?");
        builder.setPositiveButton("Igen", (dialog, which) -> resetGame());
        builder.setNegativeButton("Nem", (dialog, which) -> finish());
        builder.show();
    }

    @SuppressLint("SetTextI18n")
    private void resetGame() {
        gameCount = 0;
        winCount = 0;
        loseCount = 0;
        flipCount = 0;
        flipCountTextView.setText("Fordulat: 0");
        coinImageView.setImageResource(R.drawable.silver_1);
    }
}
