package Logic;

public class NextMove {

	private Position pos;
	private int result;
	// בנאי של המשתנה
	public NextMove(Position pos, int result) {
		this.pos = pos;
		this.result = result;
	}
	// בנאי של המשתנה
	public NextMove(NextMove nm) {
		this.pos = nm.pos;
		this.result = nm.result;
	}
	// נותן את מיקום הנקודה
	public Position getPosition() {
		return this.pos;
	}
	// נותן את שווי הנקודה
	public int getResult() {
		return this.result;
	}

	// משמש להדפסת המיקום וערכו
	public String toString() {
		return(pos + ", " + this.result);
	}
}

