<#-- @ftlvariable name="" type="nz.co.wing.tvmanager.view.TorrentFilesView" -->
<#list files as file>
	<tr>
		<td>${file.name!""}</td>
		<td>${file.size}</td>
		<td>${file.priority}</td>
		<td>
			<#assign p=(file.downloaded / file.size) * 100 />
			<div class="progress">
				<div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="${p}" aria-valuemin="0" aria-valuemax="100" style="width: ${p}%;">
					${p?string["0.##"]}%
				</div>
			</div>
		</td>
	</tr>
</#list>
