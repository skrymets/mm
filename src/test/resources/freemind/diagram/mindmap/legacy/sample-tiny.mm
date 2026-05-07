<?xml version="1.0" encoding="UTF-8"?>
<map version="1.0.1">
  <node ID="ID_root" TEXT="Root">
    <node ID="ID_a" TEXT="Node A">
      <attribute NAME="priority" VALUE="high"/>
      <arrowlink DESTINATION="ID_b"/>
    </node>
    <node ID="ID_b" TEXT="Node B"/>
    <node ID="ID_c">
      <richcontent TYPE="NODE">
        <html>
          <body>
            <p>Rich <b>content</b> here</p>
          </body>
        </html>
      </richcontent>
    </node>
  </node>
</map>
