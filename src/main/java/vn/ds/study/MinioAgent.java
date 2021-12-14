package vn.ds.study;

import io.minio.MinioClient;
import org.wso2.carbon.connector.core.AbstractConnector;

public abstract class MinioAgent extends AbstractConnector {

    protected MinioClient getClient(String address, final String accessKey, final String secretKey){
        MinioClient minioClient =
                MinioClient.builder()
                        .endpoint(address)
                        .credentials(accessKey, secretKey)
                        .build();

        return minioClient;
    }
}
