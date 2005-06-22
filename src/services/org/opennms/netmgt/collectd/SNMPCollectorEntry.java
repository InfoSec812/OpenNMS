//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2003 Jan 31: Cleaned up some unused imports.
//
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact: 
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
// Tab Size = 8
//

package org.opennms.netmgt.collectd;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.snmp.AbstractSnmpStore;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;
import org.opennms.netmgt.snmp.SnmpValue;

/**
 * <P>
 * The SNMPCollectorEntry class is designed to hold all SNMP collected data
 * pertaining to a particular interface.
 * </P>
 * 
 * <P>
 * An instance of this class is created by calling the constructor and passing a
 * list of SnmpVarBind objects from an SNMP PDU response. This class extends
 * java.util.TreeMap which is used to store each of the collected data points
 * indexed by object identifier.
 * </P>
 * 
 * @author <A>Jon Whetzel </A>
 * @author <A HREF="mailto:mike@opennms.org">Mike Davidson </A>
 */
public final class SNMPCollectorEntry extends AbstractSnmpStore {
    /**
     * The list of MIBObjects that will used for associating the the data within
     * the map.
     */
    private java.util.List m_objList;

    public SNMPCollectorEntry(List objList) {
        if (objList == null) throw new NullPointerException("objList is null!");
        m_objList = objList;
    }


    private Category log() {
        return ThreadCategory.getInstance(getClass());
    }
    
    private MibObject findMibObjectWitOid(SnmpObjId base) {
        for (Iterator it = m_objList.iterator(); it.hasNext();) {
            MibObject mibObj = (MibObject) it.next();
            if (base.equals(mibObj.getSnmpObjId()))
                return mibObj;
        }
        return null;
    }
    
    public void storeResult(SnmpObjId base, SnmpInstId inst, SnmpValue val) {
        String key = base.append(inst).toString();
        putValue(key, val);
        MibObject mibObject = findMibObjectWitOid(base);
        if (mibObject == null) throw new IllegalArgumentException("Received result for unexpected oid ["+base+"].["+inst+"]");
        if (mibObject.getInstance().equals(MibObject.INSTANCE_IFINDEX))
            putIfIndex(inst.toInt());
        log().debug("storeResult: added value for "+mibObject.getAlias()+": ["+base+"].["+inst+"] = "+val);
    }
}
