package com.example.a2048;

import static com.example.a2048.MainActivity.USERID;
import static com.example.a2048.MainActivity.twentyTb;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Locale;

public class GameStarter extends AppCompatActivity {
    private GestureDetectorCompat mDetector;
    private final String DEBUG_TAG = "Oof";
    private int score = 0;
    private int moves = 0;
    private TextView scoreText;
    private TextView movesText;
    private boolean hasWon = false;
    private boolean running = false;
    private long startTime = -1;
    private Game game;
    private boolean end = false;
    private long time = 0;
    private LinearLayout layouts[][];
    private TextView gameCells[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        scoreText = findViewById(R.id.Score);
        movesText = findViewById(R.id.Moves);
        GridLayout grid = findViewById(R.id.grid);
        grid.setRowCount(extras.getInt("Size"));
        grid.setColumnCount(extras.getInt("Size"));
        game = new Game(grid.getRowCount(),grid.getColumnCount(),extras.getInt("Score"));
        game.random();
        timerStart();
        layouts = new LinearLayout[grid.getColumnCount()][grid.getRowCount()];
        gameCells = new TextView[grid.getColumnCount()][grid.getRowCount()];
        GridLayout.LayoutParams params;
        LinearLayout.LayoutParams textParams;
        try {
            for (int i = 0; i < grid.getRowCount(); i++) {
                for (int j = 0; j < grid.getColumnCount(); j++) {
                    params = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f),GridLayout.spec(GridLayout.UNDEFINED, 1f));
                    textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
                    layouts[i][j] = new LinearLayout(getApplicationContext());
                    layouts[i][j].setBackground(getDrawable(R.drawable.border));
                    layouts[i][j].setLayoutParams(params);
                    layouts[i][j].setGravity(Gravity.CENTER);
                    layouts[i][j].setOrientation(LinearLayout.HORIZONTAL);
                    gameCells[i][j] = new TextView(getApplicationContext());
                    gameCells[i][j].setGravity(Gravity.CENTER);
                    gameCells[i][j].setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    if(grid.getRowCount() == 5){
                        gameCells[i][j].setTextSize(18f);
                    }else {
                        gameCells[i][j].setTextSize(24f);
                    }
                    gameCells[i][j].setLayoutParams(textParams);
                    layouts[i][j].addView(gameCells[i][j]);
                }
            }
        }catch (Exception e){}
        try{
            for (int i = 0; i < grid.getRowCount(); i++) {
                for (int j = 0; j < grid.getColumnCount(); j++) {
                    grid.addView(layouts[i][j]);
                }
            }
        }catch (Exception e){}
        mDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float angle = (float) Math.toDegrees(Math.atan2(e1.getY() - e2.getY(), e1.getX() - e2.getX()));
                boolean valid_move = false;
                //Must check if move is possible
                if (angle > -45 && angle <= 45) {
                    Log.d(DEBUG_TAG, "Right to Left swipe performed");
                    if (!game.check_moves(3)) {
                        running = true;
                        score += game.move(3);
                        valid_move = true;
                    } else {
                        Log.d("Yeh: ", "No moves on that direction");
                        valid_move = false;
                    }
                }
                if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
                    Log.d(DEBUG_TAG, "Left to Right swipe performed");
                    if (!game.check_moves(1)) {
                        running = true;
                        score += game.move(1);
                        valid_move = true;
                    } else {
                        Log.d("Yeh: ", "No moves on that direction");
                        valid_move = false;
                    }
                }
                if (angle < -45 && angle >= -135) {
                    Log.d(DEBUG_TAG, "Up to Down swipe performed");
                    if (!game.check_moves(2)) {
                        running = true;
                        score += game.move(2);
                        valid_move = true;
                    } else {
                        Log.d("Yeh: ", "No moves on that direction");
                        valid_move = false;
                    }
                }
                if (angle > 45 && angle <= 135) {
                    Log.d(DEBUG_TAG, "Down to Up swipe performed");
                    if (!game.check_moves(0)) {
                        running = true;
                        score += game.move(0);
                        valid_move = true;
                    } else {
                        Log.d("Yeh: ", "No moves on that direction");
                        valid_move = false;
                    }
                }
                if (valid_move) {
                    if(!hasWon && game.isWon()){
                        score = score * 2; //if you win your score is multiplied by 2
                        hasWon = true; //just to avoid having a popup everytime
                        end = true;
                        twentyTb.insertAll(new TwentyScore(score, time, USERID, gameCells.length, extras.getInt("Score")));
                        showWinPopup(findViewById(R.id.grid));
                    }
                    moves++;
                    game.random();
                    printValues();
                }else if(game.isLost()){
                    //splash screen telling he lost, store score, time and ask name
                    end = true;
                    showLosePopup(findViewById(R.id.grid));
                    TextView timer = findViewById(R.id.timerTwo);
                    twentyTb.insertAll(new TwentyScore(score, time, USERID, gameCells.length, extras.getInt("Score")));
                }

                return valid_move;
            }
        });
        printValues();
    }

    public void showWinPopup(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.twenty_win_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void showLosePopup(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.twenty_lose_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void printValues(){
        String[][] newVals = game.print();
        TextView tv;
        scoreText.setText("Score: " + score);
        movesText.setText("Moves: " + moves);
        for(int i=0;i< newVals.length;i++){
            for(int j=0;j< newVals[0].length;j++){
                tv = gameCells[i][j];
                if(newVals[i][j].equals(Integer.toString(-1))){
                    tv.setText("                ");
                }else{
                    tv.setText(newVals[i][j]);
                }
            }
        }
    }

    private void timerStart(){
        // Get the text view.
        final TextView timeView
                = (TextView)findViewById(
                R.id.timerTwo);

        // Creates a new Handler
        final Handler handler
                = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int timeChange = 0;
                if(running){
                    if(startTime == -1){
                        startTime = System.currentTimeMillis();
                    }else{
                        timeChange = (int)(System.currentTimeMillis() - startTime);
                    }
                    time = timeChange;
                }
                int millis = timeChange%1000;
                int secs = (timeChange/1000)%60;
                int minutes = (timeChange/1000)/60;


                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d",
                                minutes, secs, millis);

                // Set the text view text.
                timeView.setText(time);

                // Post the code again
                // with a delay of 1 second.
                if(!end) {
                    handler.postDelayed(this, 100);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return mDetector.onTouchEvent(motionEvent);
    }
}
