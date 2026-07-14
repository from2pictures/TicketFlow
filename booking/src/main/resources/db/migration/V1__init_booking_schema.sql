-- Таблица бронирований
CREATE TABLE bookings (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL,
    event_id            UUID NOT NULL,

    quantity            INTEGER NOT NULL CHECK (quantity > 0),
    unit_price          DECIMAL(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price         DECIMAL(10, 2) NOT NULL CHECK (total_price >= 0),

    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_id          UUID,

    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at          TIMESTAMP WITH TIME ZONE,

    version             INTEGER DEFAULT 0 NOT NULL
);

-- Индексы для производительности
CREATE INDEX idx_bookings_user_id ON bookings(user_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_bookings_event_id ON bookings(event_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_created_at ON bookings(created_at);

-- Составной индекс для частых запросов
CREATE INDEX idx_bookings_user_status ON bookings(user_id, status);

-- Комментарий к таблице
COMMENT ON TABLE bookings IS 'Таблица бронирований билетов';
COMMENT ON COLUMN bookings.status IS 'Статусы: PENDING, CONFIRMED, PAID, CANCELLED, REFUNDED';


-- Outbox таблица для Transactional Outbox pattern
CREATE TABLE outbox_events (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id    UUID NOT NULL,
    aggregate_type  VARCHAR(50) NOT NULL,
    event_type      VARCHAR(100) NOT NULL,

    payload         JSONB NOT NULL,

    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    retry_count     INTEGER NOT NULL DEFAULT 0,
    last_error      TEXT,

    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    published_at    TIMESTAMP
);

-- Индексы для опроса фоновым процессом
CREATE INDEX idx_outbox_status_created
    ON outbox_events(status, created_at)
    WHERE status = 'PENDING';

CREATE INDEX idx_outbox_aggregate
    ON outbox_events(aggregate_id, aggregate_type);

COMMENT ON TABLE outbox_events IS 'Outbox для событий (Transactional Outbox Pattern)';