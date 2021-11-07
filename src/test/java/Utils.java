import org.ewn.jaxb.Sense;

import java.util.Collection;
import java.util.function.Function;

public class Utils
{
	private Utils()
	{
	}

	/**
	 * Extract sensekey
	 *
	 * @param sense sense
	 * @return sensekey
	 */
	static String getSensekey(Sense sense)
	{
		String sk = sense.getId();
		int b = sk.indexOf("__");

		String lemma = sk.substring(0, b) //
				.replace("-ap-", "'") //
				.replace("-ap-", "'") //
				.replace("-lb-", "(") //
				.replace("-rb-", ")") //
				.replace("-sl-", "/") //
				.replace("-cm-", ",") //
				.replace("-ex-", "!") //
				.replace("-cl-", ":") //
				.replace("-sp-", "_");

		String tail = sk.substring(b + 2) //
				.replace(".", ":") //
				.replace("-ap-", "'") //
				.replace("-ap-", "'") //
				.replace("-lb-", "(") //
				.replace("-rb-", ")") //
				.replace("-sl-", "/") //
				.replace("-cm-", ",") //
				.replace("-ex-", "!") //
				.replace("-cl-", ":") //
				.replace("-sp-", "_");

		return lemma + '%' + tail;
	}

	/**
	 * Join items
	 *
	 * @param items collection of items of type T
	 * @param delim delimiter
	 * @param f     string function to represent item
	 * @return joined string representation of items
	 */
	static <T> String join(Collection<T> items, char delim, Function<T, String> f)
	{
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (T item : items)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				sb.append(delim);
			}
			String value = f.apply(item);
			sb.append(value);
		}
		return sb.toString();
	}
}
