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
					<li><a href="/ui/shows">Shows</a></li>
					<li class="active"><a href="/ui/torrents">Torrents <span class="sr-only">(current)</span></a></li>
					<li><a href="/ui/files">Files</a></li>
					<li><a href="/ui/xbmc">XBMC</a></li>
				</ul>
				<form class="navbar-form navbar-right">
					<div class="input-group">
						<input type="text" id="torrentSearch" class="form-control" placeholder="Search for..." />
						<span class="input-group-btn">
							<button class="btn btn-success" id="searchButton" type="button" data-toggle="modal" data-target="#torrentModal">
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

	<div class="container" id="torrents">
	</div>
<#include "/scripts.ftl">
		<script>
function load() {
	$.ajax({
		type: "GET",
		url: "/torrents",
		accepts: "text/html",
		success: function (data, textStatus, jqXHR) {
			$('#torrents').html(data);
		},
		error: function (jqXHR, textStatus, errorThrown) {
			alert("failure");
		}
	});
}

load();

var refreshId = setInterval(load, 10000);
var selected;
var table = $('#torrentResults').DataTable({
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
table.on('select', function(e, dt, type, indexes) {
	if ( type === 'row' ) {
		selected = indexes;
	}
});

$(function () {
    $(document).on('click', '#searchButton', function(e) {
        var value = $('#torrentSearch').val();
		table.ajax.url("/torrents/search?s=" + value).load();
    });
	$(document).on('click', '.nav-tabs li a', function(e) {
	  e.preventDefault();
	  $(this).tab('show');
	});
	$(document).on('click', '.torrent', function(e) {
		e.stopPropagation();

    	$('.expanded').removeClass('expanded');
        $(this).addClass('expanded');

		var hash = $(this).data('hash');
		$.ajax({
			type: "GET",
			url: "/torrents/" + hash + "/files",
			accepts: "text/html",
			success: function(data, textStatus, jqXHR) {
				$("#filestable-" + hash + " tbody").html(data);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
		/*
        var showId = $(this).data('show');
        $.ajax({
            type: "GET",
            url: "/series/" + showId + "/episodes",
            accepts: "text/html",
         	success: function(data, textStatus, jqXHR) {
         		var selector = '#episodes-' + showId + ' tbody';
	         	$(selector).html(data);

				$(selector + ' tr').on('click', function(e) {
					e.stopPropagation();
			    	$('tr.active').removeClass('active');
			        $(this).addClass('active');

					var id = $(this).data('episode');
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

				$(selector + ' .search').on('click', function(e) {
					e.stopPropagation();
					$('#torrentModal').modal('show');

					var id = $(this).data('episode');
					table = $('#torrentResults').DataTable({
			        	"ajax": "/episodes/" + id + "/search",
						"scrollY": "200px",
						"scrollCollapse": true,
						"paging": false,
						"searching": false,
						"info": false,
						"columns": [
			        		{ "data": "title" },
			        		{ "data": "seeds" },
			        		{ "data": "leechers" }
			        	],
			        	"order": [[ 1, "desc" ]]
			        });
					$('#torrentResults tbody tr').on('click', function(e) {
				        var id = this.id;
				        var index = $.inArray(id, selected);
				 
			            selected = id;
				 
				        if ( $(this).hasClass('active') ) {
				            $(this).removeClass('active');
				        } else {
				            table.$('tr.active').removeClass('active');
				            $(this).addClass('active');
				        }
				    });
				});
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
		*/
	});
    $(document).on('click', '#addTorrent', function(e) {
    	var selectedData = table.row(selected).data();
        $.ajax({
            type: "POST",
            data: JSON.stringify(selectedData),
            url: "/torrents",
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
	$(document).on('click', '.torrent .remove', function(e) {
		e.stopPropagation();
	    
	    var hash = $(this).data('hash');
	    $.ajax({
	        type: "DELETE",
	        url: "/torrents/" + hash,
	     	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	});
	$(document).on('click', '.torrent .start', function(e) {
		e.stopPropagation();
	    
	    var hash = $(this).data('hash');
	    $.ajax({
	        type: "POST",
	        url: "/torrents/" + hash + "/start",
	     	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	});
	$(document).on('click', '.torrent .pause', function(e) {
		e.stopPropagation();
	    
	    var hash = $(this).data('hash');
	    $.ajax({
	        type: "POST",
	        url: "/torrents/" + hash + "/pause",
	     	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            alert("failure: " + textStatus + ", " + errorThrown);
	        }
	    });
	});
	$(document).on('click', '.torrent .stop', function(e) {
		e.stopPropagation();
	    
	    var hash = $(this).data('hash');
	    $.ajax({
	        type: "POST",
	        url: "/torrents/" + hash + "/stop",
	     	success: function(data, textStatus, jqXHR) {
	     		location.reload(true);
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
