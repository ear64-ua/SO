package model;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * Class that represents a slot with the space it has and the location in memory
 * @author Enrique Abma Romero
 */
public class Slot implements Comparable<Slot>{

	private int space;

	private int x;

	/***
	 * Constructor of the class
	 * @param space that will be assigned
	 * @param x position in memory
	 */
	public Slot(int space, int x) {
		this.space=space;
		this.x=x;
	}

	/**
	 * Constructor that uses deep copy
	 * @param s slot that will be copied
	 */
	public Slot(@NotNull Slot s){
		this.space = s.space;
		this.x = s.x;
	}

	/**
	 * Getter of space
	 * @return the amount of space it has
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

	/**
	 * Setter of space
	 * @param space that will be assigned
	 */
	public void setSpace(int space){
		this.space=space;
	}

	/**
	 * Setter of the position
	 * @param x position that will be updated
	 */
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

	@Override
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
