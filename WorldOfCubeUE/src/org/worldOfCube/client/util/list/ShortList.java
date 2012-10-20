/*
 * Copyright (c) 2012 matheusdev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.worldOfCube.client.util.list;

import java.util.Arrays;

public class ShortList {

	private short[] data;
	private int size;

	public ShortList(int initialCapacity) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("IllegalCapacity: " + initialCapacity);
		}
		data = new short[initialCapacity];
	}

	public ShortList() {
		this(10);
	}

	public void ensureCapacity(int minCapacity) {
		int oldCapacity = data.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3)/2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			data = Arrays.copyOf(data, newCapacity);
		}
	}

	public boolean contains(short s) {
		return indexOf(s) >= 0;
	}

	public void set(int index, short s) {
		rangeCheck(index);
		data[index] = s;
	}

	public void add(short s) {
		ensureCapacity(size + 1);
		data[size++] = s;
	}

	public boolean remove(short s) {
		return remove(indexOf(s));
	}

	public boolean remove(int index) {
		if (index >= 0) {
			rangeCheck(index);
			int numMoved = size - index - 1;
			if (numMoved > 0) {
				System.arraycopy(data, index+1, data, index, numMoved);
			}
			return true;
		}
		return false;
	}

	public int indexOf(short s) {
		for (int i = 0; i < size; i++) {
			if (data[i] == s) {
				return i;
			}
		}
		return -1;
	}

	public short get(int index) {
		rangeCheck(index);
		return data[index];
	}

	public void rangeCheck(int index) {
		if (index >= size || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
	}

	public int capacity() {
		return data.length;
	}

	public int size() {
		return size;
	}

	public short[] toArray() {
		return Arrays.copyOf(data, size);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ShortList (size: " + size + ", capacity: " + capacity() + ") data: ");
		for (int i = 0; i < size; i++) {
			sb.append(" [" + data[i] + "]");
		}
		return sb.toString();
	}

}
