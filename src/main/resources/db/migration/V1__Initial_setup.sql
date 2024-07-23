CREATE TABLE phone (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       model VARCHAR(255) NOT NULL,
                       is_available BOOLEAN NOT NULL,
                       version INT NOT NULL
);

CREATE TABLE booking (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         phone_id BIGINT NOT NULL,
                         booked_by VARCHAR(255) NOT NULL,
                         booked_at TIMESTAMP NOT NULL,
                         CONSTRAINT fk_phone FOREIGN KEY (phone_id) REFERENCES phone(id)
);

INSERT INTO phone (model, is_available, version) VALUES ('Samsung Galaxy S9', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Samsung Galaxy S8', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Samsung Galaxy S8', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Motorola Nexus 6', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Oneplus 9', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Apple iPhone 13', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Apple iPhone 12', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Apple iPhone 11', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('iPhone X', TRUE, 0);
INSERT INTO phone (model, is_available, version) VALUES ('Nokia 3310', TRUE, 0);
