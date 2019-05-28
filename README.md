
# common-utils

Common-Utils For Java Developers. 收集各种常用工具类.


请在 dev 分支下查看代码, master 完善中...




# 使用说明

## com.x4096.common.utils.captcha

验证码工具集使用示例

```$xslt
@RequestMapping(value = "/captcha")
public void captcha(HttpServletResponse response) throws IOException {
    Captcha jpgCaptcha = new JPGCaptcha(90, 40, 6, 20);
    jpgCaptcha.out(response.getOutputStream());
}
```

