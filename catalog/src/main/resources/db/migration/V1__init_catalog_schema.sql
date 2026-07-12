CREATE TABLE venues (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(255) NOT NULL,
    description TEXT,
    logo_url VARCHAR(500),

    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(500) NOT NULL,
    postal_code VARCHAR(20),

    latitude DECIMAL(10, 8) CHECK (latitude >= -90 AND latitude <= 90),
    longitude DECIMAL(11, 8) CHECK (longitude >= -180 AND longitude <= 180),

    venue_type VARCHAR(50) NOT NULL,
    CONSTRAINT chk_venue_type CHECK (venue_type IN ('STADIUM', 'ARENA', 'THEATER', 'CLUB', 'HALL', 'OUTDOOR', 'OTHER')),

    capacity INTEGER NOT NULL CHECK (capacity > 0),
    has_seating BOOLEAN DEFAULT FALSE,

    phone VARCHAR(20),
    email VARCHAR(255),
    website VARCHAR(500),

    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT chk_venue_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'UNDER_RENOVATION', 'CLOSED')),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version INTEGER DEFAULT 0 NOT NULL,

    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_venues_city_country ON venues(city, country) WHERE deleted_at IS NULL;
CREATE INDEX idx_venues_status ON venues(status) WHERE deleted_at IS NULL;
CREATE INDEX idx_venues_type ON venues(venue_type) WHERE deleted_at IS NULL;
CREATE INDEX idx_venues_name ON venues(name) WHERE deleted_at IS NULL;

CREATE INDEX idx_venues_coordinates ON venues(latitude, longitude) WHERE deleted_at IS NULL;

COMMENT ON TABLE venues IS 'Площадки для проведения концертов и мероприятий';
COMMENT ON COLUMN venues.venue_type IS 'Тип площадки: стадион, арена, театр, клуб и т.д.';
COMMENT ON COLUMN venues.capacity IS 'Максимальная вместимость площадки';
COMMENT ON COLUMN venues.has_seating IS 'Есть ли нумерованные места или только стоячие';

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