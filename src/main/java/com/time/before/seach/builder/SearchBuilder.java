package com.time.before.seach.builder;

public class SearchBuilder {

	public SearchBuilder() {
	}

	public String[] indices;
	public String[] type;
	public int size;

	public SearchBuilder setIndex(String... indices) {

		if (indices == null) {
			throw new IllegalArgumentException("indices must not be null");
		} else {
			for (int i = 0; i < indices.length; i++) {
				if (indices[i] == null) {
					throw new IllegalArgumentException("indices[" + i + "] must not be null");
				}
			}
		}
		this.indices = indices;
		return this;
	}

	public SearchBuilder setType(String... type) {
		
		if (type == null) {
			throw new IllegalArgumentException("indices must not be null");
		} else {
			for (int i = 0; i < indices.length; i++) {
				if (type[i] == null) {
					throw new IllegalArgumentException("indices[" + i + "] must not be null");
				}
			}
		}
		this.type = type;
		return this;
	}
	
	public SearchBuilder setSize(int size){
		
		this.size = size;
		return this;
	}

}
