import org.ewn.jaxb.Factory;
import org.ewn.jaxb.LexicalResource;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import jakarta.xml.bind.JAXBException;

import static org.junit.Assert.assertNotNull;

public class Tests
{
	private String source = System.getProperty("SOURCE");

	private final boolean silent = System.getProperties().containsKey("SILENT");

	private LexicalResource lexicalResource;

	@Before
	public void init() throws JAXBException, XMLStreamException
	{
		if (source == null)
		{
			System.err.printf("Define source file with -DSOURCE=path%n");
			System.exit(1);
		}
		final File xmlFile = new File(source);
		System.out.printf("source=%s%n", xmlFile.getAbsolutePath());
		this.lexicalResource = Factory.make(xmlFile);
	}

	@Test
	public void scanLexEntries()
	{
		assertNotNull(this.lexicalResource);
		new Scan(!silent).scanLexEntries(this.lexicalResource);
	}

	@Test
	public void scanSenses()
	{
		assertNotNull(this.lexicalResource);
		new Scan(!silent).scanSenses(this.lexicalResource);
	}

	@Test
	public void scanSynsets()
	{
		assertNotNull(this.lexicalResource);
		new Scan(!silent).scanSynsets(this.lexicalResource);
	}
}