CREATE TABLE IF NOT EXISTS scv_data
(
    id           bigserial primary key,
    created_date timestamp default now(),
    file_name    varchar not null,
    row_number   integer not null check (row_number > 1),
    json_data    jsonb
);

CREATE OR REPLACE FUNCTION find_by_json_key(key varchar)
    RETURNS TABLE
            (
                file_name  varchar,
                row_number integer,
                json_value text
            )
AS
'
    BEGIN
        RETURN query
            SELECT scv_data.file_name,
                   scv_data.row_number,
                   scv_data.json_data::jsonb ->> key
            FROM scv_data
            WHERE json_data::jsonb ? key;
    END;' LANGUAGE plpgsql;