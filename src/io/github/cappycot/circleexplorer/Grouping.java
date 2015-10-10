package io.github.cappycot.circleexplorer;

public class Grouping {
	/* Instance Variables */
	private int size = 1;
	private int cluster = 1;

	/* Constructors */
	public Grouping(int size, int cluster) {
		this.size = size;
		this.cluster = cluster;
	}

	public Grouping(Grouping group) {
		this.size = group.size;
		this.cluster = group.cluster;
	}

	/* Getters */
	public int getSize() {
		return size;
	}

	public int getCluster() {
		return cluster;
	}

	public int getTotal() {
		return size * cluster;
	}

	/* Setters */
	public Grouping incSize(int inc) {
		size += inc;
		if (size < 1)
			size = 1;
		return this;
	}

	public Grouping incCluster(int inc) {
		cluster += inc;
		if (cluster < 1)
			cluster = 1;
		return this;
	}

	public Grouping conform(Grouping group) {
		this.size = group.size;
		this.cluster = group.cluster;
		return this;
	}

	/* Operations */
	public Grouping rotate() {
		int p = this.size;
		size = cluster;
		cluster = p;
		return this;
	}

	/**
	 * Multiplies the cluster of this Grouping by another Grouping.
	 * 
	 * @param group
	 */
	public Grouping merge(Grouping group) {
		this.cluster += group.getTotal();
		return this;
	}

	/**
	 * Attempts to append the units from another Grouping to this Grouping
	 * divided by a common cluster.
	 * 
	 * @param group
	 * @return remainder
	 */
	public Grouping append(Grouping group) {
		if (this.cluster > group.getTotal()) {
			return group;
		} else if (this.cluster == group.cluster) {
			this.size += group.size;
			return null;
		} else if (this.cluster == group.rotate().cluster) {
			this.size += group.size;
			return null;
		}
		this.size += group.getTotal() / this.cluster;
		group = new Grouping(1, group.getTotal() % this.cluster);
		return group;
	}
}
