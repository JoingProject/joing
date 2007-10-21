/*
 * AppsByGroup.java
 *
 * Created on 6 de junio de 2007, 12:55
 *
 * Copyright (C) 2007 Francisco Morero Peyrona
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.joing.common.ejb.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Francisco Morero Peyrona
 */
public class AppsByGroup implements Serializable {

    private String description;
    private byte[] iconPNG;
    private byte[] iconSVG;

    private List<AppDescriptor> apps;

    /**
     * Creates a new instance of AppsByGroup
     */
    public AppsByGroup() {
        this(null);
    }

    /**
     * Creates a new instance of AppsByGroup
     */
    public AppsByGroup(String sDescription) {
        this.description = sDescription;
        this.iconPNG = null;
        this.iconSVG = null;
        this.apps = new ArrayList<AppDescriptor>();
    }

    public String getDescription() {
        return description;
    }

    public byte[] getIconPNG() {
        return iconPNG;
    }

    public byte[] getIconSVG() {
        return iconSVG;
    }

    public List<AppDescriptor> getApplications() {
        return apps;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconPNG(byte[] icon) {
        if (icon != null) {
            byte[] copy = new byte[icon.length];
            System.arraycopy(icon, 0, copy, 0, icon.length); // defensive copy
            iconPNG = copy;
        } else {
            iconPNG = null;
        }
    }

    public void setIconSVG(byte[] icon) {
        if (icon != null) {
            byte[] copy = new byte[icon.length];
            System.arraycopy(icon, 0, copy, 0, icon.length); // defensive copy
            iconSVG = copy;
        } else {
            iconSVG = null;
        }
    }

    public void addApplication(AppDescriptor app) {
        apps.add(app);
    }

    public void addApplications(List<AppDescriptor> apps) {
        if (apps != null) {
            this.apps = apps;
        }
    }
}