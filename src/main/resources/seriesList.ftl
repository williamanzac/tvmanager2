<#-- @ftlvariable name="" type="nz.co.wing.tvmanager.view.SeriesListView" -->
<#list list as show>
		<div class="row tvshow" data-show="${show.id}" id="${show.seriesName}">
			<div class="pull-right actions btn-group" role="group">
				<button class="btn btn-default refresh" data-show="${show.id}">
					<span class="glyphicon glyphicon-refresh"></span>
				</button>
				<button class="btn btn-warning remove" data-show="${show.id}">
					<span class="glyphicon glyphicon-trash"></span>
				</button>
			</div>
			<header>
				<b>${show.seriesName}</b>
				<div id="carousel-${show.id}" class="carousel slide">
					<!-- Wrapper for slides -->
					<div class="carousel-inner" role="listbox">
					</div>
				</div>
			</header>
			<div class="showContent">
				<div class="col-md-9 episodes">
					<table class="table table-hover table-responsive" id="episodes-${show.id}">
						<tbody>
 						</tbody>
					</table>
				</div>
				<div class="col-md-3">
					<div class="panel panel-default detail">
						<div class="panel-heading">
							<h3 class="panel-title">Episode</h3>
						</div>
						<div class="panel-body" id="episode-detail-${show.id}">
						</div>
					</div>
				</div>
			</div>
		</div>
</#list>
