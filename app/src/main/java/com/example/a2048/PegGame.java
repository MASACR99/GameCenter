package com.example.a2048;

import com.example.pegsolitaire.Square;

public class PegGame {
    private com.example.pegsolitaire.Square[][] board;

    private boolean[][] tableBool;
    private int centerX;
    private int centerY;

    public PegGame(boolean[][] tableBool, int centerX, int centerY){
        this.tableBool = tableBool;
        this.centerX = centerX;
        this.centerY = centerY;
        board = new com.example.pegsolitaire.Square[tableBool.length][tableBool[0].length];
        for(int i = 0; i< board.length;i++){
            for(int j=0;j< board[0].length;j++){
                board[i][j] = new com.example.pegsolitaire.Square(tableBool[i][j], tableBool[i][j]);
            }
        }
        board[centerX][centerY].setPegged(false);
    }

    public PegGame(PegGame copy){
        this.board = new Square[copy.board.length][copy.board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = new Square(copy.board[i][j].isValid(),copy.board[i][j].isPegged());
            }
        }
        this.tableBool = new boolean[copy.tableBool.length][copy.tableBool[0].length];
        for (int i = 0; i < copy.tableBool.length; i++) {
            for (int j = 0; j < copy.tableBool[0].length; j++) {
                this.tableBool[i][j] = copy.tableBool[i][j];
            }
        }
        this.centerX = copy.centerX;
        this.centerY = copy.centerY;
    }

    public boolean move(int x1, int y1, int x2, int y2){
        if(!board[x2][y2].isPegged() && board[x1][y1].isPegged() && board[x2][y2].isValid()) {
            if (x1 == x2) {
                if (Math.abs(y2 - y1) == 2 && board[x1][(y1+y2)/2].isPegged()) {
                    board[x1][y1].setPegged(false);
                    board[x1][(y1+y2)/2].setPegged(false);
                    board[x2][y2].setPegged(true);
                    return true;
                }else{
                    return false;
                }
            } else if (y1 == y2) {
                if (Math.abs(x2 - x1) == 2 && board[(x1+x2)/2][y1].isPegged()) {
                    board[x1][y1].setPegged(false);
                    board[(x1+x2)/2][y1].setPegged(false);
                    board[x2][y2].setPegged(true);
                    return true;
                }else{
                    return false;
                }
            } else {
                if (Math.abs(x2 - x1) == 2 && Math.abs(y2-y1) == 2 && board[(x1+x2)/2][(y1+y2)/2].isPegged()){
                    board[x1][y1].setPegged(false);
                    board[(x1+x2)/2][(y1+y2)/2].setPegged(false);
                    board[x2][y2].setPegged(true);
                    return true;
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
    }

    public boolean checkWin(){
        int pegs = 0;
        for(int i = 0; i<board.length && pegs < 2;i++){
            for(int j = 0; j<board[0].length && pegs < 2;j++){
                if(board[i][j].isPegged()){
                    pegs++;
                }
            }
        }
        if(pegs > 1){
            return false;
        }else{
            return true;
        }
    }

    private boolean checkLeftMove(int i, int j){
        try {
            if (board[i - 1][j].isPegged() && board[i - 1][j].isValid()
                    && !board[i - 2][j].isPegged() && board[i - 2][j].isValid()) {
                return true;
            }
        }catch(Exception ex){}
        return false;
    }

    private boolean checkRightMove(int i, int j){
        try {
            if (board[i + 1][j].isPegged() && board[i + 1][j].isValid()
                    && !board[i + 2][j].isPegged() && board[i + 2][j].isValid()) {
                return true;
            }
        }catch(Exception ex){}
        return false;
    }

    private boolean checkTopMove(int i, int j){
        try {
            if (board[i][j - 1].isPegged() && board[i][j - 1].isValid()
                    && !board[i][j - 2].isPegged() && board[i][j - 2].isValid()) {
                return true;
            }
        }catch (Exception ex){}
        return false;
    }

    private boolean checkBottomMove(int i, int j){
        try {
            if (board[i][j + 1].isPegged() && board[i][j + 1].isValid()
                    && !board[i][j + 2].isPegged() && board[i][j + 2].isValid()) {
                return true;
            }
        }catch(Exception ex){}
        return false;
    }

    private boolean checkTopRight(int i, int j){
        try{
             if (board[i + 1][j - 1].isPegged() && board[i + 1][j - 1].isValid()
                    && !board[i + 2][j - 2].isPegged() && board[i + 2][j - 2].isValid()) {
                return true;
            }
        }catch(Exception ex){}
        return false;
    }

    private boolean checkTopLeft(int i, int j){
        try {
            if (board[i - 1][j - 1].isPegged() && board[i - 1][j - 1].isValid()
                    && !board[i - 2][j - 2].isPegged() && board[i - 2][j - 2].isValid()) {
                return true;
            }
        }catch(Exception ex){}
        return false;
    }

    private boolean checkBottomRight(int i, int j){
        try{
             if (board[i + 1][j + 1].isPegged() && board[i+1][j+1].isValid()
                    && !board[i + 2][j + 2].isPegged() && board[i+2][j+2].isValid()) {
                return true;
            }
        }catch (Exception ex){}
        return false;
    }

    private boolean checkBottomLeft(int i, int j){
        try{
            if (board[i - 1][j + 1].isPegged() && board[i - 1][j + 1].isValid()
                    && !board[i - 2][j + 2].isPegged() && board[i - 2][j + 2].isValid()) {
                return true;
            }
        }catch (Exception ex){}
        return false;
    }

    public boolean checkLose(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j].isPegged() && board[i][j].isValid()){
                    if(checkTopMove(i,j) || checkBottomMove(i,j) || checkLeftMove(i, j)
                            ||checkRightMove(i,j) || checkTopLeft(i,j) || checkTopRight(i,j)
                            || checkTopLeft(i,j) || checkBottomLeft(i,j) || checkBottomRight(i,j)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void removePeg(int x, int y){
        board[x][y].setPegged(false);
    }

    public void addPeg(int x, int y){
        board[x][y].setPegged(true);
    }

    public boolean isPegged(int x, int y){
        return board[x][y].isPegged();
    }

    public boolean isValid(int x, int y){
        return board[x][y].isValid();
    }

    public int getLengthX(){
        return board.length;
    }

    public int getLengthY(){
        return board[0].length;
    }
}
