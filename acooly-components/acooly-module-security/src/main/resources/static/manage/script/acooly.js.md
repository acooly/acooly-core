Acooly framework JS/CSS

# JS

## acooly.format.js

### 文件格式化
根据输入的byte大小，自动格式化为b,k,m,g等可读显示方式

```html
<th field="fileSize" formatter="fileSizeFormatter">文件大小</th>
```

```js
$.acooly.format.fileSize(101212);
```

<div>原始数据: <input type="text" id="fileSize" value="0" /> byte</div>
<div>格式化后: <span id="fileSize_format"></span></div>
<button class="btn btn-primary" type="button" onclick="$('#fileSize_format').html($.acooly.format.fileSize($('#fileSize').val()))">文件大小格式化</button>
