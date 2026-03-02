/*
 * Created on 05.05.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.model.MindMapNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;

public interface Condition {
    boolean checkNode(Controller c, MindMapNode node);

    JComponent getListCellRendererComponent();

    void save(Document doc, Element parent);
}
