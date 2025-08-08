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
package vn.ds.study.mi.connector.minio;

import lombok.extern.slf4j.Slf4j;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.infinispan.Cache;
import org.wso2.carbon.connector.core.ConnectException;
import vn.ds.study.mi.connector.minio.utils.CacheFactory;

import javax.activation.DataHandler;
import java.io.IOException;
import java.util.Optional;


@Slf4j
public class PutObject extends AbstractFunction {

    @Override
    public void execute(MessageContext messageContext) throws ConnectException {

        Axis2MessageContext context = (Axis2MessageContext) messageContext;

        final String key = getParameterAsString("cachedKey");
        final Cache cache = CacheFactory.getCache("sample-cache");

        log.info("Put object key = {}", key);

        Optional.of(context)
                .map(Axis2MessageContext::getEnvelope)
                .map(SOAPEnvelope::getBody)
                .map(OMElement::getFirstElement)
                .map(OMContainer::getFirstOMChild)
                .map(OMText.class::cast)
                .map(OMText::getDataHandler)
                .map(DataHandler.class::cast)
                .map(this::inputStream)
                .map(is -> cache.put(key, is))
                .ifPresent(res -> {
                    log.info("Put object successfully {}", key);
                });
    }

    private byte[] inputStream(DataHandler dataHandler) {
        try {
            return dataHandler.getInputStream()
                              .readAllBytes();
        } catch (IOException e) {
            log.error("Failed to obtain input stream from Envelope element", e);
        }

        return null;
    }
}