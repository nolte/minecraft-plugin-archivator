package de.noltarium.minecraft.utils;

import java.util.Arrays;

public enum ArchiveType {
	TAR("tar.gz"), ZIP("zip");

	private final String extention;

	ArchiveType(String extention) {
		this.extention = extention;
	}

	public String value() {
		return this.name().toLowerCase();
	}

	public static ArchiveType fromString(String s) throws IllegalArgumentException {
		return Arrays.stream(ArchiveType.values()).filter(v -> v.name().equalsIgnoreCase(s)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
	}

	public String getExtention() {
		return extention;
	}

}