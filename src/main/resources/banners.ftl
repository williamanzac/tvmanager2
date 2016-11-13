<#-- @ftlvariable name="" type="nz.co.wing.tvmanager.view.BannersView" -->
<#assign first="active" />
<#list banners.seriesList as banner>
	<#if banner.bannerType == "SERIES">
		<div class="item ${first}">
			<img class="img-responsive center-block" src="${banner.url}" />
		</div>
		<#assign first="" />
	</#if>
</#list>
