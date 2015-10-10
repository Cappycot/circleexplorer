package io.github.cappycot.circleexplorer;

public class Grouping {

	private int size = 1;
	private int cluster = 1;

	public Grouping(int size, int cluster) {
		this.size = size;
		this.cluster = cluster;
	}

	public Grouping(Grouping group) {
		this.size = group.size;
		this.cluster = group.cluster;
	}

	public int getSize() {
		return size;
	}

	public int getCluster() {
		return cluster;
	}
	
	public int getTotal() {
		return size * cluster;
	}

	public void incSize(int inc) {
		size += inc;
		if (size < 1)
			size = 1;
	}

	public void incCluster(int inc) {
		cluster += inc;
		if (cluster < 1)
			cluster = 1;
	}

	public void conform(Grouping group) {
		this.size = group.size;
		this.cluster = group.cluster;
	}
}
