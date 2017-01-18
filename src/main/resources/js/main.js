function zeroPad(n, length) {
	var s = n + "", needed = length - s.length;
	if (needed > 0) {
		s = (Math.pow(10, needed) + "").slice(1) + s;
	}
	return s;
};

function torrentPercent(hash, torrents) {
	var value = 0;
	if (torrents != null) {
		torrents.forEach(function(t) {
			if (t.hash == hash) {
				value = t.percentComplete;
			}
		});
	}
	return value;
};

function convertBannerItem(banner) {
	return {
		src : banner.url,
		alt : ''
	};
};

function formatSpeed(value) {
	return (value / 1024).toFixed(2) + ' kB/s';
}

function formatTime(data) {
	return zeroPad(Math.floor(data.eta / 3600), 2) + ":" + zeroPad(Math.floor((data.eta - (Math.floor(data.eta / 3600)) * 3600) / 60), 2) + ":" + zeroPad((data.eta - (Math.floor(data.eta / 60)) * 60), 2);
}

function getTreeFilename(tree, selectedNodes) {
	var filename = "";
	if (selectedNodes.length > 0) {
		var n = selectedNodes[0];
		while (n.text != "torrents" && n.text != "/" && n.text != "storage") {
			filename = "/" + n.text + filename;
			n = tree.treeview('getParent', n);
		}
	}
	return filename;
}

function getFilename(fullname) {
	var i = fullname.lastIndexOf("\\");
	var filename = fullname.substring(i + 1);
	return filename;
}

function padDigits(number, digits) {
	return Array(Math.max(digits - String(number).length + 1, 0)).join(0) + number;
}

function ShowsModel() {
	var self = this;
	self.series = ko.observableArray([]);
	self.showName = ko.observable();
	self.tvShows = ko.observableArray([]);
	self.chosenShow = ko.observable();
	self.episodes = ko.observableArray([]);
	self.torrents = ko.observableArray([]);
	self.tvModalVisible = ko.observable(false);
	self.chosenEpisode = ko.observable();
	self.episode = ko.observable();
	self.banners = ko.observableArray([]);
	
	self.getSeries = function() {
		$.getJSON("/series", function(data) {
			self.series(data);
		});
	};
	self.refreshShow = function(data, event) {
		event.stopPropagation();
		var id = data.id;
		$.post("/series/" + id + "/update", function() {
			self.getSeries();
		});
	};
	self.removeShow = function(data, event) {
		event.stopPropagation();
		var id = data.id;
		$.ajax({
			method : "DELETE",
			url : "/series/" + id,
			dataType : "json"
		}).done(function() {
			self.getSeries();
		});
	};
	self.showTvModal = function() {
		self.tvModalVisible(true);
	};
	self.hideTvModal = function() {
		self.tvModalVisible(false);
	};
	self.findShow = function() {
		self.tvShows([]);
		self.showTvModal();
		$.getJSON("/series/search?s=" + self.showName(), function(data) {
			self.tvShows(data);
		});
	};
	self.addShow = function(data, event) {
		self.hideTvModal();
		$.ajax({
			method : "POST",
			data : JSON.stringify(data),
			url : "/series",
			contentType : "application/json",
			dataType : "json"
		}).done(function() {
			self.tvShows([]);
			self.getSeries();
		});
	};
	self.openShow = function(data) {
		location.hash = "Shows/" + data.id;
	};
	self.getSeriesData = function() {
		$.getJSON("/torrents", function(data) {
			self.torrents(data);
		});
		$.getJSON("/series/" + self.chosenShow() + "/episodes", function(data) {
			self.episodes(data);
		});
		$.getJSON("/series/" + self.chosenShow() + "/banners", function(data) {
			var banners = new Array();
			data.seriesList.forEach(function(b) {
				if (b.bannerType == "SERIES") {
					banners.push(b);
				}
			});
			self.banners(banners);
		});
	};
	self.setEpisodeState = function(state, episode, event) {
		event.stopPropagation();
		$.ajax({
			method : "PUT",
			url : "/episodes/" + episode.id + "/status/" + state
		}).done(function() {
			self.shows.getSeriesData();
		});
	};
	self.findEpisode = function(episode, event) {
		event.stopPropagation();
		mainModel.torrents.foundTorrents([]);
		mainModel.torrents.showTorrentModal();
		self.chosenEpisode(episode.id);
		$.getJSON("/episodes/" + episode.id + "/search", function(data) {
			mainModel.torrents.foundTorrents(data);
		});
	};
	self.removeEpisode = function(episode, event) {
		event.stopPropagation();
		$.ajax({
			method : "DELETE",
			url : "/episodes/" + episode.id
		}).done(function() {
			self.getSeriesData();
		});
	};
	self.showEpisode = function(episode, event) {
		event.stopPropagation();
		location.hash = "Shows/" + episode.seriesId + "/" + episode.id;
	};
	self.getEpisodeData = function() {
		$.getJSON("/episodes/" + self.chosenEpisode(), function(data) {
			self.episode(data);
		});
	};
};

function TorrentsModel() {
	var self = this;
	self.torrents = ko.observableArray([]);
	self.torrentModalVisible = ko.observable(false);
	self.torrentName = ko.observable();
	self.chosenTorrent = ko.observable();
	self.torrentFiles = ko.observableArray([]);
	self.torrentInfo = ko.observable();
	self.foundTorrents = ko.observableArray([]);
	
	self.showTorrentModal = function() {
		self.torrentModalVisible(true);
	};
	self.hideTorrentModal = function() {
		self.torrentModalVisible(false);
	};
	self.addTorrent = function(torrent) {
		self.hideTorrentModal();
		if (mainModel.chosenSection() == 'Shows') {
			$.ajax({
				method : "POST",
				data : JSON.stringify(torrent),
				url : "/episodes/" + mainModel.shows.chosenEpisode() + "/torrent",
				contentType : "application/json",
				dataType : "json"
			}).done(function() {
				self.foundTorrents([]);
				mainModel.shows.getSeriesData();
			});
		} else if (mainModel.chosenSection() == "Torrents") {
			$.ajax({
				method : "POST",
				data : JSON.stringify(torrent),
				url : "/torrents/",
				contentType : "application/json",
				dataType : "json"
			}).done(function() {
				self.foundTorrents([]);
				//self.getTorrents();
			});
		}
	};
	self.findTorrent = function() {
		self.foundTorrents([]);
		self.showTorrentModal();
		$.getJSON("/torrents/search?s=" + self.torrentName(), function(data) {
			self.foundTorrents(data);
		});
	};
	self.getTorrents = function() {
		$.getJSON("/torrents", function(data) {
			self.torrents(data);
		});
	};
	self.openTorrent = function(data) {
		location.hash = "Torrents/" + data.hash;
	};
	self.getTorrentData = function() {
		$.getJSON("/torrents/" + self.chosenTorrent() + "/files", function(data) {
			self.torrentFiles(data);
		});
	};
	self.refreshTorrents = function() {
		self.getTorrents();
 		if (self.chosenTorrent() != null) {
 			self.getTorrentData();
 		}
	};
	self.removeTorrent = function(torrent, event) {
		event.stopPropagation();
		$.ajax({
			method : "DELETE",
			url : "/torrents/" + torrent.hash
		});
	};
	self.startTorrent = function(torrent, event) {
		event.stopPropagation();
		$.post("/torrents/" + torrent.hash + "/start");
	};
	self.pauseTorrent = function(torrent, event) {
		event.stopPropagation();
		$.post("/torrents/" + torrent.hash + "/pause");
	};
	self.stopTorrent = function(torrent, event) {
		event.stopPropagation();
		$.post("/torrents/" + torrent.hash + "/stop");
	};
};

function FilesModel() {
	var self = this;
	self.torrentDirFiles = new ko.treeview.viewmodel({
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	self.destinationDirFiles = new ko.treeview.viewmodel({
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	self.fileTasks = ko.observableArray([]);
	self.renameModalVisible = ko.observable(false);
	self.renamedFilename = ko.observable();
	self.originalTorrentFilename;
	self.originalDestinationFilename;
	
	self.getTorrentDirFiles = function() {
		$.getJSON("/files/torrent", function (data) {
			self.torrentDirFiles.data([data]);
		});
	};
	self.getDestinationDirFiles = function() {
		$.getJSON("/files/destination", function (data) {
			self.destinationDirFiles.data([data]);
		});
	};
	self.getFileTasks = function() {
		$.getJSON("/files/tasks", function (data) {
			self.fileTasks(data);
		});
	};
	self.showRenameModal = function() {
		self.renameModalVisible(true);
	};
	self.hideRenameModal = function() {
		self.renameModalVisible(false);
	};
	self.renameFile = function() {
		self.hideRenameModal();
		if (self.torrentDirFiles.selectedNodes().length > 0) {
			var selectedData = {
				newName: self.renamedFilename(),
				orgName: self.originalTorrentFilename
			};
			$.ajax({
				method: "POST",
				data: JSON.stringify(selectedData),
				url: "/files/torrent/rename",
				contentType: "application/json",
				dataType: "json"
			}).done(function() {
				self.getTorrentDirFiles();
			});
		} else if (self.destinationDirFiles.selectedNodes().length > 0) {
			var selectedData = {
				newName: self.renamedFilename(),
				orgName: self.originalDestinationFilename
			};
			$.ajax({
				method: "POST",
				data: JSON.stringify(selectedData),
				url: "/files/destination/rename",
				contentType: "application/json",
				dataType: "json"
			}).done(function() {
				self.getDestinationDirFiles();
			});
		}
	};
	self.deleteFile = function() {
		if (self.torrentDirFiles.selectedNodes().length > 0) {
			var selectedData = {
				orgName: self.originalTorrentFilename
			};
			$.ajax({
				method: "DELETE",
				data: JSON.stringify(selectedData),
				url: "/files/torrent",
				contentType: "application/json",
				dataType: "json"
			}).done(function() {
				self.getTorrentDirFiles();
			});
		} else if (self.destinationDirFiles.selectedNodes().length > 0) {
			var selectedData = {
				orgName: self.originalDestinationFilename
			};
			$.ajax({
				method: "DELETE",
				data: JSON.stringify(selectedData),
				url: "/files/destination",
				contentType: "application/json",
				dataType: "json"
			}).done(function() {
				self.getDestinationDirFiles();
			});
		}
	};
	self.moveFile = function() {
		var selectedData = {
			orgName: self.originalTorrentFilename,
			newName: self.originalDestinationFilename
		};
		$.ajax({
			method: "POST",
			data: JSON.stringify(selectedData),
			url: "/files/torrent/move/destination",
			contentType: "application/json",
			dataType: "json"
		});
	};
	self.setRenamedTorrentFilename = function(tree, selectedNodes) {
		var filename = getTreeFilename(tree, selectedNodes);
		self.renamedFilename(filename);
		self.originalTorrentFilename = filename;
	};
	self.setRenamedDestinationFilename = function(tree, selectedNodes) {
		var filename = getTreeFilename(tree, selectedNodes);
		self.renamedFilename(filename);
		self.originalDestinationFilename = filename;
	};
	self.torrentDirFiles.updateFunction = self.setRenamedTorrentFilename;
	self.destinationDirFiles.updateFunction = self.setRenamedDestinationFilename;
};

function XBMCModel() {
	var self = this;
	self.xbmcFiles = new ko.treeview.viewmodel({
		showBorder: false,
		onhoverColor: '#49515a',
		selectedBackColor: '#49515a'
	});
	self.playerProgress = ko.observable(0);
	self.time = ko.observable();
	self.totalTime = ko.observable();
	self.playerId = ko.observable(1);
	self.playlistProperties = ["runtime", "showtitle", "season", "title", "artist"];
	self.playerProperties = ["percentage", "time", "totaltime"];
	self.playlist = ko.observableArray([]);
	self.filename;
	
	self.getXBMCFiles = function() {
		$.getJSON("/xbmc/files", "", function (data) {
			self.xbmcFiles.data([data]);
		});
	};
	self.go = function(action, data, event) {
		if (action == "Previous") {
			self.sendPlayerAction('goto/previous');
		} else if (action == "Next") {
			self.sendPlayerAction('goto/next');
		}
	};
	self.sendPlayerAction = function(action) {
		self.sendAction("player/" + self.playerId() + "/" + action);
	};
	self.sendAction = function(action) {
		$.post("/xbmc/" + action);
	};
	self.playPause = function() {
		self.sendPlayerAction('playPause');
	};
	self.stop = function() {
		self.sendPlayerAction('stop');
	};
	self.volume = function(action, data) {
		action = action.toLowerCase();
		if (action != 'mute') {
			if (action != 'down') {
				action = 'setVolume/true';
			} else {
				action = 'setVolume/false';
			}
		}
		self.sendAction("application/" + action);
	};
	self.input = function(action, data) {
		self.sendAction("input/" + action.toLowerCase());
	};
	self.shutdown = function() {
		self.sendAction("system/shutdown");
	};
	self.update = function() {
		self.getPlayerProperties();
		self.getPlaylistItems();
	};
	self.getPlaylistItems = function() {
		$.post("/xbmc/playlist/getPlaylists", function(data) {
			//console.log(data);
			data.forEach(function(entry) {
				if (entry.playlistid = self.playerId()) {
					$.ajax({
						method: "POST",
						url: "/xbmc/playlist/" + self.playerId() + "/getItems",
						data: JSON.stringify(self.playlistProperties),
						accepts: "application/json",
				        contentType: "application/json",
				        dataType: "json"
					}).done(function(data) {
						//console.log(data);
						self.playlist(data.items);
					});
				}
			});
		});
	};
	self.getPlayerProperties = function() {
		$.post("/xbmc/player/getActivePlayers", function(data) {
			//console.log(data);
			data.forEach(function(entry) {
				if (entry.playerid = self.playerId()) {
					$.ajax({
						method: "POST",
						url: "/xbmc/player/" + self.playerId() + "/getProperties",
						data: JSON.stringify(self.playerProperties),
						accepts: "application/json",
				        contentType: "application/json",
				        dataType: "json"
					}).done(function(data, textStatus, jqXHR) {
						//console.log(data);
						self.playerProgress(data.percentage.toPrecision(2));
						self.time(padDigits(data.time.hours, 2) + ":" + padDigits(data.time.minutes, 2) + ":" + padDigits(data.time.seconds, 2));
						self.totalTime(padDigits(data.totaltime.hours, 2) + ":" + padDigits(data.totaltime.minutes, 2) + ":" + padDigits(data.totaltime.seconds, 2));
					});
				}
			});
		});
	};
	self.playFile = function() {
		alert(self.filename);
		$.post("/xbmc/player/open?file=" + self.filename);
	};
	self.setFilename = function(tree, selectedNodes) {
		self.filename = "/storage" + getTreeFilename(tree, selectedNodes);
	};
	self.xbmcFiles.updateFunction = self.setFilename;
};

function ManagerViewModel() {
	// Data
	var self = this;
	self.sections = [ "Shows", "Torrents", "Files", "XBMC" ];
	self.chosenSection = ko.observable();
	self.refreshId;
	self.shows = new ShowsModel();
	self.torrents = new TorrentsModel();
	self.files = new FilesModel();
	self.xbmc = new XBMCModel();

	// Behaviours
	self.goToSection = function(section) {
		location.hash = section
	};

	// Client-side routes
	Sammy(function() {
		this.get('#:section', function() {
			self.chosenSection(this.params.section);
			if (self.refreshId != null) {
				clearInterval(self.refreshId);
			}
			if (this.params.section == "Shows") {
				self.shows.chosenShow(null);
				self.shows.getSeries();
			} else if (this.params.section == "Torrents") {
				self.torrents.chosenTorrent(null);
				self.torrents.getTorrents();
				self.refreshId = setInterval(self.torrents.refreshTorrents, 10000);
			} else if (this.params.section == "Files") {
				self.files.getTorrentDirFiles();
				self.files.getDestinationDirFiles();
				self.refreshId = setInterval(self.files.getFileTasks, 10000);
			} else if (this.params.section == "XBMC") {
				self.xbmc.getXBMCFiles();
				self.refreshId = setInterval(self.xbmc.update, 1000);
			}
		});
		this.get("#Shows/:show", function() {
			self.chosenSection("Shows");
			if (self.refreshId != null) {
				clearInterval(self.refreshId);
			}
			if (self.shows.series().length == 0) {
				self.shows.getSeries();
			}
			if (this.params.show != null) {
				self.shows.chosenShow(this.params.show);
				self.shows.getSeriesData();
			}
		});
		this.get("#Shows/:show/:episode", function() {
			self.chosenSection("Shows");
			if (self.refreshId != null) {
				clearInterval(self.refreshId);
			}
			if (self.shows.series().length == 0) {
				self.shows.getSeries();
			}
			if (this.params.show != null) {
				self.shows.chosenShow(this.params.show);
				if (self.shows.episodes().length == 0) {
					self.shows.getSeriesData();
				}
				if (this.params.episode != null) {
					self.shows.chosenEpisode(this.params.episode);
					self.shows.getEpisodeData();
				}
			}
		});
		this.get("#Torrents/:torrent", function() {
			self.chosenSection("Torrents");
			if (self.refreshId != null) {
				clearInterval(self.refreshId);
			}
			if (self.torrents.torrents().length == 0) {
				self.torrents.getTorrents();
			}
			self.refreshId = setInterval(self.refreshTorrents, 10000);
			if (this.params.torrent != null) {
				self.torrents.chosenTorrent(this.params.torrent);
				self.torrents.getTorrentData();
			}
		});

		this.get('', function() {
			this.app.runRoute('get', '#Shows')
		});
	}).run();
};

ko.options.deferUpdates = true;
var mainModel = new ManagerViewModel();
ko.applyBindings(mainModel);