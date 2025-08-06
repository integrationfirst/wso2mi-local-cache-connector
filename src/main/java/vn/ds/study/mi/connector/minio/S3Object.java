package vn.ds.study.mi.connector.minio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3Object {
    private String key;
    private String bucket;
    private String region;
    private byte[] bytes;
    private String text;
    private String contentType;
    private long size;
    private String version;
    private String lastModified;

    public static S3ObjectBuilder from(String path) {
        String[] parts = path.split("/", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException(String.format("Invalid S3 path %s, it should in form [bucketName/objectName] ", path));
        }
        return S3Object.builder()
                       .bucket(parts[0])
                       .key(parts[1]);
    }
}
