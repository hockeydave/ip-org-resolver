CREATE TABLE org_type (
                        id serial PRIMARY KEY,
                        name VARCHAR(255) UNIQUE);

CREATE TABLE org (
                        id serial PRIMARY KEY,
                        name VARCHAR(255) UNIQUE,
                        org_type_id INT REFERENCES org_type(id),
                        created_at TIMESTAMP without TIME ZONE DEFAULT now(),
                        updated_at TIMESTAMP without TIME ZONE DEFAULT now()
);

CREATE TABLE org_ip (
                          id serial PRIMARY KEY,
                          start_block_ip  NUMERIC(40,0),
                          end_block_ip NUMERIC(40,0),
                          org_id INT REFERENCES org(id),
                          created_at TIMESTAMP without TIME ZONE DEFAULT now(),
                          updated_at TIMESTAMP without time zone default now()
);

INSERT INTO org_type(name) values ('MOBILE_ISP');
INSERT INTO org_type(name) values ('RESIDENTIAL_ISP');
INSERT INTO org_type(name) values ('CLOUD');
INSERT INTO org_type(name) values ('CORPORATE');



