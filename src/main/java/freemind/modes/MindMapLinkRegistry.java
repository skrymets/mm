package freemind.modes;

import freemind.main.MindMapUtils;
import freemind.model.LinkAdapter;
import freemind.model.MindMapLink;
import freemind.model.MindMapNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

/**
 * Interface for the registry, which manages the ids of nodes and the existing
 * links in a map. Thus, this interface is bound to a map model, because other
 * maps have a different registry.
 */

@Slf4j
public class MindMapLinkRegistry {
    /**
     * All elements put into this sort of vectors are put into the
     * SourceToLinks, too. This structure is kept synchronous to the IDToLinks
     * structure, but reversed.
     */
    @SuppressWarnings("serial")
    private class SynchronousLinksList extends ArrayList<MindMapLink> {

        @Override
        public boolean add(MindMapLink pE) {
            boolean added = super.add(pE);
            if (pE instanceof MindMapLink) {
                MindMapLink link = pE;
                MindMapNode source = link.getSource();
                if (!mSourceToLinks.containsKey(source)) {
                    mSourceToLinks.put(source, new ArrayList<>());
                }
                mSourceToLinks.get(source).add(pE);
            }
            return added;
        }

        @Override
        public MindMapLink remove(int pIndex) {
            MindMapLink link = get(pIndex);
            MindMapNode source = link.getSource();
            List<MindMapLink> list = mSourceToLinks.get(source);
            if (list != null) {
                list.remove(link);
                if (list.isEmpty()) {
                    mSourceToLinks.remove(source);
                }
            }
            return super.remove(pIndex);
        }

    }

    /**
     * source -> vector of links with same source
     */
    protected final HashMap<MindMapNode, List<MindMapLink>> mSourceToLinks = new HashMap<>();

    // //////////////////////////////////////////////////////////////////////////////////////
    // // Attributes /////
    // //////////////////////////////////////////////////////////////////////////////////////

    /**
     * MindMapNode = Target -> ID.
     */
    protected final HashMap<MindMapNode, String> mTargetToId;
    /**
     * MindMapNode-> ID.
     */
    protected final HashMap<String, MindMapNode> mIdToTarget;
    /**
     * id -> vector of links whose TargetToID.get(target) == id.
     */
    protected final HashMap<String, List<MindMapLink>> mIdToLinks;
    /**
     * id -> link
     */
    protected final HashMap<String, MindMapLink> mIdToLink;
    /**
     * id
     */
    protected final HashSet<String> mLocallyLinkedIds;

    public MindMapLinkRegistry(/* MindMap map */) {
        mTargetToId = new HashMap<>();
        mIdToTarget = new HashMap<>();
        mIdToLinks = new HashMap<>();
        mIdToLink = new HashMap<>();
        mLocallyLinkedIds = new HashSet<>();
    }

    /**
     * This can be used, if the id has to be known, before a node can be
     * labeled.
     */
    public String generateUniqueID(String proposedID) {
        return MindMapUtils.generateID(proposedID, mIdToLinks, "ID_");
    }

    /**
     * This can be used, if the id has to be known, before a link can be labled.
     */
    public String generateUniqueLinkId(String proposedID) {
        return MindMapUtils.generateID(proposedID, mIdToLink, "Arrow_ID_");
    }

    public String registerLinkTarget(MindMapNode pTarget) {
        return _registerLinkTarget(pTarget);

    }

    /**
     * The second variant of the main method. The difference is that here an ID
     * is proposed, but has not to be taken, though.
     */
    public String registerLinkTarget(MindMapNode pTarget, String pProposedID) {
        return _registerLinkTarget(pTarget, pProposedID);
    }

    /**
     * The main method. Registeres a node with a new (or an existing) node-id.
     */
    public String _registerLinkTarget(MindMapNode target) {
        return _registerLinkTarget(target, null);
    }

    public String _registerLinkTarget(MindMapNode target, String proposedID) {
        // id already exists?
        if (mTargetToId.containsKey(target)) {
            String id = mTargetToId.get(target);
            if (id != null)
                return id;
            // blank state.
            // is equal to no state.
        }
        // generate new id:
        String newId = generateUniqueID(proposedID);
        mTargetToId.put(target, newId);
        mIdToTarget.put(newId, target);

        // log.trace("Register target node:"+target+", with ID="+newID);
        /*
         * This is to allocate the link target in the IDToLinks map!.
         */
        getAssignedLinksVector(newId);
        return newId;
    }

    /**
     * @return null, if not registered.
     */
    public String getState(MindMapNode node) {
        if (mTargetToId.containsKey(node))
            return mTargetToId.get(node);
        return null;
    }

    /**
     * Reverses the getLabel method: searches for a node with the id given as
     * the argument.
     */
    public MindMapNode getTargetForId(String ID) {
        return mIdToTarget.get(ID);
    }

    /**
     * @return a Vector of {@link MindMapLink}s
     */
    private List<MindMapLink> getAssignedLinksVector(String newId) {
        String id = newId;
        // look, if target is already present:
        List<MindMapLink> vec;
        if (mIdToLinks.containsKey(id)) {
            vec = mIdToLinks.get(id);
        } else {
            vec = new SynchronousLinksList();
            mIdToLinks.put(id, vec);
        }

        // Dimitry : log is a performance killer here
        // //log.trace("getAssignedLinksVector "+vec);
        return vec;
    }

    /**
     * If there are still targets registered, they are removed, too.
     */
    public void deregisterLinkTarget(MindMapNode target)
            throws java.lang.IllegalArgumentException {
        // deregister all links :
        List<MindMapLink> links = getAllLinks(target);
        for (int i = links.size() - 1; i >= 0; --i) {
            MindMapLink link = links.get(i);
            deregisterLink(link);
        }
        // and process my sons:
        for (ListIterator<MindMapNode> e = target.childrenUnfolded(); e.hasNext(); ) {
            MindMapNode child = e.next();
            deregisterLinkTarget(child);
        }
        String id = getState(target);
        if (id != null) {
            // log.trace("Deregister target node:"+target);
            mTargetToId.remove(target);
            mIdToTarget.remove(id);
            mIdToLinks.remove(id);
        }
    }

    /**
     * Method to keep track of the sources associated to a target node. This
     * method also sets the new id to the target. Moreover, it is not required
     * that the target node is already registered. This will be done on the fly.
     */
    public void registerLink(MindMapLink link)
            throws java.lang.IllegalArgumentException {
        if ((link.getSource() == null) || (link.getTarget() == null)
                || (link.getDestinationLabel() == null))
            throw new java.lang.IllegalArgumentException(
                    "Illegal link specification." + link);
        MindMapNode source = link.getSource();
        MindMapNode target = link.getTarget();
        log.trace("Register link ({}) from source node:{} to target {}", link, source, target);
        String id = _registerLinkTarget(target);
        List<MindMapLink> vec = getAssignedLinksVector(id);
        // already present?
        for (MindMapLink mindMapLink : vec) {
            if (mindMapLink == link)
                return;
        }
        vec.add(link);
        String uniqueId = link.getUniqueId();
        if (uniqueId == null) {
            ((LinkAdapter) link).setUniqueId(generateUniqueLinkId(uniqueId));
            uniqueId = link.getUniqueId();
        }
        if (mIdToLink.containsKey(uniqueId)) {
            if (mIdToLink.get(uniqueId) != link) {
                log.warn("link with duplicated unique id found:{}", link);
                // new id:
                ((LinkAdapter) link)
                        .setUniqueId(generateUniqueLinkId(uniqueId));
            }
        }
        mIdToLink.put(link.getUniqueId(), link);
    }

    public void deregisterLink(MindMapLink link) {
        MindMapNode target = link.getTarget();
        String id = _registerLinkTarget(target);
        List<MindMapLink> vec = getAssignedLinksVector(id);
        for (int i = vec.size() - 1; i >= 0; --i) {
            // log.trace("Test for equal node:"+source+" to vector(i) " +
            // vec.get(i));
            if (vec.get(i) == link) {
                vec.remove(i);
                log.info("Deregister link  ({}) from source node:{} to target {}", link, link.getSource(), target);
            }
        }
        mIdToLink.remove(link.getUniqueId());
    }

    /**
     * Reverses the getUniqueID method: searches for a link with the id given as
     * the argument.
     */
    public MindMapLink getLinkForId(String pId) {
        if (mIdToLink.containsKey(pId)) {
            return mIdToLink.get(pId);
        }
        return null;
    }

    /**
     * @return Returns a Vector of {@link MindMapNode}s that point to the given
     * target node.
     */
    public List<MindMapNode> getAllSources(MindMapNode target) {
        List<MindMapNode> returnValue;
        returnValue = new ArrayList<>();
        String id = getState(target);
        if (id != null) {
            List<MindMapLink> vec = getAssignedLinksVector(id);
            for (MindMapLink mindMapLink : vec) {
                returnValue.add(mindMapLink.getSource());
            }
        }
        return returnValue;
    }

    /**
     * @return returns all links from or to this node.
     */
    public List<MindMapLink> getAllLinks(MindMapNode node) {
        List<MindMapLink> returnValue = new ArrayList<>();
        returnValue.addAll(getAllLinksIntoMe(node));
        returnValue.addAll(getAllLinksFromMe(node));
        // Dimitry : log is a performance killer here
        // //log.trace("All links  ("+returnValue+") from  node:"+node);
        return returnValue;
    }

    /**
     * @return returns all links to this node as {@link MindMapLink} vector.
     */
    public List<MindMapLink> getAllLinksIntoMe(MindMapNode target) {
        List<MindMapLink> returnValue = new ArrayList<>();
        String id = getState(target);
        if (id != null) {
            List<MindMapLink> vec = getAssignedLinksVector(id);
            /* "clone" */
            returnValue.addAll(vec);
        }
        return returnValue;
    }

    /**
     * @return returns all links from this node as {@link MindMapLink} vector.
     */
    public List<MindMapLink> getAllLinksFromMe(MindMapNode source) {
        List<MindMapLink> returnValue = new ArrayList<>();
        Collection<MindMapLink> vec = mSourceToLinks.get(source);
        if (vec != null) {
            returnValue.addAll(vec);
        }
        return returnValue;
    }

    public String getLabel(MindMapNode target) {
        return getState(target);
    }

    public void registerLocalHyperlinkId(String pTargetId) {
        mLocallyLinkedIds.add(pTargetId);
    }

    public boolean isTargetOfLocalHyperlinks(String pTargetId) {
        return mLocallyLinkedIds.contains(pTargetId);
    }
}
