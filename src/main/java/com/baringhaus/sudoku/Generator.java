//This file has been replaced

package com.baringhaus.sudoku;

import java.util.*;
import org.javatuples.Pair;

public class Generator {
    private int BOARD_SIZE = 9;

    public boolean valAlreadyInRow(int[][] grid, int row, int val) {

        for (int i = 0; i < grid.length; i++) {
            if (grid[i][row] == val) {
                return true;
            }
        }
        return false;
    }

    public boolean valAlreadyInCol(int[][] grid, int col, int val) {

        for (int i = 0; i < grid.length; i++) {
            if (grid[col][i] == val) {
                return true;
            }
        }
        return false;
    }

    private boolean valAlreadyInSubgrid(int[][] grid, int col, int row, int val) {

        int subGridSize = (int) Math.sqrt(BOARD_SIZE);
        for (int x = 0; x < subGridSize; x++) {
            for (int y = 0; y < subGridSize; y++) {
                if (grid[col / subGridSize * subGridSize + x][row / subGridSize * subGridSize + y] == val) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSafe(int[][] grid, int col, int row, int num) {

        return (!valAlreadyInCol(grid, col, num)
                && !valAlreadyInRow(grid, row, num)
                && !valAlreadyInSubgrid(grid, col, row, num));
    }

    public int[] unassigned(int[][] grid) {

        List<Pair<Integer, Integer>> empty = new LinkedList<>();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (grid[x][y] == 0) {
                    empty.add(Pair.with(x, y));
                }
            }
        }
        return (empty.isEmpty() ?
                new int[]{-1,-1} :
                new int[]{empty.get(0).getValue0(), empty.get(0).getValue1()});
    }

    public int[] validValuesForPosition(int[][] grid, int col, int row) {
        List<Integer> values = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        int subgridsize = (int)Math.sqrt(BOARD_SIZE);
        int subgridcol =  col/subgridsize;
        int subgridrow =  row/subgridsize;

        for(int i = 0; i < 9; i++) {
            values.removeAll(Collections.singleton(grid[col][i]));
            values.removeAll(Collections.singleton(grid[i][row]));
            values.removeAll(Collections.singleton(grid[subgridcol*subgridsize+(i%3)][subgridrow*subgridsize+i/3]));
        }
        return values.stream().filter(Objects::nonNull).mapToInt(i -> i).toArray();
    }

    public static void shuffleArray(int[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(int[] a, int i, int change) {
        int helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }

    public boolean sudoku(int[][] grid) {
        int[] ra = unassigned(grid);
        if (ra[0] == -1) {
            return true;
        }

        int row = ra[1];
        int col = ra[0];

       int[] randomValues = validValuesForPosition(grid, col, row);

        shuffleArray(randomValues);

        for (int num : randomValues) {
            grid[col][row] = num;
            if (this.sudoku(grid)) {
                return true;
            }
            grid[col][row] = 0;
        }
        return false;
    }

    public int[][] makeSubgrid() {
        int[][] sub = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        List<Integer> values = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(values);

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                sub[x][y]=values.get(0);
                values.remove(0);
            }
        }
        return sub;
    }

    public void copySubgridToGrid(int row, int col, int [][] sub, int [][]grid) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                grid[col*3+i][row*3+j] = sub[i][j];
            }
        }
    }

    public int Asudoku(int[][] grid) {
        int[] ra = unassigned(grid);
        if (ra[0] == -1) {
            return 1;
        }

        int row = ra[1];
        int col = ra[0];

        int[] randomValues = validValuesForPosition(grid, col, row);

        shuffleArray(randomValues);

        int count = 0;

        for (int num : randomValues) {
            grid[col][row] = num;
            count += this.Asudoku(grid);
            grid[col][row] = 0;
        }
        return count;
    }

    public int emptyCount(int[][] grid) {
        int count = 0;
        for(int x = 0; x < BOARD_SIZE; x++) {
            for(int y = 0; y < BOARD_SIZE; y++) {
                if(grid[x][y]==0) {
                    count++;
                }
            }
        }
        return count;
    }
}
