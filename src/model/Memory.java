package model;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * This class uses a TreeMap for the value Process and key Slot, which will be ordered by
 * the position of the slot
 * @author Enrique Abma Romero
 */
public class Memory{

	private final int size = 2000;
	public TreeMap<Slot, Process> memory;
	private int nextSlotPos = 0;

	/**
	 * Principal constructor of the class
	 */
	public Memory() {
		memory = new TreeMap<>();
		memory.put(new Slot(size,0),null);
	}

	/**
	 * Constructor that uses a deep copy of the memory given
	 * @param m memory which will be copied
	 */
	public Memory(Memory m){
		memory = new TreeMap<>();
		for (Map.Entry<Slot,Process> pairEntry: m.getMemory().entrySet() ){
			memory.put(new Slot(pairEntry.getKey()),pairEntry.getValue() == null ? null : new Process(pairEntry.getValue()));
		}
		this.nextSlotPos = m.nextSlotPos;
	}

	/**
	 * Getter of memory
	 * @return the value of memory
	 */
	public TreeMap<Slot, Process> getMemory(){
		return new TreeMap<>(memory);
	}

	/**
	 * Getter of nextSlotPos
	 * @return the value of nextSlotPos
	 */
	public int getNextSlotPos(){
		return nextSlotPos;
	}

	/**
	 * Method that removes a process contained in the memory
	 * @param process which will be searched for
	 * @throws ProcessNotFound if the process is not found
	 */
	public void removeProcess(Process process) throws ProcessNotFound{

		Slot slot2Remove = null;
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if (pairEntry.getValue()!=null && Objects.equals(pairEntry.getValue().getName(), process.getName())) {
				slot2Remove = pairEntry.getKey();
				break;
			}
		}

		if (slot2Remove != null && memory.containsKey(slot2Remove)) {
			memory.remove(slot2Remove);
			memory.put(slot2Remove, null);
			process.setSlot(null);
		}

		else
			throw new ProcessNotFound(process);
	}

	/**
	 * Method that checks if a slot is associated to a Process
	 * @param slot which be analyzed in the map
	 * @return {@code true} if it is contained , {@code false} if it's not
	 */
	public boolean slotIsEmpty(Slot slot){
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if ( pairEntry.getKey().equals(slot) && ( pairEntry.getValue()==null)){
				return true;
			}
		}

		return false;
	}

	/**
	 * This method checks if the Process in param exists inside the memory
	 * @param process that we are looking for
	 * @return {@code true} if the memory has the process, {@code false} if not
	 */
	public boolean containsProcess(Process process){
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if (pairEntry.getValue() != null && Objects.equals(pairEntry.getValue().getName(), process.getName())) {
				return true;
			}
		}

		return false;
	}

	public Slot getSlot(Map.Entry<Slot,Process> pairEntry){
		return pairEntry.getKey();
	}

	/**
	 * Gives the position of a slot if it matches the size in param
	 * @param space size of the slot we are searching for
	 * @return the position of the slot contained in the array of slots
	 */
	public int getPos(int space) {
		int i = 0;
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if (pairEntry.getKey().getSpace() == space && slotIsEmpty(getSlot(pairEntry)))
			{
				return i;
			}
			i++;
		}

		return 0;
	}

	/**
	 * This method sets a process into the slot it has assigned
	 * @param x position of the slot
	 * @param p process that is going to be set
	 * @return boolean value depending if was set
	 */
	public boolean setInSlot(int x, Process p){
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if (pairEntry.getKey().getX()==x){
				Slot s1 = new Slot(p.getMemory(),pairEntry.getKey().getX());
				memory.put(s1,p);
				pushSlots(x+1);
				memory.remove(pairEntry.getKey(),null);
				if(pairEntry.getKey().getSpace()-p.getMemory()!=0) {
					memory.put(new Slot(pairEntry.getKey().getSpace() - p.getMemory(), x + 1), null);
					//System.out.println(p.getName()+" "+(pairEntry.getKey().getSpace() - p.getMemory())+ " "+ ( p.getMemory()-pairEntry.getKey().getSpace()));
				}

				if(pairEntry.getKey().getSpace()-p.getMemory()==0)
					pullSlots(x+1);

				return true;
			}
		}
		return false;
	}

	/**
	 * Gives a String representing the slots that are formed in the memory
	 * @return a concatenation of all the slots
	 */
	public String showMemory(){

		StringBuilder s = new StringBuilder();
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if (pairEntry.getValue() != null) {
				s.append(String.valueOf((pairEntry.getValue().getColor()).getRed())).append(",")
						.append(String.valueOf((pairEntry.getValue().getColor()).getGreen())).append(",")
						.append(String.valueOf((pairEntry.getValue().getColor()).getBlue())).append(" ").
						append(pairEntry.getKey().getSpace());
			}
			else {
				s.append(String.valueOf(Color.white.getRed())).append(",").append(String.valueOf(Color.white.getGreen())).append(",").
						append(String.valueOf(Color.white.getBlue())).append(" ").append(pairEntry.getKey().getSpace());
			}

			s.append("/");
		}
		if(s.length() > 0){
			s.deleteCharAt(s.length() - 1);
		}

		return ""+s;
	}

	public boolean isSpace(int space) {

		boolean isSpace = false;

		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			if (slotIsEmpty( pairEntry.getKey())) {
				if ( pairEntry.getKey().getSpace()>=space)
					isSpace = true;
			}
		}

		return isSpace;
	}

	/**
	 * Finds the slot that fits the best for a process given, following the bestSlot algorithm
	 * @param process that will try to be added into memory
	 * @return {@code true} if assigned, {@code false} if not
	 */
	public boolean bestSlot(@NotNull Process process) {

		int i = 0;
		int position=0;
		boolean allowed = false;
		Slot oldSlot = new Slot(size,i);
		Slot bestSlot = new Slot(size+1,i);

		// if there's no more space we will return false
		if(!isSpace(process.getMemory()) || containsProcess(process)) {
			return false;
		}

		// for every Pair of key,value we will search for the optimal slot
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {

			// if we find a slot with the same value as the space we want to divide, and it is empty then
			if (pairEntry.getValue()==null){
				// if it's the same amount of space we will assign the process to this slot
				if (( pairEntry.getKey()).getSpace() == process.getMemory()){
					process.setSlot(pairEntry.getKey());
					process.setPosition(pairEntry.getKey().getX());
					memory.put((pairEntry.getKey()),process);

					return true;
				}
				// else if the space in this slot is bigger then...
				else if ((pairEntry.getKey()).getSpace() > process.getMemory()){
					// if the auxiliary Slot size is still bigger, then replace for this one
					if(bestSlot.getSpace() > (pairEntry.getKey()).getSpace()){
						allowed = true;
						oldSlot=pairEntry.getKey();
						position = (pairEntry.getKey()).getX();

						bestSlot.setSpace(process.getMemory());
						bestSlot.setX(i);
						//memory.remove(((Slot) pairEntry.getKey()));

					}
				}
			}

			i++;
		}

		if (allowed){
			pushSlots(position+1);
			// we remove the slot that will be replaced by the subtraction of the new space
			memory.remove(oldSlot,null);
			oldSlot.setSpace(oldSlot.getSpace()-process.getMemory());
			oldSlot.setX(position+1);
			memory.put(oldSlot,null);
			process.setSlot(bestSlot);
			memory.put(bestSlot,process);
			return true;
		}

		return false;

	}

	/**
	 * The function for this method is to push the pair Key and Value one the position
	 * @param position in which we will start to push
	 */
	public void pushSlots(int position){

		// both arrays will contain the new pushed keys with their value
		ArrayList<Slot> slots = new ArrayList<>();
		ArrayList<Process> processes = new ArrayList<>();

		// we add to this list the slots that will be replaced
		ArrayList<Slot> slots2remove = new ArrayList<>();

		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {

			// we will be pushing from this position
			if (( pairEntry.getKey()).getX()==position) {
				slots2remove.add(pairEntry.getKey());
				slots.add(new Slot ((pairEntry.getKey()).getSpace(),( pairEntry.getKey()).getX()+1));
				processes.add(pairEntry.getValue());
			}

			// and forward the position given
			if (( pairEntry.getKey()).getX()>position) {
				slots2remove.add(pairEntry.getKey());
				slots.add(new Slot (( pairEntry.getKey()).getSpace(),( pairEntry.getKey()).getX()+1));
				processes.add(pairEntry.getValue());

			}
		}

		for (Slot slot : slots2remove) memory.remove(slot);

		for (int i = 0; i < slots.size(); i++)
			memory.put(slots.get(i),processes.get(i));

	}

	/**
	 * This method does the opposite to pushSlots, it updates the values from the position given to X-1
	 * @param position from which we will start downgrading the value of X for every slot that is after
	 */
	public void pullSlots(int position){

		ArrayList<Slot> slots = new ArrayList<>();
		ArrayList<Process> processes = new ArrayList<>();
		Slot slot2remove = new Slot(0,-1);
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {

			if (( pairEntry.getKey()).getX()>position) {
				slots.add(new Slot (( pairEntry.getKey()).getSpace(),( pairEntry.getKey()).getX()-1));
				processes.add(pairEntry.getValue());

			}
		}
		memory.remove(slot2remove);

		for (int i = 0; i < slots.size(); i++){
			memory.put(slots.get(i),processes.get(i));
		}

		for (int i = 0; i < slots.size(); i++){
			memory.remove(new Slot(slots.get(i).getSpace(),slots.get(i).getX()+1),processes.get(i));
		}
	}

	/**
	 * Finds the slot that fits with the size of the process, but in this case, the chosen slot depends on
	 * the last slot assigned to a process
	 * @param process that will be
	 * @return {@code true} if assigned, {@code false} if not
	 */
	public boolean nextSlot(Process process){
		int i = 0;
		Slot oldSlot = new Slot(size,i);
		Slot newSlot = new Slot(size+1,i);
		boolean allowed = false;

		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet() ) {
			// if we find a slot that matches the position we are searching for or greater then
			if (pairEntry.getKey().getX() >= nextSlotPos && pairEntry.getValue()==null){

				// if the space in slot is equal to the space we are searching for then
				if (pairEntry.getKey().getSpace() == process.getMemory()){
					memory.put( pairEntry.getKey(),process);
					nextSlotPos=pairEntry.getKey().getX()+1;
					return true;
				}
				// else if the space in this slot is bigger, then we will divide it into two slots
				if ( pairEntry.getKey().getSpace() > process.getMemory()){
					oldSlot=pairEntry.getKey();

					newSlot.setSpace(process.getMemory());
					newSlot.setX(i);
					// since we divided into two, the next slot will jump + 2 positions
					nextSlotPos=pairEntry.getKey().getX()+1;

					allowed = true;
					//position = (pairEntry.getKey()).getX();
					break;

				}

			}

			i++;
		}

		if (!allowed){

			nextSlotPos=0;
			for (Map.Entry<Slot, Process> pairEntry : memory.entrySet()) {
				// if we find a slot that matches the position we are searching for or greater then
				if (pairEntry.getKey().getX() >= nextSlotPos && pairEntry.getValue() == null) {

					// if the space in slot is equal to the space we are searching for then
					if (pairEntry.getKey().getSpace() == process.getMemory()) {
						memory.put(pairEntry.getKey(), process);
						nextSlotPos = pairEntry.getKey().getX() + 1;
						return true;
					}
					// else if the space in this slot is bigger, then we will divide it into two slots
					if (pairEntry.getKey().getSpace() > process.getMemory()) {
						oldSlot = pairEntry.getKey();


						newSlot.setSpace(process.getMemory());
						newSlot.setX(i);
						// since we divided into two, the next slot will jump + 1 positions
						nextSlotPos = pairEntry.getKey().getX() + 1;

						allowed = true;
						//position = (pairEntry.getKey()).getX();
						break;

					}

				}

				i++;
			}
		}

		if (allowed){
			pushSlots(oldSlot.getX()+1);
			// we remove the slot that will be replaced by the subtraction of the new space
			memory.remove(oldSlot,null);
			oldSlot.setSpace(oldSlot.getSpace()-process.getMemory());
			oldSlot.setX(oldSlot.getX()+1);
			memory.put(oldSlot,null);
			memory.put(newSlot,process);
		}
		// since we reached the last slot in memory, we will reset the next slot position
		else
			nextSlotPos=0;

		return allowed;
	}

	public void setNextSlotPos(int i){
		nextSlotPos=i;
	}

	/**
	 * If there are two slots next to each other and are empty, this slots will be joined into a single one
	 * using pull method if necessary
	 */
	public void joinEmptySlots(){

		Slot removeSlot = new Slot(size+1,-2);
		int memSize = memory.size();
		boolean found = false;
		int position = 0;
		Slot lastSlot = new Slot(size+1,-2);

		for (int i = 0; i < memSize;i++) {
			for (Map.Entry<Slot, Process> pairEntry : memory.entrySet()) {
				// if the last slot was before this one, and both null Processes then we will change its size
				if (pairEntry.getKey().getX() - lastSlot.getX() == 1 && pairEntry.getValue() == null) {
					found = true;
					removeSlot = pairEntry.getKey();
					position = lastSlot.getX();
					break;
				}
				// if this slot's Process is null then we will store it in case the next one is null as well
				if (pairEntry.getValue() == null) {
					lastSlot = pairEntry.getKey();
				}

			}

			if (found){
				memory.remove(lastSlot);
				memory.remove(removeSlot);
				lastSlot.setSpace(lastSlot.getSpace()+removeSlot.getSpace());
				memory.put(lastSlot,null);
				pullSlots(position);
				found=false;
			}
		}
	}

	/**
	 * @return a String concatenation with the values of every slot with his value assigned
	 */
	public String toString() {
		StringBuilder s = new StringBuilder("[");
		for (Map.Entry<Slot,Process> pairEntry: memory.entrySet()) {
			s.append(" (").append(pairEntry.getKey());

			if ( pairEntry.getValue() != null)
				s.append("|").append(pairEntry.getValue().showName());

			s.append(") ");
		}
		return s + "]";

	}

}
