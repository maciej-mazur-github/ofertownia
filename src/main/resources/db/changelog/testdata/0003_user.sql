INSERT INTO
    application_user(first_name, last_name, email, password)
VALUES
    -- jan@kowalski.com / asdf / admin
    ('Jan', 'Kowalski', 'jan@kowalski.com', '{bcrypt}$2a$10$6jaUYwxZ0RjCxL3zmrOF7.WoD0GUGK7.5XBiw6m7WNwMtBCZwmGNu'),
    -- piotr@nowak.com / qwerty / superadmin
    ('Piotr', 'Nowak', 'piotr@nowak.com', '{argon2}$argon2id$v=19$m=16384,t=2,p=1$Pqtshtzvf++Y+n0FMQ6GTw$dVdgYrhK/UAratQ3hqYQQmFsLiv3/SSwsKRaFPObEVU'),
    -- anna@wolska.com / zxcv / superadmin
    ('Anna', 'Wolska', 'anna@wolska.com', '{scrypt}$100801$+NTV04mCKTWRDEHWGdcnzg==$8mEdTwEoLFbui6720yAVARXOrNikHqLR15JCozzvNqE='),
    -- karol@kowal.com / mnbv / admin
    ('Karol', 'Kowal', 'karol@kowal.com', '{bcrypt}$2a$10$g0gM3lVPLG7Dp0GwVZEZ4ugcWHA9qc0Psel/LcFm.ZHR1DgYAzP4O'),
    -- john@smith.com / hjkl / user
    ('John', 'Smith', 'john@smith.com', '{bcrypt}$2a$10$yYmg5ub2pK3fF1vxHpAvv.0PcTrsJBVL7ABrjOE6J5FNm9EtShs6G'),
    -- kate@moss.com / hard / user
    ('Kate', 'Moss', 'kate@moss.com', '{bcrypt}$2a$10$bSiG/sYNC.owiyOEz0AJjOeyd7xZui74IdUd.xlQfV6tfzNkrK6U.');
