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

package slash.navigation.catalog.local;

import slash.navigation.catalog.domain.Catalog;
import slash.navigation.catalog.domain.Category;

import java.io.File;

/**
 * Encapsulates access to the local file system.
 *
 * @author Christian Pesch
 */

public class LocalCatalog implements Catalog {
    private static final String USER_NAME = System.getProperty("user.name");
    private final String rootFolder;

    public LocalCatalog(String rootFolder) {
        this.rootFolder = rootFolder;
    }

    public Category getRootCategory() {
        return new LocalCategory(this, new File(rootFolder));
    }

    public String getUserName() {
        return USER_NAME;
    }
}
