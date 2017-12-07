package sic;

public class Triplet {
	private int length;
	private String address, value;

	public Triplet(int length, String value,String address) {
		this.length = length;
		this.value = value;
		this.address =address;
	}

	public int getLength() {
		return length;
	}

	public String getAddress() {
		return address;
	}

	public String getValue() {
		return value;
	}

}
