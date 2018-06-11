package utils;

public enum StatusCode {
	SUCCESS("SUCCESS"), 
	FAILURE("FAILURE");
	
	private String value;

	private StatusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

