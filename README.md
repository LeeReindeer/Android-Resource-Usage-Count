# Android-Resource-Usage-Count-Color

Fork from [Android-Resource-Usage-Count](https://github.com/niorgai/Android-Resource-Usage-Count)

> 试图修复这个[issue](https://github.com/niorgai/Android-Resource-Usage-Count/issues/27)失败后的妥协品，将显示数字改为显示颜色来表示计数。

Auto count resource usage and show it in the left of each line.

Result Color
---
* 0 -> grey color
* 1 -> green color
* other -> red color

You configure colors in setting.

![](http://ojvnx00zs.bkt.clouddn.com/resource-color.png)

Use in Android Studio and IntelliJ IDEA.

If count not show, please edit/reopen it.

Tag to count
---

* `array`
* `attr`
* `bool`
* `color`
* `declare-styleable`
* `dimen`
* `drawable`
* `eat-comment`
* `fraction`
* `integer`
* `integer-array`
* `item`
* `plurals`
* `string`
* `string-array`
* `style`

Custom color in `Preferences` - `Other Settings` - `Android Resource Usage Count`

Path not count
---
* build/
* bin/
