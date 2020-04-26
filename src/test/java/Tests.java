import org.ewn.jaxb.Factory;
import org.ewn.jaxb.LexicalResource;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class Tests
{
	private String TEST_FILE = "wn-verb.body.xml";

	private final String ewnHome = System.getenv("EWNHOME") + File.separator + "master" + File.separator + "src";

	private LexicalResource lexicalResource;

	@Before public void init() throws JAXBException, IOException, XMLStreamException
	{
		final File xmlFile = new File(ewnHome, TEST_FILE);
		this.lexicalResource = Factory.make(xmlFile);
	}

	@Test public void scanSenses()
	{
		assertNotNull(this.lexicalResource);
		new Scan(true).scanSenses(this.lexicalResource);
	}

	@Test public void scanSynsets()
	{
		assertNotNull(this.lexicalResource);
		new Scan(true).scanSynsets(this.lexicalResource);
	}
}