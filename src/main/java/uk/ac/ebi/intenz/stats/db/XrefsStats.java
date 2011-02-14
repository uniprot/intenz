package uk.ac.ebi.intenz.stats.db;

public class XrefsStats {
	
	private int total;
	private int distinct;
	
	XrefsStats(int total, int distinct) {
		this.total = total;
		this.distinct = distinct;
	}
	
	public int getTotal() {
		return total;
	}
	
	public int getDistinct() {
		return distinct;
	}
	
}