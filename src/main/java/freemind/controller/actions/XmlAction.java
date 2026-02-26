
package freemind.controller.actions;

import jakarta.xml.bind.annotation.*;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="xml_action"/>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({
    // Actions (from freemind_actions.xsd)
    CompoundAction.class, SelectNodeAction.class, CutNodeAction.class,
    PasteNodeAction.class, UndoPasteNodeAction.class, RevertXmlAction.class,
    BoldNodeAction.class, StrikethroughNodeAction.class, ItalicNodeAction.class,
    UnderlinedNodeAction.class, FontSizeNodeAction.class, FontNodeAction.class,
    NodeColorFormatAction.class, NodeBackgroundColorFormatAction.class,
    NodeStyleFormatAction.class, EdgeColorFormatAction.class,
    EdgeWidthFormatAction.class, EdgeStyleFormatAction.class,
    DeleteNodeAction.class, EditNodeAction.class, NewNodeAction.class,
    FoldAction.class, MoveNodesAction.class, HookNodeAction.class,
    AddIconAction.class, RemoveIconXmlAction.class, RemoveAllIconsXmlAction.class,
    MoveNodeXmlAction.class, AddCloudXmlAction.class, CloudColorXmlAction.class,
    AddArrowLinkXmlAction.class, AddLinkXmlAction.class,
    RemoveArrowLinkXmlAction.class, ArrowLinkColorXmlAction.class,
    ArrowLinkArrowXmlAction.class, ArrowLinkPointXmlAction.class,
    SetAttributeAction.class, InsertAttributeAction.class,
    AddAttributeAction.class, RemoveAttributeAction.class,
    EditNoteToNodeAction.class, PlaceNodeXmlAction.class,
    ChangeRootNodeAction.class,
    // Window storage
    NormalWindowConfigurationStorage.class, OptionPanelWindowConfigurationStorage.class,
    MapWindowConfigurationStorage.class, TimeWindowConfigurationStorage.class,
    ScriptEditorWindowConfigurationStorage.class, ManageStyleEditorWindowConfigurationStorage.class,
    LogFileViewerConfigurationStorage.class,
    // Patterns
    Pattern.class, Patterns.class,
    // State
    MindmapLastStateStorage.class, MindmapLastStateMapStorage.class,
    NodeList.class,
    // Collaboration
    CollaborationAction.class,
    CollaborationHello.class, CollaborationWhoAreYou.class,
    CollaborationWelcome.class, CollaborationUserInformation.class,
    CollaborationPublishNewMap.class, CollaborationGetOffers.class,
    CollaborationOffers.class, CollaborationTransaction.class,
    CollaborationReceiveLock.class, CollaborationGoodbye.class,
    CollaborationRequireLock.class, CollaborationUnableToLock.class,
    CollaborationWrongCredentials.class, CollaborationWrongMap.class,
    // Other
    Place.class, AttributeTableProperties.class,
    Searchresults.class, Reversegeocode.class,
    // Transferable
    TransferableContent.class, TransferableFile.class,
    // Intermediate types
    NodeAction.class, FormatNodeAction.class, TextNodeAction.class,
    WindowConfigurationStorage.class, CollaborationActionBase.class
})
public class XmlAction
{
}
