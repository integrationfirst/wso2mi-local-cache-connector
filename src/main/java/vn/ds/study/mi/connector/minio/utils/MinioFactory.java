package vn.ds.study.mi.connector.minio.utils;

import io.minio.MinioClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MinioFactory {
    public static final String DEFAULT_REGION = "eu-central-1";
    private static Map<String, MinioClient> clients = new HashMap<>();

    public static final MinioClient getClient(String endpoint, String region, final String accessKey,
                                              final String secretKey) {
        return Optional.ofNullable(clients.get(endpoint))
                       .orElseGet(() -> clients.put(endpoint, MinioClient.builder()
                                                                         .endpoint(endpoint)
                                                                         .region(region)
                                                                         .credentials(accessKey, secretKey)
                                                                         .build()));
    }

    public static final MinioClient getClient(String endpoint, final String accessKey,
                                              final String secretKey) {
        return getClient(endpoint, DEFAULT_REGION, accessKey, secretKey);
    }
}
