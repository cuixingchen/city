package com.hdsx.taxi.woxing.location.distributeservice;

public class TaxiIndexZoneArgments {
	@Override
	public String toString() {
		return "TaxiIndexArgments [id=" + id + ", xmin=" + xmin + ", ymin="
				+ ymin + ", xmax=" + xmax + ", ymax=" + ymax + ", cell=" + cell
				+ "]";
	}
	String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getXmin() {
		return xmin;
	}
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	public double getYmin() {
		return ymin;
	}
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	public double getXmax() {
		return xmax;
	}
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	public double getYmax() {
		return ymax;
	}
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	public double getCell() {
		return cell;
	}
	public void setCell(double cell) {
		this.cell = cell;
	}
	double xmin, ymin, xmax, ymax, cell;

}
