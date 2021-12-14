/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package vn.ds.study;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.synapse.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.connector.core.ConnectException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PutObject extends MinioAgent {

    private static final Logger LOGGER= LoggerFactory.getLogger(PutObject.class);

    @Override
    public void execute(MessageContext messageContext) throws ConnectException {

        String address = getParameterAsString( "address");
        String bucket =  getParameterAsString("bucket");
        String filename =  getParameterAsString( "filename");
        String accessKey =  getParameterAsString( "accessKey");
        String secretKey =  getParameterAsString( "secretKey");

        LOGGER.info("Put object {} to OS address {}", filename, address);
        MinioClient client = getClient(address, accessKey,secretKey);

        if (client == null){
            LOGGER.info("Failed to login into OS with access: {} and secret: {}", accessKey, secretKey);
            return;
        }

        LOGGER.info("Successfully login into OS");
        String fileContent = getParameterAsString("fileContent");
        String filePath = getParameterAsString("filePath");

        InputStream is =null;
        ObjectWriteResponse rsp = null;
        try {
            if (!StringUtils.isEmpty(fileContent)){

                is =new ByteArrayInputStream(fileContent.getBytes());
                rsp = putObject(is,client,bucket,filename);
                LOGGER.info("Successfully putObject into OS");
            } else if (!StringUtils.isEmpty(filePath)){

                LOGGER.info("Put file {} to OS",filePath);
                is = new FileInputStream(new File(filePath));
                rsp = putObject(is, client, bucket, filename);
                LOGGER.info("Successfully putObject into OS from filePath");
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Could not find {}", filePath, e);
        }
        finally {
            if (is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (rsp == null){
            return;
        }
        LOGGER.info("putObject result object: {}; versionId: {}", rsp.object(), rsp.versionId());
        messageContext.getContextEntries().put("object",rsp.object());
        messageContext.getContextEntries().put("versionId",rsp.versionId());
    }

    private ObjectWriteResponse putObject(final InputStream is, final MinioClient client, final String bucket, final String filename) {
        try {
            return client.putObject(
                    PutObjectArgs.builder().bucket(bucket).object(filename)
                            .stream(is, -1, 10485760).build()
                                                           );
        } catch (Exception e) {
            LOGGER.error("Failed to execute putObject", e);
        }

        return null;
    }
}