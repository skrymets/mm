/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.modes.ExtendedMapFeedback;

/**
 * Creates all XmlActors needed for the MindMapController
 *
 * @author foltin
 * {@code @date} 16.03.2014
 */
public class XmlActorFactory {

    private final ItalicNodeActor mActionActor;
    private final BoldNodeActor mBoldActor;
    private final StrikethroughNodeActor mStrikethroughActor;
    private final NewChildActor mNewChildActor;
    private final DeleteChildActor mDeleteChildActor;
    private final PasteActor mPasteActor;
    private final RemoveAllIconsActor mRemoveAllIconsActor;
    private final AddIconActor mAddIconActor;
    private final RemoveIconActor mRemoveIconActor;
    private final CloudActor mCloudActor;
    private final EdgeStyleActor mEdgeStyleActor;
    private final EdgeWidthActor mEdgeWidthActor;
    private final FontFamilyActor mFontFamilyActor;
    private final FontSizeActor mFontSizeActor;
    private final MoveNodeActor mMoveNodeActor;
    private final NodeStyleActor mNodeStyleActor;
    private final UnderlineActor mUnderlineActor;
    private final AddArrowLinkActor mAddArrowLinkActor;
    private final RemoveArrowLinkActor mRemoveArrowLinkActor;
    private final ChangeArrowLinkEndPointsActor mChangeArrowLinkEndPointsActor;
    private final ChangeArrowsInArrowLinkActor mChangeArrowsInArrowLinkActor;
    private final CloudColorActor mCloudColorActor;
    private final ColorArrowLinkActor mColorArrowLinkActor;
    private final EdgeColorActor mEdgeColorActor;
    private final EditActor mEditActor;
    private final NodeBackgroundColorActor mNodeBackgroundColorActor;
    private final NodeColorActor mNodeColorActor;
    private final AddHookActor mAddHookActor;
    private final NodeUpActor mNodeUpActor;
    private final RevertActor mRevertActor;
    private final ToggleFoldedActor mToggleFoldedActor;
    private final SetLinkActor mSetLinkActor;
    private final AddAttributeActor mAddAttributeActor;
    private final InsertAttributeActor mInsertAttributeActor;
    private final RemoveAttributeActor mRemoveAttributeActor;
    private final SetAttributeActor mSetAttributeActor;
    private final CutActor mCutActor;
    private final CompoundActor mCompoundActor;
    private final UndoPasteActor mUndoPasteActor;
    private final ChangeNoteTextActor mChangeNoteTextActor;

    public XmlActorFactory(ExtendedMapFeedback mapFeedback) {
        mActionActor = new ItalicNodeActor(mapFeedback);
        mBoldActor = new BoldNodeActor(mapFeedback);
        mStrikethroughActor = new StrikethroughNodeActor(mapFeedback);
        mNewChildActor = new NewChildActor(mapFeedback);
        mDeleteChildActor = new DeleteChildActor(mapFeedback);
        mPasteActor = new PasteActor(mapFeedback);
        mRemoveAllIconsActor = new RemoveAllIconsActor(mapFeedback);
        mAddIconActor = new AddIconActor(mapFeedback);
        mRemoveIconActor = new RemoveIconActor(mapFeedback);
        mCloudActor = new CloudActor(mapFeedback);
        mEdgeStyleActor = new EdgeStyleActor(mapFeedback);
        mEdgeWidthActor = new EdgeWidthActor(mapFeedback);
        mFontFamilyActor = new FontFamilyActor(mapFeedback);
        mFontSizeActor = new FontSizeActor(mapFeedback);
        mMoveNodeActor = new MoveNodeActor(mapFeedback);
        mNodeStyleActor = new NodeStyleActor(mapFeedback);
        mUnderlineActor = new UnderlineActor(mapFeedback);
        mAddArrowLinkActor = new AddArrowLinkActor(mapFeedback);
        mRemoveArrowLinkActor = new RemoveArrowLinkActor(mapFeedback);
        mChangeArrowLinkEndPointsActor = new ChangeArrowLinkEndPointsActor(mapFeedback);
        mChangeArrowsInArrowLinkActor = new ChangeArrowsInArrowLinkActor(mapFeedback);
        mCloudColorActor = new CloudColorActor(mapFeedback);
        mColorArrowLinkActor = new ColorArrowLinkActor(mapFeedback);
        mEdgeColorActor = new EdgeColorActor(mapFeedback);
        mEditActor = new EditActor(mapFeedback);
        mNodeBackgroundColorActor = new NodeBackgroundColorActor(mapFeedback);
        mNodeColorActor = new NodeColorActor(mapFeedback);
        mAddHookActor = new AddHookActor(mapFeedback);
        mNodeUpActor = new NodeUpActor(mapFeedback);
        mRevertActor = new RevertActor(mapFeedback);
        mToggleFoldedActor = new ToggleFoldedActor(mapFeedback);
        mSetLinkActor = new SetLinkActor(mapFeedback);
        mAddAttributeActor = new AddAttributeActor(mapFeedback);
        mInsertAttributeActor = new InsertAttributeActor(mapFeedback);
        mRemoveAttributeActor = new RemoveAttributeActor(mapFeedback);
        mSetAttributeActor = new SetAttributeActor(mapFeedback);
        mCompoundActor = new CompoundActor(mapFeedback);
        mCutActor = new CutActor(mapFeedback);
        mUndoPasteActor = new UndoPasteActor(mapFeedback);
        mChangeNoteTextActor = new ChangeNoteTextActor(mapFeedback);
    }

    public ItalicNodeActor getItalicActor() {
        return mActionActor;
    }

    public BoldNodeActor getBoldActor() {
        return mBoldActor;
    }

    public StrikethroughNodeActor getStrikethroughActor() {
        return mStrikethroughActor;
    }

    public NewChildActor getNewChildActor() {
        return mNewChildActor;
    }

    public DeleteChildActor getDeleteChildActor() {
        return mDeleteChildActor;
    }

    public PasteActor getPasteActor() {
        return mPasteActor;
    }

    public RemoveAllIconsActor getRemoveAllIconsActor() {
        return mRemoveAllIconsActor;
    }

    public AddIconActor getAddIconActor() {
        return mAddIconActor;
    }

    public RemoveIconActor getRemoveIconActor() {
        return mRemoveIconActor;
    }

    public CloudActor getCloudActor() {
        return mCloudActor;
    }

    public EdgeStyleActor getEdgeStyleActor() {
        return mEdgeStyleActor;
    }

    public EdgeWidthActor getEdgeWidthActor() {
        return mEdgeWidthActor;
    }

    public FontFamilyActor getFontFamilyActor() {
        return mFontFamilyActor;
    }

    public FontSizeActor getFontSizeActor() {
        return mFontSizeActor;
    }

    public MoveNodeActor getMoveNodeActor() {
        return mMoveNodeActor;
    }

    public NodeStyleActor getNodeStyleActor() {
        return mNodeStyleActor;
    }

    public UnderlineActor getUnderlineActor() {
        return mUnderlineActor;
    }

    public AddArrowLinkActor getAddArrowLinkActor() {
        return mAddArrowLinkActor;
    }

    public RemoveArrowLinkActor getRemoveArrowLinkActor() {
        return mRemoveArrowLinkActor;
    }

    public ChangeArrowLinkEndPointsActor getChangeArrowLinkEndPointsActor() {
        return mChangeArrowLinkEndPointsActor;
    }

    public ChangeArrowsInArrowLinkActor getChangeArrowsInArrowLinkActor() {
        return mChangeArrowsInArrowLinkActor;
    }

    public CloudColorActor getCloudColorActor() {
        return mCloudColorActor;
    }

    public ColorArrowLinkActor getColorArrowLinkActor() {
        return mColorArrowLinkActor;
    }

    public EdgeColorActor getEdgeColorActor() {
        return mEdgeColorActor;
    }

    public EditActor getEditActor() {
        return mEditActor;
    }

    public NodeBackgroundColorActor getNodeBackgroundColorActor() {
        return mNodeBackgroundColorActor;
    }

    public NodeColorActor getNodeColorActor() {
        return mNodeColorActor;
    }

    public AddHookActor getAddHookActor() {
        return mAddHookActor;
    }

    public NodeUpActor getNodeUpActor() {
        return mNodeUpActor;
    }

    public RevertActor getRevertActor() {
        return mRevertActor;
    }

    public ToggleFoldedActor getToggleFoldedActor() {
        return mToggleFoldedActor;
    }

    public SetLinkActor getSetLinkActor() {
        return mSetLinkActor;
    }

    public InsertAttributeActor getInsertAttributeActor() {
        return mInsertAttributeActor;
    }

    public AddAttributeActor getAddAttributeActor() {
        return mAddAttributeActor;
    }

    public RemoveAttributeActor getRemoveAttributeActor() {
        return mRemoveAttributeActor;
    }

    public SetAttributeActor getSetAttributeActor() {
        return mSetAttributeActor;
    }

    public CutActor getCutActor() {
        return mCutActor;
    }

    public CompoundActor getCompoundActor() {
        return mCompoundActor;
    }

    public UndoPasteActor getUndoPasteActor() {
        return mUndoPasteActor;
    }

    public ChangeNoteTextActor getChangeNoteTextActor() {
        return mChangeNoteTextActor;
    }

}
