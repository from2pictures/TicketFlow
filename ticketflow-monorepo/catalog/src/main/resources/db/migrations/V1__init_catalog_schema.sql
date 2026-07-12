CREATE TABLE events (
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

title VARCHAR(255) NOT NULL,
description TEXT,
image_url VARCHAR(500),
genre VARCHAR(100),

venue_id UUID NOT NULL,
address VARCHAR(500),

start_time TIMESTAMP WITH TIME ZONE NOT NULL,
duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),

total_seats INTEGER NOT NULL CHECK (total_seats >= 0),
available_seats INTEGER NOT NULL CHECK (available_seats >= 0),

base_price DECIMAL(10,2) NOT NULL CHECK (base_price >= 0),

status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
CONSTRAINT chk_event_status CHECK (status IN ('DRAFT', 'PUBLISHED', 'CANCELLED', 'COMPLETED')),

created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
version INTEGER DEFAULT 0 NOT NULL,

deleted_at TIMESTAMP WITH TIME ZONE,

CONSTRAINT idx_events_venue FOREIGN KEY (venue_id) REFERENCES venues(id)
);

CREATE INDEX idx_events_status_start ON events(status, start_time);
CREATE INDEX idx_events_start_time ON events(start_time);
CREATE INDEX idx_events_genre_status ON events(genre, status) WHERE deleted_at IS NULL;