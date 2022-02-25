package vn.ds.study;

import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.AbstractConnector;
import org.wso2.carbon.connector.core.ConnectException;

import io.minio.MinioClient;

public abstract class MinioAgent extends AbstractConnector {

    private MinioClient minioClient;
    
    protected MinioClient getClient(String address, final String accessKey, final String secretKey) {
        if (minioClient == null) {
            MinioClient minioClient = MinioClient.builder().endpoint(address).credentials(accessKey, secretKey).build();
            return minioClient;
        }
        return minioClient;
    }

	private MessageContext context;

	@Override
	public final void connect(final MessageContext messageContext) throws ConnectException {
		this.context = messageContext;
		execute(messageContext);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getParameter(String parameterName, Class<T> type) {
		return (T) getParameter(context, parameterName);
	}

	protected String getParameterAsString(String parameterName) {
		return getParameter(parameterName, String.class);
	}

	protected abstract void execute(final MessageContext messageContext) throws ConnectException;

}
