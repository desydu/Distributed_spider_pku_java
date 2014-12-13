package edu.pku.ss.hw;

public class DecimalUtils {
	/**
	   * ËÄÉáÎåÈë
	   */
	  public static Double round2digit(Double d) {
	    if (d == null) {
	      return 0.0;
	    }
	    Double result = Double.parseDouble(String.format("%.2f", d));
	    return result;
	  }

	  public static float round2digitFloat(Float d) {
	    if (d == null) {
	      return 0.0f;
	    }
	    Float result = Float.parseFloat(String.format("%.2f", d));
	    return result;
	  }
}
