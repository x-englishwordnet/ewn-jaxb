package org.ewn.jaxb;

import java.math.BigInteger;
import java.util.List;

public class Strings {
	public static String toString(LexicalEntry lexEntry)
	{
		Lemma lemma = lexEntry.getLemma();
		PartOfSpeechType pos = lemma.getPartOfSpeech();
		String writtenForm = lemma.getWrittenForm();
		List<Pronunciation> pronunciations = lexEntry.getLemma().getPronunciation();
		String pronunciation = Utils.join(pronunciations, ' ', p -> {

			String ipa = p.getContent();
			String variety = p.getVariety();
			return String.format("%s/%s/", variety == null ? "" : "[" + variety + "] ", ipa);

		});
		return String.format("%s '%s' %s", pos, writtenForm, pronunciation);
	}

	public static String toString(Sense sense)
	{
		String senseId = sense.getId();
		BigInteger n = sense.getN();
		AdjPositionType adjPosition = sense.getAdjposition();
		String sensekey = Utils.getSensekey(sense);
		return String.format("%s sensekey:'%s' n:%d adjpos:%s", senseId, sensekey, n, adjPosition);
	}

	public static String toString(Synset synset)
	{
		String synsetid = synset.getId();
		PartOfSpeechType pos = synset.getPartOfSpeech();
		List<Object> members = synset.getMembers();
		String members2 = Utils.join(members, ',', m -> ((LexicalEntry) m).getLemma().getWrittenForm());
		String definition = synset.getDefinition().get(0).getContent();
		return String.format("%s %s {%s} \"%s\"", synsetid, pos, members2, definition.length() > 32 ? definition.substring(0, 32) + "..." : definition);
	}
}
