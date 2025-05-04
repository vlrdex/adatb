-- Táblák törlése fordított sorrendben (ha már léteznek)
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE FOGLALAS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE JEGYEK';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE JARATOK';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE SZALLODAK';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE REPULOGEP';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE JEGYKATEGORIA';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE BIZTOSITASOK';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE VAROS';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE MODELL';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE FELHASZNALOK';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

-- Táblák létrehozása
CREATE TABLE MODELL (
    modell VARCHAR2(100) PRIMARY KEY,
    nev VARCHAR2(100) NOT NULL,
    ulohelyek_szama INTEGER NOT NULL

);

CREATE TABLE VAROS (
    id INTEGER PRIMARY KEY,
    nev VARCHAR2(100) NOT NULL
);

CREATE TABLE BIZTOSITASOK (
    id INTEGER PRIMARY KEY,
    nev VARCHAR2(100) NOT NULL,
    ar INTEGER NOT NULL,
    leiras VARCHAR2(256)
);

CREATE TABLE JEGYKATEGORIA (
    id INTEGER PRIMARY KEY,
    nev VARCHAR2(100) NOT NULL,
    kedvezmeny INTEGER NOT NULL
);

CREATE TABLE FELHASZNALOK (
    email VARCHAR2(100) PRIMARY KEY,
    nev VARCHAR2(100) NOT NULL,
    jelszo VARCHAR2(100) NOT NULL,
    szuletesi_datum DATE NOT NULL,
    admin INTEGER NOT NULL
);

CREATE TABLE REPULOGEP (
    id INTEGER PRIMARY KEY,
    szolgaltato VARCHAR2(100) NOT NULL,
    modell VARCHAR2(100),
    CONSTRAINT fk_modell FOREIGN KEY (modell) REFERENCES MODELL (modell)
);

CREATE TABLE SZALLODAK (
    id INTEGER PRIMARY KEY,
    varos_id INTEGER NOT NULL,
    nev VARCHAR2(100) NOT NULL,
    leiras VARCHAR2(256),
    CONSTRAINT fk_varos FOREIGN KEY (varos_id) REFERENCES VAROS (id)
);

CREATE TABLE JARATOK (
    id INTEGER PRIMARY KEY,
    kiindulasi_hely INTEGER NOT NULL,
    kiindulasi_idopont DATE NOT NULL,
    erkezesi_hely INTEGER NOT NULL,
    erkezesi_idopont DATE NOT NULL,
    repulo_id INTEGER NOT NULL,
    ar INTEGER NOT NULL,
    CONSTRAINT fk_repulo FOREIGN KEY (repulo_id) REFERENCES REPULOGEP (id),
    CONSTRAINT fk_kiindulasi_hely FOREIGN KEY (kiindulasi_hely) REFERENCES VAROS (id),
    CONSTRAINT fk_erkezesi_hely FOREIGN KEY (erkezesi_hely) REFERENCES VAROS (id)
);

CREATE TABLE JEGYEK (
    jarat_id INTEGER NOT NULL,
    ulohely INTEGER NOT NULL,
    biztositas_id INTEGER,
    nev VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL,
    PRIMARY KEY (jarat_id, ulohely),
    CONSTRAINT fk_email FOREIGN KEY (email) REFERENCES FELHASZNALOK (email),
    CONSTRAINT fk_jarat_jegyek FOREIGN KEY (jarat_id) REFERENCES JARATOK (id),
    CONSTRAINT fk_biztositas FOREIGN KEY (biztositas_id) REFERENCES BIZTOSITASOK (id)
);

CREATE TABLE FOGLALAS (
    id INTEGER PRIMARY KEY,
    jarat_id INTEGER NOT NULL,
    jegykategoria_id INTEGER,
    ulohely INTEGER NOT NULL,
    CONSTRAINT fk_jarat_foglalas FOREIGN KEY (jarat_id) REFERENCES JARATOK (id),
    CONSTRAINT fk_jegykategoria FOREIGN KEY (jegykategoria_id) REFERENCES JEGYKATEGORIA (id),
    CONSTRAINT fk_jegy FOREIGN KEY (jarat_id, ulohely) REFERENCES JEGYEK (jarat_id, ulohely)
);

-- Adatok beszúrása
-- Futtassuk az INSERT utasításokat külön-külön, hogy jobban követhető legyen a hibák helye

INSERT INTO MODELL (modell, nev, ulohelyek_szama) VALUES ('Boeing_737', 'Boeing 737 Max', 180);
INSERT INTO MODELL (modell, nev, ulohelyek_szama) VALUES ('Airbus_A320', 'Airbus A320neo', 200);
INSERT INTO MODELL (modell, nev, ulohelyek_szama) VALUES ('Embraer_E190', 'Embraer E190', 150);
INSERT INTO MODELL (modell, nev, ulohelyek_szama) VALUES ('Boeing_787', 'Boeing 787 Dreamliner', 300);
INSERT INTO MODELL (modell, nev, ulohelyek_szama) VALUES ('Airbus_A380', 'Airbus A380', 500);

INSERT INTO VAROS (id, nev) VALUES (1, 'Budapest');
INSERT INTO VAROS (id, nev) VALUES (2, 'Berlin');
INSERT INTO VAROS (id, nev) VALUES (3, 'Paris');
INSERT INTO VAROS (id, nev) VALUES (4, 'London');
INSERT INTO VAROS (id, nev) VALUES (5, 'New York');
INSERT INTO VAROS (id, nev) VALUES (6, 'Tokyo');
INSERT INTO VAROS (id, nev) VALUES (7, 'Moscow');
INSERT INTO VAROS (id, nev) VALUES (8, 'Rome');
INSERT INTO VAROS (id, nev) VALUES (9, 'Madrid');
INSERT INTO VAROS (id, nev) VALUES (10, 'Vienna');
INSERT INTO VAROS (id, nev) VALUES (11, 'Amsterdam');
INSERT INTO VAROS (id, nev) VALUES (12, 'Prague');
INSERT INTO VAROS (id, nev) VALUES (13, 'Warsaw');
INSERT INTO VAROS (id, nev) VALUES (14, 'Copenhagen');
INSERT INTO VAROS (id, nev) VALUES (15, 'Brussels');
INSERT INTO VAROS (id, nev) VALUES (16, 'Stockholm');
INSERT INTO VAROS (id, nev) VALUES (17, 'Oslo');
INSERT INTO VAROS (id, nev) VALUES (18, 'Helsinki');
INSERT INTO VAROS (id, nev) VALUES (19, 'Lisbon');
INSERT INTO VAROS (id, nev) VALUES (20, 'Athens');
INSERT INTO VAROS (id, nev) VALUES (21, 'Istanbul');
INSERT INTO VAROS (id, nev) VALUES (22, 'Zurich');
INSERT INTO VAROS (id, nev) VALUES (23, 'Munich');
INSERT INTO VAROS (id, nev) VALUES (24, 'Frankfurt');
INSERT INTO VAROS (id, nev) VALUES (25, 'Barcelona');
INSERT INTO VAROS (id, nev) VALUES (26, 'Chicago');
INSERT INTO VAROS (id, nev) VALUES (27, 'Los Angeles');
INSERT INTO VAROS (id, nev) VALUES (28, 'Toronto');
INSERT INTO VAROS (id, nev) VALUES (29, 'Mexico City');
INSERT INTO VAROS (id, nev) VALUES (30, 'Buenos Aires');
INSERT INTO VAROS (id, nev) VALUES (31, 'São Paulo');
INSERT INTO VAROS (id, nev) VALUES (32, 'Shanghai');
INSERT INTO VAROS (id, nev) VALUES (33, 'Seoul');
INSERT INTO VAROS (id, nev) VALUES (34, 'Bangkok');
INSERT INTO VAROS (id, nev) VALUES (35, 'Sydney');


INSERT INTO BIZTOSITASOK (id, nev, ar, leiras) VALUES (1, 'Alapbiztosítás', 5000, 'Alap szintű utazási biztosítás');
INSERT INTO BIZTOSITASOK (id, nev, ar, leiras) VALUES (2, 'Teljeskörű biztosítás', 10000, 'Teljes körű utazási és egészségbiztosítás');
INSERT INTO BIZTOSITASOK (id, nev, ar, leiras) VALUES (3, 'Csomagbiztosítás', 3000, 'Külön csomagok biztosítása');
INSERT INTO BIZTOSITASOK (id, nev, ar, leiras) VALUES (4, 'Balesetbiztosítás', 7000, 'Baleseti biztosítás a repülőutakra');
INSERT INTO BIZTOSITASOK (id, nev, ar, leiras) VALUES (5, 'Személybiztosítás', 12000, 'Személyes balesetbiztosítás utazás közben');

INSERT INTO JEGYKATEGORIA (id, nev, kedvezmeny) VALUES (1, 'Economy Class', 20);
INSERT INTO JEGYKATEGORIA (id, nev, kedvezmeny) VALUES (2, 'Business Class', 15);
INSERT INTO JEGYKATEGORIA (id, nev, kedvezmeny) VALUES (3, 'First Class', 25);
INSERT INTO JEGYKATEGORIA (id, nev, kedvezmeny) VALUES (4, 'Premium Economy', 30);
INSERT INTO JEGYKATEGORIA (id, nev, kedvezmeny) VALUES (5, 'Economy Plus', 25);

INSERT INTO FELHASZNALOK (email, nev, jelszo, szuletesi_datum, admin) VALUES
('user1@example.com', 'Péter Kovács', '$2a$10$G8Pvwr5C5rnNjDgX7Qu4e.r4l0WAUqlxgCv04c3loyA3TvrObo6Ga', TO_DATE('1990-05-10', 'YYYY-MM-DD'), 1);
INSERT INTO FELHASZNALOK (email, nev, jelszo, szuletesi_datum, admin) VALUES
('user2@example.com', 'Anna Horváth', '$2a$10$G8Pvwr5C5rnNjDgX7Qu4e.r4l0WAUqlxgCv04c3loyA3TvrObo6Ga', TO_DATE('1985-11-20', 'YYYY-MM-DD'), 0);
INSERT INTO FELHASZNALOK (email, nev, jelszo, szuletesi_datum, admin) VALUES
('user3@example.com', 'János Kiss', '$2a$10$G8Pvwr5C5rnNjDgX7Qu4e.r4l0WAUqlxgCv04c3loyA3TvrObo6Ga', TO_DATE('1992-03-15', 'YYYY-MM-DD'), 1);
INSERT INTO FELHASZNALOK (email, nev, jelszo, szuletesi_datum, admin) VALUES
('user4@example.com', 'László Tóth', '$2a$10$G8Pvwr5C5rnNjDgX7Qu4e.r4l0WAUqlxgCv04c3loyA3TvrObo6Ga', TO_DATE('1995-07-25', 'YYYY-MM-DD'), 0);
INSERT INTO FELHASZNALOK (email, nev, jelszo, szuletesi_datum, admin) VALUES
('user5@example.com', 'Mária Nagy', '$2a$10$G8Pvwr5C5rnNjDgX7Qu4e.r4l0WAUqlxgCv04c3loyA3TvrObo6Ga', TO_DATE('1988-02-18', 'YYYY-MM-DD'), 1);

INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (1, 'Lufthansa', 'Boeing_737');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (2, 'Ryanair', 'Airbus_A320');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (3, 'Wizz Air', 'Embraer_E190');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (4, 'Emirates', 'Boeing_787');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (5, 'Qatar Airways', 'Airbus_A380');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (6, 'Lufthansa', 'Boeing_737');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (7, 'Ryanair', 'Airbus_A320');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (8, 'Wizz Air', 'Embraer_E190');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (9, 'Emirates', 'Boeing_787');
INSERT INTO REPULOGEP (id, szolgaltato, modell) VALUES
    (10, 'Qatar Airways', 'Airbus_A380');



INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
(1, 1, TO_DATE('2025-05-01 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 5000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
(2, 3, TO_DATE('2025-05-02 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-05-02 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 8000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
(3, 4, TO_DATE('2025-05-03 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-05-03 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 6000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
(4, 2, TO_DATE('2025-05-04 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-05-04 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
(5, 5, TO_DATE('2025-05-05 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-05-05 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 12000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (6, 1, TO_DATE('2025-05-06 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-06 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 5500);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (7, 3, TO_DATE('2025-05-07 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-05-07 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 8500);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (8, 4, TO_DATE('2025-05-08 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-05-08 12:30:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 6500);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (9, 2, TO_DATE('2025-05-09 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-05-09 16:30:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10500);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (10, 5, TO_DATE('2025-05-10 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-05-10 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 12500);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (11, 1, TO_DATE('2025-05-11 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-05-11 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 5700);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (12, 4, TO_DATE('2025-05-12 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-12 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 8000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (13, 3, TO_DATE('2025-05-13 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-05-13 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 6200);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (14, 1, TO_DATE('2025-05-14 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-14 19:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10200);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (15, 2, TO_DATE('2025-05-15 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-05-15 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 11000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (16, 5, TO_DATE('2025-05-16 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-05-16 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 9800);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (17, 4, TO_DATE('2025-05-17 13:30:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-17 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 5400);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (18, 1, TO_DATE('2025-05-18 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-05-18 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 6400);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (19, 2, TO_DATE('2025-05-19 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-05-19 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (20, 3, TO_DATE('2025-05-20 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-20 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 7500);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (21, 4, TO_DATE('2025-05-21 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-05-21 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 6700);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (22, 5, TO_DATE('2025-05-22 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-22 20:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10900);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (23, 3, TO_DATE('2025-05-23 11:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-05-23 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 12700);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (24, 2, TO_DATE('2025-05-24 12:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-05-24 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 8300);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (25, 1, TO_DATE('2025-05-25 08:30:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-05-25 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 6000);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (26, 5, TO_DATE('2025-05-26 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-05-26 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 9400);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (27, 4, TO_DATE('2025-05-27 11:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-05-27 13:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 11800);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (28, 3, TO_DATE('2025-05-28 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-05-28 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10300);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (29, 2, TO_DATE('2025-05-29 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-05-29 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 5600);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (30, 5, TO_DATE('2025-05-30 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-05-30 19:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 9900);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (31, 1, TO_DATE('2025-06-01 10:30:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-06-01 12:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 7700);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (32, 3, TO_DATE('2025-06-02 14:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-06-02 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 8900);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (33, 4, TO_DATE('2025-06-03 18:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-06-03 20:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 9600);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (34, 5, TO_DATE('2025-06-04 07:00:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-06-04 09:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 10800);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (35, 2, TO_DATE('2025-06-05 11:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-06-05 13:30:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 8300);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (36, 1, TO_DATE('2025-06-06 12:30:00', 'YYYY-MM-DD HH24:MI:SS'), 4, TO_DATE('2025-06-06 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 8900);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (37, 3, TO_DATE('2025-06-07 13:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, TO_DATE('2025-06-07 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 7100);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (38, 4, TO_DATE('2025-06-08 14:30:00', 'YYYY-MM-DD HH24:MI:SS'), 5, TO_DATE('2025-06-08 16:30:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 8900);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (39, 5, TO_DATE('2025-06-09 15:30:00', 'YYYY-MM-DD HH24:MI:SS'), 2, TO_DATE('2025-06-09 17:30:00', 'YYYY-MM-DD HH24:MI:SS'), 4, 10600);
INSERT INTO JARATOK (id, kiindulasi_hely, kiindulasi_idopont, erkezesi_hely, erkezesi_idopont, repulo_id, ar) VALUES
    (40, 1, TO_DATE('2025-06-10 08:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, TO_DATE('2025-06-10 10:00:00', 'YYYY-MM-DD HH24:MI:SS'), 5, 9500);
-- 20 more entries can be generated similarly...


INSERT INTO JEGYEK (jarat_id, ulohely, biztositas_id, nev, email) VALUES
(1, 1, 1, 'Péter Kovács', 'user1@example.com');
INSERT INTO JEGYEK (jarat_id, ulohely, biztositas_id, nev, email) VALUES
(2, 5, 2, 'Anna Horváth', 'user2@example.com');
INSERT INTO JEGYEK (jarat_id, ulohely, biztositas_id, nev, email) VALUES
(3, 10, 3, 'János Kiss', 'user3@example.com');
INSERT INTO JEGYEK (jarat_id, ulohely, biztositas_id, nev, email) VALUES
(4, 2, 4, 'László Tóth', 'user4@example.com');
INSERT INTO JEGYEK (jarat_id, ulohely, biztositas_id, nev, email) VALUES
(5, 3, 5, 'Mária Nagy', 'user5@example.com');

INSERT INTO SZALLODAK (id, varos_id, nev, leiras) VALUES
(1, 1, 'Hotel Gellért', 'Luxus szálloda a Duna partján');
INSERT INTO SZALLODAK (id, varos_id, nev, leiras) VALUES
(2, 2, 'Berlin Palace', 'Történelmi szálloda Berlin központjában');
INSERT INTO SZALLODAK (id, varos_id, nev, leiras) VALUES
(3, 3, 'Le Meurice', '5 csillagos szálloda a Párizsi belvárosban');
INSERT INTO SZALLODAK (id, varos_id, nev, leiras) VALUES
(4, 4, 'The Ritz London', 'Elegáns szálloda a Buckingham Palace közelében');
INSERT INTO SZALLODAK (id, varos_id, nev, leiras) VALUES
(5, 5, 'The Plaza', 'Prémium szálloda New York szívében');

INSERT INTO FOGLALAS (id, jarat_id, jegykategoria_id, ulohely) VALUES
(1, 1, 2, 1);
INSERT INTO FOGLALAS (id, jarat_id, jegykategoria_id, ulohely) VALUES
(2, 2, 3, 5);
INSERT INTO FOGLALAS (id, jarat_id, jegykategoria_id, ulohely) VALUES
(3, 3, 1, 10);
INSERT INTO FOGLALAS (id, jarat_id, jegykategoria_id, ulohely) VALUES
(4, 4, 4, 2);
INSERT INTO FOGLALAS (id, jarat_id, jegykategoria_id, ulohely) VALUES
(5, 5, 5, 3);

COMMIT;

-- MODELL táblához tartozó triggerek
-- (ON UPDATE CASCADE a REPULOGEP táblához)
CREATE OR REPLACE TRIGGER modell_update_cascade
AFTER UPDATE OF modell ON MODELL
FOR EACH ROW
BEGIN
    UPDATE REPULOGEP
    SET modell = :NEW.modell
    WHERE modell = :OLD.modell;
END;
/

-- (ON DELETE SET NULL a REPULOGEP táblához)
CREATE OR REPLACE TRIGGER modell_delete_set_null
BEFORE DELETE ON MODELL
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count 
    FROM REPULOGEP R 
    WHERE R.modell = :OLD.modell;
    
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Nem törölhető a modell, mert repologep kapcsolódnak hozzá!');
    END IF;
END;
/

-- VAROS táblához tartozó triggerek
-- (ON UPDATE CASCADE a SZALLODAK táblához)
CREATE OR REPLACE TRIGGER varos_update_szallodak_cascade
AFTER UPDATE OF id ON VAROS
FOR EACH ROW
BEGIN
    UPDATE SZALLODAK
    SET varos_id = :NEW.id
    WHERE varos_id = :OLD.id;
END;
/

-- (ON DELETE CASCADE a SZALLODAK táblához)
CREATE OR REPLACE TRIGGER varos_delete_szallodak_cascade
BEFORE DELETE ON VAROS
FOR EACH ROW
BEGIN
    DELETE FROM SZALLODAK
    WHERE varos_id = :OLD.id;
END;
/

-- (ON UPDATE CASCADE a JARATOK táblához indulási helyszín esetén)
CREATE OR REPLACE TRIGGER varos_update_jaratok_indulas_cascade
AFTER UPDATE OF id ON VAROS
FOR EACH ROW
BEGIN
    UPDATE JARATOK
    SET kiindulasi_hely = :NEW.id
    WHERE kiindulasi_hely = :OLD.id;
END;
/

-- (ON UPDATE CASCADE a JARATOK táblához érkezési helyszín esetén)
CREATE OR REPLACE TRIGGER varos_update_jaratok_erkezes_cascade
AFTER UPDATE OF id ON VAROS
FOR EACH ROW
BEGIN
    UPDATE JARATOK
    SET erkezesi_hely = :NEW.id
    WHERE erkezesi_hely = :OLD.id;
END;
/

-- (ON DELETE RESTRICT a JARATOK táblához - megakadályozza a törlést, ha van kapcsolódó járat)
CREATE OR REPLACE TRIGGER varos_delete_jaratok_restrict
BEFORE DELETE ON VAROS
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count 
    FROM JARATOK 
    WHERE kiindulasi_hely = :OLD.id OR erkezesi_hely = :OLD.id;
    
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Nem törölhető a város, mert járatok kapcsolódnak hozzá!');
    END IF;
END;
/

-- REPULOGEP táblához tartozó trigger
-- (ON UPDATE CASCADE a JARATOK táblához)
CREATE OR REPLACE TRIGGER repulogep_update_jaratok_cascade
AFTER UPDATE OF id ON REPULOGEP
FOR EACH ROW
BEGIN
    UPDATE JARATOK
    SET repulo_id = :NEW.id
    WHERE repulo_id = :OLD.id;
END;
/

-- (ON DELETE RESTRICT a JARATOK táblához - megakadályozza a törlést, ha van kapcsolódó járat)
CREATE OR REPLACE TRIGGER repulogep_delete_jaratok_restrict
BEFORE DELETE ON REPULOGEP
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count 
    FROM JARATOK 
    WHERE repulo_id = :OLD.id;
    
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Nem törölhető a repülőgép, mert járatok kapcsolódnak hozzá!');
    END IF;
END;
/

-- BIZTOSITASOK táblához tartozó trigger
-- (ON UPDATE CASCADE a JEGYEK táblához)
CREATE OR REPLACE TRIGGER biztositasok_update_jegyek_cascade
AFTER UPDATE OF id ON BIZTOSITASOK
FOR EACH ROW
BEGIN
    UPDATE JEGYEK
    SET biztositas_id = :NEW.id
    WHERE biztositas_id = :OLD.id;
END;
/

-- (ON DELETE SET NULL a JEGYEK táblához)
CREATE OR REPLACE TRIGGER biztositasok_delete_jegyek_set_null
BEFORE DELETE ON BIZTOSITASOK
FOR EACH ROW
BEGIN
    UPDATE JEGYEK
    SET biztositas_id = NULL
    WHERE biztositas_id = :OLD.id;
END;
/

-- JARATOK táblához tartozó triggerek
-- (ON UPDATE CASCADE a JEGYEK táblához)
CREATE OR REPLACE TRIGGER jaratok_update_jegyek_cascade
AFTER UPDATE OF id ON JARATOK
FOR EACH ROW
BEGIN
    UPDATE JEGYEK
    SET jarat_id = :NEW.id
    WHERE jarat_id = :OLD.id;
    
    UPDATE FOGLALAS
    SET jarat_id = :NEW.id
    WHERE jarat_id = :OLD.id;
END;
/

-- (ON DELETE CASCADE a JEGYEK és FOGLALAS táblához)
CREATE OR REPLACE TRIGGER jaratok_delete_cascade
BEFORE DELETE ON JARATOK
FOR EACH ROW
BEGIN
    DELETE FROM JEGYEK
    WHERE jarat_id = :OLD.id;
    
    DELETE FROM FOGLALAS
    WHERE jarat_id = :OLD.id;
END;
/

-- JEGYKATEGORIA táblához tartozó triggerek
-- (ON UPDATE CASCADE a FOGLALAS táblához)
CREATE OR REPLACE TRIGGER jegykategoria_update_foglalas_cascade
AFTER UPDATE OF id ON JEGYKATEGORIA
FOR EACH ROW
BEGIN
    UPDATE FOGLALAS
    SET jegykategoria_id = :NEW.id
    WHERE jegykategoria_id = :OLD.id;
END;
/

-- (ON DELETE SET NULL a FOGLALAS táblához)
CREATE OR REPLACE TRIGGER jegykategoria_delete_foglalas_set_null
BEFORE DELETE ON JEGYKATEGORIA
FOR EACH ROW
BEGIN
    UPDATE FOGLALAS
    SET jegykategoria_id = NULL
    WHERE jegykategoria_id = :OLD.id;
END;
/

-- JEGYEK táblához tartozó triggerek
-- (ON UPDATE CASCADE a FOGLALAS táblához)
CREATE OR REPLACE TRIGGER jegyek_update_foglalas_cascade
AFTER UPDATE OF jarat_id, ulohely ON JEGYEK
FOR EACH ROW
BEGIN
    UPDATE FOGLALAS
    SET jarat_id = :NEW.jarat_id, ulohely = :NEW.ulohely
    WHERE jarat_id = :OLD.jarat_id AND ulohely = :OLD.ulohely;
END;
/

-- (ON DELETE CASCADE a FOGLALAS táblához)
CREATE OR REPLACE TRIGGER jegyek_delete_foglalas_set_null
BEFORE DELETE ON JEGYEK
FOR EACH ROW
BEGIN
    DELETE FROM FOGLALAS
    WHERE jarat_id = :OLD.jarat_id AND ulohely = :OLD.ulohely;
END;
/

-- (ON UPDATE CASCADE a JEGYEK táblához)
CREATE OR REPLACE TRIGGER felhasznalok_update_jegyek_cascade
AFTER UPDATE OF email ON FELHASZNALOK
FOR EACH ROW
BEGIN
    UPDATE JEGYEK
    SET email = :NEW.email
    WHERE email = :OLD.email;
END;
/

-- (ON DELETE CASCADE a JEGYEK és FOGLALAS táblához)
CREATE OR REPLACE TRIGGER felhasznalok_delete_cascade
BEFORE DELETE ON FELHASZNALOK
FOR EACH ROW
BEGIN
    DELETE FROM JEGYEK
    WHERE email = :OLD.email;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE varos_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/
CREATE SEQUENCE varos_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER varos_bi
BEFORE INSERT ON varos
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := varos_seq.NEXTVAL;
  END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE biztositasok_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE biztositasok_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER biztositasok_bi
BEFORE INSERT ON biztositasok
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := biztositasok_seq.NEXTVAL;
  END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE jegykategoria_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE jegykategoria_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER jegykategoria_bi
BEFORE INSERT ON jegykategoria
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := jegykategoria_seq.NEXTVAL;
  END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE repulogep_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE repulogep_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER repulogep_bi
BEFORE INSERT ON repulogep
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := repulogep_seq.NEXTVAL;
  END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE szallodak_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE szallodak_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER szallodak_bi
BEFORE INSERT ON szallodak
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := szallodak_seq.NEXTVAL;
  END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE jaratok_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE jaratok_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER jaratok_bi
BEFORE INSERT ON jaratok
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := jaratok_seq.NEXTVAL;
  END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE foglalas_seq';
EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE foglalas_seq START WITH 6 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER foglalas_bi
BEFORE INSERT ON foglalas
FOR EACH ROW
BEGIN
  IF :new.id IS NULL THEN
    :new.id := foglalas_seq.NEXTVAL;
  END IF;
END;
/
CREATE OR REPLACE TRIGGER check_price_limit
BEFORE INSERT OR UPDATE ON JARATOK
FOR EACH ROW
BEGIN
   IF :NEW.ar > 100000 THEN
      RAISE_APPLICATION_ERROR(-20001, 'Az ár túl magas!');
END IF;
END;
/

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';


CREATE OR REPLACE PROCEDURE kereses(nap IN VARCHAR2, ki_hely IN NUMBER, be_hely IN NUMBER ,p_cursor OUT SYS_REFCURSOR) AS
    cnt NUMBER;
BEGIN
    SELECT COUNT(*) INTO cnt 
    FROM JARATOK J 
    WHERE TRUNC(J.kiindulasi_idopont) = TRUNC(TO_DATE(nap,'YYYY-MM-DD HH24:MI:SS'))
        AND J.kiindulasi_hely = ki_hely 
        AND J.erkezesi_hely = be_hely;
        
    IF cnt=0 THEN
        OPEN p_cursor FOR SELECT J1.id As first,J2.id as second FROM JARATOK J1, JARATOK J2 
                WHERE J1.erkezesi_hely=J2.kiindulasi_hely AND TRUNC(J1.kiindulasi_idopont)=TRUNC(J2.kiindulasi_idopont) 
                AND J1.erkezesi_idopont - TRUNC(J1.erkezesi_idopont) < J2.kiindulasi_idopont - TRUNC(J2.kiindulasi_idopont)
                 AND TRUNC(J1.kiindulasi_idopont) = TRUNC(TO_DATE(nap,'YYYY-MM-DD HH24:MI:SS')) AND J1.kiindulasi_hely=ki_hely AND J2.erkezesi_hely=be_hely;
    Else
        OPEN p_cursor FOR SELECT J.id As first,J.id as second FROM JARATOK J WHERE TRUNC(J.kiindulasi_idopont) = TRUNC(TO_DATE(nap,'YYYY-MM-DD HH24:MI:SS')) AND J.kiindulasi_hely=ki_hely AND J.erkezesi_hely=be_hely;
    END if;
        
END;
/

CREATE OR REPLACE PROCEDURE foglalas_statisztika (
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT J.email as email, COUNT(*) AS darab
        FROM JEGYEK J
        GROUP BY J.email;
END;
/

CREATE OR REPLACE TRIGGER foglalt_ulohely
BEFORE INSERT
ON FOGLALAS
FOR EACH ROW
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM FOGLALAS F
    WHERE JARAT_ID = :NEW.JARAT_ID
    AND F.ULOHELY = :NEW.ULOHELY;
    IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20005, 'Ez az ülőhely már foglalt erre a járatra!');
        END IF;
END;
/

CREATE OR REPLACE PROCEDURE legnepszerubb_utvonalak (
    p_limit IN NUMBER,
    p_cursor OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
        SELECT V1.NEV AS KI, V2.NEV AS BE, COUNT(*) AS EMBEREK
        FROM VAROS V1, VAROS V2, JARATOK, FOGLALAS
        WHERE V1.ID = JARATOK.KIINDULASI_HELY
          AND V2.ID = JARATOK.ERKEZESI_HELY
          AND FOGLALAS.JARAT_ID = JARATOK.ID
        GROUP BY V1.NEV, V2.NEV
        ORDER BY EMBEREK DESC
        FETCH FIRST p_limit ROWS ONLY;
END;
/

CREATE OR REPLACE TRIGGER ellenoriz_szuletesi_datum
BEFORE INSERT OR UPDATE ON FELHASZNALOK
FOR EACH ROW
BEGIN
   IF :NEW.szuletesi_datum > TRUNC(SYSDATE) THEN
      RAISE_APPLICATION_ERROR(-20010, 'A születési dátum nem lehet jövőbeli!');
   END IF;
END;



