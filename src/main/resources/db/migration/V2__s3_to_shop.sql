ALTER TABLE IF EXISTS shop
    ADD COLUMN s3_url varchar,
    ADD COLUMN s3_key varchar,
    ADD COLUMN s3_secret_key varchar,
    ADD COLUMN s3_bucket varchar;