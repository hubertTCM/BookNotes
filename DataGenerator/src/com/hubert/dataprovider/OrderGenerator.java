package com.hubert.dataprovider;

public class OrderGenerator {
	private int mCurrent = 0;

	public int nextOrder() {
		mCurrent += 1;
		return mCurrent;
	}
}
