/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package freemind.controller;

import freemind.controller.actions.generated.instance.MindmapLastStateMapStorage;
import freemind.controller.actions.generated.instance.MindmapLastStateStorage;
import freemind.controller.actions.generated.instance.NodeListMember;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.main.Tools;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * @author foltin
 */
@Log4j2
public class LastStateStorageManagement {
    public static final int LIST_AMOUNT_LIMIT = 50;
    private MindmapLastStateMapStorage mLastStatesMap = null;

    public LastStateStorageManagement(String pXml) {
        try {
            XmlAction action = Tools.unMarshall(pXml);
            if (action != null) {
                if (action instanceof MindmapLastStateMapStorage) {
                    mLastStatesMap = (MindmapLastStateMapStorage) action;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

        if (mLastStatesMap == null) {
            log.warn("Creating a new last state map storage as there was no old one or it was corrupt.");
            mLastStatesMap = new MindmapLastStateMapStorage();
        }
    }

    public String getXml() {
        return Tools.marshall(mLastStatesMap);
    }

    public void clearTabIndices() {
        for (MindmapLastStateStorage store : emptyIfNull(mLastStatesMap.getMindmapLastStateStorageList())) {
            store.setTabIndex(-1);
        }
    }

    public void changeOrAdd(MindmapLastStateStorage pStore) {
        boolean found = false;
        for (MindmapLastStateStorage store : mLastStatesMap.getMindmapLastStateStorageList()) {
            if (Tools.safeEquals(pStore.getRestorableName(), store.getRestorableName())) {
                // deep copy
                store.setLastZoom(pStore.getLastZoom());
                store.setLastSelected(pStore.getLastSelected());
                store.setX(pStore.getX());
                store.setY(pStore.getY());
                List<NodeListMember> listCopy = new ArrayList<>(pStore.getNodeListMemberList());
                store.clearNodeListMemberList();
                for (NodeListMember member : listCopy) {
                    store.addNodeListMember(member);
                }
                found = true;
                setLastChanged(store);
                break;
            }
        }
        if (!found) {
            setLastChanged(pStore);
            mLastStatesMap.addMindmapLastStateStorage(pStore);
        }
        // size limit
        if (mLastStatesMap.sizeMindmapLastStateStorageList() > LIST_AMOUNT_LIMIT) {
            // make map from date to object:
            TreeMap<Long, MindmapLastStateStorage> dateToStoreMap = new TreeMap<>();
            for (MindmapLastStateStorage store : mLastStatesMap.getMindmapLastStateStorageList()) {
                dateToStoreMap.put(Long.valueOf(-store.getLastChanged()), store);
            }
            // clear list
            mLastStatesMap.clearMindmapLastStateStorageList();
            // rebuild
            int counter = 0;
            for (Entry<Long, MindmapLastStateStorage> entry : dateToStoreMap.entrySet()) {
                mLastStatesMap.addMindmapLastStateStorage(entry.getValue());
                counter++;
                if (counter >= LIST_AMOUNT_LIMIT) {
                    // drop the rest of the elements.
                    break;
                }
            }
        }
    }

    private void setLastChanged(MindmapLastStateStorage pStore) {
        pStore.setLastChanged(System.currentTimeMillis());
    }

    public MindmapLastStateStorage getStorage(String pRestorableName) {
        for (MindmapLastStateStorage store : mLastStatesMap.getMindmapLastStateStorageList()) {
            if (Tools.safeEquals(pRestorableName, store.getRestorableName())) {
                setLastChanged(store);
                return store;
            }
        }
        return null;
    }

    public List<MindmapLastStateStorage> getLastOpenList() {
        return emptyIfNull(mLastStatesMap.getMindmapLastStateStorageList())
                .stream()
                .filter(storage -> storage.getTabIndex() >= 0)
                .sorted(comparingInt(MindmapLastStateStorage::getTabIndex))
                .collect(toList());

    }

    public int getLastFocussedTab() {
        return mLastStatesMap.getLastFocusedTab();
    }

    public void setLastFocussedTab(int pIndex) {
        mLastStatesMap.setLastFocusedTab(pIndex);
    }

}
