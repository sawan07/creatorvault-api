CREATE TABLE IF NOT EXISTS users
(
    id
    UUID
    PRIMARY
    KEY,
    email
    VARCHAR
(
    255
) NOT NULL,
    created_at TIMESTAMP DEFAULT now
(
)
    );

CREATE TABLE IF NOT EXISTS videos
(
    id
    UUID
    PRIMARY
    KEY,
    user_id
    UUID
    REFERENCES
    users
(
    id
),
    title VARCHAR
(
    255
),
    source_url TEXT NOT NULL,
    status VARCHAR
(
    50
),
    file_url TEXT,
    created_at TIMESTAMP DEFAULT now
(
)
    );

CREATE TABLE IF NOT EXISTS highlights
(
    id
    UUID
    PRIMARY
    KEY,
    video_id
    UUID
    REFERENCES
    videos
(
    id
),
    start_time DOUBLE PRECISION,
    end_time DOUBLE PRECISION,
    confidence DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT now
(
)
    );
