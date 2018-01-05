package com.adit.sudokusolver;

import java.util.HashSet;
import java.util.Stack;

class Cell {
    private int value;
    private Boolean set;
    private HashSet<Integer> possible;
    private int guessValue;
    private Boolean guessSet;
    private Boolean solved;
    private Boolean input;
    private Stack<HashSet<Integer>> guesses;

    public Cell(int val){
        value = val;
        set = val != 0;
        guesses = new Stack<>();
        solved = false;
        input = false;
    }

    public Cell(int val, Boolean in){
        value = val;
        set = val != 0;
        guesses = new Stack<>();
        solved = false;
        input = in;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.set = true;
    }

    public void setSolved(){
        solved = true;
    }

    public Boolean getSet() {
        return set;
    }

    public void setPossible(HashSet<Integer> possible) {
        this.possible = possible;
    }

    public HashSet<Integer> getPossible() {
        return possible;
    }

    public int getGuessValue() {
        return guessValue;
    }

    public void setGuessValue(int guessValue) {
        this.guessValue = guessValue;
        this.guessSet = true;
    }

    public Boolean getGuessSet() {
        if (guessSet == null)
            guessSet = false;
        return guessSet;
    }

    public Boolean getSolved(){
        return solved;
    }

    public Boolean getInput() { return input; }

    public HashSet<Integer> getGuess() {
        if (guesses.empty()){
            return null;
        }
        else return guesses.peek();
    }

    public void addGuess(HashSet<Integer> guess) {
        this.guesses.push(guess);
    }

    public Boolean emptyStack() {
        return this.guesses.empty();
    }

    public void resetGuessPossible(){
        this.guesses.pop();
    }

    public void resetGuess() {
        this.guessValue = 0;
        this.guessSet = false;
    }
}
