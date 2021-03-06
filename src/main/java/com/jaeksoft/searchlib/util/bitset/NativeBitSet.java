/**
 * License Agreement for OpenSearchServer
 * 
 * Copyright (C) 2014 Emmanuel Keller / Jaeksoft
 * 
 * http://www.open-search-server.com
 * 
 * This file is part of OpenSearchServer.
 * 
 * OpenSearchServer is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * OpenSearchServer is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OpenSearchServer. If not, see <http://www.gnu.org/licenses/>.
 **/

package com.jaeksoft.searchlib.util.bitset;

public class NativeBitSet implements BitSetInterface {

	private long bitSetRef;

	final private long[] buffer;

	private int bufferSize;

	private NativeBitSet() {
		buffer = new long[6144];
		bufferSize = 0;
	}

	public NativeBitSet(final long numbits) {
		this();
		bitSetRef = init(numbits);
	}

	final private native long init(final long numbits);

	final private native void free(final long ref);

	@Override
	protected void finalize() {
		free(bitSetRef);
		bitSetRef = 0;
	}

	final private void flushBuffer() {
		set(bitSetRef, buffer, bufferSize);
		bufferSize = 0;
	}

	final private native long size(final long ref);

	@Override
	public long size() {
		if (bufferSize > 0)
			flushBuffer();
		return size(bitSetRef);
	}

	@Override
	final public void set(final long bit) {
		if (bufferSize == buffer.length)
			flushBuffer();
		buffer[bufferSize++] = bit;
	}

	final private native boolean get(final long ref, final long bit);

	@Override
	public boolean get(final long bit) {
		if (bufferSize > 0)
			flushBuffer();
		return get(bitSetRef, bit);
	}

	final private native long clone(final long ref);

	@Override
	public BitSetInterface clone() {
		if (bufferSize > 0)
			flushBuffer();
		NativeBitSet bitSet = new NativeBitSet();
		bitSet.bitSetRef = clone(bitSetRef);
		return bitSet;
	}

	final private native void set(final long ref, final int[] bits,
			final int length);

	@Override
	public void set(final int[] bits) {
		set(bitSetRef, bits, bits.length);
	}

	final private native void set(final long ref, final long[] buffer,
			final int length);

	@Override
	public void set(final long[] bits) {
		set(bitSetRef, bits, bits.length);
	}

	final private native long cardinality(final long ref);

	@Override
	public long cardinality() {
		if (bufferSize > 0)
			flushBuffer();
		return cardinality(bitSetRef);
	}

	final private native void flip(final long ref, final long startPos,
			final long endPos);

	@Override
	public void flip(final long startPos, final long endPos) {
		if (bufferSize > 0)
			flushBuffer();
		flip(bitSetRef, startPos, endPos);
	}

	final private native void and(final long ref, final long ref2);

	@Override
	final public void and(BitSetInterface bitSet) {
		if (bufferSize > 0)
			flushBuffer();
		and(bitSetRef, ((NativeBitSet) bitSet).bitSetRef);
	}

	final private native void or(final long ref, final long ref2);

	@Override
	final public void or(BitSetInterface bitSet) {
		if (bufferSize > 0)
			flushBuffer();
		or(bitSetRef, ((NativeBitSet) bitSet).bitSetRef);
	}

	final private native void clear(final long ref, final long bit);

	@Override
	public void clear(final long bit) {
		clear(bitSetRef, bit);
	}

	final native long nextSetBit(final long ref, final long index);

	@Override
	public long nextSetBit(final long index) {
		if (bufferSize > 0)
			flushBuffer();
		return nextSetBit(bitSetRef, index);
	}

}
