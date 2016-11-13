<#-- @ftlvariable name="" type="nz.co.wing.tvmanager.view.TorrentListView" -->
<#list list as torrent>
	<div class="row torrent" data-hash="${torrent.hash}">
		<#assign state="${torrent.state!''}" />
		<div class="pull-right actions btn-group" role="group">
			<#if state == "DOWNLOADING" || state == "SEEDING">
				<button class="btn btn-default start" data-hash="${torrent.hash}" disabled="disabled">
					<span class="glyphicon glyphicon-play"></span>
				</button>
			<#else>
				<button class="btn btn-default start" data-hash="${torrent.hash}">
					<span class="glyphicon glyphicon-play"></span>
				</button>
			</#if>
			<#if state == "DOWNLOADING_QUEUED" || state == "SEEDING_QUEUED">
				<button class="btn btn-default pause" data-hash="${torrent.hash}" disabled="disabled">
					<span class="glyphicon glyphicon-pause"></span>
				</button>
			<#else>
				<button class="btn btn-default pause" data-hash="${torrent.hash}">
					<span class="glyphicon glyphicon-pause"></span>
				</button>
			</#if>
			<button class="btn btn-default stop" data-hash="${torrent.hash}">
				<span class="glyphicon glyphicon-stop"></span>
			</button>
			<button class="btn btn-warning remove" data-hash="${torrent.hash}">
				<span class="glyphicon glyphicon-trash"></span>
			</button>
		</div>
		<header>
			<div class="col-md-4 torrentTitle">${torrent.title}</div>
			<div class="col-md-2">
				<#if state == "DOWNLOADING">
					<div class="progress">
						<div class="progress-bar" role="progressbar" aria-valuenow="${torrent.percentComplete}" aria-valuemin="0" aria-valuemax="100" style="width: ${torrent.percentComplete}%;">
							${torrent.percentComplete}%
						</div>
					</div>
				<#else>
					${state}
				</#if>
			</div>
			<div class="col-md-1">${(torrent.downSpeed / 1024)?string["0.##"]} kB/s</div>
			<div class="col-md-1">${(torrent.upSpeed / 1024)?string["0.##"]} kB/s</div>
			<div class="col-md-1">${(torrent.eta / 3600)?floor?string["00"]}:${((torrent.eta - ((torrent.eta / 3600)?floor) * 3600) / 60)?floor?string["00"]}:${(torrent.eta - ((torrent.eta / 60)?floor) * 60)?string["00"]}</div>
		</header>
		<div class="TorrentInfo">
			<div class="col-md-12 info">
				<ul class="nav nav-tabs">
					<li class="active"><a href="#files-${torrent.hash}" data-toggle="tab">Files</a></li>
					<li><a href="#info-${torrent.hash}" data-toggle="tab">Info</a></li>
				</ul>
				<div class="tab-content">
					<div class="tab-pane active" id="files-${torrent.hash}">
						<table class="table table-hover table-responsive" id="filestable-${torrent.hash}">
							<thead>
								<tr>
									<th>Path</th>
									<th>Size</th>
									<th>Done</th>
									<th>%</th>
								</tr>
							</thead>
							<tbody>
	 						</tbody>
						</table>
					</div>
					<div class="tab-pane" id="info-${torrent.hash}">
					</div>
				</div>
			</div>
		</div>
	</div>
</#list>
