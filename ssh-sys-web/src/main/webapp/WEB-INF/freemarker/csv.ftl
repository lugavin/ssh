用户Id,用户帐号,用户名,锁定标志,创建日期
<#list list as item>
${item.id},${item.code},${item.name},${item.locked},${(item.createDate?string("yyyy/MM/dd"))!}
</#list>
