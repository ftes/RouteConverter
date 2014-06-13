/*
    This file is part of RouteConverter.

    RouteConverter is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RouteConverter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RouteConverter; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Copyright (C) 2007 Christian Pesch. All Rights Reserved.
*/
package slash.navigation.download;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import slash.navigation.download.actions.Checksum;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io.*;

import static java.io.File.createTempFile;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static slash.common.io.InputOutput.readBytes;
import static slash.common.io.Transfer.UTF8_ENCODING;
import static slash.common.type.CompactCalendar.fromMillis;
import static slash.navigation.download.Action.*;
import static slash.navigation.download.DownloadManager.WAIT_TIMEOUT;
import static slash.navigation.download.State.*;

public class DownloadManagerIT {
    private static final String DOWNLOAD = System.getProperty("download", "http://static.routeconverter.com/download/test/");
    private static final String LOREM_IPSUM_DOLOR_SIT_AMET = "Lorem ipsum dolor sit amet";
    private static final String EXPECTED = LOREM_IPSUM_DOLOR_SIT_AMET + ", consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\n";

    private DownloadManager manager;
    private File target, queueFile;

    private String readFileToString(File file) throws IOException {
        return new String(readBytes(new FileInputStream(file)), UTF8_ENCODING);
    }

    private void writeStringToFile(File file, String string) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new FileOutputStream(file));
        writer.print(string);
        writer.close();
        assertEquals(string.length(), file.length());
    }

    private void delete(String path) {
        File toDelete = new File(target.getParentFile(), path);
        if (toDelete.exists())
            assertTrue(toDelete.delete());
    }

    @Before
    public void setUp() throws IOException {
        queueFile = createTempFile("queueFile", ".xml");
        manager = new DownloadManager(queueFile);
        target = createTempFile("local", ".txt");
        delete("first/second/447bytes.txt");
        delete("first/second");
        delete("first");
        delete("447bytes.txt");
    }

    @After
    public void tearDown() {
        if (target.exists())
            assertTrue(target.delete());
        if (queueFile.exists())
            assertTrue(queueFile.delete());
        manager.dispose();
    }

    private static final Object LOCK = new Object();

    void waitFor(final Download download, final State expectedState) {
        final boolean[] found = new boolean[1];
        found[0] = false;

        TableModelListener l = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (expectedState.equals(download.getState())) {
                    synchronized (LOCK) {
                        found[0] = true;
                        LOCK.notifyAll();
                    }
                }
            }
        };

        long start = currentTimeMillis();
        manager.getModel().addTableModelListener(l);
        try {
            while (true) {
                synchronized (LOCK) {
                    if (found[0])
                        break;
                    if (currentTimeMillis() - start > WAIT_TIMEOUT)
                        throw new IllegalStateException("waited for " + WAIT_TIMEOUT + " seconds without seeing " + expectedState);
                    try {
                        LOCK.wait(1000);
                    } catch (InterruptedException e) {
                        // intentionally left empty
                    }
                }
            }
        } finally {
            manager.getModel().removeTableModelListener(l);
        }
    }

    @Test
    public void testInvalidUrl() throws IOException {
        Download download = manager.queueForDownload("Does not exist", DOWNLOAD + "doesntexist.txt", null, Copy, target);
        waitFor(download, Failed);

        assertEquals(Failed, download.getState());
    }

    @Test
    public void testFreshDownload() throws IOException {
        assertTrue(target.delete());

        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", null, Copy, target);
        waitFor(download, Succeeded);

        assertEquals(Succeeded, download.getState());
        String actual = readFileToString(target);
        assertEquals(EXPECTED, actual);
    }

    @Test
    public void testDownloadWithCorrectChecksum() throws IOException {
        assertTrue(target.delete());

        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", new Checksum("597D5107C0DC296DF4F6128257F6F8D2079FA11A", 447L, fromMillis(1387698493000L)), Copy, target);
        waitFor(download, Succeeded);

        assertEquals(Succeeded, download.getState());
        String actual = readFileToString(target);
        assertEquals(EXPECTED, actual);
    }

    @Test
    public void testDownloadWithWrongSize() throws IOException {
        assertTrue(target.delete());

        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", new Checksum("597D5107C0DC296DF4F6128257F6F8D2079FA11A", 4711L, fromMillis(1387698493000L)), Copy, target);
        waitFor(download, Downloading);
        waitFor(download, ChecksumError);

        assertEquals(ChecksumError, download.getState());
    }

    @Test
    public void testDownloadWithWrongChecksum() throws IOException {
        assertTrue(target.delete());

        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", new Checksum("notdefined", 447L, fromMillis(1387698493000L)), Copy, target);
        waitFor(download, Downloading);
        waitFor(download, ChecksumError);

        assertEquals(ChecksumError, download.getState());
    }

    @Test
    public void testDownloadWithWrongTimestamp() throws IOException {
        assertTrue(target.delete());

        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", new Checksum("597D5107C0DC296DF4F6128257F6F8D2079FA11A", 447L, fromMillis(0)), Copy, target);
        waitFor(download, Downloading);
        waitFor(download, ChecksumError);

        assertEquals(ChecksumError, download.getState());
    }

    @Test
    public void testResumeDownload() throws IOException {
        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", null, Copy, target);
        // write content to temp file and patch download object
        writeStringToFile(download.getTempFile(), LOREM_IPSUM_DOLOR_SIT_AMET);
        download.setLastModified(fromMillis(download.getTempFile().lastModified()));
        download.setContentLength(447L);

        waitFor(download, Succeeded);

        assertEquals(Succeeded, download.getState());
        String actual = readFileToString(target);
        assertEquals(EXPECTED, actual);
    }

    @Test
    public void testNotModifiedDownload() throws IOException {
        Download download = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", null, Copy, target);
        // write content to temp file and patch download object
        writeStringToFile(download.getTempFile(), EXPECTED);
        download.setLastModified(fromMillis(download.getTempFile().lastModified()));
        download.setContentLength(download.getTempFile().length());

        waitFor(download, Succeeded);

        assertEquals(Succeeded, download.getState());
        String actual = readFileToString(target);
        assertEquals(EXPECTED, actual);
    }

    @Test
    public void testDownloadSameUrl() throws IOException {
        writeStringToFile(target, LOREM_IPSUM_DOLOR_SIT_AMET);

        Download download1 = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", null, Copy, target);
        Download download2 = manager.queueForDownload("447 Bytes", DOWNLOAD + "447bytes.txt", null, Copy, target);
        waitFor(download2, Succeeded);

        assertEquals(download2, download1);
    }

    @Test
    public void testDownloadAndFlatten() throws IOException {
        File extracted = new File(target.getParentFile(), "447bytes.txt");

        try {
            Download download = manager.queueForDownload("447 Bytes in a ZIP", DOWNLOAD + "447bytes.zip", null, Flatten, target.getParentFile());
            waitFor(download, Processing);
            assertEquals(Processing, download.getState());

            waitFor(download, Succeeded);
            assertEquals(Succeeded, download.getState());

            assertTrue(extracted.exists());
            String actual = readFileToString(extracted);
            assertEquals(EXPECTED, actual);
        } finally {
            if (extracted.exists())
                assertTrue(extracted.delete());
        }
    }

    @Test
    public void testDownloadAndExtract() throws IOException {
        File extracted = new File(target.getParentFile(), "first/second/447bytes.txt");

        try {
            Download download = manager.queueForDownload("447 Bytes in a ZIP", DOWNLOAD + "447bytes.zip", null, Extract, target.getParentFile());
            waitFor(download, Processing);
            assertEquals(Processing, download.getState());

            waitFor(download, Succeeded);
            assertEquals(Succeeded, download.getState());

            assertTrue(extracted.exists());
            String actual = readFileToString(extracted);
            assertEquals(EXPECTED, actual);
        } finally {
            if (extracted.exists())
                assertTrue(extracted.delete());
        }
    }
}
