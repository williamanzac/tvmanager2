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
					<li class="active"><a href="/ui/files">Files <span class="sr-only">(current)</span></a></li>
					<li><a href="/ui/xbmc">XBMC</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<!-- Rename Modal -->
	<div class="modal fade" id="renameModal" tabindex="-1" role="dialog" aria-labelledby="renameModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="renameModalLabel">Rename File</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<label for="filename" class="control-label">New Name:</label>
							<input type="text" class="form-control" id="filename">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					<button id="renameFile" type="button" class="btn btn-primary">Rename</button>
				</div>
			</div>
		</div>
	</div>

	<div class="container" id="files">
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<h3 class="panel-title">File Tasks</h3>
					</div>
					<table class="table table-hover table-responsive" id="fileTasks">
						<thead>
							<tr>
								<th>Source</th>
								<th>Target</th>
								<th>Size</th>
								<th>Percent</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<div class="pull-right btn-group" role="group">
							<button id="moveTorrent" disabled="disabled" class="btn btn-default move">
								<span class="glyphicon glyphicon-copy"></span>
							</button>
							<button id="renameTorrent" disabled="disabled" class="btn btn-default edit" data-toggle="modal" data-target="#renameModal">
								<span class="glyphicon glyphicon-pencil"></span>
							</button>
							<button id="removeTorrent" disabled="disabled" class="btn btn-warning remove">
								<span class="glyphicon glyphicon-trash"></span>
							</button>
						</div>
						<h3 class="panel-title">Torrent Directory</h3>
					</div>
					<div id="torrentTree">
					</div>
				</div>
			</div>
<!--
			<div class="col-xs-12 col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<div class="pull-right btn-group" role="group">
							<button id="moveIntermediate" disabled="disabled" class="btn btn-default move">
								<span class="glyphicon glyphicon-copy"></span>
							</button>
							<button id="renameIntermediate" disabled="disabled" class="btn btn-default edit" data-toggle="modal" data-target="#renameModal">
								<span class="glyphicon glyphicon-pencil"></span>
							</button>
							<button id="removeIntermediate" disabled="disabled" class="btn btn-warning remove">
								<span class="glyphicon glyphicon-trash"></span>
							</button>
						</div>
						<h3 class="panel-title">Intermediate Directory</h3>
					</div>
					<div id="intermediateTree">
					</div>
				</div>
			</div>
-->
			<div class="col-xs-12 col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading clearfix">
						<div class="pull-right btn-group" role="group">
							<button id="renameDestination" disabled="disabled" class="btn btn-default edit" data-toggle="modal" data-target="#renameModal">
								<span class="glyphicon glyphicon-pencil"></span>
							</button>
							<button id="removeDestination" disabled="disabled" class="btn btn-warning remove">
								<span class="glyphicon glyphicon-trash"></span>
							</button>
						</div>
						<h3 class="panel-title">Destination Directory</h3>
					</div>
					<div id="destinationTree">
					</div>
				</div>
			</div>
		</div>
	</div>
<#include "/scripts.ftl">
		<script>
var selectedTorrent;
var selectedIntermediate;
var selectedDestination;
$.getJSON("/files/torrent", "", function (data) {
	var $torrentTree = $('#torrentTree').treeview({
		data: [data],
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	$('#torrentTree').on('nodeSelected', function(event, node) {
		selectedTorrent = "";
		var n = node;
		while (n.text != "torrents") {
			selectedTorrent = "/" + n.text + selectedTorrent;
			n = $torrentTree.treeview('getParent', n);
		}
		$("#filename").val(selectedTorrent);
		//alert(selectedTorrent);
		if (selectedTorrent != '') {
			$("#renameTorrent").prop('disabled', false);
			$("#removeTorrent").prop('disabled', false);
			$("#moveTorrent").prop('disabled', false);
		} else {
			$("#renameTorrent").prop('disabled', true);
			$("#removeTorrent").prop('disabled', true);
			$("#moveTorrent").prop('disabled', true);
		}
	});
});
/*
$.getJSON("/files/intermediate", "", function (data) {
	var $intermediateTree = $('#intermediateTree').treeview({
		data: [data],
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	$('#intermediateTree').on('nodeSelected', function(event, node) {
		selectedIntermediate = "";
		var n = node;
		while (n.text != "ours") {
			selectedIntermediate = "/" + n.text + selectedIntermediate;
			n = $intermediateTree.treeview('getParent', n);
		}
		$("#filename").val(selectedIntermediate);
		//alert(selectedIntermediate);
		if (selectedTorrent != '') {
			$("#renameIntermediate").prop('disabled', false);
		} else {
			$("#renameIntermediate").prop('disabled', true);
		}
	});
});
*/
$.getJSON("/files/destination", "", function (data) {
	var $destinationTree = $('#destinationTree').treeview({
		data: [data],
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	$('#destinationTree').on('nodeSelected', function(event, node) {
		selectedDestination = "";
		var n = node;
		while (n.text != "/") {
			selectedDestination = "/" + n.text + selectedDestination;
			n = $destinationTree.treeview('getParent', n);
		}
		$("#filename").val(selectedDestination);
		//alert(selectedDestination);
		if (selectedDestination != '') {
			$("#renameDestination").prop('disabled', false);
		} else {
			$("#renameDestination").prop('disabled', true);
		}
	});
});

function getOrgName() {
	if (selectedTorrent != '' && !(selectedTorrent === undefined)) {
		return selectedTorrent;
	}
	if (selectedIntermediate != '' && !(selectedIntermediate === undefined)) {
		return selectedIntermediate;
	}
	if (selectedDestination != '' && !(selectedDestination === undefined)) {
		return selectedDestination;
	}
	return null;
}

function getNewName() {
	if (selectedDestination != '' && !(selectedDestination === undefined)) {
		return selectedDestination;
	}
	if (selectedIntermediate != '' && !(selectedIntermediate === undefined)) {
		return selectedIntermediate;
	}
	if (selectedTorrent != '' && !(selectedTorrent === undefined)) {
		return selectedTorrent;
	}
	return null;
}

function getRenameURL() {
	if (selectedTorrent != '' && !(selectedTorrent === undefined)) {
		return "/files/torrent/rename";
	}
	if (selectedIntermediate != '' && !(selectedIntermediate === undefined)) {
		return "/files/intermediate/rename";
	}
	if (selectedDestination != '' && !(selectedDestination === undefined)) {
		return "/files/destination/rename";
	}
	return null;
}

function getRemoveURL() {
	if (selectedTorrent != '' && !(selectedTorrent === undefined)) {
		return "/files/torrent";
	}
	if (selectedIntermediate != '' && !(selectedIntermediate === undefined)) {
		return "/files/intermediate";
	}
	if (selectedDestination != '' && !(selectedDestination === undefined)) {
		return "/files/destination";
	}
	return null;
}

function getMoveURL() {
	if (selectedTorrent != '' && !(selectedTorrent === undefined) && selectedIntermediate != '' && !(selectedIntermediate === undefined)) {
		return "/files/torrent/move/intermediate";
	}
	if (selectedTorrent != '' && !(selectedTorrent === undefined) && selectedDestination != '' && !(selectedDestination === undefined)) {
		return "/files/torrent/move/destination";
	}
	if (selectedIntermediate != '' && !(selectedIntermediate === undefined) && selectedDestination != '' && !(selectedDestination === undefined)) {
		return "/files/intermediate/move/destination";
	}
	return null;
}

$(function() {
	$(document).on('click', '#renameFile', function(e) {
		//alert(selectedTorrent);
		var orgName = getOrgName();
		var selectedData = {
			newName: $("#filename").val(),
			orgName: orgName
		};
		//alert(JSON.stringify(selectedData));
		$.ajax({
			type: "POST",
			data: JSON.stringify(selectedData),
			url: getRenameURL(),
			contentType: "application/json",
			dataType: "json",
			success: function(data, textStatus, jqXHR) {
				//$('#renameModal').modal('hide');
				location.reload(true);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
	});
	$(document).on('click', '#removeTorrent', function(e) {
		var orgName = getOrgName();
		var selectedData = {
			orgName: orgName
		};
		//alert(JSON.stringify(selectedData));
		$.ajax({
			type: "DELETE",
			data: JSON.stringify(selectedData),
			url: getRemoveURL(),
			contentType: "application/json",
			dataType: "json",
			success: function(data, textStatus, jqXHR) {
				//$('#renameModal').modal('hide');
				location.reload(true);
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
	});
	$(document).on('click', '#moveTorrent', function(e) {
		var orgName = getOrgName();
		var newName = getNewName();
		var selectedData = {
			orgName: orgName,
			newName: newName
		};
		//alert(JSON.stringify(selectedData));
		$.ajax({
			type: "POST",
			data: JSON.stringify(selectedData),
			url: getMoveURL(),
			contentType: "application/json",
			dataType: "json",
			success: function(data, textStatus, jqXHR) {
				//$('#renameModal').modal('hide');
				//location.reload(true);
				load();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert("failure: " + textStatus + ", " + errorThrown);
			}
		});
	});
});

var tasksTable = $('#fileTasks').DataTable({
	"paging": false,
	"searching": false,
	"info": false,
	"columns": [
		{ "data": "source" },
		{ "data": "target" },
		{ "data": "size" },
		{ "data": "percentComplete" }
	],
	"ajax": {
		"dataSrc":""
	}
});

function load() {
	tasksTable.ajax.url("/files/tasks").load();
}

load();

var refreshId = setInterval(load, 10000);
		</script>
	</body>
</html>
