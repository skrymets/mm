# Phase 3: Dependency Upgrades & Dead Code Removal - Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Remove dead dependencies, decompose Tools.java, replace XMLElement with DOM API, integrate core search, and audit dependencies.

**Architecture:** Bottom-up mechanical approach. Section 1 removes dead deps and adds core search. Section 2 decomposes Tools.java into focused utility classes using Apache Commons where possible. Section 3 replaces XMLElement with JDK DOM API. Section 4 audits remaining deps.

**Tech Stack:** Java 17, Maven, Apache Commons (lang3 3.12.0, io 2.14.0), JDK DOM API (javax.xml.parsers), Guice 7.0.0

---

## Section 1: Dead Dependency Removal + Core Search

### Task 1: Remove dead dependencies from pom.xml

**Files:**
- Modify: `pom.xml` (lines 18, 31-52)
- Delete: `src/main/resources/plugins/Search.xml.disabled`

**Step 1: Remove Lucene and Groovy dependencies**

Remove the lucene.version property (line 18) and all 4 dependency blocks:

```xml
<!-- REMOVE these from pom.xml: -->

<!-- Line 18: remove property -->
<lucene.version>4.6.0</lucene.version>

<!-- Lines 31-46: remove all three Lucene deps -->
<dependency>
    <groupId>org.apache.lucene</groupId>
    <artifactId>lucene-core</artifactId>
    <version>${lucene.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.lucene</groupId>
    <artifactId>lucene-queryparser</artifactId>
    <version>${lucene.version}</version>
</dependency>
<dependency>
    <groupId>org.apache.lucene</groupId>
    <artifactId>lucene-analyzers-common</artifactId>
    <version>${lucene.version}</version>
</dependency>

<!-- Lines 48-52: remove Groovy -->
<dependency>
    <groupId>org.codehaus.groovy</groupId>
    <artifactId>groovy-all</artifactId>
    <version>2.4.21</version>
</dependency>
```

**Step 2: Delete disabled plugin config**

Delete `src/main/resources/plugins/Search.xml.disabled`

**Step 3: Build and verify**

Run: `mvn clean compile`
Expected: BUILD SUCCESS (no main source uses these deps)

Run: `mvn test`
Expected: 99 tests pass

**Step 4: Commit**

```bash
git add pom.xml
git rm src/main/resources/plugins/Search.xml.disabled
git commit -m "Remove dead dependencies: Lucene 4.6, Groovy 2.4

Both were only used by disabled plugins in src/out/. Zero usage in main source."
```

---

### Task 2: Create SearchService with in-memory search

**Files:**
- Create: `src/main/java/freemind/modes/mindmapmode/services/SearchService.java`
- Test: `src/test/java/freemind/modes/mindmapmode/services/SearchServiceTest.java`

**Step 1: Write the test**

```java
package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService();
    }

    @Test
    void searchFindsNodeByText() {
        MindMapNode root = mockNode("Root", null, List.of());
        MindMapNode child = mockNode("Hello World", null, List.of());
        when(root.getChildren()).thenReturn(List.of(child));
        when(child.getChildren()).thenReturn(List.of());

        List<SearchService.SearchResult> results = searchService.searchNodes(
                List.of(root), "Hello", false);

        assertEquals(1, results.size());
        assertEquals(child, results.get(0).node());
    }

    @Test
    void searchIsCaseInsensitive() {
        MindMapNode root = mockNode("HELLO world", null, List.of());
        when(root.getChildren()).thenReturn(List.of());

        List<SearchService.SearchResult> results = searchService.searchNodes(
                List.of(root), "hello", false);

        assertEquals(1, results.size());
    }

    @Test
    void searchFindsInNoteText() {
        MindMapNode root = mockNode("Node", "This is a note with keyword", List.of());
        when(root.getChildren()).thenReturn(List.of());

        List<SearchService.SearchResult> results = searchService.searchNodes(
                List.of(root), "keyword", false);

        assertEquals(1, results.size());
        assertEquals("note", results.get(0).matchLocation());
    }

    @Test
    void searchReturnsEmptyForNoMatch() {
        MindMapNode root = mockNode("Root", null, List.of());
        when(root.getChildren()).thenReturn(List.of());

        List<SearchService.SearchResult> results = searchService.searchNodes(
                List.of(root), "nonexistent", false);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchSupportsRegex() {
        MindMapNode root = mockNode("Item-123-abc", null, List.of());
        when(root.getChildren()).thenReturn(List.of());

        List<SearchService.SearchResult> results = searchService.searchNodes(
                List.of(root), "Item-\\d+-\\w+", true);

        assertEquals(1, results.size());
    }

    @Test
    void searchTraversesDeepTree() {
        MindMapNode grandchild = mockNode("Deep Target", null, List.of());
        when(grandchild.getChildren()).thenReturn(List.of());
        MindMapNode child = mockNode("Middle", null, List.of());
        when(child.getChildren()).thenReturn(List.of(grandchild));
        MindMapNode root = mockNode("Root", null, List.of());
        when(root.getChildren()).thenReturn(List.of(child));

        List<SearchService.SearchResult> results = searchService.searchNodes(
                List.of(root), "Deep", false);

        assertEquals(1, results.size());
        assertEquals(grandchild, results.get(0).node());
    }

    private MindMapNode mockNode(String text, String noteText, List<MindMapNode> children) {
        MindMapNode node = mock(MindMapNode.class);
        when(node.getText()).thenReturn(text);
        when(node.getPlainTextContent()).thenReturn(text);
        when(node.getNoteText()).thenReturn(noteText);
        when(node.getAttributeKeyList()).thenReturn(List.of());
        when(node.getChildren()).thenReturn(children);
        return node;
    }
}
```

**Step 2: Add Mockito dependency to pom.xml**

Add to `<dependencies>` section:

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.11.0</version>
    <scope>test</scope>
</dependency>
```

**Step 3: Run test to verify it fails**

Run: `mvn test -pl . -Dtest=SearchServiceTest -Dsurefire.useFile=false`
Expected: FAIL (SearchService class doesn't exist yet)

**Step 4: Implement SearchService**

```java
package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchService {

    public record SearchResult(MindMapNode node, String matchLocation, String matchedText) {}

    public List<SearchResult> searchNodes(List<MindMapNode> rootNodes, String query, boolean useRegex) {
        List<SearchResult> results = new ArrayList<>();
        Pattern pattern = useRegex
                ? Pattern.compile(query, Pattern.CASE_INSENSITIVE)
                : Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE);

        for (MindMapNode root : rootNodes) {
            searchRecursive(root, pattern, results);
        }
        return results;
    }

    private void searchRecursive(MindMapNode node, Pattern pattern, List<SearchResult> results) {
        // Search node text
        String text = node.getPlainTextContent();
        if (text != null && pattern.matcher(text).find()) {
            results.add(new SearchResult(node, "text", text));
        }

        // Search note text
        String noteText = node.getNoteText();
        if (noteText != null && pattern.matcher(noteText).find()) {
            results.add(new SearchResult(node, "note", noteText));
        }

        // Search attributes
        for (String key : node.getAttributeKeyList()) {
            String value = node.getAttribute(key);
            if (value != null && pattern.matcher(value).find()) {
                results.add(new SearchResult(node, "attribute:" + key, value));
            }
        }

        // Recurse into children
        for (Object child : node.getChildren()) {
            if (child instanceof MindMapNode childNode) {
                searchRecursive(childNode, pattern, results);
            }
        }
    }
}
```

**Step 5: Run tests to verify they pass**

Run: `mvn test -pl . -Dtest=SearchServiceTest -Dsurefire.useFile=false`
Expected: All 6 tests PASS

**Step 6: Commit**

```bash
git add src/main/java/freemind/modes/mindmapmode/services/SearchService.java
git add src/test/java/freemind/modes/mindmapmode/services/SearchServiceTest.java
git add pom.xml
git commit -m "Add SearchService: in-memory search across open maps

Walks node trees searching text, notes, and attributes.
Supports plain text and regex queries, case-insensitive."
```

---

### Task 3: Create SearchAction and wire into Edit menu

**Files:**
- Create: `src/main/java/freemind/modes/mindmapmode/actions/SearchAction.java`
- Modify: `src/main/java/freemind/modes/mindmapmode/MindMapController.java` (register action)
- Modify: `src/main/resources/mindmap_menus.xml` (line ~62, add to find category)

**Step 1: Create SearchAction**

```java
package freemind.modes.mindmapmode.actions;

import freemind.controller.Controller;
import freemind.controller.MapModuleManager;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.services.SearchService;
import freemind.modes.mindmapmode.services.SearchService.SearchResult;
import freemind.view.MapModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchAction extends AbstractAction {

    private final Controller controller;
    private final SearchService searchService;

    public SearchAction(Controller controller) {
        super(controller.getResourceString("search_all_maps"));
        this.controller = controller;
        this.searchService = new SearchService();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String query = JOptionPane.showInputDialog(
                controller.getFrame().getJFrame(),
                controller.getResourceString("search_query_prompt"),
                controller.getResourceString("search_all_maps"),
                JOptionPane.QUESTION_MESSAGE);

        if (query == null || query.trim().isEmpty()) {
            return;
        }

        MapModuleManager manager = controller.getMapModuleManager();
        List<MindMapNode> rootNodes = new ArrayList<>();
        for (MapModule module : manager.getMapModuleList()) {
            MindMap map = module.getModel();
            if (map != null && map.getRootNode() != null) {
                rootNodes.add(map.getRootNode());
            }
        }

        List<SearchResult> results = searchService.searchNodes(rootNodes, query.trim(), false);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(
                    controller.getFrame().getJFrame(),
                    controller.getResourceString("search_no_results"),
                    controller.getResourceString("search_all_maps"),
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Show results in a simple list dialog
        showResultsDialog(results, query);
    }

    private void showResultsDialog(List<SearchResult> results, String query) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (SearchResult result : results) {
            String display = result.node().getPlainTextContent()
                    + " [" + result.matchLocation() + "]";
            listModel.addElement(display);
        }

        JList<String> resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        resultList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = resultList.getSelectedIndex();
                if (index >= 0 && index < results.size()) {
                    navigateToNode(results.get(index).node());
                }
            }
        });

        JDialog dialog = new JDialog(controller.getFrame().getJFrame(),
                controller.getResourceString("search_all_maps") + ": " + query, false);
        dialog.add(scrollPane);
        dialog.pack();
        dialog.setLocationRelativeTo(controller.getFrame().getJFrame());
        dialog.setVisible(true);
    }

    private void navigateToNode(MindMapNode node) {
        // Select the node in the current view
        controller.getView().selectAsTheOnlyOneSelected(
                controller.getView().getNodeView(node));
        controller.getView().scrollNodeToVisible(
                controller.getView().getNodeView(node));
    }
}
```

**Note:** This is a starting implementation. The navigateToNode method may need adjustment for cross-map navigation (switching to the map that contains the node). Refine during implementation based on actual MapView API.

**Step 2: Add resource strings**

Add to `src/main/resources/freemind/resources/FreeMind.properties`:

```properties
search_all_maps=Search All Maps
search_query_prompt=Enter search text:
search_no_results=No results found.
```

**Step 3: Register action in MindMapController**

Add the SearchAction field and registration in `MindMapController.java`. Find where other actions are registered (look for `find` action) and add alongside it.

**Step 4: Wire into menu XML**

In `src/main/resources/mindmap_menus.xml`, add to the `find` category (around line 62):

```xml
<menu_category name="find">
    <menu_action field="find" key_ref="keystroke_find"/>
    <menu_action field="findNext" key_ref="keystroke_find_next"/>
    <menu_category name="findReplace"/>
    <menu_action field="searchAllMaps" key_ref="keystroke_search_all_maps"/>
    <menu_category name="otherFinds"/>
</menu_category>
```

**Step 5: Build and test manually**

Run: `mvn clean package -DskipTests`
Run: `java -jar target/mm-1.0-SNAPSHOT.jar`
Verify: Edit menu shows "Search All Maps" option

**Step 6: Commit**

```bash
git add src/main/java/freemind/modes/mindmapmode/actions/SearchAction.java
git add src/main/resources/freemind/resources/FreeMind.properties
git add src/main/java/freemind/modes/mindmapmode/MindMapController.java
git add src/main/resources/mindmap_menus.xml
git commit -m "Wire SearchAction into Edit menu for cross-map search

Simple dialog-based search across all open maps. Navigates to
selected result node."
```

---

## Section 2: Tools.java Decomposition

### Task 4: Create ColorUtils and migrate callers

**Files:**
- Create: `src/main/java/freemind/main/ColorUtils.java`
- Test: `src/test/java/freemind/main/ColorUtilsTest.java`
- Modify: `src/main/java/freemind/main/Tools.java` (remove methods at lines 121-153)
- Modify: ~25 files that call `Tools.colorToXml()` or `Tools.xmlToColor()`

**Step 1: Write failing tests**

```java
package freemind.main;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    @Test
    void colorToXml() {
        assertEquals("#ff0000", ColorUtils.colorToXml(Color.RED));
        assertEquals("#00ff00", ColorUtils.colorToXml(new Color(0, 255, 0)));
        assertEquals("#000000", ColorUtils.colorToXml(Color.BLACK));
    }

    @Test
    void xmlToColor() {
        assertEquals(Color.RED, ColorUtils.xmlToColor("#ff0000"));
        assertEquals(Color.BLACK, ColorUtils.xmlToColor("#000000"));
    }

    @Test
    void xmlToColorHandlesNull() {
        assertNull(ColorUtils.xmlToColor(null));
    }

    @Test
    void roundTrip() {
        Color original = new Color(128, 64, 32);
        assertEquals(original, ColorUtils.xmlToColor(ColorUtils.colorToXml(original)));
    }
}
```

**Step 2: Run test to verify it fails**

Run: `mvn test -Dtest=ColorUtilsTest -Dsurefire.useFile=false`
Expected: FAIL

**Step 3: Create ColorUtils by extracting from Tools.java**

```java
package freemind.main;

import java.awt.Color;

public final class ColorUtils {

    private ColorUtils() {}

    public static String colorToXml(Color col) {
        // Extract exact implementation from Tools.java lines 121-141
    }

    public static Color xmlToColor(String xmlColor) {
        // Extract exact implementation from Tools.java lines 143-153
    }
}
```

Copy the exact method bodies from `Tools.java` lines 121-153.

**Step 4: Run test to verify it passes**

Run: `mvn test -Dtest=ColorUtilsTest -Dsurefire.useFile=false`
Expected: PASS

**Step 5: Update all callers**

Replace `Tools.colorToXml(` with `ColorUtils.colorToXml(` and `Tools.xmlToColor(` with `ColorUtils.xmlToColor(` across all 25+ files. Update imports accordingly.

Key files to update (search for `Tools.colorToXml` and `Tools.xmlToColor`):
- `freemind/modes/XMLElementAdapter.java`
- `freemind/modes/ArrowLinkAdapter.java`
- `freemind/modes/CloudAdapter.java`
- `freemind/model/EdgeAdapter.java`
- `freemind/view/mindmapview/*.java` (several view files)
- `freemind/controller/filter/condition/CompareConditionAdapter.java`
- `accessories/plugins/*.java`

**Step 6: Remove methods from Tools.java**

Delete `colorToXml()` and `xmlToColor()` from Tools.java. Remove any now-unused imports.

**Step 7: Build and test**

Run: `mvn clean test`
Expected: All tests pass, BUILD SUCCESS

**Step 8: Commit**

```bash
git add -A
git commit -m "Extract ColorUtils from Tools.java

Move colorToXml/xmlToColor to dedicated ColorUtils class.
Update all 25+ callers."
```

---

### Task 5: Create PointUtils and migrate callers

**Files:**
- Create: `src/main/java/freemind/main/PointUtils.java`
- Test: `src/test/java/freemind/main/PointUtilsTest.java`
- Modify: `Tools.java` (remove lines ~155-185)
- Modify: ~6 files using `Tools.PointToXml`/`Tools.xmlToPoint`
- Modify: ~23 files using `Tools.convertPointToAncestor`/`Tools.convertPointFromAncestor`

**Step 1: Write tests**

```java
package freemind.main;

import org.junit.jupiter.api.Test;
import java.awt.Point;
import static org.junit.jupiter.api.Assertions.*;

class PointUtilsTest {

    @Test
    void pointToXml() {
        assertEquals("10;20", PointUtils.pointToXml(new Point(10, 20)));
    }

    @Test
    void xmlToPoint() {
        assertEquals(new Point(10, 20), PointUtils.xmlToPoint("10;20"));
    }

    @Test
    void roundTrip() {
        Point original = new Point(42, -7);
        assertEquals(original, PointUtils.xmlToPoint(PointUtils.pointToXml(original)));
    }
}
```

**Step 2-8:** Same pattern as Task 4. Extract `PointToXml`, `xmlToPoint`, `convertPointToAncestor`, `convertPointFromAncestor` from Tools.java. Update callers. Commit.

---

### Task 6: Create SwingUtils and migrate callers

**Files:**
- Create: `src/main/java/freemind/main/SwingUtils.java`
- Modify: `Tools.java` (remove ~15 Swing utility methods)
- Modify: ~35 files using `Tools.setLabelAndMnemonic`, ~13 using `Tools.addEscapeActionToDialog`, etc.

**Methods to move:**
- `setLabelAndMnemonic(AbstractButton, String)` (line 1285)
- `setLabelAndMnemonic(Action, String)` (line 1294)
- `addEscapeActionToDialog(JDialog)` (line 992)
- `addEscapeActionToDialog(JDialog, Action)` (line 996)
- `addKeyActionToDialog(JDialog, Action, String, String)` (line 1011)
- `getKeyStroke(String)` (line 1048)
- `waitForEventQueue()` (line 1582)
- `invokeAndWait(Runnable)` (line 1590 area)
- `setDialogLocationRelativeTo(JDialog, Component)` (line 1158 area)
- `getAvailableFonts()`, `isAvailableFontFamily(String)`, `updateFontSize(Font, float, int)`
- `restoreAntialiasing(Graphics2D, Object)`, `scaleAllFonts(float)`
- `getScalingFactor()`, `getScalingFactorPlain()`
- `removeMnemonic(String)` (line 1279)
- `correctJSplitPaneKeyMap()`, `logTransferable(Transferable)`
- `isHeadless()`, `isRetina()`

No test needed for most of these (pure Swing wrappers). Verify with `mvn clean compile`.

**Step 1: Create SwingUtils with all methods**
**Step 2: Update all callers (find/replace `Tools.setLabelAndMnemonic` → `SwingUtils.setLabelAndMnemonic`, etc.)**
**Step 3: Remove from Tools.java**
**Step 4: `mvn clean test` - all pass**
**Step 5: Commit**

```bash
git commit -m "Extract SwingUtils from Tools.java

Move 15+ Swing utility methods (mnemonics, dialogs, fonts, scaling)
to dedicated SwingUtils class. Update ~50 callers."
```

---

### Task 7: Create EncryptionUtils and migrate callers

**Files:**
- Create: `src/main/java/freemind/main/EncryptionUtils.java`
- Modify: `Tools.java` (remove inner classes and methods at lines 505-700)
- Modify: ~3 files using Base64, ~1 using compress/decompress, ~1 using TripleDesEncrypter

**Methods/classes to move:**
- `DesEncrypter` inner class (line 505-609)
- `SingleDesEncrypter` (line 611-618)
- `TripleDesEncrypter` (line 619-628)
- `toBase64(byte[])` → replace with `java.util.Base64.getEncoder().encodeToString()`
- `toBase64(String)` → replace with `java.util.Base64`
- `fromBase64(String)` → replace with `java.util.Base64.getDecoder().decode()`
- `compress(String)` (line 647)
- `decompress(String)` (line 679)

**Step 1: Create EncryptionUtils**

Move inner classes as top-level classes within EncryptionUtils. Replace custom Base64Coding calls with `java.util.Base64`.

**Step 2: Update callers**
**Step 3: Remove from Tools.java**
**Step 4: `mvn clean test` - all pass**
**Step 5: Commit**

```bash
git commit -m "Extract EncryptionUtils from Tools.java

Move DES encryption classes, Base64 (now using java.util.Base64),
and compression utilities to dedicated class."
```

---

### Task 8: Create MindMapUtils and migrate callers

**Files:**
- Create: `src/main/java/freemind/main/MindMapUtils.java`
- Modify: `Tools.java` (remove ~10 mindmap-specific methods)
- Modify: ~15 files

**Methods to move:**
- `getFileNameProposal(MindMapNode)` (line ~1492)
- `getNodeTextHierarchy(MindMapNode, String)` (line 1595)
- `getMindMapNodesFromClipboard(MindMapController)` (line 1613)
- `getClipboard()` (line 1605)
- `iconFirstIndex(MindMapNode, String)` (line ~1640)
- `iconLastIndex(MindMapNode, String)` (line ~1650)
- `edgeWidthStringToInt(String)` (line ~1660)
- `getFileNameFromRestorable(String)` (line 1475)
- `getModeFromRestorable(String)` (line 1488)
- `generateID(String, Map, String)`
- `MindMapNodePair` inner class (line 1221-1239)

**Steps:** Same pattern. Create, move, update callers, remove from Tools, test, commit.

```bash
git commit -m "Extract MindMapUtils from Tools.java

Move domain-specific mind map utilities (node operations, clipboard,
icon helpers, ID generation) to dedicated class."
```

---

### Task 9: Replace Tools methods with Apache Commons / JDK equivalents

**Files:**
- Modify: `Tools.java` (remove ~15 methods)
- Modify: callers of each replaced method

**Replacements to make (inline at call sites or thin wrapper):**

| Method | Replacement | Callers |
|---|---|---|
| `getExtension(File)` | `FilenameUtils.getExtension(f.getName())` | ~9 files |
| `getExtension(String)` | `FilenameUtils.getExtension(s)` | ~0 files |
| `removeExtension(String)` | `FilenameUtils.removeExtension(s)` | ~1 file |
| `isAbsolutePath(String)` | `new File(path).isAbsolute()` | ~3 files |
| `getFile(File)` | `FileUtils.readFileToString(f, StandardCharsets.UTF_8)` | ~1 file |
| `getFile(Reader)` | `IOUtils.toString(reader)` | ~1 file |
| `copyStream(in, out, close)` | `IOUtils.copy(in, out)` + conditional close | ~3 files |
| `listToString(List)` | `StringUtils.join(list, ";")` | ~3 files |
| `stringToList(String)` | `Arrays.asList(StringUtils.split(s, ";"))` | ~0 files |
| `countOccurrences(s, sub)` | `StringUtils.countMatches(s, sub)` | ~1 file |
| `isWindows()` | `SystemUtils.IS_OS_WINDOWS` | ~0 files |
| `isMacOsX()` | `SystemUtils.IS_OS_MAC` | ~8 files |
| `isLinux()` | `SystemUtils.IS_OS_LINUX` | ~0 files |
| `isUnix()` | `SystemUtils.IS_OS_UNIX` | ~1 file |
| `BooleanToXml(bool)` | `String.valueOf(bool)` | ~3 files |
| `xmlToBoolean(str)` | `Boolean.parseBoolean(str)` | ~8 files |
| `safeEquals(a, b)` | `Objects.equals(a, b)` | ~0 files |
| `getSingletonList(t)` | `Collections.singletonList(t)` | ~11 files |

**Step 1: Replace each method at all call sites**

Work through the table above. For each method:
1. Find all callers with grep
2. Replace the call with the Apache Commons / JDK equivalent
3. Add the import for the replacement class
4. Remove the method from Tools.java

**Step 2: Build and test**

Run: `mvn clean test`
Expected: All tests pass

**Step 3: Commit**

```bash
git commit -m "Replace 15+ Tools methods with Apache Commons / JDK equivalents

Use FilenameUtils, IOUtils, StringUtils, SystemUtils, Objects,
Collections, java.util.Base64 instead of custom implementations."
```

---

### Task 10: Clean up remaining Tools.java

**Files:**
- Modify: `src/main/java/freemind/main/Tools.java`

After Tasks 4-9, Tools.java should have only these remaining methods:
- XML serialization delegates: `marshall()`, `unMarshall()`, `deepCopy()`, `printXmlAction()`
- File URL conversions: `fileToUrl()`, `urlToFile()`, `fileToRelativeUrlString()`, `toRelativeURL()`
- Page format: `setPageFormatFromString()`, `getPageFormatAsString()`
- XSLT: `getUpdateReader()`, `getActualReader()`
- Platform-specific file ops: `setHidden()`, `makeFileHidden()`, `setPermissions()`, `executableByExtension()`
- Misc: `expandFileName()`, `expandPlaceholders()`, `compareText()`, `copyChangedProperties()`, `getFreeMindBasePath()`
- `ReaderCreator` interface, `FileReaderCreator`, `StringReaderCreator`

**Step 1: Review remaining methods**

Verify Tools.java is now under ~500 lines. Clean up unused imports.

**Step 2: Build and test**

Run: `mvn clean test`
Expected: All 99+ tests pass

**Step 3: Commit**

```bash
git commit -m "Clean up Tools.java: now under 500 lines

Remaining methods are XML serialization, file URL conversion,
page format, XSLT, and platform-specific file operations."
```

---

## Section 3: XMLElement Replacement

### Task 11: Create FreeMindXml utility class

**Files:**
- Create: `src/main/java/freemind/main/FreeMindXml.java`
- Test: `src/test/java/freemind/main/FreeMindXmlTest.java`

**Step 1: Write tests**

```java
package freemind.main;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreeMindXmlTest {

    @Test
    void parseSimpleXml() throws Exception {
        Document doc = FreeMindXml.parse(new StringReader("<root attr=\"value\"/>"));
        Element root = doc.getDocumentElement();
        assertEquals("root", root.getTagName());
        assertEquals("value", root.getAttribute("attr"));
    }

    @Test
    void getChildElements() throws Exception {
        Document doc = FreeMindXml.parse(new StringReader(
                "<root><child1/><child2/></root>"));
        List<Element> children = FreeMindXml.getChildElements(doc.getDocumentElement());
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getTagName());
        assertEquals("child2", children.get(1).getTagName());
    }

    @Test
    void getStringAttribute() throws Exception {
        Document doc = FreeMindXml.parse(new StringReader("<node name=\"test\"/>"));
        Element el = doc.getDocumentElement();
        assertEquals("test", FreeMindXml.getStringAttribute(el, "name"));
        assertNull(FreeMindXml.getStringAttribute(el, "missing"));
    }

    @Test
    void getIntAttribute() throws Exception {
        Document doc = FreeMindXml.parse(new StringReader("<node count=\"42\"/>"));
        assertEquals(42, FreeMindXml.getIntAttribute(doc.getDocumentElement(), "count", 0));
    }

    @Test
    void createElement() throws Exception {
        Document doc = FreeMindXml.newDocument();
        Element root = FreeMindXml.createElement(doc, "root");
        root.setAttribute("key", "value");
        doc.appendChild(root);

        Element child = FreeMindXml.createElement(doc, "child");
        root.appendChild(child);

        StringWriter writer = new StringWriter();
        FreeMindXml.write(doc, writer);
        String xml = writer.toString();
        assertTrue(xml.contains("<root"));
        assertTrue(xml.contains("key=\"value\""));
        assertTrue(xml.contains("<child"));
    }

    @Test
    void writeAndParse() throws Exception {
        Document doc = FreeMindXml.newDocument();
        Element root = FreeMindXml.createElement(doc, "filter");
        root.setAttribute("name", "test");
        doc.appendChild(root);
        Element cond = FreeMindXml.createElement(doc, "condition");
        cond.setAttribute("type", "contains");
        root.appendChild(cond);

        StringWriter sw = new StringWriter();
        FreeMindXml.write(doc, sw);

        Document parsed = FreeMindXml.parse(new StringReader(sw.toString()));
        Element parsedRoot = parsed.getDocumentElement();
        assertEquals("filter", parsedRoot.getTagName());
        assertEquals("test", parsedRoot.getAttribute("name"));
        List<Element> kids = FreeMindXml.getChildElements(parsedRoot);
        assertEquals(1, kids.size());
        assertEquals("contains", kids.get(0).getAttribute("type"));
    }
}
```

**Step 2: Run to verify failure**

Run: `mvn test -Dtest=FreeMindXmlTest -Dsurefire.useFile=false`
Expected: FAIL

**Step 3: Implement FreeMindXml**

```java
package freemind.main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class FreeMindXml {

    private static final DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory TF = TransformerFactory.newInstance();

    private FreeMindXml() {}

    public static Document parse(Reader reader) throws Exception {
        DocumentBuilder builder = DBF.newDocumentBuilder();
        return builder.parse(new InputSource(reader));
    }

    public static Document newDocument() throws Exception {
        DocumentBuilder builder = DBF.newDocumentBuilder();
        return builder.newDocument();
    }

    public static Element createElement(Document doc, String tagName) {
        return doc.createElement(tagName);
    }

    public static List<Element> getChildElements(Element parent) {
        List<Element> result = new ArrayList<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) child);
            }
        }
        return result;
    }

    public static String getStringAttribute(Element el, String name) {
        if (!el.hasAttribute(name)) {
            return null;
        }
        return el.getAttribute(name);
    }

    public static int getIntAttribute(Element el, String name, int defaultValue) {
        String val = getStringAttribute(el, name);
        if (val == null) {
            return defaultValue;
        }
        return Integer.parseInt(val);
    }

    public static double getDoubleAttribute(Element el, String name, double defaultValue) {
        String val = getStringAttribute(el, name);
        if (val == null) {
            return defaultValue;
        }
        return Double.parseDouble(val);
    }

    public static boolean getBooleanAttribute(Element el, String name, boolean defaultValue) {
        String val = getStringAttribute(el, name);
        if (val == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(val);
    }

    public static void write(Document doc, Writer writer) throws Exception {
        Transformer transformer = TF.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
    }

    public static void write(Element element, Writer writer) throws Exception {
        Transformer transformer = TF.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(element), new StreamResult(writer));
    }
}
```

**Step 4: Run tests**

Run: `mvn test -Dtest=FreeMindXmlTest -Dsurefire.useFile=false`
Expected: All PASS

**Step 5: Commit**

```bash
git add src/main/java/freemind/main/FreeMindXml.java
git add src/test/java/freemind/main/FreeMindXmlTest.java
git commit -m "Add FreeMindXml: DOM-based utility replacing XMLElement API

Provides parse, write, attribute access, and child element traversal
using standard JDK DOM API."
```

---

### Task 12: Migrate filter conditions from XMLElement to DOM

**Files:**
- Modify: `src/main/java/freemind/controller/filter/condition/Condition.java`
- Modify: All 14 condition classes in `freemind/controller/filter/condition/`
- Modify: `src/main/java/freemind/controller/filter/FilterController.java`

This is the largest XMLElement consumer group. The pattern is consistent:

**Current pattern (XMLElement):**
```java
public void save(XMLElement element) {
    XMLElement child = new XMLElement();
    child.setName(NAME);
    super.saveAttributes(child);
    child.setAttribute(VALUE, value);
    element.addChild(child);
}

static Condition load(XMLElement element) {
    return new NodeContainsCondition(
        element.getStringAttribute(VALUE));
}
```

**New pattern (DOM):**
```java
public void save(Document doc, Element parent) {
    Element child = doc.createElement(NAME);
    super.saveAttributes(child);
    child.setAttribute(VALUE, value);
    parent.appendChild(child);
}

static Condition load(Element element) {
    return new NodeContainsCondition(
        FreeMindXml.getStringAttribute(element, VALUE));
}
```

**Step 1: Update Condition interface**

Change `save(XMLElement element)` to `save(Document doc, Element parent)`.

**Step 2: Update each condition class**

Files to update:
- `Condition.java` - interface
- `CompareConditionAdapter.java`
- `NodeContainsCondition.java`
- `NodeCompareCondition.java`
- `IconContainedCondition.java`
- `IconNotContainedCondition.java`
- `AttributeExistsCondition.java`
- `AttributeNotExistsCondition.java`
- `AttributeCompareCondition.java`
- `DisjunctConditions.java`
- `ConjunctConditions.java`
- `ConditionNotSatisfiedDecorator.java`
- `ConditionFactory.java`
- `SelectedViewCondition.java`
- `NoFilteringCondition.java`

**Step 3: Update FilterController**

Modify `FilterController.java` to use `FreeMindXml.parse()` and `FreeMindXml.write()` instead of `XMLElement.parseFromReader()` and `XMLElement.write()`.

**Step 4: Build and test**

Run: `mvn clean test`
Expected: All tests pass

**Step 5: Commit**

```bash
git commit -m "Migrate filter conditions from XMLElement to DOM API

Update Condition interface and all 14 implementations to use
org.w3c.dom.Element instead of XMLElement for save/load."
```

---

### Task 13: Migrate model adapters from XMLElement to DOM

**Files:**
- Modify: `freemind/model/EdgeAdapter.java`
- Modify: `freemind/model/MindMapEdge.java`
- Modify: `freemind/model/MindMapNode.java` (interface)
- Modify: `freemind/modes/CloudAdapter.java`
- Modify: `freemind/modes/ArrowLinkAdapter.java`
- Modify: `freemind/modes/ArrowLinkTarget.java`
- Modify: `freemind/modes/MindMapArrowLink.java`
- Modify: `freemind/modes/MindMapCloud.java`

These classes have `save(XMLElement)` methods similar to filter conditions. Same mechanical migration pattern.

**Commit:**
```bash
git commit -m "Migrate model adapters from XMLElement to DOM API

Update EdgeAdapter, CloudAdapter, ArrowLinkAdapter and related
classes to use org.w3c.dom.Element."
```

---

### Task 14: Migrate hook/extension system from XMLElement to DOM

**Files:**
- Modify: `freemind/extensions/PermanentNodeHook.java`
- Modify: `freemind/extensions/PermanentNodeHookAdapter.java`
- Modify: `freemind/extensions/PermanentNodeHookSubstituteUnknown.java`
- Modify: `freemind/modes/mindmapmode/actions/xml/actors/AddHookActor.java`
- Modify: `freemind/modes/common/plugins/ReminderHookBase.java`
- Modify: `freemind/modes/common/plugins/MapNodePositionHolderBase.java`

Same migration pattern. Hook classes use `save(XMLElement)`/`loadFrom(XMLElement)`.

**Commit:**
```bash
git commit -m "Migrate hook/extension system from XMLElement to DOM API"
```

---

### Task 15: Migrate browse mode models from XMLElement to DOM

**Files:**
- Modify: `freemind/modes/browsemode/BrowseNodeModel.java`
- Modify: `freemind/modes/browsemode/BrowseEdgeModel.java`
- Modify: `freemind/modes/browsemode/BrowseCloudModel.java`
- Modify: `freemind/modes/browsemode/BrowseArrowLinkModel.java`

**Commit:**
```bash
git commit -m "Migrate browse mode models from XMLElement to DOM API"
```

---

### Task 16: Migrate remaining XMLElement users

**Files:**
- Modify: `freemind/modes/StylePattern.java`
- Modify: `freemind/model/MapAdapter.java`
- Modify: `freemind/modes/EncryptedMindMapNode` (if in main source)
- Modify: `accessories/plugins/ClonePlugin.java`

**Note on MapAdapter:** This is the most complex migration because it uses `XMLElementAdapter` (which extends XMLElement) for the mind map file loading pipeline. This may require keeping XMLElementAdapter temporarily or migrating it to a DOM-based approach.

**Commit:**
```bash
git commit -m "Migrate remaining XMLElement users to DOM API"
```

---

### Task 17: Handle XMLElementAdapter and .mm file loading

**Files:**
- Modify: `freemind/modes/XMLElementAdapter.java` (630 lines)
- Modify: `freemind/model/MapAdapter.java`

**This is the highest-risk task.** XMLElementAdapter is a subclass of XMLElement that overrides `setName()`, `addChild()`, and `setAttribute()` to build the mind map node tree during parsing. It's the bridge between XML parsing and the model.

**Two approaches (decide during implementation):**

**A. Convert XMLElementAdapter to use DOM:**
- Change it to accept a DOM `Element` instead of extending XMLElement
- The overridden methods become a visitor/builder pattern that walks the DOM tree
- MapAdapter.load() parses with DOM first, then passes to the builder

**B. Keep XMLElementAdapter as legacy bridge (temporary):**
- Mark as `@Deprecated`
- Keep XMLElement.java but only for this one code path
- Plan removal in a future phase

**Recommendation:** Approach A is cleaner but higher risk. Investigate the coupling depth during implementation. If > 2 days effort, use Approach B.

**Commit:**
```bash
git commit -m "Migrate XMLElementAdapter to DOM-based builder pattern

Replace XMLElement inheritance with DOM tree walking for .mm file loading."
```

---

### Task 18: Delete XMLElement.java

**Files:**
- Delete: `src/main/java/freemind/main/XMLElement.java`

**Prerequisite:** All callers migrated (Tasks 12-17 complete).

**Step 1: Verify no remaining imports**

Run: `grep -r "import freemind.main.XMLElement" src/main/java/`
Expected: No matches

**Step 2: Delete the file**

**Step 3: Build and test**

Run: `mvn clean test`
Expected: All tests pass

**Step 4: Commit**

```bash
git rm src/main/java/freemind/main/XMLElement.java
git commit -m "Delete XMLElement.java (2957 lines)

Custom NanoXML parser fully replaced by JDK DOM API via FreeMindXml utility."
```

---

## Section 4: Dependency Audit

### Task 19: Audit and update dependencies

**Step 1: Check for outdated dependencies**

Run: `mvn versions:display-dependency-updates`

**Step 2: Check for unused dependencies**

Run: `mvn dependency:analyze`

**Step 3: Update outdated dependencies**

Review each outdated dependency. Update if:
- Security vulnerability exists
- Major version behind (e.g. 2.x → 4.x)
- No breaking API changes

Skip if:
- Minor version behind with no security issues
- Would require significant code changes

**Step 4: Verify JGoodies Forms**

Check if JGoodies Forms is still actively used. If minimal usage, consider MigLayout replacement. If heavily used, keep as-is (low priority per design).

**Step 5: Build and test**

Run: `mvn clean test`
Expected: All tests pass

**Step 6: Commit**

```bash
git commit -m "Audit and update dependencies

[List specific dependency updates made]"
```

---

## Summary

| Task | Description | Estimated Complexity |
|---|---|---|
| 1 | Remove dead deps (Lucene, Groovy) | Small |
| 2 | Create SearchService + tests | Medium |
| 3 | Create SearchAction, wire menu | Medium |
| 4 | Extract ColorUtils | Small |
| 5 | Extract PointUtils | Small |
| 6 | Extract SwingUtils | Medium (many callers) |
| 7 | Extract EncryptionUtils | Small |
| 8 | Extract MindMapUtils | Medium |
| 9 | Replace Tools methods with Commons/JDK | Medium (many callers) |
| 10 | Clean up remaining Tools.java | Small |
| 11 | Create FreeMindXml utility | Medium |
| 12 | Migrate filter conditions | Medium (14 files) |
| 13 | Migrate model adapters | Medium (8 files) |
| 14 | Migrate hook/extension system | Medium (6 files) |
| 15 | Migrate browse mode models | Small (4 files) |
| 16 | Migrate remaining XMLElement users | Medium |
| 17 | Handle XMLElementAdapter (.mm loading) | High (risk) |
| 18 | Delete XMLElement.java | Small |
| 19 | Dependency audit | Small |
