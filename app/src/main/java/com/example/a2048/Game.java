package com.example.a2048;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int rows;
    private int columns;
    private int winningNumber;
    private int[][] board;
    private ArrayList<Integer> emptySpots = new ArrayList<>();
    private boolean[][] alreadyFused;

    public Game(int rows, int columns, int winningNumber){
        this.rows = rows;
        this.columns = columns;
        this.winningNumber = winningNumber;
        board = new int[rows][columns];
        alreadyFused = new boolean[rows][columns];
        for(int i = 0; i<rows;i++){
            for(int j=0;j<columns;j++){
                board[i][j] = -1;
                emptySpots.add(i*columns+j);
                alreadyFused[i][j] = false;
            }
        }
    }

    public void random(){
        Log.i("Random","Got into random");
        Random ran = new Random();
        int position;
        int random;
        if(emptySpots.size() != 0) {
            position = emptySpots.get(ran.nextInt(emptySpots.size()));
        }else{
            position = 0;
        }
        random = ran.nextInt(100);
        if(random <= 10) {
            board[(int)position/rows][position%columns] = 4;
        }else{
            board[(int)position/rows][position%columns] = 2;
        }
    }

    public String[][] print(){
        String[][] retVal = new String[rows][columns];
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
                if(board[i][j] == -1){
                    retVal[i][j] = "-1";
                }else{
                    retVal[i][j] = Integer.toString(board[i][j]);
                }
            }
        }
        return retVal;
    }

    public void update_empty(){
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(board[i][j] == -1){
                    if(!emptySpots.contains(new Integer(i*columns+j))){
                        emptySpots.add(i*columns+j);
                    }
                }else{
                    if(emptySpots.contains(new Integer(i*columns+j))){
                        emptySpots.remove(new Integer(i*columns+j));
                    }
                }
            }
        }
    }

    //Up 0, right 1, down 2, left 3
    public int move(int direction){
        Log.i("Moving","Got into move");
        int points = 0;
        switch(direction){
            case 0:
                while(!check_moves(0)){
                    for(int i = 1; i < rows;i++){
                        for(int j = 0;j < columns;j++){
                            if(board[i][j] != -1 && board[i-1][j] == -1){
                                board[i-1][j] = board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i-1][j] = alreadyFused[i][j];
                            }else if((board[i][j] != -1 && board[i][j] == board[i-1][j]) && !alreadyFused[i][j] && !alreadyFused[i-1][j]){
                                points += board[i][j];
                                board[i-1][j] += board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i-1][j] = true;
                            }
                        }
                    }
                }
                break;
            case 1:
                while(!check_moves(1)) {
                    for (int i = 0; i < rows; i++) {
                        for (int j = columns-2; j >= 0; j--) {
                            if(board[i][j] != -1 && board[i][j+1] == -1){
                                board[i][j+1] = board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i][j+1] = alreadyFused[i][j];
                            }else if(board[i][j] != -1 && board[i][j] == board[i][j+1] && !alreadyFused[i][j] && !alreadyFused[i][j+1]){
                                points += board[i][j];
                                board[i][j+1] += board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i][j+1] = true;
                            }
                        }
                    }
                }
                break;
            case 2:
                while(!check_moves(2)) {
                    for (int i = rows-2; i >= 0; i--) {
                        for (int j = columns-1; j >= 0; j--) {
                            if(board[i][j] != -1 && board[i+1][j] == -1){
                                board[i+1][j] = board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i+1][j] = alreadyFused[i][j];
                            }else if(board[i][j] != -1 && board[i][j] == board[i+1][j] && !alreadyFused[i][j] && !alreadyFused[i+1][j]){
                                points += board[i][j];
                                board[i+1][j] += board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i+1][j] = true;
                            }
                        }
                    }
                }
                break;
            case 3:
                while(!check_moves(3)) {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 1; j < columns; j++) {
                            if(board[i][j] != -1 && board[i][j-1] == -1){
                                board[i][j-1] = board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i][j-1] = alreadyFused[i][j];
                            }else if(board[i][j] != -1 && board[i][j] == board[i][j-1] && !alreadyFused[i][j] && !alreadyFused[i][j-1]){
                                points += board[i][j];
                                board[i][j-1] += board[i][j];
                                board[i][j] = -1;
                                alreadyFused[i][j-1] = true;
                            }
                        }
                    }
                }
                break;
            default:
                Log.wtf("nah dude", "get the fuck out of here");
                break;
        }
        setBoolsToFalse();
        update_empty();
        return points;
    }

    public boolean check_moves(int direction){
        switch(direction){
            case 0:
                for(int i = 1; i < rows;i++){
                    for(int j = 0;j < columns;j++){
                        if((board[i][j] != -1 && board[i-1][j] == -1) || ((board[i][j] != -1 && board[i][j] == board[i-1][j]) && !alreadyFused[i][j] && !alreadyFused[i-1][j])){
                            return false;
                        }
                    }
                }
                return true;
            case 1:
                for(int i = 0; i < rows;i++){
                    for(int j = 0;j < columns-1;j++){
                        if((board[i][j] != -1 && board[i][j+1] == -1) || ((board[i][j] != -1 && board[i][j] == board[i][j+1]) && !alreadyFused[i][j] && !alreadyFused[i][j+1])){
                            return false;
                        }
                    }
                }
                return true;
            case 2:
                for(int i = 0; i < rows-1;i++){
                    for(int j = 0;j < columns;j++){
                        if((board[i][j] != -1 && board[i+1][j] == -1) || ((board[i][j] != -1 && board[i][j] == board[i+1][j]) && !alreadyFused[i][j] && !alreadyFused[i+1][j])){
                            return false;
                        }
                    }
                }
                return true;
            case 3:
                for(int i = 0; i < rows;i++){
                    for(int j = 1;j < columns;j++){
                        if((board[i][j] != -1 && board[i][j-1] == -1) || ((board[i][j] != -1 && board[i][j] == board[i][j-1]) && !alreadyFused[i][j] && !alreadyFused[i][j-1])){
                            return false;
                        }
                    }
                }
                return true;
            default:
                Log.wtf("Oh hell no", "How the fuck?");
                return false;
        }
    }

    public boolean isWon(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == winningNumber){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLost(){
        if(this.check_moves(0)){
            if(this.check_moves(1)){
                if(this.check_moves(2)){
                    if(this.check_moves(3)){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private void setBoolsToFalse(){
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<columns; j++){
                alreadyFused[i][j] = false;
            }
        }
    }
}