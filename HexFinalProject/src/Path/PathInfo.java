package Path;

import Logic.Position;

public class PathInfo {

	private Position pos;
	private int movementCost;
	// בנאי
	public PathInfo(Position pos, int movementCost) {
		this.pos = pos;
		this.movementCost = movementCost;
	}
	// מחזיר מיקום של המשתנה
	public Position getPosition() {
		return this.pos;
	}
	// מחזיר את המחיר של המשתנה
	public int getMovementCost() {
		return this.movementCost;
	}	
}
