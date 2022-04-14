package com.directional;

/**
 * Simple 2 axis object
 * @author viney
 *
 */
public class Vec2 {
	private int x;
	private int y;
	
	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get x-y coordinate of this Vec2
	 * @return {x, y}
	 */
	public int[] getOrigin() {
		return new int[] {x, y};
	}
	public int getX() { return x; }
	public int getY() { return y; }
	
	/**
	 * Evaluate if this Vec2 contains the same coordinates as the passed Vec2
	 * @param o - Vec2
	 * @return - true if this.x and this.y equal o.x and o.y
	 */
	public boolean equals(Vec2 o) {
		return x == o.x && y == o.y;
	}
	
	/**
	 * Perform an addition operation from one Vec2 to another.
	 * Passes the result into a new Vec2
	 * @param o - Vec2
	 * @return - Vec2
	 */
	public Vec2 add(Vec2 o) {
		return new Vec2(this.x + o.x, this.y + o.y);
	}
	
	/**
	 * Perform a subtraction operation from one Vec2 to another.
	 * Passes the result into a new Vec2
	 * @param o - Vec2
	 * @return - Vec2
	 */
	public Vec2 subtract(Vec2 o) {
		return new Vec2(this.x - o.x, this.y - o.y);
	}
	
	/**
	 * Evaluate the direction of traversal from this Vec2 to another.
	 * 
	 * @param o - Vec2
	 * @return - Enum
	 * @see com.directional.Direction
	 */
	public Direction directionTo(Vec2 o) {
		Vec2 r = this.subtract(o);
		if(r.x == 0 && r.y == 0) return Direction.SAME;
		if(r.x == 0) {
			switch(r.y >>> 31) {
			case 0:
				return Direction.LEFT;
			case 1:
				return Direction.RIGHT;
			}
		}
		if(r.y == 0) {
			switch(r.x >>> 31) {
			case 0:
				return Direction.UP;
			case 1:
				return Direction.DOWN;
			}
		} return Direction.TANGEANT;
	}
	
	/**
	 * Instantiate a new Vec2 of a single integer unit
	 * derived from the parameter.
	 * @param d - Direction
	 * @return - Vec2
	 */
	public static Vec2 fromDirection(Direction d) {
		switch(d) {
		case UP:
			return new Vec2(0, 1);
		case DOWN:
			return new Vec2(0, -1);
		case LEFT:
			return new Vec2(-1, 0);
		case RIGHT:
			return new Vec2(1, 0);
		case SAME:
			return new Vec2(0, 0);
		} return null;
	}
	
}
