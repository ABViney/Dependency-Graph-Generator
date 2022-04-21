package com.directional;

/**
 * Key descriptions of axis traversal.
 * 
 * @author viney
 *
 */
public enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT,
	SAME,
	TANGEANT;
	
	public static Direction oppositeOf(Direction o) {
		switch(o) {
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case SAME:
			return TANGEANT;
		case TANGEANT:
			return SAME;
		} return null;
	}
	
//	public static String toString(Direction d) {
//		switch(d) {
//		case UP:
//			return "UP";
//		case DOWN:
//			return "DOWN";
//		case LEFT:
//			return "RIGHT";
//		case RIGHT:
//			return "LEFT";
//		case SAME:
//			return "SAME";
//		case TANGEANT:
//			return "TANGEANT";
//		} return null;
//	}
}
