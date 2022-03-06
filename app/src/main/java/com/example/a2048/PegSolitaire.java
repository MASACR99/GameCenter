package com.example.a2048;


import static com.example.a2048.MainActivity.USERID;
import static com.example.a2048.MainActivity.pegTb;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Locale;

public class PegSolitaire extends AppCompatActivity {

    private int originalId = 0;
    private int x1 = -1;
    private int y1 = -1;
    private int x2 = -1;
    private int y2 = -1;
    private int originalX = -1;
    private int originalY = -1;
    private boolean found1 = false;
    private boolean found2 = false;
    private boolean first_drag = true;
    private int moves = 0;
    private boolean end = false;
    private long time = 0;
    private long startTime = -1;
    private boolean running = false;
    private TextView movesText;
    private PegGame game;
    private ArrayList<PegGame> previousMoves;
    private GridLayout grid;
    private boolean saved = true;
    private boolean finish = false;

    private boolean[][] tableBoolEnglish = {
            {false, false, true, true, true, false, false},
            {false, false, true, true, true, false, false},
            {true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true},
            {false, false, true, true, true, false, false},
            {false, false, true, true, true, false, false}
    };
    private int centerEnglishX = 3;
    private int centerEnglishY = 3;

    private boolean[][] tableBoolFrench = {
            {false, false, true, true, true, false, false},
            {false, true, true, true, true, true, false},
            {true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true},
            {false, true, true, true, true, true, false},
            {false, false, true, true, true, false, false}
    };
    private int centerFrenchX = 3;
    private int centerFrenchY = 2;

    private boolean[][] tableBoolGermany = {
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, true, true, true, false, false, false},
            {true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true},
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, true, true, true, false, false, false}
    };
    private int centerGermanyX = 4;
    private int centerGermanyY = 4;

    private boolean[][] tableBoolDiamond = {
            {false, false, false, false, true, false, false, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, true, true, true, true, true, false, false},
            {false, true, true, true, true, true, true, true, false},
            {true, true, true, true, true, true, true, true, true},
            {false, true, true, true, true, true, true, true, false},
            {false, false, true, true, true, true, true, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, false, true, false, false, false, false}
    };
    private int centerDiamondX = 4;
    private int centerDiamondY = 4;

    private boolean tableBool[][];
    private int centerX;
    private int centerY;

    private ImageButton[][] buttons;
    private Bundle bunda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: On start read Board_type from the intent and setup the grid
        //based on that aswell as the logic
        bunda = getIntent().getExtras();
        switch(bunda.getString("Board_type")){
            case "French":
                Log.d("BRUH","French");
                this.tableBool = new boolean[this.tableBoolFrench.length][this.tableBoolFrench[0].length];
                for (int i = 0; i < this.tableBoolFrench.length; i++) {
                    for (int j = 0; j < this.tableBoolFrench[0].length; j++) {
                        this.tableBool[i][j] = this.tableBoolFrench[i][j];
                    }
                }
                this.centerX = this.centerFrenchX;
                this.centerY = this.centerFrenchY;
                break;
            case "German":
                Log.d("BRUH","Germany");
                this.tableBool = new boolean[this.tableBoolGermany.length][this.tableBoolGermany[0].length];
                for (int i = 0; i < this.tableBoolGermany.length; i++) {
                    for (int j = 0; j < this.tableBoolGermany[0].length; j++) {
                        this.tableBool[i][j] = this.tableBoolGermany[i][j];
                    }
                }
                this.centerX = this.centerGermanyX;
                this.centerY = this.centerGermanyY;
                break;
            case "English":
                Log.d("BRUH","English");
                this.tableBool = new boolean[this.tableBoolEnglish.length][this.tableBoolEnglish[0].length];
                for (int i = 0; i < this.tableBoolEnglish.length; i++) {
                    for (int j = 0; j < this.tableBoolEnglish[0].length; j++) {
                        this.tableBool[i][j] = this.tableBoolEnglish[i][j];
                    }
                }
                this.centerX = this.centerEnglishX;
                this.centerY = this.centerEnglishY;
                break;
            case "Diamond":
                Log.d("BRUH","Diamond");
                this.tableBool = new boolean[this.tableBoolDiamond.length][this.tableBoolDiamond[0].length];
                for (int i = 0; i < this.tableBoolDiamond.length; i++) {
                    for (int j = 0; j < this.tableBoolDiamond[0].length; j++) {
                        this.tableBool[i][j] = this.tableBoolDiamond[i][j];
                    }
                }
                this.centerX = this.centerDiamondX;
                this.centerY = this.centerDiamondY;
                break;
            default:
                Log.e("Error", "What happened here?");
                break;
        }
        setContentView(R.layout.peg_game);
        ImageButton button = findViewById(R.id.revertButtonPeg);
        previousMoves = new ArrayList<>();
        game = new PegGame(tableBool,centerX,centerY);
        previousMoves.add(new PegGame(game));
        movesText = findViewById(R.id.movesPeg);
        grid = findViewById(R.id.grid);
        grid.setColumnCount(tableBool.length);
        grid.setRowCount(tableBool[0].length);
        buttons = new ImageButton[tableBool.length][tableBool[0].length];
        GridLayout.LayoutParams params;
        LinearLayout.LayoutParams textParams;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 115;
        for (int i = 0; i < tableBool.length; i++) {
            for (int j = 0; j < tableBool[0].length; j++) {
                params = new GridLayout.LayoutParams();
                params.rightMargin = 0;
                params.topMargin = 0;
                params.height = width/tableBool[0].length +20;
                params.width = width/tableBool.length;
                params.setGravity(Gravity.CENTER);
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                buttons[i][j] = new ImageButton(getApplicationContext());
                buttons[i][j].setId(i*tableBool[0].length+j);
                buttons[i][j].setLayoutParams(params);
                buttons[i][j].setBackground(getDrawable(R.drawable.daco_4431349));
                grid.addView(buttons[i][j]);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(previousMoves.size() > 0) {
                    Log.i("Back", "Moving back");
                    if(saved) {
                        game = previousMoves.get(previousMoves.size() - 1);
                        previousMoves.remove(previousMoves.size() - 1);
                        saved = false;
                    }
                    game = previousMoves.get(previousMoves.size() - 1);
                    previousMoves.remove(previousMoves.size() - 1);
                    gameStarter();
                }
            }
        });
        timerStart();
        gameStarter();
    }

    private void gameStarter(){
        for(int i = 0; i < tableBool.length;i++){
            for(int j = 0; j < tableBool[0].length;j++){
                ImageButton selectedButton = buttons[i][j];
                if (game.isValid(i,j)){
                    if(game.isPegged(i,j)){
                        buttons[i][j].setBackground(getDrawable(R.drawable.pan_blue_circle));
                        buttons[i][j].setTag("blue");
                    }else{
                        buttons[i][j].setBackground(getDrawable(R.drawable.daco_4431349));
                        buttons[i][j].setTag("yellow");
                    }
                }else {
                    buttons[i][j].setBackground(getDrawable(android.R.color.transparent));
                    buttons[i][j].setTag("no touch");
                }
                buttons[i][j].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(selectedButton);
                            selectedButton.startDrag(null, shadowBuilder, selectedButton, 0);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                buttons[i][j].setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent) {
                        running = true;
                        ImageView original = (ImageView) dragEvent.getLocalState();
                        Rect impact;
                        if(!original.getTag().equals("no touch")){
                            switch (dragEvent.getAction()) {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    if(first_drag) {
                                        originalX = (int)dragEvent.getX();
                                        originalY = (int)dragEvent.getY();
                                        if (original.getTag() == "yellow") {
                                            originalId = R.drawable.daco_4431349;
                                        } else if (original.getTag() == "blue") {
                                            originalId = R.drawable.pan_blue_circle;
                                        }
                                        impact = new Rect();
                                        for (int i = 0; i < tableBool.length && !found1; i++) {
                                            for (int j = 0; j < tableBool[0].length && !found1; j++) {
                                                buttons[i][j].getHitRect(impact);
                                                if (impact.contains((int) dragEvent.getX(), (int) dragEvent.getY())) {
                                                    found1 = true;
                                                    x1 = i;
                                                    y1 = j;
                                                    Log.d("BRUH", "Got a correct grab: " + x1 + ", " + y1);
                                                }
                                            }
                                        }
                                        first_drag = false;
                                        original.setBackground(getDrawable(android.R.color.transparent));
                                    }
                                    break;
                                case DragEvent.ACTION_DROP:
                                    //Dropped image will be var image
                                    first_drag = true;
                                    for (int i = 0; i < tableBool.length && !found2; i++) {
                                        for (int j = 0; j < tableBool[0].length && !found2; j++) {
                                            if(buttons[i][j].getId() == selectedButton.getId()){
                                                x2 = i;
                                                y2 = j;
                                                found2 = true;
                                                Log.d("BRUH", "Got a correct drop: " + x2 + ", " + y2);
                                                Log.d("BRUH", "Button id" + buttons[i][j].getId());
                                            }
                                        }
                                    }
                                    if (found1 && found2) {
                                        Log.d("BRUH", "Attempting move: " + x1 + ", " + y1 + " | " + x2 + ", " + y2);
                                        if (game.move(x1, y1, x2, y2)) {
                                            buttons[x1][y1].setBackground(getDrawable(R.drawable.daco_4431349));
                                            buttons[x1][y1].setTag("yellow");
                                            buttons[x2][y2].setBackground(getDrawable(R.drawable.pan_blue_circle));
                                            buttons[x2][y2].setTag("blue");
                                            moves++;
                                            movesText.setText("Moves: " + Integer.toString(moves));
                                            if(x1 == x2){
                                                buttons[x1][(y1+y2)/2].setBackground(getDrawable(R.drawable.daco_4431349));
                                                buttons[x1][(y1+y2)/2].setTag("yellow");
                                            }else if(y1 == y2){
                                                buttons[(x1+x2)/2][y1].setBackground(getDrawable(R.drawable.daco_4431349));
                                                buttons[(x1+x2)/2][y1].setTag("yellow");
                                            }else{
                                                buttons[(x1+x2)/2][(y1+y2)/2].setBackground(getDrawable(R.drawable.daco_4431349));
                                                buttons[(x1+x2)/2][(y1+y2)/2].setTag("yellow");
                                            }
                                            if(game.checkWin()){
                                                Toast.makeText(getApplicationContext(),"You won!", Toast.LENGTH_LONG).show();
                                                end = true;
                                                pegTb.insertAll(new PegScore(moves, time, USERID,bunda.getString("Board_type")));
                                                try {
                                                    Thread.sleep(2000);
                                                } catch (InterruptedException e) {}
                                                finish = true;
                                                //prompt that the user won and go back to a main menu or something
                                            }else if(game.checkLose()){
                                                Toast.makeText(getApplicationContext(),"No more moves", Toast.LENGTH_LONG).show();
                                                end = true;
                                                try{
                                                    Thread.sleep(2000);
                                                }catch (InterruptedException e){}
                                                finish = true;
                                            }
                                            previousMoves.add(new PegGame(game));
                                            saved = true;
                                        }else{
                                            original.setBackground(getDrawable(originalId));
                                        }
                                    }else{
                                        original.setBackground(getDrawable(originalId));
                                    }
                                    found1 = false;
                                    found2 = false;
                                    break;
                            }
                        }
                        if(finish){finish();}
                        return true;
                    }
                });
            }
        }
    }

    private void timerStart(){
        // Get the text view.
        final TextView timeView
                = (TextView)findViewById(
                R.id.timerPeg);

        // Creates a new Handler
        final Handler handler
                = new Handler();
        Log.d("Starting timer", "Timer on");
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
                }else{
                    Log.d("Timer ending", "Timer is dead");
                }
            }
        });
    }
}