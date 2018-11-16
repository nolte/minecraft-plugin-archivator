package de.noltarium.minecraft.archivator.core.database;

import java.util.Arrays;

public enum SupportedDatabaseType {
	SQLITE;

	public String value() {
		return this.name().toLowerCase();
	}

	public static SupportedDatabaseType fromString(String s) throws IllegalArgumentException {
		return Arrays.stream(SupportedDatabaseType.values()).filter(v -> v.name().equalsIgnoreCase(s)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
	}
}
