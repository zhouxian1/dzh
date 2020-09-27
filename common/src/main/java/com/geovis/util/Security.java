package com.geovis.util;

public enum Security {
	AES(1,"aes"),RSA(2,"rsa"),DES(3,"des"),URL_ENCODE(4,"url_encode"),BASE64(5,"base64"),AES_RSA(6,"aes_rsa");
	
	private int way;
	private String mark;
	
	private Security(int way, String mark){
		this.setWay(way);
		this.mark = mark;
	}
	
	public static Security getSecurity(int way){
		switch (way) {
		case 1:
			return Security.AES;
		case 2:
			return Security.RSA;
		case 3:
			return Security.DES;
		case 4:
			return Security.URL_ENCODE;
		case 5:
			return Security.BASE64;
		case 6:
			return Security.AES_RSA;
		default:
			return null;
		}
	}

	public int getWay() {
		return way;
	}
	
	@Override
	public String toString() {
		return this.mark+":"+this.way;
	}

	public void setWay(int way) {
		this.way = way;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	
}
