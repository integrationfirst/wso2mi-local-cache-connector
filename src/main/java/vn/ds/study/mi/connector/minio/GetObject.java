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

import lombok.extern.slf4j.Slf4j;
import org.apache.synapse.MessageContext;
import org.infinispan.Cache;
import org.wso2.carbon.connector.core.ConnectException;
import vn.ds.study.mi.connector.minio.utils.CacheFactory;

@Slf4j
public class GetObject extends AbstractFunction {

    @Override
    protected void execute(final MessageContext messageContext) throws ConnectException {

        final String key = getParameterAsString("cachedKey");
        try {
            final Cache cache = CacheFactory.getCache("sample-cache");

            log.debug("Getting the cache by key = {}", key);
            messageContext.setProperty("cachedValue", cache.get(key));
            log.info("Get the object and put to the cachedValue property.");
        } catch (Exception e) {
            log.error("", e);
            throw new ConnectException(e, "Failed to download file. Detail: ");
        }
    }
}