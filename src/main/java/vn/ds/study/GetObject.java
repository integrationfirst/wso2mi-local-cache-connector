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
package vn.ds.study;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.synapse.MessageContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.connector.core.ConnectException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.minio.DownloadObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import junit.framework.Assert;
import vn.ds.study.utils.CompressUtils;

public class GetObject extends MinioAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetObject.class);

    @Override
    protected void execute(final MessageContext messageContext) throws ConnectException {

        final String address = getParameterAsString("address");
        final String bucket = getParameterAsString("bucket");
        final String filename = getParameterAsString("filename");
        final String accessKey = getParameterAsString("accessKey");
        final String secretKey = getParameterAsString("secretKey");
        try {
            final MinioClient client = getClient(address, accessKey, secretKey);

            LOGGER.debug("Created a minio client. {}", client);

            Assert.assertNotNull("Minio client initialization failed", client);

            final DownloadObjectArgs downloadObjectArgs = DownloadObjectArgs.builder().bucket(bucket).object(
                filename).build();
            LOGGER.info("Prepared download object arguments.");

            final GetObjectArgs args = new GetObjectArgs(downloadObjectArgs);

            LOGGER.debug("Prepared arguments.");
            final GetObjectResponse getObjectResponse = client.getObject(args);

            try (final InputStream in = getObjectResponse;) {
                final ByteArrayOutputStream output = CompressUtils.decompressBytes(in);
                JsonObject jsonObject = new JsonParser().parse(new String(output.toByteArray())).getAsJsonObject();
                LOGGER.info("{}", jsonObject);
                messageContext.setProperty("objectResult", jsonObject);
            }
            LOGGER.info("Get the object and put to the objectResult property.");
        } catch (Exception e) {
            LOGGER.error("Failed to download file. Detail: ", e);
        }
    }
}