<!DOCTYPE html>
<html lang="en">
<#assign title="TV Manager" />
<#include "/head.ftl">
	<body>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">${title}</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li class="active"><a href="/ui/shows">Shows <span class="sr-only">(current)</span></a></li>
					<li><a href="/ui/torrents">Torrents</a></li>
					<li><a href="/ui/files">Files</a></li>
					<li><a href="/ui/xbmc">XBMC</a></li>
				</ul>
				<form class="navbar-form navbar-right">
					<div class="input-group">
						<input type="text" id="tvSearch" class="form-control" placeholder="Search for..." />
						<span class="input-group-btn">
							<button class="btn btn-success" id="searchButton" type="button" data-toggle="modal" data-target="#tvModal">
								<span class="glyphicon glyphicon-plus"></span>
							</button>
						</span>
					</div>
					<!-- /input-group -->
				</form>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<!-- Search Show Modal -->
	<div class="modal fade" id="tvModal" tabindex="-1" role="dialog" aria-labelledby="tvModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="tvModalLabel">Add Tv Show</h4>
				</div>
				<div class="modal-body">
					<table class="table table-hover table-responsive" id="showResults">
						<thead>
							<tr>
								<th>Name</th>
								<th>Status</th>
								<th>Genres</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button id="addShow" type="button" class="btn btn-primary">Add</button>
				</div>
			</div>
		</div>
	</div>

	<!-- Search Torrent Modal -->
	<div class="modal fade" id="torrentModal" tabindex="-1" role="dialog" aria-labelledby="torrentModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="torrentModalLabel">Find Torrent</h4>
				</div>
				<div class="modal-body">
					<table class="table table-hover table-responsive" id="torrentResults">
						<thead>
							<tr>
								<th>Title</th>
								<th>Seeds</th>
								<th>Leachers</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button id="addTorrent" type="button" class="btn btn-primary">Add</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container" id="tvShows">
	</div>
<#include "/scripts.ftl">
		<script>
var torrentTable = $('#torrentResults').DataTable({
	"scrollY": "200px",
	"scrollCollapse": true,
	"paging": false,
	"searching": false,
	"select": 'single',
	"info": false,
	"processing": true,
	"columns": [
		{ "data": "title" },
		{ "data": "seeds" },
		{ "data": "leechers" }
	],
	"order": [[ 1, "desc" ]]
});
torrentTable.on( 'select', function(e, dt, type, indexes) {
	if ( type === 'row' ) {
		selected = indexes;
	}
});

var selected;
var tvTable = $('#showResults').DataTable({
	"scrollY": "200px",
	"scrollCollapse": true,
	"paging": false,
	"searching": false,
	"info": false,
	"select": 'single',
	"processing": true,
	"columns": [
		{ "data": "seriesName" },
		{ "data": "status" },
		{ "data": "genres" }
	]
});
tvTable.on( 'select', function(e, dt, type, indexes) {
	if ( type === 'row' ) {
		selected = indexes;
	}
});

$(function() {
	$.ajax({
		type: "GET",
		url: "/series",
		accepts: "text/html",
		success: function (data, textStatus, jqXHR) {
			$('#tvShows').html(data);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			alert("failure");
		}
	});
	$(document).on('click', '.tvshow', function(e) {
		e.stopPropagation();
		$('.expanded').removeClass('expanded');
	    $(this).addClass('expanded');
	    
	    var showId = $(this).data('show');
	    $.ajax({
	        type: "GET",
	        url: "/series/" + showId + "/episodes",
	        accepts: "text/html",
	     	success: function(data, textStatus, jqXHR) {
	     		var selector = '#episodes-' + showId + ' tbody';
	         	$(selector).html(data);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	    $.ajax({
	        type: "GET",
	        url: "/series/" + showId + "/banners",
	        accepts: "text/html",
	     	success: function(data, textStatus, jqXHR) {
	     		var selector = '#carousel-' + showId + ' .carousel-inner';
	         	$(selector).html(data);
	     		$('#carousel-' + showId).carousel();
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	});
	$(document).on('click', '.tvshow .remove', function(e) {
		e.stopPropagation();

	    var showId = $(this).data('show');
	    $.ajax({
	        type: "DELETE",
	        url: "/series/" + showId,
	        accepts: "text/html",
	     	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	});
	$(document).on('click', '.tvshow .refresh', function(e) {
		e.stopPropagation();

	    var showId = $(this).data('show');
	    $.ajax({
	        type: "POST",
	        url: "/series/" + showId + "/update",
	     	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	});
	$(document).on('click', '.episodes table tr', function(e) {
		e.stopPropagation();
    	$('tr.active').removeClass('active');
        $(this).addClass('active');

		var id = $(this).data('episode');
		var showId = $(this).data('show');
        $.ajax({
            type: "GET",
            url: "/episodes/" + id,
            accepts: "text/html",
         	success: function(data, textStatus, jqXHR) {
         		var selector = '#episode-detail-' + showId;
	         	$(selector).html(data);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("failure: " + textStatus + ", " + errorThrown);
            }
        });
	});
	$(document).on('click', '.episodes table tr .search', function(e) {
		e.stopPropagation();
		$('#torrentModal').modal('show');

		var id = $(this).data('episode');
		torrentTable.ajax.url("/episodes/" + id + "/search").load();
	});
	$(document).on('click', '.episodes table tr .removeEpisode', function(e) {
		e.stopPropagation();
		var id = $(this).data('episode');
        $.ajax({
            type: "DELETE",
            url: "/episodes/" + id,
         	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("failure: " + textStatus + ", " + errorThrown);
            }
        });
	});
    $(document).on('click', '#searchButton', function(e) {
        var value = $('#tvSearch').val();
		tvTable.ajax.url("/series/search?s=" + value).load();
    });
    $(document).on('click', '#addShow', function(e) {
    	var selectedData = tvTable.row(selected).data();
        $.ajax({
            type: "POST",
            data: JSON.stringify(selectedData),
            url: "/series",
            contentType: "application/json",
            dataType: "json",
         	success: function(data, textStatus, jqXHR) {
                location.reload(true);  
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("failure: " + textStatus + ", " + errorThrown);
            }
        });
    });
    $(document).on('click', '#addTorrent', function(e) {
    	var selectedData = torrentTable.row(selected).data();
    	var url = torrentTable.ajax.url();
    	url = url.replace("/search", "/torrent");
        $.ajax({
            type: "POST",
            data: JSON.stringify(selectedData),
            url: url,
            contentType: "application/json",
            dataType: "json",
         	success: function(data, textStatus, jqXHR) {
                $('#torrentModal').modal('hide');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                alert("failure: " + textStatus + ", " + errorThrown);
            }
        });
    });
});
		</script>
	</body>
</html>
