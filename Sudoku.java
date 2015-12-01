/*
 * Author: Damien Robichaud
 * File: Sudoku.java
 * Date: Insert Date
 * Desciption: Sudoku add description
 * Course: CSCI1110
 */

import java.util.Scanner;

public class Sudoku {
	static int[] cols = new int[9];
	static int[] rows = new int[9];
	static int[] quad = new int[9];
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
		for(int i = 0; i < 9;) {
			for(int j = 0; j < 9;) {
				System.out.print(grid[j][i]);
				if(++j < 9) System.out.print(" ");
				if(j % 3 == 0) System.out.print(" ");
			}
			System.out.println();
			if(++i % 3 == 0) System.out.println();
		}
		calcPossible();
	}

	public static void calcPossible() {
		
	}
}
