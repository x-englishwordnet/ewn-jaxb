import org.ewn.jaxb.*;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class Scan
{
	private final boolean verbose;

	public Scan(final boolean verbose)
	{
		this.verbose = verbose;
	}

	public void scanLexEntries(final LexicalResource lexicalResource)
	{
		assertNotNull(lexicalResource);
		final Lexicon lexicon = (Lexicon) lexicalResource.getLexiconOrLexiconExtension().get(0);
		assertNotNull(lexicon);

		for (LexicalEntry lexEntry : lexicon.getLexicalEntry())
		{
			walkLexEntry(lexEntry, "");
		}
	}

	public void scanSenses(final LexicalResource lexicalResource)
	{
		assertNotNull(lexicalResource);
		final Lexicon lexicon = (Lexicon) lexicalResource.getLexiconOrLexiconExtension().get(0);
		assertNotNull(lexicon);

		for (LexicalEntry lexEntry : lexicon.getLexicalEntry())
		{
			if (this.verbose)
			{
				System.out.printf("senses of %s%n", toString(lexEntry));
			}

			for (Sense sense : lexEntry.getSense())
			{
				walkSense(sense, "\t");
				Synset synset = (Synset) sense.getSynset();
				if (this.verbose)
				{
					System.out.printf("\tsynset %s%n", toString(synset));
					// walkSynset(synset, "\t");
				}
				System.out.println();
			}

			// OBSOLETE
			// List<SyntacticBehaviour> syntacticBehaviours = lexEntry.getSyntacticBehaviour();
		}
	}

	public void scanSynsets(final LexicalResource lexicalResource)
	{
		assertNotNull(lexicalResource);
		final Lexicon lexicon = (Lexicon) lexicalResource.getLexiconOrLexiconExtension().get(0);
		assertNotNull(lexicon);
		for (Synset synset : lexicon.getSynset())
		{
			walkSynset(synset, "");
			System.out.println();
		}
	}

	public String toString(LexicalEntry lexEntry)
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

	public String toString(Sense sense)
	{
		String senseId = sense.getId();
		BigInteger n = sense.getN();
		AdjPositionType adjPosition = sense.getAdjposition();
		String sensekey = Utils.getSensekey(sense);
		// OBSOLETE
		// String identifier = sense.getIdentifier();
		return String.format("%s sensekey:'%s' n:%d adjpos:%s", senseId, sensekey, n, adjPosition);
	}

	public String toString(Synset synset)
	{
		String synsetid = synset.getId();
		PartOfSpeechType pos = synset.getPartOfSpeech();
		List<Object> members = synset.getMembers();
		String members2 = Utils.join(members, ',', m -> ((LexicalEntry) m).getLemma().getWrittenForm());
		String definition = synset.getDefinition().get(0).getContent();
		return String.format("%s %s {%s} \"%s\"", synsetid, pos, members2, definition.length() > 32 ? definition.substring(0, 32) + "..." : definition);
	}

	public void walkLexEntry(LexicalEntry lexEntry, CharSequence indent)
	{
		if (this.verbose)
		{
			System.out.printf("%slexentry %s%n", indent, toString(lexEntry));
		}
	}

	public void walkSense(Sense sense, CharSequence indent)
	{
		if (verbose)
		{
			System.out.printf("%ssense %s%n", indent, toString(sense));
		}

		// lexical entry
		// LexicalEntry lexEntry = (LexicalEntry) sense.getParent();
		// walkLexEntry(lexEntry, indent);

		// verb frames and templates
		List<Object> subcats = sense.getSubcat();
		for (Object subcat : subcats)
		{
			SyntacticBehaviour verbFrame = (SyntacticBehaviour) subcat;
			if (verbose)
			{
				System.out.printf("%sverb frame : %s = %s%n", indent, verbFrame.getId(), verbFrame.getSubcategorizationFrame());
			}
		}

		/*
		// OBSOLETE
		List<SyntacticBehaviour> syntacticBehaviours = lexEntry.getSyntacticBehaviour();
		if (syntacticBehaviours != null)
		{
			for (SyntacticBehaviour syntacticBehaviour : syntacticBehaviours)
			{
				if (syntacticBehaviour.getSenses().contains(senseId))
				{
					String verbFrame = syntacticBehaviour.getSubcategorizationFrame();
					if (verbose)
					{
						System.out.printf("verbframe: %s%n", verbFrame);
					}
				}
			}
		}
		*/

		// lex relations
		for (SenseRelation senseRelation : sense.getSenseRelation())
		{
			Sense target = (Sense) senseRelation.getTarget();
			SenseRelationType type = senseRelation.getRelType();
			if (target != null) // local ref within this file
			{
				/*
				LexicalEntry targetLexEntry = (LexicalEntry) target.getParent();
				String targetLemma = targetLexEntry.getLemma().getWrittenForm();
				Synset targetSynset = (Synset) target.getSynset();
				if (verbose)
				{
					System.out.printf("%srelation: %s to target lemma '%s' synset '%s'%n", indent, type, targetLemma, targetSynset.getDefinition().get(0).getContent());
				}
				*/
			}
		}
	}

	public void walkSynset(Synset synset, CharSequence indent)
	{
		if (verbose)
		{
			System.out.printf("%ssynset %s%n", indent, toString(synset));
		}

		List<Object> members = synset.getMembers();
		for (Object member : members)
		{
			LexicalEntry memberEntry = (LexicalEntry) member;
			String pronunciation = Utils.join(memberEntry.getLemma().getPronunciation(), ' ', p -> {

				String ipa = p.getContent();
				String variety = p.getVariety();
				return String.format("%s/%s/", variety == null ? "" : "[" + variety + "] ", ipa);

			});
			if (this.verbose)
			{
				System.out.printf("%s\tmember: %s '%s' %s%n", indent, memberEntry.getId(), memberEntry.getLemma().getWrittenForm(), pronunciation);
			}
		}
		Definition definition = synset.getDefinition().get(0);
		if (verbose)
		{
			System.out.printf("%s\tdefinition: '%s'%n", indent, definition.getContent());
		}
		for (Example example : synset.getExample())
		{
			if (this.verbose)
			{
				System.out.printf("%s\texample: %s%n", indent, example.getContent());
			}
		}
		for (SynsetRelation synsetRelation : synset.getSynsetRelation())
		{
			Synset target = (Synset) synsetRelation.getTarget();
			if (target != null) // local ref within this file
			{
				SynsetRelationType type = synsetRelation.getRelType();
				if (this.verbose)
				{
					System.out.printf("%s\trelation: %s to target synset '%s'%n", indent, type, target.getDefinition().get(0).getContent());
				}
			}
		}
	}
}