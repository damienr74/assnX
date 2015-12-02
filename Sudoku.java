/*
 * Author: Damien Robichaud
 * File: Sudoku.java
 * Date: December 1
 * Desciption: Sudoku add description
 * Course: CSCI1110
 */

import java.util.Scanner;
import java.util.ArrayList;

public class Sudoku {
	static int[] cols = new int[9];
	static int[] rows = new int[9];
	static int[][] quad = new int[3][3];
		// Main function of Sudoku class
	public static void main( String [] arvs ) {
		//
		Scanner s = new Scanner(System.in);
		int [][] grid = new int[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				grid[j][i] = s.nextInt();
			}
		}
		calcPossible(grid);

		solve(grid);

		/**
		 * output for solved sudoku :)
		 */
		for(int i = 0; i < 9;) {
			for(int j = 0; j < 9;) {
				System.out.print(grid[j][i]);
				if(++j < 9) System.out.print(" ");
				if(j % 3 == 0) System.out.print(" ");
			}
			System.out.println();
			if((++i % 3 == 0) && i != 9) System.out.println();
		}
	}

	public static boolean solve(int[][] grid) {
		//if(invalid(grid)) {
			return false;
		//} else if (isComplete(grid)) {
			return true;
		//}
		Options best = bestBlank(grid);
	}

	public static void calcPossible(int[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] != 0) {
					int flag = (1 << (grid[j][i] - 1));
					cols[j] = (cols[j] | flag);
					rows[i] = (rows[i] | flag);
					quad[j/3][i/3] = (quad[j/3][i/3] | flag);
				}
			}
		}
	}

	public static Options bestBlank(int[][] grid) {
		Options best;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] == 0) {
					Options tmp = countOpt(j,i);
					if(best == null || best.count > tmp.count) {
						best = tmp;
						best.x = j;
						best.y = i;
					}
				}
			}
		}
		return best;
	}

	public static Options countOpt(int column, int row) {
		Options tmp = new Options(column, row);
		for(int i = 0; i < 9; i++) {
			if((rows[row] & (1 << i)) != 0) {
				tmp.count++;
				tmp.options.add(i);
			} else if((cols[column] & (1 << i)) != 0) {
				tmp.count++;
				tmp.options.add(i);
			} else if((quad[column/3][row/3] & (1 << i)) != 0) {
				tmp.count++;
				tmp.options.add(i);
			}
		}
		return tmp;
	}

	class Options {
		int x;
		int y;
		ArrayList<Integer> options;
		int count;
		public Options(int column, int row) {
			this.options = new ArrayList<Integer>();
			this.count = 0;
			this.x = column;
			this.y = row;
		}
	}
}
