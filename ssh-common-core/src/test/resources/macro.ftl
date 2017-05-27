<#-- 在模板中定义变量 -->
<#assign name = 'Freemarker'>
<#-- 宏定义 -->
<#macro greet>
Hello ${name}!
</#macro>
<#-- 宏调用 -->
<@greet/>

<#function avg x y>
    <#return (x + y) / 2>
</#function>
${avg(10, 20)}