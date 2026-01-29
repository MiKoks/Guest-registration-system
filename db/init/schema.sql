DROP TABLE IF EXISTS registrations;
DROP TABLE IF EXISTS company_participants;
DROP TABLE IF EXISTS private_participants;
DROP TABLE IF EXISTS participants;
DROP TABLE IF EXISTS payment_methods;
DROP TABLE IF EXISTS events;

-- =========================
-- EVENTS
-- =========================
CREATE TABLE events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  event_time DATETIME NOT NULL,
  location VARCHAR(255) NOT NULL,
  additional_info VARCHAR(1000),
  CHECK (CHAR_LENGTH(additional_info) <= 1000)
);

-- =========================
-- PAYMENT METHODS
-- =========================
CREATE TABLE payment_methods (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL UNIQUE,
  display_name VARCHAR(100) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO payment_methods (code, display_name, active)
VALUES
  ('BANK_TRANSFER', 'Pangaülekanne', TRUE),
  ('CASH', 'Sularaha', TRUE);

-- =========================
-- PARTICIPANTS (base)
-- =========================
CREATE TABLE participants (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type ENUM('PRIVATE','COMPANY') NOT NULL
);

-- =========================
-- PRIVATE PARTICIPANT DETAILS
-- =========================
CREATE TABLE private_participants (
  participant_id BIGINT PRIMARY KEY,

  first_name VARCHAR(100) NOT NULL,
  last_name  VARCHAR(100) NOT NULL,
  personal_code CHAR(11) NOT NULL,

  additional_info VARCHAR(1500),

  CONSTRAINT fk_private_participant
    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE,

  CONSTRAINT uq_private_personal_code UNIQUE (personal_code),
  
  CHECK (personal_code REGEXP '^[0-9]{11}$'),
  CHECK (CHAR_LENGTH(additional_info) <= 1500)
);

-- =========================
-- COMPANY PARTICIPANT DETAILS
-- =========================
CREATE TABLE company_participants (
  participant_id BIGINT PRIMARY KEY,

  legal_name VARCHAR(255) NOT NULL,
  registry_code VARCHAR(50) NOT NULL,
  attendee_count INT NOT NULL,

  additional_info VARCHAR(5000),

  CONSTRAINT fk_company_participant
    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE CASCADE,

  CONSTRAINT uq_company_registry_code UNIQUE (registry_code),

  CHECK (attendee_count > 0),
  CHECK (CHAR_LENGTH(additional_info) <= 5000)
);

-- =========================
-- REGISTRATIONS (join event/participant/payment )
-- =========================
CREATE TABLE registrations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,

  event_id BIGINT NOT NULL,
  participant_id BIGINT NOT NULL,
  payment_method_id BIGINT NOT NULL,

  additional_info VARCHAR(5000),

  CONSTRAINT fk_reg_event
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,

  CONSTRAINT fk_reg_participant
    FOREIGN KEY (participant_id) REFERENCES participants(id) ON DELETE RESTRICT,

  CONSTRAINT fk_reg_payment
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE RESTRICT,

  CONSTRAINT uq_event_participant UNIQUE (event_id, participant_id),

  CHECK (CHAR_LENGTH(additional_info) <= 5000)
);

CREATE INDEX idx_events_time ON events(event_time);
CREATE INDEX idx_reg_event ON registrations(event_id);
CREATE INDEX idx_reg_participant ON registrations(participant_id);
