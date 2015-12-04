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
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;) {
				System.out.print(grid[j][i]);
				if(++j < 9) System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static boolean solve(int[][] grid) {
		if(invalid(grid)) {
			return false;
		} else if (isComplete(grid)) {
			return true;
		}
		Options best = bestBlank(grid);
		for( Integer option : best.options ) {
			grid[best.x][best.y] = option + 1;
			cols[best.x] = (cols[best.x] | (1 << option));
			rows[best.y] = (rows[best.y] | (1 << option));
			quad[best.x/3][best.y/3] = (quad[best.x/3][best.y/3] | (1 << option));
			if(solve(grid)) {
				return true;
			}
			grid[best.x][best.y] = 0;
			cols[best.x] = (cols[best.x] & ~(1 << option));
			rows[best.y] = (rows[best.y] & ~(1 << option));
			quad[best.x/3][best.y/3] = (quad[best.x/3][best.y/3] & ~(1 << option));
		}
		return false;
	}

	public static boolean invalid(int[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] == 0) {
				/*
				 * check if there are available options to put in this place.
				 * if there are none, return false, if there are, return true.
				 */
					Options tmp = countOpt(j, i);
					if(tmp.count == 0) return true;
				}
			}
		}
		return false;
	}

	public static boolean isComplete(int[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] == 0) return false;
			}
		}
		return true;
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
		Options best = null;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] == 0) {
					Options tmp = countOpt(j,i);
					if(best == null || best.count > tmp.count) {
						best = tmp;
					}
				}
			}
		}
		return best;
	}

	public static Options countOpt(int column, int row) {
		Options tmp = new Options(column, row);
		for(int i = 0; i < 9; i++) {
			if((rows[row] & (1 << i)) == 0) {
				if((cols[column] & (1 << i)) == 0) {
					if((quad[column/3][row/3] & (1 << i)) == 0) {
						tmp.count++;
						tmp.options.add(i);
					}
				}
			}
		}
		return tmp;
	}

	static class Options {
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
