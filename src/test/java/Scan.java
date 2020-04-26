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

	public void scanSenses(final LexicalResource lexicalResource)
	{
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexicon().get(0);
		assertNotNull(lexicon);

		for (LexicalEntry lexEntry : lexicon.getLexicalEntry())
		{
			List<SyntacticBehaviour> syntacticBehaviours = lexEntry.getSyntacticBehaviour();
			Lemma lemma = lexEntry.getLemma();
			PartOfSpeechType pos = lemma.getPartOfSpeech();
			String writtenForm = lemma.getWrittenForm();
			if (this.verbose)
				System.out.printf("lemma %s %s%n", writtenForm, pos);

			for (Sense sense : lexEntry.getSense())
			{
				walkSense(sense);
				Synset synset = (Synset) sense.getSynset();
				walkSynset(synset);
				System.out.println();
			}
		}
	}

	public void scanSynsets(final LexicalResource lexicalResource)
	{
		assertNotNull(lexicalResource);
		final Lexicon lexicon = lexicalResource.getLexicon().get(0);
		assertNotNull(lexicon);
		for (Synset synset : lexicon.getSynset())
		{
			walkSynset(synset);
			System.out.println();
		}
	}

	public void walkSense(Sense sense)
	{
		String senseId = sense.getId();

		BigInteger n = sense.getN();
		String identifier = sense.getIdentifier();
		AdjPositionType adjPosition = sense.getAdjposition();
		if (verbose)
			System.out.printf("senseid:'%s' n:%d identifier:%s adjpos:%s%n", senseId, n, identifier, adjPosition);

		// verb frames and templates
		LexicalEntry lexEntry = (LexicalEntry) sense.getParent();
		List<SyntacticBehaviour> syntacticBehaviours = lexEntry.getSyntacticBehaviour();
		if (syntacticBehaviours != null)
			for (SyntacticBehaviour syntacticBehaviour : syntacticBehaviours)
			{
				if (syntacticBehaviour.getSenses().contains(senseId))
				{
					String verbFrame = syntacticBehaviour.getSubcategorizationFrame();
					if (verbose)
						System.out.printf("verbframe: %s%n", verbFrame);
				}
			}
		// lex relations
		for (SenseRelation senseRelation : sense.getSenseRelation())
		{
			Sense target = (Sense) senseRelation.getTarget();
			SenseRelationType type = senseRelation.getRelType();
			if (target != null) // local ref within this file
			{
				LexicalEntry targetLexEntry = (LexicalEntry) target.getParent();
				String targetLemma = targetLexEntry.getLemma().getWrittenForm();
				Synset targetSynset = (Synset) target.getSynset();
				if (verbose)
					System.out.printf("relation: %s to target lemma '%s' synset '%s'%n", type, targetLemma, targetSynset.getDefinition().get(0).getContent());
			}
		}
	}

	public void walkSynset(Synset synset)
	{
		String synsetid = synset.getId();
		PartOfSpeechType pos = synset.getPartOfSpeech();
		if (verbose)
			System.out.printf("synsetid: %s pos: '%s'%n", synsetid, pos);
		Definition definition = synset.getDefinition().get(0);
		if (verbose)
			System.out.printf("definition: '%s'%n", definition.getContent());
		for (Example example : synset.getExample())
		{
			if (this.verbose)
				System.out.printf("example: %s%n", definition.getContent());
		}
		for (SynsetRelation synsetRelation : synset.getSynsetRelation())
		{
			Synset target = (Synset) synsetRelation.getTarget();
			if (target != null) // local ref within this file
			{
				SynsetRelationType type = synsetRelation.getRelType();
				if (this.verbose)
					System.out.printf("relation: %s to target synset '%s'%n", type, target.getDefinition().get(0).getContent());
			}
		}
	}
}