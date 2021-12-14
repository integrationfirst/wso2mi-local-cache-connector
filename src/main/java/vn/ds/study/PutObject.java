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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PutObject extends MinioAgent {

    private static final Logger LOGGER= LoggerFactory.getLogger(PutObject.class);

    @Override
    public void connect(final MessageContext messageContext) throws ConnectException {
        super.connect(messageContext);
    }

    @Override
    public void execute(MessageContext messageContext) throws ConnectException {

        String address = getParameterAsString( "address");
        String bucket =  getParameterAsString("bucket");
        String filename =  getParameterAsString( "filename");
        String accessKey =  getParameterAsString( "accessKey");
        String secretKey =  getParameterAsString( "secretKey");

        LOGGER.info("Put object {} to OS address {}", filename, address);
        MinioClient client = getClient(address, accessKey,secretKey);

        if (client!=null){
            LOGGER.info("Successfully login into OS");
            String fileContent = getParameterAsString("fileContent");

            if (!StringUtils.isEmpty(fileContent)){
                try {
                    ObjectWriteResponse response = client.putObject(
                            PutObjectArgs.builder().bucket(bucket).object(filename)
                                    .stream(new ByteArrayInputStream(fileContent.getBytes()), -1, 10485760).build()
                                                                              );
                    LOGGER.info("Successfully putObject into OS");
                } catch (ErrorResponseException e) {
                    e.printStackTrace();
                } catch (InsufficientDataException e) {
                    e.printStackTrace();
                } catch (InternalException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidResponseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (ServerException e) {
                    e.printStackTrace();
                } catch (XmlParserException e) {
                    e.printStackTrace();
                }

            }

        }else {
            LOGGER.info("Failed to login into OS with access: {} and secret: {}", accessKey, secretKey);
        }

    }
}