ALTER TABLE events DROP COLUMN IF EXISTS address;

CREATE INDEX IF NOT EXISTS idx_events_venue_id ON events(venue_id);
CREATE INDEX IF NOT EXISTS idx_venues_events ON venues(id) WHERE deleted_at IS NULL;