package freemind.controller;

import freemind.controller.actions.MindmapLastStateMapStorage;
import freemind.controller.actions.MindmapLastStateStorage;
import freemind.controller.actions.NodeListMember;
import freemind.controller.actions.XmlAction;
import freemind.main.Tools;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Slf4j
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
            log.error(e.getLocalizedMessage(), e);
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
        for (MindmapLastStateStorage store : emptyIfNull(mLastStatesMap.getMindmapLastStateStorageList())) {
            if (Objects.equals(pStore.getRestorableName(), store.getRestorableName())) {
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
            // Ensure list is initialized (JiBX may leave it null for optional empty collections)
            if (mLastStatesMap.getMindmapLastStateStorageList() == null) {
                mLastStatesMap.setMindmapLastStateStorageList(new ArrayList<>());
            }
            mLastStatesMap.addMindmapLastStateStorage(pStore);
        }
        // size limit
        if (mLastStatesMap.sizeMindmapLastStateStorageList() > LIST_AMOUNT_LIMIT) {
            // make map from date to object:
            TreeMap<Long, MindmapLastStateStorage> dateToStoreMap = new TreeMap<>();
            for (MindmapLastStateStorage store : emptyIfNull(mLastStatesMap.getMindmapLastStateStorageList())) {
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
        for (MindmapLastStateStorage store : emptyIfNull(mLastStatesMap.getMindmapLastStateStorageList())) {
            if (Objects.equals(pRestorableName, store.getRestorableName())) {
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
