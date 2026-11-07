PRAGMA foreign_keys = ON;

CREATE TABLE users (
	user_id INTEGER PRIMARY KEY,
	full_name TEXT NOT NULL
		CHECK(length(trim(full_name)) > 0),
	email TEXT NOT NULL COLLATE NOCASE UNIQUE
		CHECK(length(trim(email)) > 0),
	age INTEGER NOT NULL
		CHECK (age>0),
	role TEXT NOT NULL
		CHECK(role IN ('ADMIN', 'PARTICIPANT'))
);
CREATE TABLE credentials (
    user_id INTEGER PRIMARY KEY,
    password_hash TEXT NOT NULL
        CHECK (length(trim(password_hash)) > 0),

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
);

CREATE TABLE conferences (
    conference_id INTEGER PRIMARY KEY,
    title TEXT NOT NULL
        CHECK (length(trim(title)) > 0),
    description TEXT,
    capacity INTEGER NOT NULL
        CHECK (capacity > 0),
    start_datetime TEXT NOT NULL,
    end_datetime TEXT NOT NULL,
    location TEXT,

    CHECK (end_datetime > start_datetime)
);

CREATE TABLE registrations (
    registration_id INTEGER PRIMARY KEY,
    user_id INTEGER NOT NULL,
    conference_id INTEGER NOT NULL,
    status TEXT NOT NULL
        CHECK (status IN ('CONFIRMED', 'WAITLISTED')),
    wait_position INTEGER,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    FOREIGN KEY (conference_id)
        REFERENCES conferences(conference_id)
        ON DELETE RESTRICT,

    UNIQUE (user_id, conference_id),
    UNIQUE (conference_id, wait_position),

    CHECK (
        (status = 'CONFIRMED' AND wait_position IS NULL)
        OR
        (status = 'WAITLISTED'
         AND wait_position IS NOT NULL
         AND wait_position > 0)
    )
);