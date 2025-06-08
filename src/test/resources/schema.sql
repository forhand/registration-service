CREATE TABLE IF NOT EXISTS registration_verifications (
    registration_id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255),
    phone VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    registration_type VARCHAR(50) NOT NULL,
    verification_code VARCHAR(255),
    verification_token VARCHAR(255),
    expires_at TIMESTAMP NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
); 