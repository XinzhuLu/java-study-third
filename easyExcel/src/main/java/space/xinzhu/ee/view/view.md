#### 其他注解：

基本和读取时一致

- @ContentRowHeight()	标注在类上或属性上，指定内容行高

- @HeadRowHeight()  标注在类上或属性上，指定列头行高

- @ColumnWidth() 标注在类上或属性上，指定列宽

- ExcelIgnore` 默认所有字段都会写入excel，这个注解会忽略这个字段

- `DateTimeFormat` 日期转换，将`Date`写到excel会调用这个注解。里面的`value`参照`java.text.SimpleDateFormat`

- `NumberFormat` 数字转换，用`Number`写excel会调用这个注解。里面的`value`参照`java.text.DecimalFormat`

- `ExcelIgnoreUnannotated` 默认不加 `ExcelProperty` 的注解的都会参与读写，加了不会参与



### 7、写入时通用参数

`WriteWorkbook`、`WriteSheet`都会有的参数，如果为空，默认使用上级。

- `converter` 转换器，默认加载了很多转换器。也可以自定义。

- `writeHandler` 写的处理器。可以实现`WorkbookWriteHandler`,`SheetWriteHandler`,`RowWriteHandler`,`CellWriteHandler`，在写入excel的不同阶段会调用，对使用者透明不可见。

- `relativeHeadRowIndex` 距离多少行后开始。也就是开头空几行

- `needHead` 是否导出头

- `head` 与`clazz`二选一。写入文件的头列表，建议使用class。

- `clazz` 与`head`二选一。写入文件的头对应的class，也可以使用注解。

- `autoTrim`  字符串、表头等数据自动trim



### 8、WriteWorkbook（工作簿对象）参数

- `excelType` 当前excel的类型，默认为`xlsx`

- `outputStream` 与`file`二选一。写入文件的流

- `file` 与`outputStream`二选一。写入的文件

- `templateInputStream` 模板的文件流

- `templateFile` 模板文件

- `autoCloseStream` 自动关闭流。

- `password`  写的时候是否需要使用密码

- `useDefaultStyle` 写的时候是否是使用默认头



### 9、WriteSheet（工作表对象）参数

- `sheetNo` 需要写入的编号。默认0

- `sheetName` 需要些的Sheet名称，默认同sheetNo



