<!DOCTYPE html>
<html lang="en">
<#assign title="TV Manager" />
<#include "/head.ftl">
	<body>
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">${title}</a>
			</div>
			<div id="navbar" class="collapse navbar-collapse">
				<ul class="nav navbar-nav">
					<li><a href="/ui/shows">Shows</a></li>
					<li><a href="/ui/torrents">Torrents</a></li>
					<li><a href="/ui/files">Files</a></li>
					<li class="active"><a href="/ui/xbmc">XBMC <span class="sr-only">(current)</span></a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<div class="container" id="xbmc">
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<h3 class="panel-title">Remote Control</h3>
					</div>
					<div class="panel-body">
						<div class="btn-group" role="group">
							<button class="btn btn-default btn-player" data-id="1" data-action="goto.previous">
								<span class="glyphicon glyphicon-step-backward"></span>
							</button>
							<button class="btn btn-default btn-player" data-id="1">
								<span class="glyphicon glyphicon-backward"></span>
							</button>
							<button class="btn btn-default btn-player" data-id="1" data-action="playPause">
								<span class="glyphicon glyphicon-play"></span>
							</button>
							<button class="btn btn-default btn-player" data-id="1" data-action="playPause">
								<span class="glyphicon glyphicon-pause"></span>
							</button>
							<button class="btn btn-default btn-player" data-id="1" data-action="stop">
								<span class="glyphicon glyphicon-stop"></span>
							</button>
							<button class="btn btn-default btn-player" data-id="1">
								<span class="glyphicon glyphicon-forward"></span>
							</button>
							<button class="btn btn-default btn-player" data-id="1" data-action="goto.next">
								<span class="glyphicon glyphicon-step-forward"></span>
							</button>
						</div>
						<div class="btn-group" role="group">
							<button class="btn btn-default btn-input" data-action="application.mute">
								<span class="glyphicon glyphicon-volume-off"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="application.setVolume.false">
								<span class="glyphicon glyphicon-volume-down"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="application.setVolume.true">
								<span class="glyphicon glyphicon-volume-up"></span>
							</button>
						</div>
						<div class="btn-group" role="group">
							<button class="btn btn-default btn-input" data-action="input.left">
								<span class="glyphicon glyphicon-chevron-left"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="input.down">
								<span class="glyphicon glyphicon-chevron-down"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="input.up">
								<span class="glyphicon glyphicon-chevron-up"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="input.right">
								<span class="glyphicon glyphicon-chevron-right"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="input.select">
								<span class="glyphicon glyphicon-record"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="input.back">
								<span class="glyphicon glyphicon-share-alt"></span>
							</button>
							<button class="btn btn-default btn-input" data-action="input.home">
								<span class="glyphicon glyphicon-home"></span>
							</button>
							<button class="btn btn-warning btn-input" data-action="system.shutdown">
								<span class="glyphicon glyphicon-off"></span>
							</button>
						</div>
						<div>
							<span id="time">--:--:--</span> / <span id="totaltime">--:--:--</span>
						</div>
						<div class="progress">
							<div id="progress" class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-3">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<h3 class="panel-title">Playlist</h3>
					</div>
					<div class="panel-body">
						<ul id="playlist" class="nav nav-stacked">
						</ul>
					</div>
				</div>
			</div>
			<div class="col-xs-6">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<div class="pull-right btn-group" role="group">
							<button id="playFile" disabled="disabled" class="btn btn-default">
								<span class="glyphicon glyphicon-play"></span>
							</button>
						</div>
						<h3 class="panel-title">Files</h3>
					</div>
					<div id="filesTree">
					</div>
				</div>
			</div>
		</div>
	</div>
<#include "/scripts.ftl">
		<script>
var selectedNode;
$.getJSON("/xbmc/files", "", function (data) {
	var $destinationTree = $('#filesTree').treeview({
		data: [data],
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	$('#filesTree').on('nodeSelected', function(event, node) {
		selectedNode = node;
		if (selectedNode != null) {
			$("#playFile").prop('disabled', false);
		} else {
			$("#playFile").prop('disabled', true);
		}
	});
});

$(function () {
    $(document).on('click', '.btn-input', function(e) {
        var action = $(this).data("action").replace(/\./g, "/");
		$.ajax({
			type: "POST",
			url: "/xbmc/" + action,
			accepts: "application/json",
			success: function(data, textStatus, jqXHR) {
				//;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
    });
    $(document).on('click', '.btn-player', function(e) {
    	var id = $(this).data("id");
        var action = $(this).data("action").replace(".", "/");
		$.ajax({
			type: "POST",
			url: "/xbmc/player/" + id + "/" + action,
			accepts: "application/json",
			success: function(data, textStatus, jqXHR) {
				//;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
    });
    $(document).on('click', '#playFile', function(e) {
		var filename = "";
		var n = selectedNode;
		while (n.text != "storage") {
			filename = "/" + n.text + filename;
			n = $('#filesTree').treeview('getParent', n);
		}
		filename = "/storage" + filename;
		alert(filename);
		$.ajax({
			type: "POST",
			url: "/xbmc/player/open?file=" + filename,
			accepts: "application/json",
			success: function(data, textStatus, jqXHR) {
				//;
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
    });
});

function padDigits(number, digits) {
	return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}

function getPlayerProperties(playerId) {
	var properties = ["percentage", "time", "totaltime"];
	$.ajax({
		type: "POST",
		url: "/xbmc/player/getActivePlayers",
		accepts: "application/json",
		success: function(data, textStatus, jqXHR) {
			console.log(data);
			data.forEach(function(entry) {
				if (entry.playerid = playerId) {
					$.ajax({
						type: "POST",
						url: "/xbmc/player/" + playerId + "/getProperties",
						data: JSON.stringify(properties),
						accepts: "application/json",
				        contentType: "application/json",
				        dataType: "json",
						success: function(data, textStatus, jqXHR) {
							console.log(data);
							var percent = data.percentage.toPrecision(2);
							var time = padDigits(data.time.hours, 2) + ":" + padDigits(data.time.minutes, 2) + ":" + padDigits(data.time.seconds, 2);
							var totaltime = padDigits(data.totaltime.hours, 2) + ":" + padDigits(data.totaltime.minutes, 2) + ":" + padDigits(data.totaltime.seconds, 2);
							console.log(percent);
							console.log(time);
							console.log(totaltime);
							$("#progress").width(percent + "%");
							$("#time").text(time);
							$("#totaltime").text(totaltime);
						},
						error: function(jqXHR, textStatus, errorThrown) {
							alert("failure: " + textStatus + ", " + errorThrown);
						}
					});
				}
			});
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("failure: " + textStatus + ", " + errorThrown);
		}
	});
}

function getPlaylistItems(playlistId) {
	var properties = ["runtime", "showtitle", "season", "title", "artist"];
	$.ajax({
		type: "POST",
		url: "/xbmc/playlist/getPlaylists",
		accepts: "application/json",
		success: function(data, textStatus, jqXHR) {
			console.log(data);
			data.forEach(function(entry) {
				if (entry.playlistid = playlistId) {
					$.ajax({
						type: "POST",
						url: "/xbmc/playlist/" + playlistId + "/getItems",
						data: JSON.stringify(properties),
						accepts: "application/json",
				        contentType: "application/json",
				        dataType: "json",
						success: function(data, textStatus, jqXHR) {
							console.log(data);
							var $list = $("#playlist");
							var tmplt = "<li>{{name}}</li>";
							$list.html($.map(data.items, function(item) {
								return tmplt.replace(/{{name}}/, item.label );
							}).join(""));
						},
						error: function(jqXHR, textStatus, errorThrown) {
							alert("failure: " + textStatus + ", " + errorThrown);
						}
					});
				}
			});
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert("failure: " + textStatus + ", " + errorThrown);
		}
	});
}

function update() {
	getPlayerProperties(1);
	getPlaylistItems(1);
}
var refreshId = setInterval(update, 1000);
		</script>
	</body>
</html>
