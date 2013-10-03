/*
 This script scans an array of lines of text looking for content spec metadata elements
 */

var tags = null;

self.addEventListener('message', function (e) {
	if (e.data.tagDB) {
		var database = JSON.parse(e.data.tagDB);
		tags = [];

		for (var property in database) {
			if (database.hasOwnProperty(property)) {
				tags.push(property);
			}
		}
	}

	if (e.data.lines && tags != null) {
		var lines = e.data.lines;

		var retValue = [];
		for (var lineIndex = 0, linesLength = lines.length; lineIndex < linesLength; ++lineIndex) {

			var line = lines[lineIndex];
			var tagDetails = [];

			// Match anything that looks like metadata
			var metadataRe = /^(.+?)\s*=\s*/g;
			var metadataMatch = null;
			while (metadataMatch = metadataRe.exec(line)) {
				var tag = metadataMatch[1];

				if (tags.indexOf(tag) != -1) {
					var start = metadataMatch.index;
					var end = start + tag.length;
					tagDetails.push([start, end]);
				}
			}

			// match anything that looks like a container
			var containerRE = /^((\s{2})*)(\S+)\s*:/g;
			var containerMatch = null;
			while (containerMatch = containerRE.exec(line)) {
				var container = containerMatch[3];

				if (tags.indexOf(container) != -1) {
					var prefix = containerMatch[1] ? containerMatch[1].length : 0;
					var start = containerMatch.index + prefix;
					var end = start + container.length;
					tagDetails.push([start, end]);
				}
			}

			// match anything that looks like a element metadata piece
			var elementMetadataRE = /(\s*\[)(.+?)\s*:\s*/g;
			var elementMatch = null;
			while (elementMatch = elementMetadataRE.exec(line)) {
				var element = elementMatch[2];

				if (tags.indexOf(element) != -1) {
					var prefix = elementMatch[1].length;
					var start = elementMatch.index + prefix;
					var end = start + element.length;
					tagDetails.push([start, end]);
				}
			}

			retValue.push(tagDetails);
		}

		postMessage(retValue);
	}
});