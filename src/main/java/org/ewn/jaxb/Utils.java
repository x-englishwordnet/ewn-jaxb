package org.ewn.jaxb;

import java.util.Collection;
import java.util.function.Function;

public class Utils
{
	private Utils()
	{
	}

	/**
	 * Get sense's lexical entry
	 *
	 * @param sense sense
	 * @return lexical entry
	 */
	static public LexicalEntry getLexicalEntry(Sense sense)
	{
		return (LexicalEntry) sense.getParent();
	}

	/**
	 * Extract sensekey
	 *
	 * @param sense sense
	 * @return sensekey
	 */
	static public String getSensekey(Sense sense)
	{
		String senseId = sense.getId();
		return toSensekey(senseId);
	}

	/**
	 * Convert ID to sensekey
	 *
	 * @param senseId sense id
	 * @return sensekey
	 */
	static public String toSensekey(String senseId)
	{
		String sk = senseId.substring("oewn-".length());
		int b = sk.indexOf("__");

		String lemma = sk.substring(0, b) //
				.replace("-ap-", "'") //
				.replace("-lb-", "(") // extension
				.replace("-rb-", ")") // extension
				.replace("-sl-", "/") //
				.replace("-ex-", "!") //
				.replace("-cm-", ",") //
				.replace("-cl-", ":") //
				.replace("-sp-", "_"); // extension

		String tail = sk.substring(b + 2) //
				.replace(".", ":") //
				.replace("-ap-", "'") // extension
				.replace("-ap-", "'") // extension
				.replace("-lb-", "(") // extension
				.replace("-rb-", ")") // extension
				.replace("-sl-", "/") // extension
				.replace("-cm-", ",") // extension
				.replace("-ex-", "!") // extension
				.replace("-cl-", ":") // extension
				.replace("-sp-", "_");

		return lemma + '%' + tail;
	}

	/*
	def unmap_sense_key(sk):
	    if "__" in sk:
	        e = sk.split("__")
	        l = e[0][KEY_PREFIX_LEN:]
	        r = "__".join(e[1:])
	        return (
	        l
	        .replace("-ap-", "'")
	        .replace("-sl-", "/")
	        .replace("-ex-", "!")
	        .replace("-cm-",",")
	        .replace("-cl-",":")
	        + "%" +
	        r
	        .replace(".", ":")
	        .replace("-sp-","_"))
	    else:
	        return sk[KEY_PREFIX_LEN:].replace("__", "%").replace("-ap-", "'").replace("-sl-", "/").replace("-ex-", "!").replace("-cm-",",").replace("-cl-",":")
	*/

	/**
	 * Join items
	 *
	 * @param items collection of items of type T
	 * @param delim delimiter
	 * @param f     string function to represent item
	 * @return joined string representation of items
	 */
	static public <T> String join(Collection<T> items, char delim, Function<T, String> f)
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
