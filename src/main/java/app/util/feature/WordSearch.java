package app.util.feature;

import static java.lang.Math.max;
import static java.math.BigInteger.ZERO;
import static java.util.Arrays.copyOf;

import java.math.BigInteger;
import java.nio.charset.Charset;

public class WordSearch {

	public WordSearch() {

	}

	public int interpolate(String ys, String xs, String iOfTs, int id) {
		int maxLen = max(max(xs.length(), ys.length()), iOfTs.length());
		BigInteger x = new BigInteger(1, copyOf(xs.getBytes(Charset.defaultCharset()), maxLen));
		BigInteger y = new BigInteger(1, copyOf(ys.getBytes(Charset.defaultCharset()), maxLen));
		BigInteger iOfT = new BigInteger(1, copyOf(iOfTs.getBytes(Charset.defaultCharset()), maxLen));
		BigInteger d = BigInteger.valueOf(id);
		BigInteger den = x.subtract(y);
		return ZERO.equals(den) ? 0 : (int) d.multiply(iOfT.subtract(y)).divide(den).longValue();
	}

	public int interpolationSearch(String[] a, String target) {
		int p = 0;
		int q = a.length - 1;
		while ((target.compareTo(a[p]) >= 0) && (target.compareTo(a[q]) <= 0)) {
			int m = p + interpolate(a[p], a[q], target, q - p);
			int cmp = target.compareTo(a[m]);
			if (cmp < 0)
				q = m - 1;
			else if (cmp > 0)
				p = m + 1;
			else
				return m;
		}
		return -1; // search fail
	}

}
