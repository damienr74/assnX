/*
 * Author: Damien Robichaud
 * File: Sudoku.java
 * Date: December 1
 * Desciption: Sudoku Solves sudoku puzzles
 * Course: CSCI1110
 */

import java.util.Scanner;
import java.util.ArrayList;

public class Sudoku {
	/*
	 * create three new arrays to store 9 ints.
	 * These ints will be used for bit masking
	 * and flag setting
	 */
	static int[] cols = new int[9];
	static int[] rows = new int[9];
	static int[][] quad = new int[3][3];
		// Main function of Sudoku class
	public static void main( String [] arvs ) {
		Scanner s = new Scanner(System.in);
		// create 9x9 grid
		int [][] grid = new int[9][9];
		// fill list with ints from input
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				grid[j][i] = s.nextInt();
			}
		}
		//call calcPossible function
		calcPossible(grid);
		// solve the grid
		solve(grid);

		/**
		 * output for solved sudoku :)
		 */
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;) {
				System.out.print(grid[j][i]);
				// if there is another number on the line,
				// print " "
				if(++j < 9) System.out.print(" ");
			}
			System.out.println(); // print nl char.
		}
	}

	// boolean function solve. takes in a 2d-array
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

	//Checks if grid is invalid. Used for backtracking.
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

	/*
	 * when it is complete, there should not be any 0's
	 * the bit logic takes care of the rest.
	 */
	public static boolean isComplete(int[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] == 0) return false;
			}
		}
		return true;
	}

	// sets the integer flags in the three arrays
	public static void calcPossible(int[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(grid[j][i] != 0) {
					/*
					 * stores a comparason flag, if this value exists
					 * in the row colomn or quadrant, set the bit
					 * representing that value to 1. (meaning others
					 * cannot take that value.)
					 * flag 0 represents 1, 1 -> 2 and so forth.
					 * 1 << 0 == 0b01; 1 << 1 == 0b10;
					 */
					int flag = (1 << (grid[j][i] - 1));
					/*
					 * reasign cols[j] to cols[j] or flag
					 * i.e. 0b000000000 | 0b000000001 = 0b000000001
					 * 0b001000100 | 0b000001000 = 0b001001100
					 */
					cols[j] = (cols[j] | flag);
					// reassing rows[j] to rows or flag
					rows[i] = (rows[i] | flag);
					// reassing quad[j/3][i/3] to quad'' or flag
					quad[j/3][i/3] = (quad[j/3][i/3] | flag);
				}
			}
		}
	}

	/*
	 * bestBlank function. returns Options object. this objec
	 * Contains all the information to locate the point, what
	 * values are possible and option count. function chooses
	 * position with the least options available.
	 */
	public static Options bestBlank(int[][] grid) {
		Options best = null;
		// go through every grid element to find the element
		// with the least amount options
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				// if element is zero, call countOpt(location)
				if(grid[j][i] == 0) {
					// assing function call to tmp
					Options tmp = countOpt(j,i);
					// if tmp is the lowes thus far, set best to tmp
					if(best == null || best.count > tmp.count) {
						best = tmp;
					}
				}
			}
		}
		return best;
	}

	/*
	 * countOpt function returns Options object with location
	 * (column, row) in the grid. This object contains the
	 * options list, the count and location in the grid.
	 */
	public static Options countOpt(int column, int row) {
		Options tmp = new Options(column, row);
		// for all nine possible options,
		for(int i = 0; i < 9; i++) {
			/*
			 * if the flags set for rows[row] cols[column] and
			 * grid[column/3][row/3] are set to zero, increase
			 * count and add i to options list. (it's a valid option)
			 */
			if(((rows[row] & (1 << i)) == 0) &&
				((cols[column] & (1 << i)) == 0) &&
					 ((quad[column/3][row/3] & (1 << i)) == 0)) {
						tmp.count++;
						tmp.options.add(i);
			}
		}
		return tmp;
	}

	// Options static class.
	static class Options {
		// has x and y ints for location
		int x;
		int y;
		// has a list of valid options for said location.
		ArrayList<Integer> options;
		// has a count, representing the number of options
		int count;
		public Options(int column, int row) {
			this.options = new ArrayList<Integer>();
			this.count = 0;
			this.x = column;
			this.y = row;
		}
	}
}
