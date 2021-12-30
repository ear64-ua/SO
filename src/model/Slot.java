package model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Slot implements Comparable<Slot>{

	private int space;

	private int x;

	public Slot(int space, int x) {
		this.space=space;
		this.x=x;
	}

	public Slot(@NotNull Slot s){
		this.space = s.space;
		this.x = s.x;
	}

	/**
	 * returns whether is occupied by a Process
	 * @return
	 */
	
	public int getSpace() {
		return space;
	}

	/**
	 * @return the value contained in x, it's position in memory
	 */
	public int getX(){
		return x;
	}

	public void setSpace(int space){
		this.space=space;
	}

	public void setX(int x){
		this.x=x;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Slot slot = (Slot) o;
		return x == slot.x;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x,space);
	}

	public String toString(){
		return " "+ x +": "+ space + " ";
	}

	@Override
	public int compareTo(Slot o) {

		if (this.x < o.getX())
			return -1;

		else if (this.x > o.getX())
			return 1;

		else if (this.x == o.getX() && this.space < o.getSpace())
			return -1;
		else if (this.x == o.getX() && this.space > o.getSpace())
			return 1;
		else
			return 0;
	}
}
