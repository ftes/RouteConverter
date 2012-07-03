package slash.navigation.catalog.domain;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.apache.commons.io.IOUtils.copy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FilesIT extends RouteCatalogServiceBase {

    @Test
    public void testAddFileWithUmlauts() throws Exception {
        String name = "Category for files with umlauts " + System.currentTimeMillis();
        Category root = catalog.getRootCategory();
        Category category = root.create(name);

        File in = File.createTempFile("äöüß", ".file");
        FileInputStream input = new FileInputStream(new File(TEST_PATH + "filestest.gpx"));
        FileOutputStream output = new FileOutputStream(in);
        copy(input, output);
        closeQuietly(input);
        closeQuietly(output);
        long inLength = in.length();

        Route route = category.createRoute("File with umlauts", in);
        assertNotNull(route);

        assertTrue(in.delete());

        InputStream out = route.getDataUrl().openStream();
        assertEquals(inLength, out.available());
    }
}
