
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="map_window_configuration_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="window_configuration_storage">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="table_column_setting" minOccurs="0" maxOccurs="unbounded"/>
 *           &lt;xs:element ref="map_location_storage" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:double" use="optional" name="map_center_longitude"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="map_center_latitude"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="cursor_longitude"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="cursor_latitude"/>
 *         &lt;xs:attribute type="xs:int" use="optional" name="zoom"/>
 *         &lt;xs:attribute type="xs:int" use="optional" name="last_divider_position"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="tile_source"/>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="true" name="showMapMarker"/>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="false" name="tileGridVisible"/>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="true" name="zoomControlsVisible"/>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="true" name="searchControlVisible"/>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="true" name="hideFoldedNodes"/>
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="false" name="limitSearchToVisibleArea"/>
 *         &lt;xs:attribute type="xs:int" use="optional" name="map_location_storage_index"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class MapWindowConfigurationStorage extends WindowConfigurationStorage
{
    private List<TableColumnSettingStore> tableColumnSettingList = new ArrayList<TableColumnSettingStore>();
    private List<MapLocationStorage> mapLocationStorageList = new ArrayList<MapLocationStorage>();
    private Double mapCenterLongitude;
    private Double mapCenterLatitude;
    private Double cursorLongitude;
    private Double cursorLatitude;
    private Integer zoom;
    private Integer lastDividerPosition;
    private String tileSource;
    private Boolean showMapMarker;
    private Boolean tileGridVisible;
    private Boolean zoomControlsVisible;
    private Boolean searchControlVisible;
    private Boolean hideFoldedNodes;
    private Boolean limitSearchToVisibleArea;
    private Integer mapLocationStorageIndex;

    /** 
     * Get the list of 'table_column_setting' element items.
     * 
     * @return list
     */
    public List<TableColumnSettingStore> getTableColumnSettingList() {
        return tableColumnSettingList;
    }

    /** 
     * Set the list of 'table_column_setting' element items.
     * 
     * @param list
     */
    public void setTableColumnSettingList(List<TableColumnSettingStore> list) {
        tableColumnSettingList = list;
    }

    /** 
     * Get the number of 'table_column_setting' element items.
     * @return count
     */
    public int sizeTableColumnSettingList() {
        return tableColumnSettingList.size();
    }

    /** 
     * Add a 'table_column_setting' element item.
     * @param item
     */
    public void addTableColumnSetting(TableColumnSettingStore item) {
        tableColumnSettingList.add(item);
    }

    /** 
     * Get 'table_column_setting' element item by position.
     * @return item
     * @param index
     */
    public TableColumnSettingStore getTableColumnSetting(int index) {
        return tableColumnSettingList.get(index);
    }

    /** 
     * Remove all 'table_column_setting' element items.
     */
    public void clearTableColumnSettingList() {
        tableColumnSettingList.clear();
    }

    /** 
     * Get the list of 'map_location_storage' element items.
     * 
     * @return list
     */
    public List<MapLocationStorage> getMapLocationStorageList() {
        return mapLocationStorageList;
    }

    /** 
     * Set the list of 'map_location_storage' element items.
     * 
     * @param list
     */
    public void setMapLocationStorageList(List<MapLocationStorage> list) {
        mapLocationStorageList = list;
    }

    /** 
     * Get the number of 'map_location_storage' element items.
     * @return count
     */
    public int sizeMapLocationStorageList() {
        return mapLocationStorageList.size();
    }

    /** 
     * Add a 'map_location_storage' element item.
     * @param item
     */
    public void addMapLocationStorage(MapLocationStorage item) {
        mapLocationStorageList.add(item);
    }

    /** 
     * Get 'map_location_storage' element item by position.
     * @return item
     * @param index
     */
    public MapLocationStorage getMapLocationStorage(int index) {
        return mapLocationStorageList.get(index);
    }

    /** 
     * Remove all 'map_location_storage' element items.
     */
    public void clearMapLocationStorageList() {
        mapLocationStorageList.clear();
    }

    /** 
     * Get the 'map_center_longitude' attribute value.
     * 
     * @return value
     */
    public Double getMapCenterLongitude() {
        return mapCenterLongitude;
    }

    /** 
     * Set the 'map_center_longitude' attribute value.
     * 
     * @param mapCenterLongitude
     */
    public void setMapCenterLongitude(Double mapCenterLongitude) {
        this.mapCenterLongitude = mapCenterLongitude;
    }

    /** 
     * Get the 'map_center_latitude' attribute value.
     * 
     * @return value
     */
    public Double getMapCenterLatitude() {
        return mapCenterLatitude;
    }

    /** 
     * Set the 'map_center_latitude' attribute value.
     * 
     * @param mapCenterLatitude
     */
    public void setMapCenterLatitude(Double mapCenterLatitude) {
        this.mapCenterLatitude = mapCenterLatitude;
    }

    /** 
     * Get the 'cursor_longitude' attribute value.
     * 
     * @return value
     */
    public Double getCursorLongitude() {
        return cursorLongitude;
    }

    /** 
     * Set the 'cursor_longitude' attribute value.
     * 
     * @param cursorLongitude
     */
    public void setCursorLongitude(Double cursorLongitude) {
        this.cursorLongitude = cursorLongitude;
    }

    /** 
     * Get the 'cursor_latitude' attribute value.
     * 
     * @return value
     */
    public Double getCursorLatitude() {
        return cursorLatitude;
    }

    /** 
     * Set the 'cursor_latitude' attribute value.
     * 
     * @param cursorLatitude
     */
    public void setCursorLatitude(Double cursorLatitude) {
        this.cursorLatitude = cursorLatitude;
    }

    /** 
     * Get the 'zoom' attribute value.
     * 
     * @return value
     */
    public Integer getZoom() {
        return zoom;
    }

    /** 
     * Set the 'zoom' attribute value.
     * 
     * @param zoom
     */
    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    /** 
     * Get the 'last_divider_position' attribute value.
     * 
     * @return value
     */
    public Integer getLastDividerPosition() {
        return lastDividerPosition;
    }

    /** 
     * Set the 'last_divider_position' attribute value.
     * 
     * @param lastDividerPosition
     */
    public void setLastDividerPosition(Integer lastDividerPosition) {
        this.lastDividerPosition = lastDividerPosition;
    }

    /** 
     * Get the 'tile_source' attribute value.
     * 
     * @return value
     */
    public String getTileSource() {
        return tileSource;
    }

    /** 
     * Set the 'tile_source' attribute value.
     * 
     * @param tileSource
     */
    public void setTileSource(String tileSource) {
        this.tileSource = tileSource;
    }

    /** 
     * Get the 'showMapMarker' attribute value.
     * 
     * @return value
     */
    public Boolean getShowMapMarker() {
        return showMapMarker;
    }

    /** 
     * Set the 'showMapMarker' attribute value.
     * 
     * @param showMapMarker
     */
    public void setShowMapMarker(Boolean showMapMarker) {
        this.showMapMarker = showMapMarker;
    }

    /** 
     * Get the 'tileGridVisible' attribute value.
     * 
     * @return value
     */
    public Boolean getTileGridVisible() {
        return tileGridVisible;
    }

    /** 
     * Set the 'tileGridVisible' attribute value.
     * 
     * @param tileGridVisible
     */
    public void setTileGridVisible(Boolean tileGridVisible) {
        this.tileGridVisible = tileGridVisible;
    }

    /** 
     * Get the 'zoomControlsVisible' attribute value.
     * 
     * @return value
     */
    public Boolean getZoomControlsVisible() {
        return zoomControlsVisible;
    }

    /** 
     * Set the 'zoomControlsVisible' attribute value.
     * 
     * @param zoomControlsVisible
     */
    public void setZoomControlsVisible(Boolean zoomControlsVisible) {
        this.zoomControlsVisible = zoomControlsVisible;
    }

    /** 
     * Get the 'searchControlVisible' attribute value.
     * 
     * @return value
     */
    public Boolean getSearchControlVisible() {
        return searchControlVisible;
    }

    /** 
     * Set the 'searchControlVisible' attribute value.
     * 
     * @param searchControlVisible
     */
    public void setSearchControlVisible(Boolean searchControlVisible) {
        this.searchControlVisible = searchControlVisible;
    }

    /** 
     * Get the 'hideFoldedNodes' attribute value.
     * 
     * @return value
     */
    public Boolean getHideFoldedNodes() {
        return hideFoldedNodes;
    }

    /** 
     * Set the 'hideFoldedNodes' attribute value.
     * 
     * @param hideFoldedNodes
     */
    public void setHideFoldedNodes(Boolean hideFoldedNodes) {
        this.hideFoldedNodes = hideFoldedNodes;
    }

    /** 
     * Get the 'limitSearchToVisibleArea' attribute value.
     * 
     * @return value
     */
    public Boolean getLimitSearchToVisibleArea() {
        return limitSearchToVisibleArea;
    }

    /** 
     * Set the 'limitSearchToVisibleArea' attribute value.
     * 
     * @param limitSearchToVisibleArea
     */
    public void setLimitSearchToVisibleArea(Boolean limitSearchToVisibleArea) {
        this.limitSearchToVisibleArea = limitSearchToVisibleArea;
    }

    /** 
     * Get the 'map_location_storage_index' attribute value.
     * 
     * @return value
     */
    public Integer getMapLocationStorageIndex() {
        return mapLocationStorageIndex;
    }

    /** 
     * Set the 'map_location_storage_index' attribute value.
     * 
     * @param mapLocationStorageIndex
     */
    public void setMapLocationStorageIndex(Integer mapLocationStorageIndex) {
        this.mapLocationStorageIndex = mapLocationStorageIndex;
    }
}
