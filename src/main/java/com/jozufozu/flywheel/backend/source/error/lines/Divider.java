package com.jozufozu.flywheel.backend.source.error.lines;

public enum Divider {
	BAR(" | "),
	ARROW("-->"),
	;

	private final String s;

	Divider(String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}
}
