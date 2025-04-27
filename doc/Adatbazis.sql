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

ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';
