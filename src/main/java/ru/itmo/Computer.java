package ru.itmo;

public class Computer {

	private final long id;
	private String cpu;

	public Computer(long id, String cpu) {
		this.id = id;
		this.cpu = cpu;
	}

	public long getId() {
		return id;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
}
