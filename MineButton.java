package mines;

import javafx.scene.control.Button;

public class MineButton extends Button{
	private int i,j;
	public MineButton(String txt, int i, int j) { //saves the text, i, j, and set size for 35x35
		super(txt);
		this.i=i;
		this.j=j;
		this.setPrefSize(35, 35);
	}
	public int iGet() { //return i value of the button
		return i;
	}
	public int jGet() { //return j value of the button
		return j;
	}
}
