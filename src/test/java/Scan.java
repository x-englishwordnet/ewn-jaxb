import org.ewn.jaxb.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class Scan
{
	private final PrintStream ps;

	public Scan(final boolean verbose)
	{
		this.ps = verbose ? System.out : new PrintStream(new OutputStream()
		{
			public void write(int b)
			{
				//DO NOTHING
			}
		});
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
			ps.printf("senses of %s%n", Strings.toString(lexEntry));

			for (Sense sense : lexEntry.getSense())
			{
				walkSense(sense, "\t");
				ps.println();
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
			ps.println();
		}
	}

	public void walkLexEntry(LexicalEntry lexEntry, CharSequence indent)
	{
		Dump.dumpLex(lexEntry, indent, ps);
	}

	public void walkLexEntryShort(LexicalEntry lexEntry, CharSequence indent)
	{
		ps.printf("%slexentry %s%n", indent, Strings.toString(lexEntry));
	}

	public void walkSense(Sense sense, CharSequence indent)
	{
		Dump.dumpSense(sense, indent, ps);
	}

	public void walkSenseShort(Sense sense, CharSequence indent)
	{
		ps.printf("%ssense %s%n", indent, Strings.toString(sense));

		// lexical entry
		LexicalEntry lexEntry = (LexicalEntry) sense.getParent();
		walkLexEntry(lexEntry, indent);
	}

	public void walkSynset(Synset synset, CharSequence indent)
	{
		Dump.dumpSynset(synset, indent, ps);
	}

	public void walkSynsetShort(Synset synset, CharSequence indent)
	{
		ps.printf("%ssynset %s%n", indent, Strings.toString(synset));

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
			ps.printf("%s\tmember: %s '%s' %s%n", indent, memberEntry.getId(), memberEntry.getLemma().getWrittenForm(), pronunciation);
		}
		Definition definition = synset.getDefinition().get(0);
		ps.printf("%s\tdefinition: '%s'%n", indent, definition.getContent());
	}
}