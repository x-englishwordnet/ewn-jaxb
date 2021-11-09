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

		return (LexicalResource) jaxbUnmarshaller.unmarshal(xsr);
	}
}
