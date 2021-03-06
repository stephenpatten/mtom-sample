package rst.sample.mtom.client.ws;

import java.io.InputStream;
import java.util.Random;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import rst.sample.mtom.jaxb.Document;
import rst.sample.mtom.jaxb.StoreDocumentRequest;
import rst.sample.mtom.jaxb.StoreDocumentResponse;

public class DocumentsClient extends WebServiceGatewaySupport {

	private static final String[] AUTHORS = new String[] {"Herbert", "Ernie", "Bibo", "Bert"};
	private Random random = new Random();

	
	public DocumentsClient() {
		setMessageSender(new ChunkedEncodingMessageSender());
	}

	public boolean storeDocument(int size) {
		Document document = new Document();
		document.setContent(getContentAsDataHandler(size));
		document.setAuthor(getAuthor());
		document.setName(Integer.toString(size));

		StoreDocumentRequest request = new StoreDocumentRequest();
		request.setDocument(document);

		System.out.println();
		System.out.println("Storing document of size " + size);

		StoreDocumentResponse response = (StoreDocumentResponse) getWebServiceTemplate()
				.marshalSendAndReceive(request);
		boolean success = response.isSuccess();
		System.out.println("success: " + true);
		return success;
	}

	private DataHandler getContentAsDataHandler(final int size) {
		InputStream input = getContentAsStream(size);
		DataSource source = new InputStreamDataSource(input, Integer.toString(size));
		return new DataHandler(source);
	}
	
	private InputStream getContentAsStream(final int size) {
		return new RandomSizeInputStream(size);
	}
	
	private String getAuthor() {
		return AUTHORS[random.nextInt(AUTHORS.length)];
	}

}
