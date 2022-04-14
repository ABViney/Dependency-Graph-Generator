package Tester;

public class IForgotHowArraysWorkTester {
	public static void main(String[] args) {
		/**
		 * Disregard this tester. Used to test and remember syntax stuff
		 */
		int[][] xy = new int[5][4];
		xy[0][0] = 1;
		xy[1][1] = 1;
		xy[2][2] = 1;
		xy[3][3] = 1;
		xy[4][3] = 1;
		
		for(int y = 0; y < xy.length; y++) {
			for(int x = 0; x < xy[0].length; x++) {
				if(xy[y][x] == 1) System.out.print(" X ");
				else System.out.print("   ");
			} System.out.println();
		}
		
		System.out.println("-1 XOR 1 : " + (-1 ^ 0));
	}
}
