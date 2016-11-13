<#-- @ftlvariable name="" type="nz.co.wing.tvmanager.view.EpisodeListView" -->
<#list episodes as episode>
	<tr data-episode="${episode.id}" data-show="${episode.seriesId}">
		<td class="col-xs-2">s${episode.seasonNumber?string["00"]}e${episode.episodeNumber?string["00"]}</td>
		<td class="col-xs-3">${episode.episodeName}</td>
		<td class="col-xs-2">${episode.firstAired}</td>
		<td class="col-xs-2">
			<#assign state="${episode.state!''}" />
			<#if state == "DOWNLOADING">
				<div class="progress">
					<div class="progress-bar" role="progressbar" aria-valuenow="${torrentMap[episode.hash!""]!0}" aria-valuemin="0" aria-valuemax="100" style="width: ${torrentMap[episode.hash!""]!0}%;">
						${torrentMap[episode.hash!""]!0}%
					</div>
				</div>
			<#else />
				${state}
			</#if>
		</td>
		<td class="col-xs-3">
			<div class="btn-group" role="group">
				<button class="btn btn-default">
					<span class="glyphicon glyphicon-film"></span>
				</button>
				<button class="btn btn-default">
					<span class="glyphicon glyphicon-duplicate"></span>
				</button>
				<button class="btn btn-default search" data-episode="${episode.id}" type="button" data-toggle="modal" data-target="#torrentModal">
					<span class="glyphicon glyphicon-search"></span>
				</button>
				<button class="btn btn-warning removeEpisode" data-episode="${episode.id}">
					<span class="glyphicon glyphicon-trash"></span>
				</button>
			</div>
		</td>
	</tr>
</#list>
