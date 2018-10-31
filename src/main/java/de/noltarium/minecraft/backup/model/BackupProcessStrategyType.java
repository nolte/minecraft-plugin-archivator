package de.noltarium.minecraft.backup.model;

import java.util.Arrays;

public enum BackupProcessStrategyType {
	SAFTY, DIRECT;

	public String value() {
		return this.name().toLowerCase();
	}

	public static BackupProcessStrategyType fromString(String s) throws IllegalArgumentException {
		return Arrays.stream(BackupProcessStrategyType.values()).filter(v -> v.name().equalsIgnoreCase(s)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("unknown value: " + s));
	}

}
