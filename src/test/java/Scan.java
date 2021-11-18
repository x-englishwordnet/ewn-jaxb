import org.ewn.jaxb.*;

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
				System.out.printf("senses of %s%n", Strings.toString(lexEntry));
			}

			for (Sense sense : lexEntry.getSense())
			{
				walkSense(sense, "\t");
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

	public void walkLexEntry(LexicalEntry lexEntry, CharSequence indent)
	{
		Dump.dumpLex(lexEntry, indent, !verbose);
	}

	public void walkLexEntry2(LexicalEntry lexEntry, CharSequence indent)
	{
		if (this.verbose)
		{
			System.out.printf("%slexentry %s%n", indent, Strings.toString(lexEntry));
		}
	}

	public void walkSense(Sense sense, CharSequence indent)
	{
		Dump.dumpSense(sense, indent, !verbose);
	}

	public void walkSense2(Sense sense, CharSequence indent)
	{
		if (verbose)
		{
			System.out.printf("%ssense %s%n", indent, Strings.toString(sense));
		}

		// lexical entry
		LexicalEntry lexEntry = (LexicalEntry) sense.getParent();
		walkLexEntry(lexEntry, indent);

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

		// lex relations
		for (SenseRelation senseRelation : sense.getSenseRelation())
		{
			Sense target = (Sense) senseRelation.getTarget();
			SenseRelationType type = senseRelation.getRelType();
			if (target != null) // local ref within this file
			{
				LexicalEntry targetLexEntry = Utils.getLexicalEntry(target);
				String targetLemma = targetLexEntry.getLemma().getWrittenForm();
				Synset targetSynset = (Synset) target.getSynset();
				if (verbose)
				{
					System.out.printf("%srelation: %s to target lemma '%s' synset '%s'%n", indent, type, targetLemma, targetSynset.getDefinition().get(0).getContent());
					System.out.printf("%srelation: %s to target lemma '%s' synset '%s'%n", indent, type, targetLemma, targetSynset.getDefinition().get(0).getContent());
				}
			}
		}
	}

	public void walkSynset(Synset synset, CharSequence indent)
	{
		Dump.dumpSynset(synset,indent,!this.verbose);
	}

	public void walkSynset2(Synset synset, CharSequence indent)
	{
		if (verbose)
		{
			System.out.printf("%ssynset %s%n", indent, Strings.toString(synset));
		}

		// members
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