package org.worldOfCube.client.util.list;

/**
 * @author matheusdev
 *
 */
public class OverwriteList<E> {
	
	protected E[] values;
	protected int top = 0;
	
	@SuppressWarnings("unchecked")
	public OverwriteList(int size) {
		values = (E[]) new Object[size];
	}
	
	public void add(E item) {
		increaseTop();
		values[top] = item;
	}
	
	public void remove(int id) {
		
	}
	
	private void increaseTop() {
		top++;
		top %= values.length;
	}
	
	public int indexOf(E item) {
		for (int i = 0; i < values.length; i++) {
			if (values[i] == item) {
				return i;
			}
		}
		return -1;
	}
	
}
