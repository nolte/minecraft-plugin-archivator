package de.noltarium.minecraft.archivator.core.backup;

public interface ProcessListener<T> {

	public void receiveProcessState(T eventObject);

}
