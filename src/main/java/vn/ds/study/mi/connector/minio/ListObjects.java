/*
 * Class: GetObject
 *
 * Created on Jan 26, 2022
 *
 * (c) Copyright Swiss Post Solutions Ltd, unpublished work
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Swiss Post Solution.
 * Floor 4-5-8, ICT Tower, Quang Trung Software City
 */
package vn.ds.study.mi.connector.minio;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.synapse.MessageContext;
import org.wso2.carbon.connector.core.ConnectException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ListObjects extends MinioAgent {

    private int parseInt(String intString) {
        try {
            return Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            log.error("Failed to parse {} into integer", intString);
            return 50;
        }
    }

    @Override
    protected void execute(final MessageContext messageContext) throws ConnectException {
        log.info("Listing the objects from object storage");
        try {
            final String address = getParameterAsString("address");
            final String region = getParameterAsString("region");
            final String bucket = getParameterAsString("bucket");
            final String filename = getParameterAsString("filename");
            final String accessKey = getParameterAsString("accessKey");
            final String secretKey = getParameterAsString("secretKey");
            final String prefix = getParameterAsString("prefix");
            final String maxString = getParameterAsString("max");
            final int max = parseInt(maxString);

            final MinioClient client = getClient(address, region, accessKey, secretKey);

            log.info("Created a minio client {} region {}", address, region);

            final ListObjectsArgs args = ListObjectsArgs.builder()
                                                        .bucket(bucket)
                                                        .startAfter(filename)
                                                        .prefix(prefix)
                                                        .build();
            log.info(String.format("List the objects with the bucket [%s] prefix [%s] limit [%d]", bucket, prefix, max));
            Iterable<Result<Item>> results = client.listObjects(args);
            List<S3Object> objects = new ArrayList<>();

            results.forEach(res -> {
                try {
                    Item obj = res.get();
                    if (!obj.isDir()) {
                        objects.add(S3Object.builder()
                                            .bucket(bucket)
                                            .key(obj.objectName())
                                            .lastModified(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(obj.lastModified()))
                                            .size(obj.size())
                                            .build());
                    }
                } catch (Exception e) {
                    log.error("Failed to get object details {}", e.getMessage());
                }
            });
            final List<S3Object> sortedObjects = objects.stream()
                                                        .sorted(Comparator.comparing(S3Object::getLastModified)
                                                                          .reversed())
                                                        .limit(max)
                                                        .collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            messageContext.setProperty("objectInfos", mapper.valueToTree(sortedObjects)
                                                            .toString());
            log.info("Return {}/{} objects ", max, objects.size());
        } catch (Exception e) {
            log.error("Failed to list the object", e);
            throw new ConnectException(e, "Failed to download file. Detail: ");
        }
    }
}