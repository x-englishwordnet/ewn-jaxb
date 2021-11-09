package org.ewn.jaxb;

import java.io.File;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class Factory
{
	private Factory()
	{
	}

	public static LexicalResource make(final File xmlFile) throws JAXBException, XMLStreamException
	{
		XMLInputFactory xif = XMLInputFactory.newFactory();
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(xmlFile));

		JAXBContext jaxbContext = JAXBContext.newInstance(LexicalResource.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		jaxbUnmarshaller.setListener(new Unmarshaller.Listener()
		{

			@Override
			public void beforeUnmarshal(final Object target, final Object parent)
			{
				super.beforeUnmarshal(target, parent);
				// System.out.printf("%s %s%n", target, parent);
			}

			@Override
			public void afterUnmarshal(final Object target, final Object parent)
			{
				super.afterUnmarshal(target, parent);
				// System.out.printf("%s parent %s%n", target, parent);
				((Base) target).setParent(parent);
			}
		});
		return (LexicalResource) jaxbUnmarshaller.unmarshal(xsr);
	}
}
