function () {
	for (var i = 0; i < this.wordIds.length; i++) {
        emit(this.wordIds[i], 1);
    }
}