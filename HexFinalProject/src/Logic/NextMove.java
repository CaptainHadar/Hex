package Logic;

public class NextMove {

	private Position pos;
	private int result;
	
	public NextMove(Position pos, int result) {
		this.pos = pos;
		this.result = result;
	}
	
	public NextMove(NextMove nm) {
		this.pos = nm.pos;
		this.result = nm.result;
	}
	
	public Position getPosition() {
		return this.pos;
	}
	
	public int getResult() {
		return this.result;
	}

	
	public String toString() {
		return(pos + ", " + this.result);
	}
}

