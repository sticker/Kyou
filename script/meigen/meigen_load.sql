LOAD DATA LOCAL INFILE 'meigen.csv' REPLACE
    INTO TABLE meigen
    FIELDS TERMINATED BY ','
    (`text`, `auther`)
    SET `created` = now(), `modified` = now()
;
