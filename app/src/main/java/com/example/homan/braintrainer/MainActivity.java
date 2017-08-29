package com.example.homan.braintrainer;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    CountDownTimer countDownTimer;

    Button startButton;
    Button aButton;
    Button bButton;
    Button cButton;
    Button dButton;
    RadioGroup radioMaxNumber;
    RadioGroup radioTimer;

    TextView startText;
    TextView sumTextView;
    TextView pointTextView;
    TextView resultTextView;
    TextView timerTextView;

    ConstraintLayout topLayout;
    GridLayout midLayout;
    LinearLayout bottommLayout;
    LinearLayout startLayout;

    List<Question> questions;
    Question presentQ;

    int timeLimit;
    int score;
    int numberOfQuestion;
    //test range
    int maxTestNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //=========================================================ini vars
        sumTextView = (TextView) findViewById(R.id.sumTextView);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);

        //choice buttons
        aButton = (Button) findViewById(R.id.aButton);
        bButton = (Button) findViewById(R.id.bButton);
        cButton = (Button) findViewById(R.id.cButton);
        dButton = (Button) findViewById(R.id.dButton);
        startButton = (Button) findViewById(R.id.startButton);
        radioMaxNumber = (RadioGroup) findViewById(R.id.radioMaxNumber);
        radioTimer = (RadioGroup) findViewById(R.id.radioTimer);

        //layouts
        startLayout = (LinearLayout) findViewById(R.id.startLayout);
        topLayout = (ConstraintLayout) findViewById(R.id.topLayout);
        midLayout =  (GridLayout) findViewById(R.id.midLayout);
        bottommLayout = (LinearLayout) findViewById(R.id.bottomLayout);

        //declare question array list
        questions = new ArrayList<Question>();

        numberOfQuestion = 0;
        score = 0;
        maxTestNumber = 0;

        startButton =  (Button) findViewById(R.id.startButton);
        startText = (TextView) findViewById(R.id.startText);

        //=========================================================end vars

        addListenerOnRadioButton();

    }//end onCreate

    public void playSoundEffect(String sound) {
        AssetFileDescriptor descriptor = null;
        mediaPlayer = new MediaPlayer();

        try {
            switch (sound) {
                case "tata":
                    descriptor = getAssets().openFd("sound/tata.wav");
                    break;
                case "yes":
                    descriptor = getAssets().openFd("sound/yes.wav");
                    break;
                case "wrong":
                    descriptor = getAssets().openFd("sound/wrong.wav");
                    break;
                case "try_again":
                    descriptor = getAssets().openFd("sound/try_again.wav");
                    break;
                case "congrat":
                    descriptor = getAssets().openFd("sound/congrat.wav");
                    break;
                case "firework":
                    descriptor = getAssets().openFd("sound/firework.wav");
                    break;
                default:
                    System.out.println("Invalid sound file.");
                    break;
            }
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    if (mediaPlayer != null) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addListenerOnRadioButton() {
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //play tata
                playSoundEffect("tata");

                // get selected radio button from radioGroup
                int selectedId = radioMaxNumber.getCheckedRadioButtonId();
                int timerId = radioTimer.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioMax = (RadioButton) findViewById(selectedId);
                RadioButton timerMax = (RadioButton) findViewById(timerId);

                Toast.makeText(MainActivity.this, "Max Range to "+
                        (String)radioMax.getTag()+"  &&  Time set "+
                        (String)timerMax.getTag(), Toast.LENGTH_SHORT).show();

                maxTestNumber = Integer.valueOf((String)radioMax.getTag());
                timeLimit = Integer.valueOf((String)timerMax.getTag());

                playAgain();
            }

        });
    }


    public void playAgain() {
        topLayout.setVisibility(View.VISIBLE);
        midLayout.setVisibility(View.VISIBLE);
        bottommLayout.setVisibility(View.VISIBLE);
        startLayout.setVisibility(View.INVISIBLE);

        resultTextView.setText("Your Guess?");

        //Generate 1st. question
        presentQ = new Question(maxTestNumber);
        questions.add(presentQ);
        updateQuestionScreen(presentQ);

        //Timer
        countDownTimer = new CountDownTimer(timeLimit*1000, 1000) {

            @Override
            public void onTick(long mms) {
                timerTextView.setText(String.valueOf(mms / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timerTextView.setText("X");
                resultTextView.setText("Done!");
                exitWindows();
            }
        }.start();
    }//end playAgain

    public void updateQuestionScreen(Question x) {
        sumTextView.setText(x.getEquation());
        aButton.setText(x.choiceA());
        bButton.setText(x.choiceB());
        cButton.setText(x.choiceC());
        dButton.setText(x.choiceD());
    }

    public void updateScoreScreen(boolean won) {
        //update right hand side of screen cornor
        pointTextView.setText(String.valueOf(score)+ "/"+ String.valueOf(numberOfQuestion));

        if (won) {
            resultTextView.setText("Correct!");
            resultTextView.setTextColor(Color.parseColor("#FF0000"));
            playSoundEffect("yes");

        } else {
            resultTextView.setText("Wrong!");
            resultTextView.setTextColor(Color.parseColor("#808000"));
            playSoundEffect("wrong");
        }

    }

    public void chooseAnswer(View view) {
        int clientAnswer = Integer.valueOf((String)view.getTag());
        //Log.i("tms awswer", String.valueOf(clientAnswer) + " to "+ presentQ.getAnswer());

        boolean winning = false;
        if (clientAnswer == presentQ.getAnswer()) {
            score++;
            winning = true;
        }
        numberOfQuestion++;
        //update right hand side of screen corner
        updateScoreScreen(winning);

        presentQ = new Question(maxTestNumber);
        updateQuestionScreen(presentQ);
    }


    public void updateNew() {
        //update unit
        numberOfQuestion = 0;
        score = 0;
        maxTestNumber = 0;
        timeLimit = 0;
        //hide layouts
        topLayout.setVisibility(View.INVISIBLE);
        midLayout.setVisibility(View.INVISIBLE);
        bottommLayout.setVisibility(View.INVISIBLE);
        //back to first page
        startLayout.setVisibility(View.VISIBLE);
        addListenerOnRadioButton();

        Toast.makeText(MainActivity.this, "Yes, start a new game.", Toast.LENGTH_SHORT).show();

    }

    public void newGame(View view) {
        countDownTimer.cancel();
        updateNew();
    }

    public void exitWindows() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }

        String msg = "";
        int winRate = score * 100 / numberOfQuestion;
        if (winRate < 60) {
            playSoundEffect("try_again");
            msg = "Sorry";
        } else if (winRate > 60 && winRate < 90) {
            playSoundEffect("congrat");
            msg = "Congratulation";
        } else if (winRate == 100) {
            playSoundEffect("firework");
            playSoundEffect("congrat");
            msg = "You're Genius";
        }

        builder.setTitle("Brain Test Game")
                .setMessage("Your Score:\n"+"\n\t" + String.valueOf(score)+
                        " are correct in Total " + String.valueOf(numberOfQuestion) + "\n\n"
                        + winRate + " %\n\n" + msg +
                        "!\nDo you want to play again?")
                .setNegativeButton("Continue!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //update unit
                        updateNew();
                    }
                })

                .setPositiveButton("Exit...", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
