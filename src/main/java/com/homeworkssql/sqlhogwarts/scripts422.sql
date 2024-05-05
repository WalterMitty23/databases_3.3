CREATE TABLE Person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    has_license BOOLEAN NOT NULL
);

CREATE TABLE Car (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);


CREATE TABLE Person_Car (
    person_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES Person(id),
    FOREIGN KEY (car_id) REFERENCES Car(id)
);


INSERT INTO Person (name, age, has_license) VALUES ('John Doe', 30, true);
INSERT INTO Person (name, age, has_license) VALUES ('Jane Smith', 25, false);


INSERT INTO Car (brand, model, price) VALUES ('Toyota', 'Corolla', 20000.00);
INSERT INTO Car (brand, model, price) VALUES ('Honda', 'Civic', 22000.00);


INSERT INTO Person_Car (person_id, car_id) VALUES (1, 1);
INSERT INTO Person_Car (person_id, car_id) VALUES (1, 2);