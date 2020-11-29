CREATE TABLE word (
    word_id INTEGER PRIMARY KEY,
    word TEXT NOT NULL,
	description TEXT NOT NULL,
	first_letter TEXT NOT NULL,
	word_ref NUMERIC NOT NULL
);

CREATE INDEX idx_word_word ON word(word ASC);
CREATE INDEX idx_word_first_letter ON word(first_letter ASC);

CREATE TABLE daldic_metadata (
    data_version NUMERIC
);

CREATE TABLE android_metadata (
    locale TEXT
);
