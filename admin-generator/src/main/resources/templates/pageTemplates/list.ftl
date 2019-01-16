<#macro mapperEl value>${r"#{"}${value}}</#macro>

<#macro gemHtml parameterName >
	<@compress single_line=true>
		${r"${"} ${parameterName} }
	</@compress> 
</#macro>

<#macro gemTH  name>
	<@compress single_line=true>
		${r"${item."}${name} }
	</@compress> 
</#macro>

<#macro notTran  name>
	<@compress single_line=true>
		${r"${"}${name} }
	</@compress> 
</#macro>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org" xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head th:include="include/css-head :: css-head">
</head>
<body>
<!-- Content Header (Page header) -->
<section class="content-header">
  <ol class="breadcrumb">
    <li><a href="#"><i class="fa fa-dashboard"></i> 系统配置</a></li>
    <li class="active">规则配置</li>
  </ol>
  <br/>
</section>
