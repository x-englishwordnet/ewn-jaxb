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
	@SuppressWarnings("FieldCanBeLocal")
	private final String MERGED_FILE = "merged/xewn.xml";

	private final String ewnHome = System.getenv("EWNHOME");

	private final String xewnHome = System.getenv("XEWNHOME");

	private final String target = System.getenv("TARGET");

	private LexicalResource lexicalResource;

	@Before
	public void init() throws JAXBException, XMLStreamException
	{
		// System.out.println(xewnHome);
		// System.out.println(ewnHome);
		// System.out.println(target);
		final String dir = xewnHome.isEmpty() ? ewnHome : xewnHome;
		final File xmlFile = target != null ? new File(dir, target) : new File(dir, MERGED_FILE);
		System.out.println(xmlFile.getAbsolutePath());
		this.lexicalResource = Factory.make(xmlFile);
	}

	@Test
	public void scanLexEntries()
	{
		assertNotNull(this.lexicalResource);
		new Scan(true).scanLexEntries(this.lexicalResource);
	}

	@Test
	public void scanSenses()
	{
		assertNotNull(this.lexicalResource);
		new Scan(true).scanSenses(this.lexicalResource);
	}

	@Test
	public void scanSynsets()
	{
		assertNotNull(this.lexicalResource);
		new Scan(true).scanSynsets(this.lexicalResource);
	}
}