CREATE TABLE appointment
(
    id         SERIAL,                                         -- the appointment's id
    doctor_id  BIGINT,                                         -- the doctor's id for whom the appointment is created
    date       TIMESTAMP   NOT NULL,                           -- the date of the appointment
    from_time  TIME        NOT NULL,                           -- the time of when the appointment will begin
    to_time    TIME        NOT NULL,                           -- the time of when the appointment will finish
    name       VARCHAR(60) NOT NULL,                           -- the name of the patient
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- the date when the appointment was created

    PRIMARY KEY (id)
);

CREATE TABLE doctor
(
    id         SERIAL,                                         -- the doctor's id
    start_time TIME        NOT NULL,                           -- the starting work hour
    end_time   TIME        NOT NULL,                           -- the ending work hour
    name       VARCHAR(60) NOT NULL,                           -- the name of the doctor
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- the date when the doctor was added
    version    BIGINT      NOT NULL,                           -- the version used for locking

    PRIMARY KEY (id)
);