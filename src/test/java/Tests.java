import org.ewn.jaxb.Factory;
import org.ewn.jaxb.LexicalResource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import jakarta.xml.bind.JAXBException;

import static org.junit.Assert.assertNotNull;

public class Tests
{
	private static final String source = System.getProperty("SOURCE");

	private static final boolean silent = System.getProperties().containsKey("SILENT");

	private static LexicalResource lexicalResource;

	@BeforeClass
	public static void getDocument() throws JAXBException, XMLStreamException
	{
		if (source == null)
		{
			System.err.printf("Define source file with -DSOURCE=path%n");
			System.exit(1);
		}
		final File xmlFile = new File(source);
		System.out.printf("source=%s%n", xmlFile.getAbsolutePath());
		if (!xmlFile.exists())
		{
			System.err.println("Define XML source dir that exists");
			System.exit(2);
		}
		lexicalResource = Factory.make(xmlFile);
		assertNotNull(lexicalResource);
	}

	@Test
	public void scanLexEntries()
	{
		new Scan(!silent).scanLexEntries(lexicalResource);
	}

	@Test
	public void scanSenses()
	{
		new Scan(!silent).scanSenses(lexicalResource);
	}

	@Test
	public void scanSynsets()
	{
		new Scan(!silent).scanSynsets(lexicalResource);
	}
}