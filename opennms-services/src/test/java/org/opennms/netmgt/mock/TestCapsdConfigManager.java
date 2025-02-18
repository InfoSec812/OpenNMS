/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.mock;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.CapsdConfigManager;

public class TestCapsdConfigManager extends CapsdConfigManager {
    private String m_xml;

    public TestCapsdConfigManager(String xml) throws MarshalException, ValidationException, IOException {
        super(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        save();
    }

    @Override
    protected void saveXml(String xml) throws IOException {
        m_xml = xml;
    }

    @Override
    protected void update() throws IOException, FileNotFoundException, MarshalException, ValidationException {
        loadXml(new ByteArrayInputStream(m_xml.getBytes("UTF-8")));
    }
    
    public String getXml() {
        return m_xml;
    }
    
}