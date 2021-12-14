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
import org.apache.synapse.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.connector.core.ConnectException;

public class PutObject extends MinioAgent {

    private static final Logger LOGGER= LoggerFactory.getLogger(PutObject.class);

    @Override
    public void connect(MessageContext messageContext) throws ConnectException {


        String address = (String) getParameter(messageContext, "address");
        String filename = (String) getParameter(messageContext, "filename");
        String accessKey = (String) getParameter(messageContext, "accessKey");
        String secretKey = (String) getParameter(messageContext, "secretKey");

        MinioClient client = getClient(address, "minio","minio123");
        LOGGER.info("Put object {} to OS address {}", filename, address);

        if (client!=null){
            LOGGER.info("Successfully login into OS");
        }else {
            LOGGER.info("Failed to login into OS with access: {} and secret: {}", accessKey, secretKey);
        }

    }
}