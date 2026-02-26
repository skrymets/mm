# Phase 3: Dependency Upgrades & Dead Code Removal

## Overview

Phase 3 cleans up technical debt left after modernization (Phases 0-2). It removes dead dependencies, decomposes the Tools.java god utility class, replaces the custom XMLElement parser with standard JDK DOM API, integrates search as core functionality, and audits remaining dependencies.

**Approach:** Bottom-up mechanical. Each section is independently testable.

## Section 1: Dead Dependency Removal + Core Search Integration

### 1a. Remove Dead Dependencies

Remove from `pom.xml`:
- `lucene-core` 4.6.0 (only used in disabled search plugin in `src/out/`)
- `lucene-queryparser` 4.6.0
- `lucene-analyzers-common` 4.6.0
- `groovy-all` 2.4.21 (only used in disabled scripting plugin in `src/out/`)

Zero usages in `src/main/java`. The `src/out/` directory is kept as-is for future reference.

Remove disabled plugin configs:
- `src/main/resources/plugins/Search.xml.disabled`

### 1b. Core Search Service

Integrate search across open maps as core functionality (not a plugin).

**Implementation:**
- Create `SearchService` in `freemind/modes/mindmapmode/services/`
- Walk open maps' node trees via `MapModuleManager.getMapModules()`
- Search node text, note text, and attributes using `String.contains()` or regex
- Provide a search results dialog (adapt `SearchViewPanel` UI concepts from the disabled plugin)
- Wire into Edit menu where the plugin previously hooked in

**Design decisions:**
- Simple in-memory search (no Lucene) since open maps are already loaded as `MindMapNode` trees
- No file-system indexing; only search currently open maps
- Support case-insensitive text matching and optional regex

## Section 2: Tools.java Decomposition

### Goal

Break `Tools.java` (1949 lines, 90+ methods) into focused utility classes. Replace ~15-20 general methods with Apache Commons (already in pom.xml: commons-lang3, commons-io, commons-collections4). Clean break migration (no facade/delegation).

### New Utility Classes

| Class | Location | Methods | Strategy |
|---|---|---|---|
| `ColorUtils` | `freemind/main/` | `colorToXml`, `xmlToColor`, `safeEquals(Color)` | Keep custom |
| `SwingUtils` | `freemind/main/` | `setLabelAndMnemonic`, `addEscapeActionToDialog`, `waitForEventQueue`, `getKeyStroke`, font/scaling (~15 methods) | Keep custom |
| `PointUtils` | `freemind/main/` | `PointToXml`, `xmlToPoint`, `convertPointToAncestor/FromAncestor` (~5 methods) | Keep custom |
| `MindMapUtils` | `freemind/main/` | `getFileNameProposal`, `getNodeTextHierarchy`, `getMindMapNodesFromClipboard`, icon methods (~10 methods) | Keep custom |
| `EncryptionUtils` | `freemind/main/` | `DesEncrypter`, `SingleDesEncrypter`, `TripleDesEncrypter`, `compress`/`decompress` (~7 methods) | Keep custom |

### Apache Commons / JDK Replacements

| Current Method | Replacement |
|---|---|
| `getExtension()` | `FilenameUtils.getExtension()` (commons-io) |
| `removeExtension()` | `FilenameUtils.removeExtension()` (commons-io) |
| `isAbsolutePath()` | `FilenameUtils.isAbsolutePath()` (commons-io) |
| `getFile(File/Reader)` | `FileUtils.readFileToString()` / `IOUtils.toString()` (commons-io) |
| `copyStream()` | `IOUtils.copy()` (commons-io) |
| `listToString()` | `StringUtils.join()` (commons-lang3) |
| `countOccurrences()` | `StringUtils.countMatches()` (commons-lang3) |
| `isMacOsX()`/`isWindows()` etc. | `SystemUtils.IS_OS_*` (commons-lang3) |
| `BooleanToXml()` | `String.valueOf()` |
| `xmlToBoolean()` | `Boolean.parseBoolean()` |
| `safeEquals()` | `Objects.equals()` |
| `getSingletonList()` | `Collections.singletonList()` |
| `toBase64()`/`fromBase64()` | `java.util.Base64` |

### Remaining Methods

Methods that don't fit a category (XML serialization delegates, file URL conversions, page format utils, etc.) stay in a slimmed-down `Tools.java` until a natural home emerges. Target: Tools.java under 500 lines.

## Section 3: XMLElement Replacement

### Goal

Replace `XMLElement.java` (2957-line custom NanoXML 2 Lite parser) with JDK DOM API across ~42 main-source files (7 more in `src/out/` ignored).

### Why DOM (not JAXB)

XMLElement is used for dynamic/ad-hoc XML (filters, patterns, hooks, node attribute serialization) where there's no XSD. JAXB already handles the structured `.mm` format via generated classes.

### Approach

1. **Create `FreeMindXml` utility class** wrapping DOM for a similar API surface:
   - `FreeMindXml.parse(Reader)` -> `Document`
   - `FreeMindXml.getStringAttribute(Element, String)` -> wraps `getAttribute()`
   - `FreeMindXml.getChildren(Element)` -> wraps `getChildNodes()` filtering `ELEMENT_NODE`
   - `FreeMindXml.write(Document, Writer)` -> wraps `Transformer`

2. **Migrate by subsystem:**
   - Filter conditions (14 files): `save(XMLElement)`/`load(XMLElement)` patterns
   - Model adapters (8 files): `EdgeAdapter`, `CloudAdapter`, `ArrowLinkAdapter`, etc.
   - Hook/extension system (6 files): `PermanentNodeHookAdapter` etc.
   - Browse mode models (4 files)
   - Other (10 files): `StylePattern`, `FilterController`, `MapAdapter`, etc.

3. **Handle XMLElementAdapter specially** - subclass of XMLElement used in `.mm` file parsing. Needs investigation: may migrate to DOM or to JAXB depending on coupling.

4. **Delete `XMLElement.java`** once all callers migrated.

### Risk

XMLElementAdapter is tightly coupled to the `.mm` file loading path (`MapAdapter.load()`). Deeper investigation needed during implementation planning.

## Section 4: Dependency Audit & Cleanup

- Run `mvn versions:display-dependency-updates` for outdated deps
- Run `mvn dependency:analyze` for unused/undeclared deps
- Update significantly outdated dependencies
- Evaluate JGoodies Forms -> MigLayout swap (low priority)

## Expected Outcomes

- Zero dependencies older than 2020 in `mvn dependency:tree`
- No unused dependencies in pom.xml
- Tools.java under 500 lines, with focused utility classes
- XMLElement.java deleted, replaced by standard DOM API
- Core search functionality for open maps
- ~3000+ lines of legacy code removed
- All 99 existing tests still passing

## Execution Order

1. Dead dependency removal + core search integration
2. Tools.java decomposition (clean break)
3. XMLElement replacement
4. Dependency audit
