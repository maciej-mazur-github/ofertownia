INSERT INTO
    application_user(first_name, last_name, address, postal_code, city, email, password, terms_agreement)
VALUES
    -- jan@kowalski.com / asdf / admin
    ('Jan', 'Kowalski', 'Kowalska 1/2', '01-223', 'Warszawa', 'jan@kowalski.com', '{bcrypt}$2a$10$6jaUYwxZ0RjCxL3zmrOF7.WoD0GUGK7.5XBiw6m7WNwMtBCZwmGNu', true),
    -- piotr@nowak.com / qwerty / user
    ('Piotr', 'Nowak', 'Nowakowska, 3/4', '02-321', 'Gdańsk', 'piotr@nowak.com', '{argon2}$argon2id$v=19$m=16384,t=2,p=1$Pqtshtzvf++Y+n0FMQ6GTw$dVdgYrhK/UAratQ3hqYQQmFsLiv3/SSwsKRaFPObEVU', true),
    -- anna@wolska.com / zxcv / user
    ('Anna', 'Wolska', 'Prosta 34/5', '23-543', 'Kraków', 'anna@wolska.com', '{scrypt}$100801$+NTV04mCKTWRDEHWGdcnzg==$8mEdTwEoLFbui6720yAVARXOrNikHqLR15JCozzvNqE=', true),
    -- karol@kowal.com / mnbv / admin
    ('Karol', 'Kowal', 'Wilanowska 89/3', '43-123', 'Warszawa', 'karol@kowal.com', '{bcrypt}$2a$10$g0gM3lVPLG7Dp0GwVZEZ4ugcWHA9qc0Psel/LcFm.ZHR1DgYAzP4O', true),
    -- tomasz@frankowski.com / hjkl / super admin
    ('Tomasz', 'Frankowski', 'Zakopiańska 67/78', '00-234', 'Poznań', 'tomasz@frankowski.com', '{bcrypt}$2a$10$yYmg5ub2pK3fF1vxHpAvv.0PcTrsJBVL7ABrjOE6J5FNm9EtShs6G', true),
    -- katarzyna@moniuszko.com / hard / super admin
--     ('Katarzyna', 'Moniuszko', 'Żelazna 123/45', '20-222', 'Bytom', 'katarzyna@moniuszko.com', '{bcrypt}$2a$10$bSiG/sYNC.owiyOEz0AJjOeyd7xZui74IdUd.xlQfV6tfzNkrK6U.', true);
    ('Katarzyna', 'Moniuszko', 'Żelazna 123/45', '20-222', 'Bytom', 'offerservice@interia.com', '{bcrypt}$2a$10$bSiG/sYNC.owiyOEz0AJjOeyd7xZui74IdUd.xlQfV6tfzNkrK6U.', true);
