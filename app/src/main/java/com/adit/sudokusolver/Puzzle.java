package com.adit.sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

class Puzzle {
    private ArrayList<LinkedHashSet<Integer>> rows = new ArrayList<>();
    private ArrayList<LinkedHashSet<Integer>> cols = new ArrayList<>();
    private ArrayList<LinkedHashSet<Integer>> boxes = new ArrayList<>(9);
    private Cell[][] grid;

    public Puzzle(Cell[][] input){
        this.grid = input;
        for (int j = 0; j < 9; j++) {
            List<Cell> trow0 = new ArrayList<>();
            trow0 = Arrays.asList(grid[j]);
            List<Integer> trow = new ArrayList<>();
            for(Cell cell : trow0)
                trow.add(cell.getValue());
            rows.add(new LinkedHashSet<>(trow));
            LinkedHashSet<Integer> tcol = new LinkedHashSet<>();
            for (int i = 0; i < 9; i++) {
                tcol.add(grid[i][j].getValue());
                try {
                    int box = getBox(j, i);
                    (boxes.get(box)).add(grid[j][i].getValue());
                } catch(IndexOutOfBoundsException e) {
                    LinkedHashSet<Integer> t = new LinkedHashSet<>();
                    t.add(grid[j][i].getValue());
                    boxes.add(t);
                }
            }
            cols.add(tcol);
        }
        solve();
    }

    private int getBox(int j, int i){
        if (j < 3) {
            if (i < 3) {
                return 0;
            }
            else if (i < 6){
                return 1;
            }
            else {
                return 2;
            }
        }
        else if (j < 6) {
            if (i < 3) {
                return 3;
            }
            else if (i < 6){
                return 4;
            }
            else {
                return 5;
            }
        }
        else {
            if (i < 3) {
                return 6;
            }
            else if (i < 6){
                return 7;
            }
            else {
                return 8;
            }
        }
    }

    private HashSet<Integer> possibles(int row, int col){
        HashSet<Integer> possible = new HashSet<>();
        int box = getBox(row, col);

        for (int i = 1; i < 10; i++){
            if (!(rows.get(row).contains(i)) && !(cols.get(col).contains(i)) && !(boxes.get(box).contains(i))){
                possible.add(i);
            }
        }
        return possible;
    }

    private void reset(ArrayList<ArrayList<Integer>> changesPossibles, ArrayList<ArrayList<Integer>> changesConstraints){
        for (ArrayList<Integer> changesPossible : changesPossibles) {
            grid[changesPossible.get(0)][changesPossible.get(1)].resetGuessPossible();
        }
        for (ArrayList<Integer> changesConstraint : changesConstraints) {
            int i = changesConstraint.get(0);
            int j = changesConstraint.get(1);
            int val = changesConstraint.get(2);
            grid[i][j].resetGuess();
            rows.get(i).remove(val);
            cols.get(j).remove(val);
            boxes.get(getBox(i, j)).remove(val);
        }
    }

    private void guess(int[] g){
        Iterator<Integer> potentialIterator;
        if (grid[g[0]][g[1]].emptyStack())
            potentialIterator = grid[g[0]][g[1]].getPossible().iterator();
        else potentialIterator = grid[g[0]][g[1]].getGuess().iterator();
        finish:
        while (potentialIterator.hasNext()) {
            ArrayList<ArrayList<Integer>> changesPossibles = new ArrayList<>();
            ArrayList<ArrayList<Integer>> changesConstraints = new ArrayList<>();
            int potential = potentialIterator.next();
            grid[g[0]][g[1]].setGuessValue(potential);
            rows.get(g[0]).add(potential);
            cols.get(g[1]).add(potential);
            boxes.get(getBox(g[0],g[1])).add(potential);
            changesConstraints.add(new ArrayList<>(Arrays.asList(g[0], g[1], potential)));
            cycle:
            while (true) {
                int improve = 0;
                int[] bestGuess = {-1, -1, 9};
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        HashSet<Integer> initial;
                        if (grid[i][j].emptyStack()) {
                            initial = grid[i][j].getPossible();
                        } else {
                            initial = grid[i][j].getGuess();
                        }
                        HashSet<Integer> temp;
                        if (!(grid[i][j].getSet() || grid[i][j].getGuessSet())) {
                            temp = possibles(i, j);
                            if (initial.size() > temp.size()) {
                                grid[i][j].addGuess(temp);
                                changesPossibles.add(new ArrayList<>(Arrays.asList(i, j)));
                            }
                            if (temp.size() != 1 && temp.size() < bestGuess[2]) {
                                bestGuess[2] = temp.size();
                                bestGuess[1] = j;
                                bestGuess[0] = i;
                            }

                            if (temp.size() == 1) {
                                int val = temp.iterator().next();
                                grid[i][j].setGuessValue(val);
                                rows.get(i).add(val);
                                cols.get(j).add(val);
                                boxes.get(getBox(i, j)).add(val);
                                changesConstraints.add(new ArrayList<>(Arrays.asList(i, j, val)));
                                improve++;
                            } else if (temp.size() == 0) {
                                // Dead end, guess was incorrect
                                reset(changesPossibles, changesConstraints);
                                break cycle;
                            }
                        }
                    }
                }
                if (improve == 0) {
                    if (bestGuess[0] != -1) {
                        //printGrid(grid);
                        guess(bestGuess);
                        if (!grid[0][0].getSolved())
                            reset(changesPossibles, changesConstraints);
                        //printGrid(grid);
                        break;
                    }
                    else {
                        //System.out.print("SOLVED");
                        // printGrid(grid);
                        grid[0][0].setSolved();
                        for (int i = 0; i < 9; i++){
                            for (int j = 0; j < 9; j++){
                                if (grid[i][j].getValue() == 0)
                                    grid[i][j].setValue(grid[i][j].getGuessValue());
                            }
                        }
                        break finish;
                    }
                }
            }
        }
    }

    private void solve(){
        while (true) {
            int improve = 0;
            int[] bestGuess = {-1,-1,9};
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    HashSet<Integer> temp = new HashSet<>();
                    if (!grid[i][j].getSet()) {
                        temp = possibles(i, j);
                        grid[i][j].setPossible(temp);
                        if (temp.size() < bestGuess[2]){
                            bestGuess[2] = temp.size();
                            bestGuess[1] = j;
                            bestGuess[0] = i;
                        }
                    }
                    if (temp.size() == 1){
                        int val = temp.iterator().next();
                        grid[i][j].setValue(val);
                        rows.get(i).add(val);
                        cols.get(j).add(val);
                        boxes.get(getBox(i,j)).add(val);
                        grid[i][j].setPossible(null);
                        improve++;
                    }
                }
            }
            if (improve == 0){
                if (bestGuess[0] != -1){
                    guess(bestGuess);
                }
                break;
            }
        }
    }

    public Cell[][] answer(){
        return grid;
    }

}
