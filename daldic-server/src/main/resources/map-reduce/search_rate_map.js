function () {
	for (var i = 0; i < this.searchStrings.length; i++) {
        emit(this.searchStrings[i], 1);
    }
}