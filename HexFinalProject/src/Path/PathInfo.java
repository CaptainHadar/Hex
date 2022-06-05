package Path;

import Logic.Position;

public class PathInfo {

	private Position pos;
	private int movementCost;
	
	public PathInfo(Position pos, int movementCost) {
		this.pos = pos;
		this.movementCost = movementCost;
	}
	
	public Position getPosition() {
		return this.pos;
	}
	
	public int getMovementCost() {
		return this.movementCost;
	}	
}
