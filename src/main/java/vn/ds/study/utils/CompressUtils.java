/*
 * Class: CompressUtils
 *
 * Created on Feb 18, 2022
 *
 * (c) Copyright Swiss Post Solutions Ltd, unpublished work
 * All use, disclosure, and/or reproduction of this material is prohibited
 * unless authorized in writing.  All Rights Reserved.
 * Rights in this program belong to:
 * Swiss Post Solution.
 * Floor 4-5-8, ICT Tower, Quang Trung Software City
 */
package vn.ds.study.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;

public final class CompressUtils {

    private CompressUtils() {
    }

    public static ByteArrayOutputStream decompressBytes(final InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try (GZIPInputStream gZIPInputStream = new GZIPInputStream(input);) {
            IOUtils.copy(gZIPInputStream, arrayOutputStream);
        }
        InputStream inputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());

        try (TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(inputStream);) {
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tarArchiveInputStream.getNextTarEntry()) != null) {
                if (!tarEntry.isDirectory()) {
                    IOUtils.copy(tarArchiveInputStream, output);
                    break;
                }
            }
        }
        return output;
    }
}