INSERT INTO users (user_id, full_name, email, age, role)
VALUES
    (1, 'Admin', 'admin@example.com', 30, 'ADMIN'),
    (2, 'Alice', 'alice@example.com', 20, 'PARTICIPANT');

INSERT INTO credentials (user_id, password_hash)
VALUES
    (1, '$2a$12$coHCRNXEEDdGZHNuLkqQneONoCj9u8VOixuR.7YkEY4cyFCCQabs.'),
    (2, '$2a$12$addQYGlxA5EivnGuw8YVR.qOJ4Y0TIeW8sOckL8j8uZ4QzrZ3w0SW');

INSERT INTO conferences (
    title, description, capacity,
    start_datetime, end_datetime, location
)
VALUES (
    'Java Conference', 'Introduction to Java', 20,
    '2027-03-10T09:00:00', '2027-03-10T12:00:00', 'Room 101'
);